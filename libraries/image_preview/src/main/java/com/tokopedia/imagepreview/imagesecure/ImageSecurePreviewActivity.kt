package com.tokopedia.imagepreview.imagesecure

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.imagepreview.utils.ImageDownloader
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.file.FileUtil
import java.util.ArrayList

class ImageSecurePreviewActivity : ImagePreviewActivity() {

    private var touchImageAdapter: TouchImageAdapterKt? = null
    private var isSecure: Boolean = false
    private var userSession: UserSessionInterface? = null

    override fun setupAdapter() {
        isSecure = intent.extras?.getBoolean(IS_SECURE, false) ?: false
        userSession = UserSession(this@ImageSecurePreviewActivity).apply {
            touchImageAdapter = TouchImageAdapterKt(fileLocations, this, isSecure)
            touchImageAdapter?.setOnImageStateChangeListener(
                object : TouchImageAdapterKt.OnImageStateChange {
                    override fun onStateDefault() {
                        viewPager.SetAllowPageSwitching(true)
                    }

                    override fun onStateZoom() {
                        viewPager.SetAllowPageSwitching(false)
                    }
                }
            )
            viewPager.adapter = touchImageAdapter
            viewPager.currentItem = position
        }
    }

    override fun actionDownloadAndSavePicture() {
        val filenameParam = FileUtil.generateUniqueFileNameDateFormat(viewPager.currentItem)
        val notificationId = filenameParam.hashCode()
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(this, ANDROID_GENERAL_CHANNEL)

        userSession?.let {
            val imageDownloader = ImageDownloader(
                this@ImageSecurePreviewActivity,
                fileLocations = fileLocations,
                userSessionInterface = it
            )
            imageDownloader.downloadImage(
                viewPagePosition = viewPager.currentItem,
                filenameParam = filenameParam,
                notificationId = notificationId,
                notificationBuilder = notificationBuilder,
                notificationManager = notificationManager,
                onActionClick = { resultUri ->
                    openImageDownloaded(resultUri)
                },
                onDismissed = {
                    this@ImageSecurePreviewActivity.window.setFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN
                    )
                }
            )
        }
    }

    companion object {
        private const val IS_SECURE = "is_image_secure"

        @JvmStatic
        @JvmOverloads
        fun getCallingIntent(
            context: Context,
            imageUris: ArrayList<String>,
            imageDesc: ArrayList<String>? = null,
            position: Int = 0,
            title: String? = null,
            description: String? = null,
            disableDownloadButton: Boolean = true,
            isSecure: Boolean = false
        ): Intent {
            val intent = Intent(context, ImageSecurePreviewActivity::class.java)
            val bundle = Bundle()
            bundle.putString(TITLE, title)
            bundle.putString(DESCRIPTION, description)
            bundle.putStringArrayList(IMAGE_URIS, imageUris)
            bundle.putStringArrayList(IMAGE_DESC, imageDesc)
            bundle.putInt(IMG_POSITION, position)
            bundle.putBoolean(DISABLE_DOWNLOAD, disableDownloadButton)
            bundle.putBoolean(IS_SECURE, isSecure)
            intent.putExtras(bundle)
            return intent
        }
    }
}