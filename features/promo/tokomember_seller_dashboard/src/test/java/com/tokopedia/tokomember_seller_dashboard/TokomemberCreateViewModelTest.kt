package com.tokopedia.tokomember_seller_dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.tokomember_seller_dashboard.domain.TmCouponDetailUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TmKuponCreateUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TmKuponCreateValidateUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TmKuponInitialUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TmKuponProgramValidateUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TmKuponUpdateUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TmSingleCouponCreateUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberCardColorMapperUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberDashCardUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberDashEditCardUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberDashGetProgramFormUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberDashUpdateProgramUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemeberCardBgUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.*
import com.tokopedia.tokomember_seller_dashboard.model.*
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberCardBgItem
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberCardColor
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberCardColorItem
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmDashCreateViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class TokomemberCreateViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockThrowable = Throwable(message = "exception")
    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: TmDashCreateViewModel
    private val tokomemberDashEditCardUsecase = mockk<TokomemberDashEditCardUsecase>(relaxed = true)
    private val tokomemberDashCardUsecase = mockk<TokomemberDashCardUsecase>(relaxed = true)
    private val tokomemberDashGetProgramFormUsecase =
        mockk<TokomemberDashGetProgramFormUsecase>(relaxed = true)
    private val tokomemberDashUpdateProgramUsecase =
        mockk<TokomemberDashUpdateProgramUsecase>(relaxed = true)
    private val tokomemberCardColorMapperUsecase =
        mockk<TokomemberCardColorMapperUsecase>(relaxed = true)
    private val tokomemeberCardBgUsecase = mockk<TokomemeberCardBgUsecase>(relaxed = true)

    private val tmKuponCreateValidateUsecase =  mockk<TmKuponCreateValidateUsecase>(relaxed = true)
    private val tmKuponCreateUsecase = mockk<TmKuponCreateUsecase>(relaxed = true)
    private val tmKuponUpdateUsecase = mockk<TmKuponUpdateUsecase>(relaxed = true)
    private val tmKuponProgramValidateUsecase = mockk<TmKuponProgramValidateUsecase>(relaxed = true)
    private val tmKuponInitialUsecase = mockk<TmKuponInitialUsecase>(relaxed = true)
    private val uploaderUseCase = mockk<UploaderUseCase>(relaxed = true)
    private val tmCouponDetailUsecase = mockk<TmCouponDetailUsecase>(relaxed = true)
    private val tmSingleCouponCreateUsecase =  mockk<TmSingleCouponCreateUsecase>(relaxed = true)

    private val editCardObserver: Observer<TokoLiveDataResult<MembershipCreateEditCard>> = mockk(relaxed = true)

    @Before
    fun setUp() {
        viewModel = TmDashCreateViewModel(
            tokomemberDashCardUsecase,
            tokomemberCardColorMapperUsecase,
            tokomemeberCardBgUsecase,
            tokomemberDashEditCardUsecase,
            tokomemberDashGetProgramFormUsecase,
            tokomemberDashUpdateProgramUsecase,
            tmKuponCreateValidateUsecase,
            tmKuponCreateUsecase,
            tmSingleCouponCreateUsecase,
            tmKuponUpdateUsecase,
            tmKuponProgramValidateUsecase,
            tmKuponInitialUsecase,
            uploaderUseCase,
            tmCouponDetailUsecase,
            dispatcher
        )
    }

    @Test
    fun failGetCardInfo() {
        coEvery {
            tokomemberDashCardUsecase.getMembershipCardInfo(any(), any(), 0)
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getCardInfo(0)
        Assert.assertEquals(
            (viewModel.tmCardResultLiveData.value as TokoLiveDataResult).error,
            mockThrowable
        )
    }

    @Test
    fun successGetCardInfo() {
        val cardData = mockk<CardData>(relaxed = true)
        val data = CardDataTemplate(
            card = cardData.membershipGetCardForm?.card,
            cardTemplate = cardData.membershipGetCardForm?.cardTemplate,
            cardTemplateImageList = cardData.membershipGetCardForm?.cardTemplateImageList,
            shopAvatar = cardData.membershipGetCardForm?.shopAvatar?:""
        )
        coEvery {
            tokomemberDashCardUsecase.getMembershipCardInfo(any(), any(), 0)
        } coAnswers {
            firstArg<(CardData) -> Unit>().invoke(cardData)
        }
        viewModel.getCardInfo(0)
        Assert.assertEquals(
            (viewModel.tmCardResultLiveData.value as TokoLiveDataResult).data,
            data
        )
    }

    @Test
    fun successGetCardBackgroundData() {
        val cardData = CardData()
        val data = mockk<TokomemberCardBgItem>(relaxed = true)
        coEvery {
            tokomemeberCardBgUsecase.getCardBgDataN(cardData, "", arrayListOf(), any(), any())
        } coAnswers {
            arg<(TokomemberCardBgItem) -> Unit>(3).invoke(data)
        }
        viewModel.getCardBackgroundData(cardData, "", arrayListOf())
        Assert.assertEquals(
            (viewModel.tokomemberCardBgResultLiveData.value as Success).data,
            data
        )
    }

    @Test
    fun failureGetCardBackgroundData() {
        val cardData = CardData()
        coEvery {
            tokomemeberCardBgUsecase.getCardBgDataN(cardData, "", arrayListOf(), any(), any())
        } coAnswers {
            lastArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getCardBackgroundData(cardData, "", arrayListOf())
        Assert.assertEquals(
            (viewModel.tokomemberCardBgResultLiveData.value as Fail).throwable,
            mockThrowable
        )
    }


    @Test
    fun successProgramForm(){
        val data = mockk<ProgramDetailData>(relaxed = true)
        coEvery {
            tokomemberDashGetProgramFormUsecase.getProgramInfo(any(), any(), 0,0,"","")
        } coAnswers {
            firstArg<(ProgramDetailData) -> Unit>().invoke(data)
        }
        viewModel.getProgramInfo(0,0,"","")

        Assert.assertEquals(
            (viewModel.tmProgramResultLiveData.value as TokoLiveDataResult).data,
            data
        )
    }

    @Test
    fun failProgramForm() {
        coEvery {
            tokomemberDashGetProgramFormUsecase.getProgramInfo(any(), any(), 0,0,"","")
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getProgramInfo(0,0,"","")
        Assert.assertEquals(
            (viewModel.tmProgramResultLiveData.value as TokoLiveDataResult).error,
            mockThrowable
        )
    }

    @Test
    fun successUpdateProgram(){
        val data = mockk<ProgramUpdateResponse>(relaxed = true)
        coEvery {
            tokomemberDashUpdateProgramUsecase.updateProgram(any(), any(), any())
        } coAnswers {
            firstArg<(ProgramUpdateResponse) -> Unit>().invoke(data)
        }
        viewModel.updateProgram(ProgramUpdateDataInput())

        Assert.assertEquals(
            (viewModel.tokomemberProgramUpdateResultLiveData.value as TokoLiveDataResult).data,
            data
        )
    }

    @Test
    fun failUpdateProgram() {
        coEvery {
            tokomemberDashUpdateProgramUsecase.updateProgram(any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.updateProgram(ProgramUpdateDataInput())
        Assert.assertEquals(
            (viewModel.tokomemberProgramUpdateResultLiveData.value as TokoLiveDataResult).error,
          mockThrowable)
    }


    @Test
    fun successUpdateCard(){
        val observer = mockk<Observer<TokoLiveDataResult<MembershipCreateEditCard>>> {
            every { onChanged(any()) } just Runs
        }
        val data = mockk<MembershipCreateEditCardResponse>(relaxed = true)
        coEvery {
            tokomemberDashEditCardUsecase.modifyShopCard(any(), any(), any())
        } coAnswers {
            firstArg<(MembershipCreateEditCardResponse) -> Unit>().invoke(data)
        }
        viewModel.modifyShopCard(TmCardModifyInput())

        Assert.assertEquals(
            (viewModel.tokomemberCardModifyLiveData.value as TokoLiveDataResult).data,
            data
        )
    }

    @Test
    fun failUpdateCard() {
        coEvery {
            tokomemberDashEditCardUsecase.modifyShopCard(any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.modifyShopCard(TmCardModifyInput())
        Assert.assertEquals(
            (viewModel.tokomemberCardModifyLiveData.value?.error),
            mockThrowable
        )
    }

    @Test
    fun `create coupon success`(){
        val couponCreateRes = mockk<CouponCreateMultiple>()
        val merchantRequest = mockk<TmMerchantCouponUnifyRequest>()
       every {
           tmKuponCreateUsecase.createKupon(any(),any(),any())
       } answers {
           firstArg<(CouponCreateMultiple) -> Unit>().invoke(couponCreateRes)
       }
        viewModel.createCoupon(merchantRequest)
        assertEquals(
            (viewModel.tmCouponCreateLiveData.value as Success).data,
            couponCreateRes
        )
    }

    @Test
    fun `create coupon failure`(){
        val merchantRequest = mockk<TmMerchantCouponUnifyRequest>()
        every {
            tmKuponCreateUsecase.createKupon(any(),any(),any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.createCoupon(merchantRequest)
        assertEquals(
            (viewModel.tmCouponCreateLiveData.value as Fail).throwable,
            mockThrowable
        )
    }

    @Test
    fun `create single coupon success`(){
        val couponSingleRes = mockk<CouponCreateSingle>()
        val merchantRequest = mockk<TmCouponCreateRequest>()
        every {
            tmSingleCouponCreateUsecase.createSingleCoupon(any(),any(),any())
        } answers {
            firstArg<(CouponCreateSingle) -> Unit>().invoke(couponSingleRes)
        }
        viewModel.createSingleCoupon(merchantRequest)
        assertEquals(
            (viewModel.tmSingleCouponCreateLiveData.value as Success).data,
            couponSingleRes
        )
    }

    @Test
    fun `create single coupon failure`(){
        val merchantRequest = mockk<TmCouponCreateRequest>()
        every {
            tmSingleCouponCreateUsecase.createSingleCoupon(any(),any(),any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.createSingleCoupon(merchantRequest)
        assertEquals(
            (viewModel.tmSingleCouponCreateLiveData.value as Fail).throwable,
            mockThrowable
        )
    }

    @Test
    fun `update coupon success`(){
        val updateCouponRes = mockk<TmKuponUpdateMVResponse>()
        val updateRequest = mockk<TmCouponUpdateRequest>()
        every {
            tmKuponUpdateUsecase.updateCoupon(any(),any(),any())
        } answers {
            firstArg<(TmKuponUpdateMVResponse) -> Unit>().invoke(updateCouponRes)
        }
        viewModel.updateCoupon(updateRequest)
        assertEquals(
            viewModel.tmCouponUpdateLiveData.value?.status,
            TokoLiveDataResult.STATUS.SUCCESS
        )
    }

    @Test
    fun `update coupon failure`(){
        val updateRequest = mockk<TmCouponUpdateRequest>()
        every {
            tmKuponUpdateUsecase.updateCoupon(any(),any(),any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.updateCoupon(updateRequest)
        assertEquals(
            viewModel.tmCouponUpdateLiveData.value?.status,
            TokoLiveDataResult.STATUS.ERROR
        )
    }

    @Test
    fun `get initial coupon data success`(){
        val couponInitialRes = mockk<TmCouponInitialResponse>()
        every {
            tmKuponInitialUsecase.getInitialCoupon(any(),any(),any(),any())
        } answers {
            firstArg<(TmCouponInitialResponse) -> Unit>().invoke(couponInitialRes)
        }
        viewModel.getInitialCouponData("","")
        assertEquals(
            viewModel.tmCouponInitialLiveData.value?.status,
            TokoLiveDataResult.STATUS.SUCCESS
        )
    }

    @Test
    fun `get initial coupon data failure`(){
        every {
            tmKuponInitialUsecase.getInitialCoupon(any(),any(),any(),any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getInitialCouponData("","")
        assertEquals(
            viewModel.tmCouponInitialLiveData.value?.status,
            TokoLiveDataResult.STATUS.ERROR
        )
    }

    @Test
    fun `validate program success`(){
        val response = mockk<MemberShipValidateResponse>()
        every {
            tmKuponProgramValidateUsecase.getMembershipValidateInfo(any(),any(),any(),any(),any(),any())
        } answers {
            firstArg<(MemberShipValidateResponse) -> Unit>().invoke(response)
        }
        viewModel.validateProgram("","","","")
        assertEquals(
            viewModel.tmProgramValidateLiveData.value?.status,
            TokoLiveDataResult.STATUS.SUCCESS
        )
    }

    @Test
    fun `validate program failure`(){
        every {
            tmKuponProgramValidateUsecase.getMembershipValidateInfo(any(),any(),any(),any(),any(),any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.validateProgram("","","","")
        assertEquals(
            viewModel.tmProgramValidateLiveData.value?.status,
            TokoLiveDataResult.STATUS.ERROR
        )
    }

    @Test
    fun `pre validate coupon success`(){
        val response = mockk<TmVoucherValidationPartialResponse>()
        val request = mockk<TmCouponValidateRequest>()
        every {
            tmKuponCreateValidateUsecase.getPartialValidateData(any(),any(),any())
        } answers {
            firstArg<(TmVoucherValidationPartialResponse) -> Unit>().invoke(response)
        }
        viewModel.preValidateCoupon(request)
        assertEquals(
            viewModel.tmCouponPreValidateSingleCouponLiveData.value?.status,
            TokoLiveDataResult.STATUS.SUCCESS
        )
    }

    @Test
    fun `pre validate coupon failure`(){
        val request = mockk<TmCouponValidateRequest>()
        every {
            tmKuponCreateValidateUsecase.getPartialValidateData(any(),any(),any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.preValidateCoupon(request)
        assertEquals(
            viewModel.tmCouponPreValidateSingleCouponLiveData.value?.status,
            TokoLiveDataResult.STATUS.ERROR
        )
    }

    @Test
    fun `pre validate multiple coupon success`(){
        val response = mockk<TmVoucherValidationPartialResponse>()
        val request = mockk<TmCouponValidateRequest>()
        every {
            tmKuponCreateValidateUsecase.getPartialValidateData(any(),any(),any())
        } answers {
            firstArg<(TmVoucherValidationPartialResponse) -> Unit>().invoke(response)
        }
        viewModel.preValidateMultipleCoupon(request)
        assertEquals(
            viewModel.tmCouponPreValidateMultipleCouponLiveData.value?.status,
            TokoLiveDataResult.STATUS.SUCCESS
        )
    }

    @Test
    fun `pre validate multiple coupon failure`(){
        val request = mockk<TmCouponValidateRequest>()
        every {
            tmKuponCreateValidateUsecase.getPartialValidateData(any(),any(),any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.preValidateMultipleCoupon(request)
        assertEquals(
            viewModel.tmCouponPreValidateMultipleCouponLiveData.value?.status,
            TokoLiveDataResult.STATUS.ERROR
        )
    }

    @Test
    fun `upload image vip success`(){
        val imageFile = mockk<File>()
        coEvery { uploaderUseCase(any()) } returns UploadResult.Success()
        viewModel.uploadImageVip(imageFile)
        assertEquals(
            viewModel.tmCouponUploadLiveData.value?.status,
            TokoLiveDataResult.STATUS.SUCCESS
        )
    }

    @Test
    fun `upload image vip failure`(){
        val imageFile = mockk<File>()
        coEvery { uploaderUseCase(any()) } throws  mockThrowable
        viewModel.uploadImageVip(imageFile)
        assertEquals(
            viewModel.tmCouponUploadLiveData.value?.status,
            TokoLiveDataResult.STATUS.ERROR
        )
    }

    @Test
    fun `upload image premium success`(){
        val imageFile = mockk<File>()
        coEvery { uploaderUseCase(any()) } returns UploadResult.Success()
        viewModel.uploadImagePremium(imageFile)
        assertEquals(
            viewModel.tmCouponUploadMultipleLiveData.value?.status,
            TokoLiveDataResult.STATUS.SUCCESS
        )
    }

    @Test
    fun `upload image premium failure`(){
        val imageFile = mockk<File>()
        coEvery { uploaderUseCase(any()) } throws  mockThrowable
        viewModel.uploadImagePremium(imageFile)
        assertEquals(
            viewModel.tmCouponUploadMultipleLiveData.value?.status,
            TokoLiveDataResult.STATUS.ERROR
        )
    }

    @Test
    fun `get coupon detail success`(){
        val response = mockk<TmCouponDetailResponseData>()
        every {
            tmCouponDetailUsecase.getCouponDetail(any(),any(),any())
        } answers {
            firstArg<(TmCouponDetailResponseData) -> Unit>().invoke(response)
        }
        viewModel.getCouponDetail(0)
        assertEquals(
            viewModel.tmCouponDetaillLiveData.value?.status,
            TokoLiveDataResult.STATUS.SUCCESS
        )
    }

    @Test
    fun `get coupon detail failure`(){
        every {
            tmCouponDetailUsecase.getCouponDetail(any(),any(),any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getCouponDetail(0)
        assertEquals(
            viewModel.tmCouponDetaillLiveData.value?.status,
            TokoLiveDataResult.STATUS.ERROR
        )
    }

    @Test
    fun `get card color data success`(){
        val cardData = mockk<CardData>()
        val cardColor = mockk<TokomemberCardColor>(relaxed = true)
        val response = TokomemberCardColorItem(
            ArrayList<Visitable<*>>().apply {
                add(cardColor)
            }
        )
        every{
            tokomemberCardColorMapperUsecase.getCardColorData(any(),any(),any())
        } answers {
            secondArg<(TokomemberCardColorItem) -> Unit>().invoke(response)
        }
        viewModel.getCardColorData(cardData)
        assertEquals(
            (viewModel.tokomemberCardColorResultLiveData.value as Success).data,
            response
        )
    }

    @Test
    fun `get card color data failure`(){
        val cardData = mockk<CardData>()
        every{
            tokomemberCardColorMapperUsecase.getCardColorData(any(),any(),any())
        } answers {
            lastArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getCardColorData(cardData)
        assertEquals(
            (viewModel.tokomemberCardColorResultLiveData.value as Fail).throwable,
            mockThrowable
        )
    }
}
