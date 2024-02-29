package com.tokopedia.home_account.explicitprofile.personalize

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_account.explicitprofile.data.QuestionDataModel
import com.tokopedia.home_account.explicitprofile.domain.ExplicitProfileGetQuestionUseCase
import com.tokopedia.home_account.explicitprofile.personalize.ui.OptionSelected
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExplicitPersonalizeViewModel @Inject constructor(
    val explicitProfileGetQuestionUseCase: ExplicitProfileGetQuestionUseCase,
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    private val _uiState = MutableLiveData<ExplicitPersonalizeResult>()
    val uiState : LiveData<ExplicitPersonalizeResult> get() = _uiState

    private val _counterState = MutableLiveData(0)
    val counterState : LiveData<Int> get() = _counterState

    private val selectedOptions = mutableListOf<String>()

    init {
        getQuestion()
    }

    fun itemSelected(item: OptionSelected) {
        val currentCounter = _counterState.value
        if (item.isSelected) {
            selectedOptions.remove(item.name)
        } else {
            selectedOptions.add(item.name)
        }

        _counterState.value = if (item.isSelected) {
                (currentCounter?.minus(1))
            } else {
                (currentCounter?.plus(1))
            }

        when (val state = uiState.value) {
            is ExplicitPersonalizeResult.Success -> {
                val currentList = state.listQuestion
                currentList[item.indexCategory].property.options[item.indexOption].isSelected = !item.isSelected

                _uiState.value = ExplicitPersonalizeResult.Success(
                    listQuestion = currentList,
                    maxItemSelected = state.maxItemSelected
                )
            }
            else -> {}
        }
    }

    fun getQuestion() {
        _uiState.value = ExplicitPersonalizeResult.Loading

        launchCatchError (
            block = {
                withContext(coroutineContext) {
                    val response = explicitProfileGetQuestionUseCase(CATEGORY_PREFERENCE)
                    if (response.isNotEmpty() && response.first().layout == MULTIPLE_ANSWER) {
                        _uiState.value = ExplicitPersonalizeResult.Success(
                            listQuestion = response.first().questions,
                            maxItemSelected = response.first().maxAnswer
                        )
                    } else {
                        _uiState.value = ExplicitPersonalizeResult.Failed
                    }
                }
            },
            onError = {
                _uiState.value = ExplicitPersonalizeResult.Failed
            }
        )

    }

    companion object {
        private const val CATEGORY_PREFERENCE = "category_preference"
        private const val MULTIPLE_ANSWER = "MultipleAnswer"
    }

}

sealed interface ExplicitPersonalizeResult {
    object Loading : ExplicitPersonalizeResult
    data class Success(
        val listQuestion: List<QuestionDataModel>,
        val maxItemSelected: Int
    ) : ExplicitPersonalizeResult
    object Failed : ExplicitPersonalizeResult
}
