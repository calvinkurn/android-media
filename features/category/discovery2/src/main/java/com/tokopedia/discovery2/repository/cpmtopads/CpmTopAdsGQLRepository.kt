package com.tokopedia.discovery2.repository.cpmtopads

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.cpmtopads.CpmTopAdsResponse
import com.tokopedia.discovery2.data.cpmtopads.DataItem
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import javax.inject.Inject


open class CpmTopAdsGQLRepository @Inject constructor(val getGQLString: (Int) -> String) : BaseRepository(), CpmTopAdsRepository {


    override suspend fun getCpmTopAdsData(paramsMobile: String): DiscoveryDataMapper.CpmTopAdsData? {
        val cpmTopAdsResponse = getGQLData(getGQLString(R.raw.query_cpm_topads_gql),
                CpmTopAdsResponse::class.java, mapOf("params" to paramsMobile)) as CpmTopAdsResponse
        val discoveryDataMapper = DiscoveryDataMapper()
        val data = cpmTopAdsResponse.displayAdsV3?.data?.getOrElse(0) { DataItem() }

        val listOfProduct = data?.let { discoveryDataMapper.addShopItemToProductList(it) }
        val listComponentsItem = discoveryDataMapper.mapProductListToComponentsList(listOfProduct)

        return data?.headline?.let { discoveryDataMapper.mapToCpmTopAdsData(it, listComponentsItem) }
    }


}


