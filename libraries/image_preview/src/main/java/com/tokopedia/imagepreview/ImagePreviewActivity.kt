package com.tokopedia.imagepreview

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.design.component.ticker.TouchViewPager
import com.tokopedia.imagepreview.imagesecure.TouchImageAdapterKt
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.getBitmapImageUrl
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.file.PublicFolderUtil
import java.io.File
import java.util.*
import com.tokopedia.resources.common.R as resourcescommonR
import com.tokopedia.abstraction.R as abstractionR

@Deprecated("Do not use this class",
    ReplaceWith("Please use ImageSecurePreviewActivity")
)
open class ImagePreviewActivity : BaseSimpleActivity() {
    private var title: String? = null
    private var description: String? = null
    private var adapter: TouchImageAdapterKt? = null
    var fileLocations: ArrayList<String>? = null
    private var imageDescriptions: ArrayList<String>? = null
    var position = 0
    private var disableDownload = false

    val viewPager by lazy {
        findViewById<TouchViewPager>(R.id.viewPager)
    }

    open fun layoutId(): Int {
        return if (title.isNullOrEmpty()) {
            R.layout.activity_image_preview_button
        } else {
            R.layout.activity_image_preview
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        val extras = intent.extras
        if (extras != null) {
            title = extras.getString(TITLE)
            description = extras.getString(DESCRIPTION)
            fileLocations = extras.getStringArrayList(IMAGE_URIS)
            imageDescriptions = extras.getStringArrayList(IMAGE_DESC)
            position = extras.getInt(IMG_POSITION, 0)
            disableDownload = extras.getBoolean(DISABLE_DOWNLOAD, false)
        } else {
            fileLocations = ArrayList()
        }

        setContentView(layoutId())
        setupAdapter()

        findViewById<TextView>(R.id.tvTitle)?.setTextAndCheckShow(title)
        findViewById<TextView>(R.id.tvDescription)?.setTextAndCheckShow(description)

        findViewById<View>(R.id.ivClose).setOnClickListener { finish() }
        setupDownloadButton()
    }

    open fun setupAdapter() {
        adapter = TouchImageAdapterKt(fileLocations, UserSession(this), false)
        adapter?.setOnImageStateChangeListener(object : TouchImageAdapterKt.OnImageStateChange {
            override fun onStateDefault() {
                viewPager.SetAllowPageSwitching(true)
            }

            override fun onStateZoom() {
                viewPager.SetAllowPageSwitching(false)
            }
        })
        viewPager.adapter = adapter
        viewPager.currentItem = position
    }

    private fun setupDownloadButton() {
        val ivDownload = findViewById<View>(R.id.ivDownload)
        if (disableDownload) {
            ivDownload.hide()
        } else {
            ivDownload.show()
            ivDownload.setOnClickListener {
                downloadImageCheckPermission()
            }
        }
    }

    private fun downloadImageCheckPermission(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this,
                    permissions, REQUEST_PERMISSIONS)
        } else {
            actionDownloadAndSavePicture()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this@ImagePreviewActivity, getString(R.string.storage_permission_enabled_needed), Toast.LENGTH_LONG).show()
            } else {
                actionDownloadAndSavePicture()
            }
        }
    }

    protected fun openImageDownloaded(uri: Uri) {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            setDataAndType(uri, "image/*")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(intent)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun getScreenName(): String? {
        return SCREEN_NAME
    }

    open fun actionDownloadAndSavePicture() {
        val filenameParam = FileUtil.generateUniqueFileNameDateFormat(viewPager.currentItem)
        val notificationId = filenameParam.hashCode()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(this@ImagePreviewActivity,
                ANDROID_GENERAL_CHANNEL)
        notificationBuilder.setContentTitle(filenameParam)
                .setContentText(getString(R.string.download_in_process))
                .setSmallIcon(resourcescommonR.drawable.ic_status_bar_notif_customerapp)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                    resourcescommonR.drawable.ic_big_notif_customerapp))
                .setAutoCancel(true)
        notificationBuilder.setProgress(0, 0, true);
        notificationManager.notify(notificationId, notificationBuilder.build())

        fileLocations?.getOrNull(viewPager.currentItem)?.let {
            it.getBitmapImageUrl(this@ImagePreviewActivity, target = MediaBitmapEmptyTarget(
                onFailed = {
                    showFailedDownload(notificationId, notificationBuilder)
                },
                onReady = { resource ->
                    val fileAndUri: Pair<File?, Uri?>
                    try {
                        fileAndUri = PublicFolderUtil.putImageToPublicFolder(
                            this@ImagePreviewActivity,
                            bitmap = resource,
                            fileName = filenameParam)
                    } catch (e: Throwable) {
                        showFailedDownload(notificationId, notificationBuilder)
                        return@MediaBitmapEmptyTarget
                    }
                    val resultUri = fileAndUri.second
                    if (resultUri == null) {
                        showFailedDownload(notificationId, notificationBuilder)
                    } else {
                        val intent = Intent().apply {
                            action = Intent.ACTION_VIEW
                        }
                        intent.setDataAndType(resultUri, "image/*")
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                        val pIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            PendingIntent.getActivity(this@ImagePreviewActivity, 0, intent, PendingIntent.FLAG_MUTABLE)
                        } else {
                            PendingIntent.getActivity(
                                this@ImagePreviewActivity,
                                0,
                                intent,
                                PendingIntent.FLAG_IMMUTABLE
                            )
                        }

                        notificationBuilder.setContentText(getString(R.string.download_success))
                            .setProgress(0, 0, false)
                            .setContentIntent(pIntent);

                        notificationBuilder.build().flags =
                            notificationBuilder.build().flags or Notification.FLAG_AUTO_CANCEL;
                        notificationManager.notify(notificationId, notificationBuilder.build());
                        this@ImagePreviewActivity.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN)

                        val snackbar = SnackbarManager.make(
                            findViewById<View>(android.R.id.content),
                            getString(R.string.download_success),
                            Snackbar.LENGTH_SHORT)
                        snackbar.setAction(getString(R.string.image_preview_label_open)) {
                            openImageDownloaded(resultUri)
                        }
                        snackbar.addCallback(object : Snackbar.Callback() {
                            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                this@ImagePreviewActivity.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
                            }
                        })
                        snackbar.show()
                    }
                },
                onCleared = {}
            ), properties = {
                isAnimate(false)
                setCacheStrategy(MediaCacheStrategy.DATA)
                centerCrop()
            })
        }
    }

    fun showFailedDownload(notificationId: Int, notificationBuilder: NotificationCompat.Builder) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationBuilder.setContentText(getString(R.string.download_failed))
                .setProgress(0, 0, false);
        notificationBuilder.build().flags =
                notificationBuilder.build().flags or Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(notificationId, notificationBuilder.build())
        Toaster.make(findViewById<View>(android.R.id.content), getString(R.string.download_failed),
                Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR, getString(
                abstractionR.string.title_ok))
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    companion object {

        const val IMAGE_URIS = "image_uris"
        const val IMG_POSITION = "img_pos"
        const val DISABLE_DOWNLOAD = "disable_download"
        const val IMAGE_DESC = "image_desc"
        const val TITLE = "title"
        const val DESCRIPTION = "desc"


        const val ANDROID_GENERAL_CHANNEL = "ANDROID_GENERAL_CHANNEL"
        const val REQUEST_PERMISSIONS = 109
        const val SCREEN_NAME = "Preview Image Product page"
        //TODO
        //for log
        //private val PREVIEW_IMAGE_PDP = "PREVIEW_IMAGE_PDP"

        @JvmStatic
        @JvmOverloads
        fun getCallingIntent(context: Context,
                             imageUris: ArrayList<String>,
                             imageDesc: ArrayList<String>? = null,
                             position: Int = 0,
                             title: String? = null,
                             description: String? = null,
                             disableDownloadButton: Boolean = true): Intent {
            val intent = Intent(context, ImagePreviewActivity::class.java)
            val bundle = Bundle()
            bundle.putString(TITLE, title)
            bundle.putString(DESCRIPTION, description)
            bundle.putStringArrayList(IMAGE_URIS, imageUris)
            bundle.putStringArrayList(IMAGE_DESC, imageDesc)
            bundle.putInt(IMG_POSITION, position)
            bundle.putBoolean(DISABLE_DOWNLOAD, disableDownloadButton)
            intent.putExtras(bundle)
            return intent
        }
    }
}
