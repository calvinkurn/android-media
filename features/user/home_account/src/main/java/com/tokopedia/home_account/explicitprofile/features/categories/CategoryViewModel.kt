package com.tokopedia.home_account.explicitprofile.features.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_account.explicitprofile.data.ExplicitprofileGetQuestion
import com.tokopedia.home_account.explicitprofile.domain.GetQuestionsUseCase
import com.tokopedia.home_account.explicitprofile.wrapper.ExplicitProfileResult
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import javax.inject.Inject

class CategoryViewModel @Inject constructor(
    private val getQuestionsUseCase: GetQuestionsUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _questions = MutableLiveData<ExplicitProfileResult<ExplicitprofileGetQuestion>>()
    val questions : LiveData<ExplicitProfileResult<ExplicitprofileGetQuestion>>
        get() = _questions

    fun getQuestion(template: String) {
        launchCatchError(coroutineContext, {
            _questions.value = ExplicitProfileResult.Loading()

            val response = getQuestionsUseCase(
                GetQuestionsUseCase.QuestionParams(
                    checkActive = false,
                    templateName = template
                )
            )

            _questions.value = ExplicitProfileResult.Success(response)
        }, {
            _questions.value = ExplicitProfileResult.Failure(it)
        })
    }
}