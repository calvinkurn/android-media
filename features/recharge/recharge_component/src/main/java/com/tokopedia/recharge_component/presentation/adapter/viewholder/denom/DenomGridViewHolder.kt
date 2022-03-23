package com.tokopedia.recharge_component.presentation.adapter.viewholder.denom

import android.graphics.Paint
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.recharge_component.R
import com.tokopedia.recharge_component.databinding.ViewRechargeDenomGridBinding
import com.tokopedia.recharge_component.listener.RechargeDenomGridListener
import com.tokopedia.recharge_component.model.denom.DenomConst.DENOM_STATUS_OUT_OF_STOCK
import com.tokopedia.recharge_component.model.denom.DenomConst.setStatusNormal
import com.tokopedia.recharge_component.model.denom.DenomConst.setStatusOutOfStockColor
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifyprinciples.R.dimen as unifyDimens

class DenomGridViewHolder (
    private val denomGridListener: RechargeDenomGridListener,
    private val binding: ViewRechargeDenomGridBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(denomGrid: DenomData, denomType: DenomWidgetEnum,
             isSelectedItem: Boolean, position: Int){

        with(binding){
            tgDenomGridTitle.run {
                if (!denomGrid.title.isNullOrEmpty()){
                    show()
                    setStatusOutOfStockColor(denomGrid.status, context)
                    text = denomGrid.title
                } else hide()
            }

            labelDenomGridSpecial.run {
                if (!denomGrid.specialLabel.isNullOrEmpty() && denomGrid.status != DENOM_STATUS_OUT_OF_STOCK){
                    show()
                    text = denomGrid.specialLabel
                } else hide()
            }

            tgDenomGridPeriode.run {
                if (!denomGrid.expiredDate.isNullOrEmpty()){
                    show()
                    setStatusOutOfStockColor(denomGrid.status, context)
                    text = denomGrid.expiredDate
                } else hide()
            }

            tgDenomGridPrice.run {
                if (!denomGrid.price.isNullOrEmpty()){
                    show()
                    setStatusOutOfStockColor(denomGrid.status, context)
                    text = denomGrid.price
                } else hide()
            }

            labelDenomGridDiscount.run {
                if (denomGrid.status == DENOM_STATUS_OUT_OF_STOCK){
                    show()
                    setStatusOutOfStockColor(resources.getString(com.tokopedia.recharge_component.R.string.out_of_stock_label_denom_digital))

                    val labelParams = this.layoutParams as ConstraintLayout.LayoutParams
                    labelParams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
                    labelParams.topToTop = ConstraintLayout.LayoutParams.UNSET
                    labelParams.topToBottom = tgDenomGridPrice.id
                    labelParams.bottomToTop = pgDenomGridFlashSale.id
                    layoutParams = labelParams

                    tgDenomGridPrice.run {
                        val priceLabelParam = this.layoutParams as ConstraintLayout.LayoutParams
                        priceLabelParam.bottomToTop = labelDenomGridDiscount.id
                        this.layoutParams = priceLabelParam
                    }

                    pgDenomGridFlashSale.run {
                        val flashSaleLabelParam = this.layoutParams as ConstraintLayout.LayoutParams
                        flashSaleLabelParam.topToBottom = labelDenomGridDiscount.id
                        this.layoutParams = flashSaleLabelParam
                    }

                    setMargin(
                        getDimens(unifyDimens.unify_space_0),
                        getDimens(unifyDimens.spacing_lvl2),
                        getDimens(unifyDimens.unify_space_0),
                        getDimens(unifyDimens.unify_space_0)
                            )

                } else if (!denomGrid.discountLabel.isNullOrEmpty()){
                    show()

                    val labelParams = this.layoutParams as ConstraintLayout.LayoutParams
                    labelParams.topToBottom = ConstraintLayout.LayoutParams.UNSET
                    labelParams.bottomToTop = ConstraintLayout.LayoutParams.UNSET
                    labelParams.bottomToBottom = tgDenomGridSlashPrice.id
                    labelParams.topToTop = tgDenomGridSlashPrice.id
                    layoutParams = labelParams

                    tgDenomGridPrice.run {
                        val priceLabelParam = this.layoutParams as ConstraintLayout.LayoutParams
                        priceLabelParam.bottomToTop = tgDenomGridSlashPrice.id
                        this.layoutParams = priceLabelParam
                    }

                    pgDenomGridFlashSale.run {
                        val flashSaleLabelParam = this.layoutParams as ConstraintLayout.LayoutParams
                        flashSaleLabelParam.topToBottom = tgDenomGridSlashPrice.id
                        this.layoutParams = flashSaleLabelParam
                    }

                    setMargin(
                        getDimens(unifyDimens.unify_space_0),
                        getDimens(unifyDimens.unify_space_0),
                        getDimens(unifyDimens.unify_space_0),
                        getDimens(unifyDimens.unify_space_0)
                    )

                    setStatusNormal(denomGrid.discountLabel)
                } else hide()
            }


            tgDenomGridSoldPercentageLabel.run {
                if (!denomGrid.flashSaleLabel.isNullOrEmpty() && denomGrid.status != DENOM_STATUS_OUT_OF_STOCK){
                    show()
                    text = denomGrid.flashSaleLabel
                } else hide()
            }

            pgDenomGridFlashSale.run {
                if (denomGrid.flashSalePercentage.isMoreThanZero() && denomGrid.status != DENOM_STATUS_OUT_OF_STOCK) {
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
                    setValue(denomGrid.flashSalePercentage, false)
                } else hide()
            }

            tgDenomGridSlashPrice.run {
                if (!denomGrid.slashPrice.isNullOrEmpty() && denomGrid.status != DENOM_STATUS_OUT_OF_STOCK) {
                    show()
                    text = denomGrid.slashPrice
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    if(denomGrid.discountLabel.isNullOrEmpty()) {
                        setMargin(
                            getDimens(unifyDimens.unify_space_0),
                            getDimens(unifyDimens.spacing_lvl2),
                            getDimens(unifyDimens.unify_space_0),
                            getDimens(unifyDimens.unify_space_0)
                        )
                    } else setMargin(
                        getDimens(unifyDimens.spacing_lvl2),
                        getDimens(unifyDimens.spacing_lvl3),
                        getDimens(unifyDimens.unify_space_0),
                        getDimens(unifyDimens.unify_space_0)
                    )
                } else if (denomType == DenomWidgetEnum.MCCM_GRID_TYPE && denomGrid.status != DENOM_STATUS_OUT_OF_STOCK) invisible()
                  else hide()
            }

            cardDenomGrid.run {
                layoutParams.width = if (denomType == DenomWidgetEnum.GRID_TYPE){
                    ViewGroup.LayoutParams.MATCH_PARENT
                } else getDimens(R.dimen.widget_denom_grid_width)

                layoutParams.height = if (denomType == DenomWidgetEnum.MCCM_GRID_TYPE){
                    getDimens (R.dimen.widget_denom_grid_height)
                } else ViewGroup.LayoutParams.WRAP_CONTENT

                setBackgroundColor(ContextCompat.getColor(rootView.context, com.tokopedia.unifyprinciples.R.color.Unify_Background))

                cardType = if(denomGrid.status == DENOM_STATUS_OUT_OF_STOCK) CardUnify.TYPE_BORDER_DISABLED
                        else if (isSelectedItem) CardUnify.TYPE_BORDER_ACTIVE
                        else if (denomType == DenomWidgetEnum.MCCM_GRID_TYPE) CardUnify.TYPE_SHADOW
                        else CardUnify.TYPE_BORDER
            }

            root.setOnClickListener {
                if(denomGrid.status != DENOM_STATUS_OUT_OF_STOCK) {
                    denomGridListener.onDenomGridClicked(denomGrid, denomType, position, "", true)
                }
            }

            root.addOnImpressionListener(denomGrid, {
                denomGridListener.onDenomGridImpression(denomGrid, denomType, position)
            })
        }
    }
}