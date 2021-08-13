package com.tokopedia.promotionstarget.presenter

import com.tokopedia.promotionstarget.data.claim.ClaimPayload
import com.tokopedia.promotionstarget.data.claim.ClaimPopGratificationResponse
import com.tokopedia.promotionstarget.data.pop.GetPopGratificationResponse
import com.tokopedia.promotionstarget.data.pop.PopGratificationBenefitsItem
import com.tokopedia.promotionstarget.domain.presenter.DialogManagerPresenter
import com.tokopedia.promotionstarget.domain.usecase.ClaimPopGratificationUseCase
import com.tokopedia.promotionstarget.domain.usecase.GetCouponDetailUseCase
import com.tokopedia.promotionstarget.domain.usecase.GetPopGratificationUseCase
import com.tokopedia.promotionstarget.presentation.subscriber.GratificationData
import com.tokopedia.user.session.UserSession
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.spekframework.spek2.Spek

@ExperimentalCoroutinesApi
class DialogManagerPresenterTest : Spek({
    lateinit var presenter: DialogManagerPresenter
    lateinit var getPopGratificationUseCase: GetPopGratificationUseCase
    lateinit var couponDetailUseCase: GetCouponDetailUseCase
    lateinit var claimPopGratificationUseCase: ClaimPopGratificationUseCase
    lateinit var userSession: UserSession

    beforeEachTest {
        userSession = mockk()
        getPopGratificationUseCase = mockk()
        couponDetailUseCase = mockk()
        claimPopGratificationUseCase = mockk()
        presenter = DialogManagerPresenter(userSession, getPopGratificationUseCase, couponDetailUseCase, claimPopGratificationUseCase)
    }

    group("check function invoke") {
        test("getGratificationAndShowDialog") {
            runBlockingTest {
                val gratificationData = GratificationData("pop", "page")
                val map: HashMap<String, Any> = mockk()
                val response: GetPopGratificationResponse = mockk()
                every { getPopGratificationUseCase.getQueryParams(gratificationData.popSlug, gratificationData.page) } returns map
                coEvery { getPopGratificationUseCase.getResponse(map) } returns response

                val data = presenter.getGratificationAndShowDialog(gratificationData)

                Assert.assertEquals(data, response)

                coVerify {
                    getPopGratificationUseCase.getQueryParams(gratificationData.popSlug, gratificationData.page)
                    getPopGratificationUseCase.getResponse(map)
                }
            }
        }

        test("claimGratification") {
            runBlockingTest {
                val claimPayload = ClaimPayload("campaignSlug", "page")
                val response: ClaimPopGratificationResponse = mockk()
                val map: HashMap<String, Any> = mockk()
                every { claimPopGratificationUseCase.getQueryParams(claimPayload) } returns map
                coEvery {
                    claimPopGratificationUseCase.getResponse(map)
                } returns response

                val data = presenter.claimGratification(claimPayload)

                Assert.assertEquals(response, data)
                coVerify {
                    claimPopGratificationUseCase.getQueryParams(claimPayload)
                    claimPopGratificationUseCase.getResponse(map)
                }
            }
        }

        group("mapperGratificationResponseToCouponIds") {


            test("empty popGratificationBenefits") {
                val ids = presenter.mapperGratificationResponseToCouponIds(emptyList())
                Assert.assertEquals(ids.size, 0)
            }
            test("null popGratificationBenefits") {
                val ids = presenter.mapperGratificationResponseToCouponIds(null)
                Assert.assertEquals(ids.size, 0)
            }

            test("null popGratificationBenefits") {
                val popGratificationBenefits = ArrayList<PopGratificationBenefitsItem?>()
                popGratificationBenefits.add(null)
                popGratificationBenefits.add(null)
                val ids = presenter.mapperGratificationResponseToCouponIds(null)
                Assert.assertEquals(ids.size, 0)
            }

            test("filled popGratificationBenefits") {
                val popGratificationBenefits = ArrayList<PopGratificationBenefitsItem?>()
                popGratificationBenefits.add(PopGratificationBenefitsItem("b", 1, 1))
                popGratificationBenefits.add(PopGratificationBenefitsItem("b", 1, 2))
                popGratificationBenefits.add(PopGratificationBenefitsItem("b", 1, 3))

                val ids = presenter.mapperGratificationResponseToCouponIds(popGratificationBenefits)
                Assert.assertEquals(ids[0], popGratificationBenefits[0]!!.referenceID.toString())
                Assert.assertEquals(ids[1], popGratificationBenefits[1]!!.referenceID.toString())
                Assert.assertEquals(ids[2], popGratificationBenefits[2]!!.referenceID.toString())
            }

            test("null referenceID") {
                val popGratificationBenefits = ArrayList<PopGratificationBenefitsItem?>()
                popGratificationBenefits.add(PopGratificationBenefitsItem("b", 1, null))
                val ids = presenter.mapperGratificationResponseToCouponIds(popGratificationBenefits)
                Assert.assertEquals(ids.size, 0)
            }
        }

        group("getCatalogDetail function invocation") {
            test("getCatalogDetail") {
                runBlockingTest {
                    val ids: List<String> = emptyList()
                    coEvery { couponDetailUseCase.getResponse(ids) } returns mockk()

                    presenter.getCatalogDetail(ids)
                    coVerify {
                        couponDetailUseCase.getResponse(ids)
                    }
                }
            }
        }
    }
})