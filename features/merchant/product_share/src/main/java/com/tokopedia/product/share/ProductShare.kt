package com.tokopedia.product.share

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.imagepicker.common.util.ImageUtils
import com.tokopedia.product.share.ekstensions.getShareContent
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.util.LinkProperties
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
            putExtra(Intent.EXTRA_HTML_TEXT, shareUri)
            putExtra(Intent.EXTRA_TITLE, title)
            putExtra(Intent.EXTRA_TEXT, shareContent)
            putExtra(Intent.EXTRA_SUBJECT, title)
        }

        activity.startActivity(Intent.createChooser(shareIntent, SHARE_PRODUCT_TITLE))
    }

    private fun isBranchUrlActive() = remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_ACTIVATE_BRANCH_LINKS, true)
    private fun isAndroidIosUrlActivated() = remoteConfig.getBoolean(FIREBASE_KEY_INCLUDEMOBILEWEB, true)

    private fun generateBranchLink(file: File?, data: ProductData) {
        if (isBranchUrlActive()){
            val branchUniversalObject = createBranchUniversalObject(data)
            val linkProperties = createLinkProperties(data)
            branchUniversalObject.generateShortUrl(activity, linkProperties) {url, error ->
                if (error == null){
                    openIntentShare(file, data.productName, data.getShareContent(url), url)
                } else {
                    openIntentShare(file,  data.productName, data.getShareContent(data.renderShareUri), data.renderShareUri)
                }
            }
        } else {
            openIntentShare(file,  data.productName, data.getShareContent(data.renderShareUri), data.renderShareUri)
        }

    }

    private fun createBranchUniversalObject(data: ProductData) = BranchUniversalObject().apply {
        canonicalIdentifier = data.productId
        title = data.productName
        setContentDescription(data.productName)
        setContentImageUrl(data.productImageUrl ?: "")
        setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
    }

    private fun createLinkProperties(data: ProductData) = LinkProperties().apply {
        campaign = CAMPAIGN_NAME
        channel = ARG_UTM_SOURCE
        feature = ARG_UTM_MEDIUM
        addControlParameter(PARAM_OG_URL, null)
        addControlParameter(PARAM_OG_TITLE, data.productName)
        addControlParameter(PARAM_OG_IMAGE_URL, data.productImageUrl)
        addControlParameter(PARAM_OG_DESCR, data.productName)
        addControlParameter(BRANCH_DESKTOP_URL_KEY, data.renderShareUri)

        val deeplinkUrl = generateAppLink(ApplinkConst.PRODUCT_INFO, data.productId)

        addControlParameter(BRANCH_ANDROID_DEEPLINK_PATH_KEY, deeplinkUrl)
        addControlParameter(BRANCH_IOS_DEEPLINK_PATH_KEY, deeplinkUrl)

        if (isAndroidIosUrlActivated()){
            addControlParameter(BRANCH_ANDROID_DESKTOP_URL_KEY, data.renderShareUri)
            addControlParameter(BRANCH_IOS_DESKTOP_URL_KEY, data.renderShareUri)
        }
    }

    private fun generateAppLink(applinkTemplate: String, id: String): String {
        return if (applinkTemplate.contains(APPLINK_SCHEME)){
            applinkTemplate.replaceFirst(APPLINK_SCHEME, "").replaceFirst("""\{.*?\} ?""".toRegex(), id)
        } else if (applinkTemplate.contains(DESKTOP_URL_SCHEME)){
            applinkTemplate.replace(DESKTOP_URL_SCHEME, "")
        } else if (applinkTemplate.contains(MOBILE_URL_SCHEME)){
            applinkTemplate.replace(MOBILE_URL_SCHEME, "")
        } else {
            applinkTemplate
        }
    }


    companion object {
        private const val DEFAULT_IMAGE_WIDTH = 2048
        private const val DEFAULT_IMAGE_HEIGHT = 2048

        private const val SHARE_PRODUCT_TITLE = "Bagikan Produk Ini"
        private const val CAMPAIGN_NAME = "Product Share"
        private const val ARG_UTM_SOURCE = "Android"
        private const val ARG_UTM_MEDIUM = "Share"

        private const val PARAM_OG_URL = "\$og_url"
        private const val PARAM_OG_TITLE = "\$og_title"
        private const val PARAM_OG_IMAGE_URL = "\$og_image_url"
        private const val PARAM_OG_DESCR = "\$og_description"

        private const val BRANCH_DESKTOP_URL_KEY = "\$desktop_url"
        private const val BRANCH_ANDROID_DESKTOP_URL_KEY = "\$android_url"
        private const val BRANCH_IOS_DESKTOP_URL_KEY = "\$ios_url"
        private const val BRANCH_ANDROID_DEEPLINK_PATH_KEY = "\$android_deeplink_path"
        private const val BRANCH_IOS_DEEPLINK_PATH_KEY = "\$ios_deeplink_path"

        private const val APPLINK_SCHEME = "tokopedia://"
        private const val DESKTOP_URL_SCHEME = "https://www.tokopedia.com/"
        private const val MOBILE_URL_SCHEME = "https://m.tokopedia.com/"

        private const val FIREBASE_KEY_INCLUDEMOBILEWEB = "app_branch_include_mobileweb"

        const val MODE_TEXT = 0
        const val MODE_IMAGE = 1
    }
}