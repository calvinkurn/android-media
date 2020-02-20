package com.tokopedia.gamification.pdp.presentation.views

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
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
import com.tokopedia.gamification.cracktoken.fragment.CrackTokenFragment
import com.tokopedia.gamification.pdp.data.LiveDataResult
import com.tokopedia.gamification.pdp.data.Recommendation
import com.tokopedia.gamification.pdp.data.di.components.DaggerPdpComponent
import com.tokopedia.gamification.pdp.presentation.*
import com.tokopedia.gamification.pdp.presentation.adapters.PdpGamificationAdapter
import com.tokopedia.gamification.pdp.presentation.adapters.PdpGamificationAdapterTypeFactory
import com.tokopedia.gamification.pdp.presentation.viewmodels.PdpDialogViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.TrackApp
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class PdpGamificationView : FrameLayout {

    private val CONTAINER_LIST = 0
    private val CONTAINER_LOADING = 1
    private val CONTAINER_ERROR = 2

    private lateinit var PAGE_NAME: String
    private var DEFAULT_SPAN_COUNT = 2

    private lateinit var tvTitle: Typography
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingView: LinearLayout
    private lateinit var viewFlipper: ViewFlipper
    private lateinit var globalError: GlobalError
    var fragment: CrackTokenFragment? = null

    private lateinit var adapter: PdpGamificationAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var dataList: ArrayList<Visitable<*>>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: PdpDialogViewModel
    var listener: GamiPdpRecommendationListener? = null

    fun getLayout() = R.layout.dialog_pdp_gamification

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
        initViews(v)
    }

    private fun initViews(root: View) {

        dataList = ArrayList()
        PAGE_NAME = "gamepage"

        injectComponents()

        recyclerView = root.findViewById(R.id.recyclerView)
        viewFlipper = root.findViewById(R.id.viewFlipper)
        tvTitle = root.findViewById(R.id.tvTitle)
        globalError = root.findViewById(R.id.globalError)
        loadingView = root.findViewById(R.id.loadingView)
        globalError.setType(GlobalError.SERVER_ERROR)
        viewFlipper.displayedChild = CONTAINER_LOADING

        prepareShimmer()
        setupRv()
        setListeners()
        getRecommendationParams()

    }

    private fun setupRv() {
        listener = getRecommendationListener()
        val typeFactory = PdpGamificationAdapterTypeFactory(listener!!)
        adapter = PdpGamificationAdapter(dataList, typeFactory)
        val layoutManager = StaggeredGridLayoutManager(DEFAULT_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
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
                .build()
        component.inject(this)


        if (context is AppCompatActivity) {
            val viewModelProvider = ViewModelProviders.of(context as AppCompatActivity, viewModelFactory)
            viewModel = viewModelProvider[PdpDialogViewModel::class.java]
        }
    }

    private fun setListeners() {

        viewModel.titleLiveData.observe(context as AppCompatActivity, Observer {
            when (it.status) {
                LiveDataResult.STATUS.SUCCESS -> {
                    if (!TextUtils.isEmpty(it.data)) {
                        tvTitle.text = it.data
                    }
                }
            }
        })

        viewModel.productLiveData.observe(context as AppCompatActivity, Observer {
            when (it.status) {
                LiveDataResult.STATUS.SUCCESS -> {
                    if (it.data != null && it.data.isNotEmpty()) {
                        val oldSize = dataList.size
                        dataList.addAll(oldSize, it.data)
                        adapter.notifyItemRangeInserted(oldSize, it.data.size)
                        scrollListener.updateStateAfterGetData()
                    }

                    if (viewFlipper.displayedChild != CONTAINER_LIST) {
                        viewFlipper.displayedChild = CONTAINER_LIST
                    }
                }
                LiveDataResult.STATUS.ERROR -> {
                    //Do nothing
                    viewFlipper.displayedChild = CONTAINER_ERROR
                }
            }
        })

        viewModel.recommendationLiveData.observe(context as AppCompatActivity, Observer {
            when (it.status) {
                LiveDataResult.STATUS.ERROR -> {
                    viewFlipper.displayedChild = CONTAINER_ERROR
                }
            }
        })

        globalError.setOnClickListener {
            viewFlipper.displayedChild = CONTAINER_LOADING
            getRecommendationParams()
        }
    }

    protected fun getRecommendationParams() {
        viewModel.getRecommendationParams(PAGE_NAME)
    }


    fun getRecommendationListener(): GamiPdpRecommendationListener {

        val listener = object : GamiPdpRecommendationListener {

            override fun onProductImpression(item: RecommendationItem, position: Int) {
                val resultMap = mutableMapOf<String, Any>()
                val eCommerceMap = HashMap<Any, Any>()
                val clickMap = mutableMapOf<Any, Any>()
                val actionFieldMap = mutableMapOf<Any, Any>()

                val eventName = "productView"
                val eventCategory = "lucky egg - crack the egg"
                val eventAction = "click - product recommendation{ - nonlogin}"


                val productItemMap = HashMap<Any, Any>()
                val productArray = mutableListOf<HashMap<Any, Any>>()
                productArray.add(productItemMap)

                productItemMap[ProductKeys.NAME] = item.name
                productItemMap[ProductKeys.ID] = item.productId
                productItemMap[ProductKeys.POSITION] = position

                resultMap[TrackerConstants.EVENT] = eventName
                resultMap[TrackerConstants.EVENT_CATEGORY] = eventCategory
                resultMap[TrackerConstants.EVENT_ACTION] = eventAction
                resultMap[TrackerConstants.ECOMMERCE] = eCommerceMap

                eCommerceMap[EcommerceKeys.CLICK] = clickMap

                clickMap[ClickKeys.ACTION_FIELD] = actionFieldMap
                clickMap[ClickKeys.PRODUCTS] = productArray

                TrackApp.getInstance().gtm.sendGeneralEvent(resultMap)

            }

            override fun onProductClick(item: RecommendationItem, layoutType: String?, vararg position: Int) {

                val resultMap = mutableMapOf<String, Any>()
                val eCommerceMap = HashMap<Any, Any>()
                val clickMap = mutableMapOf<Any, Any>()
                val actionFieldMap = mutableMapOf<Any, Any>()

                val eventName = "productClick"
                val eventCategory = "lucky egg - crack the egg"
                val eventAction = "click - product recommendation{ - nonlogin}"


                val productItemMap = HashMap<Any, Any>()
                val productArray = mutableListOf<HashMap<Any, Any>>()
                productArray.add(productItemMap)

                productItemMap[ProductKeys.NAME] = item.name
                productItemMap[ProductKeys.ID] = item.productId
                productItemMap[ProductKeys.POSITION] = position

                resultMap[TrackerConstants.EVENT] = eventName
                resultMap[TrackerConstants.EVENT_CATEGORY] = eventCategory
                resultMap[TrackerConstants.EVENT_ACTION] = eventAction
                resultMap[TrackerConstants.ECOMMERCE] = eCommerceMap

                eCommerceMap[EcommerceKeys.CLICK] = clickMap

                clickMap[ClickKeys.ACTION_FIELD] = actionFieldMap
                clickMap[ClickKeys.PRODUCTS] = productArray

                TrackApp.getInstance().gtm.sendGeneralEvent(resultMap)

                val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, item.productId.toString())
                if (position.isNotEmpty()) intent.putExtra(Wishlist.PDP_EXTRA_UPDATED_POSITION, position[0])
                fragment?.startActivityForResult(intent, Wishlist.REQUEST_FROM_PDP)
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

                //send events
                val resultMap = mutableMapOf<String, Any>()

                val eventName = "productClick"
                val eventCategory = "lucky egg - crack the egg"
                val eventAction = "click - product recommendation{ - nonlogin}"
                val type = if (item.isTopAds) "topads" else "general"
                val label = "${item.productId} - $type - source : ${item}"

                resultMap[TrackerConstants.EVENT] = eventName
                resultMap[TrackerConstants.EVENT_CATEGORY] = eventCategory
                resultMap[TrackerConstants.EVENT_ACTION] = eventAction
                resultMap[TrackerConstants.EVENT_LABEL] = label
                TrackApp.getInstance().gtm.sendGeneralEvent(resultMap)
            }
        }

        return listener
    }

    private fun prepareShimmer() {
        (0..1).forEach { _ ->
            val v = LayoutInflater.from(context).inflate(R.layout.shimmer_pdp_gamification, null, false)
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