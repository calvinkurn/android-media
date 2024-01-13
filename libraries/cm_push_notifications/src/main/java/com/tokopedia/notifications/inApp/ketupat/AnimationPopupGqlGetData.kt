package com.tokopedia.notifications.inApp.ketupat

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.notifications.domain.AnimationCrackCouponUseCase
import com.tokopedia.notifications.domain.AnimationScratchPopupUseCase
import com.tokopedia.notifications.domain.data.GamiScratchCardCrack
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
        onError: (Throwable) -> Unit
    ) {
        try {
            getCrackCouponUseCase().getAnimationCrackCouponData({
                onSuccess(it)
            }, {
                onError(it)
            })

    } catch (_: Exception) {
        }
    }

    fun getAnimationScratchPopupData() {
        try {
            getScratchPopupUseCase().getAnimationPopupData({}, {})

        } catch (_: Exception) {
        }
    }
}
