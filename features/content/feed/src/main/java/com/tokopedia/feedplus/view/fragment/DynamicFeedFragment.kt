package com.tokopedia.feedplus.view.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.feedcomponent.view.adapter.viewholder.highlight.HighlightAdapter
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.data.pojo.FeedTabs
import com.tokopedia.feedplus.view.adapter.typefactory.dynamicfeed.DynamicFeedTypeFactoryImpl
import com.tokopedia.feedplus.view.di.DaggerFeedPlusComponent
import com.tokopedia.feedplus.view.listener.DynamicFeedContract
import com.tokopedia.feedplus.view.presenter.DynamicFeedPresenter
import com.tokopedia.kol.KolComponentInstance
import com.tokopedia.user.session.UserSession
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

        val KEY_FEED_KEY = ""

        fun newInstance(feedKey: String): DynamicFeedFragment {
            val fragment = DynamicFeedFragment()
            val bundle = Bundle()
            bundle.putString(KEY_FEED_KEY, feedKey)
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var presenter: DynamicFeedContract.Presenter

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
        presenter.attachView(this)
        if (arguments != null) {
            feedKey = arguments?.getString(KEY_FEED_KEY) ?: ""
        }
        rv_dynamic_feed.adapter = adapter
        rv_dynamic_feed.layoutManager = LinearLayoutManager(activity)
    }

    private fun initViewListener() {

    }

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? {
        return swipe_refresh_layout
    }

    override fun onSwipeRefresh() {
        hideSnackBarRetry()
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActionRedirect(redirectUrl: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTitleCtaClick(redirectUrl: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSuccessDeletePost(rowNumber: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserSession(): UserSession {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onEmptyFeedButtonClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemClicked(t: Visitable<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getScreenName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLikeClick(positionInFeed: Int, id: Int, isLiked: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCommentClick(positionInFeed: Int, id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFooterActionClick(positionInFeed: Int, redirectUrl: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAffiliateTrackClicked(trackList: MutableList<TrackingViewModel>, isClick: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun showSnackbar(s: String) {
        NetworkErrorHelper.showSnackbar(activity, s)
    }
}