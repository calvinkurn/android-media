package com.tokopedia.talk.feature.write.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.talk.common.coroutine.CoroutineDispatchers
import com.tokopedia.talk.feature.write.data.model.DiscussionGetWritingForm
import com.tokopedia.talk.feature.write.data.TalkCreateNewTalk
import com.tokopedia.talk.feature.write.domain.usecase.DiscussionGetWritingFormUseCase
import com.tokopedia.talk.feature.write.domain.usecase.TalkCreateNewTalkUseCase
import com.tokopedia.talk.feature.write.presentation.uimodel.TalkWriteCategory
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TalkWriteViewModel @Inject constructor(private val dispatchers: CoroutineDispatchers,
                                             private val discussionGetWritingFormUseCase: DiscussionGetWritingFormUseCase,
                                             private val talkCreateNewTalkUseCase: TalkCreateNewTalkUseCase
): BaseViewModel(dispatchers.io) {

    private val productId = MutableLiveData<Int>()

    val writeFormData: LiveData<Result<DiscussionGetWritingForm>> = Transformations.switchMap(productId) {
        getWriteFormData(it)
    }

    val categoryChips: LiveData<List<TalkWriteCategory>> = Transformations.map(writeFormData) {
        when(it) {
            is Success -> {
                it.data.categories
                emptyList()
            }
            is Fail -> {
                emptyList()
            }
        }
    }

    private val _talkCreateNewTalkResponse = MutableLiveData<Result<TalkCreateNewTalk>>()
    val talkCreateNewTalkResponse: LiveData<Result<TalkCreateNewTalk>>
        get() = _talkCreateNewTalkResponse

    private fun getWriteFormData(productId: Int) : LiveData<Result<DiscussionGetWritingForm>> {
        val result = MutableLiveData<Result<DiscussionGetWritingForm>>()
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                discussionGetWritingFormUseCase.setParams(productId)
                discussionGetWritingFormUseCase.executeOnBackground()
            }
            result.postValue(Success(response.discussionGetWritingForm))
        }) {
            result.postValue(Fail(it))
        }
        return result
    }

    fun submitWriteForm(text: String) {
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                productId.value?.let { talkCreateNewTalkUseCase.setParams(it, text) }
                talkCreateNewTalkUseCase.executeOnBackground()
            }
            if(response.talkCreateNewTalk.talkMutationData.isSuccess == 1) {
                _talkCreateNewTalkResponse.postValue(Success(response.talkCreateNewTalk))
            } else {
                _talkCreateNewTalkResponse.postValue(Fail(Throwable(response.talkCreateNewTalk.messageError.firstOrNull())))
            }
        }) {
            _talkCreateNewTalkResponse.postValue(Fail(it))
        }
    }

    fun setProductId(productId: Int) {
        this.productId.value = productId
    }
}