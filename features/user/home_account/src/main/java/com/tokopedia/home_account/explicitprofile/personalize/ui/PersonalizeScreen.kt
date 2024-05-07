package com.tokopedia.home_account.explicitprofile.personalize.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.LiveData
import com.tokopedia.home_account.explicitprofile.ExplicitProfileConstant
import com.tokopedia.home_account.explicitprofile.personalize.ExplicitPersonalizeResult
import com.tokopedia.home_account.explicitprofile.personalize.PersonalizeSaveAnswerResult
import com.tokopedia.nest.principles.ui.NestTheme

@Composable
fun PersonalizeScreen(
    uiState: LiveData<ExplicitPersonalizeResult>,
    saveAnswerState: LiveData<PersonalizeSaveAnswerResult>,
    counterState: LiveData<Int>,
    onSave: () -> Unit,
    onOptionSelected: (OptionSelected) -> Unit,
    onSkip: () -> Unit,
    onSuccessSaveAnswer: () -> Unit
) {
    val uiStateObserver by uiState.observeAsState(initial = ExplicitPersonalizeResult.Loading)
    val counterStateObserver by counterState.observeAsState(initial = ExplicitProfileConstant.MAX_ANSWER_DEFAULT)
    val saveAnswerStateObserver by saveAnswerState.observeAsState()
    NestTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            if (saveAnswerStateObserver is PersonalizeSaveAnswerResult.Success) {
                SuccessScreen()
                onSuccessSaveAnswer.invoke()
            } else {
                when (val state = uiStateObserver) {
                    is ExplicitPersonalizeResult.Loading -> {
                        LoadingScreen()
                    }
                    is ExplicitPersonalizeResult.Success -> {
                        PersonalizeQuestionScreen(
                            listQuestion = state.listQuestion,
                            countItemSelected = counterStateObserver,
                            maxItemSelected = state.maxItemSelected,
                            minItemSelected = state.minItemSelected,
                            onSave = { onSave.invoke() },
                            onSkip = { onSkip.invoke() },
                            onOptionSelected = { onOptionSelected(it) },
                            isLoadingSaveAnswer = saveAnswerStateObserver is PersonalizeSaveAnswerResult.Loading
                        )
                    }
                    else -> {}
                }
            }
        }
    }
}
