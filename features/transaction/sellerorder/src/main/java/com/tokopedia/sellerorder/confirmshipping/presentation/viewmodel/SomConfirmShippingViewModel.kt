package com.tokopedia.sellerorder.confirmshipping.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-11-15.
 */
class SomConfirmShippingViewModel @Inject constructor(dispatcher: CoroutineDispatcher,
                                                      private val graphqlRepository: GraphqlRepository) : BaseViewModel(dispatcher) {
}