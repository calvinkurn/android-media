package com.tokopedia.thankyou_native.recommendation.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.viewModel.state.Loaded
import com.tokopedia.thankyou_native.recommendation.analytics.RecommendationAnalytics
import com.tokopedia.thankyou_native.recommendation.di.component.DaggerRecommendationComponent
import com.tokopedia.thankyou_native.recommendation.presentation.adapter.ThankYouRecomAdapter
import com.tokopedia.thankyou_native.recommendation.presentation.adapter.ThankYouRecomViewListener
import com.tokopedia.thankyou_native.recommendation.presentation.adapter.decorator.ProductCardDefaultDecorator
import com.tokopedia.thankyou_native.recommendation.presentation.adapter.model.ThankYouRecommendationModel
import com.tokopedia.thankyou_native.recommendation.presentation.viewmodel.RecommendationViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.thank_pdp_recommendation.view.*
import javax.inject.Inject

class PDPThankYouPageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var viewModel: RecommendationViewModel? = null

    @Inject
    lateinit var analytics: RecommendationAnalytics

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private lateinit var adapter: ThankYouRecomAdapter

    var listener: ThankYouRecomViewListener? = null

    fun getLayout() = R.layout.thank_pdp_recommendation

    private var fragment: BaseDaggerFragment? = null

    init {
        injectComponents()
        initUI()
    }

    private fun injectComponents() {
        DaggerRecommendationComponent.builder()
                .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
                .build().inject(this)
        if (context is AppCompatActivity) {
            val viewModelProvider = ViewModelProviders.of(context as AppCompatActivity, viewModelFactory)
            viewModel = viewModelProvider[RecommendationViewModel::class.java]
        }
    }

    private fun initUI() {
        val v = LayoutInflater.from(context).inflate(getLayout(), this, true)
        viewModel?.let {
            startViewModelObserver(it)
        }
    }

    fun loadRecommendation(fragment: BaseDaggerFragment) {
        this.fragment = fragment
        viewModel?.loadRecommendationData()
    }

    private fun startViewModelObserver(recommendationViewModel: RecommendationViewModel) {
        recommendationViewModel.titleLiveData.observe(context as AppCompatActivity, Observer {
            when (it) {
                is Loaded -> {
                    (it.data as? Success)?.data?.let { title ->
                        tvTitle.text = title
                    }
                }
            }
        })

        recommendationViewModel.recommendationListModel.observe(context as AppCompatActivity, Observer {
            when (it) {
                is Loaded -> {
                    (it.data as? Success)?.data?.let { result ->
                        addItemsToAdapter(result)
                    }
                }
                else -> {

                }
            }
        })
    }

    private fun addItemsToAdapter(result: List<RecommendationWidget>) {
        val recommendationItemList = result[0].recommendationItemList
        if (recommendationItemList.isNotEmpty()) {
            tvTitle.visible()
            val thankYouRecommendationModelList = getProductCardModel(recommendationItemList)
            val blankSpaceConfig = computeBlankSpaceConfig(thankYouRecommendationModelList)
            setupRecyclerView(thankYouRecommendationModelList, blankSpaceConfig)
            adapter.notifyDataSetChanged()
        }
    }

    private fun getProductCardModel(recommendationItemList: List<RecommendationItem>)
            : List<ThankYouRecommendationModel> {
        return recommendationItemList.map { recommendationItem ->
            ThankYouRecommendationModel(recommendationItem,
                    ProductCardModel(
                            slashedPrice = recommendationItem.slashedPrice,
                            productName = recommendationItem.name,
                            formattedPrice = recommendationItem.price,
                            productImageUrl = recommendationItem.imageUrl,
                            isTopAds = recommendationItem.isTopAds,
                            discountPercentage = recommendationItem.discountPercentage.toString(),
                            reviewCount = recommendationItem.countReview,
                            ratingCount = recommendationItem.rating,
                            shopLocation = recommendationItem.location,
                            isWishlistVisible = true,
                            isWishlisted = recommendationItem.isWishlist,
                            shopBadgeList = recommendationItem.badgesUrl.map {
                                ProductCardModel.ShopBadge(imageUrl = it ?: "")
                            },
                            freeOngkir = ProductCardModel.FreeOngkir(
                                    isActive = recommendationItem.isFreeOngkirActive,
                                    imageUrl = recommendationItem.freeOngkirImageUrl
                            )
                    )
            )
        }
    }

    private fun setupRecyclerView(thankYouRecommendationModelList: List<ThankYouRecommendationModel>,
                                  blankSpaceConfig: BlankSpaceConfig) {
        listener = getRecommendationListener()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL,
                false)
        adapter = ThankYouRecomAdapter(thankYouRecommendationModelList, blankSpaceConfig, listener)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(ProductCardDefaultDecorator())
    }

    private fun computeBlankSpaceConfig(productCardModelList: List<ThankYouRecommendationModel>)
            : BlankSpaceConfig {
        val blankSpaceConfig = BlankSpaceConfig(
                twoLinesProductName = true
        )
        productCardModelList.forEach {
            val productCardModel = it.productCardModel
            if (productCardModel.freeOngkir.isActive) blankSpaceConfig.freeOngkir = true
            if (productCardModel.shopName.isNotEmpty()) blankSpaceConfig.shopName = true
            if (productCardModel.productName.isNotEmpty()) blankSpaceConfig.productName = true
            if (productCardModel.labelPromo.title.isNotEmpty()) blankSpaceConfig.labelPromo = true
            if (productCardModel.slashedPrice.isNotEmpty()) blankSpaceConfig.slashedPrice = true
            if (productCardModel.discountPercentage.isNotEmpty()) blankSpaceConfig.discountPercentage = true
            if (productCardModel.formattedPrice.isNotEmpty()) blankSpaceConfig.price = true
            if (productCardModel.shopBadgeList.isNotEmpty()) blankSpaceConfig.shopBadge = true
            if (productCardModel.shopLocation.isNotEmpty()) blankSpaceConfig.shopLocation = true
            if (productCardModel.ratingCount != 0) blankSpaceConfig.ratingCount = true
            if (productCardModel.reviewCount != 0) blankSpaceConfig.reviewCount = true
            if (productCardModel.labelCredibility.title.isNotEmpty()) blankSpaceConfig.labelCredibility = true
            if (productCardModel.labelOffers.title.isNotEmpty()) blankSpaceConfig.labelOffers = true
            if (productCardModel.isTopAds) blankSpaceConfig.topAdsIcon = true
        }
        return blankSpaceConfig
    }

    private fun getRecommendationListener(): ThankYouRecomViewListener {
        return object : ThankYouRecomViewListener {

            override fun onProductClick(item: RecommendationItem,
                                        layoutType: String?, vararg position: Int) {
                analytics.sendRecommendationItemClick(item, position = position[0] + 1)
                val intent = RouteManager.getIntent(context,
                        ApplinkConstInternalMarketplace.PRODUCT_DETAIL, item.productId.toString())
                if (position.isNotEmpty())
                    intent.putExtra(WishList.PDP_EXTRA_UPDATED_POSITION, position[0])
                fragment?.startActivityForResult(intent, WishList.REQUEST_FROM_PDP)
            }

            override fun onProductImpression(item: RecommendationItem, position: Int) {

            }

            override fun onProductImpression(item: RecommendationItem) {

            }

            override fun onProductAddToCartClick(item: RecommendationItem, position: Int) {

            }

            override fun onWishListedSuccessfully(message: String) {
                fragment?.view?.let { view ->
                    Toaster.make(view,
                            message,
                            Snackbar.LENGTH_LONG,
                            actionText = view.context.getString(R.string.thank_recom_go_to_wishlist),
                            clickListener = View.OnClickListener {
                                RouteManager.route(view.context, ApplinkConst.WISHLIST)
                            },
                            type = Toaster.TYPE_NORMAL)
                }
            }

            override fun onRemoveFromWishList(message: String) {
                fragment?.view?.let { view ->
                    Toaster.make(view, message, Snackbar.LENGTH_LONG)
                }
            }

            override fun onShowError(throwable: Throwable?) {
                fragment?.view?.let { view ->
                    val message = ErrorHandler.getErrorMessage(view.context, throwable)
                    Toaster.make(view,
                            message,
                            Snackbar.LENGTH_LONG,
                            type = Toaster.TYPE_ERROR)
                }
            }

            override fun onRecommendationItemDisplayed(recommendationItem: RecommendationItem,
                                                       position: Int) {
                analytics.sendRecommendationItemDisplayed(recommendationItem, position)
            }

            override fun onWishlistClick(item: RecommendationItem, isAddWishlist: Boolean,
                                         callback: (Boolean, Throwable?) -> Unit) {
                if (userSessionInterface.isLoggedIn) {
                    if (!isAddWishlist) {
                        viewModel?.addToWishList(item, callback)
                    } else {
                        viewModel?.removeFromWishList(item, callback)
                    }
                } else {
                    RouteManager.route(context, ApplinkConst.LOGIN)
                }
            }
        }
    }

    fun onActivityResult(position: Int, wishListStatusFromPdp: Boolean) {
        if (::adapter.isInitialized) {
            val thankYouRecommendationModel = adapter.thankYouRecommendationModelList[position]
            thankYouRecommendationModel.recommendationItem.isWishlist = wishListStatusFromPdp
            thankYouRecommendationModel.productCardModel.isWishlisted = wishListStatusFromPdp
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        listener = null
    }

}

object WishList {
    const val PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition"
    const val PDP_WIHSLIST_STATUS_IS_WISHLIST = "isWishlist"
    const val REQUEST_FROM_PDP = 138
}