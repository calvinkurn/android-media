package com.tokopedia.homecredit.domain

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject


class HomeCreditUseCase @Inject constructor(
     @ApplicationContext val context: Context): UseCase<OutputState>() {


     fun saveDetail(success : (OutputState)-> Unit, onFail:(OutputState)-> Unit){
          execute({
               success(OutputState.SuccessState)
          },{
               onFail(OutputState.FailState)
          })
     }

     override suspend fun executeOnBackground(): OutputState {
          return OutputState.SuccessState
     }


}


sealed class OutputState()
{
     object SuccessState: OutputState()
     object FailState: OutputState()
}