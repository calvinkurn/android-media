package com.tokopedia.feedplus.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker
import com.tokopedia.feedcomponent.view.adapter.viewholder.highlight.HighlightAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.highlight.HighlightViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.highlight.HighlightCardViewModel
import com.tokopedia.feedcomponent.view.viewmodel.highlight.HighlightViewModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.data.pojo.FeedTabs
import com.tokopedia.feedplus.view.adapter.typefactory.dynamicfeed.DynamicFeedTypeFactoryImpl
import com.tokopedia.feedplus.view.di.DaggerFeedPlusComponent
import com.tokopedia.feedplus.view.listener.DynamicFeedContract
import com.tokopedia.kol.KolComponentInstance
import com.tokopedia.kol.feature.comment.view.activity.KolCommentActivity
import com.tokopedia.kol.feature.comment.view.fragment.KolCommentFragment
import kotlinx.android.synthetic.main.fragment_dynamic_feed.*
import javax.inject.Inject

/**
 * @author by yoasfs on 2019-08-06
 */
class DynamicFeedFragment:
        BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>(),
        HighlightAdapter.HighlightListener,
        CardTitleView.CardTitleListener,
        DynamicFeedContract.View {

    companion object {
        private const val KOL_COMMENT_CODE = 13
        private const val KEY_FEED  = "KEY_FEED"
        fun newInstance(feedKey: String): DynamicFeedFragment {
            val fragment = DynamicFeedFragment()
            val bundle = Bundle()
            bundle.putString(KEY_FEED, feedKey)
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var presenter: DynamicFeedContract.Presenter

    @Inject
    lateinit var feedAnalyticTracker: FeedAnalyticTracker

    private var isLoading = false
    private var isForceRefresh = false
    private var feedKey = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dynamic_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initViewListener()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView() {
        feedKey = arguments?.getString(KEY_FEED) ?: ""
        presenter.attachView(this)
        rv_dynamic_feed.adapter = adapter
        rv_dynamic_feed.layoutManager = LinearLayoutManager(activity)
        feedAnalyticTracker.eventOpenTrendingPage()

    }

    private fun initViewListener() {

    }

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? {
        return swipe_refresh_layout
    }

    override fun onSwipeRefresh() {
        hideSnackBarRetry()
        updateCursor("")
        swipeToRefresh.isRefreshing = true
        presenter.getFeedFirstPage(true)
    }

    override fun getRecyclerView(view: View?): RecyclerView {
        return rv_dynamic_feed
    }

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return DynamicFeedTypeFactoryImpl(this, this)
    }


    override fun onSuccessGetFeedFirstPage(element: List<Visitable<*>>, lastCursor: String) {
        isLoading = false
        clearAllData()
        renderList(element, lastCursor.isNotEmpty())
        updateCursor(lastCursor)
    }

    override fun onSuccessGetFeed(visitables: List<Visitable<*>>, lastCursor: String) {
        isLoading = false
        renderList(visitables, lastCursor.isNotEmpty())
        updateCursor(lastCursor)
    }

    override fun updateCursor(cursor: String) {
        presenter.cursor = cursor
    }

    override fun onActionPopup() {

    }

    override fun onActionRedirect(redirectUrl: String) {
        onGoToLink(redirectUrl)
    }

    override fun onTitleCtaClick(redirectUrl: String, adapterPosition: Int) {
        val item = ((adapter.list[adapterPosition]) as HighlightViewModel)
        feedAnalyticTracker.eventTrendingClickSeeAll(item.postId)
        onGoToLink(redirectUrl)
    }

    override fun onSuccessDeletePost(rowNumber: Int) {

    }

    override fun onEmptyFeedButtonClicked() {

    }


    override fun onItemClicked(t: Visitable<*>?) {
    }

    override fun getScreenName(): String {
        var screenName = ""
        when(feedKey){
            FeedTabs.KEY_TRENDING -> screenName = FeedAnalyticTracker.Screen.TRENDING
        }
        return screenName
    }

    override fun initInjector() {
        if (activity != null && activity!!.application != null) {
            DaggerFeedPlusComponent.builder()
                    .kolComponent(KolComponentInstance.getKolComponent(activity!!
                            .application))
                    .build()
                    .inject(this)
        }
    }

    override fun loadData(page: Int) {
        isLoading = true
        if (isLoadingInitialData) {
            presenter.getFeedFirstPage(isForceRefresh)
        } else {
            presenter.getFeed()
        }
    }

    override fun onAvatarClick(positionInFeed: Int, redirectUrl: String) {
        val item = ((adapter.list[positionInFeed]) as HighlightViewModel)
        feedAnalyticTracker.eventTrendingClickProfile(item.postId)
        onGoToLink(redirectUrl)
    }

    override fun onLikeClick(positionInFeed: Int, columnNumber: Int, id: Int, isLiked: Boolean) {
        presenter.likeKol(id, positionInFeed, columnNumber)
    }

    override fun onCommentClick(positionInFeed: Int, columnNumber: Int, id: Int) {
        startActivityForResult(KolCommentActivity.getCallingIntent(activity, id, positionInFeed, columnNumber), KOL_COMMENT_CODE)
    }

    override fun onFooterActionClick(positionInFeed: Int, redirectUrl: String) {
        onGoToLink(redirectUrl)
    }

    override fun onSuccessLike(rowNumber: Int, columnNumber: Int) {
        onSuccessLikeUnlike(rowNumber, columnNumber)
    }

    override fun onSuccessUnlike(rowNumber: Int, columnNumber: Int) {
        onSuccessLikeUnlike(rowNumber, columnNumber)
    }

    override fun onErrorLikeUnlike(err: String) {
        showSnackbar(err)
    }

    override fun onAffiliateTrackClicked(trackList: MutableList<TrackingViewModel>, isClick: Boolean) {
        for (track in trackList) {
            presenter.trackAffiliate(
                    if (isClick) track.clickURL
                    else track.viewURL
            )
        }
    }

    override fun onHighlightItemClicked(positionInFeed: Int, item: HighlightCardViewModel) {
        feedAnalyticTracker.eventTrendingClickMedia(item.postId.toString())
        onGoToLink(item.applink)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            KOL_COMMENT_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    onSuccessAddDeleteKolComment(
                            data.getIntExtra(KolCommentActivity.ARGS_POSITION, -1),
                            data.getIntExtra(KolCommentActivity.ARGS_POSITION_COLUMN, -1),
                            data.getIntExtra(KolCommentFragment.ARGS_TOTAL_COMMENT, 0))
                }
            }
        }
    }

    private fun onGoToLink(url: String) {
        if (RouteManager.isSupportApplink(activity, url)) {
            RouteManager.route(activity, url)
        } else {
            RouteManager.route(
                    activity,
                    String.format("%s?url=%s", ApplinkConst.WEBVIEW, url)
            )
        }
    }

    private fun onSuccessLikeUnlike(rowNumber: Int, columnNumber: Int) {
        try {
            if (adapter.data.size > rowNumber
                    && adapter.data[rowNumber] != null
                    && adapter.data[rowNumber] is HighlightViewModel) {
                val model = adapter.data[rowNumber] as HighlightViewModel
                val like = model.cards[columnNumber].footer.like
                like.isChecked = !like.isChecked
                if (like.isChecked) {
                    try {
                        val likeValue = Integer.valueOf(like.fmt) + 1
                        like.fmt = likeValue.toString()
                    } catch (ignored: NumberFormatException) {
                    }

                    like.value = like.value + 1
                } else {
                    try {
                        val likeValue = Integer.valueOf(like.fmt) - 1
                        like.fmt = likeValue.toString()
                    } catch (ignored: NumberFormatException) {
                    }

                    like.value = like.value - 1
                }
                val payloads: MutableList<Int> = ArrayList()
                payloads.add(HighlightViewHolder.PAYLOAD_UPDATE_LIKE)
                payloads.add(columnNumber)
                adapter.notifyItemChanged(rowNumber, payloads)
            }
        } catch(e: Exception) {
            e.localizedMessage
        }
    }

    private fun onSuccessAddDeleteKolComment(rowNumber: Int, columnNumber: Int, totalNewComment: Int) {

        if (adapter.data.size > rowNumber &&
                adapter.data[rowNumber] != null &&
                adapter.data[rowNumber] is HighlightViewModel) {
            val comment = ((adapter.data[rowNumber]) as HighlightViewModel).cards[columnNumber].footer.comment
            try {
                val commentValue = Integer.valueOf(comment.fmt) + totalNewComment
                comment.fmt = commentValue.toString()
            } catch (ignored: NumberFormatException) {
            }

            comment.value = comment.value + totalNewComment
            val payloads: MutableList<Int> = ArrayList()
            payloads.add(HighlightViewHolder.PAYLOAD_UPDATE_COMMENT)
            payloads.add(columnNumber)
            adapter.notifyItemChanged(rowNumber, payloads)
        }
    }

    private fun showSnackbar(s: String) {
        NetworkErrorHelper.showSnackbar(activity, s)
    }
}