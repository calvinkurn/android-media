package com.tokopedia.content.common.ui.viewholder.tnc

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.content.common.R
import com.tokopedia.content.common.ui.model.TermsAndConditionUiModel
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by jegul on 04/10/21
 */
class PlayTermsAndConditionAdapter : BaseDiffUtilAdapter<TermsAndConditionUiModel>() {

    init {
        delegatesManager
            .addDelegate(AdapterDelegate())
    }

    override fun areItemsTheSame(
        oldItem: TermsAndConditionUiModel,
        newItem: TermsAndConditionUiModel
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: TermsAndConditionUiModel,
        newItem: TermsAndConditionUiModel
    ): Boolean {
        return oldItem == newItem
    }

    private class AdapterDelegate :
        TypedAdapterDelegate<TermsAndConditionUiModel, TermsAndConditionUiModel, ViewHolder>(
            ViewHolder.LAYOUT
    ) {
        override fun onBindViewHolder(item: TermsAndConditionUiModel, holder: ViewHolder) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ViewHolder {
            return ViewHolder(basicView)
        }
    }

    private class ViewHolder(itemView: View) : BaseViewHolder(itemView) {

        private val tvTnc = itemView.findViewById<Typography>(R.id.tv_tnc)

        fun bind(tnc: TermsAndConditionUiModel) {
            tvTnc.text = tnc.desc
        }

        companion object {
            val LAYOUT = R.layout.item_play_bro_tnc
        }
    }
}