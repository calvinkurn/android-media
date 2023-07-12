package com.tokopedia.promousage.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.promousage.view.model.PromoSection

internal class PromoUsageDiffUtilCallback : DiffUtil.ItemCallback<PromoSection>() {

    override fun areItemsTheSame(oldItem: PromoSection, newItem: PromoSection): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PromoSection, newItem: PromoSection): Boolean {
        return oldItem == newItem
    }
}
