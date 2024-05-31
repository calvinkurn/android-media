package com.tokopedia.discovery2.repository.discoveryPage

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.config.GlobalConfig
import com.tokopedia.discovery2.Constant.ChooseAddressQueryParams.USER_ADDRESS_KEY
import com.tokopedia.discovery2.Constant.QueryParamConstants.QUERY_PARAMS_KEY
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.DataResponse
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.productcard.experiments.ProductCardExperiment
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

private const val IDENTIFIER = "identifier"
private const val VERSION = "version"
private const val DEVICE = "device"
private const val DEVICE_VALUE = "Android"
private const val ACCEPT_SECTION = "accept_section"
private const val REF = "ref"

class DiscoveryDataGQLRepository @Inject constructor(
    val getGQLString: (Int) -> String,
) : BaseRepository(), DiscoveryPageRepository {
    lateinit var userSession: UserSession
    override suspend fun getDiscoveryPageData(
        pageIdentifier: String,
        extraParams: Map<String, Any>?,
        additionalQueryParamsString: String,
    ): DiscoveryResponse {
        return (
            getGQLData(
                queryDiscoveryData,
                DataResponse::class.java,
                getQueryMap(pageIdentifier, extraParams, additionalQueryParamsString),
                "discoveryPageInfo"
            ) as DataResponse
            ).data
    }

    private fun getQueryMap(pageIdentifier: String, extraParams: Map<String, Any>?, additionalQueryParamsString: String): Map<String, Any> {
        val queryMap = mutableMapOf(
            IDENTIFIER to pageIdentifier,
            VERSION to GlobalConfig.VERSION_NAME,
            DEVICE to DEVICE_VALUE
        )
        extraParams?.let {
            val localCacheModel = it[USER_ADDRESS_KEY] as? LocalCacheModel
            val addMap: MutableMap<String, Any>? =
                (Utils.addAddressQueryMapWithWareHouse(localCacheModel) as? MutableMap<String, Any>)
            val filterMap = addMap ?: mutableMapOf()
            filterMap[ACCEPT_SECTION] = true
            if (ProductCardExperiment.isReimagine()) {
                filterMap[Utils.SRE_IDENTIFIER] = Utils.SRE_VALUE
            }
            var finalQueryString = Utils.getQueryString(filterMap)
            if (it.containsKey(QUERY_PARAMS_KEY) && !(it[QUERY_PARAMS_KEY] as? String).isNullOrEmpty()) {
                finalQueryString = finalQueryString + "&" + it[QUERY_PARAMS_KEY] as String
            }
            if (additionalQueryParamsString.isNotBlank()) {
                finalQueryString = "$finalQueryString&$additionalQueryParamsString"
            }
            queryMap[Utils.FILTERS] = finalQueryString
        }
        return queryMap
    }

    private val queryDiscoveryData: String = """query DiscoPageLayoutQuery(${'$'}identifier: String!, ${'$'}version: String!, ${'$'}device: String!, ${'$'}filters: String) {
  discoveryPageInfo(identifier: ${'$'}identifier, version: ${'$'}version, device: ${'$'}device,filters: ${'$'}filters) {
    data {
      title
      components
     page_info {
            Id
            Identifier
            Name
            Path
            Type
            share_config
            campaign_code
            search_applink
            search_title
            show_choose_address
            tokonow_has_mini_cart_active
            is_affiliate
            thematic_header {
                  title
       			  subtitle
        	      color
    			  image
    		}
            labels
          }
      additional_info
    }
  }
}
    """.trimIndent()
}
