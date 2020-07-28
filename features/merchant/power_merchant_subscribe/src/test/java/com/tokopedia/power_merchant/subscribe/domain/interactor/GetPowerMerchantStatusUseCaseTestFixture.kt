package com.tokopedia.power_merchant.subscribe.domain.interactor

import com.tokopedia.gm.common.data.source.cloud.model.GoldGetPmOsStatus
import com.tokopedia.gm.common.data.source.cloud.model.ShopScoreResult
import com.tokopedia.gm.common.domain.interactor.GetPowerMerchantStatusUseCase
import com.tokopedia.gm.common.domain.interactor.GetShopScoreUseCase
import com.tokopedia.gm.common.domain.interactor.GetShopStatusUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user_identification_common.domain.pojo.KycUserProjectInfoPojo
import com.tokopedia.user_identification_common.domain.usecase.GetUserProjectInfoUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import rx.Observable
import rx.Scheduler
import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.schedulers.Schedulers

open class GetPowerMerchantStatusUseCaseTestFixture {

    private lateinit var getShopStatusUseCase: GetShopStatusUseCase
    private lateinit var getUserProjectInfoUseCase: GetUserProjectInfoUseCase
    private lateinit var getShopScoreUseCase: GetShopScoreUseCase

    protected lateinit var useCase: GetPowerMerchantStatusUseCase

    @Before
    fun setUp() {
        getShopStatusUseCase = mockk(relaxed = true)
        getUserProjectInfoUseCase = mockk(relaxed = true)
        getShopScoreUseCase = mockk(relaxed = true)

        useCase = GetPowerMerchantStatusUseCase(
            getShopStatusUseCase,
            getUserProjectInfoUseCase,
            getShopScoreUseCase
        )

        resetSchedulerHook()
        registerSchedulerHook()
    }

    @After
    fun tearDown() {
        resetSchedulerHook()
    }

    protected fun onGetShopStatus_thenReturn(goldGetPmOsStatus: GoldGetPmOsStatus) {
        every {
            getShopStatusUseCase.createObservable(any())
        } returns Observable.just(goldGetPmOsStatus)
    }

    protected fun onGetShopStatus_thenReturn(error: Throwable) {
        every {
            getShopStatusUseCase.createObservable(any())
        } returns Observable.error(error)
    }

    protected fun onGetShopScore_thenReturn(shopScoreResult: ShopScoreResult) {
        every {
            getShopScoreUseCase.createObservable(any())
        } returns Observable.just(shopScoreResult)
    }

    protected fun onGetUserProjectInfo_thenReturn(kycUserProjectInfo: KycUserProjectInfoPojo) {
        every {
            getUserProjectInfoUseCase.execute(any())
        } returns Observable.just(kycUserProjectInfo)
    }

    protected fun verifyAllUseCaseCalled() {
        verify {
            getShopScoreUseCase.createObservable(any())
            getUserProjectInfoUseCase.execute(any())
            getShopScoreUseCase.createObservable(any())
        }
    }

    private fun registerSchedulerHook() {
        RxAndroidPlugins.getInstance().registerSchedulersHook(object : RxAndroidSchedulersHook() {
            override fun getMainThreadScheduler(): Scheduler {
                return Schedulers.immediate()
            }
        })
    }

    private fun resetSchedulerHook() {
        @Suppress("UnstableApiUsage")
        RxAndroidPlugins.getInstance().reset()
    }
}