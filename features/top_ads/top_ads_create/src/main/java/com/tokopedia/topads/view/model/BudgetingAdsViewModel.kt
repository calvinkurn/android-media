package com.tokopedia.topads.view.model

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Author errysuprayogi on 06,November,2019
 */
class BudgetingAdsViewModel(
        private val dispatcher: CoroutineDispatcher,
        private val repository: GraphqlRepository): BaseViewModel(dispatcher) {
}