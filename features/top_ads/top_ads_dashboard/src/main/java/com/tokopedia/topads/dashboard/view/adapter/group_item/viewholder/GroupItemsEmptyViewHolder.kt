package com.tokopedia.topads.dashboard.view.adapter.group_item.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.view.adapter.group_item.viewmodel.GroupItemsEmptyViewModel
import kotlinx.android.synthetic.main.topads_dash_group_empty_state.view.*

/**
 * Created by Pika on 2/6/20.
 */

class GroupItemsEmptyViewHolder(val view: View) : GroupItemsViewHolder<GroupItemsEmptyViewModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.topads_dash_group_empty_state
    }

    override fun bind(item: GroupItemsEmptyViewModel, selectedMode: Boolean, fromSearch: Boolean) {
        if(!fromSearch) {
            view.text_title.text = view.context.getString(R.string.topads_dash_empty_group_title)
            view.text_desc.text = view.context.getString(R.string.topads_dash_empty_group_desc)
            view.btn_submit.visibility = View.VISIBLE
            view.btn_submit.setOnClickListener {
                RouteManager.route(view.context, ApplinkConstInternalTopAds.TOPADS_CREATE_ADS)
            }
        }
        else{
            view.text_title.text = view.context.getString(R.string.topads_dash_group_no_search_result_title)
            view.text_desc.text = view.context.getString(R.string.topads_empty_on_search_desc)
            view.btn_submit.visibility = View.GONE
        }
    }
}