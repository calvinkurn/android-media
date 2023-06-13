package com.tokopedia.unifyorderhistory.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifyorderhistory.R
import com.tokopedia.unifyorderhistory.data.model.UohEmptyState
import com.tokopedia.unifyorderhistory.data.model.UohTypeData
import com.tokopedia.unifyorderhistory.databinding.UohEmptyStateBinding
import com.tokopedia.unifyorderhistory.view.adapter.UohItemAdapter

/**
 * Created by fwidjaja on 22/07/20.
 */
class UohEmptyStateViewHolder(private val binding: UohEmptyStateBinding, private val actionListener: UohItemAdapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: UohTypeData) {
        if (item.dataObject is UohEmptyState) {
            binding.uohEmptyState.run {
                setImageUrl(item.dataObject.imgUrl)
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
