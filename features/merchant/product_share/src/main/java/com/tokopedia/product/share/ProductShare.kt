package com.tokopedia.product.share

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.imagepicker.common.util.ImageUtils
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
import java.io.File
import java.lang.Exception

class ProductShare(private val activity: Activity, private val mode: Int = MODE_TEXT) {
    private val remoteConfig by lazy { FirebaseRemoteConfigImpl(activity) }

    fun share(data: ProductData, preBuildImage: ()->Unit, postBuildImage: ()-> Unit){
        if (mode == MODE_IMAGE) {
            preBuildImage()

            ImageHandler.loadImageWithTargetCenterCrop(activity, data.productImageUrl, object : SimpleTarget<Bitmap>(DEFAULT_IMAGE_WIDTH,
                    DEFAULT_IMAGE_HEIGHT){
                override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                    super.onLoadFailed(e, errorDrawable)
                    try {
                        generateBranchLink(null, data)
                    } catch (t: Throwable){
                    } finally {
                        postBuildImage()
                    }
                }

                override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                    if (resource == null){
                        onLoadFailed(null, null)
                        return
                    }
                    val sticker = ProductImageSticker(activity, resource, data)
                    try {
                        val bitmap = sticker.buildBitmapImage()
                        val file = ImageUtils.writeImageToTkpdPath(ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE, bitmap, false)
                        bitmap.recycle()
                        generateBranchLink(file, data)
                    } catch (t: Throwable){
                        generateBranchLink(null, data)
                    } finally {
                        postBuildImage()
                    }
                }
            })
        } else {
            generateBranchLink(null, data)
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

        activity.startActivity(Intent.createChooser(shareIntent, SHARE_PRODUCT_TITLE))
    }

    private fun isBranchUrlActive() = remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_ACTIVATE_BRANCH_LINKS, true)

    private fun generateBranchLink(file: File?, data: ProductData) {
        if (isBranchUrlActive()){
            LinkerManager.getInstance().executeShareRequest(LinkerUtils.createShareRequest(0,
                    productDataToLinkerDataMapper(data), object : ShareCallback {
                override fun urlCreated(linkerShareData: LinkerShareResult) {
                    openIntentShare(file, data.productName, data.getShareContent(linkerShareData.url), linkerShareData.url)
                }

                override fun onError(linkerError: LinkerError) {
                    openIntentShare(file,  data.productName, data.getShareContent(data.renderShareUri), data.renderShareUri)
                }
            }))
        } else {
            openIntentShare(file,  data.productName, data.getShareContent(data.renderShareUri), data.renderShareUri)
        }

    }

    private fun productDataToLinkerDataMapper(productData: ProductData): LinkerShareData{
        var linkerData = LinkerData();
        linkerData.id = productData.productId
        linkerData.name = productData.productName
        linkerData.description = productData.productName
        linkerData.imgUri = productData.productImageUrl
        linkerData.ogUrl = null
        linkerData.type = LinkerData.PRODUCT_TYPE
        linkerData.uri =  productData.renderShareUri
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

    }
}