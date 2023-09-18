package com.tokopedia.creation.common.upload.uploader.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.creation.common.upload.analytic.PlayShortsUploadAnalytic
import com.tokopedia.creation.common.upload.di.uploader.DaggerCreationUploaderComponent
import com.tokopedia.creation.common.upload.uploader.dialog.PlayInstallMainAppDialog
import com.tokopedia.kotlin.extensions.view.isAppInstalled
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on February 20, 2023
 */
class PlayShortsPostUploadActivity : BaseActivity() {

    @Inject
    lateinit var playShortsUploadAnalytic: PlayShortsUploadAnalytic

    private val playInstallMainAppDialog by lazy(LazyThreadSafetyMode.NONE) {
        PlayInstallMainAppDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)

        hitAnalytic()
        redirectToPlayRoom()
    }

    override fun onDestroy() {
        super.onDestroy()
        playInstallMainAppDialog.clear()
    }

    private fun inject() {
        DaggerCreationUploaderComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun hitAnalytic() {
        val channelId = intent.getStringExtra(EXTRA_CHANNEL_ID).orEmpty()
        val authorId = intent.getStringExtra(EXTRA_AUTHOR_ID).orEmpty()
        val authorType = intent.getStringExtra(EXTRA_AUTHOR_TYPE).orEmpty()

        playShortsUploadAnalytic.clickRedirectToChannelRoom(authorId, authorType, channelId)
    }

    private fun redirectToPlayRoom() {
        val appLink = intent.getStringExtra(EXTRA_APP_LINK).orEmpty()

        if (GlobalConfig.isSellerApp()) {
            if (isAppInstalled(CUSTOMER_APP_PACKAGE)) {
                startActivity(
                    Intent(Intent.ACTION_VIEW).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        data = Uri.parse(appLink)
                    }
                )
                finish()
            } else {
                playInstallMainAppDialog.openPlayStore(this) {
                    finish()
                }
            }
        } else {
            RouteManager.route(this, appLink)
            finish()
        }
    }

    companion object {
        private const val EXTRA_CHANNEL_ID = "channel_id"
        private const val EXTRA_AUTHOR_ID = "author_id"
        private const val EXTRA_AUTHOR_TYPE = "author_type"
        private const val EXTRA_APP_LINK = "app_link"

        private const val CUSTOMER_APP_PACKAGE = "com.tokopedia.tkpd"

        fun getIntent(
            context: Context,
            channelId: String,
            authorId: String,
            authorType: String,
            appLink: String,
        ): Intent {
            return Intent(context, PlayShortsPostUploadActivity::class.java).apply {
                putExtra(EXTRA_CHANNEL_ID, channelId)
                putExtra(EXTRA_AUTHOR_ID, authorId)
                putExtra(EXTRA_AUTHOR_TYPE, authorType)
                putExtra(EXTRA_APP_LINK, appLink)
            }
        }
    }
}
