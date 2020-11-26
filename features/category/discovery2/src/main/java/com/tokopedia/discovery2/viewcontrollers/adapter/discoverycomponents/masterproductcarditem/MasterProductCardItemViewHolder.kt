package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.discovery2.viewmodel.DiscoveryViewModel
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import javax.inject.Inject

class MasterProductCardItemViewHolder(itemView: View, val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private lateinit var productCardItemViewModel: MasterProductCardItemViewModel
    private var masterProductCard: ProductCardGridView = itemView.findViewById(R.id.master_product_card)
    private var productCardView: CardView = itemView.findViewById(R.id.cardViewProductCard)
    private var productCardName = ""
    private var dataItem: DataItem? = null
    private var componentPosition: Int? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var discoveryViewModel: DiscoveryViewModel

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        productCardItemViewModel = discoveryBaseViewModel as MasterProductCardItemViewModel
        getSubComponent().inject(productCardItemViewModel)
        val viewModelProvider = ViewModelProviders.of(itemView.context as FragmentActivity, viewModelFactory)
        discoveryViewModel = viewModelProvider.get(DiscoveryViewModel::class.java)
        initView()
    }

    private fun initView() {
        productCardView.setOnClickListener {
            handleUIClick(it)
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        productCardName = productCardItemViewModel.getComponentName()
        lifecycleOwner?.let {
            productCardItemViewModel.getDataItemValue().observe(lifecycleOwner, Observer { data ->
                dataItem = data
            })
            productCardItemViewModel.getProductModelValue().observe(lifecycleOwner, Observer { data ->
                populateData(data)
            })
            productCardItemViewModel.getComponentPosition().observe(lifecycleOwner, Observer { position ->
                componentPosition = position
            })

            discoveryViewModel.getWishListLiveData().observe(it, Observer { productCardOptionsModel ->
                if (productCardOptionsModel.wishlistResult.isAddWishlist) {
                    if (productCardOptionsModel.wishlistResult.isSuccess) {
                        NetworkErrorHelper.showSnackbar(itemView.context as Activity, itemView.context.getString(R.string.discovery_msg_success_add_wishlist))
                        productCardItemViewModel.components.data?.firstOrNull()?.isWishList = productCardOptionsModel.isWishlisted
                    } else {
                        NetworkErrorHelper.showSnackbar(itemView.context as Activity, itemView.context.getString(R.string.discovery_msg_error_add_wishlist))
                    }
                } else {
                    if (productCardOptionsModel.wishlistResult.isSuccess) {
                        productCardItemViewModel.components.data?.firstOrNull()?.isWishList = productCardOptionsModel.isWishlisted
                        NetworkErrorHelper.showSnackbar(itemView.context as Activity, itemView.context.getString(R.string.discovery_msg_success_remove_wishlist))
                    } else {
                        NetworkErrorHelper.showSnackbar(itemView.context as Activity, itemView.context.getString(R.string.discovery_msg_error_remove_wishlist))
                    }
                }
            })
        }
    }

    private fun populateData(productCardModel: ProductCardModel) {
        if (productCardName == ComponentNames.ProductCardCarouselItem.componentName) {
            productCardView.layoutParams.width = itemView.context.resources.getDimensionPixelSize(R.dimen.disco_product_card_width)
            masterProductCard.applyCarousel()
            masterProductCard.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            masterProductCard.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            masterProductCard.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            masterProductCard.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        masterProductCard.setProductModel(productCardModel)

        setWishlist()
    }

    private fun setWishlist() {
        masterProductCard.setThreeDotsOnClickListener {
            showProductCardOptions(itemView.context as FragmentActivity,
                    productCardItemViewModel.getProductCardOptionsModel()
            )
        }
    }

    private fun handleUIClick(view: View) {
        when (view) {
            productCardView -> {
                productCardItemViewModel.sendTopAdsClick()
                productCardItemViewModel.navigate(fragment.context, dataItem?.applinks)
                sendClickEvent()
            }
        }
    }

    private fun sendClickEvent() {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackProductCardClick(productCardItemViewModel.components, productCardItemViewModel.isUserLoggedIn())
    }

    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        productCardItemViewModel.sendTopAdsView()
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().viewProductsList(productCardItemViewModel.components, productCardItemViewModel.isUserLoggedIn())
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            discoveryViewModel.getWishListLiveData().removeObservers(it)
        }
    }
}