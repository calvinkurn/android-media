package com.tokopedia.tokopoints.view.cataloglisting

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.tokopoints.view.cataloglisting.CatalogListingRepository
import com.tokopedia.tokopoints.view.model.TokoPointDetailEntity
import com.tokopedia.tokopoints.view.model.ValidateCouponBaseEntity
import com.tokopedia.tokopoints.view.util.CommonConstant
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class CatalogListingRepositoryTest {
    lateinit var repository: CatalogListingRepository
    val map = mockk<Map<String, String>>()

    @Before
    fun setUp() {
        repository = CatalogListingRepository("tp_section_new",
                "tp_homepage_section",
                "tp_gql_reward",
                "tp_gql_usersaving", map)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getHomePageData() {
        runBlocking {
            val useCase = mockk<MultiRequestGraphqlUseCase>()
            repository.mGetHomePageData = useCase
            every { map[CommonConstant.GQLQuery.TP_GQL_TOKOPOINT_DETAIL] } returns "jhnvjdsnv"
            every { useCase.clearRequest() } just Runs
            every { useCase.addRequest(any()) } just Runs
            every { useCase.addRequest(any()) } just Runs
            every { useCase.addRequest(any()) } just Runs
            coEvery { useCase.executeOnBackground() } returns mockk()
            repository.getHomePageData("fvjn", "jvj", false)

            coVerify(ordering = Ordering.ORDERED) {
                useCase.clearRequest()
                useCase.addRequest(any())
                useCase.addRequest(any())
                useCase.addRequest(any())
                useCase.executeOnBackground()
            }
        }
    }

    @Test
    fun getPointData() {
        runBlocking {
            val useCase = mockk<MultiRequestGraphqlUseCase>()
            repository.mGetPointData = useCase
            every { map[CommonConstant.GQLQuery.TP_GQL_CURRENT_POINTS] } returns "jhnvjdsnv"
            every { useCase.clearRequest() } just Runs
            every { useCase.addRequest(any()) } just Runs
            coEvery { useCase.executeOnBackground() } returns mockk{
                every { getData<TokoPointDetailEntity>(TokoPointDetailEntity::class.java) } returns mockk()
                every { getError(TokoPointDetailEntity::class.java) } returns null
            }
            repository.getPointData()

            coVerify(ordering = Ordering.ORDERED) {
                useCase.clearRequest()
                useCase.addRequest(any())
                useCase.executeOnBackground()
            }
        }
    }
}