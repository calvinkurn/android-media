package com.tokopedia.topads.view.model

import android.content.Context
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.response.ResponseGroupValidateName
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

/**
 * Author errysuprayogi on 06,November,2019
 */
class CreateGroupAdsViewModel @Inject constructor(
        private val context: Context,
        @Named("Main")
        private val dispatcher: CoroutineDispatcher,
        private val userSession: UserSessionInterface,
        private val gqlRepository: GraphqlRepository): BaseViewModel(dispatcher) {

    companion object{
        val SHOP_ID = "shopID"
        val GROUP_NAME = "groupName"
    }

    fun validateGroup(groupName: String, onSuccess: ((ResponseGroupValidateName.TopAdsGroupValidateName.Data) -> Unit),
                      onError: ((Throwable) -> Unit)) {
        launchCatchError(
                block = {
                    val data = withContext(Dispatchers.IO) {
                        val request = GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.query_ads_create_validate_group_name),
                                ResponseGroupValidateName::class.java, mapOf(SHOP_ID to userSession.shopId.toIntOrZero(), GROUP_NAME to groupName))
                        val cacheStrategy = GraphqlCacheStrategy
                                .Builder(CacheType.CLOUD_THEN_CACHE).build()
                        gqlRepository.getReseponse(listOf(request), cacheStrategy)
                    }
                    data.getSuccessData<ResponseGroupValidateName>().let {
                        if(it.topAdsGroupValidateName.errors.isEmpty()){
                            onSuccess(it.topAdsGroupValidateName.data)
                        } else {
                            onError(Exception(it.topAdsGroupValidateName.errors.get(0).detail))
                        }
                    }
                },
                onError = {
                    onError(it)
                }
        )
    }
}