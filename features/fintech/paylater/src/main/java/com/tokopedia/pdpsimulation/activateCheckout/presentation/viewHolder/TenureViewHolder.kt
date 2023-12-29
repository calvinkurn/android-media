package com.tokopedia.pdpsimulation.activateCheckout.presentation.viewHolder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureDetail
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureSelectedModel
import com.tokopedia.pdpsimulation.activateCheckout.listner.ActivationListner
import com.tokopedia.pdpsimulation.common.utils.Util
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.utils.resources.isDarkMode
import kotlinx.android.synthetic.main.paylater_activation_individual_tenure.view.*

class TenureViewHolder(itemView: View, private val tenureSelectListener: ActivationListner) :
    RecyclerView.ViewHolder(itemView) {

    fun bindData(
        tenureDetail: TenureDetail,
        tenureSelectedModel: TenureSelectedModel?,
        currentPosition: Int
    ) {
        tenureSelectedModel?.let {
            itemView.visible()
            itemView.apply {
                changeViewColor(tenureDetail.tenureDisable)
                updateData(tenureDetail)
                if (!tenureDetail.tenureDisable) {
                    setTenureEnableLogic(tenureDetail, it, currentPosition)
                }
                else {
                    radioSelector.isChecked = false
                    individualTenureItemContainer.isClickable = false
                    radioSelector.isClickable = false
                }
                try {
                    radioSelector.skipAnimation()
                } catch (e: Exception) {}

            }
        }?:run{
            itemView.gone()
        }

    }

    @SuppressLint("PII Data Exposure")
    private fun View.setTenureEnableLogic(
        tenureDetail: TenureDetail,
        tenureSelectedModel: TenureSelectedModel,
        currentPosition: Int
    ) {
        if (tenureDetail.isSelectedTenure && !tenureDetail.tenureDisable) {
            individualTenureItemContainer.cardType = CardUnify.TYPE_BORDER_ACTIVE
            radioSelector.isChecked = true
        } else {
            individualTenureItemContainer.cardType = CardUnify.TYPE_BORDER
            radioSelector.isChecked = false
        }
        individualTenureItemContainer.isClickable = true
        radioSelector.isClickable = true
        individualTenureItemContainer.setOnClickListener {
            tenureSelectListener.selectedTenure(
                tenureSelectedModel,
                currentPosition,
                tenureDetail.promoName.orEmpty(),
            )
        }
        radioSelector.setOnClickListener {
            tenureSelectListener.selectedTenure(
                tenureSelectedModel,
                currentPosition,
                tenureDetail.promoName.orEmpty(),
            )
        }
    }

    private fun updateData(tenureDetail: TenureDetail) {
        itemView.apply {
            paymentDetailHeader.text = tenureDetail.chip_title.orEmpty()
            paymentDetailSubHeader.text = Util.getTextRBPRemoteConfig(
                context,
                tenureDetail.description.orEmpty(),
                if (context.isDarkMode()) tenureDetail.chipSubtitleDark.parseAsHtml()
                else tenureDetail.chipSubtitleLight.parseAsHtml()
            )

            if (!tenureDetail.lable.isNullOrBlank() && !tenureDetail.tenureDisable) {
                tenureRecommendation.visibility = View.VISIBLE
                tenureRecommendation.text = tenureDetail.lable
            } else
                tenureRecommendation.visibility = View.GONE
        }
    }

    private fun changeViewColor(disable: Boolean) {
        itemView.apply {
            if (disable) {
                setDisabledLogic()

            } else {
                setEnabledLogic()

            }
        }

    }

    private fun setDisabledLogic() {
        itemView.apply {
            paymentDetailHeader.isEnabled = false
            paymentDetailSubHeader.isEnabled = false
            radioSelector.isEnabled = false
            containerInCard.setBackgroundResource(com.tokopedia.unifyprinciples.R.color.Unify_NN0)
            individualTenureItemContainer.cardType = CardUnify.TYPE_BORDER

        }
    }

    private fun setEnabledLogic() {
        itemView.apply {
            paymentDetailHeader.isEnabled = true
            paymentDetailSubHeader.isEnabled = true
            radioSelector.isEnabled = true
        }
    }


    companion object {
        private val LAYOUT_ID = R.layout.paylater_activation_individual_tenure

        fun getViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup,
            tenureSelectListener: ActivationListner
        ) =
            TenureViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false), tenureSelectListener
            )
    }
}
