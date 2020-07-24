package com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.viewholder

import android.view.View
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohEmptyState
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohTypeData
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.UohItemAdapter
import kotlinx.android.synthetic.main.uoh_empty_state.view.*

/**
 * Created by fwidjaja on 22/07/20.
 */
class UohEmptyStateViewHolder(itemView: View) : UohItemAdapter.BaseViewHolder<UohTypeData>(itemView) {

    override fun bind(item: UohTypeData, position: Int) {
        if (item.dataObject is UohEmptyState) {
            itemView.uoh_empty_state?.apply {
                setImageDrawable(item.dataObject.drawableSrc)
                setTitle(item.dataObject.title)
                setDescription(item.dataObject.desc)
                if (item.dataObject.isBtnShow) {
                    setPrimaryCTAText(item.dataObject.btnStr)
                }
            }
        }
    }
}