package com.tokopedia.promocheckoutmarketplace.presentation.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.promocheckoutmarketplace.presentation.setImageFilterGrayScale
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoListHeaderUiModel
import com.tokopedia.unifyprinciples.Typography

class PromoListHeaderDisabledViewHolder(private val view: View,
                                        private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoListHeaderUiModel>(view) {

    private val imagePromoListHeader by lazy {
        view.findViewById<ImageView>(R.id.image_promo_list_header)
    }
    private val labelPromoListHeaderTitle by lazy {
        view.findViewById<Typography>(R.id.label_promo_list_header_title)
    }

    companion object {
        val LAYOUT = R.layout.promo_checkout_marketplace_module_item_promo_list_header_disabled
    }

    override fun bind(element: PromoListHeaderUiModel) {
        if (element.uiData.iconUrl.isNotBlank()) {
            ImageHandler.loadImageRounded2(itemView.context, imagePromoListHeader, element.uiData.iconUrl)
            imagePromoListHeader.show()
        } else {
            imagePromoListHeader.gone()
        }

        labelPromoListHeaderTitle.text = element.uiData.title

        setImageFilterGrayScale(imagePromoListHeader)
        itemView.setOnClickListener {}
    }

}