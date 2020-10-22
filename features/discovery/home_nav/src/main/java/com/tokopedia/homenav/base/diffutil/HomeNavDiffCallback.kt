package com.tokopedia.homenav.base.diffutil

import androidx.recyclerview.widget.DiffUtil

/**
 * Created by Lukas on 20/10/20.
 */

object HomeNavDiffCallback : DiffUtil.ItemCallback<HomeNavVisitable>() {
    override fun areItemsTheSame(oldItemShopHomeDisplay: HomeNavVisitable, newItemShopHomeDisplay: HomeNavVisitable): Boolean {
        return oldItemShopHomeDisplay.id() == newItemShopHomeDisplay.id()
    }

    override fun areContentsTheSame(oldItemShopHomeDisplay: HomeNavVisitable, newItemShopHomeDisplay: HomeNavVisitable): Boolean {
        return oldItemShopHomeDisplay.isContentTheSame(newItemShopHomeDisplay)
    }
}