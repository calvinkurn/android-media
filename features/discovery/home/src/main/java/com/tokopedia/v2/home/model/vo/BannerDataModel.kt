package com.tokopedia.v2.home.model.vo

import com.tokopedia.home.R
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.v2.home.base.adapterdelegate.ModelViewType
import com.tokopedia.v2.home.model.pojo.Banner

class BannerDataModel (
    val banner: Banner
): ModelViewType, ImpressHolder() {
    override fun isContentsTheSame(other: ModelViewType): Boolean {
        return other is BannerDataModel && other.banner.slides.size == banner.slides.size
    }

    override fun getPrimaryKey(): Int {
        return 1
    }

    override fun getViewType(): Int {
        return R.layout.home_banner
    }
}