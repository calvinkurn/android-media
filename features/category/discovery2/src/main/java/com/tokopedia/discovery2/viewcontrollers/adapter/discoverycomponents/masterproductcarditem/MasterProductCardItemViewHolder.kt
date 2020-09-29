package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
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
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel

class MasterProductCardItemViewHolder(itemView: View, val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private lateinit var productCardItemViewModel: MasterProductCardItemViewModel
    private var masterProductCard: ProductCardGridView = itemView.findViewById(R.id.master_product_card)
    private var productCardView: CardView = itemView.findViewById(R.id.cardViewProductCard)
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
            })
            productCardItemViewModel.getProductModelValue().observe(lifecycleOwner, Observer { data ->
                populateData(data)
            })
            productCardItemViewModel.getComponentPosition().observe(lifecycleOwner, Observer { position ->
                componentPosition = position
            })
        }
    }

    private fun populateData(productCardModel: ProductCardModel) {
        if (productCardName == ComponentNames.ProductCardCarouselItem.componentName) {
            val displayMetrics = getDisplayMetric(fragment.context)
            productCardView.layoutParams.width = (displayMetrics.widthPixels / 2.3).toInt()
            masterProductCard.applyCarousel()
            masterProductCard.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            masterProductCard.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            masterProductCard.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            masterProductCard.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        masterProductCard.setProductModel(productCardModel)
    }

    private fun getDisplayMetric(context: Context?): DisplayMetrics {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics
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