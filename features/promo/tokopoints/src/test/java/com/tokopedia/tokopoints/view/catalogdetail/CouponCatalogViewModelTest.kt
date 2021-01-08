package com.tokopedia.tokopoints.view.catalogdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopoints.view.cataloglisting.ConfirmRedeemDialog
import com.tokopedia.tokopoints.view.cataloglisting.ValidateMessageDialog
import com.tokopedia.tokopoints.view.model.*
import com.tokopedia.tokopoints.view.util.*
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception
import kotlin.reflect.KClass

class CouponCatalogViewModelTest {


    lateinit var viewModel: CouponCatalogViewModel
    val repository = mockk<CouponCatalogRepository>()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel = CouponCatalogViewModel(repository, "tp_send_gift_failed_title", "tp_send_gift_faild_message")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
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
            every { redeemMessage } returns "success"
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
        assert(result.redeemMessage ==  data.redeemMessage)
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
    fun `get Catalog Detail catalog Detail Success and point query fail`() {
        val catalogObserver = mockk<Observer<Resources<CatalogsValueEntity>>>() {
            every { onChanged(any()) } just Runs
        }
        val code = "uniqueCatalogCode"
        val catalogData = mockk<CatalogsValueEntity>()
        coEvery { repository.getcatalogDetail(code) } returns mockk{
            every { getData<CatalogDetailOuter>(CatalogDetailOuter::class.java) } returns  mockk{
                every {  detail } returns catalogData
            }
            every { getData<TokoPointDetailEntity>(TokoPointDetailEntity::class.java) } returns null
        }

        viewModel.catalogDetailLiveData.observeForever(catalogObserver)
        viewModel.getCatalogDetail(code)

        verify(ordering = Ordering.ORDERED) {
            catalogObserver.onChanged(ofType(Loading::class as KClass<Loading<CatalogsValueEntity>>))
            catalogObserver.onChanged(ofType(Success::class as KClass<Success<CatalogsValueEntity>>))
        }
        val result = viewModel.catalogDetailLiveData.value as Success
        assert(result.data == catalogData)
    }

    @Test
    fun `get Catalog Detail catalog Detail suucess and point query success`() {
        val catalogObserver = mockk<Observer<Resources<CatalogsValueEntity>>>{
            every { onChanged(any()) } just Runs
        }
        val codeData = "uniqueCatalogCode"
        val rewardString = "rewardStr"
        val catalogData = mockk<CatalogsValueEntity>()
        coEvery { repository.getcatalogDetail(codeData) } returns mockk{
            every { getData<CatalogDetailOuter>(CatalogDetailOuter::class.java) } returns  mockk{
                every {  detail } returns catalogData
            }
            every { getData<TokoPointDetailEntity>(TokoPointDetailEntity::class.java) } returns mockk{
                every { tokoPoints } returns mockk{
                    every {  resultStatus } returns mockk{
                        every { code  } returns CommonConstant.CouponRedemptionCode.SUCCESS
                    }
                    every { status } returns mockk{
                        every { points } returns mockk{
                            every { rewardStr } returns rewardString
                        }
                    }
                }
            }
        }

        viewModel.catalogDetailLiveData.observeForever(catalogObserver)
        viewModel.getCatalogDetail(codeData)

        verify(ordering = Ordering.ORDERED) {
            catalogObserver.onChanged(ofType(Loading::class as KClass<Loading<CatalogsValueEntity>>))
            catalogObserver.onChanged(ofType(Success::class as KClass<Success<CatalogsValueEntity>>))
        }
        val result = viewModel.catalogDetailLiveData.value as Success
        assert(result.data == catalogData)
    }

    @Test
    fun `get Catalog Detail error`() {
        val catalogObserver = mockk<Observer<Resources<CatalogsValueEntity>>>{
            every { onChanged(any()) } just Runs
        }
        val codeData = "uniqueCatalogCode"
        val errorMessage = "rewardStr"
        coEvery { repository.getcatalogDetail(codeData) } throws mockk<Exception>{
            every { this@mockk.localizedMessage } returns errorMessage
        }

        viewModel.catalogDetailLiveData.observeForever(catalogObserver)
        viewModel.getCatalogDetail(codeData)

        verify(ordering = Ordering.ORDERED) {
            catalogObserver.onChanged(ofType(Loading::class as KClass<Loading<CatalogsValueEntity>>))
            catalogObserver.onChanged(ofType(ErrorMessage::class as KClass<ErrorMessage<CatalogsValueEntity>>))
        }
        val result = viewModel.catalogDetailLiveData.value as ErrorMessage
        assert(result.data == errorMessage)
    }

    @Test
    fun `fetch Latest Status success case`() {
        val observer = mockk<Observer<CatalogStatusItem>>()
        val data = mockk<CatalogStatusItem>()
        coEvery { repository.fetchLatestStatus(any()) } returns mockk {
            every { catalogStatus } returns mockk {
                every { catalogs[0] } returns data
            }
        }
        viewModel.latestStatusLiveData.observeForever(observer)
        viewModel.fetchLatestStatus(listOf())

        verify(exactly = 1) { observer.onChanged(any()) }

        assert(viewModel.latestStatusLiveData.value == data)
    }

    @Test
    fun `fetch Latest Status error case`() {
        val observer = mockk<Observer<CatalogStatusItem>>()
        coEvery { repository.fetchLatestStatus(any()) } returns mockk {
            every { catalogStatus } returns null
        }
        viewModel.latestStatusLiveData.observeForever(observer)
        viewModel.fetchLatestStatus(listOf())

        verify(exactly = 0) { observer.onChanged(any()) }

    }

    @Test
    fun `start Send Gift success`() {
        val observer = mockk<Observer<Resources<SendGiftPage>>>()
        val id = 1
        val title = "title"
        val pointStr = "pointerString"
        val banner = "banner"
        coEvery { repository.startSendGift(id) } returns mockk {
            every { preValidateRedeem } returns mockk {
                every { isValid } returns 1
            }
        }
        viewModel.sendGiftPageLiveData.observeForever(observer)
        viewModel.startSendGift(id, title, pointStr, banner)

        verify(exactly = 1) { observer.onChanged(ofType(Success::class as KClass<Success<SendGiftPage>>)) }
        val result = viewModel.sendGiftPageLiveData.value as Success
        assert(result.data.banner == banner)
        assert(result.data.id == id)
        assert(result.data.pointStr == pointStr)
        assert(result.data.title == title)

    }

    @Test
    fun `start Send Gift error`() {
        val observer = mockk<Observer<Resources<SendGiftPage>>>()
        val id = 1
        val title = "title"
        val pointStr = "pointerString"
        val banner = "banner"
        coEvery { repository.startSendGift(id) } returns mockk {
            every { preValidateRedeem } returns mockk {
                every { isValid } returns 2
            }
        }
        viewModel.sendGiftPageLiveData.observeForever(observer)
        viewModel.startSendGift(id, title, pointStr, banner)

        verify(exactly = 0) { observer.onChanged(any()) }

    }

    @Test
    fun `start Send Gift error with error message exception`() {
        val observer = mockk<Observer<Resources<SendGiftPage>>>()
        val id = 1
        val title = "title"
        val pointStr = "pointerString"
        val banner = "banner"
        coEvery { repository.startSendGift(id) } throws mockk<MessageErrorException>{
            every { message } returns "title|message"
        }
        viewModel.sendGiftPageLiveData.observeForever(observer)
        viewModel.startSendGift(id, title, pointStr, banner)

        verify(exactly = 1) { observer.onChanged(ofType(ValidationError::class as KClass<ValidationError<SendGiftPage,PreValidateError>>)) }

        val result = (viewModel.sendGiftPageLiveData.value as ValidationError<*,*>).data as PreValidateError
        assert(result.message == "message")
        assert(result.title == "title")

    }

}