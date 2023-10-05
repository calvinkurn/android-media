package com.tokopedia.home_component.widget.atf_banner

import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * Created by frenzel
 */
interface BannerVisitable: Visitable<BannerTypeFactory> {
    fun equalsWith(b: BannerVisitable): Boolean
}
