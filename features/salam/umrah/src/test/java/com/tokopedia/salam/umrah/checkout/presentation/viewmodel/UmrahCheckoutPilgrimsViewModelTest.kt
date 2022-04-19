package com.tokopedia.salam.umrah.checkout.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.travel.passenger.data.entity.TravelContactListModel
import com.tokopedia.travel.passenger.domain.GetContactListUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
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

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var umrahCheckoutPilgrimsViewModel: UmrahCheckoutPilgrimsViewModel

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        umrahCheckoutPilgrimsViewModel = UmrahCheckoutPilgrimsViewModel(umrahGetContactListUseCase, dispatcher)
    }

    @Test
    fun `responseContactList_SuccessGetContact_ShowActualResult`(){
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
    fun `responseContactListTypeDefault_SuccessGetContact_ShowActualResult`(){
        //given
        coEvery {
            umrahGetContactListUseCase.execute(any(),any(),any())
        } returns TravelContactListModel.Response().response.contacts

        //when
        umrahCheckoutPilgrimsViewModel.getContactList("")
        val actual = umrahCheckoutPilgrimsViewModel.contactListResult.value
        assert(actual is List<TravelContactListModel.Contact>)
    }

    @Test
    fun `responseContactList_FailureGetContact_ShowFailureResult`(){
        //given
        coEvery {
            umrahGetContactListUseCase.execute(any(),any(),any())
        } returns listOf()

        umrahCheckoutPilgrimsViewModel.getContactList("","")

        val actual = umrahCheckoutPilgrimsViewModel.contactListResult.value
        assert(actual == listOf<TravelContactListModel.Contact>())
    }
}