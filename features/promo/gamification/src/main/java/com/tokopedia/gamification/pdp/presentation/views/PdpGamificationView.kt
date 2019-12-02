package com.tokopedia.gamification.pdp.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.gamification.R
import com.tokopedia.gamification.pdp.data.LiveDataResult
import com.tokopedia.gamification.pdp.data.Recommendation
import com.tokopedia.gamification.pdp.data.di.components.DaggerPdpComponent
import com.tokopedia.gamification.pdp.presentation.adapters.PdpGamificationAdapter
import com.tokopedia.gamification.pdp.presentation.adapters.PdpGamificationAdapterTypeFactory
import com.tokopedia.gamification.pdp.presentation.viewmodels.PdpDialogViewModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class PdpGamificationView : FrameLayout {

    private val CONTAINER_LIST = 0
    private val CONTAINER_LOADING = 1
    private val CONTAINER_ERROR = 2

    private lateinit var PAGE_NAME :String

    private lateinit var tvTitle: Typography
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewFlipper: ViewFlipper

    private lateinit var adapter: PdpGamificationAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var dataList : ArrayList<Visitable<*>>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: PdpDialogViewModel

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

        setupRv()
        setListeners()
        getRecommendationParams()
    }

    private fun setupRv() {
        val typeFactory = PdpGamificationAdapterTypeFactory(getRecommendationListener())
        adapter = PdpGamificationAdapter(dataList, typeFactory)
        val layoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                viewModel.getProducts()
            }

        }
        recyclerView.addOnScrollListener(scrollListener)
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

        viewModel.productLiveData.observe(context as AppCompatActivity, Observer {
            when (it.status) {
                LiveDataResult.STATUS.SUCCESS -> {
                    if (it.data != null) {
                        dataList.addAll(it.data)
                        adapter.notifyDataSetChanged()
                    }
                }
                LiveDataResult.STATUS.ERROR -> {
                    //Do nothing
                    showErrorToast("Error prod live data")
                }
            }
        })

        viewModel.recommendationLiveData.observe(context as AppCompatActivity, Observer {
            when (it.status) {
                LiveDataResult.STATUS.ERROR -> {
                    //Do nothing
                    showErrorToast("Error recommendation data")
                }
            }
        })

    }

    protected fun getRecommendationParams() {
        viewModel.getRecommendationParams(PAGE_NAME)
    }

    fun showErrorToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun getRecommendationListener(): RecommendationListener {

        fun onImpressionOrganic(item: RecommendationItem) {
//            InboxGtmTracker.getInstance().addInboxProductViewImpressions(item, item.position, item.isTopAds)
        }

        fun onClickOrganic(item: RecommendationItem) {
//            InboxGtmTracker.getInstance().eventInboxProductClick(context, item, item.position, item.isTopAds)
        }

        fun onClickTopAds(item: RecommendationItem) {
            ImpresionTask().execute(item.clickUrl)
//            InboxGtmTracker.getInstance().eventInboxProductClick(context, item, item.position, item.isTopAds)
        }

        fun onImpressionTopAds(item: RecommendationItem) {
            ImpresionTask().execute(item.trackerImageUrl)
//            InboxGtmTracker.getInstance().addInboxProductViewImpressions(item, item.position, item.isTopAds)
        }
        return object : RecommendationListener {
            override fun onProductClick(item: RecommendationItem, layoutType: String?, vararg position: Int) {
                if (item.isTopAds) {
                    ImpresionTask().execute(item.clickUrl)
                    onClickTopAds(item)
                } else {
                    onClickOrganic(item)
                }
                val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, item.productId.toString())
                if (position.size >= 1) intent.putExtra(PDP_EXTRA_UPDATED_POSITION, position[0])
                if (context is AppCompatActivity) {
                    (context as AppCompatActivity).startActivityForResult(intent, REQUEST_FROM_PDP)
                }
            }

            override fun onProductImpression(item: RecommendationItem) {
//                if (item.isTopAds) {
//                    ImpresionTask().execute(item.trackerImageUrl)
//                    onImpressionTopAds(item)
//                } else {
//                    onImpressionOrganic(item)
//                }
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
    }

    companion object Wishlist {
        const val PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition"
        const val REQUEST_FROM_PDP = 138
    }

}