package com.tokopedia.recharge_pdp_emoney.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.data.prefix_select.RechargeCatalogPrefixSelect
import com.tokopedia.common.topupbills.data.prefix_select.RechargePrefix
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.data.product.CatalogData
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.common.topupbills.data.product.CatalogProductData
import com.tokopedia.common.topupbills.usecase.RechargeCatalogPrefixSelectUseCase
import com.tokopedia.common.topupbills.usecase.RechargeCatalogProductInputUseCase
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.UnknownHostException

/**
 * @author by jessica on 15/04/21
 */
class EmoneyPdpViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var emoneyPdpViewModel: EmoneyPdpViewModel

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var rechargeCatalogPrefixUseCase: RechargeCatalogPrefixSelectUseCase

    @RelaxedMockK
    lateinit var rechargeCatalogProductInputUseCase: RechargeCatalogProductInputUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        emoneyPdpViewModel = EmoneyPdpViewModel(userSession,
                rechargeCatalogPrefixUseCase,
                rechargeCatalogProductInputUseCase)
    }

    @Test
    fun getPrefixOperator_onSuccess_shouldUpdateOperatorPrefix() {
        //given
        val prefixes = mutableListOf<RechargePrefix>()
        for (i in 0..5) {
            prefixes.add(RechargePrefix(i.toString(), (100 + i).toString()))
        }
        val response = TelcoCatalogPrefixSelect(RechargeCatalogPrefixSelect(prefixes = prefixes))
        coEvery { rechargeCatalogPrefixUseCase.execute(any(), any(), any()) } coAnswers {
            secondArg<(TelcoCatalogPrefixSelect) -> Unit>().invoke(response)
        }

        //when
        emoneyPdpViewModel.getPrefixOperator(0)

        //then
        assert(emoneyPdpViewModel.catalogPrefixSelect.value is Success)
        assert((emoneyPdpViewModel.catalogPrefixSelect.value as Success).data
                .rechargeCatalogPrefixSelect.prefixes.size == 6)
        assert((emoneyPdpViewModel.catalogPrefixSelect.value as Success).data
                .rechargeCatalogPrefixSelect.prefixes[3].key == "3")
        assert((emoneyPdpViewModel.catalogPrefixSelect.value as Success).data
                .rechargeCatalogPrefixSelect.prefixes[3].value == "103")
    }

    @Test
    fun getPrefixOperator_onFail_shouldShowErrorMessage() {
        //given
        coEvery { rechargeCatalogPrefixUseCase.execute(any(), any(), any()) } coAnswers {
            thirdArg<(Throwable) -> Unit>().invoke(UnknownHostException())
        }

        //when
        emoneyPdpViewModel.getPrefixOperator(0)

        //then
        assert(emoneyPdpViewModel.catalogPrefixSelect.value is Fail)
        assert((emoneyPdpViewModel.catalogPrefixSelect.value as Fail).throwable is UnknownHostException)
    }

    @Test
    fun getSelectedOperator_onInputNumberEmpty_shouldDoNothing() {
        //when
        emoneyPdpViewModel.getSelectedOperator("", "")

        //then
        assert(emoneyPdpViewModel.selectedOperator.value == null)
    }

    @Test
    fun getSelectedOperator_onInputNumberValid_shouldUpdateSelectedOperator() {
        //given
        getPrefixOperator_onSuccess_shouldUpdateOperatorPrefix()

        //when
        emoneyPdpViewModel.getSelectedOperator("102", "")

        //then
        assert(emoneyPdpViewModel.selectedOperator.value?.key ?: "0" == "2")
    }

    @Test
    fun getSelectedOperator_onInputNumberInvalid_shouldUpdateSelectedOperator() {
        //given
        getPrefixOperator_onSuccess_shouldUpdateOperatorPrefix()
        val errorMessage = "nomor tidak valid"

        //when
        emoneyPdpViewModel.getSelectedOperator("110", errorMessage)

        //then
        //error
        assert(emoneyPdpViewModel.inputViewError.value == errorMessage)
    }

    @Test
    fun getSelectedOperator_onCatalogPrefixSelectFailed_shouldShowErrorMessage() {
        //given
        getPrefixOperator_onFail_shouldShowErrorMessage()

        //when
        emoneyPdpViewModel.getSelectedOperator("105", "")

        //then
        //error
        assert(emoneyPdpViewModel.catalogPrefixSelect.value is Fail)
        assert(emoneyPdpViewModel.selectedOperator.value == null)
        assert(emoneyPdpViewModel.inputViewError.value == "")
    }

    @Test
    fun getProductFromOperator_onSuccess_shouldUpdateProducts() {
        //given
        val catalogProducts = mutableListOf<CatalogProduct>()
        for (i in 0..5) {
            catalogProducts.add(CatalogProduct(id = i.toString()))
        }
        val response = CatalogData.Response(CatalogData(product = CatalogProductData(
                dataCollections = listOf(CatalogProductData.DataCollection(
                        products = catalogProducts)))))
        coEvery { rechargeCatalogProductInputUseCase.execute(any(), any(), any()) } coAnswers {
            secondArg<(CatalogData.Response) -> Unit>().invoke(response)
        }

        //when
        emoneyPdpViewModel.getProductFromOperator(0, "")

        //then
        assert(emoneyPdpViewModel.catalogData.value is Success)
        assert((emoneyPdpViewModel.catalogData.value as Success).data.product.dataCollections.isNotEmpty())
        assert((emoneyPdpViewModel.catalogData.value as Success).data.product.dataCollections.first().products.size == 6)
        assert((emoneyPdpViewModel.catalogData.value as Success).data.product.dataCollections.first().products[4].id == "4")
    }

    @Test
    fun getProductFromOperator_onFail_shouldThrowThrowable() {
        //given
        val message = "This is custom message"
        coEvery { rechargeCatalogProductInputUseCase.execute(any(), any(), any()) } coAnswers {
            thirdArg<(Throwable) -> Unit>().invoke(MessageErrorException(message))
        }

        //when
        emoneyPdpViewModel.getProductFromOperator(0, "")

        //then
        assert(emoneyPdpViewModel.catalogData.value is Fail)
        assert((emoneyPdpViewModel.catalogData.value as Fail).throwable is MessageErrorException)
        assert((emoneyPdpViewModel.catalogData.value as Fail).throwable.message == message)
    }

    @Test
    fun setSelectedProduct_shouldUpdateSelectedProduct() {
        //given
        val id = "12344"
        val product = CatalogProduct(id = id)

        //when
        emoneyPdpViewModel.setSelectedProduct(product)

        //then
        assert(emoneyPdpViewModel.selectedProduct.value != null)
        assert(emoneyPdpViewModel.selectedProduct.value?.id.equals(id))
    }

    @Test
    fun setSelectedRecentNumber_shouldUpdateSelectedRecentNumber() {
        //given
        val catalogId = "12344"
        val productId = "1222"
        val clientNumber = "1234566789"
        val topUpBillsRecommendation = TopupBillsRecommendation(categoryId = catalogId.toIntOrZero(),
                productId = productId.toIntOrZero(), clientNumber = clientNumber)

        //when
        emoneyPdpViewModel.setSelectedRecentNumber(topUpBillsRecommendation)

        //then
        assert(emoneyPdpViewModel.selectedRecentNumber.value != null)
        assert(emoneyPdpViewModel.selectedRecentNumber.value?.categoryId == catalogId.toIntOrNull())
        assert(emoneyPdpViewModel.selectedRecentNumber.value?.productId == productId.toIntOrNull())
        assert(emoneyPdpViewModel.selectedRecentNumber.value?.clientNumber?.equals(clientNumber)
                ?: false)
    }

    @Test
    fun generateCheckoutPassData_withProductCatalogId_shouldGenerateAndUpdateCorrectPassData() {
        //given
        val copiedPromo = "QATOPED"
        val clientNumber = "1234566789"
        val productId = "1222"
        val operatorId = "14141414"
        val userId = "12345"
        coEvery { userSession.userId } coAnswers { userId }

        //when
        emoneyPdpViewModel.generateCheckoutPassData(copiedPromo, clientNumber, productId, operatorId)

        //then
        assert(emoneyPdpViewModel.digitalCheckoutPassData.idemPotencyKey?.isNotEmpty() ?: false)
        assert(emoneyPdpViewModel.digitalCheckoutPassData.voucherCodeCopied.equals(copiedPromo))
        assert(emoneyPdpViewModel.digitalCheckoutPassData.clientNumber.equals(clientNumber))
        assert(emoneyPdpViewModel.digitalCheckoutPassData.productId.equals(productId))
        assert(emoneyPdpViewModel.digitalCheckoutPassData.operatorId.equals(operatorId))
        assert(emoneyPdpViewModel.digitalCheckoutPassData.isFromPDP)
    }

    @Test
    fun generateCheckoutPassData_withoutProductCatalogId_shouldGenerateAndUpdateCorrectPassData() {
        //given
        val copiedPromo = "QATOPED"
        val clientNumber = "1234566789"
        val userId = "12345"
        coEvery { userSession.userId } coAnswers { userId }
        getSelectedOperator_onInputNumberValid_shouldUpdateSelectedOperator()
        setSelectedProduct_shouldUpdateSelectedProduct()

        //when
        emoneyPdpViewModel.generateCheckoutPassData(copiedPromo, clientNumber)

        //then
        assert(emoneyPdpViewModel.digitalCheckoutPassData.idemPotencyKey?.isNotEmpty() ?: false)
        assert(emoneyPdpViewModel.digitalCheckoutPassData.voucherCodeCopied.equals(copiedPromo))
        assert(emoneyPdpViewModel.digitalCheckoutPassData.clientNumber.equals(clientNumber))
        assert(emoneyPdpViewModel.digitalCheckoutPassData.productId.equals("12344"))
        assert(emoneyPdpViewModel.digitalCheckoutPassData.operatorId.equals("2"))
        assert(emoneyPdpViewModel.digitalCheckoutPassData.isFromPDP)
    }

}