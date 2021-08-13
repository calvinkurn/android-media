package com.tokopedia.favorite.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.favorite.R
import com.tokopedia.favorite.di.component.DaggerFavoriteComponent
import com.tokopedia.favorite.view.adapter.FavoriteAdapter
import com.tokopedia.favorite.view.adapter.FavoriteAdapterTypeFactory
import com.tokopedia.favorite.view.adapter.TopAdsShopAdapter
import com.tokopedia.favorite.view.viewlistener.FavoriteClickListener
import com.tokopedia.favorite.view.viewmodel.FavoriteShopUiModel
import com.tokopedia.favorite.view.viewmodel.TopAdsShopItem
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashSet

/**
 * @author Kulomady on 1/20/17.
 */
class FragmentFavorite() : BaseDaggerFragment(), FavoriteClickListener, OnRefreshListener, TopAdsShopAdapter.ImpressionImageLoadedListener {

    companion object {
        val TAG = FragmentFavorite::class.java.simpleName
        private val LOGIN_STATUS = "logged_in_status"
        private val IS_FAVORITE_EMPTY = "is_favorite_empty"
        private val OPEN_FAVORITE = "Favorite_Screen_Launched"
        private val SCREEN_UNIFY_HOME_SHOP_FAVORIT = "/fav-shop"
        private val DURATION_ANIMATOR: Long = 1000
        private val FAVORITE_TRACE = "mp_favourite_shop"
        private val FAVORITE_SHOP_SCREEN_NAME = "/favorite"
        val SCREEN_PARAM_IS_LOGGED_IN_STATUS = "isLoggedInStatus"
        val SCREEN_PARAM_IS_FAVOURITE_EMPTY = "isFavouriteEmpty"

        //value is logged in is always true, because favorite shop can only be open from
        //account fragment, which is only logged in user that can access that tab
        val VALUE_IS_LOGGED_IN_FAVORITE_SHOP = "true"
        fun newInstance(): Fragment {
            return FragmentFavorite()
        }
    }

    private var isUserEventTrackerDoneTrack = false
    lateinit var recyclerView: RecyclerView
    lateinit var swipeToRefresh: SwipeToRefresh
    lateinit var progressBar: ProgressBar
    lateinit var mainContent: RelativeLayout
    private var wishlistNotLoggedIn: View? = null
    private var btnLogin: Button? = null

    @JvmField
    @Inject
    var viewModel: FavoriteViewModel? = null

    private var favoriteAdapter: FavoriteAdapter? = null
    private lateinit var recylerviewScrollListener: EndlessRecyclerViewScrollListener
    private var messageSnackbar: SnackbarRetry? = null
    private var isNetworkFailed = false
    private var favoriteShopViewSelected: View? = null
    private var shopItemSelected: TopAdsShopItem? = null
    private var performanceMonitoring: PerformanceMonitoring? = null
    private var userSession: UserSessionInterface? = null
    private val alreadyHitImpressionUrls = HashSet<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        userSession = UserSession(context)
        performanceMonitoring = PerformanceMonitoring.start(FAVORITE_TRACE)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val parentView = inflater.inflate(
                R.layout.favorite_fragment_index_favorite_v2, container, false)
        recyclerView = parentView.findViewById(R.id.index_favorite_recycler_view)
        swipeToRefresh = parentView.findViewById(R.id.swipe_refresh_layout)
        progressBar = parentView.findViewById(R.id.include_loading)
        mainContent = parentView.findViewById(R.id.main_content)
        wishlistNotLoggedIn = parentView.findViewById(R.id.partial_empty_wishlist)
        btnLogin = parentView.findViewById(R.id.btn_login)

        val isLoggedIn = (userSession?.isLoggedIn) ?: false
        if (isLoggedIn) {
            prepareView()
            viewModel!!.loadInitialData()
        } else {
            wishlistNotLoggedIn?.setVisibility(View.VISIBLE)
            mainContent.setVisibility(View.GONE)
        }
        return parentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnLogin?.setOnClickListener {
            val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
            startActivity(intent)
        }

        viewModel!!.refresh.observe(viewLifecycleOwner, Observer { isRefreshing: Boolean ->
            if (isRefreshing) {
                showRefreshLoading()
            } else {
                hideRefreshLoading()
            }
        })

        viewModel!!.isErrorLoad.observe(viewLifecycleOwner, Observer { isErrorLoad: Boolean ->
            if (isErrorLoad) {
                showErrorLoadData()
            }
        })

        viewModel!!.initialData.observe(viewLifecycleOwner,
                Observer<List<Visitable<*>>> { data: List<Visitable<*>> ->
                    val visitables: MutableList<Visitable<*>> = ArrayList();
                    visitables.addAll(data);
                    showInitialDataPage(visitables);
                    validateMessageError();
                    stopTracePerformanceMonitoring()
                }
        )
        viewModel!!.isNetworkFailed.observe(viewLifecycleOwner, Observer { isFailed: Boolean ->
            isNetworkFailed = isFailed
            validateMessageError()
        })
        viewModel!!.isLoadingFavoriteShop.observe(viewLifecycleOwner, Observer { isLoading: Boolean ->
            if (isLoading) {
                showLoadMoreLoading()
            } else {
                stopLoadingFavoriteShop()
            }
        })
        viewModel!!.isErrorAddFavoriteShop.observe(viewLifecycleOwner, Observer { isError: Boolean ->
            if (isError) {
                showErrorAddFavoriteShop()
            }
        })
        viewModel!!.addedFavoriteShop.observe(viewLifecycleOwner, Observer { favoriteShop: FavoriteShopUiModel -> addFavoriteShop(favoriteShop) })
        viewModel!!.isErrorLoadMore.observe(viewLifecycleOwner, Observer { isError: Boolean ->
            if (isError) {
                showErrorLoadMore()
            }
        })
        viewModel!!.moreDataFavoriteShop.observe(viewLifecycleOwner,
                Observer<List<Visitable<*>>> { data: List<Visitable<*>> ->
                    val visitables: MutableList<Visitable<*>> = ArrayList();
                    visitables.addAll(data);
                    showMoreDataFavoriteShop(visitables)
                }
        )
        viewModel!!.favoriteShopImpression.observe(viewLifecycleOwner, Observer {
            clickUrl: String -> sendFavoriteShopImpression(clickUrl)
        })
        viewModel!!.refreshData.observe(viewLifecycleOwner,
                Observer<List<Visitable<*>>> { data: List<Visitable<*>> ->
                    val visitables: MutableList<Visitable<*>> = ArrayList();
                    visitables.addAll(data);
                    refreshDataFavorite(visitables);
                    hideRefreshLoading();
                    validateMessageError()
                }
        )
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        try {
            if (isVisibleToUser && isAdded && (activity != null)) {
                if (isAdapterNotEmpty) {
                    validateMessageError()
                } else {
                    viewModel!!.loadInitialData()
                }
                TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
            } else {
                if (messageSnackbar != null && messageSnackbar!!.isShown) {
                    messageSnackbar!!.hideRetrySnackbar()
                }
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
            onCreate(Bundle())
        }
    }

    private fun validateMessageError() {
        if (messageSnackbar != null) {
            if (isNetworkFailed) {
                messageSnackbar!!.showRetrySnackbar()
            } else {
                messageSnackbar!!.hideRetrySnackbar()
            }
        }
    }

    private fun showErrorAddFavoriteShop() {
        NetworkErrorHelper.createSnackbarWithAction(activity) {
            val viewSelected = favoriteShopViewSelected
            if (viewSelected != null && shopItemSelected != null) {
                viewSelected.isEnabled = false
                viewModel!!.addFavoriteShop(viewSelected, shopItemSelected!!)
            }
        }.showRetrySnackbar()
    }

    private fun stopLoadingFavoriteShop() {
        favoriteAdapter!!.hideLoading()
        updateEndlessRecyclerViewListener()
    }

    override fun initInjector() {
        val daggerFavoriteComponent = DaggerFavoriteComponent.builder()
                .baseAppComponent(baseAppComponent)
                .build()
        daggerFavoriteComponent.inject(this)
    }

    val baseAppComponent: BaseAppComponent
        get() = (requireActivity().application as BaseMainApplication).baseAppComponent

    override fun getScreenName(): String {
        return SCREEN_UNIFY_HOME_SHOP_FAVORIT
    }

    private fun refreshDataFavorite(elementList: List<Visitable<*>>) {
        favoriteAdapter!!.hideLoading()
        favoriteAdapter!!.clearData()
        favoriteAdapter!!.setElement(elementList)
        updateEndlessRecyclerViewListener()
    }

    private fun showInitialDataPage(dataFavorite: List<Visitable<*>>) {
        sendFavoriteShopScreenTracker(dataFavorite.isEmpty())
        favoriteAdapter!!.hideLoading()
        favoriteAdapter!!.clearData()
        favoriteAdapter!!.setElement(dataFavorite)
        updateEndlessRecyclerViewListener()
        val value = DataLayer.mapOf(
                LOGIN_STATUS, userSession!!.isLoggedIn,
                IS_FAVORITE_EMPTY, dataFavorite.isEmpty()
        )
        TrackApp.getInstance().moEngage.sendTrackEvent(value, OPEN_FAVORITE)
    }

    private fun showMoreDataFavoriteShop(elementList: List<Visitable<*>>) {
        favoriteAdapter!!.hideLoading()
        favoriteAdapter!!.addMoreData(elementList)
        updateEndlessRecyclerViewListener()
    }

    private fun showRefreshLoading() {
        swipeToRefresh.isRefreshing = true
    }

    private fun hideRefreshLoading() {
        swipeToRefresh.isRefreshing = false
        recylerviewScrollListener.resetState()
    }

    private fun stopTracePerformanceMonitoring() {
        performanceMonitoring!!.stopTrace()
    }

    private fun showErrorLoadMore() {
        NetworkErrorHelper.createSnackbarWithAction(
                activity,
                {
                    if (!isLoading) {
                        viewModel!!.loadMoreFavoriteShop()
                    }
                }).showRetrySnackbar()
        favoriteAdapter!!.hideLoading()
        updateEndlessRecyclerViewListener()
    }

    private fun showErrorLoadData() {
        if (isAdapterNotEmpty) {
            validateMessageError()
        } else {
            NetworkErrorHelper.showEmptyState(context,
                    mainContent,
                    { viewModel!!.refreshAllDataFavoritePage() })
        }
    }

    private val isLoading: Boolean
        get() = favoriteAdapter!!.isLoading

    private fun showLoadMoreLoading() {
        favoriteAdapter!!.showLoading()
    }

    private fun addFavoriteShop(shopUiModel: FavoriteShopUiModel) {
        val favoriteShopPosition = 2
        favoriteAdapter?.addElement(favoriteShopPosition, shopUiModel)
    }

    private fun sendFavoriteShopImpression(clickUrl: String) {
        ImpresionTask(activity?.localClassName, userSession).execute(clickUrl)
    }

    override fun onRefresh() {
        viewModel!!.refreshAllDataFavoritePage()
    }

    override fun onFavoriteShopClicked(view: View?, shopItemSelected: TopAdsShopItem?) {
        favoriteShopViewSelected = view
        this.shopItemSelected = shopItemSelected
        favoriteShopViewSelected?.isEnabled = false
        viewModel!!.addFavoriteShop(favoriteShopViewSelected!!, this.shopItemSelected!!)
    }

    private fun prepareView() {
        initRecyclerview()
        swipeToRefresh.setOnRefreshListener(this)
        messageSnackbar = NetworkErrorHelper.createSnackbarWithAction(activity,
                { viewModel!!.refreshAllDataFavoritePage() })
    }

    private fun initRecyclerview() {
        val typeFactoryForList = FavoriteAdapterTypeFactory(this, this)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        favoriteAdapter = FavoriteAdapter(typeFactoryForList, ArrayList())
        val animator = DefaultItemAnimator()
        animator.addDuration = DURATION_ANIMATOR
        recylerviewScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                viewModel!!.loadMoreFavoriteShop()
            }
        }
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = animator
        recyclerView.adapter = favoriteAdapter
        recyclerView.addOnScrollListener(recylerviewScrollListener)
    }

    private val isAdapterNotEmpty: Boolean
        get() = favoriteAdapter!!.itemCount > 0

    private fun sendFavoriteShopScreenTracker(isFavouriteEmpty: Boolean) {
        if (!isUserEventTrackerDoneTrack) {
            val customDimensions = HashMap<String, String>()
            customDimensions[SCREEN_PARAM_IS_LOGGED_IN_STATUS] = VALUE_IS_LOGGED_IN_FAVORITE_SHOP
            customDimensions[SCREEN_PARAM_IS_FAVOURITE_EMPTY] = isFavouriteEmpty.toString()
            TrackApp.getInstance().gtm.sendScreenAuthenticated(
                    FAVORITE_SHOP_SCREEN_NAME,
                    customDimensions
            )
            isUserEventTrackerDoneTrack = true
        }
    }

    private fun updateEndlessRecyclerViewListener() {
        recylerviewScrollListener.updateStateAfterGetData()
        recylerviewScrollListener.setHasNextPage(viewModel!!.hasNextPage())
    }

    override fun onImageLoaded(
            className: String?,
            url: String,
            productId: String?,
            productName: String?,
            imageUrl: String?
    ) {
        if (!alreadyHitImpressionUrls.contains(url)) {
            alreadyHitImpressionUrls.add(url)
            TopAdsUrlHitter(context).hitImpressionUrl(className, url, productId, productName, imageUrl)
        }
    }

}
