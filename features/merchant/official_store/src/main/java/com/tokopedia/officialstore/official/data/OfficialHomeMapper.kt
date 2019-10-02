@file:JvmName("OfficialHomeMapper")
package com.tokopedia.officialstore.official.data

import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapter
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.OfficialBannerViewModel

class OfficialHomeMapper {

    companion object {

        fun mappingBanners(banner: OfficialStoreBanners, adapter: OfficialHomeAdapter?) {
            adapter?.addElement(0, OfficialBannerViewModel(banner.banners))
            adapter?.notifyItemInserted(0)
        }
    }
}