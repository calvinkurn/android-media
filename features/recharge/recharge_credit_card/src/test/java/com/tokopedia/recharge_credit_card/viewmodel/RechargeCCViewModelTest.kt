package com.tokopedia.recharge_credit_card.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.topupbills.favoritepdp.domain.model.AutoCompleteModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.FavoriteChipModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.FavoriteGroupModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.PrefillModel
import com.tokopedia.common.topupbills.favoritepdp.domain.repository.RechargeFavoriteNumberRepository
import com.tokopedia.common.topupbills.favoritepdp.util.FavoriteNumberType
import com.tokopedia.common_digital.common.presentation.model.DigiPersoRecommendationData
import com.tokopedia.common_digital.common.presentation.model.DigiPersoRecommendationItem
import com.tokopedia.common_digital.common.presentation.model.DigitalDppoConsent
import com.tokopedia.common_digital.common.usecase.GetDppoConsentUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.network.authentication.HEADER_X_TKPD_APP_VERSION
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCBank
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCBankList
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCBankListReponse
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCCatalogPrefix
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCMenuDetail
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCMenuDetailResponse
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCUserPerso
import com.tokopedia.recharge_credit_card.datamodel.TickerCreditCard
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

class RechargeCCViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = UnconfinedTestRule()

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    @RelaxedMockK
    lateinit var rechargeFavoriteNumberRepo: RechargeFavoriteNumberRepository

    @RelaxedMockK
    lateinit var dppoConsentUseCase: GetDppoConsentUseCase

    lateinit var rechargeCCViewModel: RechargeCCViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        rechargeCCViewModel = RechargeCCViewModel(
            graphqlRepository,
            testCoroutineRule.dispatchers.coroutineDispatcher,
            rechargeFavoriteNumberRepo,
            dppoConsentUseCase
        )
    }

    // ========================================= Bank List ===================================

    @Test
    fun getListBank_ErrorMessageEmpty_SuccessGetListBank() {
        // given
        val bankList = getMockBankList()

        val result = HashMap<Type, Any>()
        result[RechargeCCBankListReponse::class.java] = RechargeCCBankListReponse(RechargeCCBankList(bankList = getMockBankList()))
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        // when
        rechargeCCViewModel.getListBank("", 0)

        // then
        val actualData = rechargeCCViewModel.rechargeCCBankList
        Assert.assertNotNull(actualData)
        Assert.assertEquals(bankList, actualData.value?.bankList)
    }

    @Test
    fun getListBank_ErrorMessageIsNotEmpty_FailedGetListBank() {
        // given
        val errorMessage = "Maaf terjadi kendala teknis"

        val result = HashMap<Type, Any>()
        result[RechargeCCBankListReponse::class.java] = RechargeCCBankListReponse(RechargeCCBankList(bankList = mutableListOf(), messageError = errorMessage))
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        // when
        rechargeCCViewModel.getListBank("", 0)

        // then
        val actualData = rechargeCCViewModel.errorCCBankList
        Assert.assertNotNull(actualData)
        Assert.assertEquals(errorMessage, actualData.value?.message)
    }

    @Test
    fun getListBank_ErrorGetApi_FailedGetListBank() {
        // given
        val errorGql = GraphqlError()
        errorGql.message = "Error gql"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[RechargeCCBankListReponse::class.java] = listOf(errorGql)
        val gqlResponse = GraphqlResponse(HashMap<Type, Any?>(), errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse
        // when
        rechargeCCViewModel.getListBank("", 0)

        // then
        val actualData = rechargeCCViewModel.errorCCBankList
        Assert.assertNotNull(actualData)
        Assert.assertEquals(errorGql.message, actualData.value?.message)
    }

    // ========================================= MENU DETAIL ================================

    @Test
    fun getMenuDetail_TickersNotEmpty_SuccessGetTicker() {
        // given
        val tickers = getMockTickers()
        val loyaltyStatus = "tokopedia"
        val menuName = "Kartu Kredit"

        val result = HashMap<Type, Any>()
        result[RechargeCCMenuDetailResponse::class.java] = RechargeCCMenuDetailResponse(
            RechargeCCMenuDetail(
                menuName = menuName,
                tickers = tickers,
                userPerso = RechargeCCUserPerso(
                    loyaltyStatus = loyaltyStatus
                )
            )
        )
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        // when
        rechargeCCViewModel.getMenuDetail("", "169")

        // then
        val actualData = (rechargeCCViewModel.menuDetail.value as RechargeNetworkResult.Success).data
        Assert.assertNotNull(actualData)
        Assert.assertEquals(tickers, actualData.tickers)

        Assert.assertEquals(loyaltyStatus, rechargeCCViewModel.loyaltyStatus)
        Assert.assertEquals(menuName, rechargeCCViewModel.categoryName)
    }

    @Test
    fun getMenuDetail_TickerEmpty_FailedGetTicker() {
        // given
        val tickers = mutableListOf<TickerCreditCard>()

        val result = HashMap<Type, Any>()
        result[RechargeCCMenuDetailResponse::class.java] = RechargeCCMenuDetailResponse(
            RechargeCCMenuDetail(tickers = tickers)
        )
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        // when
        rechargeCCViewModel.getMenuDetail("", "169")

        // then
        val actualData = (rechargeCCViewModel.menuDetail.value as RechargeNetworkResult.Success).data
        Assert.assertNotNull(actualData)
        Assert.assertEquals(tickers, actualData.tickers)
    }

    @Test
    fun getMenuDetail_ErrorGetApi_FailedGetTicker() {
        // given
        val errorGql = GraphqlError()
        errorGql.message = "Error gql"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[RechargeCCMenuDetail::class.java] = listOf(errorGql)
        val gqlResponse = GraphqlResponse(HashMap<Type, Any?>(), errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        // when
        rechargeCCViewModel.getMenuDetail("", "169")

        // then
        val actualData = rechargeCCViewModel.menuDetail.value
        Assert.assertTrue(actualData is RechargeNetworkResult.Fail)
    }

    // ========================================= PREFIX =====================================

    @Test
    fun getPrefix_ErrorGetApi_FailedGetPrefix() {
        // given
        val result = HashMap<Type, Any>()
        result[RechargeCCCatalogPrefix::class.java] = RechargeCCCatalogPrefix()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse
        // when
        rechargeCCViewModel.getPrefixes("", "169")

        // then
        val actualData = rechargeCCViewModel.prefixSelect
        Assert.assertNotNull(actualData)
        Assert.assertEquals(RechargeCCCatalogPrefix(), (actualData.value as RechargeNetworkResult.Success).data)
    }

    @Test
    fun getPrefix_SuccessGetPrefix() {
        // given
        val errorGql = GraphqlError()
        errorGql.message = "Error gql"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[RechargeCCCatalogPrefix::class.java] = listOf(errorGql)
        val gqlResponse = GraphqlResponse(HashMap<Type, Any?>(), errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse
        // when
        rechargeCCViewModel.getPrefixes("", "169")

        // then
        val actualData = rechargeCCViewModel.prefixSelect
        Assert.assertNotNull(actualData)
        Assert.assertEquals("Error gql", (actualData.value as RechargeNetworkResult.Fail).error.message)
    }

    // ======================================= Favorite Number =====================================
    @Test
    fun setFavoriteNumberLoading_ShouldReturnChipsLoading() {
        rechargeCCViewModel.setFavoriteNumberLoading()

        val actualData = rechargeCCViewModel.favoriteChipsData
        Assert.assertEquals(RechargeNetworkResult.Loading, (actualData.value as RechargeNetworkResult.Loading))
    }

    @Test
    fun getFavoriteNumber_WithPrefill_ReturnSuccessResult() {
        val favoriteNumberTypes = listOf(
            FavoriteNumberType.CHIP,
            FavoriteNumberType.LIST,
            FavoriteNumberType.PREFILL
        )

        val favoriteGroupModel = FavoriteGroupModel(
            prefill = PrefillModel(),
            autoCompletes = listOf(AutoCompleteModel()),
            favoriteChips = listOf(FavoriteChipModel())
        )

        coEvery {
            rechargeFavoriteNumberRepo.getFavoriteNumbers(any(), any())
        } returns favoriteGroupModel

        rechargeCCViewModel.getFavoriteNumbers(listOf(), favoriteNumberTypes)

        Assert.assertNotNull(rechargeCCViewModel.prefillData.value)
        Assert.assertNotNull(rechargeCCViewModel.autoCompleteData.value)
        Assert.assertNotNull(rechargeCCViewModel.favoriteChipsData.value)

        Assert.assertEquals(
            PrefillModel(),
            (rechargeCCViewModel.prefillData.value as RechargeNetworkResult.Success).data
        )
        Assert.assertEquals(
            listOf(AutoCompleteModel()),
            (rechargeCCViewModel.autoCompleteData.value as RechargeNetworkResult.Success).data
        )
        Assert.assertEquals(
            listOf(FavoriteChipModel()),
            (rechargeCCViewModel.favoriteChipsData.value as RechargeNetworkResult.Success).data
        )
    }

    @Test
    fun getFavoriteNumber_WithoutPrefill_ReturnSuccessResultExceptPrefill() {
        val favoriteNumberTypes = listOf(
            FavoriteNumberType.CHIP,
            FavoriteNumberType.LIST
        )

        val favoriteGroupModel = FavoriteGroupModel(
            prefill = PrefillModel(),
            autoCompletes = listOf(AutoCompleteModel()),
            favoriteChips = listOf(FavoriteChipModel())
        )

        coEvery {
            rechargeFavoriteNumberRepo.getFavoriteNumbers(any(), any())
        } returns favoriteGroupModel

        rechargeCCViewModel.getFavoriteNumbers(listOf(), favoriteNumberTypes)

        Assert.assertNull(rechargeCCViewModel.prefillData.value)
        Assert.assertNotNull(rechargeCCViewModel.autoCompleteData.value)
        Assert.assertNotNull(rechargeCCViewModel.favoriteChipsData.value)

        Assert.assertEquals(
            listOf(AutoCompleteModel()),
            (rechargeCCViewModel.autoCompleteData.value as RechargeNetworkResult.Success).data
        )
        Assert.assertEquals(
            listOf(FavoriteChipModel()),
            (rechargeCCViewModel.favoriteChipsData.value as RechargeNetworkResult.Success).data
        )
    }

    @Test
    fun getFavoriteNumber_InvalidFavoriteNumberType_LiveDataShouldBeEmpty() {
        val favoriteNumberTypes = listOf(
            FavoriteNumberType.CHIP,
            FavoriteNumberType.LIST
        )

        val favoriteGroupModel = FavoriteGroupModel(
            prefill = PrefillModel(),
            autoCompletes = listOf(AutoCompleteModel()),
            favoriteChips = listOf(FavoriteChipModel())
        )

        coEvery {
            rechargeFavoriteNumberRepo.getFavoriteNumbers(any(), any())
        } returns favoriteGroupModel

        rechargeCCViewModel.getFavoriteNumbers(listOf(), favoriteNumberTypes)

        Assert.assertNull(rechargeCCViewModel.prefillData.value)
        Assert.assertNotNull(rechargeCCViewModel.autoCompleteData.value)
        Assert.assertNotNull(rechargeCCViewModel.favoriteChipsData.value)

        Assert.assertEquals(
            listOf(AutoCompleteModel()),
            (rechargeCCViewModel.autoCompleteData.value as RechargeNetworkResult.Success).data
        )
        Assert.assertEquals(
            listOf(FavoriteChipModel()),
            (rechargeCCViewModel.favoriteChipsData.value as RechargeNetworkResult.Success).data
        )
    }

    @Test
    fun getFavoriteNumber_ErrorGetApi_ShouldReturnNothing() {
        val favoriteNumberTypes = listOf(
            FavoriteNumberType.PREFILL,
            FavoriteNumberType.CHIP,
            FavoriteNumberType.LIST
        )

        val favoriteGroupModel = FavoriteGroupModel(
            prefill = PrefillModel(),
            autoCompletes = listOf(AutoCompleteModel()),
            favoriteChips = listOf(FavoriteChipModel())
        )

        coEvery {
            rechargeFavoriteNumberRepo.getFavoriteNumbers(any(), any())
        } throws MessageErrorException("")

        rechargeCCViewModel.getFavoriteNumbers(listOf(), favoriteNumberTypes)

        Assert.assertNull(rechargeCCViewModel.prefillData.value)
        Assert.assertNull(rechargeCCViewModel.autoCompleteData.value)
        Assert.assertNull(rechargeCCViewModel.favoriteChipsData.value)
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

        coEvery { dppoConsentUseCase.execute(any()) } returns digitalDPPOConsent

        // when
        rechargeCCViewModel.getDppoConsent(1)

        // then
        val actualData = rechargeCCViewModel.dppoConsent.value
        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is Success)
        Assert.assertTrue((actualData as Success).data.description == consentDesc)
    }

    @Test
    fun getDppoConsentRecharge_EmptyData() {
        // given
        val digitalDPPOConsent = DigitalDppoConsent(
            DigiPersoRecommendationData(emptyList())
        )

        coEvery { dppoConsentUseCase.execute(any()) } returns digitalDPPOConsent

        // when
        rechargeCCViewModel.getDppoConsent(1)

        // then
        val actualData = rechargeCCViewModel.dppoConsent.value
        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is Success)
        Assert.assertTrue((actualData as Success).data.description == "")
    }

    @Test
    fun getDppoConsentRecharge_Fail() {
        // given
        val errorMessage = "Tokopedia"
        coEvery { dppoConsentUseCase.execute(any()) } throws MessageErrorException(errorMessage)

        // when
        rechargeCCViewModel.getDppoConsent(1)

        // then
        val actualData = rechargeCCViewModel.dppoConsent.value
        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is Fail)
        Assert.assertTrue((actualData as Fail).throwable.message == errorMessage)
    }

    // ======================================= Util ===========================================
    @Test
    fun getPcidssCustomHeaders_shouldReturnCorrectFormat() {
        val customHeaders = rechargeCCViewModel.getPcidssCustomHeaders()
        val expectedPrefix = "android-"

        Assert.assertEquals(Int.ONE, customHeaders.size)
        Assert.assertNotNull(customHeaders[HEADER_X_TKPD_APP_VERSION])
        Assert.assertTrue(customHeaders[HEADER_X_TKPD_APP_VERSION]?.startsWith(expectedPrefix) == true)
    }

    // ======================================= Mock Data ===========================================

    private fun getMockTickers(): List<TickerCreditCard> {
        val tickers = mutableListOf<TickerCreditCard>()
        tickers.add(TickerCreditCard("1", "test1", "desc1", "info"))
        tickers.add(TickerCreditCard("2", "test2", "desc2", "warning"))

        return tickers
    }

    private fun getMockBankList(): List<RechargeCCBank> {
        val bankList = mutableListOf<RechargeCCBank>()
        bankList.add(RechargeCCBank("CITIBANK", "https://images.tokopedia.net/img/recharge/operator/esia.png"))
        bankList.add(RechargeCCBank("VISA", "https://images.tokopedia.net/img/attachment/2019/10/8/8966534/8966534_172ecb33-180b-422a-b694-ffeb82ec95b6.png"))

        return bankList
    }

    companion object {
        private const val VALID_CC_NUMBER = "4111111111111111"
        private const val INVALID_CC_NUMBER = "0000000000000000"
        private const val MASKED_CC_NUMBER = "411111******1111"
    }
}
