package com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.viewholder.specs.bullets

import android.view.View
import androidx.recyclerview.widget.RecyclerView
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
                iconSpecBullet.loadImage(com.tokopedia.recommendation_widget_common.R.drawable.spec_bullet)
            } else {
                iconSpecBullet.loadImage(model.icon)
            }
        }
    }
}
