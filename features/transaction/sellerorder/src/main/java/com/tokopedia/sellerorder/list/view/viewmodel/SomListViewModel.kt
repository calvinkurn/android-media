package com.tokopedia.sellerorder.list.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-08-27.
 */
class SomListViewModel @Inject constructor(private val coroutineThread: CoroutineThread,
                                          private val graphqlUseCase: GraphqlUseCase<GetExploreData>,
                                           dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {
}