package com.tokopedia.promocheckoutmarketplace.presentation.viewholder

import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.databinding.PromoCheckoutMarketplaceModuleItemPromoTabBinding
import com.tokopedia.promocheckoutmarketplace.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoTabUiModel

class PromoTabViewHolder(
    private val viewBinding: PromoCheckoutMarketplaceModuleItemPromoTabBinding,
    private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoTabUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.promo_checkout_marketplace_module_item_promo_tab
    }

    var tabSelectedListener: TabLayout.OnTabSelectedListener? = null

    override fun bind(element: PromoTabUiModel) {
        with(viewBinding) {
            tabSelectedListener?.let {
                tabsPromo.getUnifyTabLayout().removeOnTabSelectedListener(it)
            }

            if (element.uiState.selectedTabPosition != tabsPromo.getUnifyTabLayout().selectedTabPosition) {
                tabsPromo.getUnifyTabLayout().getTabAt(element.uiState.selectedTabPosition)?.select()
            }

            element.uiState.isInitialization = false
            tabsPromo.customTabMode = TabLayout.MODE_SCROLLABLE
            tabsPromo.customTabGravity = TabLayout.GRAVITY_FILL
            if (element.uiData.tabs.isNotEmpty() && tabsPromo.getUnifyTabLayout().getTabAt(0) == null) {
                element.uiData.tabs.forEach {
                    tabsPromo.addNewTab(it.title)
                }
            }

            tabSelectedListener = object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    element.uiState.selectedTabPosition = tab?.position ?: 0
                    element.uiState.isSelectionAction = true
                    listener.onTabSelected(element)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    /* No-op */
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    /* No-op */
                }
            }
            tabsPromo.getUnifyTabLayout().addOnTabSelectedListener(tabSelectedListener!!)
        }
    }
}
