package com.tokopedia.topads.dashboard.view.adapter.autoads.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.view.adapter.autoads.viewmodel.AutoAdsItemsEmptyModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Pika on 2/6/20.
 */

class AutoAdsItemsEmptyViewHolder(val view: View) :
    AutoAdsItemsViewHolder<AutoAdsItemsEmptyModel>(view) {

    private val imageEmpty: ImageUnify = view.findViewById(R.id.image_empty)
    private val textTitle: Typography = view.findViewById(R.id.text_title)
    private val textDesc: Typography = view.findViewById(R.id.text_desc)
    private val btnSubmit: UnifyButton = view.findViewById(R.id.btn_submit)

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.topads_dash_group_empty_state
    }

    override fun bind(item: AutoAdsItemsEmptyModel, statsData: MutableList<WithoutGroupDataItem>) {
        imageEmpty.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.no_products))
        textTitle.text = view.context.getString(R.string.topads_dash_empty_non_group_title)
        textDesc.text = view.context.getString(R.string.topads_dash_empty_non_group_desc)
        btnSubmit.text = view.context.getString(R.string.topads_dash_empty_non_group_butt)

        btnSubmit.setOnClickListener {
            RouteManager.route(it.context, ApplinkConstInternalTopAds.TOPADS_AUTOADS_CREATE)
        }
    }
}
