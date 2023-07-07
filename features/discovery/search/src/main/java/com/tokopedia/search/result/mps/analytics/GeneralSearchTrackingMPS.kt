package com.tokopedia.search.result.mps.analytics

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.utils.Dimension90Utils
import com.tokopedia.kotlin.extensions.view.ifNullOrBlank
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.result.mps.domain.model.MPSModel

data class GeneralSearchTrackingMPS(
    val eventLabel: String,
    val pageSource: String,
    val isResultFound: String,
    val userId: String,
    val relatedKeyword: String,
    val searchFilter: String,
    val externalReference: String,
    val componentId: String,
) {
    companion object {
        fun create(
            mpsModel: MPSModel,
            parameter: Map<String, String>,
            userId: String,
        ): GeneralSearchTrackingMPS {
            val dimension90 = Dimension90Utils.getDimension90(parameter)
            return GeneralSearchTrackingMPS(
                eventLabel = createEventLabel(mpsModel, parameter),
                pageSource = dimension90,
                isResultFound = mpsModel.searchShopMPS.shopList.isNotEmpty().toString(),
                userId = userId,
                relatedKeyword = "",
                searchFilter = "",
                externalReference = getExternalReference(parameter),
                componentId = "",
            )
        }

        private fun createEventLabel(
            mpsModel: MPSModel,
            parameter: Map<String, String>,
        ): String {
            val queryForTracker = parameter.joinMpsQueryString()
            val responseCode = if (mpsModel.shopList.isNotEmpty()) "0" else "1"
            return String.format(
                SearchEventTracking.Label.GENERAL_SEARCH_EVENT_LABEL,
                queryForTracker,
                mpsModel.searchShopMPS.header.treatmentCode,
                responseCode,
                SearchEventTracking.PHYSICAL_GOODS,
                getNavSourceForGeneralSearchTracking(parameter),
                getPageTitleForGeneralSearchTracking(parameter),
                mpsModel.totalData,
            )
        }

        private fun Map<String, String>.joinMpsQueryString(
            separator: String = "^"
        ): String {
            val keywords = mutableListOf<String>().also {
                it.addIfKeyExist(this, SearchApiConst.Q1)
                it.addIfKeyExist(this, SearchApiConst.Q2)
                it.addIfKeyExist(this, SearchApiConst.Q3)
            }
            return keywords.joinToString(separator)
        }

        private fun MutableList<String>.addIfKeyExist(map: Map<String, String>, key: String) {
            if (map.contains(key) && !map[key].isNullOrBlank()) {
                add(map[key].orEmpty())
            }
        }

        private fun getNavSourceForGeneralSearchTracking(parameter: Map<String, String>): String {
            return parameter[SearchApiConst.NAVSOURCE].orNone()
        }

        private fun getPageTitleForGeneralSearchTracking(parameter: Map<String, String>): String {
            return parameter[SearchApiConst.SRP_PAGE_TITLE].orNone()
        }

        private fun getExternalReference(parameter: Map<String, String>): String {
            return parameter[SearchApiConst.SRP_EXT_REF].orNone()
        }

        private fun String?.orNone(): String {
            return this.ifNullOrBlank { SearchEventTracking.NONE }
        }
    }
}
