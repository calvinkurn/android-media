package com.tokopedia.purchase_platform.features.one_click_checkout.order.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.checkout.CheckoutOccGqlResponse
import javax.inject.Inject

class CheckoutOccUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase<CheckoutOccGqlResponse>) {

    fun execute() {

    }
}