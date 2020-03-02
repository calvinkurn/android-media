package com.tokopedia.purchase_platform.features.promo.presentation.viewholder

import android.view.View
import android.widget.LinearLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.promo.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoEmptyStateUiModel
import kotlinx.android.synthetic.main.item_promo_empty.view.*

class PromoEmptyStateViewHolder(private val view: View,
                                private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoEmptyStateUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_promo_empty
    }

    override fun bind(element: PromoEmptyStateUiModel) {
        ImageHandler.loadImageRounded2(itemView.context, itemView.image_empty_state, element.uiData.imageUrl)
        itemView.label_empty_state_title.text = element.uiData.title
        itemView.label_empty_state_sub_title.text = element.uiData.subTitle

        if (element.uiState.isShowButton) {
            itemView.button_action.show()
        } else {
            itemView.button_action.gone()
        }

        val viewHeight = listener.getEmptyStateHeight()
        itemView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, viewHeight)
    }

}