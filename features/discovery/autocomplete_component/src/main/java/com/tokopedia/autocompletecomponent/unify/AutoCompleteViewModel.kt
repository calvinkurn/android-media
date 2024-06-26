package com.tokopedia.autocompletecomponent.unify

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.tokopedia.autocompletecomponent.initialstate.DELETE_RECENT_SEARCH_USE_CASE
import com.tokopedia.autocompletecomponent.initialstate.domain.deleterecentsearch.DeleteRecentSearchUseCase
import com.tokopedia.autocompletecomponent.suggestion.domain.suggestiontracker.SuggestionTrackerUseCase
import com.tokopedia.autocompletecomponent.unify.byteio.SugSessionId
import com.tokopedia.autocompletecomponent.unify.domain.AutoCompleteUnifyRequestUtil
import com.tokopedia.autocompletecomponent.unify.domain.AutoCompleteUnifyRequestUtil.URL_TRACKER_USE_CASE
import com.tokopedia.autocompletecomponent.unify.domain.model.SuggestionUnify
import com.tokopedia.autocompletecomponent.unify.domain.model.UniverseSuggestionUnifyModel
import com.tokopedia.autocompletecomponent.util.ACTION_DELETE
import com.tokopedia.autocompletecomponent.util.ACTION_REPLACE
import com.tokopedia.autocompletecomponent.util.AutoCompleteNavigate
import com.tokopedia.autocompletecomponent.util.ChooseAddressUtilsWrapper
import com.tokopedia.autocompletecomponent.util.DEFAULT_COUNT
import com.tokopedia.autocompletecomponent.util.DEVICE_ID
import com.tokopedia.autocompletecomponent.util.FEATURE_ID_RECENT_SEARCH
import com.tokopedia.autocompletecomponent.util.IS_TYPING
import com.tokopedia.autocompletecomponent.util.KEY_COUNT
import com.tokopedia.autocompletecomponent.util.SEARCHBAR
import com.tokopedia.autocompletecomponent.util.putChooseAddressParams
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named
import com.tokopedia.usecase.UseCase as RxUseCase

internal class AutoCompleteViewModel @Inject constructor(
    autoCompleteState: AutoCompleteState,
    @Named(AutoCompleteUnifyRequestUtil.INITIAL_STATE_USE_CASE)
    private val initialStateUseCase: UseCase<UniverseSuggestionUnifyModel>,
    @Named(AutoCompleteUnifyRequestUtil.SUGGESTION_STATE_USE_CASE)
    private val suggestionStateUseCase: UseCase<UniverseSuggestionUnifyModel>,
    @Named(DELETE_RECENT_SEARCH_USE_CASE)
    private val deleteRecentSearchUseCase: RxUseCase<Boolean>,
    @Named(URL_TRACKER_USE_CASE)
    private val suggestionTrackerUseCase: RxUseCase<Void?>,
    private val userSession: UserSessionInterface,
    private val chooseAddressUtilsWrapper: ChooseAddressUtilsWrapper,
    private val topAdsUrlHitter: TopAdsUrlHitter,
    private val sugSessionId: SugSessionId,
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(autoCompleteState)
    val stateFlow = _stateFlow.asStateFlow()

    val stateValue
        get() = stateFlow.value

    val isInitialState
        get() = stateValue.isInitialState

    val isSuggestion
        get() = stateValue.isSuggestion

    val query
        get() = stateValue.query

    fun onScreenInitialized() {
        actOnParameter()
    }

    fun onScreenUpdateParameter(updatedParameter: Map<String, String>) {
        _stateFlow.value = stateValue.updateParameter(updatedParameter)
        actOnParameter()
    }

    fun setIsTyping(isTyping: Boolean) {
        _stateFlow.value = stateValue.updateIsTyping(isTyping)
    }

    private fun actOnParameter() {
        val parameter = stateValue.parameter
        if (parameterIsMps() && !stateValue.isSuggestion) {
            return
        }

        if (stateValue.isSuggestion) getSuggestionStateData(parameter)
        else getInitialStateData(parameter)
    }

    fun parameterIsMps(parameter: Map<String, String> = stateValue.parameter) =
        parameter.containsKey(SearchApiConst.Q1) || parameter.containsKey(SearchApiConst.Q2) || parameter.containsKey(
            SearchApiConst.Q3
        )

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
        _stateFlow.value = stateValue.updateResultList(model, sugSessionId.generate())
    }

    private fun onGetDataError(throwable: Throwable) {
    }

    fun onAutoCompleteItemClick(item: AutoCompleteUnifyDataView, className: String) {
        trackSuggestionShopAds(item, className)
        trackItemWithUrl(item)
        _stateFlow.value = stateValue.updateNavigate(
            AutoCompleteNavigate(
                applink = item.domainModel.applink,
                featureId = item.domainModel.featureId,
            )
        )
    }

    private fun trackItemWithUrl(item: AutoCompleteUnifyDataView) {
        val param =
            createSuggestionTrackerParams(item.domainModel.tracking.trackerUrl)
        suggestionTrackerUseCase.execute(
            param,
            createEmptySuggestionTrackerSubscriber()
        )
    }

    private fun trackSuggestionShopAds(item: AutoCompleteUnifyDataView, className: String) {
        val shopAdsDataView = item.shopAdsDataView ?: return
        topAdsUrlHitter.hitClickUrl(
            className,
            shopAdsDataView.clickUrl,
            "",
            "",
            shopAdsDataView.imageUrl,
        )
    }

    private fun createSuggestionTrackerParams(urlTracker: String) = SuggestionTrackerUseCase.getParams(
        urlTracker,
        userSession.deviceId,
        userSession.userId
    )

    private fun createEmptySuggestionTrackerSubscriber() = object : Subscriber<Void?>() {
        override fun onCompleted() {
        }

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(aVoid: Void?) {
        }
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
            getDeleteRecentSearchSubscriber(item)
        )
    }

    private fun getDeleteRecentSearchSubscriber(item: SuggestionUnify): Subscriber<Boolean> =
        object : Subscriber<Boolean>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(isSuccess: Boolean) {
                if (isSuccess) {
                    deleteSearchItemFromStateResult(item)
                }
            }
        }

    private fun deleteSearchItemFromStateResult(item: SuggestionUnify) {
        val resultList = stateValue.resultList.toMutableList()
        resultList.removeFirst { it.domainModel == item }
        _stateFlow.value = stateValue.updateResultList(resultList)
    }

    private fun getNavSource() = stateValue.getParameterMap()[SearchApiConst.NAVSOURCE] ?: ""

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
            putBoolean(IS_TYPING, stateValue.isTyping)
            putChooseAddressParams(chooseAddressUtilsWrapper.getLocalizingAddressData())
        }
    }

    private fun getUniqueId(userId: String, registrationId: String) = if (userId.isNotEmpty()) {
        AuthHelper.getMD5Hash(userId)
    } else {
        AuthHelper.getMD5Hash(registrationId)
    }

    fun impressTopAds(item: AutoCompleteUnifyDataView, className: String) {
        val adsDataView = item.shopAdsDataView ?: return

        topAdsUrlHitter.hitImpressionUrl(
            className,
            adsDataView.impressionUrl,
            "",
            "",
            adsDataView.imageUrl,
        )
    }
}
