package com.tokopedia.hotlist.domain.mapper

import com.tokopedia.hotlist.data.cpmAds.CpmItem
import com.tokopedia.hotlist.data.cpmAds.CpmTopAdsResponse

class CpmItemMapper {

    fun transform(cpmTopAdsResponse: CpmTopAdsResponse): List<CpmItem> {

        val list = ArrayList<CpmItem>()

        if (cpmTopAdsResponse.displayAdsV3?.data?.size ?: 0 > 0) {
            val applink = cpmTopAdsResponse.displayAdsV3?.data?.get(0)?.applinks
            val adClickUrl = cpmTopAdsResponse.displayAdsV3?.data?.get(0)?.adClickUrl
            var badge: String? = ""
            if (cpmTopAdsResponse.displayAdsV3?.data?.get(0)?.headline?.badges?.size ?: 0 > 0) {
                badge = cpmTopAdsResponse.displayAdsV3?.data?.get(0)?.headline?.badges?.get(0)?.imageUrl
                        ?: ""
            }
            cpmTopAdsResponse.displayAdsV3?.data?.get(0)?.headline?.let { headline ->
                list.add(CpmItem(headline.name,
                        headline.description,
                        headline.shop?.imageShop?.xsUrl,
                        "",
                        false,
                        applink,
                        adClickUrl,
                        headline.image?.fullUrl,
                        badge,
                        id = headline.shop?.id))

                headline.shop?.product?.let {

                    for (productItem in it) {
                        productItem?.let { element ->
                            list.add(CpmItem(element.name,
                                    "",
                                    element.imageProduct?.imageUrl,
                                    element.priceFormat,
                                    true,
                                    element.applinks,
                                    element.imageProduct?.imageClickUrl,
                                    "",
                                    "",
                                    id = element.id))
                        }
                    }
                }
            }
        }
        return list
    }
}