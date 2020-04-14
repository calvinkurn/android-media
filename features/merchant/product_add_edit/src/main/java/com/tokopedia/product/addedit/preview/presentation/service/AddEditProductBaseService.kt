package com.tokopedia.product.addedit.preview.presentation.service

import androidx.core.app.JobIntentService
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mediauploader.data.state.UploadResult
import com.tokopedia.mediauploader.domain.UploaderUseCase
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.common.constant.AddEditProductExtraConstant.Companion.IMAGE_SOURCE_ID
import com.tokopedia.product.addedit.common.util.AddEditProductNotificationManager
import com.tokopedia.product.addedit.draft.domain.usecase.DeleteProductDraftUseCase
import com.tokopedia.product.addedit.draft.domain.usecase.SaveProductDraftUseCase
import com.tokopedia.product.addedit.preview.di.AddEditProductPreviewModule
import com.tokopedia.product.addedit.preview.di.DaggerAddEditProductPreviewComponent
import com.tokopedia.product.addedit.preview.domain.mapper.AddProductInputMapper
import com.tokopedia.product.addedit.preview.domain.mapper.DuplicateProductInputMapper
import com.tokopedia.product.addedit.preview.domain.mapper.EditProductInputMapper
import com.tokopedia.product.addedit.preview.domain.usecase.ProductAddUseCase
import com.tokopedia.product.addedit.preview.domain.usecase.ProductEditUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

abstract class AddEditProductBaseService : JobIntentService(), CoroutineScope {
    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var uploaderUseCase: UploaderUseCase
    @Inject
    lateinit var addProductInputMapper: AddProductInputMapper
    @Inject
    lateinit var productAddUseCase: ProductAddUseCase
    @Inject
    lateinit var productEditUseCase: ProductEditUseCase
    @Inject
    lateinit var editProductInputMapper: EditProductInputMapper
    @Inject
    lateinit var saveProductDraftUseCase: SaveProductDraftUseCase
    @Inject
    lateinit var deleteProductDraftUseCase: DeleteProductDraftUseCase
    @Inject
    lateinit var duplicateProductInputMapper: DuplicateProductInputMapper

    private var notificationManager: AddEditProductNotificationManager? = null

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

    fun setUploadProductDataSuccess() {
        notificationManager?.onSuccessUpload()
    }

    fun setUploadProductDataError(errorMessage: String) {
        notificationManager?.onFailedUpload(errorMessage)
    }

    fun uploadProductImages(imageUrlOrPathList: List<String>, sizeChartPath: String) {
        val uploadIdList: ArrayList<String> = ArrayList()
        val urlImageCount = imageUrlOrPathList.size
        var sizeChartUploadId = ""
        // if sizeChartPath valid then add to progress
        notificationManager = if (sizeChartPath.isNotEmpty()) {
            getNotificationManager(urlImageCount + 1)
        } else {
            getNotificationManager(urlImageCount)
        }
        notificationManager?.onSubmitUpload()
        launchCatchError(block = {
            repeat(urlImageCount) { i ->
                val imageId = uploadImageAndGetId(imageUrlOrPathList[i])
                uploadIdList.add(imageId)
            }
            if (sizeChartPath.isNotEmpty()) { // if sizeChartPath valid then upload the image
                sizeChartUploadId = uploadImageAndGetId(sizeChartPath)
            }
            onUploadProductImagesDone(uploadIdList, sizeChartUploadId)
        }, onError = {
            it.message?.let { errorMessage -> setUploadProductDataError(errorMessage) }
        })
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
                notificationManager?.onFailedUpload(result.reason.name)
                ""
            }
        }
    }

    abstract fun getNotificationManager(urlImageCount: Int): AddEditProductNotificationManager
    abstract fun onUploadProductImagesDone(uploadIdList: ArrayList<String>, sizeChartId: String)
}
