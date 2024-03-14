package com.tokopedia.autocompletecomponent.unify

import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.CANCEL
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.ENTER
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
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.ENTER_METHOD
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.discovery.common.utils.Dimension90Utils

data class AutoCompleteState(
    val parameter: Map<String, String> = emptyMap(),
    val resultList: List<AutoCompleteUnifyDataView> = listOf(),
    val navigate: AutoCompleteNavigate? = null,
    val appLogData: AutoCompleteAppLogData = AutoCompleteAppLogData(),
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
        actionReplaceKeyword = actionReplaceKeyword
    )

    val isInitialState: Boolean
        get() = parameter[SearchApiConst.Q].isNullOrBlank()

    val isSuggestion: Boolean
        get() = !isInitialState

    val enterMethod: String
        get() = parameter[ENTER_METHOD] ?: ENTER

    val query: String
        get() = parameter[SearchApiConst.Q] ?: ""

    fun getParameterMap(): Map<String, String> = parameter

    fun updateParameter(parameter: Map<String, String>): AutoCompleteState {
        val previousState = this
        val updatedParameterState = copy(parameter = parameter)
        val enterMethod = updatedEnterMethod(previousState, updatedParameterState)

        return updatedParameterState.copy(
            parameter = updatedParameterState.parameter +
                mapOf(ENTER_METHOD to enterMethod)
        )
    }

    private fun updatedEnterMethod(previousState: AutoCompleteState, currentState: AutoCompleteState) =
        if (previousState.isInitialState && currentState.isSuggestion) ENTER
        else if (previousState.isSuggestion && currentState.isInitialState) CANCEL
        else previousState.enterMethod

    fun updateResultList(
        resultData: UniverseSuggestionUnifyModel,
        newSugSessionId: String,
    ): AutoCompleteState {
        val resultList = autoCompleteResultList(resultData)
        return copy(
            resultList = resultList,
            appLogData = autoCompleteAppLogData(resultData, newSugSessionId)
        )
    }

    fun autoCompleteResultList(resultData: UniverseSuggestionUnifyModel): List<AutoCompleteUnifyDataView> {
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

        val suggestionUnifyList =
            if (adsModel == null) resultData.data.filterNot(SuggestionUnify::isAds)
            else resultData.data

        val mappedResultList = suggestionUnifyList.mapIndexed { index, it ->
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
                } else {
                    it
                }

            AutoCompleteUnifyDataView(
                domainModel = domainModel,
                searchTerm = searchTerm,
                dimension90 = dimension90,
                shopAdsDataView = shopAdsDataView,
                appLogIndex = index,
            )
        }
        return mappedResultList
    }

    private fun autoCompleteAppLogData(
        resultData: UniverseSuggestionUnifyModel,
        newSugSessionId: String,
    ) = AutoCompleteAppLogData(
            newSugSessionId = sugSessionId(newSugSessionId),
            imprId = resultData.requestId(),
            wordsSource = resultData.wordsSource(),
        )

    private fun sugSessionId(newSugSessionId: String) =
        if (isInitialState) ""
        else if (appLogData.newSugSessionId.isNotBlank()) appLogData.newSugSessionId
        else newSugSessionId

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
