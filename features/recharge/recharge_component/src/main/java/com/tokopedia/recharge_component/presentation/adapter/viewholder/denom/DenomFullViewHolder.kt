package com.tokopedia.recharge_component.presentation.adapter.viewholder.denom

import android.graphics.Paint
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.recharge_component.R
import com.tokopedia.recharge_component.databinding.ViewRechargeDenomFullBinding
import com.tokopedia.recharge_component.listener.RechargeDenomFullListener
import com.tokopedia.recharge_component.model.denom.DenomConst
import com.tokopedia.recharge_component.model.denom.DenomConst.setStatusNormal
import com.tokopedia.recharge_component.model.denom.DenomConst.setStatusOutOfStockColor
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifyprinciples.R.dimen as unifyDimens

class DenomFullViewHolder(
    private val denomFullListener: RechargeDenomFullListener,
    private val binding: ViewRechargeDenomFullBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(denomFull: DenomData, denomType: DenomWidgetEnum,
             isSelectedItem: Boolean, isOnlyOneSize: Boolean, position: Int, isPlacebo: Boolean = false){

        with(binding){
            tgDenomFullTitle.run {
                if (!denomFull.title.isNullOrEmpty()){
                    show()
                    setStatusOutOfStockColor(denomFull.status, context)
                    text = denomFull.title
                } else hide()
            }

            tgDenomFullDesc.run {
                if (!denomFull.description.isNullOrEmpty()){
                    show()
                    setStatusOutOfStockColor(denomFull.status, context)
                    text = denomFull.description
                } else hide()
            }

            tgDenomFullQuota.run {
                if (!denomFull.quotaInfo.isNullOrEmpty()){
                    show()
                    setStatusOutOfStockColor(denomFull.status, context)
                    text = denomFull.quotaInfo
                } else hide()
            }

            viewSeparatorTypeDenomFull.run {
                if (!denomFull.expiredDays.isNullOrEmpty() && !denomFull.quotaInfo.isNullOrEmpty()){
                    show()
                } else hide()
            }

            tgDenomFullExpired.run {
                if (!denomFull.expiredDays.isNullOrEmpty()){
                    show()
                    setStatusOutOfStockColor(denomFull.status, context)
                    text = denomFull.expiredDays

                    val labelParams = this.layoutParams as ConstraintLayout.LayoutParams

                    if(!denomFull.quotaInfo.isNullOrEmpty()){

                        setMargin(
                             getDimens(unifyDimens.spacing_lvl3),
                             getDimens(unifyDimens.spacing_lvl1),
                             getDimens(unifyDimens.unify_space_0),
                             getDimens(unifyDimens.unify_space_0)
                                )

                        labelParams.topToBottom = ConstraintLayout.LayoutParams.UNSET
                        labelParams.leftToLeft = ConstraintLayout.LayoutParams.UNSET
                        labelParams.topToTop = viewSeparatorTypeDenomFull.id
                        labelParams.leftToRight = viewSeparatorTypeDenomFull.id
                        labelParams.bottomToBottom = viewSeparatorTypeDenomFull.id

                        viewSeparatorTypeDenomFull.run {
                            val viewSeparatorlabelParams = this.layoutParams as ConstraintLayout.LayoutParams
                            viewSeparatorlabelParams.topToTop = tgDenomFullQuota.id
                            viewSeparatorlabelParams.leftToRight = tgDenomFullQuota.id
                            viewSeparatorlabelParams.bottomToBottom = tgDenomFullQuota.id
                            this.layoutParams = viewSeparatorlabelParams
                        }

                    } else {

                        labelParams.topToTop = ConstraintLayout.LayoutParams.UNSET
                        labelParams.leftToRight = ConstraintLayout.LayoutParams.UNSET
                        labelParams.topToBottom = tgDenomFullDesc.id
                        labelParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
                        labelParams.bottomToTop = labelDenomFullSpecial.id

                        labelDenomFullSpecial.run {
                            val labelDenomSpeciallabelParams = this.layoutParams as ConstraintLayout.LayoutParams
                            labelDenomSpeciallabelParams.topToBottom = tgDenomFullExpired.id
                            this.layoutParams = labelDenomSpeciallabelParams
                        }

                        tgDenomFullDesc.run {
                            val tgDenomFullDesclabelParams = this.layoutParams as ConstraintLayout.LayoutParams
                            tgDenomFullDesclabelParams.bottomToTop = tgDenomFullExpired.id
                            this.layoutParams = tgDenomFullDesclabelParams
                        }

                        setMargin(
                             getDimens(unifyDimens.unify_space_0),
                             getDimens(unifyDimens.spacing_lvl1),
                             getDimens(unifyDimens.unify_space_0),
                             getDimens(unifyDimens.unify_space_0))
                    }

                    layoutParams = labelParams
                } else hide()
            }

            labelDenomFullSpecial.run {
                if (!denomFull.specialLabel.isNullOrEmpty() && denomFull.status != DenomConst.DENOM_STATUS_OUT_OF_STOCK){
                    show()
                    text = denomFull.specialLabel
                } else hide()
            }

            tgDenomFullPrice.run {
                if (!denomFull.price.isNullOrEmpty()){
                    show()
                    setStatusOutOfStockColor(denomFull.status, context)
                    text = denomFull.price
                } else hide()
            }

            labelDenomFullDiscount.run {
                if (denomFull.status == DenomConst.DENOM_STATUS_OUT_OF_STOCK) {

                    show()
                    setStatusOutOfStockColor(resources.getString(com.tokopedia.recharge_component.R.string.out_of_stock_label_denom_digital))

                    val labelParams = this.layoutParams as ConstraintLayout.LayoutParams
                    labelParams.topToTop = ConstraintLayout.LayoutParams.UNSET
                    labelParams.topToBottom = tgDenomFullPrice.id
                    labelParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                    labelParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
                    layoutParams = labelParams

                    setMargin(
                         getDimens(unifyDimens.unify_space_0),
                         getDimens(unifyDimens.spacing_lvl2),
                         getDimens(unifyDimens.unify_space_0),
                         getDimens(unifyDimens.unify_space_0))

                } else if (!denomFull.discountLabel.isNullOrEmpty()){

                    show()
                    val labelParams = this.layoutParams as ConstraintLayout.LayoutParams
                    labelParams.topToTop = tgDenomFullPrice.id
                    labelParams.topToBottom = ConstraintLayout.LayoutParams.UNSET
                    labelParams.bottomToBottom = tgDenomFullPrice.id
                    labelParams.leftToRight = tgDenomFullPrice.id
                    labelParams.leftToLeft = ConstraintLayout.LayoutParams.UNSET
                    layoutParams = labelParams

                    setMargin(
                         getDimens(unifyDimens.spacing_lvl2),
                         getDimens(unifyDimens.unify_space_0),
                         getDimens(unifyDimens.unify_space_0),
                         getDimens(unifyDimens.unify_space_0))

                    setStatusNormal(denomFull.discountLabel)

                } else hide()
            }

            tgDenomFullSlashPrice.run {
                if (!denomFull.slashPrice.isNullOrEmpty() && denomFull.status != DenomConst.DENOM_STATUS_OUT_OF_STOCK) {
                    show()
                    text = denomFull.slashPrice
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else hide()
            }


            tgDenomFullSoldPercentageLabel.run {
                if (!denomFull.flashSaleLabel.isNullOrEmpty() && denomFull.status != DenomConst.DENOM_STATUS_OUT_OF_STOCK){
                    show()
                    text = denomFull.flashSaleLabel
                } else hide()
            }

            pgDenomFullFlashSale.run {
                if (denomFull.flashSalePercentage.isMoreThanZero() && denomFull.status != DenomConst.DENOM_STATUS_OUT_OF_STOCK) {
                    show()
                    setProgressIcon(
                        icon = ContextCompat.getDrawable(
                            context,
                            com.tokopedia.resources.common.R.drawable.ic_fire_filled_product_card
                        ),
                        width = getDimens(R.dimen.widget_denom_flash_sale_image_width),
                        height = getDimens(R.dimen.widget_denom_flash_sale_image_height)
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
                    if (denomFull.status != DenomConst.DENOM_STATUS_OUT_OF_STOCK)
                    denomFullListener.onChevronDenomClicked(denomFull, position, denomType)
                }
            }

            cardDenomFull.run {
                if (!isPlacebo) {
                    layoutParams.width = if (denomType == DenomWidgetEnum.FULL_TYPE || isOnlyOneSize){
                        ViewGroup.LayoutParams.MATCH_PARENT
                    } else getDimens(R.dimen.widget_denom_full_width)

                    layoutParams.height = if (denomType == DenomWidgetEnum.MCCM_FULL_TYPE){
                        ViewGroup.LayoutParams.MATCH_PARENT
                    } else ViewGroup.LayoutParams.WRAP_CONTENT
                }

                cardType = if(denomFull.status == DenomConst.DENOM_STATUS_OUT_OF_STOCK) CardUnify.TYPE_BORDER_DISABLED
                    else if (isSelectedItem) CardUnify.TYPE_BORDER_ACTIVE
                    else if (denomType == DenomWidgetEnum.MCCM_FULL_TYPE) CardUnify.TYPE_SHADOW
                    else CardUnify.TYPE_BORDER

                if (denomType == DenomWidgetEnum.MCCM_FULL_TYPE && isOnlyOneSize){
                    setMargin(0, 0,  getDimens(unifyDimens.spacing_lvl3), 0)
                }
            }

            root.setOnClickListener {
                if(denomFull.status != DenomConst.DENOM_STATUS_OUT_OF_STOCK) {
                    denomFullListener.onDenomFullClicked(denomFull, denomType, position, "", true)
                }
            }

            root.addOnImpressionListener(denomFull) {
                denomFullListener.onDenomFullImpression(denomFull, denomType, position)
            }
        }
    }
}