package com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.viewholder.specs.bullets

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.recommendation_widget_common.databinding.ItemBpcSpecBulletBinding
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.viewholder.specs.BpcSpecsSummaryBullet

/**
 * Created by Frenzel
 */
class SpecBulletItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    private var binding: ItemBpcSpecBulletBinding? by viewBinding()
    fun bind(model: BpcSpecsSummaryBullet) {
        binding?.run {
            tvSummary.text = model.specsSummary
            if(model.icon.isNullOrBlank()) {
                val layoutParams = iconSpecBullet.layoutParams
                iconSpecBullet.layoutParams = layoutParams
                layoutParams.height = view.context.resources.getDimensionPixelSize(com.tokopedia.recommendation_widget_common.R.dimen.comparison_bpc_specs_bullet_size)
                layoutParams.width = view.context.resources.getDimensionPixelSize(com.tokopedia.recommendation_widget_common.R.dimen.comparison_bpc_specs_bullet_size)
                tvSummary.setPadding(view.context.resources.getDimensionPixelSize(com.tokopedia.recommendation_widget_common.R.dimen.comparison_bpc_specs_bullet_margin), Int.ZERO, Int.ZERO, Int.ZERO)
                holderSpecs.setPadding(view.context.resources.getDimensionPixelSize(com.tokopedia.recommendation_widget_common.R.dimen.comparison_bpc_specs_bullet_margin), Int.ZERO, Int.ZERO, Int.ZERO)
                iconSpecBullet.loadImage(com.tokopedia.recommendation_widget_common.R.drawable.spec_bullet)
            } else {
                val layoutParams = iconSpecBullet.layoutParams
                layoutParams.height = view.context.resources.getDimensionPixelSize(com.tokopedia.recommendation_widget_common.R.dimen.comparison_bpc_specs_icon_size)
                layoutParams.width = view.context.resources.getDimensionPixelSize(com.tokopedia.recommendation_widget_common.R.dimen.comparison_bpc_specs_icon_size)
                iconSpecBullet.layoutParams = layoutParams
                tvSummary.setPadding(view.context.resources.getDimensionPixelSize(com.tokopedia.recommendation_widget_common.R.dimen.comparison_bpc_specs_icon_margin), Int.ZERO, Int.ZERO, Int.ZERO)
                holderSpecs.setPadding(Int.ZERO, Int.ZERO, Int.ZERO, Int.ZERO)
                iconSpecBullet.loadImage(model.icon)
            }
        }
    }
}
