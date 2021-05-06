package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.datamodel.ErrorStateOngoingTransactionModel
import kotlinx.android.synthetic.main.holder_transaction_error_state.view.*

class ErrorStateOngoingTransactionViewHolder(itemView: View,
                                             val mainNavListener: MainNavListener
): AbstractViewHolder<ErrorStateOngoingTransactionModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_transaction_error_state
    }

    override fun bind(element: ErrorStateOngoingTransactionModel) {
        itemView.localload_transaction.refreshBtn?.setOnClickListener {
            mainNavListener.onErrorTransactionListClicked(adapterPosition)
        }
    }
}