package com.tokopedia.smartbills.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class SmartBillsAddTelcoViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val dispatcher: CoroutineDispatchers
        ): BaseViewModel(dispatcher.io){


}