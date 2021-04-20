package com.tokopedia.deals.home

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.deals.DealsJsonMapper
import com.tokopedia.deals.common.domain.GetNearestLocationUseCase
import com.tokopedia.deals.common.model.response.Brand
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.deals.common.ui.dataview.DealsBrandsDataView
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.deals.home.data.DealsEventHome
import com.tokopedia.deals.home.domain.GetEventHomeBrandPopularUseCase
import com.tokopedia.deals.home.domain.GetEventHomeLayoutUseCase
import com.tokopedia.deals.home.ui.dataview.BannersDataView
import com.tokopedia.deals.home.ui.dataview.VoucherPlacePopularDataView
import com.tokopedia.deals.home.ui.viewmodel.DealsHomeViewModel
import com.tokopedia.deals.home.util.DealsHomeMapper
import com.tokopedia.deals.location_picker.DealsLocationConstants
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.deals.location_picker.model.response.LocationData
import com.tokopedia.deals.location_picker.model.response.LocationType
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DealsHomeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val dispatcher = CoroutineTestDispatchersProvider

    private val getHomeLayoutUseCase: GetEventHomeLayoutUseCase = mockk()
    private val getBrandPopularUseCase: GetEventHomeBrandPopularUseCase = mockk()
    private val getNearestLocationUseCase: GetNearestLocationUseCase = mockk()
    private val context: Context = mockk(relaxed = true)

    private lateinit var viewModel: DealsHomeViewModel
    private lateinit var mapper: DealsHomeMapper
    private lateinit var mockHomeResponse: DealsEventHome.Response
    private lateinit var mockSearchData: SearchData
    private lateinit var mockLocationData: LocationData
    private lateinit var mockBannersData: BannersDataView
    private lateinit var mockThrowable: Throwable

    @Before
    fun setup() {
        mapper = DealsHomeMapper(context)
        viewModel = DealsHomeViewModel(
                dispatcher,
                mapper,
                getHomeLayoutUseCase,
                getBrandPopularUseCase,
                getNearestLocationUseCase
        )

        mockHomeResponse = Gson().fromJson(
                DealsJsonMapper.getJson("event_home.json"),
                DealsEventHome.Response::class.java
        )
        mockSearchData = Gson().fromJson(
                DealsJsonMapper.getJson("event_search.json"),
                SearchData::class.java
        )
        mockLocationData = Gson().fromJson(
                DealsJsonMapper.getJson("event_location_search.json"),
                LocationData::class.java
        )
        mockBannersData = BannersDataView("see-all", "see-all-url", listOf())
        mockThrowable = Throwable("Error fetch")
    }

    @Test
    fun getLayout_fetchHomeLayoutFailed_shouldShowFail() {
        // given
        coEvery { getHomeLayoutUseCase.useParams(any()) } returns mockk()
        coEvery { getHomeLayoutUseCase.executeOnBackground() } coAnswers { throw  mockThrowable }
        coEvery { getBrandPopularUseCase.useParams(any()) } returns mockk()
        coEvery { getBrandPopularUseCase.executeOnBackground() } returns mockSearchData
        coEvery { getNearestLocationUseCase.useParams(any()) } returns mockk()
        coEvery { getNearestLocationUseCase.executeOnBackground() } returns mockLocationData

        // when
        viewModel.getLayout(Location())

        // then
        assert(viewModel.observableEventHomeLayout.value is Fail)
        assertEquals((viewModel.observableEventHomeLayout.value as Fail).throwable, mockThrowable)
    }

    @Test
    fun getLayout_fetchBrandPopularFailed_brandPopularShouldBeEmpty() {
        val homeLayouts = mockHomeResponse.response.layout
        val brandPopular = emptyList<Brand>()
        val locations = mockLocationData.eventLocationSearch.locations
        val mockMapping = mapper.mapLayoutToBaseItemViewModel(homeLayouts, brandPopular, locations)

        // given
        coEvery { getHomeLayoutUseCase.useParams(any()) } returns mockk()
        coEvery { getHomeLayoutUseCase.executeOnBackground() } returns mockHomeResponse
        coEvery { getBrandPopularUseCase.useParams(any()) } returns mockk()
        coEvery { getBrandPopularUseCase.executeOnBackground() } coAnswers { throw mockThrowable }
        coEvery { getNearestLocationUseCase.useParams(any()) } returns mockk()
        coEvery { getNearestLocationUseCase.executeOnBackground() } returns mockLocationData

        // when
        viewModel.getLayout(Location())

        // then
        assertEquals((viewModel.observableEventHomeLayout.value as Success).data, mockMapping)
    }

    @Test
    fun getLayout_fetchNearestLocationFailed_locationShouldBeEmpty() {
        val homeLayouts = mockHomeResponse.response.layout
        val brandPopular = mockSearchData.eventSearch.brands
        val locations = emptyList<Location>()
        val mockMapping = mapper.mapLayoutToBaseItemViewModel(homeLayouts, brandPopular, locations)

        // given
        coEvery { getHomeLayoutUseCase.useParams(any()) } returns mockk()
        coEvery { getHomeLayoutUseCase.executeOnBackground() } returns mockHomeResponse
        coEvery { getBrandPopularUseCase.useParams(any()) } returns mockk()
        coEvery { getBrandPopularUseCase.executeOnBackground() } returns mockSearchData
        coEvery { getNearestLocationUseCase.useParams(any()) } returns mockk()
        coEvery { getNearestLocationUseCase.executeOnBackground() } coAnswers { throw mockThrowable }

        // when
        viewModel.getLayout(Location())

        // then
        assertEquals((viewModel.observableEventHomeLayout.value as Success).data, mockMapping)
    }

    @Test
    fun getLayout_allFetchSuccess_homeLayoutShouldNotEmpty() {
        val homeLayouts = mockHomeResponse.response.layout
        val brandPopular = mockSearchData.eventSearch.brands
        val locations = mockLocationData.eventLocationSearch.locations
        val mockMapping = mapper.mapLayoutToBaseItemViewModel(homeLayouts, brandPopular, locations)

        // given
        coEvery { getHomeLayoutUseCase.useParams(any()) } returns mockk()
        coEvery { getHomeLayoutUseCase.executeOnBackground() } returns mockHomeResponse
        coEvery { getBrandPopularUseCase.useParams(any()) } returns mockk()
        coEvery { getBrandPopularUseCase.executeOnBackground() } returns mockSearchData
        coEvery { getNearestLocationUseCase.useParams(any()) } returns mockk()
        coEvery { getNearestLocationUseCase.executeOnBackground() } returns mockLocationData

        // when
        viewModel.getLayout(Location())

        // then
        assertEquals((viewModel.observableEventHomeLayout.value as Success).data, mockMapping)
    }

    @Test
    fun getLayout_allFetchSuccess_homeLocationNotLandmark() {
        val homeLayouts = mockHomeResponse.response.layout
        val brandPopular = mockSearchData.eventSearch.brands
        val locations = listOf<Location>()
        val mockMapping = mapper.mapLayoutToBaseItemViewModel(homeLayouts, brandPopular, locations)

        // given
        coEvery { getHomeLayoutUseCase.useParams(any()) } returns mockk()
        coEvery { getHomeLayoutUseCase.executeOnBackground() } returns mockHomeResponse
        coEvery { getBrandPopularUseCase.useParams(any()) } returns mockk()
        coEvery { getBrandPopularUseCase.executeOnBackground() } returns mockSearchData
        coEvery { getNearestLocationUseCase.useParams(any()) } returns mockk()
        coEvery { getNearestLocationUseCase.executeOnBackground() } returns mockLocationData

        // when
        viewModel.getLayout(Location(locType = LocationType(name = DealsLocationConstants.LANDMARK)))

        // then
        assertEquals((viewModel.observableEventHomeLayout.value as Success).data, mockMapping)
    }
}