package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem

import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Constant.ProductTemplate.GRID
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.productcard.ProductCardModel

class MasterProductCardItemViewHolder(itemView: View, val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private lateinit var productCardItemViewModel: MasterProductCardItemViewModel
//    private var masterProductCard: ProductCardGridView = itemView.findViewById(R.id.master_product_card_grid)
    private var masterProductCardGridView: ProductCardGridView? = null
    private var masterProductCardListView: ProductCardListView? = null
    private var productCardView: CardView = itemView.findViewById(R.id.cardViewProductCard)
    private var productCardName = ""
    private var dataItem: DataItem? = null
    private var componentPosition: Int? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        productCardItemViewModel = discoveryBaseViewModel as MasterProductCardItemViewModel
        initView()
    }

    private fun initView() {
        if(productCardItemViewModel.getTemplateType() == GRID){
            masterProductCardGridView = itemView.findViewById(R.id.master_product_card_grid)
            masterProductCardGridView?.show()
            masterProductCardListView?.hide()
        }else{
            masterProductCardListView = itemView.findViewById(R.id.master_product_card_list)
            masterProductCardListView?.show()
            masterProductCardGridView?.hide()
        }
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
            productCardView.layoutParams.width = itemView.context.resources.getDimensionPixelSize(R.dimen.disco_product_card_width)
            masterProductCardGridView?.let {
                it.applyCarousel()
                it.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                it.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            }

            masterProductCardListView?.let {
                it.applyCarousel()
                it.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                it.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            }
        } else {
            masterProductCardGridView?.let {
                it.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                it.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            masterProductCardListView?.let {
                it.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                it.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
        }
        masterProductCardGridView?.let {
            it.setProductModel(productCardModel)
        }
        masterProductCardListView?.let {
            it.setProductModel(productCardModel)
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
}