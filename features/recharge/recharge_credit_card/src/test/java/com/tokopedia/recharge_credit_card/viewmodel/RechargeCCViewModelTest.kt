package com.tokopedia.recharge_credit_card.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.topupbills.favoritecommon.data.TopupBillsPersoFavNumber
import com.tokopedia.common.topupbills.favoritecommon.data.TopupBillsPersoFavNumberData
import com.tokopedia.common.topupbills.favoritepdp.domain.model.AutoCompleteModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.FavoriteChipModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.FavoriteGroupModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.PrefillModel
import com.tokopedia.common.topupbills.favoritepdp.domain.repository.RechargeFavoriteNumberRepository
import com.tokopedia.common.topupbills.favoritepdp.util.FavoriteNumberType
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import com.tokopedia.recharge_credit_card.datamodel.CatalogOperator
import com.tokopedia.recharge_credit_card.datamodel.CatalogPrefixAttributes
import com.tokopedia.recharge_credit_card.datamodel.CatalogPrefixSelect
import com.tokopedia.recharge_credit_card.datamodel.CatalogPrefixs
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCBank
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCBankList
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCBankListReponse
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCCatalogPrefix
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCMenuDetail
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCMenuDetailResponse
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCUserPerso
import com.tokopedia.recharge_credit_card.datamodel.RechargeCreditCard
import com.tokopedia.recharge_credit_card.datamodel.TickerCreditCard
import com.tokopedia.recharge_credit_card.datamodel.Validation
import com.tokopedia.recharge_credit_card.util.RechargeCCConst
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Job
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

class RechargeCCViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = CoroutineTestRule()

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    @RelaxedMockK
    lateinit var rechargeFavoriteNumberRepo: RechargeFavoriteNumberRepository

    lateinit var rechargeCCViewModel: RechargeCCViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        rechargeCCViewModel = RechargeCCViewModel(
            graphqlRepository,
            testCoroutineRule.dispatchers.coroutineDispatcher,
            rechargeFavoriteNumberRepo
        )
    }

    //========================================= Bank List ===================================

    @Test
    fun getListBank_ErrorMessageEmpty_SuccessGetListBank() {
        //given
        val bankList = getMockBankList()

        val result = HashMap<Type, Any>()
        result[RechargeCCBankListReponse::class.java] = RechargeCCBankListReponse(RechargeCCBankList(bankList = getMockBankList()))
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        //when
        rechargeCCViewModel.getListBank("", 0)

        //then
        val actualData = rechargeCCViewModel.rechargeCCBankList
        Assert.assertNotNull(actualData)
        Assert.assertEquals(bankList, actualData.value?.bankList)
    }

    @Test
    fun getListBank_ErrorMessageIsNotEmpty_FailedGetListBank() {
        //given
        val errorMessage = "Maaf terjadi kendala teknis"

        val result = HashMap<Type, Any>()
        result[RechargeCCBankListReponse::class.java] = RechargeCCBankListReponse(RechargeCCBankList(bankList = mutableListOf(), messageError = errorMessage))
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        //when
        rechargeCCViewModel.getListBank("", 0)

        //then
        val actualData = rechargeCCViewModel.errorCCBankList
        Assert.assertNotNull(actualData)
        Assert.assertEquals(errorMessage, actualData.value?.message)
    }

    @Test
    fun getListBank_ErrorGetApi_FailedGetListBank() {
        //given
        val errorGql = GraphqlError()
        errorGql.message = "Error gql"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[RechargeCCBankListReponse::class.java] = listOf(errorGql)
        val gqlResponse = GraphqlResponse(HashMap<Type, Any?>(), errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse
        //when
        rechargeCCViewModel.getListBank("", 0)

        //then
        val actualData = rechargeCCViewModel.errorCCBankList
        Assert.assertNotNull(actualData)
        Assert.assertEquals(errorGql.message, actualData.value?.message)
    }

    //========================================= MENU DETAIL ================================

    @Test
    fun getMenuDetail_TickersNotEmpty_SuccessGetTicker() {
        //given
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

        //when
        rechargeCCViewModel.getMenuDetail("", "169")

        //then
        val actualData = rechargeCCViewModel.tickers
        Assert.assertNotNull(actualData)
        Assert.assertEquals(tickers, actualData.value)

        Assert.assertEquals(loyaltyStatus, rechargeCCViewModel.loyaltyStatus)
        Assert.assertEquals(menuName, rechargeCCViewModel.categoryName)
    }

    @Test
    fun getMenuDetail_TickerEmpty_FailedGetTicker() {
        //given
        val tickers = mutableListOf<TickerCreditCard>()

        val result = HashMap<Type, Any>()
        result[RechargeCCMenuDetailResponse::class.java] = RechargeCCMenuDetailResponse(
            RechargeCCMenuDetail(tickers = tickers)
        )
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        //when
        rechargeCCViewModel.getMenuDetail("", "169")

        //then
        val actualData = rechargeCCViewModel.tickers
        Assert.assertNotNull(actualData)
        Assert.assertEquals(tickers, actualData.value)
    }

    @Test
    fun getMenuDetail_ErrorGetApi_FailedGetTicker() {
        //given
        val errorGql = GraphqlError()
        errorGql.message = "Error gql"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[RechargeCCMenuDetail::class.java] = listOf(errorGql)
        val gqlResponse = GraphqlResponse(HashMap<Type, Any?>(), errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        //when
        rechargeCCViewModel.getMenuDetail("", "169")

        //then
        val actualData = rechargeCCViewModel.tickers
        Assert.assertNull(actualData.value)
    }

    //========================================= PREFIX =====================================

    @Test
    fun checkPrefixNumber_PrefixesNotEmpty_SuccessGetPrefixMatch() {
        //given
        val prefixes = getMockPrefixes()
        val validations = getMockValidations()
        val rechargeCCSelected = RechargeCreditCard("13", "15", "image2")

        rechargeCCViewModel.prefixData = RechargeCCCatalogPrefix(
            CatalogPrefixSelect(
            prefixes = prefixes,
            validations = validations
            )
        )

        //when
        rechargeCCViewModel.checkPrefixNumber(VALID_CC_NUMBER)

        //then
        val actualData = rechargeCCViewModel.creditCardSelected
        Assert.assertNotNull(actualData)
        Assert.assertEquals(rechargeCCSelected.defaultProductId, actualData.value?.defaultProductId)
        Assert.assertEquals(rechargeCCSelected.operatorId, actualData.value?.operatorId)
        Assert.assertEquals(rechargeCCSelected.imageUrl, actualData.value?.imageUrl)

        val actualRules = rechargeCCViewModel.prefixData.prefixSelect.validations
        Assert.assertNotNull(actualRules)
        Assert.assertEquals(validations.size, actualRules.size)
        Assert.assertEquals(validations[0].message, actualRules[0].message)
        Assert.assertEquals(validations[0].title, actualRules[0].title)
        Assert.assertEquals(validations[0].rule, actualRules[0].rule)
        Assert.assertEquals(validations[1].message, actualRules[1].message)
        Assert.assertEquals(validations[1].title, actualRules[1].title)
        Assert.assertEquals(validations[1].rule, actualRules[1].rule)
    }

    @Test
    fun checkPrefixNumber_PrefixNotFound_ReturnEmptyResult() {
        //given
        val prefixes = getMockPrefixes()
        val validations = getMockValidations()

        rechargeCCViewModel.prefixData = RechargeCCCatalogPrefix(
            CatalogPrefixSelect(
                prefixes = prefixes,
                validations = validations
            )
        )

        //when
        rechargeCCViewModel.checkPrefixNumber(INVALID_CC_NUMBER)

        //then
        val actualData = rechargeCCViewModel.creditCardSelected
        Assert.assertNotNull(actualData.value)
        Assert.assertEquals("", actualData.value?.defaultProductId)
        Assert.assertEquals("", actualData.value?.operatorId)
        Assert.assertEquals("", actualData.value?.imageUrl)
    }

    @Test
    fun checkPrefixNumber_PrefixEmpty_ReturnEmptyResult() {
        //given
        val prefixes = listOf<CatalogPrefixs>()
        val validations = getMockValidations()

        rechargeCCViewModel.prefixData = RechargeCCCatalogPrefix(
            CatalogPrefixSelect(
                prefixes = prefixes,
                validations = validations
            )
        )

        //when
        rechargeCCViewModel.checkPrefixNumber(INVALID_CC_NUMBER)

        //then
        val actualData = rechargeCCViewModel.creditCardSelected
        Assert.assertNotNull(actualData.value)
        Assert.assertEquals("", actualData.value?.defaultProductId)
        Assert.assertEquals("", actualData.value?.operatorId)
        Assert.assertEquals("", actualData.value?.imageUrl)
    }

    @Test
    fun getPrefix_ErrorGetApi_FailedGetPrefix() {
        //given
        val result = HashMap<Type, Any>()
        result[RechargeCCCatalogPrefix::class.java] = RechargeCCCatalogPrefix()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse
        //when
        rechargeCCViewModel.getPrefixes("", "169")

        //then
        val actualData = rechargeCCViewModel.prefixSelect
        Assert.assertNotNull(actualData)
        Assert.assertEquals(RechargeCCCatalogPrefix(), (actualData.value as RechargeNetworkResult.Success).data)
    }

    @Test
    fun getPrefix_SuccessGetPrefix() {
        //given
        val errorGql = GraphqlError()
        errorGql.message = "Error gql"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[RechargeCCCatalogPrefix::class.java] = listOf(errorGql)
        val gqlResponse = GraphqlResponse(HashMap<Type, Any?>(), errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse
        //when
        rechargeCCViewModel.getPrefixes("", "169")

        //then
        val actualData = rechargeCCViewModel.prefixSelect
        Assert.assertNotNull(actualData)
        Assert.assertEquals("Error gql", (actualData.value as RechargeNetworkResult.Fail).error.message)
    }

    @Test
    fun validateCCNumber_GivenValidCC_ReturnTrue() {
        rechargeCCViewModel.prefixData = RechargeCCCatalogPrefix(
            CatalogPrefixSelect(
                prefixes = getMockPrefixes(),
                validations = getMockValidations()
            )
        )
        testCoroutineRule.runBlockingTest {
            val clientNumber = VALID_CC_NUMBER
            rechargeCCViewModel.validateCCNumber(clientNumber)
            advanceTimeBy(RechargeCCConst.VALIDATOR_DELAY_TIME)

            val actualData = rechargeCCViewModel.prefixValidation
            Assert.assertNotNull(actualData)
            Assert.assertTrue(actualData.value == true)
        }
    }

    @Test
    fun validateCCNumber_GivenMaskedCC_ReturnTrue() {
        rechargeCCViewModel.prefixData = RechargeCCCatalogPrefix(
            CatalogPrefixSelect(
                prefixes = getMockPrefixes(),
                validations = getMockValidations()
            )
        )
        testCoroutineRule.runBlockingTest {
            val clientNumber = MASKED_CC_NUMBER
            rechargeCCViewModel.validateCCNumber(clientNumber)

            val actualData = rechargeCCViewModel.prefixValidation
            Assert.assertNotNull(actualData)
            Assert.assertTrue(actualData.value == true)
        }
    }

    @Test
    fun cancelValidatorJob_GivenNonNullJob_ShouldCancelJob() {
        testCoroutineRule.runBlockingTest {
            rechargeCCViewModel.validateCCNumber(VALID_CC_NUMBER)
            rechargeCCViewModel.cancelValidatorJob()
            Assert.assertTrue(rechargeCCViewModel.validatorJob?.isCancelled == true)
        }
    }

    @Test
    fun cancelValidatorJob_GivenNullJob_JobStaysNull() {
        testCoroutineRule.runBlockingTest {
            rechargeCCViewModel.cancelValidatorJob()
            Assert.assertNull(rechargeCCViewModel.validatorJob)
        }
    }

    @Test
    fun setValidatorJob_GivenJobNull_ShouldReturnNonNullJob() {
        rechargeCCViewModel.validatorJob = Job()
        Assert.assertNotNull(rechargeCCViewModel.validatorJob)
    }

    //======================================= Favorite Number =====================================
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
            favoriteChips = listOf(FavoriteChipModel()),
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
            favoriteChips = listOf(FavoriteChipModel()),
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
            favoriteChips = listOf(FavoriteChipModel()),
        )

        coEvery {
            rechargeFavoriteNumberRepo.getFavoriteNumbers(any(), any())
        } throws MessageErrorException("")

        rechargeCCViewModel.getFavoriteNumbers(listOf(), favoriteNumberTypes)

        Assert.assertNull(rechargeCCViewModel.prefillData.value)
        Assert.assertNull(rechargeCCViewModel.autoCompleteData.value)
        Assert.assertNull(rechargeCCViewModel.favoriteChipsData.value)
    }

    //======================================= Mock Data ===========================================

    private fun getMockValidations(): List<Validation> {
        return listOf(
            Validation("Max 16 digits", "Maksimal 16 digits", "^\\\\d{0,16}\$"),
            Validation("Min 15 digits", "Minimal 15 digit", "^\\d{15,}$")
        )
    }

    private fun getMockPrefixes(): List<CatalogPrefixs> {
        val prefixes = mutableListOf<CatalogPrefixs>()
        prefixes.add(
            CatalogPrefixs("1", "1234",
                CatalogOperator("12", CatalogPrefixAttributes("image1", "14"))
            )
        )
        prefixes.add(
            CatalogPrefixs("2", "4111",
                CatalogOperator("13", CatalogPrefixAttributes("image2", "15"))
            )
        )

        return prefixes
    }

    private fun getMockTickers(): List<TickerCreditCard> {
        val tickers = mutableListOf<TickerCreditCard>()
        tickers.add(TickerCreditCard("1", "test1", "desc1", "info"))
        tickers.add(TickerCreditCard("2", "test2", "desc2", "warning"))

        return tickers
    }

    private fun getMockBankList() : List<RechargeCCBank> {
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
