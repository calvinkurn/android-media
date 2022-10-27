package com.tokopedia.product.share

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.product.share.ekstensions.getShareContent
import com.tokopedia.product.share.ekstensions.getTextDescription
import com.tokopedia.product.share.ekstensions.showImmediately
import com.tokopedia.product.share.tracker.ProductShareTracking.onClickAccessPhotoMediaAndFiles
import com.tokopedia.product.share.tracker.ProductShareTracking.onClickChannelWidgetClicked
import com.tokopedia.product.share.tracker.ProductShareTracking.onCloseShareWidgetClicked
import com.tokopedia.product.share.tracker.ProductShareTracking.onImpressShareWidget
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.universal_sharing.constants.ImageGeneratorConstants
import com.tokopedia.universal_sharing.model.ImageGeneratorParamModel
import com.tokopedia.universal_sharing.model.PdpParamModel
import com.tokopedia.universal_sharing.view.bottomsheet.ScreenshotDetector
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.PermissionListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.AffiliatePDPInput
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.utils.image.ImageProcessingUtil
import java.io.File

class ProductShare(private val activity: Activity, private val mode: Int = MODE_TEXT) {

    companion object {
        private const val DEFAULT_IMAGE_WIDTH = 2048
        private const val DEFAULT_IMAGE_HEIGHT = 2048

        private const val SHARE_PRODUCT_TITLE = "Bagikan Produk Ini"

        const val MODE_TEXT = 0
        const val MODE_IMAGE = 1

        const val log_tag = "BRANCH_GENERATE"
        const val TIMEOUT_LOG = 5_000L // seconds
    }

    private val remoteConfig by lazy { FirebaseRemoteConfigImpl(activity) }
    private var cancelShare: Boolean = false
    private var universalShareBottomSheet: UniversalShareBottomSheet? = null
    private lateinit var productData: ProductData
    private lateinit var preBuildImage: () -> Unit
    private lateinit var postBuildImage: () -> Unit

    //View is the Fragment View
    private lateinit var parentView: View

    fun cancelShare(cancelShare: Boolean) {
        this.cancelShare = cancelShare
        if (cancelShare && isLog) {
            val timeEnd = System.currentTimeMillis()
            val duration = timeEnd - timeStartShare
            if (duration > TIMEOUT_LOG) {
                if (branchTime == 0L) {
                    branchTime = duration
                }
                log(mode, imageProcess, resourceReady, branchTime, err, null)
            }
        }
    }

    var branchTime = 0L
    var imageProcess = 0L
    var resourceReady = 0L
    var timeStartShare = 0L
    var isLog = false
    var err: MutableList<Throwable> = mutableListOf()

    private fun resetLog() {
        branchTime = 0L
        imageProcess = 0L
        resourceReady = 0L
        timeStartShare = 0L
        isLog = false
        err = mutableListOf()
    }

    fun share(
        data: ProductData,
        preBuildImage: () -> Unit,
        postBuildImage: () -> Unit,
        isLog: Boolean = false
    ) {
        cancelShare = false
        resetLog()
        this.isLog = isLog
        timeStartShare = System.currentTimeMillis()

        if (mode == MODE_IMAGE) {
            preBuildImage()
            val target = object : CustomTarget<Bitmap>(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val timeResourceEnd = System.currentTimeMillis()
                    resourceReady = timeResourceEnd - timeStartShare
                    val sticker = ProductImageSticker(activity, resource, data)
                    try {
                        val bitmap = sticker.buildBitmapImage()
                        val file = ImageProcessingUtil.writeImageToTkpdPath(
                            bitmap,
                            Bitmap.CompressFormat.JPEG
                        )
                        bitmap.recycle()
                        val timeBitmapEnd = System.currentTimeMillis()
                        imageProcess = timeBitmapEnd - timeResourceEnd
                        generateBranchLink(file, data, isLog = isLog)
                    } catch (t: Throwable) {
                        logExceptionToFirebase(t)
                        err.add(t)
                        generateBranchLink(null, data, isLog = isLog)
                    } finally {
                        postBuildImage()
                    }
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    try {
                        generateBranchLink(null, data, isLog = isLog)
                    } catch (t: Throwable) {
                        logExceptionToFirebase(t)
                    } finally {
                        postBuildImage()
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            }

            ImageHandler.loadImageWithTargetCenterCrop(activity, data.productImageUrl, target)
        } else {
            preBuildImage.invoke()
            generateBranchLink(null, data, postBuildImage, isLog = isLog)
        }
    }

    @SuppressLint("BinaryOperationInTimber")
    fun log(
        mode: Int,
        imageReady: Long,
        imageProcess: Long,
        branchTime: Long,
        error: List<Throwable>,
        linkerError: LinkerError?
    ) {
        // only log fail or long timeout
        if (imageReady > TIMEOUT_LOG ||
            imageProcess > TIMEOUT_LOG ||
            branchTime > TIMEOUT_LOG ||
            linkerError != null ||
            error.isNotEmpty()
        ) {
            ServerLogger.log(
                Priority.P2, log_tag, mapOf(
                    "type" to "log",
                    "mode" to mode.toString(),
                    "img_ready" to imageReady.toString(),
                    "img_process" to imageProcess.toString(),
                    "branch_time" to branchTime.toString(),
                    "err" to error.map { it.stackTraceToString().take(200) }.joinToString(","),
                    "linker_err" to linkerError?.errorCode.toString()
                )
            )
        }
    }

    fun logExceptionToFirebase(e: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(ProductShareException(e))
    }

    private fun openIntentShare(
        file: File?,
        title: String?,
        shareContent: String,
        shareUri: String
    ) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = if (file == null || mode == MODE_TEXT) "text/plain" else "image/*"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (file != null) {
                putExtra(Intent.EXTRA_STREAM, MethodChecker.getUri(activity, file))
            }
            putExtra(Intent.EXTRA_REFERRER, shareUri)
            putExtra(Intent.EXTRA_HTML_TEXT, shareContent)
            putExtra(Intent.EXTRA_TITLE, title)
            putExtra(Intent.EXTRA_TEXT, shareContent)
            putExtra(Intent.EXTRA_SUBJECT, title)
        }

        if (!cancelShare) {
            activity.startActivity(Intent.createChooser(shareIntent, SHARE_PRODUCT_TITLE))
        }
    }

    private fun isBranchUrlActive() =
        remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_ACTIVATE_BRANCH_LINKS, true)

    private fun generateBranchLink(
        file: File?,
        data: ProductData,
        postBuildImage: (() -> Unit?)? = null,
        isLog: Boolean = false
    ) {
        if (isBranchUrlActive()) {
            val branchStart = System.currentTimeMillis()
            LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest(
                    0,
                    productDataToLinkerDataMapper(data),
                    object : ShareCallback {
                        override fun urlCreated(linkerShareData: LinkerShareResult) {
                            val branchEnd = System.currentTimeMillis()
                            branchTime = (branchEnd - branchStart)
                            postBuildImage?.invoke()
                            try {
                                openIntentShare(
                                    file,
                                    data.productName,
                                    data.getShareContent(linkerShareData.url),
                                    linkerShareData.url
                                )
                            } catch (e: Exception) {
                                err.add(e)
                                logExceptionToFirebase(e)
                                openIntentShareDefault(file, data)
                            }
                            if (isLog) {
                                log(mode, resourceReady, imageProcess, branchTime, err, null)
                            }
                        }

                        override fun onError(linkerError: LinkerError) {
                            postBuildImage?.invoke()
                            openIntentShareDefault(file, data)
                            if (isLog) {
                                log(mode, resourceReady, imageProcess, branchTime, err, linkerError)
                            }
                        }
                    })
            )
        } else {
            postBuildImage?.invoke()
            openIntentShareDefault(file, data)
            if (isLog) {
                log(mode, resourceReady, imageProcess, branchTime, err, null)
            }
        }
    }

    private fun openIntentShareDefault(file: File?, data: ProductData) {
        openIntentShare(
            file,
            data.productName,
            data.getShareContent(data.renderShareUri),
            data.renderShareUri
        )
    }

    private fun productDataToLinkerDataMapper(productData: ProductData): LinkerShareData {
        var linkerData = LinkerData();
        linkerData.id = productData.productId
        linkerData.name = productData.productName
        linkerData.description = productData.productName
        linkerData.imgUri = productData.productImageUrl
        linkerData.ogUrl = null
        linkerData.type = LinkerData.PRODUCT_TYPE
        linkerData.uri = productData.renderShareUri
        linkerData.isThrowOnError = true
        var linkerShareData = LinkerShareData()
        linkerShareData.linkerData = linkerData
        return linkerShareData
    }

    //region universal sharing
    private fun openIntentShareDefaultUniversalSharing(
        file: File?,
        shareProductName: String = "",
        shareDescription: String = "",
        shareUrl: String = ""
    ) {
        openIntentShare(file, shareProductName, shareDescription, shareUrl)
    }

    private fun shareChannelClicked(shareModel: ShareModel) {
        if (isBranchUrlActive()) {
            val branchStart = System.currentTimeMillis()

            onClickChannelWidgetClicked(
                UniversalShareBottomSheet.getShareBottomSheetType(),
                shareModel.channel.orEmpty(),
                productData.userId,
                productData.productId,
                productData.campaignId,
                productData.bundleId
            )

            val linkerShareData = productDataToLinkerDataMapper(productData)
            linkerShareData.linkerData?.apply {
                feature = shareModel.feature
                channel = shareModel.channel
                campaign = shareModel.campaign
                uri = productData.productUrl
                ogTitle = generateOgTitle(productData)
                ogDescription = generateOgDescription(productData)
                if (shareModel.ogImgUrl != null && shareModel.ogImgUrl!!.isNotEmpty()) {
                    ogImageUrl = shareModel.ogImgUrl
                }
                isAffiliate = shareModel.isAffiliate
            }

            LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest(0, linkerShareData, object : ShareCallback {
                    override fun urlCreated(linkerShareResult: LinkerShareResult) {
                        val branchEnd = System.currentTimeMillis()
                        branchTime = (branchEnd - branchStart)
                        postBuildImage.invoke()
                        try {
                            val shareString = productData.getTextDescription(
                                activity.applicationContext,
                                linkerShareResult.url
                            )
                            shareModel.subjectName = productData.productName ?: ""
                            SharingUtil.executeShareIntent(
                                shareModel,
                                linkerShareResult,
                                activity,
                                parentView,
                                shareString
                            )
                        } catch (e: Exception) {
                            err.add(e)
                            logExceptionToFirebase(e)
                            openIntentShareDefaultUniversalSharing(
                                null, productData.productName ?: "",
                                productData.getTextDescription(
                                    activity.applicationContext,
                                    linkerShareData.linkerData.renderShareUri()
                                ),
                                linkerShareData.linkerData.renderShareUri()
                            )
                        }

                        if (isLog) {
                            log(mode, resourceReady, imageProcess, branchTime, err, null)
                        }
                        universalShareBottomSheet?.dismiss()
                    }

                    override fun onError(linkerError: LinkerError) {
                        postBuildImage.invoke()
                        openIntentShareDefaultUniversalSharing(
                            null, productData.productName ?: "",
                            productData.getTextDescription(
                                activity.applicationContext,
                                linkerShareData.linkerData.renderShareUri()
                            ),
                            linkerShareData.linkerData.renderShareUri()
                        )
                        if (isLog) {
                            log(mode, resourceReady, imageProcess, branchTime, err, linkerError)
                        }
                        universalShareBottomSheet?.dismiss()
                    }
                })
            )
        } else {
            postBuildImage.invoke()
            openIntentShareDefaultUniversalSharing(
                null, productData.productName ?: "",
                productData.getTextDescription(
                    activity.applicationContext,
                    productData.renderShareUri
                ),
                productData.renderShareUri
            )
            if (isLog) {
                log(mode, resourceReady, imageProcess, branchTime, err, null)
            }
        }
    }

    private fun generateOgTitle(productData: ProductData): String {
        return "${productData.productName} - ${productData.priceText}"
    }

    private fun generateOgDescription(productData: ProductData): String {
        return "${productData.shopName} - ${productData.productShareDescription}"
    }

    private fun onCloseShareClicked() {
        onCloseShareWidgetClicked(
            UniversalShareBottomSheet.getShareBottomSheetType(),
            productData.userId,
            productData.productId,
            productData.campaignId,
            productData.bundleId
        )
        universalShareBottomSheet?.dismiss()
    }

    fun showUniversalShareBottomSheet(
        fragmentManager: FragmentManager,
        fragment: Fragment,
        data: ProductData,
        affiliateInput: AffiliatePDPInput,
        isLog: Boolean = false,
        view: View? = null,
        productImgList: ArrayList<String>? = null,
        preBuildImg: () -> Unit,
        postBuildImg: () -> Unit,
        screenshotDetector: ScreenshotDetector? = null,
        paramImageGenerator: PdpParamModel,
    ) {
        cancelShare = false
        resetLog()
        this.isLog = isLog
        timeStartShare = System.currentTimeMillis()
        productData = data
        postBuildImage = postBuildImg
        preBuildImage = preBuildImg
        if (view != null) {
            parentView = view
        }

        showImmediately(
            fragmentManager = fragmentManager,
            parent = fragment,
            screenshotDetector = screenshotDetector
        ) {
            UniversalShareBottomSheet.createInstance().apply {
                getImageFromMedia(true)
                setupAffiliate(affiliateInput, this)
                setMediaPageSourceId(ImageGeneratorConstants.ImageGeneratorSourceId.PDP)
                setImageGeneratorParam(paramImageGenerator)
                init(object : ShareBottomsheetListener {
                    override fun onShareOptionClicked(shareModel: ShareModel) {
                        shareChannelClicked(shareModel)
                    }

                    override fun onCloseOptionClicked() {
                        onCloseShareClicked()
                    }

                })
                setUtmCampaignData("PDP", productData.userId, productData.productId, "share")
                setMetaData(
                    productData.productName ?: "",
                    productData.productImageUrl ?: "",
                    "", productImgList
                )
                universalShareBottomSheet = this
            }
        }
        onImpressShareWidget(
            UniversalShareBottomSheet.getShareBottomSheetType(),
            productData.userId,
            productData.productId,
            productData.campaignId,
            productData.bundleId
        )
    }

    fun updateAffiliate(shopStatus: Int) {
        val existingAffiliateInput = universalShareBottomSheet?.getAffiliateRequestHolder()
        if (existingAffiliateInput == null ||
            existingAffiliateInput.shop?.shopStatus != null
        ) return // means the data already there, no need to update

        universalShareBottomSheet?.run {
            val affiliateData = existingAffiliateInput.copy(
                shop = existingAffiliateInput.shop?.copy(
                    shopStatus = shopStatus
                )
            )
            setupAffiliate(affiliateData, this)
        }
    }

    private fun setupAffiliate(
        affiliateInput: AffiliatePDPInput,
        universalShareBottomSheet: UniversalShareBottomSheet
    ) {
        universalShareBottomSheet.setAffiliateRequestHolder(affiliateInput)
        if (affiliateInput.shop?.shopStatus == null) {
            universalShareBottomSheet.affiliateRequestDataAwaited()
        } else {
            universalShareBottomSheet.affiliateRequestDataReceived(true)
        }
    }
    //endregion

    val universalSharePermissionListener = object : PermissionListener {
        override fun permissionAction(action: String, label: String) {
            onClickAccessPhotoMediaAndFiles(productData.userId, productData.productId, label)
        }
    }
}

class ProductShareException(e: Throwable) : Throwable(e)
