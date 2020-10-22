package com.tokopedia.entertainment.home.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Author errysuprayogi on 06,February,2020
 */

class HomeEventViewModelFactory @Inject constructor(
        private val dispatcher: CoroutineDispatcher,
        private val gqlRepository: GraphqlRepository,
        private val restRepository: RestRepository,
        private val userSession: UserSessionInterface) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeEventViewModel(dispatcher, gqlRepository, restRepository, userSession) as T
    }

}