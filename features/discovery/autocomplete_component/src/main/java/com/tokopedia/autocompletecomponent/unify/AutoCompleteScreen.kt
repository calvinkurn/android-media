package com.tokopedia.autocompletecomponent.unify

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.SEARCH_HISTORY
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.SEARCH_SUG
import com.tokopedia.applink.RouteManager
import com.tokopedia.autocompletecomponent.unify.compose_component.AutoCompleteEducationComponent
import com.tokopedia.autocompletecomponent.unify.compose_component.AutoCompleteMasterComponent
import com.tokopedia.autocompletecomponent.unify.compose_component.AutoCompleteTitleComponent
import com.tokopedia.autocompletecomponent.util.AutoCompleteNavigate
import com.tokopedia.autocompletecomponent.util.AutoCompleteTemplateEnum
import com.tokopedia.autocompletecomponent.util.getModifiedApplink
import com.tokopedia.iris.Iris
import com.tokopedia.nest.principles.utils.tag
import com.tokopedia.track.TrackApp
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle

@Composable
internal fun AutoCompleteScreen(
    viewModel: AutoCompleteViewModel,
    iris: Iris,
    listener: AutoCompleteListener?,
) {
    val analytics = TrackApp.getInstance().gtm

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
            trackByteIOAutoComplete(state.value)
        }

        LazyColumn(
            modifier = Modifier.tag("MainLazyColumn")
        ) {
            items(
                state.value.resultList
            ) { item ->
                LaunchedEffect(key1 = item, block = {
                    item.impress(iris)
                    if (viewModel.stateValue.isSuggestion)
                        AppLogSearch.eventTrendingWordsShowSuggestion(
                            trendingWordsSuggestion(viewModel, item)
                        )
                })
                when (item.domainModel.template) {
                    AutoCompleteTemplateEnum.Master.toString() -> {
                        AutoCompleteMasterComponent(
                            item,
                            onItemClicked = {
                                viewModel.onAutoCompleteItemClick(it)
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
                                        trendingWordsSuggestion(viewModel, item)
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
                            viewModel.onAutoCompleteItemClick(it)
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
    }
}

private fun trackByteIOAutoComplete(state: AutoCompleteState) {
    if (state.appLogData.imprId.isBlank()) return

    if (state.isInitialState) {
        val enterFrom = AppLogSearch.enterFrom()

        AppLogSearch.eventEnterSearchBlankPage(enterFrom, state.enterMethod)

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
                }.size
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
    if (navigate != null) {
        LocalContext.current.apply {
            val modifiedApplink = getModifiedApplink(
                navigate.applink,
                viewModel.stateValue.parameter,
                enterMethod(viewModel),
            )

            AppLogAnalytics.putPageData(AppLogParam.IS_SHADOW, true)

            listener?.finish()
            RouteManager.route(this, modifiedApplink)
        }
        viewModel.onNavigated()
    }
}

private fun enterMethod(viewModel: AutoCompleteViewModel) =
    if (viewModel.isInitialState) SEARCH_HISTORY
    else if (viewModel.isSuggestion) SEARCH_SUG
    else ""

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
    item: AutoCompleteUnifyDataView
) = AppLogSearch.TrendingWordsSuggestion(
    groupId = item.groupId,
    imprId = viewModel.stateValue.appLogData.imprId,
    newSugSessionId = viewModel.stateValue.appLogData.newSugSessionId,
    rawQuery = item.searchTerm,
    enterMethod = viewModel.stateValue.enterMethod,
    sugType = item.sugType,
    wordsContent = item.domainModel.title.text,
    wordsPosition = item.appLogIndex,
)
