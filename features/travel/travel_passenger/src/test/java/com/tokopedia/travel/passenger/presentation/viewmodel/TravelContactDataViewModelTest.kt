package com.tokopedia.travel.passenger.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.travel.passenger.data.entity.TravelContactListModel
import com.tokopedia.travel.passenger.data.entity.TravelUpsertContactModel
import com.tokopedia.travel.passenger.domain.GetContactListUseCase
import com.tokopedia.travel.passenger.domain.UpsertContactListUseCase
import com.tokopedia.travel.passenger.util.TravelPassengerGqlMutation
import com.tokopedia.travel.passenger.util.TravelPassengerGqlQuery
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TravelContactDataViewModelTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var travelContactDataViewModel: TravelContactDataViewModel

    @MockK
    lateinit var getContactListUseCase: GetContactListUseCase

    @MockK
    lateinit var upsertContactListUseCase: UpsertContactListUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        travelContactDataViewModel = TravelContactDataViewModel(getContactListUseCase,upsertContactListUseCase, Dispatchers.Unconfined)
    }

    @Test
    fun getContactList_isCalled(){
        //given
        val query = TravelPassengerGqlQuery.CONTACT_LIST
        val product = "hotel"
        val expectedData = listOf<TravelContactListModel.Contact>(
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
    fun updateContactList_isCalled(){
        //given
        val query = TravelPassengerGqlMutation.UPSERT_CONTACT
        val contactData = TravelUpsertContactModel.Contact(fullName = "Asti Dhiya", email = "asti@tokopedia.com", phoneNumber = "81255216660",
                phoneCountryCode = 62)

        //when
        travelContactDataViewModel.updateContactList(query, contactData)

        //then
    }
}