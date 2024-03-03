package com.tokopedia.autocompletecomponent.unify

import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.CANCEL
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.ENTER
import com.tokopedia.autocompletecomponent.unify.domain.model.SuggestionUnify
import com.tokopedia.autocompletecomponent.unify.domain.model.UniverseSuggestionUnifyModel
import com.tokopedia.autocompletecomponent.util.AutoCompleteNavigate
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.ENTER_METHOD
import com.tokopedia.discovery.common.utils.Dimension90Utils

data class AutoCompleteState(
    val parameter: Map<String, String> = emptyMap(),
    val resultList: List<AutoCompleteUnifyDataView> = listOf(),
    val navigate: AutoCompleteNavigate? = null,
    val appLogData: AutoCompleteAppLogData = AutoCompleteAppLogData(),
    val actionReplaceKeyword: String? = null,
) {

    val isInitialState: Boolean
        get() = parameter[SearchApiConst.Q].isNullOrBlank()

    val isSuggestion: Boolean
        get() = !isInitialState

    val enterMethod: String
        get() = parameter[ENTER_METHOD] ?: ""

    val query: String
        get() = parameter[SearchApiConst.Q] ?: ""

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

    private fun autoCompleteResultList(resultData: UniverseSuggestionUnifyModel): List<AutoCompleteUnifyDataView> {
        val dimension90 = Dimension90Utils.getDimension90(parameter)
        val searchTerm = parameter.getOrElse(SearchApiConst.Q) { "" }

        // AppLog Logic
        var masterIndex = -1

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
            val domainModel =
                if (it.isAds && adsModel != null) {
                    it.copy(
                        applink = adsModel.applinks,
                        image = it.image.copy(
                            iconImageUrl = adsModel.cpm.badges.getOrNull(0)?.imageUrl ?: "",
                            imageUrl = adsModel.cpm.cpmImage.fullUrl
                        ),
                        template = "master",
                        suggestionId = adsModel.cpm.cpmShop.id,
                        isAds = true,
                        title = it.title.copy(
                            text = adsModel.cpm.name
                        ),
                        subtitle = it.subtitle.copy(
                            text = adsModel.cpm.cpmShop.location
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
            if (domainModel.isTrendingWord()) {
                masterIndex += 1
            }
            AutoCompleteUnifyDataView(
                domainModel,
                searchTerm = searchTerm,
                dimension90 = dimension90,
                appLogIndex = masterIndex
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

    fun updateResultList(resultList: List<AutoCompleteUnifyDataView>) = copy(resultList = resultList)

    fun updateNavigate(navigate: AutoCompleteNavigate?) = copy(navigate = navigate)

    fun updateReplaceKeyword(keyword: String?) = copy(actionReplaceKeyword = keyword)
}
