package com.tokopedia.kol.feature.comment.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kol.analytics.KolEventTracking
import com.tokopedia.kol.feature.comment.view.fragment.KolCommentFragment
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * @author by nisie on 10/27/17.
 */

/**
 * Deeplink format:
 * tokopedia-android-internal://content/comment/{post_id}
 */
class KolCommentActivity : BaseSimpleActivity() {
    private var kolId: Int = 0
    private var fromApplink = false

    override fun onCreate(savedInstanceState: Bundle?) {
        getDataFromIntent()
        super.onCreate(savedInstanceState)
    }

    private fun getDataFromIntent() {
        intent.data?.let {
            kolId = it.lastPathSegment?.toIntOrNull() ?: 0
            it.getQueryParameter(ARGS_FROM_APPLINK)?.let { isAppLink ->
                fromApplink = isAppLink == "true"
            }
        }
    }

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        val postId = intent.data?.lastPathSegment
        if (!postId.isNullOrEmpty()) {
            bundle.putInt(ARGS_ID, postId.toIntOrZero())
        }
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }

        //because all extras are retrieved as String from deeplink
        val rowPosition = bundle[ARGS_POSITION]
        if (rowPosition is String) bundle.putInt(ARGS_POSITION, rowPosition.toInt())

        val colPosition = bundle[ARGS_POSITION_COLUMN]
        if (colPosition is String) bundle.putInt(ARGS_POSITION_COLUMN, colPosition.toInt())

        return KolCommentFragment.createInstance(bundle)
    }

    override fun onBackPressed() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                KolEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                KolEventTracking.Category.FEED_CONTENT_COMMENT_DETAIL,
                KolEventTracking.Action.FEED_COMMENT_CLICK_BACK,
                KolEventTracking.EventLabel.FEED_CONTENT_COMMENT_DETAIL_BACK
        ))
        super.onBackPressed()
    }

    companion object {
        const val ARGS_HEADER = "ARGS_HEADER"
        const val ARGS_ID = "ARGS_ID"
        private const val ARGS_POSITION = "ARGS_POSITION"
        private const val ARGS_POSITION_COLUMN = "ARGS_POSITION_COLUMN"
        const val ARGS_FROM_FEED = "ARGS_FROM_FEED"
        const val ARGS_FROM_APPLINK = "isFromApplink"

        @JvmStatic
        fun getCallingIntent(context: Context, id: Int, rowNumber: Int): Intent {
            val intent = Intent(context, KolCommentActivity::class.java)
            val bundle = Bundle()
            bundle.putInt(ARGS_ID, id)
            bundle.putInt(ARGS_POSITION, rowNumber)
            intent.putExtras(bundle)
            return intent
        }

    }
}
