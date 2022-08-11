package com.tokopedia.travel.country_code.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.travel.country_code.DummyGqlQueryInterface
import com.tokopedia.travel.country_code.data.TravelPhoneCodeAttribute
import com.tokopedia.travel.country_code.data.TravelPhoneCodeCountry
import com.tokopedia.travel.country_code.data.TravelPhoneCodeEntity
import com.tokopedia.travel.country_code.presentation.model.TravelCountryPhoneCode
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable

/**
 * created by @bayazidnasir on 19/4/2022
 */

@ExperimentalCoroutinesApi
class TravelCountryCodeUseCaseTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var multiGql: MultiRequestGraphqlUseCase

    @MockK
    lateinit var gql: GraphqlUseCase

    private lateinit var useCase: TravelCountryCodeUseCase

    @Before
    fun setup(){
        MockKAnnotations.init(this)
        useCase = TravelCountryCodeUseCase(multiGql, gql)
    }

    @Test
    fun `get country code should be success`(){

        val expected = TravelCountryPhoneCode("ID", "Indonesia", 62)

        every { multiGql.setCacheStrategy(any()) } answers  {}
        every { multiGql.clearRequest() } answers {}
        every { multiGql.addRequest(any()) } answers {}
        coEvery {
            multiGql.executeOnBackground()
        } returns GraphqlResponse(
            mapOf(TravelPhoneCodeEntity.Response::class.java to TravelPhoneCodeEntity.Response(
                travelGetAllCountries = TravelPhoneCodeEntity(
                    countries = listOf(TravelPhoneCodeCountry(
                        id = "ID",
                        attributes = TravelPhoneCodeAttribute(
                            name = "Indonesia",
                            currency = "Rp",
                            phoneCode = 62
                        )
                    ))
                )
            )),
            mapOf(),
            false
        )

        runBlockingTest{
            val result = useCase.execute(DummyGqlQueryInterface())

            assertTrue(result is Success)
            assertFalse(result is Fail)

            val data = result as Success
            assertEquals(1, data.data.size)
            assertEquals(expected, data.data[0])
            assertEquals(expected.countryPhoneCode, data.data[0].countryPhoneCode)
            assertEquals(expected.countryName, data.data[0].countryName)
            assertEquals(expected.countryId, data.data[0].countryId)
        }

        coVerify { multiGql.executeOnBackground() }
    }

    @Test
    fun `get country code should be failed`(){

        every { multiGql.setCacheStrategy(any()) } answers  {}
        every { multiGql.clearRequest() } answers {}
        every { multiGql.addRequest(any()) } answers {}
        coEvery {
            multiGql.executeOnBackground()
        } returns GraphqlResponse(
            mapOf(),
            mapOf(TravelPhoneCodeEntity.Response::class.java to arrayListOf(GraphqlError().apply {
                    message = "This is Error"
                })),
            false
        )

        runBlockingTest{
            val result = useCase.execute(DummyGqlQueryInterface())

            assertTrue(result is Fail)
            assertFalse(result is Success)

            val data = (result as Fail).throwable as MessageErrorException
            assertEquals("This is Error", data.message)
        }

        coVerify { multiGql.executeOnBackground() }
    }

    @Test
    fun `create observable should be success`(){
        every { gql.clearRequest() } answers {}
        every { gql.addRequest(any()) } answers {}

        every { gql.createObservable(any()) } returns Observable.just(GraphqlResponse(
            mapOf(TravelPhoneCodeEntity.Response::class.java to TravelPhoneCodeEntity.Response(
                    travelGetAllCountries = TravelPhoneCodeEntity(
                        countries = listOf(TravelPhoneCodeCountry(
                            id = "ID",
                            attributes = TravelPhoneCodeAttribute(
                                name = "Indonesia",
                                currency = "Rp",
                                phoneCode = 62
                            )
                        ))
                    )
                )),
            mapOf(),
            false
        ))

        useCase.createObservable(DummyGqlQueryInterface())

        verify { gql.createObservable(any()) }
    }

    @Test
    fun `create observable should be failed`(){
        every { gql.clearRequest() } answers {}
        every { gql.addRequest(any()) } answers {}

        every { gql.createObservable(any()) } returns Observable.just(GraphqlResponse(
            mapOf(),
            mapOf(TravelPhoneCodeEntity.Response::class.java to arrayListOf(GraphqlError().apply {
                    message = "This is Error"
                })),
            false
        ))

        useCase.createObservable(DummyGqlQueryInterface())

        verify { gql.createObservable(any()) }
    }
}