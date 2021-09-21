package com.tokopedia.feedcomponent.domain.usecase

import com.tokopedia.feedcomponent.domain.model.commission.AffiliatedProductByProductIDs
import com.tokopedia.feedcomponent.domain.model.statistic.FeedGetStatsPosts
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 2019-11-22
 */
class GetPostStatisticCommissionUseCase @Inject constructor(
        private val getPostStatisticUseCase: GetPostStatisticUseCase,
        private val getAffiliatedProductByIdsUseCase: GetAffiliatedProductByIdsUseCase,
        private val dispatcherProvider: CoroutineDispatchers
) : UseCase<Pair<FeedGetStatsPosts, AffiliatedProductByProductIDs>>() {

    companion object {

        private const val PARAM_PRODUCT_IDS = "productIDs"
        private const val PARAM_ACTIVITY_IDS = "activityIDs"

        fun getParam(activityIDs: List<String>, productIDs: List<String>): RequestParams {
            return RequestParams.create().apply {
                putObject(PARAM_ACTIVITY_IDS, activityIDs)
                putObject(PARAM_PRODUCT_IDS, productIDs)
            }
        }
    }

    private val params: MutableMap<String, Any> = mutableMapOf()

    override suspend fun executeOnBackground(): Pair<FeedGetStatsPosts, AffiliatedProductByProductIDs> = withContext(dispatcherProvider.io) {
        val postStatistic = async {
            getPostStatisticUseCase.run {
                setParams(
                        GetPostStatisticUseCase.getParam(
                                params[PARAM_ACTIVITY_IDS] as List<String>
                        )
                )
                return@async executeOnBackground()
            }
        }

        val commission = async {
            getAffiliatedProductByIdsUseCase.run {
                setParams(
                        GetAffiliatedProductByIdsUseCase.getParam(
                                params[PARAM_PRODUCT_IDS] as List<String>
                        )
                )
                return@async executeOnBackground()
            }
        }

        return@withContext Pair(postStatistic.await(), commission.await())
    }

    fun setParams(params: RequestParams) {
        this.params.run {
            clear()
            putAll(params.parameters)
        }
    }
}