package com.tokopedia.salam.umrah.homepage.data

import com.tokopedia.salam.umrah.common.data.UmrahSearchParameterEntity

object UmrahHomepageBottomSheetMapper {
    val DEPATURE_CITIES =  "departureCities"
    val DEPATURE_PERIODS =   "departurePeriods"
    val PRICE_RANGE =  "priceRangeOptions"

    fun citiesMapper(searchParameter: UmrahSearchParameterEntity):UmrahHomepageBottomSheetData{
        val searchParameterMap = searchParameter.umrahSearchParameter.depatureCities.options
        val umrahHomepageBottomSheetwithType: List<UmrohHomepageBottomSheetwithType>
                = searchParameterMap.mapIndexed { index, options ->
            with(options){
                UmrohHomepageBottomSheetwithType( displayText,query,minimum, maximum, DEPATURE_CITIES, index)
            }
        }

        return UmrahHomepageBottomSheetData(umrahHomepageBottomSheetwithType)
    }

    fun periodsMapper(searchParameter: UmrahSearchParameterEntity):UmrahHomepageBottomSheetData{
        val searchParameterMap = searchParameter.umrahSearchParameter.departurePeriods.options
        val umrahHomepageBottomSheetwithType: List<UmrohHomepageBottomSheetwithType>
                = searchParameterMap.mapIndexed { index, options ->
            with(options){
                UmrohHomepageBottomSheetwithType(  displayText,query,minimum, maximum, DEPATURE_PERIODS, index)
            }
        }

        return UmrahHomepageBottomSheetData(umrahHomepageBottomSheetwithType)
    }

    fun priceRangerMapper(searchParameter: UmrahSearchParameterEntity):UmrahHomepageBottomSheetData{
        val searchParameterMap = searchParameter.umrahSearchParameter.priceRangeOptions.options
        val umrahHomepageBottomSheetwithType: List<UmrohHomepageBottomSheetwithType>
                = searchParameterMap.mapIndexed { index, options ->
            with(options) {
                UmrohHomepageBottomSheetwithType(rangeDisplayText, query, minimum, maximum, PRICE_RANGE, index)
            }
        }

        return UmrahHomepageBottomSheetData(umrahHomepageBottomSheetwithType)
    }
}