package com.tokopedia.thankyou_native.recommendation.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.recommendation.analytics.RecommendationAnalytics
import com.tokopedia.thankyou_native.recommendation.di.component.DaggerRecommendationComponent
import com.tokopedia.thankyou_native.recommendation.model.ProductRecommendationData
import com.tokopedia.thankyou_native.recommendation.model.ThankYouProductCardModel
import com.tokopedia.thankyou_native.recommendation.presentation.adapter.ProductCardViewAdapter
import com.tokopedia.thankyou_native.recommendation.presentation.adapter.decorator.ProductCardDefaultDecorator
import com.tokopedia.thankyou_native.recommendation.presentation.adapter.listener.ProductCardViewListener
import com.tokopedia.thankyou_native.recommendation.presentation.viewmodel.MarketPlaceRecommendationViewModel
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.thank_pdp_recommendation.view.*
import javax.inject.Inject

class MarketPlaceRecommendation : BaseCustomView, IRecommendationView {


    private lateinit var fragment: BaseDaggerFragment
    private lateinit var paymentId: String
    private lateinit var thanksPageData: ThanksPageData


    @Inject
    lateinit var analytics: dagger.Lazy<RecommendationAnalytics>

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    @Inject
    lateinit var userSessionInterface: dagger.Lazy<UserSessionInterface>

    var isObserverAttached = false

    private val viewModel: MarketPlaceRecommendationViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(fragment, viewModelFactory.get())
        viewModelProvider[MarketPlaceRecommendationViewModel::class.java]
    }

    private lateinit var adapter: ProductCardViewAdapter

    var listener: ProductCardViewListener? = null

    fun getLayout() = R.layout.thank_pdp_recommendation

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        injectComponents()
        initUI()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        injectComponents()
        initUI()
    }

    constructor(context: Context) : super(context) {
        injectComponents()
        initUI()
    }

    private fun injectComponents() {
        DaggerRecommendationComponent.builder()
                .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
                .build().inject(this)
    }

    private fun initUI() {
        LayoutInflater.from(context).inflate(getLayout(), this, true)
    }

    override fun loadRecommendation(thanksPageData: ThanksPageData, fragment: BaseDaggerFragment) {
        this.thanksPageData = thanksPageData
        this.paymentId = thanksPageData.paymentID.toString()
        this.fragment = fragment
        startViewModelObserver()
        viewModel.loadRecommendationData()
    }

    private fun startViewModelObserver() {
        if (!isObserverAttached)
            viewModel.recommendationMutableData.observe(fragment,
                    Observer {
                        when (it) {
                            is Success -> addResultToUI(it.data)
                            is Fail -> gone()
                        }
                    }
            )
        isObserverAttached = true
    }

    private fun addResultToUI(data: ProductRecommendationData) {
        tvTitle.text = data.title
        tvTitle.visible()
        setupRecyclerView(data.thankYouProductCardModelList, data.maxHeight)
        adapter.notifyDataSetChanged()
    }

    private fun setupRecyclerView(thankYouProductCardModelList: List<ThankYouProductCardModel>, maxHeight: Int) {
        listener = getRecommendationListener()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        if (thankYouProductCardModelList.isNotEmpty()) {
            val carouselLayoutParams = this.recyclerView.layoutParams
            carouselLayoutParams.height = maxHeight
            recyclerView.layoutParams = carouselLayoutParams
            recyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL,
                    false)
            adapter = ProductCardViewAdapter(thankYouProductCardModelList, listener)
            recyclerView.adapter = adapter
            recyclerView.addItemDecoration(ProductCardDefaultDecorator())
        } else {
            gone()
        }
    }


    private fun getRecommendationListener(): ProductCardViewListener {
        return object : ProductCardViewListener {
            override fun onProductClick(item: RecommendationItem,
                                        layoutType: String?, vararg position: Int) {
                if (position.isNotEmpty())
                    onRecomProductClick(item, position[0])
            }


            override fun onProductImpression(item: RecommendationItem, position: Int) {

            }


            override fun onRecommendationItemDisplayed(recommendationItem: RecommendationItem,
                                                       position: Int) {
                analytics.get().sendRecommendationItemDisplayed(thanksPageData, recommendationItem, position)
            }
        }
    }


    private fun onRecomProductClick(item: RecommendationItem, position: Int) {
        analytics.get().sendRecommendationItemClick(thanksPageData, item, position = position + 1)
        val intent = RouteManager.getIntent(context,
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL, item.productId.toString())

        intent.putExtra(PDP_EXTRA_UPDATED_POSITION, position)
        fragment.startActivityForResult(intent, REQUEST_FROM_PDP)
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        listener = null
    }

    companion object {
        const val PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition"
        const val REQUEST_FROM_PDP = 138
    }

}


/*


    const val PDP_WIHSLIST_STATUS_IS_WISHLIST = "isWishlist"
private fun onWishListedSuccessfully(message: String) {
     fragment.view?.let { view ->
         Toaster.make(view,
                 message,
                 Snackbar.LENGTH_LONG,
                 actionText = view.context.getString(R.string.thank_recom_go_to_wishlist),
                 clickListener = View.OnClickListener {
                     RouteManager.route(view.context, ApplinkConst.WISHLIST)
                 },
                 type = Toaster.TYPE_NORMAL)
     }
 }*/

/* private fun onItemRemoveFromWishList(message: String) {
     fragment.view?.let { view ->
         Toaster.make(view, message, Snackbar.LENGTH_LONG)
     }
 }*/

/* private fun onShowError(throwable: Throwable?) {
     fragment.view?.let { view ->
         val message = ErrorHandler.getErrorMessage(view.context, throwable)
         Toaster.make(view,
                 message,
                 Snackbar.LENGTH_LONG,
                 type = Toaster.TYPE_ERROR)
     }
 }*/

/*
    private fun onItemWishListClick(item: RecommendationItem, isAddWishlist: Boolean,
                                    callback: (Boolean, Throwable?) -> Unit) {
        if (userSessionInterface.get().isLoggedIn) {
            if (!isAddWishlist) {
                viewModel.addToWishList(item, callback)
            } else {
                viewModel.removeFromWishList(item, callback)
            }
        } else {
            RouteManager.route(context, ApplinkConst.LOGIN)
        }
    }*/


/*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
    return when (requestCode) {
        WishList.REQUEST_FROM_PDP -> {
            if (data != null) {
                val wishListStatusFromPdp = data
                        .getBooleanExtra(WishList.PDP_WIHSLIST_STATUS_IS_WISHLIST, false)
                val position = data.getIntExtra(WishList.PDP_EXTRA_UPDATED_POSITION, -1)
                updateWishListedItemStatus(position, wishListStatusFromPdp)
            }
            true
        }
        else -> false
    }
}*/

/*   private fun updateWishListedItemStatus(position: Int, wishListStatusFromPdp: Boolean) {
       if (::adapter.isInitialized) {
           val thankYouRecommendationModel = adapter.productCardList[position]
           thankYouRecommendationModel.recommendationItem.isWishlist = wishListStatusFromPdp
           thankYouRecommendationModel.productCardModel?.isWishlisted = wishListStatusFromPdp
           adapter.notifyDataSetChanged()
       }
   }*/
