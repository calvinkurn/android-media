package com.tokopedia.product.addedit.preview.presentation.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mediauploader.data.state.UploadResult
import com.tokopedia.mediauploader.domain.UploaderUseCase
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.common.constant.AddEditProductExtraConstant.Companion.IMAGE_SOURCE_ID
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_DESCRIPTION_INPUT
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_DETAIL_INPUT
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_SHIPMENT_INPUT
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_VARIANT_INPUT
import com.tokopedia.product.addedit.preview.domain.mapper.AddProductInputMapper
import com.tokopedia.product.addedit.preview.domain.usecase.ProductAddUseCase
import com.tokopedia.product.addedit.common.util.AddEditProductNotificationManager
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.preview.di.AddEditProductPreviewModule
import com.tokopedia.product.addedit.preview.di.DaggerAddEditProductPreviewComponent
import com.tokopedia.product.addedit.preview.presentation.activity.AddEditProductPreviewActivity
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

class AddEditProductUploadService : JobIntentService(), CoroutineScope {
    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var productAddUseCase: ProductAddUseCase
    @Inject
    lateinit var addProductInputMapper: AddProductInputMapper
    @Inject
    lateinit var uploaderUseCase: UploaderUseCase

    private var notificationManager: AddEditProductNotificationManager? = null
    private var shipmentInputModel: ShipmentInputModel = ShipmentInputModel()
    private var descriptionInputModel: DescriptionInputModel = DescriptionInputModel()
    private var detailInputModel: DetailInputModel = DetailInputModel()
    private var variantInputModel: ProductVariantInputModel = ProductVariantInputModel()

    companion object {
        private const val JOB_ID = 13131314

        fun startService(context: Context,
                         detailInputModel: DetailInputModel,
                         descriptionInputModel: DescriptionInputModel,
                         shipmentInputModel: ShipmentInputModel,
                         variantInputModel: ProductVariantInputModel) {
            val work = Intent(context, AddEditProductUploadService::class.java).apply {
                putExtra(EXTRA_DETAIL_INPUT, detailInputModel)
                putExtra(EXTRA_DESCRIPTION_INPUT, descriptionInputModel)
                putExtra(EXTRA_SHIPMENT_INPUT, shipmentInputModel)
                putExtra(EXTRA_VARIANT_INPUT, variantInputModel)
            }
            enqueueWork(context, AddEditProductUploadService::class.java, JOB_ID, work)
        }
    }

    override fun onCreate() {
        super.onCreate()
        initInjector()
    }

    private fun initInjector() {
        val baseMainApplication = applicationContext as BaseMainApplication
        DaggerAddEditProductPreviewComponent.builder()
                .addEditProductComponent(AddEditProductComponentBuilder.getComponent(baseMainApplication))
                .addEditProductPreviewModule(AddEditProductPreviewModule())
                .build()
                .inject(this)
    }

    override val coroutineContext: CoroutineContext by lazy {
        Dispatchers.IO
    }

    override fun onHandleWork(intent: Intent) {
        shipmentInputModel = intent.getParcelableExtra(EXTRA_SHIPMENT_INPUT)
        descriptionInputModel = intent.getParcelableExtra(EXTRA_DESCRIPTION_INPUT)
        detailInputModel = intent.getParcelableExtra(EXTRA_DETAIL_INPUT)
        variantInputModel = intent.getParcelableExtra(EXTRA_VARIANT_INPUT)
        uploadProductImages(detailInputModel.imageUrlOrPathList,
                variantInputModel.productSizeChart?.filePath ?: "")
    }

    private fun uploadProductImages(imageUrlOrPathList: List<String>, sizeChartPath: String) {
        val uploadIdList: ArrayList<String> = ArrayList()
        val urlImageCount = imageUrlOrPathList.size
        var sizeChartUploadId = ""
        // if sizeChartPath valid then add to progress
        notificationManager = if (sizeChartPath.isNotEmpty()) {
            getNotificationManager(urlImageCount + 1)
        } else {
            getNotificationManager(urlImageCount)
        }
        notificationManager?.onSubmitPost()
        launch(coroutineContext) {
            repeat(urlImageCount) { i ->
                val imageId = uploadImageAndGetId(imageUrlOrPathList[i])
                uploadIdList.add(imageId)
            }
            if (sizeChartPath.isNotEmpty()){ // if sizeChartPath valid then upload the image
                sizeChartUploadId = uploadImageAndGetId(sizeChartPath)
            }
            addProduct(uploadIdList, sizeChartUploadId)
        }
    }

    private suspend fun uploadImageAndGetId(imagePath: String): String {
        val filePath = File(imagePath)
        val params = uploaderUseCase.createParams(
                sourceId = IMAGE_SOURCE_ID,
                filePath = filePath
        )
        return when (val result = uploaderUseCase(params)) {
            is UploadResult.Success -> {
                notificationManager?.onAddProgress(filePath)
                result.uploadId
            }
            is UploadResult.Error -> {
                notificationManager?.onFailedPost(result.reason.name)
                ""
            }
        }
    }

    private fun addProduct(uploadIdList: ArrayList<String>, sizeChartId: String) {
        val shopId = userSession.shopId
        val param = addProductInputMapper.mapInputToParam(shopId, uploadIdList,
                sizeChartId, detailInputModel, descriptionInputModel, shipmentInputModel,
                variantInputModel)
        launchCatchError(block = {
            withContext(Dispatchers.IO) {
                productAddUseCase.params = ProductAddUseCase.createRequestParams(param)
                notificationManager?.onSuccessPost()
                return@withContext productAddUseCase.executeOnBackground()
            }
        }, onError = {
            notificationManager?.onFailedPost(it.message!!)
        })
    }

    private fun getNotificationManager(urlImageCount: Int): AddEditProductNotificationManager {
        val notifId = Random().nextInt()
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return object : AddEditProductNotificationManager(notifId, urlImageCount, manager,
                this@AddEditProductUploadService) {
            override fun getSuccessIntent(): PendingIntent {
                val intent = AddEditProductPreviewActivity.createInstance(context)
                return PendingIntent.getActivity(context, 0, intent, 0)
            }

            override fun getFailedIntent(errorMessage: String): PendingIntent {
                val intent = AddEditProductPreviewActivity.createInstance(context)
                return PendingIntent.getActivity(context, 0, intent, 0)
            }
        }
    }
}
