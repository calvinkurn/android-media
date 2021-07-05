package com.tokopedia.explore.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.explore.R
import com.tokopedia.explore.di.ExploreComponent
import com.tokopedia.explore.domain.entity.PostKol
import com.tokopedia.explore.domain.entity.Tracking
import com.tokopedia.explore.view.adapter.HashtagLandingItemAdapter
import com.tokopedia.explore.view.uimodel.PostKolUiModel
import com.tokopedia.explore.view.viewmodel.HashtagLandingPageViewModel
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_content_hashtag_landing_page.*
import javax.inject.Inject

class HashtagLandingPageFragment : BaseDaggerFragment(), HashtagLandingItemAdapter.OnHashtagPostClick {

    private var searchTag: String = ""
    private var isInitialLoad: Boolean = true

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: HashtagLandingPageViewModel

    @Inject
    lateinit var feedAnalytics: FeedAnalyticTracker

    private val layoutManager: StaggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(GRID_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
    }

    private val endlessScrollListener: EndlessRecyclerViewScrollListener by lazy {
        object : EndlessRecyclerViewScrollListener(layoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (viewModel.canLoadMore){
                    isInitialLoad = false
                    loadData()
                }
            }
        }
    }

    private val adapter: HashtagLandingItemAdapter by lazy { HashtagLandingItemAdapter(this) }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(ExploreComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HashtagLandingPageViewModel::class.java)
        arguments?.let {
            searchTag = it.getString(ARG_HASHTAG, "")
            viewModel.hashtag = searchTag
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getPostResponse().observe(viewLifecycleOwner, Observer {
            hideLoading()
            endlessScrollListener.setHasNextPage(viewModel.canLoadMore)
            endlessScrollListener.updateStateAfterGetData()
            when(it){
                is Success -> onSuccessGetData(it.data)
                is Fail -> onFailGetData(it.throwable)
            }
        })
    }

    private fun hideLoading() {
        if (swipe_refresh.isRefreshing){
            swipe_refresh.isRefreshing = false
            swipe_refresh.isEnabled = true
        }
        adapter.hideLoadMore()
    }

    private fun onFailGetData(throwable: Throwable) {
        val message = ErrorHandler.getErrorMessage(context, throwable)
        if (isInitialLoad){
            adapter.showError(message){ loadData(true) }
        } else {
            view?.let { Toaster.build(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR,
                    getString(R.string.retry_label), View.OnClickListener { loadData(false) }) }?.show()
        }
    }

    private fun onSuccessGetData(data: List<PostKolUiModel>) {
        if (isInitialLoad)
            adapter.updateList(data)
        else
            adapter.addData(data)

        if (adapter.itemCount == 0){
            adapter.showEmpty()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_content_hashtag_landing_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? BaseSimpleActivity)?.updateTitle(searchTag)

        swipe_refresh.setOnRefreshListener {
            endlessScrollListener.resetState()
            isInitialLoad = true
            loadData(true)
        }

        recycler_view.layoutManager = layoutManager
        recycler_view.addOnScrollListener(endlessScrollListener)
        recycler_view.adapter = adapter
        loadData()

        feedAnalytics.eventOpenHashtagScreen()
    }

    override fun onImageClick(post: PostKol, position: Int) {
        activity?.let { RouteManager.route(it, ApplinkConst.CONTENT_DETAIL, post.id.toString()) }
        feedAnalytics.eventHashtagPageClickThumbnail(post.id.toString(), searchTag, position)
    }

    override fun onUserImageClick(post: PostKol) {
        /** uncomment this when the result of explore post could differentiate whether the creator is shop **/
        //activity?.let { RouteManager.route(it,
        // if (isShopPost)ApplinkConst.SHOP else ApplinkConst.PROFILE, post.userId.toString()) }
        feedAnalytics.eventHashtagPageClickNameAvatar(post.userId.toString())
    }

    override fun onUserNameClick(post: PostKol) {
        /** uncomment this when the result of explore post could differentiate whether the creator is shop **/
        //activity?.let { RouteManager.route(it,
        // if (isShopPost)ApplinkConst.SHOP else ApplinkConst.PROFILE, post.userId.toString()) }
        feedAnalytics.eventHashtagPageClickNameAvatar(post.userId.toString())
    }

    override fun onImageFirstTimeSeen(post: PostKol, position: Int) {
        feedAnalytics.eventHashtagPageViewPost(post.id.toString(), searchTag, position)
    }

    override fun onAffiliateTrack(trackingList: List<Tracking>, isClick: Boolean) {
        trackingList.forEach { tracking ->
            viewModel.trackAffiliate(
                    if (isClick) tracking.clickURL
                    else tracking.viewURL
            )
        }
    }

    override fun onPause() {
        super.onPause()
        feedAnalytics.sendPendingAnalytics()
    }

    private fun loadData(isForceRefresh: Boolean = false){
        adapter.hideError()
        if (!isForceRefresh && isLoadMore){
            adapter.showLoadMore()
        } else {
            if (!swipe_refresh.isRefreshing)
                swipe_refresh.isRefreshing = true
        }
        viewModel.getContentByHashtag(isForceRefresh)
    }

    private val isLoadMore: Boolean
        get() = adapter.itemCount > 0

    companion object{
        const val ARG_HASHTAG = "arg_hashtag"
        const val GRID_SPAN_COUNT = 2

        @JvmStatic
        fun createInstance(hashtag: String): Fragment = HashtagLandingPageFragment().also {
            it.arguments = Bundle().apply { putString(ARG_HASHTAG, hashtag) }
        }
    }
}