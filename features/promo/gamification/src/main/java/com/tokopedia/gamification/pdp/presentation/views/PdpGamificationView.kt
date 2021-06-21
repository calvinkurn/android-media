package com.tokopedia.gamification.pdp.presentation.views

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.gamification.R
import com.tokopedia.gamification.di.ActivityContextModule
import com.tokopedia.gamification.giftbox.analytics.GtmEvents
import com.tokopedia.gamification.giftbox.presentation.fragments.GiftBoxDailyFragment
import com.tokopedia.gamification.pdp.data.LiveDataResult
import com.tokopedia.gamification.pdp.data.Recommendation
import com.tokopedia.gamification.pdp.data.di.components.DaggerPdpComponent
import com.tokopedia.gamification.pdp.presentation.GamiPdpRecommendationListener
import com.tokopedia.gamification.pdp.presentation.adapters.PdpGamificationAdapter
import com.tokopedia.gamification.pdp.presentation.adapters.PdpGamificationAdapterTypeFactory
import com.tokopedia.gamification.pdp.presentation.viewmodels.PdpDialogViewModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class PdpGamificationView : LinearLayout {

    private val CONTAINER_LIST = 0
    private val CONTAINER_LOADING = 1

    private var spanCount = 2

    private lateinit var tvTitle: Typography
    lateinit var recyclerView: RecyclerView
    private lateinit var loadingView: LinearLayout
    private lateinit var viewFlipper: ViewFlipper
    var fragment: GiftBoxDailyFragment? = null

    private lateinit var adapter: PdpGamificationAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var dataList: ArrayList<Visitable<*>>
    var shopId = ""
    var userId: String? = null
    var errorListener: PdpErrorListener? = null


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: PdpDialogViewModel
    var listener: GamiPdpRecommendationListener? = null

    fun getLayout() = com.tokopedia.gamification.R.layout.dialog_pdp_gamification

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setupUi()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    ) {
        setupUi()
    }

    constructor(context: Context) : super(context) {
        setupUi()
    }

    private fun setupUi() {
        val v = LayoutInflater.from(context).inflate(getLayout(), this, true)
        orientation = LinearLayout.VERTICAL
        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        lp.gravity = Gravity.BOTTOM
        background = ContextCompat.getDrawable(context, R.drawable.gami_bottom_sheet_rounded_white)
        layoutParams = lp
        initViews(v)
    }

    private fun initViews(root: View) {

        dataList = ArrayList()

        injectComponents()

        recyclerView = root.findViewById(R.id.recyclerView)
        viewFlipper = root.findViewById(R.id.viewFlipper)
        tvTitle = root.findViewById(R.id.tvTitle)
        loadingView = root.findViewById(R.id.loadingView)
        viewFlipper.displayedChild = CONTAINER_LOADING

        prepareShimmer()
        setupRv()
        setListeners()

    }

    private fun setupRv() {
        listener = getRecommendationListener()
        val typeFactory = PdpGamificationAdapterTypeFactory(listener!!)
        adapter = PdpGamificationAdapter(dataList, typeFactory)
        spanCount = context.resources.getInteger(com.tokopedia.gamification.R.integer.gami_bottom_sheet_span_count)
        val layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                viewModel.getProducts(page)
            }

        }
        recyclerView.addOnScrollListener(scrollListener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        listener = null
    }


    private fun injectComponents() {
        val component = DaggerPdpComponent.builder()
                .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
                .activityContextModule(ActivityContextModule(context))
                .build()
        component.inject(this)


        if (context is AppCompatActivity) {
            val viewModelProvider = ViewModelProviders.of(context as AppCompatActivity, viewModelFactory)
            viewModel = viewModelProvider[PdpDialogViewModel::class.java]
        }
    }

    private fun setListeners() {

        viewModel.productLiveData.observe(context as AppCompatActivity, Observer {
            when (it.status) {
                LiveDataResult.STATUS.SUCCESS -> {

                    if (it.data != null) {
                        if (it.data.isNotEmpty()) {

                            if (viewFlipper.displayedChild != CONTAINER_LIST) {
                                viewFlipper.displayedChild = CONTAINER_LIST
                            }
                            val oldSize = dataList.size
                            val delay = if (oldSize == 0) 600L else 0L
                            val handler = Handler()
                            handler.postDelayed({
                                updateList(oldSize, it.data)
                            }, delay)
                        } else {
                            val oldSize = dataList.size
                            if (oldSize == 0 && it.data?.isNullOrEmpty()) {
                                viewModel.useEmptyShopId = true
                                viewModel.shopId = ""
                                viewModel.getProducts(0)
                            }
                        }
                    }
                }
                LiveDataResult.STATUS.ERROR -> {
                    hidePdp()
                }
            }
        })
    }

    fun hidePdp() {
        errorListener?.onError()
    }

    fun updateList(oldSize: Int, list: List<Recommendation>) {
        var isFirstLoad = false
        if(oldSize == 0 && list.isNotEmpty()){
            isFirstLoad = true
        }

        dataList.addAll(oldSize, list)
        adapter.notifyItemRangeInserted(oldSize, list.size)
        scrollListener.updateStateAfterGetData()
        sendPdpRecomImpressionEvent(isFirstLoad)
    }

    private fun sendPdpRecomImpressionEvent(isFirstLoad:Boolean){
        if(isFirstLoad) {
            GtmEvents.impressionProductRecom(userId)
        }
    }

    fun getRecommendationParams(pageName: String, shopId: String, isShopIdEmpty: Boolean) {
        this.shopId = shopId
        viewModel.shopId = shopId
        viewModel.pageName = pageName
        viewModel.useEmptyShopId = isShopIdEmpty
        viewModel.getProducts(0)
    }


    fun getRecommendationListener(): GamiPdpRecommendationListener {

        val listener = object : GamiPdpRecommendationListener {

            override fun onProductImpression(item: RecommendationItem, position: Int) {
                val productIdString: String = if (item.productId != null) {
                    item.productId.toString()
                } else {
                    ""
                }
                GtmEvents.impressionProductRecomItem(viewModel.userSession.userId,
                        productIdString,
                        item.recommendationType,
                        position,
                        "none / other",
                        item.categoryBreadcrumbs,
                        item.name,
                        "none / other",
                        item.price,
                        item.isTopAds)
            }

            override fun onProductClick(item: RecommendationItem, layoutType: String?, vararg position: Int) {

                val productIdString: String = if (item.productId != null) {
                    item.productId.toString()
                } else {
                    ""
                }
                GtmEvents.clickProductRecomItem(viewModel.userSession.userId,
                        productIdString,
                        item.recommendationType,
                        position[0],
                        "none / other",
                        item.categoryBreadcrumbs,
                        item.name,
                        "none / other",
                        item.price,
                        item.isTopAds)

                val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, item.productId.toString())
                if (position.isNotEmpty()) intent.putExtra(Wishlist.PDP_EXTRA_UPDATED_POSITION, position[0])
                fragment?.startActivityForResult(intent, Wishlist.REQUEST_FROM_PDP)
                fragment?.performAutoApply()
            }

            override fun onProductImpression(item: RecommendationItem) {

            }

            override fun onWishlistClick(item: RecommendationItem, isAddWishlist: Boolean, callback: (Boolean, Throwable?) -> Unit) {
                if (viewModel.userSession.isLoggedIn) {
                    if (isAddWishlist) {
                        viewModel.addToWishlist(item, callback)
                    } else {
                        viewModel.removeFromWishlist(item, callback)
                    }
                } else {
                    RouteManager.route(context, ApplinkConst.LOGIN)
                }

            }
        }

        return listener
    }

    private fun prepareShimmer() {
        (0..1).forEach { _ ->
            val v = LayoutInflater.from(context).inflate(R.layout.shimmer_pdp_gamification_new, null, false)
            loadingView.addView(v)
        }
    }

    fun onActivityResult(position: Int, wishListStatusFromPdp: Boolean) {
        if (adapter.list[position] is Recommendation) {
            val recommendation = adapter.list[position] as Recommendation
            recommendation.recommendationItem.isWishlist = wishListStatusFromPdp
            adapter.notifyItemChanged(position)
        }
    }
}

object Wishlist {
    const val PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition"
    const val PDP_WIHSLIST_STATUS_IS_WISHLIST = "isWishlist"
    const val REQUEST_FROM_PDP = 138
}

interface PdpErrorListener {
    fun onError()
}