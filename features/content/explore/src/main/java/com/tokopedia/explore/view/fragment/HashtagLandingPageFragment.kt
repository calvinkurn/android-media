package com.tokopedia.explore.view.fragment

import android.net.Uri
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
import com.tokopedia.applink.UriUtil
import com.tokopedia.explore.R
import com.tokopedia.explore.di.ExploreComponent
import com.tokopedia.explore.domain.entity.PostKol
import com.tokopedia.explore.domain.entity.Tracking
import com.tokopedia.explore.view.activity.HashtagLandingPageActivity
import com.tokopedia.explore.view.adapter.HashtagLandingItemAdapter
import com.tokopedia.explore.view.uimodel.PostKolUiModel
import com.tokopedia.explore.view.viewmodel.HashtagLandingPageViewModel
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_content_hashtag_landing_page.*
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class HashtagLandingPageFragment : BaseDaggerFragment(), HashtagLandingItemAdapter.OnHashtagPostClick {

    private var searchTag: String = ""
    private var isInitialLoad: Boolean = true
    private var isFromContentDetailpage: Boolean = false
    private var sourceFromContentDetail: String = ""


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
            sourceFromContentDetail = it.getString(HashtagLandingPageActivity.CONTENT_DETAIL_PAGE_SOURCE, "")
            isFromContentDetailpage = it.getBoolean(HashtagLandingPageActivity.ARG_IS_FROM_CONTENT_DETAIL_PAGE, false)
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
        if (isInitialLoad) {
            when (throwable) {
                is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                    view?.let {
                        showGlobalError(GlobalError.NO_CONNECTION)
                    }
                }
                is RuntimeException -> {
                    when (throwable.localizedMessage?.toIntOrNull()) {
                        ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(
                            GlobalError.NO_CONNECTION
                        )
                        ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                        ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)

                        else -> {
                            view?.let {
                                showGlobalError(GlobalError.SERVER_ERROR)
                                Toaster.build(
                                    it,
                                    message,
                                    Toaster.LENGTH_SHORT,
                                    type = Toaster.TYPE_ERROR
                                ).show()
                            }
                        }
                    }
                }
                else -> {
                    view?.let {
                        showGlobalError(GlobalError.SERVER_ERROR)
                        Toaster.build(
                            it, throwable.message
                                ?: message, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR
                        ).show()
                    }
                }
            }
        } else {
            view?.let {
                Toaster.build(
                    it, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR,
                    getString(com.tokopedia.abstraction.R.string.retry_label)
                ) { loadData(false) }
            }?.show()
        }
    }

    private fun showGlobalError(type: Int) {
        error_hashtag?.setType(type)
        error_hashtag?.setActionClickListener {
            loadData(true)

        }
        error_hashtag?.show()
        recycler_view?.gone()
    }


    private fun onSuccessGetData(data: List<PostKolUiModel>) {
        recycler_view?.show()
        if (isInitialLoad)
            adapter.updateList(data)
        else
            adapter.addData(data)

        if (adapter.itemCount == 0) {
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
        val contentAppLink = UriUtil.buildUri(ApplinkConst.CONTENT_DETAIL, post.id)
        val finaAppLink = Uri.parse(contentAppLink)
            .buildUpon()
            .appendQueryParameter(SOURCE, HASHTAG_PAGE)
            .build().toString()

        activity?.let {
            RouteManager.route(it, finaAppLink)
        }
        if (isFromContentDetailpage)
            feedAnalytics.eventContentDetailHashtagPageClickThumbnail(
                post.id,
                searchTag,
                sourceFromContentDetail
            )
        else
            feedAnalytics.eventHashtagPageClickThumbnail(post.id, searchTag, position)

    }

    override fun onUserImageClick(post: PostKol) {
        /** uncomment this when the result of explore post could differentiate whether the creator is shop **/
        //activity?.let { RouteManager.route(it,
        // if (isShopPost)ApplinkConst.SHOP else ApplinkConst.PROFILE, post.userId.toString()) }
        feedAnalytics.eventHashtagPageClickNameAvatar(post.userId)
    }

    override fun onUserNameClick(post: PostKol) {
        /** uncomment this when the result of explore post could differentiate whether the creator is shop **/
        //activity?.let { RouteManager.route(it,
        // if (isShopPost)ApplinkConst.SHOP else ApplinkConst.PROFILE, post.userId.toString()) }
        feedAnalytics.eventHashtagPageClickNameAvatar(post.userId)
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
        private const val SOURCE = "source"
        private const val HASHTAG_PAGE = "hashtag_page"

        @JvmStatic
        fun createInstance(hashtag: String, isFromContentDetailPage: Boolean, sourceFromContentDetailPage: String): Fragment = HashtagLandingPageFragment().also {
            it.arguments = Bundle().apply {
                putString(ARG_HASHTAG, hashtag)
                putString(HashtagLandingPageActivity.CONTENT_DETAIL_PAGE_SOURCE, sourceFromContentDetailPage)
                putBoolean(HashtagLandingPageActivity.ARG_IS_FROM_CONTENT_DETAIL_PAGE, isFromContentDetailPage)
            }
        }
    }
}
