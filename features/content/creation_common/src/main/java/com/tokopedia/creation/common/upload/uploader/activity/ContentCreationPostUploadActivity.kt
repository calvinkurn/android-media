package com.tokopedia.creation.common.upload.uploader.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.creation.common.upload.analytic.PlayShortsUploadAnalytic
import com.tokopedia.creation.common.upload.di.uploader.CreationUploaderComponentProvider
import com.tokopedia.creation.common.upload.model.CreationUploadType
import com.tokopedia.creation.common.upload.uploader.dialog.ContentInstallMainAppDialog
import com.tokopedia.kotlin.extensions.view.isAppInstalled
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on February 20, 2023
 */
class ContentCreationPostUploadActivity : BaseActivity() {

    @Inject
    lateinit var playShortsUploadAnalytic: PlayShortsUploadAnalytic

    private val uploadType: CreationUploadType by lazy {
        CreationUploadType.mapFromValue(intent.getStringExtra(EXTRA_UPLOAD_TYPE).orEmpty())
    }

    private val installMainAppDialog by lazy(LazyThreadSafetyMode.NONE) {
        ContentInstallMainAppDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)

        hitAnalytic()
        redirectToViewerRoom()
    }

    override fun onDestroy() {
        super.onDestroy()
        installMainAppDialog.clear()
    }

    private fun inject() {
        CreationUploaderComponentProvider
            .get(applicationContext)
            .inject(this)
    }

    private fun hitAnalytic() {
        when (uploadType) {
            CreationUploadType.Shorts -> {
                val channelId = intent.getStringExtra(EXTRA_CHANNEL_ID).orEmpty()
                val authorId = intent.getStringExtra(EXTRA_AUTHOR_ID).orEmpty()
                val authorType = intent.getStringExtra(EXTRA_AUTHOR_TYPE).orEmpty()

                playShortsUploadAnalytic.clickRedirectToChannelRoom(authorId, authorType, channelId)
            }
            else -> {}
        }
    }

    private fun redirectToViewerRoom() {
        val appLink = intent.getStringExtra(EXTRA_APP_LINK).orEmpty()

        if (GlobalConfig.isSellerApp()) {
            if (isAppInstalled(GlobalConfig.PACKAGE_CONSUMER_APP)) {
                startActivity(
                    Intent(Intent.ACTION_VIEW).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        data = Uri.parse(appLink)
                    }
                )
                finish()
            } else {
                installMainAppDialog.openPlayStore(this) {
                    finish()
                }
            }
        } else {
            route(appLink)
        }
    }

    private fun route(appLink: String) {
        RouteManager.route(this, appLink)
        finish()
    }

    companion object {
        private const val EXTRA_CHANNEL_ID = "channel_id"
        private const val EXTRA_AUTHOR_ID = "author_id"
        private const val EXTRA_AUTHOR_TYPE = "author_type"
        private const val EXTRA_UPLOAD_TYPE = "upload_type"
        private const val EXTRA_APP_LINK = "app_link"

        fun getIntent(
            context: Context,
            channelId: String,
            authorId: String,
            authorType: String,
            uploadType: String,
            appLink: String,
        ): Intent {
            return Intent(context, ContentCreationPostUploadActivity::class.java).apply {
                putExtra(EXTRA_CHANNEL_ID, channelId)
                putExtra(EXTRA_AUTHOR_ID, authorId)
                putExtra(EXTRA_AUTHOR_TYPE, authorType)
                putExtra(EXTRA_UPLOAD_TYPE, uploadType)
                putExtra(EXTRA_APP_LINK, appLink)
            }
        }
    }
}
