package com.tokopedia.explore.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.affiliatecommon.data.util.AffiliatePreference
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.coachmark.CoachMark
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.explore.R
import com.tokopedia.explore.analytics.ContentExloreEventTracking
import com.tokopedia.explore.analytics.ContentExploreAnalytics
import com.tokopedia.explore.di.DaggerExploreComponent
import com.tokopedia.explore.view.adapter.ExploreCategoryAdapter
import com.tokopedia.explore.view.adapter.ExploreImageAdapter
import com.tokopedia.explore.view.adapter.factory.ExploreImageTypeFactoryImpl
import com.tokopedia.explore.view.listener.ContentExploreContract
import com.tokopedia.explore.view.uimodel.ExploreCategoryViewModel
import com.tokopedia.explore.view.uimodel.ExploreImageViewModel
import com.tokopedia.explore.view.uimodel.ExploreViewModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by milhamj on 19/07/18.
 */

class ContentExploreFragment :
        BaseDaggerFragment(),
        ContentExploreContract.View,
        SwipeRefreshLayout.OnRefreshListener {

    companion object {

        const val PARAM_CATEGORY_ID = "category_id"
        private const val DEFAULT_CATEGORY = "0"
        private const val PEFORMANCE_EXPLORE = "mp_explore"
        private const val CATEGORY_POSITION_NONE = -1

        private const val IMAGE_SPAN_COUNT = 3
        private const val IMAGE_SPAN_SINGLE = 1
        private const val LOAD_MORE_THRESHOLD = 2

        @JvmStatic
        fun newInstance(bundle: Bundle?): ContentExploreFragment {
            val fragment = ContentExploreFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val isLoading: Boolean
        get() = imageAdapter.isLoading || swipeToRefresh.isRefreshing

    @Inject
    lateinit var presenter: ContentExploreContract.Presenter
    @Inject
    lateinit var categoryAdapter: ExploreCategoryAdapter
    @Inject
    lateinit var imageAdapter: ExploreImageAdapter
    @Inject
    lateinit var affiliatePreference: AffiliatePreference
    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var analytics: ContentExploreAnalytics

    private lateinit var exploreCategoryRv: RecyclerView
    private lateinit var exploreImageRv: RecyclerView
    private lateinit var swipeToRefresh: SwipeToRefresh
    private lateinit var appBarLayout: View
    private lateinit var scrollListener: RecyclerView.OnScrollListener
    private lateinit var performanceMonitoring: PerformanceMonitoring
    private lateinit var coachMark: CoachMark

    private var categoryId: Int = 0
    private var canLoadMore: Boolean = false
    private var hasLoadedOnce: Boolean = false
    private var isTraceStopped: Boolean = false
    private var coachMarkItemList: MutableList<CoachMarkItem> = arrayListOf()

    override fun getScreenName(): String {
        return ContentExloreEventTracking.Screen.SCREEN_CONTENT_STREAM
    }

    override fun initInjector() {
        val baseAppComponent = (requireContext().applicationContext as BaseMainApplication).baseAppComponent
        DaggerExploreComponent.builder()
                .baseAppComponent(baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_content_explore, container, false)
        exploreCategoryRv = view.findViewById(R.id.explore_category_rv)
        exploreImageRv = view.findViewById(R.id.explore_image_rv)
        swipeToRefresh = view.findViewById(R.id.swipe_refresh_layout)
        appBarLayout = view.findViewById(R.id.app_bar_layout)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GraphqlClient.init(requireContext())
        initVar()
        initView()
        presenter.attachView(this)
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        loadData()
    }

    private fun initView() {
        dropKeyboard()
        swipeToRefresh.setOnRefreshListener(this)

        val linearLayoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL,
                false)
        exploreCategoryRv.layoutManager = linearLayoutManager
        categoryAdapter = ExploreCategoryAdapter()
        categoryAdapter.listener = this
        exploreCategoryRv.adapter = categoryAdapter

        val gridLayoutManager = GridLayoutManager(context,
                IMAGE_SPAN_COUNT,
                GridLayoutManager.VERTICAL,
                false)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when {
                    imageAdapter.list[position] is ExploreImageViewModel -> {
                        IMAGE_SPAN_SINGLE
                    }
                    imageAdapter.list[position] is LoadingMoreModel -> {
                        IMAGE_SPAN_COUNT
                    }
                    imageAdapter.list[position] is EmptyModel -> {
                        IMAGE_SPAN_COUNT
                    }
                    else -> 0
                }
            }
        }
        exploreImageRv.layoutManager = gridLayoutManager
        exploreImageRv.addOnScrollListener(onScrollListener(gridLayoutManager))
        val typeFactory = ExploreImageTypeFactoryImpl(this)
        imageAdapter.setTypeFactory(typeFactory)
        exploreImageRv.adapter = imageAdapter
    }

    private fun initVar() {
        if (arguments != null) {
            categoryId = Integer.valueOf(arguments!!.getString(
                    PARAM_CATEGORY_ID,
                    DEFAULT_CATEGORY)
            )
            presenter.updateCategoryId(categoryId)
        }
    }

    private fun loadData() {
        if (userVisibleHint && isAdded && activity != null && ::presenter.isInitialized) {
            if (!hasLoadedOnce) {
                performanceMonitoring = PerformanceMonitoring.start(PEFORMANCE_EXPLORE)
                presenter.getExploreData(true)
                hasLoadedOnce = !hasLoadedOnce
            }
            analytics.sendScreenName(screenName)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onSuccessGetExploreData(exploreViewModel: ExploreViewModel, clearData: Boolean) {
        analytics.eventImpressionSuccessGetData()

        if (exploreViewModel.exploreImageViewModelList.isNotEmpty()) {
            loadImageData(exploreViewModel.exploreImageViewModelList)
        } else if (clearData) {
            showEmpty()
        }

        if (categoryAdapter.list.isEmpty()) {
            loadTagData(exploreViewModel.tagViewModelList)
        }

        var isCategoryExist = false
        for (i in 0 until categoryAdapter.list.size) {
            val categoryViewModel = categoryAdapter.list[i]
            if (categoryViewModel.id == categoryId) {
                categoryViewModel.isActive = true
                categoryAdapter.notifyItemChanged(i)
                exploreCategoryRv.scrollToPosition(i)
                isCategoryExist = true
                break
            }
        }
        if (!isCategoryExist && categoryId != Integer.valueOf(DEFAULT_CATEGORY)) {
            onCategoryReset()
        }
    }

    override fun onErrorGetExploreDataFirstPage(message: String) {
        NetworkErrorHelper.showEmptyState(context, view) { presenter.getExploreData(true) }
    }

    override fun onErrorGetExploreDataMore() {
        canLoadMore = false
        NetworkErrorHelper.createSnackbarWithAction(activity) { presenter.getExploreData(false) }.showRetrySnackbar()
    }

    override fun updateCursor(cursor: String) {
        canLoadMore = cursor.isNotEmpty()
        presenter.updateCursor(cursor)
    }

    override fun updateCategoryId(categoryId: Int) {
        this.categoryId = categoryId
        presenter.updateCategoryId(categoryId)
    }

    override fun updateSearch(search: String) {
        presenter.updateSearch(search)
    }

    override fun clearData() {
        imageAdapter.clearData()
    }

    override fun onCategoryClicked(position: Int, categoryId: Int, categoryName: String, view: View) {
        NetworkErrorHelper.removeEmptyState(view)
        clearSearch()
        resetDataParam()
        imageAdapter.clearData()
        val isSameCategory = setAllCategoriesInactive(position)
        if (isSameCategory) {
            updateCategoryId(0)
            analytics.eventDeselectCategory(categoryName)

        } else {
            updateCategoryId(categoryId)
            analytics.eventSelectCategory(categoryName)

            if (position > 0) {
                categoryAdapter.list[position].isActive = true
                categoryAdapter.notifyItemChanged(position)
            }
            if (categoryId == ExploreCategoryAdapter.CAT_ID_AFFILIATE && !affiliatePreference.isCoachmarkExploreAsAffiliateShown(userSession.userId)) {
                view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        val originalPost = IntArray(2)
                        view.getLocationOnScreen(originalPost)
                        val xpos2 = originalPost[0] + view.width
                        val ypos2 = originalPost[1] + view.height
                        val arrayList = intArrayOf(originalPost[0], originalPost[1], xpos2, ypos2)
                        val coachMarkItem = CoachMarkItem(
                                view,
                                activity!!.resources.getString(R.string.coachmark_explore_title_1),
                                activity!!.resources.getString(R.string.coachmark_explore_content_1)
                        ).withCustomTarget(arrayList)
                        coachMarkItemList.add(coachMarkItem)
                    }
                })
            }
        }
        presenter.getExploreData(true)
    }

    override fun onCategoryReset() {
        onCategoryClicked(CATEGORY_POSITION_NONE, Integer.valueOf(DEFAULT_CATEGORY), "", View(activity))
    }

    override fun showRefreshing() {
        swipeToRefresh.isRefreshing = true
    }

    override fun showLoading() {
        if (!isLoading) {
            imageAdapter.showLoading()
        }
    }

    override fun dismissLoading() {
        if (isLoading) {
            imageAdapter.dismissLoading()
        }
        if (swipeToRefresh.isRefreshing) {
            swipeToRefresh.isRefreshing = false
        }
    }

    override fun showEmpty() {
        imageAdapter.showEmpty()
    }

    override fun goToKolPostDetail(postId: Int, name: String, recomId: Int) {
        RouteManager.route(
                requireContext(),
                ApplinkConst.CONTENT_DETAIL,
                postId.toString()
        )
        analytics.eventTrackExploreItem(name, postId, recomId)
    }

    override fun addExploreItemCoachmark(view: View) {
        if (!affiliatePreference.isCoachmarkExploreAsAffiliateShown(userSession.userId)) {
            view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val originalPost = IntArray(2)
                    view.getLocationOnScreen(originalPost)
                    val xpos2 = originalPost[0] + view.width
                    val ypos2 = originalPost[1] + view.height
                    val arrayList = intArrayOf(originalPost[0], originalPost[1], xpos2, ypos2)
                    val coachMarkItem = CoachMarkItem(
                            view,
                            activity!!.resources.getString(R.string.coachmark_explore_title_2),
                            activity!!.resources.getString(R.string.coachmark_explore_content_2)
                    ).withCustomTarget(arrayList)
                    coachMarkItemList.add(coachMarkItem)
                    showCoachMark()
                }

            })
        }
    }

    fun showCoachMark() {
        coachMark = CoachMarkBuilder().build()
        coachMark.show(activity, String.format(AffiliatePreference.LABEL_TAG_COACHMARK_CATEGORY, userSession.userId), ArrayList(coachMarkItemList))
        affiliatePreference.setCoachmarkExploreAsAffiliateShown(userSession.userId)
    }

    override fun onRefresh() {
        presenter.onPullToRefreshTriggered()
        presenter.updateCursor("")
        presenter.getExploreData(true)
    }

    override fun dropKeyboard() {
        KeyboardHandler.DropKeyboard(activity, view)
    }

    override fun scrollToTop() {
        if (::exploreImageRv.isInitialized) {
            exploreImageRv.scrollToPosition(0)
        }
    }

    override fun resetDataParam() {
        updateSearch("")
        updateCursor("")
        updateCategoryId(0)
    }

    override fun getExploreCategory(): Int {
        return categoryId
    }

    override fun stopTrace() {
        if (::performanceMonitoring.isInitialized && !isTraceStopped) {
            performanceMonitoring.stopTrace()
            isTraceStopped = true
        }
    }

    override fun onAffiliateTrack(trackingList: List<TrackingViewModel>, isClick: Boolean) {
        if (isClick) {
            for (tracking in trackingList) {
                presenter.trackAffiliate(tracking.clickURL)
            }
        } else {
            for (tracking in trackingList) {
                presenter.appendImpressionTracking(tracking.viewURL)
            }
        }
    }

    private fun loadImageData(exploreImageViewModelList: List<ExploreImageViewModel>) {
        imageAdapter.addList(exploreImageViewModelList)
    }

    private fun loadTagData(tagViewModelList: MutableList<ExploreCategoryViewModel>) {
        categoryAdapter.setList(tagViewModelList)
        appBarLayout.visibility = View.VISIBLE
    }

    private fun clearSearch() {
        dropKeyboard()
    }

    private fun setAllCategoriesInactive(position: Int): Boolean {
        var isSameCategory = false
        for (i in 0 until categoryAdapter.list.size) {
            val categoryViewModel = categoryAdapter.list[i]
            if (categoryViewModel.isActive) {
                categoryViewModel.isActive = false
                categoryAdapter.notifyItemChanged(i)
                if (i == position) {
                    isSameCategory = true
                }
                break
            }
        }
        return isSameCategory
    }

    private fun onScrollListener(layoutManager: GridLayoutManager): RecyclerView.OnScrollListener {
        if (!::scrollListener.isInitialized) {
            scrollListener = object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    val visibleThreshold = LOAD_MORE_THRESHOLD * layoutManager.spanCount
                    if (lastVisibleItemPosition + visibleThreshold > imageAdapter.itemCount
                            && canLoadMore
                            && !isLoading) {
                        presenter.getExploreData(false)
                    }
                }
            }
        }
        return scrollListener
    }
}
