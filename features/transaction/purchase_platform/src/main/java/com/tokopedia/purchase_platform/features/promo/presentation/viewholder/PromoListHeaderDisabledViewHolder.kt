package com.tokopedia.purchase_platform.features.promo.presentation.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.promo.presentation.PromoCheckoutActionListener
import com.tokopedia.purchase_platform.features.promo.presentation.setImageFilterGrayScale
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoListHeaderUiModel
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
        val LAYOUT = R.layout.item_promo_list_header_disabled
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