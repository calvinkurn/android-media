package com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.viewholder

import android.view.View
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohTypeData
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.UohItemAdapter
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.LoaderUnify
import kotlinx.android.synthetic.main.uoh_loader_item.view.*

/**
 * Created by fwidjaja on 25/07/20.
 */
class UohLoaderItemViewHolder(itemView: View) : UohItemAdapter.BaseViewHolder<UohTypeData>(itemView) {
    override fun bind(item: UohTypeData, position: Int) {
        itemView.cl_loader.visible()
    }
}