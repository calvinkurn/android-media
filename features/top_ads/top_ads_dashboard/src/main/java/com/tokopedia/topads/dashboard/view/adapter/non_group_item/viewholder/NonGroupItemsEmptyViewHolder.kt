package com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewmodel.NonGroupItemsEmptyViewModel
import kotlinx.android.synthetic.main.topads_dash_group_empty_state.view.*

/**
 * Created by Pika on 2/6/20.
 */

class NonGroupItemsEmptyViewHolder(val view: View) : NonGroupItemsViewHolder<NonGroupItemsEmptyViewModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.topads_dash_group_empty_state
    }

    override fun bind(item: NonGroupItemsEmptyViewModel, selectedMode: Boolean, fromSearch: Boolean, statsData: MutableList<WithoutGroupDataItem>) {
        item.let {
            view.btn_submit.visibility = View.GONE
            if (!fromSearch) {
                view.text_title.text = view.context.getString(R.string.topads_dash_empty_non_group_title)
                view.text_desc.text = view.context.getString(R.string.topads_dash_empty_non_group_desc)
                view.btn_submit.text = view.context.getString(R.string.topads_dash_empty_non_group_butt)
            } else {
                view.text_title.text = view.context.getString(R.string.topads_dash_non_group_no_search_result_title)
                view.text_desc.text = view.context.getString(R.string.topads_empty_on_search_desc)
            }
        }
    }

}