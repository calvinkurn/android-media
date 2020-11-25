package com.tokopedia.thankyou_native.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.thankyou_native.data.mapper.DetailInvoiceMapper
import com.tokopedia.thankyou_native.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailInvoiceViewModel @Inject constructor(
        @CoroutineMainDispatcher dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val mutableInvoiceVisitables = MutableLiveData<ArrayList<Visitable<*>>>()

    fun createInvoiceData(thanksPageData: ThanksPageData) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.IO) {
                DetailInvoiceMapper(thanksPageData).getDetailedInvoice()
            }
            mutableInvoiceVisitables.value = data
        }) {
            it.printStackTrace()
        }
    }
}