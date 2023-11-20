package com.tokopedia.topads.dashboard.view.adapter.group_item.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.data.response.groupitem.DataItem
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.common.data.model.CountDataItem
import com.tokopedia.topads.dashboard.view.adapter.group_item.viewmodel.GroupItemsEmptyModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Pika on 2/6/20.
 */

class GroupItemsEmptyViewHolder(val view: View) : GroupItemsViewHolder<GroupItemsEmptyModel>(view) {

    private val imageEmpty: ImageUnify = view.findViewById(R.id.image_empty)
    private val textTitle: Typography = view.findViewById(R.id.text_title)
    private val textDesc: Typography = view.findViewById(R.id.text_desc)
    private val btnSubmit: UnifyButton = view.findViewById(R.id.btn_submit)

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.topads_dash_group_empty_state
    }

    override fun bind(item: GroupItemsEmptyModel, selectedMode: Boolean, fromSearch: Boolean, statsData: MutableMap<String, DataItem>, countList: MutableList<CountDataItem>) {
        if (!fromSearch) {
            textTitle.text = view.context.getString(R.string.topads_dash_empty_group_title)
            textDesc.text = view.context.getString(R.string.topads_dash_empty_group_desc)
            btnSubmit.visibility = View.VISIBLE
            btnSubmit.setOnClickListener {
                RouteManager.route(view.context, ApplinkConstInternalTopAds.TOPADS_CREATE_ADS)
            }
        } else {
            textTitle.text = view.context.getString(R.string.topads_dash_group_no_search_result_title)
            textDesc.text = view.context.getString(R.string.topads_empty_on_search_desc)
            btnSubmit.visibility = View.GONE
        }
        imageEmpty.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.no_products))
    }
}
