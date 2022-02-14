package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderTransactionErrorStateBinding
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.datamodel.ErrorStateOngoingTransactionModel
import com.tokopedia.utils.view.binding.viewBinding

class ErrorStateOngoingTransactionViewHolder(itemView: View,
                                             val mainNavListener: MainNavListener
): AbstractViewHolder<ErrorStateOngoingTransactionModel>(itemView) {
    private var binding: HolderTransactionErrorStateBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_transaction_error_state
    }

    override fun bind(element: ErrorStateOngoingTransactionModel) {
        binding?.localloadTransaction?.refreshBtn?.setOnClickListener {
            mainNavListener.onErrorTransactionListClicked(adapterPosition)
        }
    }
}