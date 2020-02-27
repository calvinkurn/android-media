package com.tokopedia.purchase_platform.features.promo.presentation.viewholder

import android.content.Context
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker.getDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.promo.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.purchase_platform.features.promo.presentation.setImageFilterGrayScale
import com.tokopedia.purchase_platform.features.promo.presentation.setImageFilterNormal
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoListItemUiModel
import com.tokopedia.unifycomponents.CardUnify
import kotlinx.android.synthetic.main.item_promo_list_item.view.*

class PromoListItemViewHolder(private val view: View,
                              private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoListItemUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_promo_list_item
    }

    override fun bind(element: PromoListItemUiModel) {
        if (element.uiData.imageResourceUrl.isNotBlank()) {
            ImageHandler.loadImageRounded2(itemView.context, itemView.image_promo_item, element.uiData.imageResourceUrl)
            itemView.label_promo_item_title.setMargin(0, itemView.context.resources.getDimension(R.dimen.dp_8).toInt(),
                    itemView.context.resources.getDimension(R.dimen.dp_12).toInt(), 0)
            itemView.image_promo_item.show()
        } else {
            itemView.label_promo_item_title.setMargin(0, 0, itemView.context.resources.getDimension(R.dimen.dp_12).toInt(), 0)
            itemView.image_promo_item.gone()
        }

        itemView.label_promo_item_title.text = element.uiData.title
        itemView.label_promo_item_sub_title.text = element.uiData.subTitle
        if (element.uiState.isEnabled) {
            renderEnablePromoItem(element)
        } else {
            renderDisablePromoItem(element)
        }

        itemView.card_promo_item.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION && element.uiState.isEnabled) {
                element.uiState.isSellected = !element.uiState.isSellected
                renderEnablePromoItem(element)

                listener.onClickPromoListItem(element)
            }
        }

    }

    private fun renderEnablePromoItem(element: PromoListItemUiModel) {
        itemView.label_promo_item_error_message.gone()
        itemView.label_promo_item_sub_title.setMargin(0, itemView.context.resources.getDimension(R.dimen.dp_2).toInt(),
                itemView.context.resources.getDimension(R.dimen.dp_12).toInt(), 0)
        if (element.uiState.isSellected) {
            itemView.card_promo_item.cardType = CardUnify.TYPE_BORDER_ACTIVE
            itemView.image_select_promo.show()
        } else {
            itemView.card_promo_item.cardType = CardUnify.TYPE_BORDER
            itemView.image_select_promo.gone()
        }
        setImageFilterNormal(itemView.image_promo_item)
    }

    private fun renderDisablePromoItem(element: PromoListItemUiModel) {
        itemView.card_promo_item.cardType = CardUnify.TYPE_BORDER_DISABLED
        itemView.label_promo_item_error_message.text = element.uiData.errorMessage
        itemView.label_promo_item_error_message.show()
        itemView.label_promo_item_sub_title.setMargin(0, itemView.context.resources.getDimension(R.dimen.dp_8).toInt(),
                itemView.context.resources.getDimension(R.dimen.dp_12).toInt(), 0)
        itemView.image_select_promo.gone()
        setImageFilterGrayScale(itemView.image_promo_item)
    }

}