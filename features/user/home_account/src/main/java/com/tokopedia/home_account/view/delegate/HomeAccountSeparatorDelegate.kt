package com.tokopedia.home_account.view.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.home_account.data.model.SeparatorView
import com.tokopedia.home_account.view.viewholder.SeparatorViewHolder

class HomeAccountSeparatorDelegate :
        TypedAdapterDelegate<SeparatorView, Any, SeparatorViewHolder>(
                SeparatorViewHolder.LAYOUT
        ) {

    override fun onBindViewHolder(item: SeparatorView, holder: SeparatorViewHolder) {
        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): SeparatorViewHolder {
        return SeparatorViewHolder(basicView)
    }
}