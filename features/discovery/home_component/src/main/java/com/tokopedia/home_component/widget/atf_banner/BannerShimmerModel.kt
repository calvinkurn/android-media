package com.tokopedia.home_component.widget.atf_banner

import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by frenzel
 */
class BannerShimmerModel: ImpressHolder(), BannerVisitable {
    override fun equalsWith(b: BannerVisitable): Boolean {
        return b is BannerShimmerModel
    }

    override fun type(typeFactory: BannerTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}
