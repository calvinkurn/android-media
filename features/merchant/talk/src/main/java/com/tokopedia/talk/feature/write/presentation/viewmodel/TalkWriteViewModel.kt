package com.tokopedia.talk.feature.write.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.talk.common.coroutine.CoroutineDispatchers
import com.tokopedia.talk.feature.reading.data.model.discussiondata.DiscussionDataResponse
import com.tokopedia.talk.feature.write.domain.usecase.DiscussionGetWritingFormUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TalkWriteViewModel @Inject constructor(private val dispatchers: CoroutineDispatchers, private val discussionGetWritingFormUseCase: DiscussionGetWritingFormUseCase): BaseViewModel(dispatchers.io) {

    private val productId = MutableLiveData<Int>()

    val writeFormData: LiveData<Result<DiscussionDataResponse>> = Transformations.switchMap(productId) {
        getWriteFormData(it)
    }

    private fun getWriteFormData(productId: Int) : LiveData<Result<DiscussionDataResponse>> {
        val result = MutableLiveData<Result<DiscussionDataResponse>>()
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                discussionGetWritingFormUseCase.setParams(productId)
                discussionGetWritingFormUseCase.executeOnBackground()
            }
            result.postValue(Success(response.discussionData))
        }) {
            result.postValue(Fail(it))
        }
        return result
    }

    fun submitWriteForm(text: String) {

    }

    fun setProductId(productId: Int) {
        this.productId.value = productId
    }
}