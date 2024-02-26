package com.tokopedia.buy_more_get_more.minicart.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.bmsm_widget.presentation.model.GiftWidgetState
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.databinding.ItemGwpMiniCartEditorGiftWidgetBinding
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.GwpMiniCartEditorAdapter
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.kotlin.extensions.orFalse

/**
 * Created by @ilhamsuaib on 05/12/23.
 */

class GwpMiniCartEditorGiftWidgetViewHolder(
    itemView: View, private val listener: GwpMiniCartEditorAdapter.Listener
) : AbstractViewHolder<BmgmMiniCartVisitable.GwpGiftWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_gwp_mini_cart_editor_gift_widget
    }

    private val binding by lazy {
        ItemGwpMiniCartEditorGiftWidgetBinding.bind(itemView)
    }

    override fun bind(element: BmgmMiniCartVisitable.GwpGiftWidgetUiModel) {
        when (element.state) {
            GiftWidgetState.SUCCESS -> showGiftList(element)
            GiftWidgetState.LOADING -> binding.miniCartGiftView.showLoadingState()
            GiftWidgetState.ERROR -> binding.miniCartGiftView.showErrorState()
        }
    }

    private fun showGiftList(element: BmgmMiniCartVisitable.GwpGiftWidgetUiModel) {
        with(binding.miniCartGiftView) {
            setRibbonText(text = element.benefitWording)
            updateData(giftList = element.productList)
            if (element.benefitCta.isNotBlank()) {
                setupCtaClickListener(text = element.benefitCta) {
                    listener.onGiftCtaClicked()
                }
            } else {
                removeCta()
            }
        }
    }
}