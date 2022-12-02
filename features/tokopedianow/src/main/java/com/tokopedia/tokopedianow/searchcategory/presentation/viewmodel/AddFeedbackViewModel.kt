package com.tokopedia.tokopedianow.searchcategory.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokopedianow.searchcategory.domain.model.AddProductFeedbackModel
import com.tokopedia.tokopedianow.searchcategory.domain.usecase.AddProductFeedbackUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class AddFeedbackViewModel @Inject constructor(
    dispatchers:CoroutineDispatchers,
    private val addProductFeedbackUseCase: UseCase<AddProductFeedbackModel>
) : BaseViewModel(dispatchers.io) {

    companion object{
        private const val SUCCESS_CODE = "200"
    }

    private val _addFeedbackResult:MutableLiveData<Result<AddProductFeedbackModel>> = MutableLiveData(null)
    val addFeedbackResult: LiveData<Result<AddProductFeedbackModel>> = _addFeedbackResult

    fun addProductFeedback(feedback:String){
        addProductFeedbackUseCase.execute(
            ::onAddFeedbackSuccess,
            ::onAddFeedbackFailure,
            getRequestParams(feedback)
        )
    }

    private fun getRequestParams(feedback: String) : RequestParams{
        return RequestParams().apply {
            putString(AddProductFeedbackUseCase.FEEDBACK_QUERY_PARAM,feedback)
        }
    }

    private fun onAddFeedbackSuccess(result:AddProductFeedbackModel){
        try{
          result.tokonowAddFeedback?.header?.let {
               if (it.errorCode == SUCCESS_CODE) {
                   _addFeedbackResult.value = Success(result)
               } else {
                   throw Throwable(it.reason)
               }
           } ?: onAddFeedbackFailure(Throwable())
        }
        catch (err:Throwable){
            onAddFeedbackFailure(err)
        }
    }

    private fun onAddFeedbackFailure(error:Throwable){
      _addFeedbackResult.value = Fail(error)
    }
}
