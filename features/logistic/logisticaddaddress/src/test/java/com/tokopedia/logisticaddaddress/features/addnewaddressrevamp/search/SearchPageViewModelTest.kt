package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.repository.KeroRepository
import com.tokopedia.logisticCommon.data.response.AutoCompleteResponse
import com.tokopedia.logisticCommon.domain.model.Place
import com.tokopedia.logisticaddaddress.domain.mapper.AutoCompleteMapper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchPageViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repo: KeroRepository = mockk(relaxed = true)
    private val autoCompleterMapper = AutoCompleteMapper()

    private lateinit var searchPageViewModel: SearchPageViewModel

    private val autoCompleteListObserver: Observer<Result<Place>> = mockk(relaxed = true)

    private val defaultThrowable = Throwable("test error")

    @Before
    fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        searchPageViewModel = SearchPageViewModel(repo, autoCompleterMapper)
        searchPageViewModel.autoCompleteList.observeForever(autoCompleteListObserver)
    }

    @Test
    fun `Get Auto Complete List Success`() {
        coEvery { repo.getAutoComplete(any(), any()) } returns AutoCompleteResponse()
        searchPageViewModel.loadAutoComplete("Jakarta")
        verify { autoCompleteListObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `verify set latlong and get auto complete list is fail`() {
        // Inject
        val latitude = 1.0
        val longitude = 1.0

        // Given
        coEvery { repo.getAutoComplete(any(), any()) } throws defaultThrowable

        // When
        searchPageViewModel.setLatLong(
            latitude = latitude,
            longitude = longitude
        )
        searchPageViewModel.loadAutoComplete("Jakarta")

        // Then
        Assert.assertEquals(searchPageViewModel.currentLat.toString(), latitude.toString())
        Assert.assertEquals(searchPageViewModel.currentLong.toString(), longitude.toString())
        verify { autoCompleteListObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `verify set gms availability flag is correct`() {
        val gmsAvailable = true
        searchPageViewModel.isGmsAvailable = gmsAvailable

        Assert.assertEquals(searchPageViewModel.isGmsAvailable, gmsAvailable)
    }

    @Test
    fun `verify set all data from arguments is correctly`() {
        // Inject
        val isPositiveFlow = true
        val isFromPinpoint = true
        val isPolygon = true
        val isEdit = true
        val source = "source"
        val addressData = spyk<SaveAddressDataModel>()

        // When
        searchPageViewModel.setDataFromArguments(
            isPositiveFlow,
            isFromPinpoint,
            isPolygon,
            isEdit,
            source,
            addressData
        )

        // Then
        with(searchPageViewModel) {
            Assert.assertTrue(this.isPositiveFlow)
            Assert.assertTrue(this.isFromPinpoint)
            Assert.assertTrue(this.isPolygon)
            Assert.assertTrue(this.isEdit)
            Assert.assertEquals(this.source, source)
            Assert.assertEquals(this.saveAddressDataModel, addressData)
        }
    }

    @Test
    fun `verify set data from arguments when address data null is correctly`() {
        // Inject
        val isPositiveFlow = false
        val isFromPinpoint = false
        val isPolygon = false
        val isEdit = false
        val source = ""

        // When
        searchPageViewModel.setDataFromArguments(
            isPositiveFlow,
            isFromPinpoint,
            isPolygon,
            isEdit,
            source,
            null
        )

        // Then
        with(searchPageViewModel) {
            Assert.assertFalse(this.isPositiveFlow)
            Assert.assertFalse(this.isFromPinpoint)
            Assert.assertFalse(this.isPolygon)
            Assert.assertFalse(this.isEdit)
            Assert.assertEquals(this.source, source)
            Assert.assertNotNull(this.saveAddressDataModel)
        }
    }
}
