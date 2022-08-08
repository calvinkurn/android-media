package com.tokopedia.kol.feature.postdetail.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kol.feature.postdetail.view.fragment.ContentDetailFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.kol.feature.postdetail.view.analytics.ContentDetailNewPageAnalytics
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailPageAnalyticsDataModel
import javax.inject.Inject

class ContentDetailActivity : BaseSimpleActivity() {
    var contentDetailFirstPostData : FeedXCard? = null

    @Inject
    lateinit var analyticsTracker: ContentDetailNewPageAnalytics


    override fun getNewFragment(): Fragment {
        val bundle = Bundle().apply {
            putString(
                PARAM_POST_ID, postId()
            )
        }
        return ContentDetailFragment.newInstance(bundle)
    }

    private fun postId(): String {
        return intent?.data?.lastPathSegment ?: DEFAULT_POST_ID
    }
    fun getSource(): String {
        return intent?.extras?.getString(PARAM_SOURCE) ?: SHARE_LINK
    }

    fun setContentDetailMainPostData(card: FeedXCard?) {
        this.contentDetailFirstPostData = card
    }

    override fun onBackPressed() {
        contentDetailFirstPostData?.let {
            if (it.isTypeSgcVideo)
                analyticsTracker.sendClickBackOnContentDetailpage(getContentDetailAnalyticsData(it))
        }
        if (getSource() == SHARE_LINK) {
            goToFeed()
        } else {
            super.onBackPressed()
        }
    }

    private fun goToFeed() {
        this.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.HOME_FEED)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finishAffinity()
        }
    }

    private fun getContentDetailAnalyticsData(feedXCard: FeedXCard) = ContentDetailPageAnalyticsDataModel(
        activityId = if (feedXCard.isTypeVOD) feedXCard.playChannelID else feedXCard.id,
        shopId = feedXCard.author.id,
        isFollowed = feedXCard.followers.isFollowed,
        type = feedXCard.typename,
        mediaType = if (feedXCard.lastCarouselIndex < feedXCard.media.size) feedXCard.media[feedXCard.lastCarouselIndex].type else "",
        mediaUrl = feedXCard.media.firstOrNull()?.mediaUrl ?: "",
        itemName = feedXCard.title,
    )


    companion object {
        const val PARAM_POST_ID = "post_id"
        const val DEFAULT_POST_ID = "0"
        const val PARAM_SOURCE = "source"
        const val SHARE_LINK = "share_link"
    }
}