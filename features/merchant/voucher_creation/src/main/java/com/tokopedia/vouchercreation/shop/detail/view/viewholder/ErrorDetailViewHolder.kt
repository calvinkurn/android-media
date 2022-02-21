package com.tokopedia.vouchercreation.shop.detail.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.detail.model.ErrorDetailUiModel
import kotlinx.android.synthetic.main.item_mvc_voucher_list_error_state.view.*

class ErrorDetailViewHolder(itemView: View?,
                            private val onTryAgain: () -> Unit): AbstractViewHolder<ErrorDetailUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_voucher_list_error_state
    }

    override fun bind(element: ErrorDetailUiModel) {
        itemView.geMvcList?.setActionClickListener {
            onTryAgain()
        }
    }
}