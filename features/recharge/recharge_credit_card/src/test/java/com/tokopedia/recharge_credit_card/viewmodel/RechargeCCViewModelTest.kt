package com.tokopedia.recharge_credit_card.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.recharge_credit_card.datamodel.*
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RechargeCCViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    lateinit var rechargeCCViewModel: RechargeCCViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        rechargeCCViewModel = RechargeCCViewModel(graphqlRepository, Dispatchers.Unconfined)
    }

    //========================================= Bank List ===================================

    @Test
    fun getListBank_ErrorMessageEmpty_SuccessGetListBank() {
        //given
        val bankList = mutableListOf<RechargeCCBank>()
        bankList.add(RechargeCCBank("CITIBANK", "https://ecs7.tokopedia.net/img/recharge/operator/esia.png"))
        bankList.add(RechargeCCBank("VISA", "https://ecs7.tokopedia.net/img/attachment/2019/10/8/8966534/8966534_172ecb33-180b-422a-b694-ffeb82ec95b6.png"))

        val gqlResponse = GraphqlResponse(
                mapOf(RechargeCCBankListReponse::class.java to
                        RechargeCCBankListReponse(RechargeCCBankList(bankList = bankList))),
                mapOf(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

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
        val gqlResponse = GraphqlResponse(
                mapOf(RechargeCCBankListReponse::class.java to
                        RechargeCCBankListReponse(RechargeCCBankList(bankList = mutableListOf(), messageError = errorMessage))),
                mapOf(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        rechargeCCViewModel.getListBank("", 0)

        //then
        val actualData = rechargeCCViewModel.errorCCBankList
        Assert.assertNotNull(actualData)
        Assert.assertEquals(errorMessage, actualData.value)
    }

    @Test
    fun getListBank_ErrorGetApi_FailedGetListBank() {
        //given
        val errorGql = GraphqlError()
        errorGql.message = "Error gql"
        val gqlFailed = GraphqlResponse(
                mapOf(), mapOf(RechargeCCBankListReponse::class.java to listOf(errorGql)), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlFailed
        //when
        rechargeCCViewModel.getListBank("", 0)

        //then
        val actualData = rechargeCCViewModel.errorCCBankList
        Assert.assertNotNull(actualData)
        Assert.assertEquals(errorGql.message, actualData.value)
    }

    //========================================= MENU DETAIL ================================

    @Test
    fun getMenuDetail_TickersNotEmpty_SuccessGetTicker() {
        //given
        val tickers = mutableListOf<TickerCreditCard>()
        tickers.add(TickerCreditCard(1, "test1", "desc1", "info"))
        tickers.add(TickerCreditCard(2, "test2", "desc2", "warning"))
        val gqlResponse = GraphqlResponse(
                mapOf(RechargeCCMenuDetailResponse::class.java to
                        RechargeCCMenuDetailResponse(RechargeCCMenuDetail(tickers = tickers))),
                mapOf(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        rechargeCCViewModel.getMenuDetail("", "169")

        //then
        val actualData = rechargeCCViewModel.tickers
        Assert.assertNotNull(actualData)
        Assert.assertEquals(tickers, actualData.value)
    }

    @Test
    fun getMenuDetail_TickerEmpty_FailedGetTicker() {
        //given
        val tickers = mutableListOf<TickerCreditCard>()
        val gqlResponse = GraphqlResponse(
                mapOf(RechargeCCMenuDetailResponse::class.java to
                        RechargeCCMenuDetailResponse(RechargeCCMenuDetail(tickers = tickers))),
                mapOf(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        rechargeCCViewModel.getMenuDetail("", "169")

        //then
        val actualData = rechargeCCViewModel.tickers
        Assert.assertNotNull(actualData)
        Assert.assertEquals(tickers, actualData.value)
    }

    //========================================= PREFIX =====================================

    @Test
    fun getPrefix_PrefixesNotEmpty_SuccessGetPrefix() {
        //given
        val prefixes = mutableListOf<CatalogPrefixs>()
        prefixes.add(CatalogPrefixs("1", "1234",
                CatalogOperator(12, CatalogPrefixAttributes("image1", 14))))
        prefixes.add(CatalogPrefixs("2", "4567",
                CatalogOperator(13, CatalogPrefixAttributes("image2", 15))))

        val rechargeCCSelected = RechargeCreditCard(13, 15, "image2")

        val gqlResponse = GraphqlResponse(
                mapOf(RechargeCCCatalogPrefix::class.java to
                        RechargeCCCatalogPrefix(CatalogPrefixSelect(prefixes = prefixes))),
                mapOf(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        rechargeCCViewModel.getPrefixes("", "45678910", "169")

        //then
        val actualData = rechargeCCViewModel.creditCardSelected
        Assert.assertNotNull(actualData)
        Assert.assertEquals(rechargeCCSelected.defaultProductId, actualData.value?.defaultProductId)
        Assert.assertEquals(rechargeCCSelected.operatorId, actualData.value?.operatorId)
        Assert.assertEquals(rechargeCCSelected.imageUrl, actualData.value?.imageUrl)
    }

    @Test
    fun getPrefix_PrefixesEmpty_FailedBankNotSupported() {
        //given
        val prefixes = mutableListOf<CatalogPrefixs>()
        val gqlResponse = GraphqlResponse(
                mapOf(RechargeCCCatalogPrefix::class.java to
                        RechargeCCCatalogPrefix(CatalogPrefixSelect(prefixes = prefixes))),
                mapOf(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        rechargeCCViewModel.getPrefixes("", "45678910", "169")

        //then
        val actualData = rechargeCCViewModel.bankNotSupported
        Assert.assertNotNull(actualData)
        Assert.assertEquals("", actualData.value)
    }

    @Test
    fun getPrefix_PrefixNotFound_FailedBankNotSupported() {
        //given
        val prefixes = mutableListOf<CatalogPrefixs>()
        prefixes.add(CatalogPrefixs("1", "1234",
                CatalogOperator(12, CatalogPrefixAttributes("image1", 14))))
        prefixes.add(CatalogPrefixs("2", "4567",
                CatalogOperator(13, CatalogPrefixAttributes("image2", 15))))

        val rechargeCCSelected = RechargeCreditCard(13, 15, "image2")

        val gqlResponse = GraphqlResponse(
                mapOf(RechargeCCCatalogPrefix::class.java to
                        RechargeCCCatalogPrefix(CatalogPrefixSelect(prefixes = prefixes))),
                mapOf(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        rechargeCCViewModel.getPrefixes("", "", "169")

        //then
        val actualData = rechargeCCViewModel.bankNotSupported
        Assert.assertNotNull(actualData)
        Assert.assertEquals("", actualData.value)
    }

    @Test
    fun getPrefix_ErrorGetApi_FailedGetPrefix() {
        //given
        val errorGql = GraphqlError()
        errorGql.message = "Error gql"
        val gqlFailed = GraphqlResponse(
                mapOf(), mapOf(RechargeCCCatalogPrefix::class.java to listOf(errorGql)), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlFailed
        //when
        rechargeCCViewModel.getPrefixes("", "", "169")

        //then
        val actualData = rechargeCCViewModel.errorPrefix
        Assert.assertNotNull(actualData)
        Assert.assertEquals(errorGql.message, actualData.value)
    }
}