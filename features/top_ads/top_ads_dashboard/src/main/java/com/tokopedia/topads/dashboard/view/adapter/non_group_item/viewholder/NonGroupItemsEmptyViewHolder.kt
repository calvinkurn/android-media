package com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewmodel.NonGroupItemsEmptyModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Pika on 2/6/20.
 */

class NonGroupItemsEmptyViewHolder(val view: View) :
    NonGroupItemsViewHolder<NonGroupItemsEmptyModel>(view) {

    private val imageEmpty: ImageUnify? = view.findViewById(R.id.image_empty)
    private val textTitle: Typography? = view.findViewById(R.id.text_title)
    private val textDesc: Typography? = view.findViewById(R.id.text_desc)
    private val btnSubmit: UnifyButton? = view.findViewById(R.id.btn_submit)

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.topads_dash_group_empty_state
    }

    override fun bind(
        item: NonGroupItemsEmptyModel, selectedMode: Boolean, fromSearch: Boolean,
        statsData: MutableList<WithoutGroupDataItem>,
    ) {
        item.let {
            btnSubmit?.visibility = View.GONE
            if (!fromSearch) {
                textTitle?.text =
                    view.context.getString(R.string.topads_dash_empty_non_group_title)
                textDesc?.text = view.context.getString(R.string.topads_dash_empty_non_group_desc)
                btnSubmit?.text = view.context.getString(R.string.topads_dash_empty_non_group_butt)
            } else {
                textTitle?.text =
                    view.context.getString(R.string.topads_dash_non_group_no_search_result_title)
                textDesc?.text = view.context.getString(R.string.topads_empty_on_search_desc)
            }
            imageEmpty?.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.no_products))
        }
    }

}