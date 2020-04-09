package com.tokopedia.purchase_platform.features.promo.presentation.viewholder

import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.promo.presentation.PromoCheckoutActionListener
import com.tokopedia.purchase_platform.features.promo.presentation.setImageFilterNormal
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoListHeaderUiModel
import com.tokopedia.unifyprinciples.Typography

class PromoListHeaderEnabledViewHolder(private val view: View,
                                       private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoListHeaderUiModel>(view) {

    private val imagePromoListHeader by lazy {
        view.findViewById<ImageView>(R.id.image_promo_list_header)
    }
    private val labelPromoListHeaderTitle by lazy {
        view.findViewById<Typography>(R.id.label_promo_list_header_title)
    }
    private val labelPromoListHeaderSubTitle by lazy {
        view.findViewById<Typography>(R.id.label_promo_list_header_sub_title)
    }
    private val imageChevron by lazy {
        view.findViewById<ImageView>(R.id.image_chevron)
    }

    companion object {
        val LAYOUT = R.layout.item_promo_list_header_enabled
    }

    override fun bind(element: PromoListHeaderUiModel) {
        if (element.uiData.iconUrl.isNotBlank()) {
            ImageHandler.loadImageRounded2(itemView.context, imagePromoListHeader, element.uiData.iconUrl)
            imagePromoListHeader.show()
        } else {
            imagePromoListHeader.gone()
        }

        labelPromoListHeaderTitle.text = element.uiData.title
        if (element.uiState.hasSelectedPromoItem) {
            labelPromoListHeaderSubTitle.text = itemView.context.getString(R.string.label_subtitle_promo_selected)
            labelPromoListHeaderSubTitle.setTextColor(ContextCompat.getColor(itemView.context, R.color.light_T500))
        } else {
            labelPromoListHeaderSubTitle.text = itemView.context.getString(R.string.label_subtitle_only_one_promo)
            labelPromoListHeaderSubTitle.setTextColor(ContextCompat.getColor(itemView.context, R.color.n_700_44))
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