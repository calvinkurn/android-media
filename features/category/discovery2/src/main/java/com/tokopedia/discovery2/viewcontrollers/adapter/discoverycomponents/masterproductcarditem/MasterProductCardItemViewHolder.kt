package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel

private const val OFFICIAL_STORE = 1
private const val GOLD_MERCHANT = 2

class MasterProductCardItemViewHolder(itemView: View, val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private lateinit var productCardItemViewModel: MasterProductCardItemViewModel
    private var masterProductCard: ProductCardGridView = itemView.findViewById(R.id.master_product_card)
    private var productCardView: CardView = itemView.findViewById(R.id.cardViewProductCard)
    private var shopBadge: ImageView = itemView.findViewById(R.id.imageShopBadge)
    private var textViewShopLocation: TextView = itemView.findViewById(R.id.textViewShopLocation)
    private var productCardName = ""
    private var dataItem: DataItem? = null
    private var componentPosition: Int? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        productCardItemViewModel = discoveryBaseViewModel as MasterProductCardItemViewModel
        initView()
    }

    private fun initView() {
        productCardItemViewModel.setContext(productCardView.context)
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
                populateData(data)
            })
            productCardItemViewModel.getComponentPosition().observe(lifecycleOwner, Observer { position ->
                componentPosition = position
            })
        }
    }

    private fun populateData(dataItem: DataItem) {
        val productCardModel = ProductCardModel(
                productName = dataItem.name ?: "",
                slashedPrice = dataItem.discountedPrice ?: "",
                formattedPrice = dataItem.price ?: "",
                discountPercentage = if (dataItem.discountPercentage?.toIntOrZero() != 0) {
                    "${dataItem.discountPercentage}%"
                } else {
                    ""
                },
                ratingCount = dataItem.rating?.toIntOrZero() ?: 0,
                reviewCount = dataItem.countReview?.toIntOrZero() ?: 0,
                productImageUrl = dataItem.imageUrlMobile ?: "",
                isTopAds = dataItem.isTopads ?: false,
                freeOngkir = ProductCardModel.FreeOngkir(imageUrl = dataItem.freeOngkir?.freeOngkirImageUrl
                        ?: "", isActive = dataItem.freeOngkir?.isActive ?: false),
                pdpViewCount = dataItem.pdpView.takeIf { it.toIntOrZero() != 0 } ?: "",
                labelGroupList = ArrayList<ProductCardModel.LabelGroup>().apply {
                    dataItem.labelsGroupList?.forEach { add(ProductCardModel.LabelGroup(it.position, it.title, it.type)) }
                }
        )
        if (productCardName == ComponentNames.ProductCardCarouselItem.componentName) {
            val displayMetrics = getDisplayMetric(fragment.context)
            productCardView.layoutParams.width = (displayMetrics.widthPixels / 2.3).toInt()
            masterProductCard.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        } else {
            masterProductCard.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        }
        showShopBadgeUI(dataItem)
        masterProductCard.setProductModel(productCardModel)
    }

    private fun getDisplayMetric(context: Context?): DisplayMetrics {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics
    }

    private fun showShopBadgeUI(dataItem: DataItem) {
        when (productCardItemViewModel.chooseShopBadge()) {
            OFFICIAL_STORE -> {
                shopBadge.show()
                shopBadge.setImageResource(R.drawable.discovery_official_store_icon)
            }
            GOLD_MERCHANT -> {
                shopBadge.show()
                shopBadge.setImageResource(R.drawable.discovery_gold_merchant_icon)
            }
            else -> shopBadge.hide()
        }
        if (!dataItem.shopLocation.isNullOrEmpty()) {
            textViewShopLocation.setTextAndCheckShow(dataItem.shopLocation)
        } else if (!dataItem.shopName.isNullOrEmpty()) {
            textViewShopLocation.setTextAndCheckShow(dataItem.shopName)
        } else {
            textViewShopLocation.hide()
        }
    }

    private fun handleUIClick(view: View) {
        when (view) {
            productCardView -> {
                productCardItemViewModel.sendTopAdsClick()
                productCardItemViewModel.handleNavigation()
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
}