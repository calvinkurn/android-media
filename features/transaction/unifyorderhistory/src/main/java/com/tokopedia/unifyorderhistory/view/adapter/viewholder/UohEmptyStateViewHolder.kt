package com.tokopedia.unifyorderhistory.view.adapter.viewholder

import android.view.View
import com.tokopedia.unifyorderhistory.data.model.UohEmptyState
import com.tokopedia.unifyorderhistory.data.model.UohTypeData
import com.tokopedia.unifyorderhistory.view.adapter.UohItemAdapter
import com.tokopedia.unifyorderhistory.R
import kotlinx.android.synthetic.main.uoh_empty_state.view.*

/**
 * Created by fwidjaja on 22/07/20.
 */
class UohEmptyStateViewHolder(itemView: View, private val actionListener: UohItemAdapter.ActionListener?) : UohItemAdapter.BaseViewHolder<UohTypeData>(itemView) {

    override fun bind(item: UohTypeData, position: Int) {
        if (item.dataObject is UohEmptyState) {
            itemView.uoh_empty_state?.apply {
                setImageDrawable(item.dataObject.drawableSrc)
                setTitle(item.dataObject.title)
                setDescription(item.dataObject.desc)
                if (item.dataObject.isBtnShow) {
                    setPrimaryCTAText(item.dataObject.btnStr)
                    if (item.dataObject.btnStr == resources.getString(R.string.uoh_filter_empty_btn)) {
                        setPrimaryCTAClickListener {
                            actionListener?.onEmptyResultResetBtnClicked()
                        }
                    } else if (item.dataObject.btnStr == resources.getString(R.string.uoh_no_order_btn)) {
                        setPrimaryCTAClickListener {
                            actionListener?.onMulaiBelanjaBtnClicked()
                        }
                    }
                }
            }
        }
    }
}