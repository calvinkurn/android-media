package com.tokopedia.play_common.shortsuploader.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.play_common.shortsuploader.analytic.PlayShortsUploadAnalytic
import com.tokopedia.play_common.shortsuploader.di.uploader.DaggerPlayShortsUploaderComponent
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on February 20, 2023
 */
class PlayShortsPostUploadActivity : BaseActivity() {

    @Inject
    lateinit var playShortsUploadAnalytic: PlayShortsUploadAnalytic

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)

        hitAnalytic()
        redirectToPlayRoom()
    }

    private fun inject() {
        DaggerPlayShortsUploaderComponent.builder()
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
        val webLink = intent.getStringExtra(EXTRA_WEB_LINK).orEmpty()
        RouteManager.route(this, webLink)
        finish()
    }

    companion object {
        private const val EXTRA_CHANNEL_ID = "channel_id"
        private const val EXTRA_AUTHOR_ID = "author_id"
        private const val EXTRA_AUTHOR_TYPE = "author_type"
        private const val EXTRA_WEB_LINK = "web_link"

        fun getIntent(
            context: Context,
            channelId: String,
            authorId: String,
            authorType: String,
            webLink: String,
        ): Intent {
            return Intent(context, PlayShortsPostUploadActivity::class.java).apply {
                putExtra(EXTRA_CHANNEL_ID, channelId)
                putExtra(EXTRA_AUTHOR_ID, authorId)
                putExtra(EXTRA_AUTHOR_TYPE, authorType)
                putExtra(EXTRA_WEB_LINK, webLink)
            }
        }
    }
}
