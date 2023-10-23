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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception
import kotlin.reflect.KClass

@ExperimentalCoroutinesApi
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
        val couponDetailEntity = CouponDetailEntity(code = "200",cta = "cta" ,title = "title" , description = "description")
        dummyCouponData.add(couponDetailEntity)
        val data = RedeemCouponEntity(coupons=dummyCouponData,redeemMessage = "claim success")
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

        coEvery { repository.startSaveCoupon(1) } throws mockk<CatalogGqlError> {
            every { messageErrorException } returns  mockk{
                every { message } returns "message"
            }
            every { developerMessage } returns  "title|message|300"
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
        val listCatalog = ArrayList<CatalogStatusItem>()
        listCatalog.add(CatalogStatusItem())
        val dummyData = CatalogStatusBase(catalogs = listCatalog)
        coEvery { repository.fetchLatestStatus(any()) } returns mockk {
            every { catalogStatus } returns dummyData
            }

        viewModel.latestStatusLiveData.observeForever(observer)
        viewModel.fetchLatestStatus(listOf())

        verify(exactly = 1) { observer.onChanged(any()) }

        assert(viewModel.latestStatusLiveData.value == dummyData.catalogs?.get(0))
    }

    @Test
    fun `fetch Latest Status error case`() {
        val observer = mockk<Observer<CatalogStatusItem>>()
        coEvery { repository.fetchLatestStatus(any()) } returns CatalogStatusOuter()
        viewModel.latestStatusLiveData.observeForever(observer)
        viewModel.fetchLatestStatus(listOf())

        verify(exactly = 0) { observer.onChanged(any()) }

    }

    @Test
    fun `start Send Gift success`() {
        val id = 1
        val title = "title"
        val pointStr = "pointerString"
        val banner = "banner"

        coEvery { repository.startSendGift(id) } returns PreValidateRedeemBase(
            preValidateRedeem = PreValidateRedeemBaseValue(isValid = 1)
        )
        viewModel.startSendGift(id, title, pointStr, banner)

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

    @Test
    fun `given error message equals 1 when startSendGift should update sendGiftPageLiveData with first error message`() {
        val errorMessage = "internal server error"

        val id = 1
        val title = "title"
        val pointStr = "pointerString"
        val banner = "banner"

        coEvery { repository.startSendGift(id) } throws MessageErrorException(errorMessage)

        viewModel.startSendGift(id, title, pointStr, banner)

        val data = (viewModel.sendGiftPageLiveData.value as ValidationError<SendGiftPage, Any>).data
        val preValidateError = data as PreValidateError
        val actualErrorMessage = preValidateError.message
        val expectedErrorMessage = "internal server error"

        assertEquals(expectedErrorMessage, actualErrorMessage)
    }
}
