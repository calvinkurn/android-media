package com.tokopedia.travelhomepage.homepage.presentation.listener

import com.tokopedia.travelhomepage.homepage.data.TravelLayoutSubhomepage

/**
 * @author by jessica on 2019-08-12
 */

interface OnItemBindListener {
    fun onBannerItemBind(travelLayoutSubhomepage: TravelLayoutSubhomepage.Data, isFromCloud: Boolean)
    fun onCategoryItemBind(travelLayoutSubhomepage: TravelLayoutSubhomepage.Data, isFromCloud: Boolean)
    fun onDestinationItemBind(travelLayoutSubhomepage: TravelLayoutSubhomepage.Data, isFromCloud: Boolean)
    fun onLegoBannerItemBind(travelLayoutSubhomepage: TravelLayoutSubhomepage.Data, isFromCloud: Boolean)
    fun onProductCardItemBind(travelLayoutSubhomepage: TravelLayoutSubhomepage.Data, isFromCloud: Boolean)
    fun onHomepageSectionItemBind(travelLayoutSubhomepage: TravelLayoutSubhomepage.Data, isFromCloud: Boolean)
}