package com.tokopedia.search.result.presentation.view.adapter.viewholder.profile

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.EmptySearchProfileViewModel
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener
import kotlinx.android.synthetic.main.search_result_profile_empty_layout.view.*

class EmptySearchProfileViewHolder(view: View, private val emptyStateListener: EmptyStateListener) : AbstractViewHolder<EmptySearchProfileViewModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_result_profile_empty_layout
    }

    override fun bind(model: EmptySearchProfileViewModel) {
        itemView.buttonSearchProfileEmptySearchAgain?.setOnClickListener {
            emptyStateListener.onEmptyButtonClicked()
        }
    }
}