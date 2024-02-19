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
import com.tokopedia.analytics.byteio.search.AppLogSearch
import com.tokopedia.autocompletecomponent.unify.compose_component.AutoCompleteEducationComponent
import com.tokopedia.autocompletecomponent.unify.compose_component.AutoCompleteMasterComponent
import com.tokopedia.autocompletecomponent.unify.compose_component.AutoCompleteTitleComponent
import com.tokopedia.autocompletecomponent.util.AutoCompleteNavigate
import com.tokopedia.autocompletecomponent.util.AutoCompleteTemplateEnum
import com.tokopedia.autocompletecomponent.util.routeManagerIntent
import com.tokopedia.autocompletecomponent.util.getModifiedApplink
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.iris.Iris
import com.tokopedia.nest.principles.utils.tag
import com.tokopedia.track.TrackApp
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle

@Composable
internal fun AutoCompleteScreen(
    viewModel: AutoCompleteViewModel,
    iris: Iris,
    listener: AutoCompleteListener?
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
            navigate = state.value.navigate
        )
        EvaluateActionReplace(
            viewModel = viewModel,
            replaceKeyword = state.value.actionReplaceKeyword,
            listener = listener
        )
        LazyColumn(
            modifier = Modifier.tag("MainLazyColumn")
        ) {
            items(
                state.value.resultList
            ) { item ->
                LaunchedEffect(key1 = !item.impressionHolder.impressed, block = {
                    item.impressionHolder.impressed = true
                    item.impress(iris)
                })
                when (item.domainModel.template) {
                    AutoCompleteTemplateEnum.Master.toString() -> {
                        AutoCompleteMasterComponent(
                            item,
                            onItemClicked = {
                                viewModel.onAutoCompleteItemClick(it)
                                it.click(analytics)
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

@Composable
private fun EvaluateNavigation(
    viewModel: AutoCompleteViewModel,
    navigate: AutoCompleteNavigate?
) {
    if (navigate != null) {
        LocalContext.current.apply {
            val modifiedApplink = getModifiedApplink(navigate.applink, SearchParameter())
            val intent = routeManagerIntent(
                context = this@apply,
                applink = modifiedApplink,
                enterMethod = enterMethod(viewModel),
            )
            startActivity(intent)
        }
        viewModel.onNavigated()
    }
}

private fun enterMethod(viewModel: AutoCompleteViewModel) =
    if (viewModel.isInitialState)
        AppLogSearch.ParamValue.DEFAULT_SEARCH_KEYWORD
    else if (viewModel.isSuggestion)
        AppLogSearch.ParamValue.SEARCH_SUG
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
