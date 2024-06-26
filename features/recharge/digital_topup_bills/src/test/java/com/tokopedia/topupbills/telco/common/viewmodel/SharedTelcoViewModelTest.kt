package com.tokopedia.topupbills.telco.common.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.topupbills.data.TopupBillsContact
import com.tokopedia.common.topupbills.data.TopupBillsPromo
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.data.prefix_select.*
import com.tokopedia.common.topupbills.data.source.ContactDataSource
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

class SharedTelcoViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var telcoViewModel: SharedTelcoViewModel

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    @RelaxedMockK
    lateinit var contactDataSource: ContactDataSource

    @RelaxedMockK
    lateinit var remoteConfig: RemoteConfig

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        telcoViewModel = SharedTelcoViewModel(graphqlRepository, contactDataSource, remoteConfig, Dispatchers.Unconfined)
    }

    @Test
    fun setPromoImpression() {
        // given

        // when
        telcoViewModel.setPromoImpression()

        // then
        assert(telcoViewModel.promoImpression.value is Unit)
    }

    @Test
    fun setRecentsImpression() {
        // given

        // when
        telcoViewModel.setRecentsImpression()

        // then
        assert(telcoViewModel.recentsImpression.value is Unit)
    }

    @Test
    fun setRecommendationTelco_dataValid() {
        // given
        val listRecommendation = ArrayList<TopupBillsRecommendation>()
        listRecommendation.add(TopupBillsRecommendation(clientNumber = "0875343243434"))
        // when
        telcoViewModel.setRecommendationTelco(listRecommendation)
        // then
        assertEquals(listRecommendation[0].clientNumber, telcoViewModel.recommendations.value?.get(0)?.clientNumber)
    }

    @Test
    fun setPromoTelco_dataValid() {
        // given
        val listPromo = ArrayList<TopupBillsPromo>()
        listPromo.add(TopupBillsPromo(title = "test promo telco"))
        // when
        telcoViewModel.setPromoTelco(listPromo)
        // then
        assertEquals(listPromo[0].title, telcoViewModel.promos.value?.get(0)?.title)
    }

    @Test
    fun setSelectedRecentNumber_dataValid() {
        // given
        val selectedRecent = TopupBillsRecommendation(clientNumber = "0875343243434")
        // when
        telcoViewModel.setSelectedRecentNumber(selectedRecent)
        // then
        assertEquals(selectedRecent.clientNumber, telcoViewModel.selectedRecentNumber.value?.clientNumber)
    }

    @Test
    fun setTitleMenu_dataValid() {
        // given
        val showMenu = true
        // when
        telcoViewModel.setTitleMenu(showMenu)
        // then
        assertEquals(showMenu, telcoViewModel.titleMenu.value)
    }

    @Test
    fun getPrefixOperator_DataValid_SuccessGetData() {
        // given
        val listPrefixes = mutableListOf<RechargePrefix>()
        listPrefixes.add(RechargePrefix(operator = TelcoOperator(attributes = TelcoAttributesOperator(name = "simpati"))))
        val catalogPrefixSelect = TelcoCatalogPrefixSelect(RechargeCatalogPrefixSelect(prefixes = listPrefixes))

        val result = HashMap<Type, Any>()
        result[TelcoCatalogPrefixSelect::class.java] = catalogPrefixSelect
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse
        coEvery { remoteConfig.getBoolean(RemoteConfigKey.ANDROID_ENABLE_DIGITAL_GQL_CACHE, false) } returns true

        // when
        telcoViewModel.getPrefixOperator("", 2)
        Thread.sleep(1000)

        // then
        val actualData = telcoViewModel.catalogPrefixSelect.value
        assertNotNull(actualData)
        assert(actualData is Success)
        val prefixSelect = (actualData as Success).data.rechargeCatalogPrefixSelect.prefixes
        assertEquals(listPrefixes, prefixSelect)
    }

    @Test
    fun getPrefixOperator_DataValid_FailedGetData() {
        // given
        val errorGql = GraphqlError()
        errorGql.message = "Error gql"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[TelcoCatalogPrefixSelect::class.java] = listOf(errorGql)
        val gqlResponse = GraphqlResponse(HashMap<Type, Any>(), errors, false)
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        // when
        telcoViewModel.getPrefixOperator("", 2)

        // then
        val actualData = telcoViewModel.catalogPrefixSelect.value
        assertNotNull(actualData)
        assert(actualData is Fail)
        val error = (actualData as Fail).throwable
        assertEquals(errorGql.message, error.message)
    }

    @Test
    fun getContactList_returnsCorrectContactData() {
        val fakeContacts = mutableListOf(
            TopupBillsContact("Tokopedia", "081234567890"),
            TopupBillsContact("GoTo", "085600001111")
        )

        every { contactDataSource.getContactList() } returns fakeContacts

        val actualContacts = telcoViewModel.getContactList()

        verify { contactDataSource.getContactList() }

        Assert.assertEquals(fakeContacts, actualContacts)
    }
}
