package com.tokopedia.promocheckoutmarketplace.presentation.viewholder

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.databinding.PromoCheckoutMarketplaceModuleItemPromoListHeaderEnabledBinding
import com.tokopedia.promocheckoutmarketplace.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.promocheckoutmarketplace.presentation.setImageFilterNormal
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoListHeaderUiModel

class PromoListHeaderEnabledViewHolder(private val viewBinding: PromoCheckoutMarketplaceModuleItemPromoListHeaderEnabledBinding,
                                       private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoListHeaderUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.promo_checkout_marketplace_module_item_promo_list_header_enabled
    }

    override fun bind(element: PromoListHeaderUiModel) {
        with(viewBinding) {
            if (element.uiData.iconUrl.isNotBlank()) {
                ImageHandler.loadImageRounded2(itemView.context, imagePromoListHeader, element.uiData.iconUrl)
                imagePromoListHeader.show()
            } else {
                imagePromoListHeader.gone()
            }

            labelPromoListHeaderTitle.text = element.uiData.title
            if (element.uiState.hasSelectedPromoItem) {
                labelPromoListHeaderSubTitle.text = itemView.context.getString(R.string.label_subtitle_promo_selected)
                labelPromoListHeaderSubTitle.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_T500))
            } else {
                labelPromoListHeaderSubTitle.text = itemView.context.getString(R.string.label_subtitle_only_one_promo)
                labelPromoListHeaderSubTitle.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
            }

            if (!element.uiState.isCollapsed) {
                imageChevron.rotation = 180f
            } else {
                imageChevron.rotation = 0f
            }

            setImageFilterNormal(imagePromoListHeader)
            labelPromoListHeaderSubTitle.show()
            imageChevron.show()
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) listener.onClickPromoListHeader(element)
            }
        }
    }

}