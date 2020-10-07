package com.tokopedia.seller.action.slices

import android.net.Uri
import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.slice.Slice
import androidx.slice.SliceProvider
import com.tokopedia.seller.action.common.const.SellerActionConst
import com.tokopedia.seller.action.common.di.DaggerSellerActionComponent
import com.tokopedia.seller.action.data.model.SellerActionOrderList
import com.tokopedia.seller.action.data.model.exception.SellerActionException
import com.tokopedia.seller.action.data.repository.SliceMainOrderListRepository
import com.tokopedia.seller.action.slices.item.SellerLoadingSlice
import com.tokopedia.seller.action.slices.item.SellerOrderSlice
import com.tokopedia.seller.action.slices.item.SellerSlice
import com.tokopedia.seller.action.slices.item.SellerUnknownSlice
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class SellerActionSliceProvider: SliceProvider(){

    @Inject
    lateinit var sliceMainOrderListRepository: SliceMainOrderListRepository

    @Inject
    lateinit var handler: Handler

    private val observer = Observer<Result<Pair<Uri, List<SellerActionOrderList.Data.OrderList.Order>>>> { result ->
        if (result != null) {
            when(result) {
                is Success -> updateSlice(result.data.first)
                is Fail -> (result.throwable as? SellerActionException)?.sliceUri?.let { updateSlice(it) }
            }
        }
    }

    lateinit var orderListLiveData: LiveData<Result<Pair<Uri, List<SellerActionOrderList.Data.OrderList.Order>>>>

    override fun onCreateSliceProvider(): Boolean {
        injectDependencies()
        return context != null
    }

    override fun onBindSlice(sliceUri: Uri): Slice {
        orderListLiveData = getOrderListLiveData(sliceUri)
        return createNewSlice(sliceUri).getSlice()
    }

    private fun injectDependencies() {
        DaggerSellerActionComponent.builder()
                .build()
                .inject(this)
    }

    private fun getOrderListLiveData(sliceUri: Uri) =
            sliceMainOrderListRepository.getOrderList(sliceUri).apply {
                handler.post {
                    observeForever(observer)
                }
            }

    private fun createNewSlice(sliceUri: Uri): SellerSlice {
        val notNullContext = requireNotNull(context)
        val orderList = orderListLiveData.value
        return when (sliceUri.path) {
            SellerActionConst.Deeplink.ORDER -> {
                if (orderList != null) {
                    handler.post {
                        orderListLiveData.removeObserver(observer)
                    }
                    when(orderList) {
                        is Success -> {
                            orderList.data.second.let { list ->
                                SellerOrderSlice(
                                        notNullContext,
                                        sliceUri,
                                        list
                                )
                            }
                        }
                        is Fail -> SellerUnknownSlice(notNullContext, sliceUri)
                    }
                } else {
                    SellerLoadingSlice(notNullContext, sliceUri)
                }
            }
            else -> SellerUnknownSlice(
                    notNullContext,
                    sliceUri
            )
        }
    }

    private fun updateSlice(sliceUri: Uri) {
        context?.contentResolver?.notifyChange(sliceUri, null)
    }

}