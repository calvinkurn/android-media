package com.tokopedia.entertainment.home.viewmodel

import android.content.Context
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.domain.GraphqlRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Author errysuprayogi on 06,February,2020
 */

class HomeEntertainmentViewModel @Inject constructor(
        private val context: Context,
        private val dispatcher: CoroutineDispatcher,
        private val repository: GraphqlRepository) : BaseViewModel(dispatcher) {


    fun getHomeData(){

    }
}