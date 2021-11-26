package com.tokopedia.promocheckoutmarketplace.presentation.viewholder

import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.databinding.PromoCheckoutMarketplaceModuleItemPromoTabBinding
import com.tokopedia.promocheckoutmarketplace.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoTabUiModel

class PromoTabViewHolder(private val viewBinding: PromoCheckoutMarketplaceModuleItemPromoTabBinding,
                         private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoTabUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.promo_checkout_marketplace_module_item_promo_tab
    }

    override fun bind(element: PromoTabUiModel) {
        with(viewBinding) {
            tabsPromo.customTabMode = TabLayout.MODE_SCROLLABLE
            if (element.uiData.tabs.isNotEmpty()) {
                element.uiData.tabs.forEach {
                    tabsPromo.addNewTab(it.title)
                }
                tabsPromo.show()
            } else {
                tabsPromo.gone()
            }
        }
    }

}