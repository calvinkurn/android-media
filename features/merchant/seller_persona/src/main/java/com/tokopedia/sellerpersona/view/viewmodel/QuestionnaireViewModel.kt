package com.tokopedia.sellerpersona.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerpersona.data.remote.usecase.FetchPersonaQuestionnaireUseCase
import com.tokopedia.sellerpersona.view.model.QuestionnairePagerUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 30/01/23.
 */

class QuestionnaireViewModel @Inject constructor(
    private val fetchQuestionnaireUseCase: Lazy<FetchPersonaQuestionnaireUseCase>,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    val questionnaire: LiveData<Result<List<QuestionnairePagerUiModel>>>
        get() = _questionnaire

    private val _questionnaire: MutableLiveData<Result<List<QuestionnairePagerUiModel>>> =
        MutableLiveData()

    fun fetchQuestionnaire() {
        launchCatchError(block = {
            val data = fetchQuestionnaireUseCase.get().execute()
            _questionnaire.postValue(Success(data))
        }, onError = {
            _questionnaire.postValue(Fail(it))
        })
    }
}