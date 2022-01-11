package com.tokopedia.recharge_component.presentation.adapter.viewholder.denom

import android.graphics.Paint
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.R
import com.tokopedia.recharge_component.databinding.ViewRechargeDenomGridBinding
import com.tokopedia.recharge_component.listener.RechargeDenomGridListener
import com.tokopedia.recharge_component.model.denom.DenomWidgetGridEnum
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ProgressBarUnify

class DenomGridViewHolder (
    private val denomGridListener: RechargeDenomGridListener,
    private val binding: ViewRechargeDenomGridBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(denomGrid: DenomWidgetModel, denomGridType: DenomWidgetGridEnum,
             isSelectedItem: Boolean, position: Int){

        with(binding){
            tgDenomGridTitle.run {
                if (!denomGrid.title.isNullOrEmpty()){
                    show()
                    text = denomGrid.title
                } else hide()
            }

            labelDenomGridSpecial.run {
                if (!denomGrid.specialLabel.isNullOrEmpty()){
                    show()
                    text = denomGrid.specialLabel
                } else hide()
            }

            tgDenomGridPeriode.run {
                if (!denomGrid.expiredDate.isNullOrEmpty()){
                    show()
                    text = denomGrid.expiredDate
                } else hide()
            }

            tgDenomGridPrice.run {
                if (!denomGrid.price.isNullOrEmpty()){
                    show()
                    text = denomGrid.price
                } else hide()
            }

            labelDenomGridDiscount.run {
                if (!denomGrid.discountLabel.isNullOrEmpty()){
                    show()
                    text = denomGrid.discountLabel
                } else hide()
            }


            tgDenomGridSoldPercentageLabel.run {
                if (!denomGrid.flashSaleLabel.isNullOrEmpty()){
                    show()
                    text = denomGrid.flashSaleLabel
                } else hide()
            }

            pgDenomGridFlashSale.run {
                if (denomGrid.flashSalePercentage.isMoreThanZero()) {
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
                    setValue(denomGrid.flashSalePercentage, false)
                } else hide()
            }

            tgDenomGridSlashPrice.run {
                if (!denomGrid.slashPrice.isNullOrEmpty()) {
                    show()
                    text = denomGrid.slashPrice
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    if(denomGrid.discountLabel.isNullOrEmpty()) {
                        setMargin(
                            resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                                .toInt(),
                            resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)
                                .toInt(),
                            resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                                .toInt(),
                            resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                                .toInt()
                        )
                    } else setMargin(
                        resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)
                            .toInt(),
                        resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
                            .toInt(),
                        resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                            .toInt(),
                        resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                            .toInt()
                    )
                } else hide()
            }

            cardDenomGrid.run {
                layoutParams.width = if (denomGridType == DenomWidgetGridEnum.GRID_TYPE){
                    ViewGroup.LayoutParams.MATCH_PARENT
                } else resources.getDimension(R.dimen.widget_denom_grid_width).toInt()

                cardType = if (isSelectedItem) CardUnify.TYPE_BORDER_ACTIVE else CardUnify.TYPE_BORDER
            }

            root.setOnClickListener {
                denomGridListener.onDenomGridClicked(denomGrid, position)
            }
        }
    }
}