package com.tokopedia.imagepreview

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.design.component.ticker.TouchViewPager
import com.tokopedia.design.list.adapter.TouchImageAdapter
import com.tokopedia.imagepreview.ImagePreviewUtils.getUri
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.file.PublicFolderUtil
import java.io.File
import java.util.*

open class ImagePreviewActivity : BaseSimpleActivity() {
    private var title: String? = null
    private var description: String? = null
    private var adapter: TouchImageAdapter? = null
    private var fileLocations: ArrayList<String>? = null
    private var imageDescriptions: ArrayList<String>? = null
    private var position = 0

    private val viewPager by lazy {
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
        } else {
            fileLocations = ArrayList()
        }

        setContentView(layoutId())

        adapter = TouchImageAdapter(this@ImagePreviewActivity, fileLocations)

        findViewById<TextView>(R.id.tvTitle)?.setTextAndCheckShow(title)
        findViewById<TextView>(R.id.tvDescription)?.setTextAndCheckShow(description)

        findViewById<View>(R.id.ivClose).setOnClickListener { finish() }
        findViewById<View>(R.id.ivDownload).setOnClickListener {
            actionDownloadAndSavePicture()
        }

        adapter?.SetonImageStateChangeListener(object : TouchImageAdapter.OnImageStateChange {
            override fun OnStateDefault() {
                viewPager.SetAllowPageSwitching(true);
            }

            override fun OnStateZoom() {
                viewPager.SetAllowPageSwitching(false);
            }

        })
        viewPager.adapter = adapter
        viewPager.currentItem = position
    }

    private fun openImageDownloaded(path: String) {
        val file = File(path)
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            val uri = getUri(this@ImagePreviewActivity, file)
            setDataAndType(uri, "image/*")
        }
        startActivity(intent);
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun getScreenName(): String? {
        return SCREEN_NAME
    }

    private fun actionDownloadAndSavePicture() {
        val filenameParam = FileUtil.generateUniqueFileNameDateFormat(viewPager.currentItem)
        val notificationId = filenameParam.hashCode()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(this@ImagePreviewActivity,
                ANDROID_GENERAL_CHANNEL)
        notificationBuilder.setContentTitle(filenameParam)
                .setContentText(getString(R.string.download_in_process))
                .setSmallIcon(R.drawable.ic_stat_notify_white)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_big_notif_customerapp))
                .setAutoCancel(true)
        notificationBuilder.setProgress(0, 0, true);
        notificationManager.notify(notificationId, notificationBuilder.build())
        val targetListener: CustomTarget<Bitmap> = object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                val path: String?
                try {
                    path = PublicFolderUtil.putImageToPublicFolder(
                            this@ImagePreviewActivity,
                            bitmap = resource,
                            fileName = filenameParam)
                } catch (e: Throwable) {
                    showFailedDownload(notificationId, notificationBuilder)
                    return
                }

                if (path == null) {
                    showFailedDownload(notificationId, notificationBuilder)
                } else {
                    val intent = Intent().apply {
                        action = Intent.ACTION_VIEW
                    }
                    val file = File(path);
                    val uri = getUri(this@ImagePreviewActivity, file);
                    intent.setDataAndType(uri, "image/*");

                    val pIntent = PendingIntent.getActivity(this@ImagePreviewActivity, 0, intent, 0);

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
                    snackbar.setAction(getString(R.string.label_open)) {
                        openImageDownloaded(path)
                    }
                    snackbar.addCallback(object : Snackbar.Callback() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            this@ImagePreviewActivity.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
                        }
                    })
                    snackbar.show()
                }
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                super.onLoadFailed(errorDrawable)
                showFailedDownload(notificationId, notificationBuilder)
            }

            override fun onLoadCleared(placeholder: Drawable?) {

            }
        }
        fileLocations?.getOrNull(viewPager.currentItem)?.let {
            ImageHandler.loadImageBitmap2(
                    this@ImagePreviewActivity,
                    it,
                    targetListener)
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
                Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR, getString(R.string.title_ok))
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    companion object {

        const val IMAGE_URIS = "image_uris"
        const val IMG_POSITION = "img_pos"
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
                             description: String? = null): Intent {
            val intent = Intent(context, ImagePreviewActivity::class.java)
            val bundle = Bundle()
            bundle.putString(TITLE, title)
            bundle.putString(DESCRIPTION, description)
            bundle.putStringArrayList(IMAGE_URIS, imageUris)
            bundle.putStringArrayList(IMAGE_DESC, imageDesc)
            bundle.putInt(IMG_POSITION, position)
            intent.putExtras(bundle)
            return intent
        }
    }
}
