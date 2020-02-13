package com.tokopedia.salam.umrah.checkout.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.salam.umrah.UmrahDispatchersProviderTest
import com.tokopedia.travel.passenger.data.entity.TravelContactListModel
import com.tokopedia.travel.passenger.domain.GetContactListUseCase
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UmrahCheckoutPilgrimsViewModelTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var umrahGetContactListUseCase: GetContactListUseCase

    private val dispatcher = UmrahDispatchersProviderTest()
    private lateinit var umrahCheckoutPilgrimsViewModel: UmrahCheckoutPilgrimsViewModel

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        umrahCheckoutPilgrimsViewModel = UmrahCheckoutPilgrimsViewModel(umrahGetContactListUseCase, dispatcher)
    }

    @Test
    fun `should return a response Contact List`(){
        //given
        coEvery {
            umrahGetContactListUseCase.execute(any(),any(),any())
        } returns TravelContactListModel.Response().response.contacts

        //when
        umrahCheckoutPilgrimsViewModel.getContactList("","")
        val actual = umrahCheckoutPilgrimsViewModel.contactListResult.value
        assert(actual is List<TravelContactListModel.Contact>)
    }

    @Test
    fun `should return a failure Contact List`(){
        //given
        coEvery {
            umrahGetContactListUseCase.execute(any(),any(),any())
        } returns listOf()

        umrahCheckoutPilgrimsViewModel.getContactList("","")

        val actual = umrahCheckoutPilgrimsViewModel.contactListResult.value
        assert(actual == listOf<TravelContactListModel.Contact>())
    }




}