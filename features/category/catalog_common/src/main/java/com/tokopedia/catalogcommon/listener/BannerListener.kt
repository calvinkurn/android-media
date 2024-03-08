package com.tokopedia.catalogcommon.listener

import com.tokopedia.catalogcommon.uimodel.AccordionInformationUiModel
import com.tokopedia.catalogcommon.uimodel.BannerCatalogUiModel
import com.tokopedia.catalogcommon.uimodel.TrustMakerUiModel

interface BannerListener {

    fun onBannerThreeByFourImpression(ration: String)

    fun onBannerImpression(element:BannerCatalogUiModel)

}
