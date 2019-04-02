package com.tokopedia.hotel.search.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.experimental.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class HotelSearchResultViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val userSessionInterface: UserSessionInterface,
        @Named("Main")
        val dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher){

    fun searchProperty(){
        launchCatchError(block = {

        }){
            it.printStackTrace()
        }
    }
}