package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.Preference
import javax.inject.Inject

class GetPreferenceEditUseCase @Inject constructor(val graphql: GraphqlUseCase<List<Preference>>) {

}