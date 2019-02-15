package com.tokopedia.expresscheckout.domain.usecase

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.data.entity.request.Cart
import com.tokopedia.expresscheckout.data.entity.request.CheckoutParam
import com.tokopedia.expresscheckout.data.entity.request.CheckoutRequestParam
import com.tokopedia.expresscheckout.data.entity.request.Profile
import com.tokopedia.expresscheckout.data.entity.response.checkout.CheckoutExpressGqlResponse
import com.tokopedia.expresscheckout.view.variant.viewmodel.FragmentViewModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.transactiondata.entity.request.DataCheckoutRequest
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 03/02/19.
 */

open class DoCheckoutExpressUseCase @Inject constructor(@ApplicationContext val context: Context) : GraphqlUseCase() {

    val variables = HashMap<String, Any?>()

    fun setParams(fragmentViewModel: FragmentViewModel, checkoutData: DataCheckoutRequest) {
        val cart = Cart()
        cart.setDefaultProfile = fragmentViewModel.getProfileViewModel()?.isDefaultProfileCheckboxChecked
        cart.promoCode = ""
        cart.isDonation = 0
        cart.data = arrayListOf(checkoutData)

        val checkoutParam = CheckoutParam()
        val profile = Profile()
        profile.addressId = fragmentViewModel.getProfileViewModel()?.addressId
        profile.description = ""
        profile.gatewayCode = fragmentViewModel.atcResponseModel?.atcDataModel?.userProfileModelDefaultModel?.paymentModel?.gatewayCode
        profile.status = fragmentViewModel.atcResponseModel?.atcDataModel?.userProfileModelDefaultModel?.status
        profile.profileId = fragmentViewModel.atcResponseModel?.atcDataModel?.userProfileModelDefaultModel?.id
        profile.checkoutParam = checkoutParam

        val checkoutRequestParam = CheckoutRequestParam()
        checkoutRequestParam.carts = cart
        checkoutRequestParam.profile = profile

        val jsonTreeCheckoutRequest = Gson().toJsonTree(checkoutRequestParam)
        val jsonObjectCheckoutRequest = jsonTreeCheckoutRequest.asJsonObject
        variables.put("params", jsonObjectCheckoutRequest)
    }

    override fun execute(requestParams: RequestParams?, subscriber: Subscriber<GraphqlResponse>?) {
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.mutation_checkout_express), CheckoutExpressGqlResponse::class.java, variables)
        clearRequest()
        addRequest(graphqlRequest)

        super.execute(requestParams, subscriber)
    }
}