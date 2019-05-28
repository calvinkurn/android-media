package com.tokopedia.topupbills.telco.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import kotlinx.coroutines.experimental.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 23/05/19.
 */
class DigitalTelcoCommonDataViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                          val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {


}