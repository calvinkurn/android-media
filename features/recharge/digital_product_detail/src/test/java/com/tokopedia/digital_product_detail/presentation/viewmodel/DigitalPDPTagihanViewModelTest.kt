package com.tokopedia.digital_product_detail.presentation.viewmodel

import com.tokopedia.common.topupbills.data.product.CatalogOperator
import com.tokopedia.common.topupbills.favoritepdp.data.mapper.FavoritePersoMapper
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.digital_product_detail.data.mapper.DigitalAtcMapper
import com.tokopedia.digital_product_detail.presentation.data.TagihanDataFactory
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class DigitalPDPTagihanViewModelTest : DigitalPDPTagihanViewModelTestFixture() {

    private val dataFactory = TagihanDataFactory()
    private val mapAtcFactory = DigitalAtcMapper()
    private val persoMapperFactory = FavoritePersoMapper()

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

        viewModel.getMenuDetail(MENU_ID)
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
        val response = dataFactory.getFavoriteNumberData(true)
        val mappedResponse = persoMapperFactory.mapDigiPersoFavoriteToModel(response)
        onGetFavoriteNumber_thenReturn(mappedResponse)

        viewModel.getFavoriteNumbers(listOf(), listOf(), listOf())
        verifyGetFavoriteNumberChipsRepoGetCalled()
        verifyGetFavoriteNumberChipsSuccess(mappedResponse.favoriteChips)
        verifyGetFavoriteNumberListSuccess(mappedResponse.autoCompletes)
        verifyGetFavoriteNumberPrefillSuccess(mappedResponse.prefill)
    }

    @Test
    fun `when getting favoriteNumber without prefill (or any type) should run and give success result with empty default`() {
        val response = dataFactory.getFavoriteNumberData(false)
        val mappedResponse = persoMapperFactory.mapDigiPersoFavoriteToModel(response)
        onGetFavoriteNumber_thenReturn(mappedResponse)

        viewModel.getFavoriteNumbers(listOf(), listOf(), listOf())
        verifyGetFavoriteNumberChipsRepoGetCalled()
        verifyGetFavoriteNumberChipsSuccess(mappedResponse.favoriteChips)
        verifyGetFavoriteNumberListSuccess(mappedResponse.autoCompletes)
        verifyGetFavoriteNumberPrefillSuccess(mappedResponse.prefill)
        verifyGetFavoriteNumberPrefillEmpty()
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
        verifySetOperatorListSuccess(response.response.operatorGroups?.firstOrNull()?.operators ?: listOf())
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
        val response = mapAtcFactory.mapAtcToResult(dataFactory.getAddToCartData())
        onGetAddToCart_thenReturn(response)

        viewModel.addToCart(RequestBodyIdentifier(), "")
        verifyAddToCartRepoGetCalled()
        verifyAddToCartSuccess(response)
    }

    @Test
    fun `when getting addToCart should run and get error ResponseErrorException should return custom message`() {
        val errorMessage = "error"
        val errorResponseException = ResponseErrorException(errorMessage)
        val errorMessageException = MessageErrorException(errorMessage)
        onGetAddToCart_thenReturn(errorResponseException)

        viewModel.addToCart(RequestBodyIdentifier(), "")
        verifyAddToCartRepoGetCalled()
        verifyAddToCartError(errorMessageException)
    }

    @Test
    fun `when getting addToCart should run and get error ResponseErrorException but empty message should return default message`() {
        val errorMessage = "Terjadi kesalahan, ulangi beberapa saat lagi"
        val errorResponseException = ResponseErrorException()
        val errorMessageException = MessageErrorException(errorMessage)
        onGetAddToCart_thenReturn(errorResponseException)

        viewModel.addToCart(RequestBodyIdentifier(), "")
        verifyAddToCartRepoGetCalled()
        verifyAddToCartError(errorMessageException)
    }

    @Test
    fun `when getting addToCart should run and get any error other than ResponseErrorException should return that error`() {
        val errorMessageException = MessageErrorException()
        onGetAddToCart_thenReturn(errorMessageException)

        viewModel.addToCart(RequestBodyIdentifier(), "")
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
        runTest {
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
        runTest {
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
        runTest {
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
        runTest {
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
    fun `when updateCheckoutPassData called should update digitalCheckoutPassData`() {
        val response = dataFactory.getTagihanProductData()
        onGetTagihanProduct_thenReturn(response)

        viewModel.getTagihanProduct(MENU_ID, TagihanDataFactory.VALID_CLIENT_NUMBER, "")
        viewModel.updateCheckoutPassData(
            TagihanDataFactory.IDEM_POTENCY_KEY,
            TagihanDataFactory.VALID_CLIENT_NUMBER
        )
        val expectedResult = dataFactory.getCheckoutPassData()
        verifyCheckoutPassDataUpdated(expectedResult)
    }

    @Test
    fun `when updateCategoryCheckoutPassData called should update digitalCheckoutPassData`() {
        verifyCheckoutPassDataCategoryIdEmpty()

        viewModel.updateCategoryCheckoutPassData(TagihanDataFactory.CATEGORY_ID)
        verifyCheckoutPassDataCategoryIdUpdated(TagihanDataFactory.CATEGORY_ID)
    }

    @Test
    fun `given tagihanProduct null when updateCheckoutPassData called should not update digitalCheckoutPassData`() {
        val expectedResult = viewModel.digitalCheckoutPassData

        viewModel.updateCheckoutPassData(
            TagihanDataFactory.IDEM_POTENCY_KEY,
            TagihanDataFactory.VALID_CLIENT_NUMBER
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
            TagihanDataFactory.VALID_CLIENT_NUMBER
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
            TagihanDataFactory.VALID_CLIENT_NUMBER
        )
        verifyCheckoutPassDataUpdated(expectedResult)
    }

    @Test
    fun `given operatorData empty when calling setOperatorDataById should update operatorData with correct data`() {
        val operatorList = dataFactory.getOperatorList()
        onGetOperatorList_thenReturn(operatorList)

        viewModel.setOperatorDataById(TagihanDataFactory.OPERATOR_ID_TAGLIS)
        verifyOperatorDataHasCorrectData(dataFactory.getOperatorDataTaglis())
    }

    @Test
    fun `given operatorData not empty when calling setOperatorDataById should update operatorData with correct data`() {
        val operatorData = dataFactory.getOperatorDataTaglis()
        val operatorList = dataFactory.getOperatorList()
        onGetOperatorData_thenReturn(operatorData)
        onGetOperatorList_thenReturn(operatorList)

        viewModel.setOperatorDataById(TagihanDataFactory.OPERATOR_ID_NON_TAGLIS)
        verifyOperatorDataHasCorrectData(dataFactory.getOperatorDataNonTaglis())
    }

    @Test
    fun `given operatorId not exists operatorList when calling setOperatorDataById should set default value`() {
        val operatorList = dataFactory.getOperatorList()
        onGetOperatorList_thenReturn(operatorList)

        viewModel.setOperatorDataById(TagihanDataFactory.OPERATOR_ID_INVALID)
        verifyOperatorDataHasCorrectData(CatalogOperator())
    }

    @Test
    fun `when getting listInfo should run and give success result`() {
        val response = dataFactory.getOperatorSelectGroupData()
        val expectedlistInfo = response.response.operatorGroups?.first()
            ?.operators?.first()?.attributes?.operatorDescriptions ?: listOf()
        onGetOperatorSelectGroup_thenReturn(response)

        viewModel.getOperatorSelectGroup(DigitalPDPTokenListrikViewModelTest.MENU_ID)
        verifyGetOperatorSelectGroupRepoGetCalled()
        verifyGetOperatorSelectGroupSuccess(response)

        val actualListInfo = viewModel.getListInfo()
        verifyListInfoSuccess(expectedlistInfo, actualListInfo)
    }

    @Test
    fun `when getting listInfo should run and give empty result`() {
        val actualListInfo = viewModel.getListInfo()
        verifyListInfoEmpty(actualListInfo)
    }

    companion object {
        const val MENU_ID = 305
    }
}
