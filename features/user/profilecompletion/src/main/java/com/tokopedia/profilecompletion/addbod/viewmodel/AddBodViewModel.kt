package com.tokopedia.profilecompletion.addbod.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.profilecompletion.addbod.data.AddBodData
import com.tokopedia.profilecompletion.addbod.data.UserProfileCompletionUpdateBodData
import com.tokopedia.profilecompletion.data.ProfileCompletionQueriesConstant
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-07-16.
 * ade.hadian@tokopedia.com
 */

class AddBodViewModel @Inject constructor(
        private val userProfileInfoUseCase: GraphqlUseCase<UserProfileCompletionUpdateBodData>,
        private val rawQueries: Map<String, String>,
        private val dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher){

    val editBodUserProfileResponse = MutableLiveData<Result<AddBodData>>()

    fun editBodUserProfile(){
        val rawQuery = rawQueries[ProfileCompletionQueriesConstant.QUERY_PROFILE_COMPLETION]
        if(!rawQuery.isNullOrEmpty()){
            val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(
                    CacheType.CACHE_FIRST)
            graphqlCacheStrategy.setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 5)
            userProfileInfoUseCase.run {
                //                setCacheStrategy(graphqlCacheStrategy.build())
                setGraphqlQuery(rawQuery)
                execute(
                        {
                            editBodUserProfileResponse.value = Success(it.addBodData)
                        },{
                            editBodUserProfileResponse.value = Fail(it)
                        }
                )
            }
        }
    }
}