package com.tokopedia.settingbank.banklist.v2.view.viewModel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class MakeAccountPrimaryViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                      private val rawQueries: Map<String, String>,
                                                      dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

}