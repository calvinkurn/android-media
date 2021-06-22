package com.tokopedia.kol.feature.comment.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker
import com.tokopedia.kol.KolComponentInstance
import com.tokopedia.kol.feature.comment.di.DaggerKolCommentComponent
import com.tokopedia.kol.feature.comment.di.KolCommentModule
import com.tokopedia.kol.feature.comment.view.fragment.KolCommentNewFragment
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import javax.inject.Inject

/**
 * Deeplink format:
 * tokopedia-android-internal://content/comment-new/{post_id}
 */

class KolCommentNewActivity : BaseSimpleActivity() {
    private var kolId: Int = 0
    private var fromApplink = false
    var postId: String? = ""

    @Inject
    internal lateinit var feedAnalytics: FeedAnalyticTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        getDataFromIntent()
        super.onCreate(savedInstanceState)
    }

    private fun initInjector() {
        DaggerKolCommentComponent.builder()
            .kolComponent(KolComponentInstance.getKolComponent(application))
            .kolCommentModule(KolCommentModule(null, null))
            .build()
            .inject(this)
    }

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        postId = intent.data?.lastPathSegment
        if (!postId.isNullOrEmpty()) {
            bundle.putInt(KolCommentActivity.ARGS_ID, postId.toIntOrZero())
        }
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }

        //because all extras are retrieved as String from deeplink
        val rowPosition = bundle[ARGS_POSITION]
        if (rowPosition is String) bundle.putInt(ARGS_POSITION, rowPosition.toInt())

        val colPosition = bundle[ARGS_POSITION_COLUMN]
        if (colPosition is String) bundle.putInt(ARGS_POSITION_COLUMN, colPosition.toInt())
        return KolCommentNewFragment.createInstance(bundle)
    }

    private fun getDataFromIntent() {
        intent.data?.let {
            kolId = it.lastPathSegment?.toIntOrNull() ?: 0
            it.getQueryParameter(KolCommentActivity.ARGS_FROM_APPLINK)?.let { isAppLink ->
                fromApplink = isAppLink == "true"
            }
        }
    }

    override fun onBackPressed() {
        feedAnalytics.clickBackButtonCommentPage(postId ?: "0")
        super.onBackPressed()
    }

    companion object {
        private const val ARGS_POSITION = "ARGS_POSITION"
        const val ARGS_ID = "ARGS_ID"
        private const val ARGS_POSITION_COLUMN = "ARGS_POSITION_COLUMN"
        const val ARGS_AUTHOR_TYPE = "ARGS_AUTHOR_TYPE"

        @JvmStatic
        fun getCallingIntent(context: Context, id: Int, rowNumber: Int): Intent {
            val intent = Intent(context, KolCommentNewActivity::class.java)
            val bundle = Bundle()
            bundle.putInt(ARGS_ID, id)
            bundle.putInt(ARGS_POSITION, rowNumber)
            intent.putExtras(bundle)
            return intent
        }
    }
}