package com.tokopedia.travel.country_code.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.travel.country_code.DummyGqlQueryInterface
import com.tokopedia.travel.country_code.presentation.model.TravelCountryPhoneCode
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * created by @bayazidnasir on 19/4/2022
 */

@ExperimentalCoroutinesApi
class TravelCountryCodeByIdUseCaseTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var travelCountryCodeUseCase: TravelCountryCodeUseCase

    lateinit var useCase: TravelCountryCodeByIdUseCase

    @Before
    fun setup(){
        MockKAnnotations.init(this)
        useCase = TravelCountryCodeByIdUseCase(travelCountryCodeUseCase)
    }

    @Test
    fun `fetch travel country code by id should be success`(){
        val countryList = listOf(TravelCountryPhoneCode(countryId = "ID", countryName = "Indonesia", countryPhoneCode = 62))

        coEvery {
            travelCountryCodeUseCase.execute(any())
        } returns Success(countryList)


        runBlockingTest {
            val result = useCase.execute(DummyGqlQueryInterface(), "ID")
            assertTrue(result is Success)
            assertFalse(result is Fail)

            val data = result as Success
            assertEquals(countryList[0].countryId, data.data.countryId)
            assertEquals(countryList[0].countryName, data.data.countryName)
            assertEquals(countryList[0].countryPhoneCode, data.data.countryPhoneCode)
        }

        coVerify { travelCountryCodeUseCase.execute(any()) }
    }

    @Test
    fun `fetch travel country code by different id should be success`(){
        val countryList = listOf(TravelCountryPhoneCode(countryId = "SGP", countryName = "Indonesia", countryPhoneCode = 62))

        coEvery {
            travelCountryCodeUseCase.execute(any())
        } returns Success(countryList)


        runBlockingTest {
            val result = useCase.execute(DummyGqlQueryInterface(), "ID")
            assertTrue(result is Success)
            assertFalse(result is Fail)

            val data = result as Success
            assertEquals("", data.data.countryId)
            assertEquals("", data.data.countryName)
            assertEquals(0, data.data.countryPhoneCode)
        }

        coVerify { travelCountryCodeUseCase.execute(any()) }
    }

    @Test
    fun `fetch travel country code by id should be failed`(){
        coEvery {
            travelCountryCodeUseCase.execute(any())
        } returns Fail(Throwable(message = "failed to fetch"))

        runBlockingTest {
            val result = useCase.execute(DummyGqlQueryInterface(), "ID")
            assertTrue(result is Fail)
            assertFalse(result is Success)

            val data = result as Fail
            assertEquals("failed to fetch", data.throwable.message)
        }

        coVerify { travelCountryCodeUseCase.execute(any()) }
    }
}