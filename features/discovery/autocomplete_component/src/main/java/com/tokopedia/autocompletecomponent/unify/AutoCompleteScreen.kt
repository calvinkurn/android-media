package com.tokopedia.autocompletecomponent.unify

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.search.AppLogSearch
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.BLANKPAGE_ENTER_FROM
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.BLANKPAGE_ENTER_METHOD
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.NEW_SUG_SESSION_ID
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.RECOM_SEARCH
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.SEARCH_HISTORY
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.SEARCH_SUG
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.SUG_RECOM
import com.tokopedia.applink.RouteManager
import com.tokopedia.autocompletecomponent.unify.compose_component.AutoCompleteEducationComponent
import com.tokopedia.autocompletecomponent.unify.compose_component.AutoCompleteMasterComponent
import com.tokopedia.autocompletecomponent.unify.compose_component.AutoCompleteTitleComponent
import com.tokopedia.autocompletecomponent.util.AutoCompleteNavigate
import com.tokopedia.autocompletecomponent.util.AutoCompleteTemplateEnum
import com.tokopedia.autocompletecomponent.util.FEATURE_ID_CURATED
import com.tokopedia.autocompletecomponent.util.FEATURE_ID_KEYWORD
import com.tokopedia.autocompletecomponent.util.FEATURE_ID_POPULAR_CURATED
import com.tokopedia.autocompletecomponent.util.FEATURE_ID_POPULAR_RELATED
import com.tokopedia.autocompletecomponent.util.FEATURE_ID_RECENT_KEYWORD
import com.tokopedia.autocompletecomponent.util.FEATURE_ID_RECENT_SEARCH
import com.tokopedia.autocompletecomponent.util.FEATURE_ID_RELATED_KEYWORD
import com.tokopedia.autocompletecomponent.util.getModifiedApplink
import com.tokopedia.iris.Iris
import com.tokopedia.nest.principles.utils.addImpression
import com.tokopedia.nest.principles.utils.tag
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle

@Composable
internal fun AutoCompleteScreen(
    viewModel: AutoCompleteViewModel,
    iris: Iris,
    listener: AutoCompleteListener?,
    searchEntrance: String,
) {
    val analytics = TrackApp.getInstance().gtm
    val lazyListState = rememberLazyListState()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
        val state = viewModel.stateFlow.collectAsStateWithLifecycle()

        EvaluateNavigation(
            viewModel = viewModel,
            navigate = state.value.navigate,
            listener = listener,
        )
        EvaluateActionReplace(
            viewModel = viewModel,
            replaceKeyword = state.value.actionReplaceKeyword,
            listener = listener
        )

        LaunchedEffect(key1 = state.value.appLogData.imprId) {
            trackByteIOAutoComplete(state.value, searchEntrance)
        }

        LazyColumn(
            modifier = Modifier.tag("MainLazyColumn"),
            state = lazyListState
        ) {
            this@LazyColumn.itemsIndexed(
                items = state.value.resultList,
                key = { idx, it ->
                    it.domainModel.title.text + it.uniqueIdentifier + idx
                }
            ) { idx, item ->
                AutoCompleteItemComponent(
                    item,
                    idx,
                    lazyListState,
                    iris,
                    viewModel,
                    analytics,
                    searchEntrance
                )
            }
        }
    }
}

@Composable
private fun AutoCompleteItemComponent(
    item: AutoCompleteUnifyDataView,
    idx: Int,
    lazyListState: LazyListState,
    iris: Iris,
    viewModel: AutoCompleteViewModel,
    analytics: ContextAnalytics,
    searchEntrance: String,
    className: String = LocalContext.current.javaClass.name
) {
    Box(
        modifier = Modifier.addImpression(
            uniqueIdentifier = item.domainModel.title.text + item.uniqueIdentifier + idx,
            impressionState = item.impressionHolder,
            state = lazyListState,
            onItemViewed = {
                item.impress(iris)
                viewModel.impressTopAds(
                    item,
                    className,
                )
                if (viewModel.stateValue.isSuggestion)
                    AppLogSearch.eventTrendingWordsShowSuggestion(
                        trendingWordsSuggestion(viewModel, item, searchEntrance)
                    )
            },
            impressInterval = 0L
        )
    ) {
        when (item.domainModel.template) {
            AutoCompleteTemplateEnum.Master.toString() -> {
                AutoCompleteMasterComponent(
                    item,
                    onItemClicked = {
                        viewModel.onAutoCompleteItemClick(
                            it,
                            className,
                        )
                        it.click(analytics)
                        if (viewModel.stateValue.isSuggestion) {
                            AppLogAnalytics.putPageData(
                                AppLogSearch.ParamKey.SUG_TYPE,
                                item.sugType,
                            )

                            AppLogAnalytics.putPageData(
                                AppLogSearch.ParamKey.PRE_CLICK_ID,
                                viewModel.stateValue.appLogData.imprId,
                            )

                            AppLogSearch.eventTrendingWordsClickSuggestion(
                                trendingWordsSuggestion(viewModel, item, searchEntrance)
                            )
                        }
                    },
                    onItemAction = {
                        viewModel.onAutocompleteItemAction(it)
                    }
                )
            }

            AutoCompleteTemplateEnum.Education.toString() -> {
                AutoCompleteEducationComponent(item, onItemClicked = {
                    viewModel.onAutoCompleteItemClick(it, className)
                    it.click(analytics)
                })
            }

            AutoCompleteTemplateEnum.Title.toString() -> {
                AutoCompleteTitleComponent(item) {
                    viewModel.onAutocompleteItemAction(it)
                }
            }
        }
    }
}

private fun trackByteIOAutoComplete(state: AutoCompleteState, searchEntrance: String) {
    if (state.appLogData.imprId.isBlank()) return

    if (state.isInitialState) {
        val enterFrom = AppLogSearch.enterFrom()

        AppLogSearch.eventEnterSearchBlankPage(enterFrom, state.enterMethod, searchEntrance)

        AppLogAnalytics.putPageData(BLANKPAGE_ENTER_FROM, enterFrom)
        AppLogAnalytics.putPageData(BLANKPAGE_ENTER_METHOD, state.enterMethod)
    } else if (state.isSuggestion) {
        AppLogSearch.eventTrendingShow(
            AppLogSearch.TrendingShow(
                imprId = state.appLogData.imprId,
                newSugSessionId = state.appLogData.newSugSessionId,
                rawQuery = state.query,
                enterMethod = state.enterMethod,
                wordsSource = state.appLogData.wordsSource,
                wordsNum = state.resultList.filter {
                    it.domainModel.isTrendingWord()
                }.size,
                searchEntrance = searchEntrance,
            )
        )

        AppLogAnalytics.putPageData(NEW_SUG_SESSION_ID, state.appLogData.newSugSessionId)
    }
}

@Composable
private fun EvaluateNavigation(
    viewModel: AutoCompleteViewModel,
    navigate: AutoCompleteNavigate?,
    listener: AutoCompleteListener?,
) {
    if(navigate == null) return
    handlePageDataBeforeNavigated(viewModel)
    LocalContext.current.apply {
        val modifiedApplink = getModifiedApplink(
            navigate.applink,
            viewModel.stateValue.parameter,
            enterMethod(navigate.featureId),
        )

        AppLogAnalytics.putPageData(AppLogParam.IS_SHADOW, true)

        listener?.finish()
        RouteManager.route(this, modifiedApplink)
    }
    viewModel.onNavigated()

}

private fun handlePageDataBeforeNavigated(viewModel: AutoCompleteViewModel){
    if(viewModel.isInitialState) {
        AppLogAnalytics.removePageData(NEW_SUG_SESSION_ID)
    }
}

private fun enterMethod(featureId: String?) =
    when (featureId) {
        FEATURE_ID_RECENT_SEARCH, FEATURE_ID_RECENT_KEYWORD -> SEARCH_HISTORY
        FEATURE_ID_POPULAR_CURATED, FEATURE_ID_POPULAR_RELATED -> RECOM_SEARCH
        FEATURE_ID_KEYWORD -> SEARCH_SUG
        FEATURE_ID_CURATED, FEATURE_ID_RELATED_KEYWORD -> SUG_RECOM
        else -> ""
    }

@Composable
private fun EvaluateActionReplace(
    viewModel: AutoCompleteViewModel,
    replaceKeyword: String?,
    listener: AutoCompleteListener?
) {
    if (replaceKeyword != null) {
        listener?.setSearchQueryKeyword(replaceKeyword)
        viewModel.onActionReplaceAcknowledged()
    }
}

private fun trendingWordsSuggestion(
    viewModel: AutoCompleteViewModel,
    item: AutoCompleteUnifyDataView,
    searchEntrance: String,
) = AppLogSearch.TrendingWordsSuggestion(
    groupId = item.groupId,
    imprId = viewModel.stateValue.appLogData.imprId,
    newSugSessionId = viewModel.stateValue.appLogData.newSugSessionId,
    rawQuery = item.searchTerm,
    enterMethod = viewModel.stateValue.enterMethod,
    sugType = item.sugType,
    wordsContent = item.domainModel.title.text,
    wordsPosition = item.appLogIndex,
    searchEntrance = searchEntrance,
)
