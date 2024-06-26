package com.tokopedia.home_component.widget.shop_flash_sale.item

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.databinding.HomeComponentErrorStateBinding
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.home_component.R as home_componentR

internal class ShopFlashSaleErrorViewHolder(
    itemView: View,
    private val listener: ShopFlashSaleErrorListener,
): AbstractViewHolder<ShopFlashSaleErrorDataModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = home_componentR.layout.home_component_error_state
    }

    private val binding: HomeComponentErrorStateBinding? by viewBinding()

    override fun bind(tab: ShopFlashSaleErrorDataModel) {
        binding?.homeComponentLocalLoad?.run {
            refreshBtn?.setOnClickListener {
                progressState = true
                listener.onRefreshClick()
            }
        }
    }
}
