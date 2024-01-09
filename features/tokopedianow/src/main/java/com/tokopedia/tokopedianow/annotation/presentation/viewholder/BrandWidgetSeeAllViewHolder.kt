package com.tokopedia.tokopedianow.annotation.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.annotation.analytic.AnnotationWidgetAnalytic
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.BrandWidgetSeeAllUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowBrandWidgetSeeAllItemBinding
import com.tokopedia.utils.view.binding.viewBinding

class BrandWidgetSeeAllViewHolder(
    itemView: View,
    private val analytic: AnnotationWidgetAnalytic
) : AbstractViewHolder<BrandWidgetSeeAllUiModel>(itemView) {

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_brand_widget_see_all_item
    }

    private val binding: ItemTokopedianowBrandWidgetSeeAllItemBinding? by viewBinding()

    override fun bind(uiModel: BrandWidgetSeeAllUiModel) {
        binding?.apply {
            root.setOnClickListener {
                RouteManager.route(it.context, uiModel.appLink)
                analytic.sendClickViewAllEvent()
            }
            imageSupergraphic.loadImage(TokopediaImageUrl.TOKOPEDIANOW_SEE_ALL_BRAND_SUPERGRAPHIC)
        }
    }
}
