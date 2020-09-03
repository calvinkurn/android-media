package com.tokopedia.entertainment.search.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.entertainment.search.viewmodel.EventLocationViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class EventLocationViewModelFactory @Inject constructor(
        private val dispatcher: CoroutineDispatcher,
        private val gqlRepository: GraphqlRepository,
        private val userSession: UserSessionInterface
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EventLocationViewModel(dispatcher, gqlRepository, userSession) as T
    }
}