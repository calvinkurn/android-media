package com.tokopedia.recharge_component.presentation.adapter.viewholder.denom

import android.graphics.Paint
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.R
import com.tokopedia.recharge_component.databinding.ViewRechargeDenomFullBinding
import com.tokopedia.recharge_component.listener.RechargeDenomFullListener
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ProgressBarUnify

class DenomFullViewHolder(
    private val denomFullListener: RechargeDenomFullListener,
    private val binding: ViewRechargeDenomFullBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(denomFull: DenomWidgetModel, denomType: DenomWidgetEnum,
             isSelectedItem: Boolean, position: Int){

        with(binding){
            tgDenomFullTitle.run {
                if (!denomFull.title.isNullOrEmpty()){
                    show()
                    text = denomFull.title
                } else hide()
            }

            tgDenomFullDesc.run {
                if (!denomFull.description.isNullOrEmpty()){
                    show()
                    text = denomFull.description
                } else hide()
            }

            tgDenomFullQuota.run {
                if (!denomFull.quotaInfo.isNullOrEmpty()){
                    show()
                    text = denomFull.quotaInfo
                } else hide()
            }

            viewSeparatorTypeDenomFull.run {
                if (!denomFull.expiredDays.isNullOrEmpty()){
                    show()
                } else hide()
            }

            tgDenomFullExpired.run {
                if (!denomFull.expiredDays.isNullOrEmpty()){
                    show()
                    text = denomFull.expiredDays
                } else hide()
            }

            labelDenomFullSpecial.run {
                if (!denomFull.specialLabel.isNullOrEmpty()){
                    show()
                    text = denomFull.specialLabel
                } else hide()
            }

            tgDenomFullPrice.run {
                if (!denomFull.price.isNullOrEmpty()){
                    show()
                    text = denomFull.price
                } else hide()
            }

            labelDenomFullDiscount.run {
                if (!denomFull.discountLabel.isNullOrEmpty()){
                    show()
                    text = denomFull.discountLabel
                } else hide()
            }

            tgDenomFullSlashPrice.run {
                if (!denomFull.slashPrice.isNullOrEmpty()) {
                    show()
                    text = denomFull.slashPrice
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else hide()
            }


            tgDenomFullSoldPercentageLabel.run {
                if (!denomFull.flashSaleLabel.isNullOrEmpty()){
                    show()
                    text = denomFull.flashSaleLabel
                } else hide()
            }

            pgDenomFullFlashSale.run {
                if (denomFull.flashSalePercentage.isMoreThanZero()) {
                    show()
                    setProgressIcon(
                        icon = ContextCompat.getDrawable(
                            context,
                            com.tokopedia.resources.common.R.drawable.ic_fire_filled_product_card
                        ),
                        width = resources.getDimension(R.dimen.widget_denom_flash_sale_image_width)
                            .toInt(),
                        height = resources.getDimension(R.dimen.widget_denom_flash_sale_image_height)
                            .toInt()
                    )
                    progressBarColorType = ProgressBarUnify.COLOR_RED
                    setValue(denomFull.flashSalePercentage, false)
                } else hide()
            }

            iconCheveronDenomFull.run {
                if (denomFull.isShowChevron){
                    show()
                } else hide()

                setOnClickListener {
                    denomFullListener.onChevronDenomClicked(denomFull, position)
                }
            }

            cardDenomFull.run {
                layoutParams.width = if (denomType == DenomWidgetEnum.FULL_TYPE){
                    ViewGroup.LayoutParams.MATCH_PARENT
                } else resources.getDimension(R.dimen.widget_denom_full_width).toInt()

                cardType = if (isSelectedItem) CardUnify.TYPE_BORDER_ACTIVE else
                    if (denomType == DenomWidgetEnum.MCCM_TYPE) CardUnify.TYPE_SHADOW
                    else CardUnify.TYPE_BORDER
            }

            root.setOnClickListener {
                denomFullListener.onDenomFullClicked(denomFull, position)
            }
        }
    }
}