package com.tokopedia.product.share

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
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
import com.tokopedia.product.share.ekstensions.getShareContent
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.utils.image.ImageProcessingUtil
import timber.log.Timber
import java.io.File

class ProductShare(private val activity: Activity, private val mode: Int = MODE_TEXT) {

    private val remoteConfig by lazy { FirebaseRemoteConfigImpl(activity) }
    private var cancelShare: Boolean = false

    fun cancelShare(cancelShare: Boolean) {
        this.cancelShare = cancelShare
    }

    var branchTime = 0L
    var imageProcess = 0L
    var resourceReady = 0L
    var err: MutableList<Throwable> = mutableListOf()
    var linkerErrorLog: LinkerError? = null

    private fun resetLog() {
        branchTime = 0L
        imageProcess = 0L
        resourceReady = 0L
        err = mutableListOf()
        linkerErrorLog = null
    }

    fun share(data: ProductData, preBuildImage: () -> Unit, postBuildImage: () -> Unit, isLog: Boolean = false) {
        cancelShare = false
        resetLog()

        if (mode == MODE_IMAGE) {
            preBuildImage()
            val timeStart = System.currentTimeMillis()
            val target = object : CustomTarget<Bitmap>(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val timeResourceEnd = System.currentTimeMillis()
                    resourceReady = timeResourceEnd - timeStart
                    val sticker = ProductImageSticker(activity, resource, data)
                    try {
                        val bitmap = sticker.buildBitmapImage()
                        val file = ImageProcessingUtil.writeImageToTkpdPath(bitmap, Bitmap.CompressFormat.JPEG)
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
            generateBranchLink(null, data, postBuildImage)
        }
    }

    @SuppressLint("BinaryOperationInTimber")
    fun log(mode: Int, imageReady: Long, imageProcess: Long, branchTime: Long, error: List<Throwable>, linkerError: LinkerError?) {
        Timber.w("P2#$log_tag#log;mode=$mode;img_ready=$imageReady;" +
                "img_process=$imageProcess;branch_time=$branchTime;" +
                "err='${error.map { it.stackTrace.toString().substring(0, 50) }.joinToString(",")}';" +
                "linker_err='${linkerError?.errorCode}'")
    }

    fun logExceptionToFirebase(e: Throwable) {
        try {
            FirebaseCrashlytics.getInstance().recordException(ProductShareException(e))
        } catch (ignored: Exception) {
        }
    }

    private fun openIntentShare(file: File?, title: String?, shareContent: String, shareUri: String) {
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

    private fun isBranchUrlActive() = remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_ACTIVATE_BRANCH_LINKS, true)

    private fun generateBranchLink(file: File?, data: ProductData, postBuildImage: (() -> Unit?)? = null, isLog: Boolean = false) {
        if (isBranchUrlActive()) {
            val branchStart = System.currentTimeMillis()
            LinkerManager.getInstance().executeShareRequest(LinkerUtils.createShareRequest(0,
                    productDataToLinkerDataMapper(data), object : ShareCallback {
                override fun urlCreated(linkerShareData: LinkerShareResult) {
                    val branchEnd = System.currentTimeMillis()
                    branchTime = (branchEnd - branchStart)
                    postBuildImage?.invoke()
                    try {
                        openIntentShare(file, data.productName, data.getShareContent(linkerShareData.url), linkerShareData.url)
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
                    linkerErrorLog = linkerError
                    postBuildImage?.invoke()
                    openIntentShareDefault(file, data)
                    if (isLog) {
                        log(mode, resourceReady, imageProcess, branchTime, err, linkerError)
                    }
                }
            }))
        } else {
            postBuildImage?.invoke()
            openIntentShareDefault(file, data)
        }
    }

    private fun openIntentShareDefault(file: File?, data: ProductData) {
        openIntentShare(file, data.productName, data.getShareContent(data.renderShareUri), data.renderShareUri)
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


    companion object {
        private const val DEFAULT_IMAGE_WIDTH = 2048
        private const val DEFAULT_IMAGE_HEIGHT = 2048

        private const val SHARE_PRODUCT_TITLE = "Bagikan Produk Ini"

        const val MODE_TEXT = 0
        const val MODE_IMAGE = 1

        const val log_tag = "BRANCH_GENERATE"

    }
}

class ProductShareException(e: Throwable) : Throwable(e)