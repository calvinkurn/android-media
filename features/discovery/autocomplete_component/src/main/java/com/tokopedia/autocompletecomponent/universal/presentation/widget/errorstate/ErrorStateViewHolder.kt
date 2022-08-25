package com.tokopedia.autocompletecomponent.universal.presentation.widget.errorstate

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R

class ErrorStateViewHolder(itemView: View):
    AbstractViewHolder<ErrorStateDataView>(itemView){
    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.universal_search_error_state_layout
    }

    override fun bind(element: ErrorStateDataView?) {

    }
}