package com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts
import kotlinx.android.synthetic.main.item_list_empty.view.*

class WaitingPaymentOrderEmptyViewHolder(itemView: View?) : AbstractViewHolder<EmptyModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_list_empty
    }

    override fun bind(element: EmptyModel?) {
        with (itemView) {
            val horizontalPadding = getDimens(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
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