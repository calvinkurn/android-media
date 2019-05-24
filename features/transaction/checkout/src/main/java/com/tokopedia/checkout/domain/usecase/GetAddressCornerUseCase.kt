package com.tokopedia.checkout.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.checkout.R
import com.tokopedia.checkout.domain.datamodel.addresscorner.AddressCornerResponse
import com.tokopedia.checkout.domain.datamodel.addresscorner.GqlKeroWithAddressResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.transactiondata.entity.request.AddressRequest
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.HashMap

/**
 * Created by fajarnuha on 2019-05-21.
 */
public class GetAddressCornerUseCase(val context: Context, val usecase: GraphqlUseCase) {

    private val PARAM_ADDRESS_USECASE: String = "input"

    fun getObservable(): Observable<AddressCornerResponse> {
        usecase.clearRequest()
        usecase.addRequest(getRequest())
        return usecase.getExecuteObservable(null)
                .map { graphqlResponse -> graphqlResponse.getData<AddressCornerResponse>(AddressCornerResponse::class.java) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private fun getRequest(): GraphqlRequest {
        val request: AddressRequest = AddressRequest(showAddress = true, showCorner = true)
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.address_corner)
        return GraphqlRequest(query, GqlKeroWithAddressResponse::class.java, getParam(request))
    }

    private fun getParam(json: Any): Map<String, Any> {
        val params = HashMap<String, Any>()
        params[PARAM_ADDRESS_USECASE] = json
        return params
    }
}