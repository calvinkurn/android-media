package com.tokopedia.layanan_finansial.view.viewModel

import android.telephony.gsm.GsmCellLocation
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.layanan_finansial.di.LayananScope
import com.tokopedia.layanan_finansial.view.models.LayananFinansialModel
import com.tokopedia.layanan_finansial.view.models.LayananFinansialOuter
import com.tokopedia.layanan_finansial.view.usecase.LayananUsecase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Dispatchers
import java.lang.NullPointerException
import javax.inject.Inject

@LayananScope
class LayananFinansialViewModel @Inject constructor(private val useCase: LayananUsecase) : BaseViewModel(Dispatchers.Main) {
    val liveData = MutableLiveData<Result<LayananFinansialModel>>()

    fun getDetail(){
        launchCatchError(block = {
          val data = useCase.execute().data
            data?.let {
                liveData.value = Success(data = data)
            } ?: throw NullPointerException()

        }){
          liveData.value = Fail(it)
        }
    }
}