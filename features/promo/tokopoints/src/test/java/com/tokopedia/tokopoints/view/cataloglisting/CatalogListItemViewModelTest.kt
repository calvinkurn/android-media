package com.tokopedia.tokopoints.view.cataloglisting

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
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
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.reflect.KClass

@ExperimentalCoroutinesApi
class CatalogListItemViewModelTest {

    lateinit var viewModel: CatalogListItemViewModel
    val repository = mockk<CatalogListingRepository>()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel = CatalogListItemViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get catalog list item success`() {
        val observer = mockk<Observer<Resources<CatalogEntity>>>() {
            every { onChanged(any()) } just runs
        }
        val listItem = mockk<CatalogEntity> ()
        coEvery {
            repository.getListOfCatalog(1, 1, 0)
        } returns mockk {
            every { catalog } returns listItem
        }
        viewModel.listCatalogItem.observeForever(observer)
        viewModel.getCataloglistItem(1, 1, 0)

        verify(ordering = Ordering.ORDERED) {
            observer.onChanged(ofType(Loading::class as KClass<Loading<CatalogEntity>>))
            observer.onChanged(ofType(Success::class as KClass<Success<CatalogEntity>>))
        }

        assert(listItem == (viewModel.listCatalogItem.value as Success).data)
    }

    @Test
    fun `get catalog list item error`() {
        val categoryId = 1152
        val subCategoryId = 5223
        val pointsRange = 2
        val exception = NullPointerException()

        coEvery { repository.getListOfCatalog(categoryId, subCategoryId, pointsRange) } throws exception

        viewModel.getCataloglistItem(categoryId, subCategoryId, pointsRange)

        viewModel.listCatalogItem
            .verifyValueEquals(ErrorMessage<NullPointerException>(data = "java.lang.NullPointerException"))
    }

    @Test
    fun `redeem Coupon success`() {
        val observer = mockk<Observer<String>>()
        val promocode = "promocode"
        val cta = "cta"
        coEvery { repository.redeemCoupon(promocode) } returns mockk()
        viewModel.onRedeemCouponLiveData.observeForever(observer)
        viewModel.redeemCoupon(promocode, cta)

        verify(exactly = 1) { observer.onChanged(any()) }

        assert(viewModel.onRedeemCouponLiveData.value == cta)
    }

    @Test
    fun `redeem Coupon error`() {
        val observer = mockk<Observer<String>>()
        val promocode = "promocode"
        val cta = "cta"
        viewModel.onRedeemCouponLiveData.observeForever(observer)
        viewModel.redeemCoupon(promocode, cta)

        verify(ordering = Ordering.ORDERED) { observer.onChanged(any()) }

        assert(viewModel.onRedeemCouponLiveData.value == cta)
    }

    @Test
    fun `start Save Coupon success`() {
        val observer = mockk<Observer<Resources<ConfirmRedeemDialog>>>()
        val item = mockk<CatalogsValueEntity>() {
            every { id } returns 1
        }
        val dummyCouponData = ArrayList<CouponDetailEntity>()
        val couponDetailEntity = CouponDetailEntity(code = "200", cta = "cta", title = "title", description = "description")
        dummyCouponData.add(couponDetailEntity)
        val data = RedeemCouponEntity(coupons = dummyCouponData, redeemMessage = "claim success")
        coEvery { repository.startSaveCoupon(1) } returns mockk {
            every { hachikoRedeem } returns data
        }
        viewModel.startSaveCouponLiveData.observeForever(observer)
        viewModel.startSaveCoupon(item)

        verify(exactly = 1) { observer.onChanged(ofType(Success::class as KClass<Success<ConfirmRedeemDialog>>)) }
        val result = (viewModel.startSaveCouponLiveData.value as Success).data
        assert(result.code == data.coupons?.get(0)?.code)
        assert(result.cta == data.coupons?.get(0)?.cta)
        assert(result.title == data.coupons?.get(0)?.title)
        assert(result.description == data.coupons?.get(0)?.description)
        assert(result.redeemMessage == data.redeemMessage)
    }

    @Test
    fun `start Save Coupon error`() {
        val observer = mockk<Observer<Resources<ConfirmRedeemDialog>>>()
        val item = mockk<CatalogsValueEntity>() {
            every { id } returns 1
        }

        coEvery { repository.startSaveCoupon(1) } throws mockk<CatalogGqlError> {
            every { messageErrorException } returns mockk {
                every { message } returns "message"
            }
            every { developerMessage } returns "title|message|300"
        }
        viewModel.startSaveCouponLiveData.observeForever(observer)
        viewModel.startSaveCoupon(item)

        verify(exactly = 1) { observer.onChanged(ofType(ValidationError::class as KClass<ValidationError<ConfirmRedeemDialog, ValidateMessageDialog>>)) }

        val result = (viewModel.startSaveCouponLiveData.value as ValidationError<*, *>).data as ValidateMessageDialog
        assert(result.desc == "message")
        assert(result.messageCode == 300)
    }

    @Test
    fun `start Save Coupon error with wrong message`() {
        val observer = mockk<Observer<Resources<ConfirmRedeemDialog>>>()
        val item = mockk<CatalogsValueEntity>() {
            every { id } returns 1
        }

        coEvery { repository.startSaveCoupon(1) } throws mockk<MessageErrorException> {
            every { message } returns "title|message|3oo"
        }
        viewModel.startSaveCouponLiveData.observeForever(observer)
        viewModel.startSaveCoupon(item)

        verify(exactly = 0) { observer.onChanged(ofType(ValidationError::class as KClass<ValidationError<ConfirmRedeemDialog, ValidateMessageDialog>>)) }
    }

    @Test
    fun `fetch Latest Status success case`() {
        val catalogsIds = listOf(1)

        val catalogStatusResponse = CatalogStatusOuter(
            catalogStatus = CatalogStatusBase(
                catalogs = listOf(
                    CatalogStatusItem(
                        catalogID = 1,
                        upperTextDesc = mutableListOf("Description")
                    )
                )
            )
        )

        coEvery { repository.fetchLatestStatus(catalogsIds) } returns catalogStatusResponse

        viewModel.fetchLatestStatus(catalogsIds)

        val expectedStatusLiveData = catalogStatusResponse.catalogStatus.catalogs

        viewModel.latestStatusLiveData
            .verifyValueEquals(expectedStatusLiveData)
    }

    @Test
    fun `fetch Latest Status error case`() {
        val observer = mockk<Observer<List<CatalogStatusItem>>>()
        coEvery { repository.fetchLatestStatus(any()) } returns mockk {
            every { catalogStatus } throws Exception()
        }
        viewModel.latestStatusLiveData.observeForever(observer)
        viewModel.fetchLatestStatus(listOf())

        verify(exactly = 0) { observer.onChanged(any()) }
    }

    @Test
    fun `when set pointRange variable should update pointRange data`() {
        viewModel.pointRange = 2

        val expected = 2
        val actual = viewModel.pointRange

        assertEquals(expected, actual)
    }
}
