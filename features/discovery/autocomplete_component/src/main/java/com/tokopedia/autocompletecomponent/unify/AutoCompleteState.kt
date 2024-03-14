package com.tokopedia.autocompletecomponent.unify

import com.tokopedia.autocompletecomponent.unify.domain.model.SuggestionUnify
import com.tokopedia.autocompletecomponent.unify.domain.model.SuggestionUnifyFlag
import com.tokopedia.autocompletecomponent.unify.domain.model.UniverseSuggestionUnifyModel
import com.tokopedia.autocompletecomponent.util.AUTOCOMPLETE_UNIFY_BADGE_OFFICIAL_STORE
import com.tokopedia.autocompletecomponent.util.AUTOCOMPLETE_UNIFY_BADGE_OFFICIAL_STORE_FLAG
import com.tokopedia.autocompletecomponent.util.AUTOCOMPLETE_UNIFY_BADGE_POWER_MERCHANT
import com.tokopedia.autocompletecomponent.util.AUTOCOMPLETE_UNIFY_BADGE_POWER_MERCHANT_FLAG
import com.tokopedia.autocompletecomponent.util.AUTOCOMPLETE_UNIFY_BADGE_POWER_MERCHANT_PRO
import com.tokopedia.autocompletecomponent.util.AUTOCOMPLETE_UNIFY_BADGE_POWER_MERCHANT_PRO_FLAG
import com.tokopedia.autocompletecomponent.util.AUTOCOMPLETE_UNIFY_SHOP_ADS_SUBTITLE
import com.tokopedia.autocompletecomponent.util.AutoCompleteNavigate
import com.tokopedia.autocompletecomponent.util.AutoCompleteTemplateEnum
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.discovery.common.utils.Dimension90Utils

data class AutoCompleteState(
    val parameter: Map<String, String> = mapOf(),
    val resultList: List<AutoCompleteUnifyDataView> = listOf(),
    val navigate: AutoCompleteNavigate? = null,
    val actionReplaceKeyword: String? = null,
    val isTyping: Boolean = false,
) {
    constructor(
        searchParameter: SearchParameter,
        resultList: List<AutoCompleteUnifyDataView> = listOf(),
        navigate: AutoCompleteNavigate? = null,
        actionReplaceKeyword: String? = null
    ) : this(
        searchParameter.getSearchParameterMap().mapValues { it.value.toString() },
        resultList,
        navigate,
        actionReplaceKeyword
    )

    fun updateParameter(parameterMap: Map<String, String>) =
        copy(
            parameter = parameterMap
        )

    fun getParameterMap(): Map<String, String> = parameter

    fun updateResultList(resultData: UniverseSuggestionUnifyModel): AutoCompleteState {
        val parameterMap = getParameterMap()
        val dimension90 = Dimension90Utils.getDimension90(parameterMap)
        val searchTerm = parameterMap.getOrElse(SearchApiConst.Q) { "" }.toString()

        // Ads Logic
        val firstAdsId = resultData.cpmModel.data.getOrNull(0)?.cpm?.cpmShop?.id
        val secondAdsId = resultData.cpmModel.data.getOrNull(1)?.cpm?.cpmShop?.id
        val adsIndex =
            if (firstAdsId != null && resultData.data.find { it.suggestionId == firstAdsId } == null) {
                // Then use first ads model
                0
            } else if (secondAdsId != null && resultData.data.find { it.suggestionId == secondAdsId } == null) {
                // Then use second ads model
                1
            } else {
                null
            }

        val adsModel = adsIndex?.let { resultData.cpmModel.data[it] }
        val mappedResultList = resultData.data.map {
            var shopAdsDataView: AutoCompleteUnifyDataView.ShopAdsDataView? = null
            val domainModel =
                if (it.isAds && adsModel != null) {
                    val cpmBadgeUrl = getAdsBadgeUrlForHeadline(
                        adsModel.cpm.badges.getOrNull(0)?.title ?: "",
                        it.flags,
                    )
                    shopAdsDataView = AutoCompleteUnifyDataView.ShopAdsDataView(
                        clickUrl = adsModel.adClickUrl,
                        impressionUrl = adsModel.cpm.cpmImage.fullUrl,
                        imageUrl = adsModel.cpm.cpmImage.fullEcs
                    )
                    it.copy(
                        applink = adsModel.applinks,
                        image = it.image.copy(
                            iconImageUrl = cpmBadgeUrl,
                            imageUrl = adsModel.cpm.cpmImage.fullUrl
                        ),
                        template = AutoCompleteTemplateEnum.Master.dataName,
                        suggestionId = adsModel.cpm.cpmShop.id,
                        isAds = true,
                        title = it.title.copy(
                            text = adsModel.cpm.name
                        ),
                        subtitle = it.subtitle.copy(
                            text = AUTOCOMPLETE_UNIFY_SHOP_ADS_SUBTITLE,
                            iconImageUrl = ""
                        ),
                        tracking = it.tracking.copy(
                            trackerUrl = adsModel.adClickUrl
                        )
                    )
                } else if (it.isAds) {
                    SuggestionUnify()
                } else {
                    it
                }
            AutoCompleteUnifyDataView(
                domainModel,
                searchTerm = searchTerm,
                dimension90 = dimension90,
                shopAdsDataView = shopAdsDataView
            )
        }
        return copy(resultList = mappedResultList)
    }

    private fun getAdsBadgeUrlForHeadline(
        headlineAdsBadgeTitle: String,
        autoCompleteFlag:  List<SuggestionUnifyFlag>
    ): String {
        return when (headlineAdsBadgeTitle) {
            AUTOCOMPLETE_UNIFY_BADGE_POWER_MERCHANT ->
                (autoCompleteFlag.find {
                    it.name == AUTOCOMPLETE_UNIFY_BADGE_POWER_MERCHANT_FLAG
                }?.value) ?: ""
            AUTOCOMPLETE_UNIFY_BADGE_OFFICIAL_STORE ->
                (autoCompleteFlag.find {
                    it.name == AUTOCOMPLETE_UNIFY_BADGE_OFFICIAL_STORE_FLAG
                }?.value) ?: ""
            AUTOCOMPLETE_UNIFY_BADGE_POWER_MERCHANT_PRO ->
                (autoCompleteFlag.find {
                    it.name == AUTOCOMPLETE_UNIFY_BADGE_POWER_MERCHANT_PRO_FLAG
                }?.value) ?: ""
            else -> ""
        }
    }

    fun updateResultList(resultList: List<AutoCompleteUnifyDataView>) =
        copy(resultList = resultList)

    fun updateNavigate(navigate: AutoCompleteNavigate?) = copy(navigate = navigate)

    fun updateReplaceKeyword(keyword: String?) = copy(actionReplaceKeyword = keyword)
    fun updateIsTyping(isTyping: Boolean) = copy(isTyping = isTyping)
    fun parameterIsMps() = parameter[SearchApiConst.ACTIVE_TAB] == SearchApiConst.ACTIVE_TAB_MPS
    fun parameterStateIsSuggestions(): Boolean =
        parameter[SearchApiConst.Q].isNullOrBlank().not()
}
