package com.tokopedia.entertainment.home.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Author errysuprayogi on 06,February,2020
 */

class HomeEventViewModelFactory @Inject constructor(
        private val context: Context,
        private val dispatcher: CoroutineDispatcher,
        private val repository: GraphqlRepository,
        private val userSession: UserSessionInterface) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeEventViewModel(context, dispatcher, repository, userSession) as T
    }

}