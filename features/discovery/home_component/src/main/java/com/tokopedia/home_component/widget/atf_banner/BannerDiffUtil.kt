package com.tokopedia.home_component.widget.atf_banner

import androidx.recyclerview.widget.DiffUtil

/**
 * Created by frenzel
 */
internal class BannerDiffUtil: DiffUtil.ItemCallback<BannerVisitable>() {
    override fun areItemsTheSame(
        oldItem: BannerVisitable,
        newItem: BannerVisitable
    ): Boolean = oldItem.equalsWith(newItem)

    override fun areContentsTheSame(
        oldItem: BannerVisitable,
        newItem: BannerVisitable
    ): Boolean = oldItem.equalsWith(newItem)
}
