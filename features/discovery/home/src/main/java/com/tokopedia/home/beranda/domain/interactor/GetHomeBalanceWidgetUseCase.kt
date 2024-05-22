package com.tokopedia.home.beranda.domain.interactor

import android.os.Bundle
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.data.model.GetHomeBalanceWidgetData
import com.tokopedia.home.beranda.domain.model.banner.HomeBannerData
import com.tokopedia.home.beranda.helper.DeviceScreenHelper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by dhaba
 */
class GetHomeBalanceWidgetUseCase @Inject constructor(
    private val graphqlUseCase: GraphqlUseCase<GetHomeBalanceWidgetData>,
    private val deviceScreenHelper: DeviceScreenHelper,
) : UseCase<GetHomeBalanceWidgetData>(), HomeRepository<GetHomeBalanceWidgetData> {
    private val params = RequestParams.create()

    companion object {
        private const val EXPERIMENT = "experiment"
        private const val VARIANT = "variant"
        const val PARAM = "param"
    }

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(GetHomeBalanceWidgetData::class.java)
    }

    override suspend fun executeOnBackground(): GetHomeBalanceWidgetData {
        graphqlUseCase.clearCache()
        params.putString(EXPERIMENT, "")
        params.putString(VARIANT, "")
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()
    }

    override suspend fun getRemoteData(bundle: Bundle): GetHomeBalanceWidgetData {
        bundle.getString(PARAM)?.let {
            params.putString(PARAM, it)
        }
        return executeOnBackground()
    }

    override suspend fun getCachedData(bundle: Bundle): GetHomeBalanceWidgetData {
        return GetHomeBalanceWidgetData()
    }
}
