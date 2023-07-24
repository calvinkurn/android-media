package com.tokopedia.universal_sharing.view.bottomsheet

import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.media.loader.loadImageWithEmptyTarget
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.util.MimeType
import com.tokopedia.universal_sharing.util.UniversalShareConst
import com.tokopedia.universal_sharing.view.bottomsheet.listener.PermissionListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ScreenShotListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.utils.image.ImageProcessingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object SharingUtil {
    private const val copyLinkToastString = "Sip, link berhasil disalin!"
    private const val actionBtnTxt = "OK"

    const val labelWhatsapp = "whatsapp"
    const val labelFbfeed = "fbfeed"
    const val labelFbstory = "fbstory"
    const val labelIgfeed = "igfeed"
    const val labelIgMessage = "igmessage"
    const val labelIgstory = "igstory"
    const val labelLine = "line"
    const val labelTwitter = "twitter"
    const val labelTelegram = "telegram"
    const val labelSalinLink = "salinlink"
    const val labelSms = "sms"
    const val labelEmail = "email"
    const val labelOthers = "lainnya"

    fun saveViewCaptureToStorage(
        resource: Bitmap,
        imageSaved: (savedImgPath: String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launchCatchError(block = {
            withContext(Dispatchers.IO) {
                val savedFile = ImageProcessingUtil.writeImageToTkpdPath(
                    resource,
                    Bitmap.CompressFormat.PNG
                )
                if (savedFile != null) {
                    imageSaved(savedFile.absolutePath)
                }
            }
        }, onError = {
                it.printStackTrace()
            })
    }

    fun saveImageFromURLToStorage(
        context: Context?,
        shareImageUrl: String,
        imageSaved: (savedImgPath: String) -> Unit
    ) {
        if (context == null) return

        CoroutineScope(Dispatchers.IO).launchCatchError(block = {
            withContext(Dispatchers.IO) {
                loadImageWithEmptyTarget(
                    context = context,
                    url = shareImageUrl,
                    mediaTarget = MediaBitmapEmptyTarget(onReady = { resource ->
                        val savedFile = ImageProcessingUtil.writeImageToTkpdPath(
                            resource,
                            Bitmap.CompressFormat.PNG
                        )
                        if (savedFile != null) {
                            imageSaved(savedFile.absolutePath)
                        }
                    })
                )
            }
        }, onError = {
                it.printStackTrace()
            })
    }

    fun triggerSS(view: View?, imageSaved: (savedImgPath: String) -> Unit) {
        val bitmap = takeScreenshot(view)
        if (bitmap != null) {
            saveViewCaptureToStorage(bitmap, imageSaved)
        }
    }

    fun takeScreenshot(view: View?): Bitmap? {
        var bitmap: Bitmap? = null
        if (view != null) {
            bitmap = Bitmap.createBitmap(
                view.width,
                view.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = bitmap?.let { Canvas(it) }
            view.draw(canvas)
        }
        return bitmap
    }

    fun executeShopPageShareIntent(shareModel: ShareModel, linkerShareData: LinkerShareResult?, activity: Activity?, view: View?, shareStringContainer: String) {
        try {
            var shareImageFileUri: Uri? = null
            if (!TextUtils.isEmpty(shareModel.savedImageFilePath)) {
                shareImageFileUri = MethodChecker.getUri(activity, File(shareModel.savedImageFilePath))
                shareModel.appIntent?.clipData = ClipData.newRawUri("", shareImageFileUri)
                shareModel.appIntent?.removeExtra(Intent.EXTRA_STREAM)
                shareModel.appIntent?.removeExtra(Intent.EXTRA_TEXT)
            }

            var shareString = shareStringContainer
            when (shareModel) {
                is ShareModel.CopyLink -> {
                    linkerShareData?.url?.let {
                        if (activity != null) {
                            ClipboardHandler().copyToClipboard(activity, it)
                        }
                    }
                    view.let {
                        if (it != null) {
                            Toaster.build(view = it, text = copyLinkToastString, actionText = actionBtnTxt).show()
                        }
                    }
                }

                is ShareModel.Instagram, is ShareModel.Facebook -> {
                    if (shareModel.shareOnlyLink) {
                        shareString = linkerShareData?.url.toString()
                    }
                    if (activity != null) {
                        ClipboardHandler().copyToClipboard(
                            activity,
                            shareString
                        )
                    }

                    if (shareModel.shareOnlyLink) {
                        activity?.startActivity(
                            shareModel.appIntent?.apply {
                                setDataAndType(shareImageFileUri, "image/*")
                                if (shareImageFileUri != null) {
                                    putExtra(Intent.EXTRA_STREAM, shareImageFileUri)
                                }
                                putExtra(Intent.EXTRA_TITLE, shareString)
                            }
                        )
                    } else {
                        activity?.startActivity(
                            shareModel.appIntent?.apply {
                                putExtra(Intent.EXTRA_TEXT, shareString)
                            }
                        )
                    }
                }

                is ShareModel.Email -> {
                    activity?.startActivity(
                        shareModel.appIntent?.apply {
                            if (shareImageFileUri != null) {
                                putExtra(Intent.EXTRA_STREAM, shareImageFileUri)
                            }
                            type = MimeType.IMAGE.type
                            if (shareModel.shareOnlyLink) {
                                shareString = linkerShareData?.url.toString()
                            }
                            putExtra(Intent.EXTRA_TEXT, shareString)
                            putExtra(Intent.EXTRA_SUBJECT, shareModel.subjectName)
                        }
                    )
                }

                is ShareModel.Others -> {
                    activity?.startActivity(
                        Intent.createChooser(
                            Intent(Intent.ACTION_SEND).apply {
                                if (shareModel.shareOnlyLink) {
                                    if (shareImageFileUri != null) {
                                        putExtra(Intent.EXTRA_STREAM, shareImageFileUri)
                                    }
                                    type = MimeType.IMAGE.type
                                    shareString = linkerShareData?.url ?: ""
                                } else {
                                    type = MimeType.TEXT.type
                                }
                                putExtra(Intent.EXTRA_TEXT, shareString)
                            },
                            activity.getString(R.string.label_to_social_media_text)
                        )
                    )
                }

                is ShareModel.Whatsapp -> {
                    activity?.startActivity(
                        shareModel.appIntent?.apply {
                            type = MimeType.TEXT.type
                            putExtra(Intent.EXTRA_TEXT, shareString)
                        }
                    )
                }

                is ShareModel.Line -> {
                    activity?.startActivity(
                        shareModel.appIntent?.apply {
                            putExtra(Intent.EXTRA_TEXT, shareString)
                        }
                    )
                }

                is ShareModel.Telegram -> {
                    activity?.startActivity(
                        shareModel.appIntent?.apply {
                            type = MimeType.ALL.type
                            putExtra(Intent.EXTRA_TEXT, shareString)
                        }
                    )
                }

                is ShareModel.Twitter -> {
                    activity?.startActivity(
                        shareModel.appIntent?.apply {
                            type = MimeType.TEXT.type
                            putExtra(Intent.EXTRA_TEXT, shareString)
                        }
                    )
                }

                else -> {
                    activity?.startActivity(
                        shareModel.appIntent?.apply {
                            if (shareImageFileUri != null) {
                                putExtra(Intent.EXTRA_STREAM, shareImageFileUri)
                            }
                            type = MimeType.IMAGE.type
                            if (shareModel.shareOnlyLink) {
                                shareString = linkerShareData?.url ?: ""
                            }
                            putExtra(Intent.EXTRA_TEXT, shareString)
                        }
                    )
                }
            }
        } catch (ex: Exception) {
            if (ex.localizedMessage != null) {
                val errorMap = mapOf("type" to "crashLog", "reason" to (ex.localizedMessage))
                logError(errorMap)
            }
        }
    }

    fun executeShareIntent(
        shareModel: ShareModel,
        linkerShareData: LinkerShareResult?,
        activity: Activity?,
        view: View?,
        shareStringContainer: String,
        onSuccessCopyLink: (() -> Unit)? = null
    ) {
        try {
            var shareImageFileUri: Uri? = null
            if (!TextUtils.isEmpty(shareModel.savedImageFilePath) && shareModel.savedImageFilePath != UniversalShareConst.ImageType.MEDIA_VALUE_PLACEHOLDER) {
                shareImageFileUri = MethodChecker.getUri(activity, File(shareModel.savedImageFilePath))
                shareModel.appIntent?.clipData = ClipData.newRawUri("", shareImageFileUri)
                shareModel.appIntent?.removeExtra(Intent.EXTRA_STREAM)
                shareModel.appIntent?.removeExtra(Intent.EXTRA_TEXT)
            }

            var shareString = shareStringContainer
            when (shareModel) {
                is ShareModel.CopyLink -> {
                    linkerShareData?.url?.let {
                        if (activity != null) {
                            ClipboardHandler().copyToClipboard(activity, it)
                        }
                    }
                    if (onSuccessCopyLink == null) {
                        view.let {
                            if (it != null) {
                                Toaster.build(view = it, text = copyLinkToastString, actionText = actionBtnTxt).show()
                            }
                        }
                    } else {
                        onSuccessCopyLink.invoke()
                    }
                }

                is ShareModel.Instagram, is ShareModel.Facebook -> {
                    if (shareModel.shareOnlyLink) {
                        shareString = linkerShareData?.url.toString()
                    }
                    if (activity != null) {
                        ClipboardHandler().copyToClipboard(
                            activity,
                            shareString
                        )
                    }

                    if (shareModel.shareOnlyLink) {
                        activity?.startActivity(
                            shareModel.appIntent?.apply {
                                setDataAndType(shareImageFileUri, "image/*")
                                if (shareImageFileUri != null) {
                                    putExtra(Intent.EXTRA_STREAM, shareImageFileUri)
                                }
                                putExtra(Intent.EXTRA_TITLE, shareString)
                            }
                        )
                    } else {
                        activity?.startActivity(
                            shareModel.appIntent?.apply {
                                putExtra(Intent.EXTRA_TEXT, shareString)
                            }
                        )
                    }
                }

                is ShareModel.Whatsapp -> {
                    activity?.startActivity(
                        shareModel.appIntent?.apply {
                            if (shareImageFileUri != null && shareModel.shareOnlyLink) {
                                putExtra(Intent.EXTRA_STREAM, shareImageFileUri)
                            }
                            type = MimeType.TEXT.type
                            if (shareModel.shareOnlyLink) {
                                shareString = linkerShareData?.url.toString()
                            }
                            putExtra(Intent.EXTRA_TEXT, shareString)
                        }
                    )
                }

                is ShareModel.Email -> {
                    activity?.startActivity(
                        shareModel.appIntent?.apply {
                            if (shareImageFileUri != null) {
                                putExtra(Intent.EXTRA_STREAM, shareImageFileUri)
                            }
                            type = MimeType.IMAGE.type
                            if (shareModel.shareOnlyLink) {
                                shareString = linkerShareData?.url.toString()
                            }
                            putExtra(Intent.EXTRA_TEXT, shareString)
                            putExtra(Intent.EXTRA_SUBJECT, shareModel.subjectName)
                        }
                    )
                }

                is ShareModel.Others -> {
                    activity?.startActivity(
                        Intent.createChooser(
                            Intent(Intent.ACTION_SEND).apply {
                                if (shareModel.shareOnlyLink) {
                                    if (shareImageFileUri != null) {
                                        putExtra(Intent.EXTRA_STREAM, shareImageFileUri)
                                    }
                                    type = MimeType.IMAGE.type
                                    shareString = linkerShareData?.url ?: ""
                                } else {
                                    type = MimeType.TEXT.type
                                }
                                putExtra(Intent.EXTRA_TEXT, shareString)
                            },
                            activity.getString(R.string.label_to_social_media_text)
                        )
                    )
                }

                is ShareModel.Line -> {
                    activity?.startActivity(
                        shareModel.appIntent?.apply {
                            if (shareModel.shareOnlyLink) {
                                if (shareImageFileUri != null) {
                                    putExtra(Intent.EXTRA_STREAM, shareImageFileUri)
                                }
                                shareString = linkerShareData?.url ?: ""
                            }
                            putExtra(Intent.EXTRA_TEXT, shareString)
                            ClipboardHandler().copyToClipboard((activity as Activity), shareString)
                        }
                    )
                }

                is ShareModel.Telegram -> {
                    activity?.startActivity(
                        shareModel.appIntent?.apply {
                            if (shareImageFileUri != null) {
                                putExtra(Intent.EXTRA_STREAM, shareImageFileUri)
                            }
                            type = MimeType.ALL.type
                            if (shareModel.shareOnlyLink) {
                                shareString = linkerShareData?.url ?: ""
                            }
                            putExtra(Intent.EXTRA_TEXT, shareString)
                        }
                    )
                }

                else -> {
                    activity?.startActivity(
                        shareModel.appIntent?.apply {
                            if (shareImageFileUri != null) {
                                putExtra(Intent.EXTRA_STREAM, shareImageFileUri)
                            }
                            type = MimeType.IMAGE.type
                            if (shareModel.shareOnlyLink) {
                                shareString = linkerShareData?.url ?: ""
                            }
                            putExtra(Intent.EXTRA_TEXT, shareString)
                        }
                    )
                }
            }
        } catch (ex: Exception) {
            if (ex.localizedMessage != null) {
                val errorMap = mapOf("type" to "crashLog", "reason" to (ex.localizedMessage))
                logError(errorMap)
            }
        }
    }

    fun logError(messageMap: Map<String, String>) {
        ServerLogger.log(Priority.P2, "SHARING_CRASH", messageMap)
    }

    fun isCustomSharingEnabled(context: Context?, remoteConfigKey: String = UniversalShareConst.RemoteConfigKey.GLOBAL_CUSTOM_SHARING_FEATURE_FLAG): Boolean {
        val isEnabled: Boolean
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        isEnabled = remoteConfig.getBoolean(remoteConfigKey)
        return isEnabled
    }

    fun removePreviousSavedImage(previousSavedImagePath: String, newSavedImagePath: String) {
        if (!TextUtils.isEmpty(previousSavedImagePath) &&
            !TextUtils.isEmpty(newSavedImagePath) &&
            previousSavedImagePath != newSavedImagePath
        ) {
            removeFile(previousSavedImagePath)
        }
    }

    fun removeFile(filePath: String) {
        if (!TextUtils.isEmpty(filePath) &&
            !filePath.contains(ScreenshotDetector.screenShotRegex, true)
        ) {
            File(filePath).apply {
                if (exists()) {
                    delete()
                }
            }
        }
    }

    fun createAndStartScreenShotDetector(
        context: Context,
        screenShotListener: ScreenShotListener,
        fragment: Fragment,
        remoteConfigKey: String = UniversalShareConst.RemoteConfigKey.GLOBAL_SCREENSHOT_SHARING_FEATURE_FLAG,
        addFragmentLifecycleObserver: Boolean = false,
        permissionListener: PermissionListener? = null
    ): ScreenshotDetector? {
        val isEnabled: Boolean
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        isEnabled = remoteConfig.getBoolean(remoteConfigKey)
        var screenshotDetector: ScreenshotDetector? = null
        if (isEnabled) {
            screenshotDetector = ScreenshotDetector(context.applicationContext, screenShotListener, permissionListener)
            if (addFragmentLifecycleObserver) {
                setFragmentLifecycleObserverForScreenShot(fragment, screenshotDetector)
            }
            screenshotDetector.detectScreenshots(fragment)
        }
        return screenshotDetector
    }

    private fun setFragmentLifecycleObserverForScreenShot(fragment: Fragment, screenshotDetector: ScreenshotDetector?) {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                screenshotDetector?.start()
            }

            override fun onStop(owner: LifecycleOwner) {
                super.onStop(owner)
                clearState(screenshotDetector)
            }

            override fun onDestroy(owner: LifecycleOwner) {
                fragment.lifecycle.removeObserver(this)
                clearState(screenshotDetector)
                super.onDestroy(owner)
            }
        })
    }

    // Use this method to get type of the Share Bottom Sheet inside the onShareOptionClicked and onCloseOptionClicked methods
    // This method can be used to get the bottomsheet type after show() method is called to send required GTM events based on bottomsheet type
    fun clearState(screenshotDetector: ScreenshotDetector?) {
        screenshotDetector?.stop()
    }

    fun transformOgImageURL(context: Context?, imageURL: String): String {
        if (context != null) {
            val remoteConfig = FirebaseRemoteConfigImpl(context)
            val ogImageTransformationEnabled = remoteConfig.getBoolean(UniversalShareConst.RemoteConfigKey.GLOBAL_ENABLE_OG_IMAGE_TRANSFORM)
            if (ogImageTransformationEnabled && !TextUtils.isEmpty(imageURL) && imageURL.endsWith(".webp")) {
                if (imageURL.endsWith(".png.webp") || imageURL.endsWith(".jpg.webp") ||
                    imageURL.endsWith(".jpeg.webp")
                ) {
                    return imageURL.replace(".webp", "")
                }
            }
        }
        return imageURL
    }
}
