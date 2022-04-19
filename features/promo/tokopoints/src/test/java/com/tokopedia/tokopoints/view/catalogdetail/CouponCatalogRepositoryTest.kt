package com.tokopedia.tokopoints.view.catalogdetail

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.tokopoints.view.model.*
import com.tokopedia.tokopoints.view.util.CommonConstant
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class CouponCatalogRepositoryTest {

    lateinit var repository: CouponCatalogRepository
    val map = mockk<Map<String, String>>()

    @Before
    fun setUp() {
        repository = CouponCatalogRepository(map , "tp_curent_point")
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getcatalogDetail() {
        runBlocking {
            val useCase = mockk<MultiRequestGraphqlUseCase>()
            repository.mGetCouponDetail = useCase
            every { map[CommonConstant.GQLQuery.TP_GQL_CATALOG_DETAIL] } returns "sdfcasdv"
            every { useCase.clearRequest() } just Runs
            every { useCase.addRequest(any()) } just Runs
            coEvery { useCase.executeOnBackground() } returns mockk()
            repository.getcatalogDetail("dnv")

            coVerify (ordering = Ordering.ORDERED){
                useCase.clearRequest()
                useCase.addRequest(any())
                useCase.addRequest(any())
                useCase.executeOnBackground()
            }

        }

    }

    @Test
    fun startSendGift() {
        runBlocking {
            val useCase = mockk<MultiRequestGraphqlUseCase>()
            repository.mStartSendGift = useCase
            every { map[CommonConstant.GQLQuery.TP_GQL_PRE_VALIDATE_REDEEM] } returns "sdfcasdv"
            every { useCase.clearRequest() } just Runs
            every { useCase.addRequest(any()) } just Runs
            coEvery { useCase.executeOnBackground() } returns mockk{
                every { getData<PreValidateRedeemBase>(PreValidateRedeemBase::class.java) } returns mockk()
                every { getError(PreValidateRedeemBase::class.java) } returns null
            }
            repository.startSendGift(1)

            coVerify (ordering = Ordering.ORDERED){
                useCase.clearRequest()
                useCase.addRequest(any())
                useCase.executeOnBackground()
            }

        }
    }

    @Test
    fun startValidateCoupon() {
        runBlocking {
            val useCase = mockk<MultiRequestGraphqlUseCase>()
            repository.mValidateCouponUseCase = useCase
            every { map[CommonConstant.GQLQuery.TP_GQL_TOKOPOINT_VALIDATE_REDEEM] } returns "sdfcasdv"
            every { useCase.clearRequest() } just Runs
            every { useCase.addRequest(any()) } just Runs
            coEvery { useCase.executeOnBackground() } returns mockk{
                every { getData<ValidateCouponBaseEntity>(ValidateCouponBaseEntity::class.java) } returns mockk()
                every { getError(ValidateCouponBaseEntity::class.java) } returns null
            }
            repository.startValidateCoupon(1)

            coVerify (ordering = Ordering.ORDERED){
                useCase.clearRequest()
                useCase.addRequest(any())
                useCase.executeOnBackground()
            }

        }
    }

    @Test
    fun fetchLatestStatus() {
        runBlocking {
            val useCase = mockk<MultiRequestGraphqlUseCase>()
            repository.mRefreshCatalogStatus = useCase
            every { map[CommonConstant.GQLQuery.TP_GQL_CATLOG_STATUS] } returns "sdfcasdv"
            every { useCase.clearRequest() } just Runs
            every { useCase.addRequest(any()) } just Runs
            coEvery { useCase.executeOnBackground() } returns mockk{
                every { getData<CatalogStatusOuter>(CatalogStatusOuter::class.java) } returns mockk()
                every { getError(CatalogStatusOuter::class.java) } returns null
            }
            repository.fetchLatestStatus(listOf())

            coVerify (ordering = Ordering.ORDERED){
                useCase.clearRequest()
                useCase.addRequest(any())
                useCase.executeOnBackground()
            }

        }
    }

    @Test
    fun startSaveCoupon() {
        runBlocking {
            val useCase = mockk<MultiRequestGraphqlUseCase>()
            repository.mRedeemCouponUseCase = useCase
            every { map[CommonConstant.GQLQuery.TP_GQL_TOKOPOINT_REDEEM_COUPON] } returns "sdfcasdv"
            every { useCase.clearRequest() } just Runs
            every { useCase.addRequest(any()) } just Runs
            coEvery { useCase.executeOnBackground() } returns mockk{
                every { getData<RedeemCouponBaseEntity>(RedeemCouponBaseEntity::class.java) } returns mockk()
                every { getError(RedeemCouponBaseEntity::class.java) } returns null
            }
            repository.startSaveCoupon(1)

            coVerify (ordering = Ordering.ORDERED){
                useCase.clearRequest()
                useCase.addRequest(any())
                useCase.executeOnBackground()
            }

        }
    }

    @Test
    fun redeemCoupon() {
        runBlocking {
            val useCase = mockk<MultiRequestGraphqlUseCase>()
            repository.mSaveCouponUseCase = useCase
            every { map[CommonConstant.GQLQuery.TP_GQL_TOKOPOINT_APPLY_COUPON] } returns "sdfcasdv"
            every { useCase.clearRequest() } just Runs
            every { useCase.addRequest(any()) } just Runs
            coEvery { useCase.executeOnBackground() } returns mockk{
                every { getData<ApplyCouponBaseEntity>(ApplyCouponBaseEntity::class.java) } returns mockk()
                every { getError(ApplyCouponBaseEntity::class.java) } returns null
            }
            repository.redeemCoupon("dnv")

            coVerify (ordering = Ordering.ORDERED){
                useCase.clearRequest()
                useCase.addRequest(any())
                useCase.executeOnBackground()
            }

        }
    }


    @Test
    fun getListOfCatalog() {
        runBlocking {
            val useCase = mockk<MultiRequestGraphqlUseCase>()
            repository.mGetCatalogUsecase = useCase
            every { map[CommonConstant.GQLQuery.TP_GQL_CATALOG_LIST] } returns "sdfcasdv"
            every { useCase.clearRequest() } just Runs
            every { useCase.addRequest(any()) } just Runs
            coEvery { useCase.executeOnBackground() } returns mockk{
                every { getData<CatalogListingOuter>(CatalogListingOuter::class.java) } returns mockk()
                every { getError(CatalogListingOuter::class.java) } returns null
            }
            repository.getListOfCatalog(1,1,0)

            coVerify (ordering = Ordering.ORDERED){
                useCase.clearRequest()
                useCase.addRequest(any())
                useCase.executeOnBackground()
            }

        }
    }
}