package com.tokopedia.seller.action.common.presentation.presenter

import android.net.Uri
import com.tokopedia.kotlin.extensions.convertFormatDate
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.seller.action.common.const.SellerActionConst
import com.tokopedia.seller.action.common.dispatcher.SellerActionDispatcherProvider
import com.tokopedia.seller.action.common.interfaces.SellerActionContract
import com.tokopedia.seller.action.common.presentation.slices.SellerSlice
import com.tokopedia.seller.action.order.domain.model.SellerActionOrderCode
import com.tokopedia.seller.action.order.domain.usecase.SliceMainOrderListUseCase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class SellerActionPresenter @Inject constructor(
        private val sliceMainOrderListUseCase: SliceMainOrderListUseCase,
        private val dispatcher: SellerActionDispatcherProvider
): SellerActionContract.Presenter {

    private var view: SellerActionContract.View? = null

    fun attachView(view: SellerActionContract.View) {
        this.view = view
    }

    override fun getOrderList(sliceUri: Uri, date: String?, sliceHashMap: HashMap<Uri, SellerSlice?>) {
        GlobalScope.launch(dispatcher.io()) {
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
        val filteredDate = date?.let {
            convertFormatDate(it, SellerActionConst.SLICE_DATE_FORMAT, SellerActionConst.REQUEST_DATE_FORMAT)
        } ?: Date().toFormattedString(SellerActionConst.REQUEST_DATE_FORMAT)
        with(sliceMainOrderListUseCase) {
            params = SliceMainOrderListUseCase.createRequestParam(filteredDate, filteredDate, SellerActionOrderCode.STATUS_CODE_DEFAULT)
            view?.onSuccessGetOrderList(sliceUri, sliceMainOrderListUseCase.executeOnBackground())
        }
    }

}