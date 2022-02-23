package com.tokopedia.digital_product_detail.presentation.viewmodel

import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.digital_product_detail.presentation.data.PulsaDataFactory
import com.tokopedia.digital_product_detail.presentation.data.TagihanDataFactory
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import org.junit.Test

@ExperimentalCoroutinesApi
class DigitalPDPTagihanViewModelTest: DigitalPDPTagihanViewModelTestFixture() {

    private val dataFactory = TagihanDataFactory()

    @Test
    fun `given menuDetail loading state then should get loading state`() {
        val loadingResponse = RechargeNetworkResult.Loading

        viewModel.setMenuDetailLoading()
        verifyGetMenuDetailLoading(loadingResponse)
    }

    @Test
    fun `when getting menuDetail should run and give success result`() {
        val response = dataFactory.getMenuDetailData()
        onGetMenuDetail_thenReturn(response)

        viewModel.getMenuDetail(MENU_ID)
        verifyGetMenuDetailSuccess(response)
    }

    @Test
    fun `when getting menuDetail should run and give fail result`() {
        onGetMenuDetail_thenReturn(NullPointerException())

        viewModel.getMenuDetail(MENU_ID, false)
        verifyGetMenuDetailRepoGetCalled()
        verifyGetMenuDetailFail()
    }

    @Test
    fun `given favoriteNumber loading state then should get loading state`() {
        val loadingResponse = RechargeNetworkResult.Loading

        viewModel.setFavoriteNumberLoading()
        verifyGetFavoriteNumberLoading(loadingResponse)
    }

    @Test
    fun `when getting favoriteNumber should run and give success result`() {
        val response = dataFactory.getFavoriteNumberData()
        onGetFavoriteNumber_thenReturn(response)

        viewModel.getFavoriteNumber(listOf())
        verifyGetFavoriteNumberRepoGetCalled()
        verifyGetFavoriteNumberSuccess(response.persoFavoriteNumber.items)
    }

    @Test
    fun `when getting favoriteNumber should run and give fail result`() {
        onGetFavoriteNumber_thenReturn(NullPointerException())

        viewModel.getFavoriteNumber(listOf())
        verifyGetFavoriteNumberRepoGetCalled()
        verifyGetFavoriteNumberFail()
    }

    @Test
    fun `given catalogSelectGroup loading state then should get loading state`() {
        val loadingResponse = RechargeNetworkResult.Loading

        viewModel.setOperatorSelectGroupLoading()
        verifyGetOperatorSelectGroupLoading(loadingResponse)
    }

    @Test
    fun `when getting catalogSelectGroup should run and give success result`() {
        val response = dataFactory.getOperatorSelectGroupData()
        onGetOperatorSelectGroup_thenReturn(response)

        viewModel.getOperatorSelectGroup(MENU_ID)
        verifyGetOperatorSelectGroupRepoGetCalled()
        verifyGetOperatorSelectGroupSuccess(response)
    }

    @Test
    fun `when getting catalogSelectGroup should run and give fail result`() {
        onGetOperatorSelectGroup_thenReturn(NullPointerException())

        viewModel.getOperatorSelectGroup(MENU_ID)
        verifyGetOperatorSelectGroupRepoGetCalled()
        verifyGetOperatorSelectGroupFail()
    }

    @Test
    fun `given tagihanProduct loading state then should get loading state`() {
        val loadingResponse = RechargeNetworkResult.Loading

        viewModel.setTagihanProductLoading()
        verifyGetTagihanProductLoading(loadingResponse)
    }

    @Test
    fun `when getting tagihanProduct should run and give success result`() {
        val response = dataFactory.getTagihanProductData()
        onGetTagihanProduct_thenReturn(response)

        viewModel.getTagihanProduct(MENU_ID, TagihanDataFactory.VALID_CLIENT_NUMBER, "")
        verifyGetTagihanProductRepoGetCalled()
        verifyGetTagihanProductSuccess(response)
    }

    @Test
    fun `when getting tagihanProduct should run and give fail result`() {
        onGetTagihanProduct_thenReturn(NullPointerException())

        viewModel.getTagihanProduct(MENU_ID, TagihanDataFactory.VALID_CLIENT_NUMBER, "")
        verifyGetTagihanProductRepoGetCalled()
        verifyGetTagihanProductFail()
    }

    @Test
    fun `when getting null tagihanProduct should not emit anything`() {
        onGetTagihanProduct_thenReturn(null)

        viewModel.getTagihanProduct(MENU_ID, TagihanDataFactory.VALID_CLIENT_NUMBER, "")
        verifyGetTagihanProductRepoGetCalled()
        verifyGetTagihanProductFail()
    }

    @Test
    fun `when getting addToCart should run and return success`() {
        val response = dataFactory.getAddToCartData().relationships?.category?.data?.id ?: ""
        onGetAddToCart_thenReturn(response)

        viewModel.addToCart(RequestBodyIdentifier(), DigitalSubscriptionParams(), "")
        verifyAddToCartRepoGetCalled()
        verifyAddToCartSuccess(response)
    }

    @Test
    fun `when getting addToCart should run and get error ResponseErrorException should return custom message`() {
        val errorMessage = "error"
        val errorResponseException = ResponseErrorException(errorMessage)
        val errorMessageException = MessageErrorException(errorMessage)
        onGetAddToCart_thenReturn(errorResponseException)

        viewModel.addToCart(RequestBodyIdentifier(), DigitalSubscriptionParams(), "")
        verifyAddToCartRepoGetCalled()
        verifyAddToCartError(errorMessageException)
    }

    @Test
    fun `when getting addToCart should run and get error ResponseErrorException but empty message should return default message`() {
        val errorMessage = "Terjadi kesalahan, ulangi beberapa saat lagi"
        val errorResponseException = ResponseErrorException()
        val errorMessageException = MessageErrorException(errorMessage)
        onGetAddToCart_thenReturn(errorResponseException)

        viewModel.addToCart(RequestBodyIdentifier(), DigitalSubscriptionParams(), "")
        verifyAddToCartRepoGetCalled()
        verifyAddToCartError(errorMessageException)
    }

    @Test
    fun `when getting addToCart should run and get any error other than ResponseErrorException should return that error`() {
        val errorMessageException = MessageErrorException()
        onGetAddToCart_thenReturn(errorMessageException)

        viewModel.addToCart(RequestBodyIdentifier(), DigitalSubscriptionParams(), "")
        verifyAddToCartRepoGetCalled()
        verifyAddToCartErrorExceptions(errorMessageException)
    }

    @Test
    fun `given addToCart loading state then should get loading state`() {
        val loadingResponse = RechargeNetworkResult.Loading

        viewModel.setAddToCartLoading()
        verifyAddToCartErrorLoading(loadingResponse)
    }

    @Test
    fun `given empty validator when validateClientNumber should set isEligibleToBuy true`() =
        testCoroutineRule.runBlockingTest {
            val response = dataFactory.getOperatorSelectGroupEmptyValData()
            onGetOperatorSelectGroup_thenReturn(response)

            viewModel.getOperatorSelectGroup(MENU_ID)
            viewModel.validateClientNumber(TagihanDataFactory.VALID_CLIENT_NUMBER)
            skipValidatorDelay()

            verifyGetOperatorSelectGroupSuccess(response)
            verifyValidateClientNumberTrue()
        }

    @Test
    fun `given non-empty validator when validateClientNumber with valid number should set isEligibleToBuy true`() =
        testCoroutineRule.runBlockingTest {
            val response = dataFactory.getOperatorSelectGroupData()
            onGetOperatorSelectGroup_thenReturn(response)

            viewModel.getOperatorSelectGroup(MENU_ID)
            viewModel.validateClientNumber(TagihanDataFactory.VALID_CLIENT_NUMBER)
            skipValidatorDelay()

            verifyGetOperatorSelectGroupSuccess(response)
            verifyValidateClientNumberTrue()
        }

    @Test
    fun `given non-empty validator when validateClientNumber with non-valid number should set isEligibleToBuy false`() =
        testCoroutineRule.runBlockingTest {
            val response = dataFactory.getOperatorSelectGroupData()
            onGetOperatorSelectGroup_thenReturn(response)

            viewModel.getOperatorSelectGroup(MENU_ID)
            viewModel.validateClientNumber(TagihanDataFactory.INVALID_CLIENT_NUMBER)
            skipValidatorDelay()

            verifyGetOperatorSelectGroupSuccess(response)
            verifyValidateClientNumberFalse()
        }

    @Test
    fun `given validateClientNumber running when cancelValidatorJob called, the job should be cancelled`() {
        testCoroutineRule.runBlockingTest {
            viewModel.validateClientNumber(TagihanDataFactory.VALID_CLIENT_NUMBER)
            viewModel.cancelValidatorJob()
            verifyValidatorJobIsCancelled()
        }
    }

    @Test
    fun `given validatorJob null when cancelValidatorJob called should do nothing`() {
        viewModel.cancelValidatorJob()
        verifyValidatorJobIsNull()
    }

    @Test
    fun `given validatorJob null when implicit setValidatorJob executed should update validatorJob to non-null`() {
        viewModel.validatorJob = Job()
        verifyValidatorJobIsNotNull()
    }

    @Test
    fun `when getting inquiry should run and give success result`() {
        val response = dataFactory.getInquiry()
        onGetInquiry_thenReturn(response)

        viewModel.inquiry(
            TagihanDataFactory.PRODUCT_ID,
            TagihanDataFactory.VALID_CLIENT_NUMBER,
            mapOf()
        )
        val expectedResult = dataFactory.getInquiry()
        verifyInquiryProductRepoGetCalled()
        verifyGetInquiryProductSuccess(expectedResult)
    }

    @Test
    fun `given inquiry loading state then should get loading state`() {
        val loadingResponse = RechargeNetworkResult.Loading

        viewModel.setInquiryLoading()
        verifyGetInquiryProductLoading(loadingResponse)
    }

    @Test
    fun `when getting inquiry should run and give fail result`() {
        onGetInquiry_thenReturn(NullPointerException())

        viewModel.inquiry(
            TagihanDataFactory.PRODUCT_ID,
            TagihanDataFactory.VALID_CLIENT_NUMBER,
            mapOf()
        )
        verifyInquiryProductRepoGetCalled()
        verifyGetInquiryProductFail()
    }

    @Test
    fun `when updateCheckoutPassData called should update digitalCheckoutPassData`() {
        val response = dataFactory.getTagihanProductData()
        onGetTagihanProduct_thenReturn(response)

        viewModel.getTagihanProduct(MENU_ID, TagihanDataFactory.VALID_CLIENT_NUMBER, "")
        viewModel.updateCheckoutPassData(
            TagihanDataFactory.IDEM_POTENCY_KEY,
            TagihanDataFactory.VALID_CLIENT_NUMBER,
        )
        val expectedResult = dataFactory.getCheckoutPassData()
        verifyCheckoutPassDataUpdated(expectedResult)
    }

    @Test
    fun `when updateCategoryCheckoutPassData called should update digitalCheckoutPassData`() {
        verifyCheckoutPassDataCategoryIdEmpty()

        viewModel.updateCategoryCheckoutPassData(PulsaDataFactory.CATEGORY_ID)
        verifyCheckoutPassDataCategoryIdUpdated(PulsaDataFactory.CATEGORY_ID)
    }

    @Test
    fun `given tagihanProduct null when updateCheckoutPassData called should not update digitalCheckoutPassData`() {
        val expectedResult = viewModel.digitalCheckoutPassData

        viewModel.updateCheckoutPassData(
            TagihanDataFactory.IDEM_POTENCY_KEY,
            TagihanDataFactory.VALID_CLIENT_NUMBER,
        )
        verifyCheckoutPassDataNotUpdated(expectedResult)
    }

    @Test
    fun `given tagihanProduct result is fail when updateCheckoutPassData called should not update digitalCheckoutPassData`() {
        val expectedResult = viewModel.digitalCheckoutPassData
        onGetTagihanProduct_thenReturn(NullPointerException())

        viewModel.getTagihanProduct(MENU_ID, TagihanDataFactory.VALID_CLIENT_NUMBER, "")
        viewModel.updateCheckoutPassData(
            TagihanDataFactory.IDEM_POTENCY_KEY,
            TagihanDataFactory.VALID_CLIENT_NUMBER,
        )
        verifyCheckoutPassDataNotUpdated(expectedResult)
    }

    @Test
    fun `given tagihanProduct's productPromo not null when updateCheckoutPassData called should update digitalCheckoutPassData with no promo`() {
        val response = dataFactory.getTagihanProductWithPromoData()
        val expectedResult = viewModel.digitalCheckoutPassData
        onGetTagihanProduct_thenReturn(response)

        viewModel.getTagihanProduct(MENU_ID, TagihanDataFactory.VALID_CLIENT_NUMBER, "")
        viewModel.updateCheckoutPassData(
            TagihanDataFactory.IDEM_POTENCY_KEY,
            TagihanDataFactory.VALID_CLIENT_NUMBER,
        )
        verifyCheckoutPassDataUpdated(expectedResult)
    }

    companion object {
        const val MENU_ID = 305
    }
}