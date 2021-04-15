package com.tokopedia.seller.action.common.presentation.presenter

import android.net.Uri
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.seller.action.common.interfaces.SellerActionContract
import com.tokopedia.seller.action.common.presentation.slices.SellerSlice
import com.tokopedia.seller.action.common.provider.SellerActionProvider
import com.tokopedia.seller.action.order.domain.model.SellerActionOrderCode
import com.tokopedia.seller.action.order.domain.usecase.SliceMainOrderListUseCase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class SellerActionPresenter @Inject constructor(
        private val sliceMainOrderListUseCase: SliceMainOrderListUseCase,
        private val dispatcher: CoroutineDispatchers,
        private val provider: SellerActionProvider
): SellerActionContract.Presenter {

    private var view: SellerActionContract.View? = null

    fun attachView(view: SellerActionContract.View) {
        this.view = view
    }

    override fun getOrderList(sliceUri: Uri, date: String?, sliceHashMap: HashMap<Uri, SellerSlice?>) {
        GlobalScope.launch(dispatcher.io) {
            if (sliceHashMap[sliceUri] == null) {
                try {
                    getSliceMainOrderList(sliceUri, date)
                } catch (ex: Exception) {
                    view?.onErrorGetOrderList(sliceUri)
                }
            }
        }
    }

    private suspend fun getSliceMainOrderList(sliceUri: Uri, date: String?) {
        val filteredDate =
                if (date == null) {
                    provider.getDefaultDate()
                } else {
                    provider.getFormattedDate(date)
                }
        with(sliceMainOrderListUseCase) {
            params = SliceMainOrderListUseCase.createRequestParam(filteredDate, filteredDate, SellerActionOrderCode.STATUS_CODE_DEFAULT)
            view?.onSuccessGetOrderList(sliceUri, sliceMainOrderListUseCase.executeOnBackground())
        }
    }

}