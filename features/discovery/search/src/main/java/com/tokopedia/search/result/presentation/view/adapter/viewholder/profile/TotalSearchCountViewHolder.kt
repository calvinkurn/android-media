package com.tokopedia.search.result.presentation.view.adapter.viewholder.profile

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.TotalSearchCountViewModel
import kotlinx.android.synthetic.main.search_layout_search_count.view.*

class TotalSearchCountViewHolder(val view: View) : AbstractViewHolder<TotalSearchCountViewModel>(view) {
    override fun bind(model: TotalSearchCountViewModel) {
        view.tv_search_count.text =
            String.format(
                view.context.getString(R.string.value_profile_count),
                model.searchCountText
            )
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_layout_search_count
    }
}
