package com.tokopedia.tokopoints.view.cataloglisting

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.tokopoints.view.model.*
import com.tokopedia.tokopoints.view.util.*
import com.tokopedia.unit.test.ext.verifyValueEquals
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.reflect.KClass

@ExperimentalCoroutinesApi
class CatalogListingViewModelTest {

    lateinit var viewModel: CatalogListingViewModel
    val repository = mockk<CatalogListingRepository>()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel = CatalogListingViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getHomePageData when data geting null`() {
        val bannerObserver = mockk<Observer<Resources<CatalogBannerBase>>>() {
            every { onChanged(any()) } just Runs
        }
        val filterObserver = mockk<Observer<Resources<CatalogFilterBase>>> {
            every { onChanged(any()) } just Runs
        }
        coEvery { repository.getHomePageData(null, null, false) } returns mockk {
            every { getData<CatalogBannerOuter>(CatalogBannerOuter::class.java) } returns null
            every { getData<CatalogFilterOuter>(CatalogFilterOuter::class.java) } returns null
        }
        viewModel.bannerLiveDate.observeForever(bannerObserver)
        viewModel.filterLiveData.observeForever(filterObserver)
        viewModel.getHomePageData(null, null, false)

        verify(ordering = Ordering.ORDERED) {
            filterObserver.onChanged(ofType(Loading::class as KClass<Loading<CatalogFilterBase>>))
            bannerObserver.onChanged(ofType(ErrorMessage::class as KClass<ErrorMessage<CatalogBannerBase>>))
            filterObserver.onChanged(ofType(ErrorMessage::class as KClass<ErrorMessage<CatalogFilterBase>>))
        }

    }

    @Test
    fun `getHomePageData when data geting not null`() {

        val bannerObserver = mockk<Observer<Resources<CatalogBannerBase>>>() {
            every { onChanged(any()) } just Runs
        }
        val filterObserver = mockk<Observer<Resources<CatalogFilterBase>>> {
            every { onChanged(any()) } just Runs
        }
        val bannerBaseData = mockk<CatalogBannerBase> {
            every { banners } returns mockk()
        }

        val filterBaseData = mockk<CatalogFilterBase>()
        coEvery { repository.getHomePageData(null, null, false) } returns mockk {
            every { getData<CatalogBannerOuter>(CatalogBannerOuter::class.java) } returns mockk {
                every { bannerData } returns bannerBaseData
            }
            every { getData<CatalogFilterOuter>(CatalogFilterOuter::class.java) } returns mockk {
                every { filter } returns filterBaseData
            }
        }
        viewModel.bannerLiveDate.observeForever(bannerObserver)
        viewModel.filterLiveData.observeForever(filterObserver)
        viewModel.getHomePageData(null, null, false)

        verify(ordering = Ordering.ORDERED) {
            filterObserver.onChanged(ofType(Loading::class as KClass<Loading<CatalogFilterBase>>))
            bannerObserver.onChanged(ofType(Success::class as KClass<Success<CatalogBannerBase>>))
            filterObserver.onChanged(ofType(Success::class as KClass<Success<CatalogFilterBase>>))

        }

        assert(bannerBaseData == (viewModel.bannerLiveDate.value as Success).data)
        assert(filterBaseData == (viewModel.filterLiveData.value as Success).data)

    }

    @Test
    fun `getHomePageData when data geting Exception`() {

        val bannerObserver = mockk<Observer<Resources<CatalogBannerBase>>>() {
            every { onChanged(any()) } just Runs
        }
        val filterObserver = mockk<Observer<Resources<CatalogFilterBase>>> {
            every { onChanged(any()) } just Runs
        }

        viewModel.bannerLiveDate.observeForever(bannerObserver)
        viewModel.filterLiveData.observeForever(filterObserver)
        viewModel.getHomePageData(null, null, false)

        verify(ordering = Ordering.ORDERED) {
            filterObserver.onChanged(ofType(Loading::class as KClass<Loading<CatalogFilterBase>>))
            filterObserver.onChanged(ofType(ErrorMessage::class as KClass<ErrorMessage<CatalogFilterBase>>))
        }

    }

    @Test
    fun `given status code 200 when getPointData should update pointLiveData with success value`(){
        val statusCode = 200
        val tokoPointDetailEntity = TokoPointDetailEntity(
            tokoPoints = TokoPointEntity(resultStatus = ResultStatusEntity(code = statusCode))
        )

        coEvery{ repository.getPointData() } returns tokoPointDetailEntity

        viewModel.getPointData()

        viewModel.pointLiveData
            .verifyValueEquals(Success(tokoPointDetailEntity.tokoPoints.status))
    }

    @Test
    fun `given status code NOT 200 when getPointData should NOT update pointLiveData`(){
        val statusCode = 404
        val tokoPointDetailEntity = TokoPointDetailEntity(
            tokoPoints = TokoPointEntity(resultStatus = ResultStatusEntity(code = statusCode))
        )

        coEvery{ repository.getPointData() } returns tokoPointDetailEntity

        viewModel.getPointData()

        viewModel.pointLiveData
            .verifyValueEquals(null)
    }

    @Test
    fun `given getPointData throws exception when getPointData should catch error`() {
        coEvery{ repository.getPointData() } throws NullPointerException()

        viewModel.getPointData()

        viewModel.pointLiveData
            .verifyValueEquals(null)
    }

    @Test
    fun `getPointData error case`(){
        coEvery{ repository.getPointData() } returns null

        viewModel.getPointData()

        assert(viewModel.pointLiveData.value == ErrorMessage<TokoPointStatusEntity>(""))
    }


    @Test
    fun setPointRangeId() {
        val id  = 2
        viewModel.pointRangeId =  id

        assert(viewModel.pointRangeId == id)
    }

    @Test
    fun setCurrentCategoryId() {
        val id  = 2
        viewModel.currentCategoryId =  id

        assert(viewModel.currentCategoryId == id)
    }

    @Test
    fun setCurrentSubCategoryId() {
        val id  = 2
        viewModel.currentSubCategoryId =  id

        assert(viewModel.currentSubCategoryId == id)
    }
}
