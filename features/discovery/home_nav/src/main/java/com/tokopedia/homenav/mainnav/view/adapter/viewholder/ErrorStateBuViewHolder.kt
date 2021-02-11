package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.datamodel.ErrorStateBuDataModel
import kotlinx.android.synthetic.main.holder_bu_error_state.view.*

class ErrorStateBuViewHolder(itemView: View,
                             val mainNavListener: MainNavListener
): AbstractViewHolder<ErrorStateBuDataModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_bu_error_state
    }

    override fun bind(element: ErrorStateBuDataModel) {
        itemView.localload_bu.refreshBtn?.setOnClickListener {
            mainNavListener.onErrorBuListClicked(adapterPosition)
        }
    }
}