@file:JvmName("OfficialHomeMapper")
package com.tokopedia.officialstore.official.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapterTypeFactory
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.OfficialBannerViewModel

class OfficialHomeMapper {

    companion object {

        fun mappingBanners(banner: OfficialStoreBanners,
                           listOfOfficialHome: ArrayList<Visitable<OfficialHomeAdapterTypeFactory>>) {
            listOfOfficialHome.add(0, OfficialBannerViewModel(banner.banners))
        }
    }
}