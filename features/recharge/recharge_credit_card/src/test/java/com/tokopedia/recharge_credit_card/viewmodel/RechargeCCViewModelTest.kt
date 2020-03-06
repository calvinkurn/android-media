package com.tokopedia.recharge_credit_card.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCBank
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCBankList
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCBankListReponse
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

    @Test
    fun getListBank_ErrorMessageEmpty_SuccessGetListBank() {
        //given
        val bankList = mutableListOf<RechargeCCBank>()
        bankList.add(RechargeCCBank("CITIBANK", "https://ecs7.tokopedia.net/img/recharge/operator/esia.png"))
        bankList.add(RechargeCCBank("VISA", "https://ecs7.tokopedia.net/img/attachment/2019/10/8/8966534/8966534_172ecb33-180b-422a-b694-ffeb82ec95b6.png"))

        val gqlResponse = GraphqlResponse(
                mapOf(RechargeCCBankListReponse::class.java to
                        RechargeCCBankListReponse(RechargeCCBankList(signature = "abcd", bankList = bankList))),
                mapOf(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        rechargeCCViewModel.getListBank("", "")

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
                        RechargeCCBankListReponse(RechargeCCBankList(signature = "abcd", bankList = mutableListOf(), messageError = errorMessage))),
                mapOf(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        rechargeCCViewModel.getListBank("", "")

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
        val gqlResponseRefreshTokenFailed = GraphqlResponse(
                mapOf(), mapOf(RechargeCCBankListReponse::class.java to listOf(errorGql)), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseRefreshTokenFailed
        //when
        rechargeCCViewModel.getListBank("", "")

        //then
        val actualData = rechargeCCViewModel.errorCCBankList
        Assert.assertNotNull(actualData)
        Assert.assertEquals(errorGql.message, actualData.value)
    }
}