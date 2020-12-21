package com.tokopedia.shop_settings.usecase.shopsettingsinfo

import com.tokopedia.shop.common.constant.ShopScheduleActionDef
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.UpdateShopScheduleUseCase
import com.tokopedia.usecase.RequestParams
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import rx.Observable

class ShopSettingsInfoUseCaseTest {

    @RelaxedMockK
    lateinit var updateShopScheduleUseCase: UpdateShopScheduleUseCase

    @Before
    fun before() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `when update shop schedule with provided action open closeNow false should return success`() {
        val params = RequestParams.create().apply {
            putInt(UpdateShopScheduleUseCase.ACTION, ShopScheduleActionDef.OPEN)
            putBoolean(UpdateShopScheduleUseCase.CLOSE_NOW, false)
            putString(UpdateShopScheduleUseCase.CLOSE_START, "")
            putString(UpdateShopScheduleUseCase.CLOSE_END, "")
            putString(UpdateShopScheduleUseCase.CLOSE_NOTE, "")
        }

        every {
            updateShopScheduleUseCase.createObservable(any())
        } returns Observable.just(String())

        val testSubscriber = updateShopScheduleUseCase.createObservable(params).test()
        verify {
            updateShopScheduleUseCase.createObservable(any())
        }
        testSubscriber.assertCompleted()
        testSubscriber.assertNoErrors()
    }

}