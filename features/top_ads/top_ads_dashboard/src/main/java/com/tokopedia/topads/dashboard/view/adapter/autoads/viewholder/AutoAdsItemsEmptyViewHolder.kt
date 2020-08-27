package com.tokopedia.topads.dashboard.view.adapter.autoads.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.dashboard.view.adapter.autoads.viewmodel.AutoAdsItemsEmptyViewModel
import kotlinx.android.synthetic.main.topads_dash_group_empty_state.view.*

/**
 * Created by Pika on 2/6/20.
 */

class AutoAdsItemsEmptyViewHolder(val view: View) : AutoAdsItemsViewHolder<AutoAdsItemsEmptyViewModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.topads_dash_group_empty_state
    }
    override fun bind(item: AutoAdsItemsEmptyViewModel, statsData: MutableList<WithoutGroupDataItem>) {
        view.image_empty.setImageDrawable(view.context.getResDrawable(R.drawable.ill_no_product))
        view.text_title.text = view.context.getString(R.string.topads_dash_empty_non_group_title)
        view.text_desc.text = view.context.getString(R.string.topads_dash_empty_non_group_desc)
        view.btn_submit.text = view.context.getString(R.string.topads_dash_empty_non_group_butt)
    }
}
