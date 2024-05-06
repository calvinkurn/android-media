package com.tokopedia.recharge_pdp_emoney.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.data.prefix_select.RechargeCatalogPrefixSelect
import com.tokopedia.common.topupbills.data.prefix_select.RechargePrefix
import com.tokopedia.common.topupbills.data.prefix_select.TelcoAttributesOperator
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.data.prefix_select.TelcoOperator
import com.tokopedia.common.topupbills.data.product.CatalogData
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.common.topupbills.data.product.CatalogProductData
import com.tokopedia.common.topupbills.favoritecommon.data.TopupBillsPersoFavNumber
import com.tokopedia.common.topupbills.favoritecommon.data.TopupBillsPersoFavNumberData
import com.tokopedia.common.topupbills.favoritecommon.data.TopupBillsPersoFavNumberItem
import com.tokopedia.common.topupbills.usecase.GetBCAGenCheckerUseCase
import com.tokopedia.common.topupbills.usecase.RechargeCatalogPrefixSelectUseCase
import com.tokopedia.common.topupbills.usecase.RechargeCatalogProductInputUseCase
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.presentation.model.DigiPersoRecommendationData
import com.tokopedia.common_digital.common.presentation.model.DigiPersoRecommendationItem
import com.tokopedia.common_digital.common.presentation.model.DigitalDppoConsent
import com.tokopedia.common_digital.common.usecase.GetDppoConsentUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertNull
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

/**
 * @author by jessica on 15/04/21
 */
class EmoneyPdpViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = UnconfinedTestRule()

    lateinit var emoneyPdpViewModel: EmoneyPdpViewModel

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var rechargeCatalogPrefixUseCase: RechargeCatalogPrefixSelectUseCase

    @RelaxedMockK
    lateinit var rechargeCatalogProductInputUseCase: RechargeCatalogProductInputUseCase

    @RelaxedMockK
    lateinit var getDppoConsentUseCase: GetDppoConsentUseCase

    @RelaxedMockK
    lateinit var getBCAGenCheckerUseCase: GetBCAGenCheckerUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        emoneyPdpViewModel = EmoneyPdpViewModel(
            userSession,
            rechargeCatalogPrefixUseCase,
            rechargeCatalogProductInputUseCase,
            getDppoConsentUseCase,
            getBCAGenCheckerUseCase
        )
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
        val topUpBillsRecommendation = TopupBillsRecommendation(categoryId = catalogId,
                productId = productId, clientNumber = clientNumber)

        //when
        emoneyPdpViewModel.setSelectedRecentNumber(topUpBillsRecommendation)

        //then
        assert(emoneyPdpViewModel.selectedRecentNumber.value != null)
        assert(emoneyPdpViewModel.selectedRecentNumber.value?.categoryId == catalogId)
        assert(emoneyPdpViewModel.selectedRecentNumber.value?.productId == productId)
        assert(emoneyPdpViewModel.selectedRecentNumber.value?.clientNumber?.equals(clientNumber)
                ?: false)
    }

    @Test
    fun setDigitalCheckoutPassData_shouldUpdateDigitalCheckoutPassData() {
        //given
        val categoryId = "34"
        val productId = "1222"
        val clientNumber = "1234566789"
        //when
        emoneyPdpViewModel.digitalCheckoutPassData = DigitalCheckoutPassData().apply {
            this.clientNumber = clientNumber
            this.productId = productId
            this.categoryId = categoryId
        }

        assert(emoneyPdpViewModel.digitalCheckoutPassData.clientNumber.equals(clientNumber))
        assert(emoneyPdpViewModel.digitalCheckoutPassData.productId.equals(productId))
        assert(emoneyPdpViewModel.digitalCheckoutPassData.categoryId.equals(categoryId))
    }

    @Test
    fun generateCheckoutPassData_withProductCatalogId_shouldGenerateAndUpdateCorrectPassData() {
        //given
        val copiedPromo = "QATOPED"
        val clientNumber = "1234566789"
        val productId = "1222"
        val operatorId = "14141414"
        val userId = "12345"
        val categoryId = "34"
        coEvery { userSession.userId } coAnswers { userId }

        //when
        emoneyPdpViewModel.generateCheckoutPassData(copiedPromo, clientNumber, productId, operatorId, categoryId)

        //then
        assert(emoneyPdpViewModel.digitalCheckoutPassData.idemPotencyKey?.isNotEmpty() ?: false)
        assert(emoneyPdpViewModel.digitalCheckoutPassData.voucherCodeCopied.equals(copiedPromo))
        assert(emoneyPdpViewModel.digitalCheckoutPassData.clientNumber.equals(clientNumber))
        assert(emoneyPdpViewModel.digitalCheckoutPassData.productId.equals(productId))
        assert(emoneyPdpViewModel.digitalCheckoutPassData.operatorId.equals(operatorId))
        assert(emoneyPdpViewModel.digitalCheckoutPassData.categoryId.equals(categoryId))
        assert(emoneyPdpViewModel.digitalCheckoutPassData.isFromPDP)
    }

    @Test
    fun generateCheckoutPassData_withoutProductCatalogId_shouldGenerateAndUpdateCorrectPassData() {
        //given
        val copiedPromo = "QATOPED"
        val clientNumber = "1234566789"
        val userId = "12345"
        val categoryId = "34"
        coEvery { userSession.userId } coAnswers { userId }
        getSelectedOperator_onInputNumberValid_shouldUpdateSelectedOperator()
        setSelectedProduct_shouldUpdateSelectedProduct()

        //when
        emoneyPdpViewModel.generateCheckoutPassData(copiedPromo, clientNumber, categoryIdFromPDP = categoryId)

        //then
        assert(emoneyPdpViewModel.digitalCheckoutPassData.idemPotencyKey?.isNotEmpty() ?: false)
        assert(emoneyPdpViewModel.digitalCheckoutPassData.voucherCodeCopied.equals(copiedPromo))
        assert(emoneyPdpViewModel.digitalCheckoutPassData.clientNumber.equals(clientNumber))
        assert(emoneyPdpViewModel.digitalCheckoutPassData.productId.equals("12344"))
        assert(emoneyPdpViewModel.digitalCheckoutPassData.operatorId.equals("2"))
        assert(emoneyPdpViewModel.digitalCheckoutPassData.categoryId.equals(categoryId))
        assert(emoneyPdpViewModel.digitalCheckoutPassData.isFromPDP)
    }

    @Test
    fun generateCheckoutPassData_nullSelectProduct_shouldGenerateWrongPassData() {
        //given
        val copiedPromo = "QATOPED"
        val clientNumber = "1234566789"
        val userId = "12345"
        val categoryId = "34"
        coEvery { userSession.userId } coAnswers { userId }

        //when
        emoneyPdpViewModel.generateCheckoutPassData(copiedPromo, clientNumber, categoryIdFromPDP = categoryId)

        //then
        assert(emoneyPdpViewModel.digitalCheckoutPassData.idemPotencyKey?.isNotEmpty() ?: false)
        assert(emoneyPdpViewModel.digitalCheckoutPassData.voucherCodeCopied.equals(copiedPromo))
        assert(emoneyPdpViewModel.digitalCheckoutPassData.clientNumber.equals(clientNumber))
        assertNull(emoneyPdpViewModel.digitalCheckoutPassData.productId)
        assertNull(emoneyPdpViewModel.digitalCheckoutPassData.operatorId)
        assert(emoneyPdpViewModel.digitalCheckoutPassData.categoryId.equals(categoryId))
        assert(emoneyPdpViewModel.digitalCheckoutPassData.isFromPDP)
    }

    @Test
    fun getDppoConsentRecharge_Success() {
        // given
        val consentDesc = "Tokopedia"
        val digitalDPPOConsent = DigitalDppoConsent(
            DigiPersoRecommendationData(
                items = listOf(
                    DigiPersoRecommendationItem(
                        id = "1",
                        title = consentDesc
                    )
                )
            )
        )

        coEvery { getDppoConsentUseCase.execute(any()) } returns digitalDPPOConsent

        // when
        emoneyPdpViewModel.getDppoConsent()

        // then
        val actualData = emoneyPdpViewModel.dppoConsent.value
        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is Success)
        Assert.assertTrue((actualData as Success).data.description == consentDesc)
    }

    @Test
    fun getDppoConsentRecharge_Fail() {
        // given
        val errorMessage = "Tokopedia"
        coEvery { getDppoConsentUseCase.execute(any()) } throws MessageErrorException(errorMessage)

        // when
        emoneyPdpViewModel.getDppoConsent()

        // then
        val actualData = emoneyPdpViewModel.dppoConsent.value
        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is Fail)
        Assert.assertTrue((actualData as Fail).throwable.message == errorMessage)
    }

    @Test
    fun getBCAGenCheck_Success() {
        // given
        val messsage = "Message"
        val bcaGenCheck = TopupBillsPersoFavNumberData(
            persoFavoriteNumber = TopupBillsPersoFavNumber(
                items = listOf(
                    TopupBillsPersoFavNumberItem(
                        label1 = "1",
                        label2 = messsage
                    )
                )
            )
        )
        coEvery { getBCAGenCheckerUseCase.execute(listOf("085327499272")) } returns bcaGenCheck

        // when
        emoneyPdpViewModel.getBCAGenCheck("085327499272")

        // then
        val actualData = emoneyPdpViewModel.bcaGenCheckerResult.value
        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is Success)
        Assert.assertTrue((actualData as Success).data.isGenOne)
        Assert.assertTrue((actualData as Success).data.message == messsage)
    }

    @Test
    fun getBCAGenCheck_Fail() {
        // given
        val messsage = "Message"
        coEvery { getBCAGenCheckerUseCase.execute(listOf("085327499272")) } throws MessageErrorException(messsage)

        // when
        emoneyPdpViewModel.getBCAGenCheck("085327499272")

        // then
        val actualData = emoneyPdpViewModel.bcaGenCheckerResult.value
        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is Fail)
        Assert.assertTrue((actualData as Fail).throwable.message == messsage)
    }

    @Test
    fun getBCAGenCheck_FailCancel() {
        // given
        val messsage = "Message"
        coEvery { getBCAGenCheckerUseCase.execute(listOf("085327499272")) } throws CancellationException(messsage)

        // when
        emoneyPdpViewModel.getBCAGenCheck("085327499272")

        // then
        val actualData = emoneyPdpViewModel.bcaGenCheckerResult.value
        Assert.assertNull(actualData)
    }

    @Test
    fun getIsSelectedOperatorIdBCA_True(){
        //given
        val prefixes = listOf(
            RechargePrefix(
                "578",
                "014520",
                operator = TelcoOperator(
                    "78",
                    attributes = TelcoAttributesOperator(
                        name = "BCA"
                    )
                )
            )
        )
        val response = TelcoCatalogPrefixSelect(RechargeCatalogPrefixSelect(prefixes = prefixes))
        coEvery { rechargeCatalogPrefixUseCase.execute(any(), any(), any()) } coAnswers {
            secondArg<(TelcoCatalogPrefixSelect) -> Unit>().invoke(response)
        }
        //when
        emoneyPdpViewModel.getPrefixOperator(0)
        emoneyPdpViewModel.getSelectedOperator("0145201100001171", "")
        val isBCA = emoneyPdpViewModel.selectedOperatorIsBCA()
        //then
        assert(isBCA)
    }

    @Test
    fun cancellationBCACheckGen() {
        // given
        val messsage = "Message"
        val bcaGenCheck = TopupBillsPersoFavNumberData(
            persoFavoriteNumber = TopupBillsPersoFavNumber(
                items = listOf(
                    TopupBillsPersoFavNumberItem(
                        label1 = "1",
                        label2 = messsage
                    )
                )
            )
        )
        coEvery { getBCAGenCheckerUseCase.execute(listOf("085327499272")) } returns bcaGenCheck

        // when
        emoneyPdpViewModel.getBCAGenCheck("085327499272")

        // then
        val actualData = emoneyPdpViewModel.bcaGenCheckerResult.value
        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is Success)
        Assert.assertTrue((actualData as Success).data.isGenOne)
        Assert.assertTrue((actualData as Success).data.message == messsage)
        assertNotNull(emoneyPdpViewModel.bcaCheckGenJob)
        emoneyPdpViewModel.cancelBCACheckGenJob()
        emoneyPdpViewModel.bcaCheckGenJob?.let {
            assert(it.isCompleted)
        }
    }

    @Test
    fun cancellationBCACheckGenNull() {
        emoneyPdpViewModel.bcaCheckGenJob = null
        emoneyPdpViewModel.cancelBCACheckGenJob()
        assertNull(emoneyPdpViewModel.bcaCheckGenJob)
    }

}
