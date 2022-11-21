package com.tokopedia.content.common.ui.viewholder.tnc

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.content.common.R
import com.tokopedia.content.common.ui.model.tnc.TermsAndConditionBenefitUiModel
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by jegul on 04/10/21
 */
class PlayTermsAndConditionBenefitAdapter : BaseDiffUtilAdapter<TermsAndConditionBenefitUiModel>() {

    init {
        delegatesManager
            .addDelegate(AdapterDelegate())
    }

    override fun areItemsTheSame(
        oldItem: TermsAndConditionBenefitUiModel,
        newItem: TermsAndConditionBenefitUiModel
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: TermsAndConditionBenefitUiModel,
        newItem: TermsAndConditionBenefitUiModel
    ): Boolean {
        return oldItem == newItem
    }

    private class AdapterDelegate :
        TypedAdapterDelegate<TermsAndConditionBenefitUiModel, TermsAndConditionBenefitUiModel, ViewHolder>(
            ViewHolder.LAYOUT
        ) {
        override fun onBindViewHolder(item: TermsAndConditionBenefitUiModel, holder: ViewHolder) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ViewHolder {
            return ViewHolder(basicView)
        }
    }

    private class ViewHolder(itemView: View) : BaseViewHolder(itemView) {

        private val iconBenefit = itemView.findViewById<IconUnify>(R.id.icon_benefit)
        private val tvBenefitDesc = itemView.findViewById<Typography>(R.id.tv_benefit_desc)

        fun bind(benefit: TermsAndConditionBenefitUiModel) {
            iconBenefit.setImage(benefit.icon)
            tvBenefitDesc.setText(benefit.desc)
        }

        companion object {
            val LAYOUT = R.layout.item_play_bro_tnc_benefit
        }
    }
}