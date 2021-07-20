package com.tokopedia.universal_sharing.view.bottomsheet

import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.utils.image.ImageProcessingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class SharingUtil {


    companion object {
        private const val copyLinkToastString = "Sip, link berhasil disalin!"
        private const val actionBtnTxt = "OK"

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
            CoroutineScope(Dispatchers.IO).launchCatchError(block = {
                withContext(Dispatchers.IO) {
                    ImageHandler.loadImageWithTarget(
                        context,
                        shareImageUrl,
                        object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                val savedFile = ImageProcessingUtil.writeImageToTkpdPath(
                                    resource,
                                    Bitmap.CompressFormat.PNG
                                )
                                if (savedFile != null) {
                                    imageSaved(savedFile.absolutePath)
                                }
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                                // no op
                            }
                        })
                }
            }, onError = {
                it.printStackTrace()
            })
        }

        fun triggerSS(view: View?, imageSaved: (savedImgPath: String) -> Unit){
            val bitmap = takeScreenshot(view)
            if(bitmap != null){
                saveViewCaptureToStorage(bitmap, imageSaved)
            }
        }

        fun takeScreenshot(view: View?): Bitmap? {
            var bitmap : Bitmap? = null
            if (view != null) {
                bitmap = Bitmap.createBitmap(
                    view.width,
                    view.height, Bitmap.Config.ARGB_8888
                )
                val canvas = bitmap?.let { Canvas(it) }
                view.draw(canvas)
            }
            return bitmap
        }

        fun executeShareIntent(shareModel: ShareModel, linkerShareData: LinkerShareResult?, activity: Activity?, view: View?, shareStringContainer: String){
            val shareImageFileUri = MethodChecker.getUri(activity, File(shareModel.savedImageFilePath))
            shareModel.appIntent?.clipData = ClipData.newRawUri("", shareImageFileUri)
            shareModel.appIntent?.removeExtra(Intent.EXTRA_STREAM)
            shareModel.appIntent?.removeExtra(Intent.EXTRA_TEXT)

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
                            Toaster.build(view = it, text = copyLinkToastString,actionText = actionBtnTxt).show()
                        }
                    }
                }
                is ShareModel.Instagram, is ShareModel.Facebook -> {
                    if(shareModel.shareOnlyLink){
                        shareString = linkerShareData?.url.toString()
                    }
                    if (activity != null) {
                        ClipboardHandler().copyToClipboard(
                            activity, shareString)
                    }

                    if(shareModel.shareOnlyLink) {
                        activity?.startActivity(shareModel.appIntent?.apply {
                            setDataAndType(shareImageFileUri, "image/*")
                            putExtra(Intent.EXTRA_STREAM, shareImageFileUri)
                            putExtra(Intent.EXTRA_TITLE, shareString)
                        })
                    }
                    else{
                        activity?.startActivity(shareModel.appIntent?.apply {
                            putExtra(Intent.EXTRA_TEXT, shareString)
                        })
                    }
                }
                is ShareModel.Whatsapp -> {
                    activity?.startActivity(shareModel.appIntent?.apply {
                        putExtra(Intent.EXTRA_STREAM, shareImageFileUri)
                        type = UniversalShareBottomSheet.MimeType.TEXT.type
                        if(shareModel.shareOnlyLink){
                            shareString = linkerShareData?.url.toString()
                        }
                        putExtra(Intent.EXTRA_TEXT, shareString)
                    })
                }

                is ShareModel.Email -> {
                    activity?.startActivity(shareModel.appIntent?.apply {
                        putExtra(Intent.EXTRA_STREAM, shareImageFileUri)
                        type = UniversalShareBottomSheet.MimeType.IMAGE.type
                        if(shareModel.shareOnlyLink){
                            shareString = linkerShareData?.url.toString()
                        }
                        putExtra(Intent.EXTRA_TEXT, shareString)
                        putExtra(Intent.EXTRA_SUBJECT, shareModel.subjectName)
                    })
                }

                is ShareModel.Others -> {
                    activity?.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                        if(shareModel.shareOnlyLink){
                            type = UniversalShareBottomSheet.MimeType.IMAGE.type
                            putExtra(Intent.EXTRA_STREAM, shareImageFileUri)
                            shareString = linkerShareData?.url ?: ""
                        }
                        else{
                            type = UniversalShareBottomSheet.MimeType.TEXT.type
                        }
                        putExtra(Intent.EXTRA_TEXT, shareString)
                    }, activity.getString(R.string.label_to_social_media_text)))
                }
                is ShareModel.Line -> {
                    activity?.startActivity(shareModel.appIntent?.apply {
                        if(shareModel.shareOnlyLink){
                            putExtra(Intent.EXTRA_STREAM, shareImageFileUri)
                            shareString = linkerShareData?.url ?: ""
                        }
                        putExtra(Intent.EXTRA_TEXT, shareString)
                        ClipboardHandler().copyToClipboard((activity as Activity), shareString)
                    })
                }
                else -> {
                    activity?.startActivity(shareModel.appIntent?.apply {
                        putExtra(Intent.EXTRA_STREAM, shareImageFileUri)
                        type = UniversalShareBottomSheet.MimeType.IMAGE.type
                        if(shareModel.shareOnlyLink){
                            shareString = linkerShareData?.url ?: ""
                        }
                        putExtra(Intent.EXTRA_TEXT, shareString)
                    })
                }
            }
        }

    }
}