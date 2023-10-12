package com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.viewholder.specs.bullets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recommendation_widget_common.databinding.ItemBpcSpecBulletBinding
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.viewholder.specs.BpcSpecsSummaryBullet

/**
 * Created by Frenzel
 */
class SpecBulletAdapter(var bullets: List<BpcSpecsSummaryBullet>): RecyclerView.Adapter<SpecBulletItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecBulletItemViewHolder {
        val view = ItemBpcSpecBulletBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SpecBulletItemViewHolder(view.root)
    }

    override fun getItemCount(): Int {
        return bullets.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: SpecBulletItemViewHolder, position: Int) {
        if (position < bullets.size) {
            holder.bind(bullets[position])
        }
    }
}
