package com.tokopedia.tokopedianow.annotation.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.annotation.analytic.AnnotationWidgetAnalytic
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.BrandWidgetItemUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowBrandWidgetItemBinding
import com.tokopedia.utils.view.binding.viewBinding

class BrandWidgetItemViewHolder(
    itemView: View,
    private val analytic: AnnotationWidgetAnalytic
) : AbstractViewHolder<BrandWidgetItemUiModel>(itemView) {

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_brand_widget_item
    }

    private val binding: ItemTokopedianowBrandWidgetItemBinding? by viewBinding()

    override fun bind(uiModel: BrandWidgetItemUiModel) {
        binding?.apply {
            root.setOnClickListener {
                RouteManager.route(it.context, uiModel.appLink)
                analytic.sendClickAnnotationCardEvent(uiModel.name)
            }
            imageBrand.loadImage(uiModel.imageUrl)
        }

        itemView.addOnImpressionListener(uiModel) {
            analytic.sendImpressionAnnotationCardEvent(uiModel.name)
        }
    }
}
