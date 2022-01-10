package com.tokopedia.recharge_component.presentation.adapter.viewholder.denom

import android.graphics.Paint
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recharge_component.R
import com.tokopedia.recharge_component.databinding.ViewRechargeDenomGridBinding
import com.tokopedia.recharge_component.listener.RechargeDenomGridListener
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ProgressBarUnify

class DenomGridViewHolder (
    private val denomGridListener: RechargeDenomGridListener,
    private val binding: ViewRechargeDenomGridBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(denomGrid: DenomWidgetModel, isSelectedItem: Boolean, position: Int){
        with(binding){
            tgDenomGridTitle.apply {
                if (!denomGrid.title.isNullOrEmpty()){
                    show()
                    text = denomGrid.title
                } else hide()
            }

            labelDenomGridSpecial.apply {
                if (!denomGrid.specialLabel.isNullOrEmpty()){
                    show()
                    text = denomGrid.specialLabel
                } else hide()
            }

            tgDenomGridPeriode.apply {
                if (!denomGrid.expiredDate.isNullOrEmpty()){
                    show()
                    text = denomGrid.expiredDate
                } else hide()
            }

            tgDenomGridPrice.apply {
                if (!denomGrid.price.isNullOrEmpty()){
                    show()
                    text = denomGrid.price
                } else hide()
            }

            labelDenomGridDiscount.apply {
                if (!denomGrid.discountLabel.isNullOrEmpty()){
                    show()
                    text = denomGrid.discountLabel
                } else hide()
            }


            tgDenomGridSoldPercentageLabel.apply {
                if (!denomGrid.flashSaleLabel.isNullOrEmpty()){
                    show()
                    text = denomGrid.flashSaleLabel
                } else hide()
            }
            pgDenomGridFlashSalse.apply {
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

            tgDenomGridSlashPrice.apply {
                if (!denomGrid.slashPrice.isNullOrEmpty()) {
                    show()
                    text = denomGrid.slashPrice
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else hide()
            }

            cardDenomGrid.cardType = if (isSelectedItem) CardUnify.TYPE_BORDER_ACTIVE else CardUnify.TYPE_SHADOW

            root.setOnClickListener {
                denomGridListener.onDenomGridClicked(denomGrid, position)
            }
        }
    }
}