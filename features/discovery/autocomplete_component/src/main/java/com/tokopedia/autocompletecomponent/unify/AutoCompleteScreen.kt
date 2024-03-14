package com.tokopedia.autocompletecomponent.unify

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.tokopedia.applink.RouteManager
import com.tokopedia.autocompletecomponent.unify.compose_component.AutoCompleteEducationComponent
import com.tokopedia.autocompletecomponent.unify.compose_component.AutoCompleteMasterComponent
import com.tokopedia.autocompletecomponent.unify.compose_component.AutoCompleteTitleComponent
import com.tokopedia.autocompletecomponent.util.AutoCompleteNavigate
import com.tokopedia.autocompletecomponent.util.AutoCompleteTemplateEnum
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
    listener: AutoCompleteListener?
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
            navigate = state.value.navigate
        )
        EvaluateActionReplace(
            viewModel = viewModel,
            replaceKeyword = state.value.actionReplaceKeyword,
            listener = listener
        )
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
                AutoCompleteItemComponent(item, idx, lazyListState, iris, viewModel, analytics)
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

@Composable
private fun EvaluateNavigation(
    viewModel: AutoCompleteViewModel,
    navigate: AutoCompleteNavigate?
) {
    if (navigate != null) {
        LocalContext.current.apply {
            val modifiedApplink = getModifiedApplink(navigate.applink, navigate.searchParameter)
            RouteManager.route(this, modifiedApplink)
            if (this is Activity) {
                this.finish()
            }
        }
        viewModel.onNavigated()
    }
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
