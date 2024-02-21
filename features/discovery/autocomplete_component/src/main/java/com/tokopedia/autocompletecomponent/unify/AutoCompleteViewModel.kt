package com.tokopedia.autocompletecomponent.unify

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.tokopedia.analytics.byteio.search.AppLogSearch
import com.tokopedia.autocompletecomponent.initialstate.DELETE_RECENT_SEARCH_USE_CASE
import com.tokopedia.autocompletecomponent.initialstate.domain.deleterecentsearch.DeleteRecentSearchUseCase
import com.tokopedia.autocompletecomponent.unify.domain.AutoCompleteUnifyRequestUtil
import com.tokopedia.autocompletecomponent.unify.domain.model.SuggestionUnify
import com.tokopedia.autocompletecomponent.unify.domain.model.UniverseSuggestionUnifyModel
import com.tokopedia.autocompletecomponent.util.ACTION_DELETE
import com.tokopedia.autocompletecomponent.util.ACTION_REPLACE
import com.tokopedia.autocompletecomponent.util.AutoCompleteNavigate
import com.tokopedia.autocompletecomponent.util.ChooseAddressUtilsWrapper
import com.tokopedia.autocompletecomponent.util.DEFAULT_COUNT
import com.tokopedia.autocompletecomponent.util.DEVICE_ID
import com.tokopedia.autocompletecomponent.util.FEATURE_ID_RECENT_SEARCH
import com.tokopedia.autocompletecomponent.util.KEY_COUNT
import com.tokopedia.autocompletecomponent.util.SEARCHBAR
import com.tokopedia.autocompletecomponent.util.putChooseAddressParams
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named

internal class AutoCompleteViewModel @Inject constructor(
    autoCompleteState: AutoCompleteState,
    @Named(AutoCompleteUnifyRequestUtil.INITIAL_STATE_USE_CASE)
    private val initialStateUseCase: UseCase<UniverseSuggestionUnifyModel>,
    @Named(AutoCompleteUnifyRequestUtil.SUGGESTION_STATE_USE_CASE)
    private val suggestionStateUseCase: UseCase<UniverseSuggestionUnifyModel>,
    @Named(DELETE_RECENT_SEARCH_USE_CASE)
    private val deleteRecentSearchUseCase: com.tokopedia.usecase.UseCase<Boolean>,
    private val userSession: UserSessionInterface,
    private val chooseAddressUtilsWrapper: ChooseAddressUtilsWrapper
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(autoCompleteState)
    val stateFlow = _stateFlow.asStateFlow()

    val stateValue
        get() = stateFlow.value

    val isInitialState
        get() = stateValue.isInitialState

    val isSuggestion
        get() = stateValue.isSuggestion

    val currentQuery
        get() = stateValue.parameter[SearchApiConst.Q] ?: ""

    fun onScreenInitialized() {
        actOnParameter()
    }

    fun onScreenUpdateParameter(updatedParameter: Map<String, String>) {
        _stateFlow.value = stateValue.updateParameter(updatedParameter)
        actOnParameter()
    }

    fun initAppLogData(enterFrom: String, searchEntrance: String) {
        val currentAppLogData = stateValue.appLogData
        _stateFlow.value = stateValue.copy(
            appLogData = currentAppLogData.copy(
                enterFrom = enterFrom,
                searchEntrance = searchEntrance
            )
        )
    }

    private fun actOnParameter() {
        val parameter = stateValue.parameter
        if (parameterIsMps() && !parameterStateIsSuggestions()) {
            return
        }
        if (parameterStateIsSuggestions(parameter)) {
            getSuggestionStateData(parameter)
            return
        }
        updateAppLogData(needNewSessionId = true)
        getInitialStateData(parameter)
    }

    private fun updateAppLogData(imprId: String? = null, needNewSessionId: Boolean = false) {
        val currentAppLogData = stateValue.appLogData
        val newImprId = imprId ?: currentAppLogData.imprId
        val newSessionId = if (needNewSessionId) {
            System.currentTimeMillis()
        } else {
            currentAppLogData.newSugSessionId
        }
        _stateFlow.value = stateValue.copy(
            appLogData = AutoCompleteAppLogData(imprId = newImprId, newSugSessionId = newSessionId)
        )
    }

    fun parameterIsMps(parameter: Map<String, String> = stateValue.parameter) =
        parameter.containsKey(SearchApiConst.Q1) || parameter.containsKey(SearchApiConst.Q2) || parameter.containsKey(
            SearchApiConst.Q3
        )

    private fun parameterStateIsSuggestions(parameter: Map<String, String> = stateValue.parameter) =
        parameter[SearchApiConst.Q].isNullOrBlank().not()

    private fun getSuggestionStateData(parameter: Map<String, String>) {
        val requestParams = getParamsMainQuery(parameter, userSession)
        suggestionStateUseCase.execute(
            ::onGetStateDataSuccessful,
            ::onGetDataError,
            useCaseRequestParams = requestParams
        )
    }

    private fun getInitialStateData(parameter: Map<String, String>) {
        val requestParams = getParamsMainQuery(parameter, userSession)
        initialStateUseCase.execute(
            ::onGetStateDataSuccessful,
            ::onGetDataError,
            useCaseRequestParams = requestParams
        )
    }

    private fun onGetStateDataSuccessful(model: UniverseSuggestionUnifyModel) {
        _stateFlow.value = stateValue.updateResultList(model)
        trackTrendingShow()
    }

    fun trackTrendingShow() {
        val appLogData = stateValue.appLogData
        AppLogSearch.eventTrendingShow(
            AppLogSearch.TrendingShow(
                searchPosition = appLogData.enterFrom,
                searchEntrance = appLogData.searchEntrance,
                imprId = "", // TODO milhamj: wait from BE
                newSugSessionId = appLogData.newSugSessionId,
                rawQuery = currentQuery,
                enterMethod = stateValue.enterMethod,
                wordsNum = stateValue.resultList.filter {
                    it.domainModel.isMasterTemplate()
                }.size
            )
        )
    }

    fun trackTrendingWordsShow(item: AutoCompleteUnifyDataView) {
        val appLogData = stateValue.appLogData
        AppLogSearch.eventTrendingWordsShowSuggestion(
            AppLogSearch.TrendingWordsShow(
                searchPosition = appLogData.enterFrom,
                searchEntrance = appLogData.searchEntrance,
                groupId = "", // TODO milhamj: wait from BE
                imprId = "", // TODO milhamj: wait from BE
                newSugSessionId = appLogData.newSugSessionId,
                rawQuery = currentQuery,
                enterMethod = stateValue.enterMethod,
                sugType = "", // TODO wait from BE
                wordsContent = item.domainModel.title.text,
                wordsPosition = item.appLogIndex
            )
        )
    }

    fun trackSearchEnterBlankPage() {
        val appLogData = stateValue.appLogData
        AppLogSearch.eventEnterSearchBlankPage(
            enterFrom = appLogData.enterFrom,
            searchEntrance = appLogData.searchEntrance
        )
    }

    private fun onGetDataError(throwable: Throwable) {
    }

    fun onAutoCompleteItemClick(item: AutoCompleteUnifyDataView) {
        _stateFlow.value = stateValue.updateNavigate(AutoCompleteNavigate(item.domainModel.applink))
    }

    fun onNavigated() {
        _stateFlow.value = stateValue.updateNavigate(null)
    }

    fun onActionReplaceAcknowledged() {
        _stateFlow.value = stateValue.updateReplaceKeyword(null)
    }

    fun onAutocompleteItemAction(item: SuggestionUnify) {
        if (item.label.action == ACTION_DELETE) {
            deleteAllAutocomplete(item)
            return
        }

        if (item.cta.action == ACTION_DELETE) {
            deleteAutocompleteItem(item)
            return
        }

        if (item.cta.action == ACTION_REPLACE) {
            replaceSearchbarQuery(item)
            return
        }
    }

    private fun replaceSearchbarQuery(item: SuggestionUnify) {
        _stateFlow.value = stateValue.updateReplaceKeyword(item.title.text)
    }

    private fun deleteAllAutocomplete(item: SuggestionUnify) {
        val params = DeleteRecentSearchUseCase.getParams(
            registrationId = userSession.deviceId,
            userId = userSession.userId,
            navSource = getNavSource()
        )
        deleteRecentSearchUseCase.execute(
            params,
            getDeleteAllRecentSearchSubscriber(item)
        )
    }

    private fun getDeleteAllRecentSearchSubscriber(item: SuggestionUnify): Subscriber<Boolean> =
        object : Subscriber<Boolean>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(isSuccess: Boolean) {
                if (isSuccess) {
                    deleteAllRecentSearchItemFromStateResult(item)
                }
            }
        }

    private fun deleteAllRecentSearchItemFromStateResult(item: SuggestionUnify) {
        val resultList = stateValue.resultList.toMutableList()
        resultList.removeAll {
            it.domainModel.featureId == FEATURE_ID_RECENT_SEARCH
        }
        _stateFlow.value = stateValue.updateResultList(resultList)
    }

    private fun deleteAutocompleteItem(item: SuggestionUnify) {
        val params = DeleteRecentSearchUseCase.getParams(
            registrationId = userSession.deviceId,
            userId = userSession.userId,
            item = item,
            navSource = getNavSource()
        )
        deleteRecentSearchUseCase.execute(
            params,
            getDeleteRecentSearchSubscriber(item.suggestionId)
        )
    }

    private fun getDeleteRecentSearchSubscriber(suggestionId: String): Subscriber<Boolean> =
        object : Subscriber<Boolean>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(isSuccess: Boolean) {
                if (isSuccess) {
                    deleteSearchItemFromStateResult(suggestionId)
                }
            }
        }

    private fun deleteSearchItemFromStateResult(suggestionId: String) {
        val resultList = stateValue.resultList.toMutableList()
        resultList.removeFirst { it.domainModel.suggestionId == suggestionId }
        _stateFlow.value = stateValue.updateResultList(resultList)
    }

    private fun getNavSource() = stateValue.parameter[SearchApiConst.NAVSOURCE] ?: ""

    @SuppressLint("PII Data Exposure")
    private fun getParamsMainQuery(
        searchParameter: Map<String, String>,
        userSession: UserSessionInterface
    ): RequestParams {
        val registrationId = userSession.deviceId
        val userId = userSession.userId
        val uniqueId = getUniqueId(userId, registrationId)

        return RequestParams.create().apply {
            putAll(searchParameter)
            putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE)
            putString(SearchApiConst.SOURCE, SEARCHBAR)
            putString(KEY_COUNT, DEFAULT_COUNT)
            putString(SearchApiConst.USER_ID, userId)
            putString(SearchApiConst.UNIQUE_ID, uniqueId)
            putString(DEVICE_ID, registrationId)
            putChooseAddressParams(chooseAddressUtilsWrapper.getLocalizingAddressData())
        }
    }

    private fun getUniqueId(userId: String, registrationId: String) = if (userId.isNotEmpty()) {
        AuthHelper.getMD5Hash(userId)
    } else {
        AuthHelper.getMD5Hash(registrationId)
    }
}
