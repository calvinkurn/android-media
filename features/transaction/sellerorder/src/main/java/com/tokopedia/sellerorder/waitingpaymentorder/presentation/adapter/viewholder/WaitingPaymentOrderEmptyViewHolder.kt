package com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.databinding.ItemListEmptyBinding
import com.tokopedia.utils.view.binding.viewBinding

class WaitingPaymentOrderEmptyViewHolder(itemView: View) : AbstractViewHolder<EmptyModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_list_empty
    }

    private val binding by viewBinding<ItemListEmptyBinding>()

    override fun bind(element: EmptyModel?) {
        binding?.run {
            val horizontalPadding = root.getDimens(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
            ivSomListEmptyStateIllustration.loadImage(SomConsts.SOM_LIST_EMPTY_STATE_NO_FILTER_ILLUSTRATION)
            tvEmptyStateTitle.apply {
                text = getString(R.string.waiting_payment_empty_state_title)
                setPadding(horizontalPadding, 0, horizontalPadding, 0)
            }
            tvEmptyStateDescription.apply {
                text = getString(R.string.waiting_payment_empty_state_description)
                setPadding(horizontalPadding, 0, horizontalPadding, 0)
            }
            btnEmptyState.gone()
        }
    }
}