package com.tokopedia.moneyin.usecase

import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.moneyin.usecase.MoneyInPickupScheduleUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.tokopedia.moneyin.model.MoneyInScheduleOptionResponse.ResponseData
import com.tokopedia.moneyin.repository.MoneyInRepository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MoneyInPickupScheduleUseCaseTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val tradeInRepository: MoneyInRepository = mockk(relaxed = true)
    private val resources: Resources = mockk()

    private var moneyInPickupScheduleUseCase = spyk(MoneyInPickupScheduleUseCase(tradeInRepository))

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**************************** getPickupScheduleOption() *******************************************/


    @Test
    fun getPickupScheduleOption() {
        val responseData: ResponseData = mockk()
        runBlocking {
            mockkStatic(GraphqlHelper::class)
            every { GraphqlHelper.loadRawString(any(), any()) } returns ""
            coEvery { tradeInRepository.getGQLData(any(), ResponseData::class.java, any())} returns responseData

            moneyInPickupScheduleUseCase.getPickupScheduleOption(resources)

            coVerify { tradeInRepository.getGQLData(any(), ResponseData::class.java, any()) }
        }
    }

    /**************************** getPickupScheduleOption() *******************************************/

}