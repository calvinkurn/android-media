package com.tokopedia.notifications.inApp.ketupat

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.notifications.domain.AnimationCrackCouponUseCase
import com.tokopedia.notifications.domain.AnimationScratchPopupUseCase
import com.tokopedia.notifications.domain.data.GamiScratchCardCrack
import com.tokopedia.notifications.domain.data.GamiScratchCardPreEvaluate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class AnimationPopupGqlGetData: CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    private val graphRepository: GraphqlRepository by lazy {
        GraphqlInteractor.getInstance().graphqlRepository
    }


    private fun getCrackCouponUseCase(): AnimationCrackCouponUseCase {
        return AnimationCrackCouponUseCase(graphRepository)
    }

    private fun getScratchPopupUseCase(): AnimationScratchPopupUseCase {
        return AnimationScratchPopupUseCase(graphRepository)
    }

    fun getAnimationCrackCouponData(
        onSuccess: (GamiScratchCardCrack) -> Unit,
        onError: (Throwable) -> Unit,
        onErrorState: (GamiScratchCardCrack) -> Unit,
        slug: String?
    ) {
        try {
            getCrackCouponUseCase().getAnimationCrackCouponData({
                onSuccess(it)
            }, {
                onError(it)
            }, {onErrorState(it)}, slug)

    } catch (_: Exception) {
        }
    }

    fun getAnimationScratchPopupData(
        onSuccess: (GamiScratchCardPreEvaluate) -> Unit,
        onError: (Throwable) -> Unit,
        pageSource: String
    ) {
        try {
            getScratchPopupUseCase().getAnimationPopupData({
                onSuccess(it)
            }, {
                onError(it)
            }, pageSource)

        } catch (_: Exception) {
        }
    }
}
