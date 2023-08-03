package com.tokopedia.home_component.widget.atf_banner

import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by frenzel
 */
data class BannerRevampItemModel(
    val id: Int,
    val url: String,
    val position: Int = 0
) : ImpressHolder(), BannerVisitable {
    override fun equalsWith(b: BannerVisitable): Boolean {
        return b is BannerRevampItemModel && b == this
    }

    override fun type(typeFactory: BannerTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}

