package com.tokopedia.common_digital.cart.data.datasource

import com.tokopedia.common_digital.cart.data.mapper.ICartMapperData
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData
import com.tokopedia.common_digital.common.data.api.DigitalRestApi
import com.tokopedia.usecase.RequestParams
import rx.Observable

class DigitalGetCartDataSource(private val digitalRestApi: DigitalRestApi, private val cartMapperData: ICartMapperData) {

    fun getCart(requestParam: RequestParams): Observable<CartDigitalInfoData> {
        return digitalRestApi
                .getCart(requestParam.parameters)
                .map { dataResponseResponse ->
                    cartMapperData.transformCartInfoData(
                            dataResponseResponse.body()!!.data
                    )
                }
    }
}
