package com.tokopedia.flight.passenger.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.flight.dummy.DUMMY_NATIONALITY_SUCCESS
import com.tokopedia.flight.dummy.DUMMY_PASSENGER_DATA
import com.tokopedia.flight.shouldBe
import com.tokopedia.travel.country_code.domain.TravelCountryCodeByIdUseCase
import com.tokopedia.travel.passenger.data.entity.TravelUpsertContactModel
import com.tokopedia.travel.passenger.domain.GetContactListUseCase
import com.tokopedia.travel.passenger.domain.UpsertContactListUseCase
import com.tokopedia.usecase.coroutines.Fail
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by furqan on 24/06/2020
 */
class FlightPassengerViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val getContactListUseCase: GetContactListUseCase = mockk()
    private val getPhoneCodeByIdUseCase: TravelCountryCodeByIdUseCase = mockk()

    @RelaxedMockK
    private lateinit var upsertContactListUseCase: UpsertContactListUseCase

    private lateinit var viewModel: FlightPassengerViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = FlightPassengerViewModel(getContactListUseCase,
                upsertContactListUseCase,
                getPhoneCodeByIdUseCase,
                CoroutineTestDispatchersProvider)
    }

    @Test
    fun getContactList_successFetchContactList_contactListResultShouldBeDummyData() {
        // given
        coEvery { getContactListUseCase.execute(any(), any(), any()) } returns DUMMY_PASSENGER_DATA

        // when
        viewModel.getContactList("", "")

        // then
        viewModel.contactListResult.value?.size shouldBe DUMMY_PASSENGER_DATA.size

        for ((index, item) in viewModel.contactListResult.value!!.withIndex()) {
            val dummyData = DUMMY_PASSENGER_DATA[index]

            item.shortTitle shouldBe dummyData.shortTitle
            item.lastName shouldBe dummyData.lastName
            item.firstName shouldBe dummyData.firstName
            item.birthDate shouldBe dummyData.birthDate
            item.email shouldBe dummyData.email
            item.gender shouldBe dummyData.gender
            item.idList.size shouldBe dummyData.idList.size
            item.nationality shouldBe dummyData.nationality
            item.phoneCountryCode shouldBe dummyData.phoneCountryCode
            item.phoneNumber shouldBe dummyData.phoneNumber
            item.title shouldBe dummyData.title
            item.titleID shouldBe dummyData.titleID
            item.type shouldBe dummyData.type
            item.uuid shouldBe dummyData.uuid

            if (dummyData.fullName.isEmpty()) {
                item.fullName shouldBe "${dummyData.firstName} ${dummyData.lastName}"
            } else {
                item.fullName shouldBe dummyData.fullName
            }
        }
    }

    @Test
    fun getContactListWithoutFilterType_successFetchContactList_contactListResultShouldBeDummyData() {
        // given
        coEvery { getContactListUseCase.execute(any(), any(), any()) } returns DUMMY_PASSENGER_DATA

        // when
        viewModel.getContactList("")

        // then
        viewModel.contactListResult.value?.size shouldBe DUMMY_PASSENGER_DATA.size

        for ((index, item) in viewModel.contactListResult.value!!.withIndex()) {
            val dummyData = DUMMY_PASSENGER_DATA[index]

            item.shortTitle shouldBe dummyData.shortTitle
            item.lastName shouldBe dummyData.lastName
            item.firstName shouldBe dummyData.firstName
            item.birthDate shouldBe dummyData.birthDate
            item.email shouldBe dummyData.email
            item.gender shouldBe dummyData.gender
            item.idList.size shouldBe dummyData.idList.size
            item.nationality shouldBe dummyData.nationality
            item.phoneCountryCode shouldBe dummyData.phoneCountryCode
            item.phoneNumber shouldBe dummyData.phoneNumber
            item.title shouldBe dummyData.title
            item.titleID shouldBe dummyData.titleID
            item.type shouldBe dummyData.type
            item.uuid shouldBe dummyData.uuid

            if (dummyData.fullName.isEmpty()) {
                item.fullName shouldBe "${dummyData.firstName} ${dummyData.lastName}"
            } else {
                item.fullName shouldBe dummyData.fullName
            }
        }
    }

    @Test
    fun updateContactList_shouldValidateUseCaseCalled() {
        // given
        val query = "DUMMY QUERY"
        val updatedContact = TravelUpsertContactModel.Contact(
                "dummyTitle",
                "Muhammad Furqan",
                "Muhammad",
                "Furqan",
                "1995-11-11",
                "ID",
                62,
                "898989898989",
                "aa@aa.com",
                arrayListOf()
        )

        // when
        viewModel.updateContactList(query, updatedContact)

        // then
        coVerify {
            upsertContactListUseCase.execute(query, any())
        }
    }

    @Test
    fun getNationalityById_failedToFetch_nationalityDataShouldBeNull() {
        // given
        coEvery { getPhoneCodeByIdUseCase.execute(any(), any()) } returns Fail(Throwable("Failed"))

        // when
        viewModel.getNationalityById("", "")

        // then
        viewModel.nationalityData.value shouldBe null
    }

    @Test
    fun getNationalityById_successToFetch_nationalityDataShouldBeDummy() {
        // given
        coEvery { getPhoneCodeByIdUseCase.execute(any(), any()) } returns DUMMY_NATIONALITY_SUCCESS

        // when
        viewModel.getNationalityById("", "")

        // then
        viewModel.nationalityData.value?.countryId shouldBe DUMMY_NATIONALITY_SUCCESS.data.countryId
        viewModel.nationalityData.value?.countryName shouldBe DUMMY_NATIONALITY_SUCCESS.data.countryName
        viewModel.nationalityData.value?.countryPhoneCode shouldBe DUMMY_NATIONALITY_SUCCESS.data.countryPhoneCode
    }

    @Test
    fun getPassportIssuerCountryById_failedToFetch_passportDataShouldBeNull() {
        // given
        coEvery { getPhoneCodeByIdUseCase.execute(any(), any()) } returns Fail(Throwable("Failed"))

        // when
        viewModel.getPassportIssuerCountryById("", "")

        // then
        viewModel.passportIssuerCountryData.value shouldBe null
    }

    @Test
    fun getPassportIssuerCountryById_successToFetch_passportDataShouldBeDummy() {
        // given
        coEvery { getPhoneCodeByIdUseCase.execute(any(), any()) } returns DUMMY_NATIONALITY_SUCCESS

        // when
        viewModel.getPassportIssuerCountryById("", "")

        // then
        viewModel.passportIssuerCountryData.value?.countryId shouldBe DUMMY_NATIONALITY_SUCCESS.data.countryId
        viewModel.passportIssuerCountryData.value?.countryName shouldBe DUMMY_NATIONALITY_SUCCESS.data.countryName
        viewModel.passportIssuerCountryData.value?.countryPhoneCode shouldBe DUMMY_NATIONALITY_SUCCESS.data.countryPhoneCode
    }

    @Test
    fun setPassportIssuerCountry() {
        // given

        // when
        viewModel.passportIssuerCountryData = MutableLiveData()

        // then
        viewModel.passportIssuerCountryData.value shouldBe null
    }

    @Test
    fun setNationality() {
        // given

        // when
        viewModel.nationalityData = MutableLiveData()

        // then
        viewModel.nationalityData.value shouldBe null
    }
}