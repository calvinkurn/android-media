package com.tokopedia.tokopoints.view.cataloglisting

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopoints.view.model.*
import com.tokopedia.tokopoints.view.util.*
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import java.lang.Error
import kotlin.reflect.KClass

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
        val observer = mockk<Observer<Resources<CatalogEntity>>>(){
            every { onChanged(any())  } just runs
        }
        val listItem = mockk<CatalogEntity> ()
        coEvery {
            repository.getListOfCatalog(1, 1, 0) } returns mockk {
            every { catalog } returns listItem
        }
        viewModel.listCatalogItem.observeForever(observer)
        viewModel.getCataloglistItem(1,1,0)

        verify(ordering = Ordering.ORDERED) {
            observer.onChanged(ofType(Loading::class as KClass<Loading<CatalogEntity>>))
            observer.onChanged(ofType(Success::class as KClass<Success<CatalogEntity>>))
        }

        assert(listItem == (viewModel.listCatalogItem.value as Success).data)
    }

    @Test
    fun `get catalog list item error`() {
        val observer = mockk<Observer<Resources<CatalogEntity>>>(){
            every { onChanged(any())  } just runs
        }
        coEvery {
            repository.getListOfCatalog(1, 1, 0) } returns mockk {
            every { catalog } returns null
        }
        viewModel.listCatalogItem.observeForever(observer)
        viewModel.getCataloglistItem(1,1,0)

        verify(ordering = Ordering.ORDERED) {
            observer.onChanged(ofType(Loading::class as KClass<Loading<CatalogEntity>>))
        }
    }


    @Test
    fun `start Validate Coupon success case`() {
        val observer = mockk<Observer<ValidateMessageDialog>>()
        val item = mockk<CatalogsValueEntity> {
            every { id } returns 1
        }

        val couponEntity = mockk<ValidateCouponEntity> {
            every { messageSuccess } returns "success message"
            every { messageTitle } returns "success title"
        }
        coEvery { repository.startValidateCoupon(1) } returns mockk {
            every { validateCoupon } returns couponEntity
        }
        viewModel.startValidateCouponLiveData.observeForever(observer)
        viewModel.startValidateCoupon(item)

        verify(exactly = 1) {
            observer.onChanged(any())
        }

        assert(couponEntity.messageSuccess == viewModel.startValidateCouponLiveData.value?.desc)
        assert(couponEntity.messageTitle == viewModel.startValidateCouponLiveData.value?.title)
    }

    @Test
    fun `start Validate Coupon error case`() {
        val observer = mockk<Observer<ValidateMessageDialog>>()
        val item = mockk<CatalogsValueEntity> {
            every { id } returns 1
        }

        coEvery { repository.startValidateCoupon(1) } throws mockk<MessageErrorException> {
            every { message } returns "success title|success message|300"
        }
        viewModel.startValidateCouponLiveData.observeForever(observer)
        viewModel.startValidateCoupon(item)

        verify(exactly = 1) {
            observer.onChanged(any())
        }

        assert("success message" == viewModel.startValidateCouponLiveData.value?.desc)
        assert("success title" == viewModel.startValidateCouponLiveData.value?.title)
    }

    @Test
    fun `start Validate Coupon error case with wrong message`() {
        val observer = mockk<Observer<ValidateMessageDialog>>()
        val item = mockk<CatalogsValueEntity> {
            every { id } returns 1
        }

        coEvery { repository.startValidateCoupon(1) } throws mockk<MessageErrorException> {
            every { message } returns "success title|success message"
        }
        viewModel.startValidateCouponLiveData.observeForever(observer)
        viewModel.startValidateCoupon(item)

        verify(exactly = 0) {
            observer.onChanged(any())
        }
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
        val data = mockk<RedeemCouponEntity> {
            every { coupons?.get(0) } returns mockk {
                every { code } returns "200"
                every { cta } returns "cta"
                every { title } returns "title"
                every { description } returns "description"

            }
            every { redeemMessage } returns "claim success"
        }
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

        coEvery { repository.startSaveCoupon(1) } throws mockk<MessageErrorException> {
            every { message } returns "title|message|300"
        }
        viewModel.startSaveCouponLiveData.observeForever(observer)
        viewModel.startSaveCoupon(item)

        verify(exactly = 1) { observer.onChanged(ofType(ValidationError::class as KClass<ValidationError<ConfirmRedeemDialog, ValidateMessageDialog>>)) }

        val result = (viewModel.startSaveCouponLiveData.value as ValidationError<*, *>).data as ValidateMessageDialog
        assert(result.desc == "message")
        assert(result.item == item)
        assert(result.messageCode == 300)
        assert(result.title == "title")
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
        val observer = mockk<Observer<List<CatalogStatusItem>>>()
        val data = mockk<List<CatalogStatusItem>>()
        coEvery { repository.fetchLatestStatus(any()) } returns mockk {
            every { catalogStatus } returns mockk {
                every { catalogs } returns data
            }
        }
        viewModel.latestStatusLiveData.observeForever(observer)
        viewModel.fetchLatestStatus(listOf())

        verify(exactly = 1) { observer.onChanged(any()) }

        assert(viewModel.latestStatusLiveData.value == data)
    }

    @Test
    fun `fetch Latest Status error case`() {
        val observer = mockk<Observer<List<CatalogStatusItem>>>()
        coEvery { repository.fetchLatestStatus(any()) } returns mockk {
            every { catalogStatus } returns null
        }
        viewModel.latestStatusLiveData.observeForever(observer)
        viewModel.fetchLatestStatus(listOf())

        verify(exactly = 0) { observer.onChanged(any()) }

    }
}