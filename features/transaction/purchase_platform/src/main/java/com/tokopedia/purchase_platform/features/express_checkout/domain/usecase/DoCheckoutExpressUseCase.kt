package com.tokopedia.purchase_platform.features.express_checkout.domain.usecase

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.purchase_platform.features.express_checkout.data.entity.request.Cart
import com.tokopedia.purchase_platform.features.express_checkout.data.entity.request.CheckoutParam
import com.tokopedia.purchase_platform.features.express_checkout.data.entity.request.CheckoutRequestParam
import com.tokopedia.purchase_platform.features.express_checkout.data.entity.request.Profile
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.uimodel.FragmentUiModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.express_checkout.data.entity.response.checkout.CheckoutExpressGqlResponse
import com.tokopedia.purchase_platform.common.data.model.request.checkout.DataCheckoutRequest
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 03/02/19.
 */

open class DoCheckoutExpressUseCase @Inject constructor(@ApplicationContext val context: Context) : GraphqlUseCase() {

    val variables = HashMap<String, Any?>()

    fun setParams(fragmentUiModel: FragmentUiModel, checkoutData: DataCheckoutRequest) {
        val cart = Cart()
        cart.setDefaultProfile = fragmentUiModel.getProfileViewModel()?.isDefaultProfileCheckboxChecked
        cart.promoCode = ""
        cart.isDonation = 0
        cart.data = arrayListOf(checkoutData)

        val checkoutParam = CheckoutParam()
        val profile = Profile()
        profile.addressId = fragmentUiModel.getProfileViewModel()?.addressId
        profile.description = ""
        profile.gatewayCode = fragmentUiModel.atcResponseModel?.atcDataModel?.userProfileModelDefaultModel?.paymentModel?.gatewayCode
        profile.status = fragmentUiModel.atcResponseModel?.atcDataModel?.userProfileModelDefaultModel?.status
        profile.profileId = fragmentUiModel.atcResponseModel?.atcDataModel?.userProfileModelDefaultModel?.id
        profile.checkoutParam = checkoutParam

        val checkoutRequestParam = CheckoutRequestParam()
        checkoutRequestParam.carts = cart
        checkoutRequestParam.profile = profile
        checkoutRequestParam.fingerprintSupport = fragmentUiModel.fingerprintPublicKey?.isNotEmpty()?.toString()
        checkoutRequestParam.fingerprintPublicKey = fragmentUiModel.fingerprintPublicKey

        val jsonTreeCheckoutRequest = Gson().toJsonTree(checkoutRequestParam)
        val jsonObjectCheckoutRequest = jsonTreeCheckoutRequest.asJsonObject
        variables.put("params", jsonObjectCheckoutRequest)
    }

    override fun execute(requestParams: RequestParams?, subscriber: Subscriber<GraphqlResponse>?) {
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.mutation_checkout_express), CheckoutExpressGqlResponse::class.java, variables, false)
        clearRequest()
        addRequest(graphqlRequest)

        super.execute(requestParams, subscriber)
    }
}