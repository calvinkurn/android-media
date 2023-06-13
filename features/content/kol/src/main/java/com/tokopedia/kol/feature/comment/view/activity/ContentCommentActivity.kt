package com.tokopedia.kol.feature.comment.view.activity

import android.annotation.SuppressLint
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
import com.tokopedia.kol.feature.postdetail.view.analytics.ContentDetailNewPageAnalytics
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.ARGS_AUTHOR_TYPE
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.ARGS_IS_POST_FOLLOWED
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.ARGS_POST_TYPE
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.ARGS_VIDEO
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.ARG_IS_FROM_CONTENT_DETAIL_PAGE
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.COMMENT_ARGS_POSITION
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailPageAnalyticsDataModel
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import javax.inject.Inject

/**
 * Deeplink format:
 * tokopedia-android-internal://content/comment/{post_id}
 */

class ContentCommentActivity : BaseSimpleActivity() {
    private var fromApplink = false
    var postId: String? = ""

    @Inject
    internal lateinit var feedAnalytics: FeedAnalyticTracker

    @Inject
    lateinit var analyticsTracker: ContentDetailNewPageAnalytics

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

    @SuppressLint("Method Call Prohibited")
    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        postId = intent.data?.lastPathSegment
        if (!postId.isNullOrEmpty()) {
            bundle.putLong(ARGS_ID, postId.toLongOrZero())
        }
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }

        //because all extras are retrieved as String from deeplink
        val rowPosition = bundle[COMMENT_ARGS_POSITION]
        if (rowPosition is String) bundle.putInt(COMMENT_ARGS_POSITION, rowPosition.toInt())

        val colPosition = bundle[ARGS_POSITION_COLUMN]
        if (colPosition is String) bundle.putInt(ARGS_POSITION_COLUMN, colPosition.toInt())
        return KolCommentNewFragment.createInstance(bundle)
    }

    private fun getDataFromIntent() {
        intent.data?.let {
            it.getQueryParameter(ARGS_FROM_APPLINK)?.let { isAppLink ->
                fromApplink = isAppLink == "true"
            }
        }
    }

    override fun onBackPressed() {
        val isFromContentDetailPage = intent.getBooleanExtra(ARG_IS_FROM_CONTENT_DETAIL_PAGE, false)
        val shopId = intent.getStringExtra(ARGS_AUTHOR_TYPE) ?: ""
        val type = intent.getStringExtra(ARGS_POST_TYPE) ?: ""
        val isFollowed = intent.getBooleanExtra(ARGS_IS_POST_FOLLOWED, true)
        val source = intent.getStringExtra(ContentDetailArgumentModel.CONTENT_DETAIL_PAGE_SOURCE)
        if (isFromContentDetailPage)
            analyticsTracker.sendClickBackOnCommentPage(
                ContentDetailPageAnalyticsDataModel(
                    activityId = postId ?: "0",
                    shopId = shopId,
                    type = type,
                    isFollowed = isFollowed,
                    source = source ?: ""
                )
            )
        else
            feedAnalytics.clickBackButtonCommentPage(
                postId ?: "0",
                intent.getStringExtra(ARGS_AUTHOR_TYPE) ?: "",
                intent.getBooleanExtra(ARGS_VIDEO, false),
                intent.getBooleanExtra(ARGS_IS_POST_FOLLOWED, true),
                intent.getStringExtra(ARGS_POST_TYPE) ?: "",
                authorType = ""
            )
        super.onBackPressed()
    }

    companion object {
        private const val ARGS_POSITION_COLUMN = "ARGS_POSITION_COLUMN"
        const val ARGS_FROM_APPLINK = "isFromApplink"
        const val ARGS_ID = "ARGS_ID"


        @JvmStatic
        fun getCallingIntent(
            context: Context,
            id: Int,
            rowNumber: Int,
            authorId: String?,
            isFollowed: Boolean? = true,
            postType: String?
        ): Intent {
            val intent = Intent(context, ContentCommentActivity::class.java)
            val bundle = Bundle()
            bundle.putInt(ARGS_ID, id)
            bundle.putInt(COMMENT_ARGS_POSITION, rowNumber)
            bundle.putBoolean(ARGS_VIDEO, true)
            bundle.putString(ARGS_AUTHOR_TYPE, authorId)
            bundle.putString(ARGS_POST_TYPE, postType)
            if (isFollowed != null)
                bundle.putBoolean(ARGS_IS_POST_FOLLOWED, isFollowed)
            else
                bundle.putBoolean(ARGS_IS_POST_FOLLOWED, true)

            intent.putExtras(bundle)
            return intent
        }
    }
}
