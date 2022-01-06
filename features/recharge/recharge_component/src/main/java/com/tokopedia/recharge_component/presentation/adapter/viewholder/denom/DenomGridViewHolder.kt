package com.tokopedia.recharge_component.presentation.adapter.viewholder.denom

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recharge_component.R
import com.tokopedia.recharge_component.databinding.ViewRechargeDenomGridBinding
import com.tokopedia.recharge_component.listener.RechargeDenomGridListener
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel
import com.tokopedia.unifycomponents.ProgressBarUnify

class DenomGridViewHolder (
    private val denomGridListener: RechargeDenomGridListener,
    private val binding: ViewRechargeDenomGridBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(denomGrid: DenomWidgetModel){
        with(binding){
            tgDenomGridTitle.text = denomGrid.title
            labelDenomGridSpecial.text = denomGrid.specialLabel
            tgDenomGridPeriode.text = denomGrid.expiredDate
            tgDenomGridPrice.text = denomGrid.price
            labelDenomGridDiscount.text = denomGrid.discountLabel
            tgDenomGridSlashPrice.text = denomGrid.slashPrice
            pgDenomGridFlashSalse.apply {
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
            }

            root.setOnClickListener {
                denomGridListener.onDenomGridClicked(denomGrid.appLink)
            }
        }
    }
}