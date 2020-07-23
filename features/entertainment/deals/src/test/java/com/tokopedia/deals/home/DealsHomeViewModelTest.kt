package com.tokopedia.deals.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.deals.common.domain.GetNearestLocationUseCase
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.deals.common.ui.dataview.DealsBrandsDataView
import com.tokopedia.deals.common.utils.DealsTestDispatcherProvider
import com.tokopedia.deals.home.data.DealsEventHome
import com.tokopedia.deals.home.domain.GetEventHomeBrandPopularUseCase
import com.tokopedia.deals.home.domain.GetEventHomeLayoutUseCase
import com.tokopedia.deals.home.ui.dataview.BannersDataView
import com.tokopedia.deals.home.ui.dataview.VoucherPlacePopularDataView
import com.tokopedia.deals.home.ui.viewmodel.DealsHomeViewModel
import com.tokopedia.deals.home.util.DealsHomeMapper
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.deals.location_picker.model.response.LocationData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DealsHomeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val dispatcher = DealsTestDispatcherProvider()

    private val getHomeLayoutUseCase: GetEventHomeLayoutUseCase = mockk()
    private val getBrandPopularUseCase: GetEventHomeBrandPopularUseCase = mockk()
    private val getNearestLocationUseCase: GetNearestLocationUseCase = mockk()
    private val mapper: DealsHomeMapper = mockk()

    private lateinit var viewModel: DealsHomeViewModel

    private val throwable = Throwable("Error fetch")

    @Before
    fun setup() {
        viewModel = DealsHomeViewModel(
            dispatcher,
            mapper,
            getHomeLayoutUseCase,
            getBrandPopularUseCase,
            getNearestLocationUseCase
        )
    }

    @Test
    fun getLayout_fetchHomeLayoutFailed_shouldShowFail() {
        // given
        coEvery { getHomeLayoutUseCase.useParams(any()) } returns mockk()
        coEvery { getHomeLayoutUseCase.executeOnBackground() } coAnswers { throw  throwable }
        coEvery { getBrandPopularUseCase.useParams(any()) } returns mockk()
        coEvery { getBrandPopularUseCase.executeOnBackground() } returns SearchData()
        coEvery { getNearestLocationUseCase.useParams(any()) } returns mockk()
        coEvery { getNearestLocationUseCase.executeOnBackground() } returns LocationData()
        coEvery { mapper.mapLayoutToBaseItemViewModel(any(), any(), any()) } returns listOf()

        // when
        viewModel.getLayout(Location())

        // then
        assert(viewModel.observableEventHomeLayout.value is Fail)
        assert((viewModel.observableEventHomeLayout.value as Fail).throwable.message == "Error fetch")
    }

    @Test
    fun getLayout_fetchBrandPopularFailed_brandPopularShouldBeEmpty() {
        // given
        coEvery { getHomeLayoutUseCase.useParams(any()) } returns mockk()
        coEvery { getHomeLayoutUseCase.executeOnBackground() } returns DealsEventHome.Response()
        coEvery { getBrandPopularUseCase.useParams(any()) } returns mockk()
        coEvery { getBrandPopularUseCase.executeOnBackground() } coAnswers { throw throwable }
        coEvery { getNearestLocationUseCase.useParams(any()) } returns mockk()
        coEvery { getNearestLocationUseCase.executeOnBackground() } returns LocationData()
        coEvery { mapper.mapLayoutToBaseItemViewModel(any(), any(), any()) } returns listOf(BannersDataView())

        // when
        viewModel.getLayout(Location())

        // then
        var containsBrand = false
        for (item in (viewModel.observableEventHomeLayout.value as Success).data) {
            if (item is DealsBrandsDataView) {
                containsBrand = true
                break
            }
        }
        assert(!containsBrand)
    }

    @Test
    fun getLayout_fetchNearestLocationFailed_locationShouldBeEmpty() {
        // given
        coEvery { getHomeLayoutUseCase.useParams(any()) } returns mockk()
        coEvery { getHomeLayoutUseCase.executeOnBackground() } returns DealsEventHome.Response()
        coEvery { getBrandPopularUseCase.useParams(any()) } returns mockk()
        coEvery { getBrandPopularUseCase.executeOnBackground() } returns SearchData()
        coEvery { getNearestLocationUseCase.useParams(any()) } returns mockk()
        coEvery { getNearestLocationUseCase.executeOnBackground() } coAnswers {throw throwable}
        coEvery { mapper.mapLayoutToBaseItemViewModel(any(), any(), any()) } returns listOf(BannersDataView())

        // when
        viewModel.getLayout(Location())

        // then
        var containsLocation = false
        for (item in (viewModel.observableEventHomeLayout.value as Success).data) {
            if (item is VoucherPlacePopularDataView) {
                containsLocation = true
                break
            }
        }
        assert(!containsLocation)
    }

    @Test
    fun getLayout_allFetchSuccess_homeLayoutShouldNotEmpty() {
        // given
        coEvery { getHomeLayoutUseCase.useParams(any()) } returns mockk()
        coEvery { getHomeLayoutUseCase.executeOnBackground() } returns DealsEventHome.Response()
        coEvery { getBrandPopularUseCase.useParams(any()) } returns mockk()
        coEvery { getBrandPopularUseCase.executeOnBackground() } returns SearchData()
        coEvery { getNearestLocationUseCase.useParams(any()) } returns mockk()
        coEvery { getNearestLocationUseCase.executeOnBackground() } returns LocationData()
        coEvery { mapper.mapLayoutToBaseItemViewModel(any(), any(), any()) } returns listOf(BannersDataView())

        // when
        viewModel.getLayout(Location())

        // then
        assert((viewModel.observableEventHomeLayout.value as Success).data.isNotEmpty())
    }
}