package com.tokopedia.product_bundle.single.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.product_bundle.single.presentation.model.SingleProductBundleItem
import com.tokopedia.product_bundle.single.presentation.model.SingleProductBundleUiModel
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

class SingleProductBundleViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val mSingleProductBundleUiModel = MutableLiveData<SingleProductBundleUiModel>()
    val singleProductBundleUiModel: LiveData<SingleProductBundleUiModel>
        get() = mSingleProductBundleUiModel

    fun getBundleData() {
        mSingleProductBundleUiModel.value = SingleProductBundleUiModel(
                50,
                List(10) { SingleProductBundleItem(
                        "Paket isi 3",
                        "Women’s Breathable Low-cut Short Socks Cotton Blend",
                        "Rp300.000",
                        "Rp200.000",
                        45,
                        "https://placekitten.com/200/300"
                )},
                "Rp100.000",
                "Rp90.000",
                "Rp10.000",
                10
        )
    }

}