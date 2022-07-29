package com.tokopedia.travel.passenger.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.travel.passenger.data.entity.TravelContactListModel
import com.tokopedia.travel.passenger.data.entity.TravelUpsertContactModel
import com.tokopedia.travel.passenger.domain.GetContactListUseCase
import com.tokopedia.travel.passenger.domain.UpsertContactListUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TravelContactDataViewModelTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val coroutineDispatcher = CoroutineTestDispatchers

    private lateinit var travelContactDataViewModel: TravelContactDataViewModel

    @MockK
    lateinit var getContactListUseCase: GetContactListUseCase

    @MockK
    lateinit var upsertContactListUseCase: UpsertContactListUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        travelContactDataViewModel = TravelContactDataViewModel(getContactListUseCase,upsertContactListUseCase, coroutineDispatcher.main)
    }

    @Test
    fun getContactList_isCalled(){
        //given
        val query = TravelGqlInterface()
        val product = "hotel"
        val expectedData = listOf(
                TravelContactListModel.Contact(uuid = "uuid_f9f3ef8ccd3a855533d304e90a819d5e", phoneNumber = "81255216660", birthDate = "26 Agustus 1998", nationality = "Indonesia", firstName = "Asti"))
        coEvery { getContactListUseCase.execute(query, product) } returns expectedData

        //when
        travelContactDataViewModel.getContactList(query, product)

        //then
        val data = (travelContactDataViewModel.contactListResult.value as List<TravelContactListModel.Contact>)
        Assert.assertEquals(data.size, expectedData.size)
        Assert.assertEquals(data, expectedData)
        Assert.assertEquals(data[0].phoneNumber, expectedData[0].phoneNumber)
        Assert.assertEquals(data[0].birthDate, expectedData[0].birthDate)
        Assert.assertEquals(data[0].nationality, expectedData[0].nationality)
    }

    @Test
    fun getContactList_isCalled_withNotEmptyFullName(){
        //given
        val product = "hotel"
        val expectedData = listOf(TravelContactListModel.Contact(
            uuid = "uuid_f9f3ef8ccd3a855533d304e90a819d5e",
            phoneNumber = "81255216660",
            birthDate = "26 Agustus 1998",
            nationality = "Indonesia",
            firstName = "Asti",
            fullName = "FullName Test"
        ))

        coEvery { getContactListUseCase.execute(any(), any(), any()) } returns expectedData

        //when
        travelContactDataViewModel.getContactList(TravelGqlInterface(), product)

        //then
        val data = (travelContactDataViewModel.contactListResult.value as List<TravelContactListModel.Contact>)

        Assert.assertEquals(data.size, expectedData.size)
        Assert.assertEquals(data, expectedData)
        Assert.assertEquals(data[0].phoneNumber, expectedData[0].phoneNumber)
        Assert.assertEquals(data[0].birthDate, expectedData[0].birthDate)
        Assert.assertEquals(data[0].nationality, expectedData[0].nationality)

        coVerify { getContactListUseCase.execute(any(), any(), any()) }
    }

    @Test
    fun updateContactList_isCalled(){
        //given
        val contactData = TravelUpsertContactModel.Contact(
            fullName = "Asti Dhiya",
            email = "asti@tokopedia.com",
            phoneNumber = "81255216660",
            phoneCountryCode = 62)
        val contactResponse = TravelUpsertContactModel.Response(
            travelUpsertContact = TravelUpsertContactModel.Response.SuccessResponse(true)
        )

        coEvery {
            upsertContactListUseCase.execute(any(), any())
        } returns Success(contactResponse)

        //when
        travelContactDataViewModel.updateContactList(TravelGqlInterface(), contactData)

        //then

        coVerify { upsertContactListUseCase.execute(any(), any()) }
    }
}