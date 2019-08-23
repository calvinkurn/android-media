package com.tokopedia.navigation_common.category

import android.content.Context
import android.content.Intent
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.navigation_common.category.model.CategoryConfigModel
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.navigation_common.R
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.remoteconfig.RemoteConfigKey.APP_CATEGORY_BROWSE_ENABLE_AB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

object CategoryNavigationConfig : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
    lateinit var remoteConfig: RemoteConfig

    private const val STATE_UNINITIALIZED = 1
    private const val STATE_NEW_CATEGORY_ENABLED = 2
    private const val STATE_NEW_CATEGORY_DISABLED = 3

    var state: Int = STATE_UNINITIALIZED


    fun updateCategoryConfig(context: Context, openNewBelanja: (context: Context) -> Intent,
                             openOldBelanja: (context: Context) -> Intent): Intent {

        remoteConfig = FirebaseRemoteConfigImpl(context)

        if (state == STATE_NEW_CATEGORY_ENABLED) {
            return openNewBelanja(context)
        } else if (state == STATE_NEW_CATEGORY_DISABLED) {
            return openOldBelanja(context)
        }

        if (!isCategoryV1Enable()) {
            return openOldBelanja(context)

        } else if (!isCategoryABTestEnable()) {
            return openNewBelanja(context)
        }
        launchCatchError(block = {
            val response = withContext(Dispatchers.IO) {
                val graphqlRepository = GraphqlInteractor.getInstance().graphqlRepository
                val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                        R.raw.category_config), CategoryConfigModel::class.java, false)
                val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                        .setExpiryTime(GraphqlConstant.ExpiryTimes.DAY.`val`() * 1).setSessionIncluded(true).build()
                graphqlRepository.getReseponse(listOf(graphqlRequest), cacheStrategy)
            }
            state = if (response.getSuccessData<CategoryConfigModel>().homeFlag!!.isRevampBelanja) {
                STATE_NEW_CATEGORY_ENABLED
            } else {
                STATE_NEW_CATEGORY_DISABLED
            }
        }, onError =
        {
            state = STATE_NEW_CATEGORY_ENABLED
            it.printStackTrace()
        })

        return openOldBelanja(context)

    }

    private fun isCategoryV1Enable(): Boolean {
        return remoteConfig.getBoolean(RemoteConfigKey.APP_CATEGORY_BROWSE_V1, true)
    }

    private fun isCategoryABTestEnable(): Boolean {
        return remoteConfig.getBoolean(APP_CATEGORY_BROWSE_ENABLE_AB, true)
    }
}

