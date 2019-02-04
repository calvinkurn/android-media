package com.tokopedia.product.detail.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationCompat
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ticker.TouchViewPager
import com.tokopedia.design.list.adapter.TouchImageAdapter
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.view.activity.ImagePreviewUtils.getUri
import com.tokopedia.product.detail.view.activity.ImagePreviewUtils.processPictureName
import com.tokopedia.product.detail.view.activity.ImagePreviewUtils.saveImageFromBitmap
import java.io.File
import java.util.*

class ImagePreviewActivity : BaseSimpleActivity() {
    private var title: String? = null
    private var description: String? = null
    private var adapter: TouchImageAdapter? = null
    private var fileLocations: ArrayList<String>? = null
    private var imageDescriptions: ArrayList<String>? = null
    private var position = 0

    private val viewPager by lazy {
        findViewById<TouchViewPager>(R.id.viewPager)
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

        if (title.isNullOrEmpty()) {
            setContentView(R.layout.activity_image_preview_button)
        } else {
            setContentView(R.layout.activity_image_preview)
        }

        adapter = TouchImageAdapter(this@ImagePreviewActivity, fileLocations)

        findViewById<TextView>(R.id.tvTitle)?.setTextAndCheckShow(title)
        findViewById<TextView>(R.id.tvDescription)?.setTextAndCheckShow(description)

        findViewById<View>(R.id.ivClose).setOnClickListener { finish() }
        findViewById<View>(R.id.ivDownload).setOnClickListener {
            downloadImageCheckPermission()
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
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        startActivity(intent);
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    private fun downloadImageCheckPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
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

    override fun getScreenName(): String? {
        return SCREEN_NAME
    }

    private fun actionDownloadAndSavePicture() {
        val filenameParam = processPictureName(viewPager.getCurrentItem())
        val notificationId = filenameParam.hashCode()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(this@ImagePreviewActivity,
                ANDROID_GENERAL_CHANNEL)
        notificationBuilder.setContentTitle(filenameParam)
                .setContentText(getString(R.string.download_in_process))
                .setSmallIcon(R.drawable.chuck_ic_notification_white_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_big_notif_customerapp))
                .setAutoCancel(true)
        notificationBuilder.setProgress(0, 0, true);
        notificationManager.notify(notificationId, notificationBuilder.build())
        val targetListener: SimpleTarget<Bitmap> = object : SimpleTarget<Bitmap>() {
            @SuppressLint("Range")
            override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                if (resource == null) {
                    return;
                }
                var path: String?
                try {
                    path = saveImageFromBitmap(
                            this@ImagePreviewActivity,
                            resource,
                            filenameParam
                    );
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
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

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

            override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                super.onLoadFailed(e, errorDrawable)
                showFailedDownload(notificationId, notificationBuilder)
            }
        };
        ImageHandler.loadImageBitmap2(this@ImagePreviewActivity,
                fileLocations?.get(viewPager.getCurrentItem()),
                targetListener);
    }

    fun showFailedDownload(notificationId: Int, notificationBuilder: NotificationCompat.Builder) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationBuilder.setContentText(getString(R.string.download_failed))
                .setProgress(0, 0, false);
        notificationBuilder.build().flags =
                notificationBuilder.build().flags or Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(notificationId, notificationBuilder.build())
        val snackbar = ToasterError.make(findViewById<View>(android.R.id.content),
                getString(R.string.download_failed), BaseToaster.LENGTH_SHORT)
        snackbar.setAction(R.string.title_ok) { snackbar.dismiss() }
        snackbar.show()
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    companion object {

        private const val IMAGE_URIS = "image_uris"
        private const val IMG_POSITION = "img_pos"
        private const val IMAGE_DESC = "image_desc"
        private const val TITLE = "title"
        private const val DESCRIPTION = "desc"


        private const val ANDROID_GENERAL_CHANNEL = "ANDROID_GENERAL_CHANNEL"
        private const val REQUEST_PERMISSIONS = 109
        private const val SCREEN_NAME = "Preview Image Product page"
        //TODO
        //for log
        //private val PREVIEW_IMAGE_PDP = "PREVIEW_IMAGE_PDP"

        @JvmStatic
        @JvmOverloads
        fun getIntent(context: Context,
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