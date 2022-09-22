package com.tokopedia.autocompletecomponent.universal.presentation.widget.errorstate

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.UniversalSearchErrorStateLayoutBinding
import com.tokopedia.utils.view.binding.viewBinding

class ErrorStateViewHolder(
    itemView: View,
    private val errorStateListener: ErrorStateListener,
): AbstractViewHolder<ErrorStateDataView>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.universal_search_error_state_layout
    }

    private var binding: UniversalSearchErrorStateLayoutBinding? by viewBinding()


    override fun bind(element: ErrorStateDataView?) {
        bindCtaClick()
    }

    private fun bindCtaClick() {
        binding?.universalSearchErrorState?.setActionClickListener {
            errorStateListener.onReload()
        }
    }

}