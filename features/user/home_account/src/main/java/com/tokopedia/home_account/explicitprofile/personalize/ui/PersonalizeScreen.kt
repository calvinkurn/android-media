package com.tokopedia.home_account.explicitprofile.personalize.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.LiveData
import com.tokopedia.home_account.explicitprofile.personalize.ExplicitPersonalizeResult
import com.tokopedia.nest.principles.ui.NestTheme

@Composable
fun PersonalizeScreen(
    uiState: LiveData<ExplicitPersonalizeResult>,
    counterState: LiveData<Int>,
    onRetry: () -> Unit,
    onSave: () -> Unit,
    onOptionSelected: (OptionSelected) -> Unit,
    onSkip: () -> Unit
) {
    val uiStateObserver by uiState.observeAsState(initial = ExplicitPersonalizeResult.Loading)
    val counterStateObserver by counterState.observeAsState(initial = 10)
    NestTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            when (val state = uiStateObserver) {
                is ExplicitPersonalizeResult.Loading -> {
                    LoadingScreen()
                }
                is ExplicitPersonalizeResult.Success -> {
                    PersonalizeQuestionScreen(
                        listQuestion = state.listQuestion,
                        countItemSelected = counterStateObserver,
                        maxItemSelected = state.maxItemSelected,
                        onSave = { onSave.invoke() },
                        onSkip = { onSkip.invoke() },
                        onOptionSelected = { onOptionSelected(it) }
                    )
                }
                is ExplicitPersonalizeResult.Failed -> {
                    FailedScreen {
                        onRetry.invoke()
                    }
                }
            }

        }
    }
}
