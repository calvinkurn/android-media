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
import com.tokopedia.product.addedit.common.constant.AddEditProductExtraConstant.Companion.IMAGE_SOURCE_ID
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_DESCRIPTION_INPUT
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_DETAIL_INPUT
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_SHIPMENT_INPUT
import com.tokopedia.product.addedit.common.domain.mapper.AddProductInputMapper
import com.tokopedia.product.addedit.common.domain.usecase.ProductAddUseCase
import com.tokopedia.product.addedit.common.util.AddEditProductNotificationManager
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
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

    companion object {
        private const val JOB_ID = 13131314

        fun startService(context: Context,
                         detailInputModel: DetailInputModel,
                         descriptionInputModel: DescriptionInputModel,
                         shipmentInputModel: ShipmentInputModel) {
            val work = Intent(context, AddEditProductUploadService::class.java).apply {
                putExtra(EXTRA_DETAIL_INPUT, detailInputModel)
                putExtra(EXTRA_DESCRIPTION_INPUT, descriptionInputModel)
                putExtra(EXTRA_SHIPMENT_INPUT, shipmentInputModel)
            }
            enqueueWork(context, AddEditProductUploadService::class.java, JOB_ID, work)
        }
    }

    override fun onCreate() {
        super.onCreate()
        initInjector()
    }

    override val coroutineContext: CoroutineContext by lazy {
        Dispatchers.IO
    }

    override fun onHandleWork(intent: Intent) {
        shipmentInputModel = intent.getParcelableExtra(EXTRA_SHIPMENT_INPUT)
        descriptionInputModel = intent.getParcelableExtra(EXTRA_DESCRIPTION_INPUT)
        detailInputModel = intent.getParcelableExtra(EXTRA_DETAIL_INPUT)
        uploadImage(detailInputModel.imageUrlOrPathList)
    }

    private fun addProduct(uploadIdList: ArrayList<String>) {
        val shopId = userSession.shopId
        val param = addProductInputMapper.mapInputToParam(shopId, uploadIdList,
                detailInputModel, descriptionInputModel, shipmentInputModel)
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

    private fun uploadImage(imageUrlOrPathList: List<String>) {
        val uploadIdList: ArrayList<String> = ArrayList()
        val urlImageCount = imageUrlOrPathList.size
        notificationManager = getNotificationManager(urlImageCount)
        notificationManager?.onSubmitPost()
        launch(coroutineContext) {
            repeat(urlImageCount) { i ->
                val filePath = File(imageUrlOrPathList[i])
                val params = uploaderUseCase.createParams(
                        sourceId = IMAGE_SOURCE_ID,
                        filePath = filePath
                )
                when (val result = uploaderUseCase(params)) {
                    is UploadResult.Success -> {
                        notificationManager?.onAddProgress(filePath)
                        uploadIdList.add(result.uploadId)
                    }
                    is UploadResult.Error -> {
                        notificationManager?.onFailedPost(result.reason.name)
                    }
                }
            }
            addProduct(uploadIdList)
        }
    }

    private fun initInjector() {
        DaggerAddEditProductPreviewComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .addEditProductPreviewModule(AddEditProductPreviewModule())
                .build()
                .inject(this)
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
