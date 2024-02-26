@file:OptIn(ExperimentalCoroutinesApi::class)

package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponModel
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchCouponModel
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchRedeemCouponModel
import com.tokopedia.search.result.presentation.model.CouponDataView
import com.tokopedia.search.result.stubExecute
import com.tokopedia.search.shouldBe
import com.tokopedia.usecase.RequestParams
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.json.JSONObject
import org.junit.Test
import rx.Subscriber

private const val inspirationProductCoupon =
    "searchproduct/inspirationcoupon/inspiration-product-coupon.json"
private const val inspirationProductGetCouponData =
    "searchproduct/inspirationcoupon/inspiration-product-get-coupon-data.json"
private const val inspirationProductGetCouponDataHabis =
    "searchproduct/inspirationcoupon/inspiration-product-get-coupon-data-habis.json"
private const val inspirationProductGetCouponDataHabisFirstPosition =
    "searchproduct/inspirationcoupon/inspiration-product-get-coupon-data-habis-first.json"
private const val inspirationProductGetCouponDataRedirect =
    "searchproduct/inspirationcoupon/inspiration-product-get-coupon-data-redirect.json"
private const val inspirationProductGetCouponDataClaim =
    "searchproduct/inspirationcoupon/inspiration-product-get-coupon-data-claim.json"
private const val inspirationProductRedeemCouponData =
    "searchproduct/inspirationcoupon/inspiration-product-redeem-coupon-data.json"

internal class SearchProductInspirationCouponTest : ProductListPresenterTestFixtures() {
    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList by lazy { visitableListSlot.captured }

    private val keyword = "samsung"
    private val searchParameter = mapOf<String, Any>(
        SearchApiConst.Q to keyword,
        SearchApiConst.START to "0",
        SearchApiConst.UNIQUE_ID to "unique_id",
        SearchApiConst.USER_ID to "0"
    )

    @Test
    fun `Show inspiration coupon general cases page 1`() {
        val searchProductModel: SearchProductModel = inspirationProductCoupon.jsonToObject()
        val searchCouponModel: SearchCouponModel =
            inspirationProductGetCouponData.jsonToObject<SearchCouponModel>()
        val requestParamsSlot = slot<RequestParams>()
        // Setup Call Coupon UseCase
        `Given couponusecase success`(searchCouponModel, requestParamsSlot)

        `When Load Data Product Search With Data`(searchProductModel)
        // 0 Adress
        // 1 Product0
        // 2 Product1
        // 3 Product2
        // 4 Product3
        // 5 Coupon Carousel with Coupon Data
        // 6 Product4
        // 7 Product5
        // 8 Product6
        // 9 Product7
        visitableList.size shouldBe 10 // 1 Address, 8 Products, 1 Carousel Coupon Card

        val couponList = visitableList.filterIsInstance<CouponDataView>()
        `Then assert coupon list size is search carousel size`(couponList, searchProductModel)

        `Given viewUpdater itemList return visitableList`()

        `Then verify couponusecase has been executed`()

        val requestParams = requestParamsSlot.captured

        val capturedSlugs = requestParams.parameters["slugs"] as List<*>
        val slug =
            searchProductModel.searchInspirationCarousel.data[0].inspirationCarouselOptions[0].identifier.split(
                ","
            )
        slug.forEach {
            capturedSlugs.contains(it) shouldBe true
        }

        `Then verify view set product list`()
        // Verify Result
        val updatedCouponList = viewUpdater.itemList?.filterIsInstance<CouponDataView>()
        `Then assert updated coupon data is automate coupon grid`(updatedCouponList)
        `Then assert Automate Coupon is Correct`(searchCouponModel, updatedCouponList)
    }

    private fun `Then verify couponusecase has been executed`() {
        verify { couponUseCase.execute(any(), any(), any()) }
    }

    private fun `Given couponusecase success`(
        searchCouponModel: SearchCouponModel,
        slot: CapturingSlot<RequestParams>
    ) {
        couponUseCase.stubExecute(slot) returns searchCouponModel
    }

    private fun `Then assert coupon list size is search carousel size`(
        couponList: List<CouponDataView>,
        searchProductModel: SearchProductModel
    ) {
        couponList.size shouldBe searchProductModel.searchInspirationCarousel.data.size
    }

    private fun `Then assert updated coupon data is automate coupon grid`(
        updatedCouponList: List<CouponDataView>?
    ) {
        assert(updatedCouponList?.get(0)?.couponModel1 is AutomateCouponModel.Grid)
    }

    private fun `Then assert Automate Coupon is Correct`(
        searchCouponModel: SearchCouponModel,
        updatedCouponList: List<CouponDataView>?
    ) {
        val expectedCoupon1 =
            searchCouponModel
                .promoCatalogGetCouponListWidget
                ?.couponListWidget
                ?.getOrNull(0)?.widgetInfo
        val actualCoupon1 = updatedCouponList?.get(0)?.couponModel1 as AutomateCouponModel.Grid
        actualCoupon1.type.value shouldBe
            (expectedCoupon1?.headerList?.getOrNull(0)?.parent?.text ?: "")
        actualCoupon1.benefit.value shouldBe
            (expectedCoupon1?.titleList?.getOrNull(0)?.parent?.text ?: "")
        actualCoupon1.tnc.value shouldBe
            (expectedCoupon1?.subtitleList?.getOrNull(0)?.parent?.text ?: "")
        actualCoupon1.backgroundUrl shouldBe
            (expectedCoupon1?.backgroundInfo?.imageURL ?: "")
        actualCoupon1.iconUrl shouldBe
            (expectedCoupon1?.iconURL ?: "")
        actualCoupon1.badgeText shouldBe
            (expectedCoupon1?.badgeList?.getOrNull(0)?.parent?.text ?: "")

        val expectedCoupon2 = searchCouponModel
            .promoCatalogGetCouponListWidget
            ?.couponListWidget
            ?.getOrNull(1)?.widgetInfo ?: return
        val actualCoupon2 = updatedCouponList[1].couponModel1 as AutomateCouponModel.Grid
        actualCoupon2.type.value shouldBe
            (expectedCoupon2.headerList.getOrNull(0)?.parent?.text ?: "")
        actualCoupon2.benefit.value shouldBe
            (expectedCoupon2.titleList.getOrNull(0)?.parent?.text ?: "")
        actualCoupon2.tnc.value shouldBe
            (expectedCoupon2.subtitleList.getOrNull(0)?.parent?.text ?: "")
        actualCoupon2.backgroundUrl shouldBe
            (expectedCoupon2.backgroundInfo?.imageURL ?: "")
        actualCoupon2.iconUrl shouldBe
            (expectedCoupon2.iconURL ?: "")
        actualCoupon2.badgeText shouldBe
            (expectedCoupon2.badgeList.getOrNull(0)?.parent?.text ?: "")
    }

    private fun `When Load Data Product Search With Data`(
        searchModel: SearchProductModel,
        searchParams: Map<String, Any> = searchParameter
    ) {
        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(
            searchModel
        )
        `Given Mechanism to save and get product position from cache`()
        `Given keyword from view`()

        `When Load Data`(searchParams)

        `Then verify view set product list`()
    }

    private fun `Given Search Product API will return SearchProductModel with Inspiration Carousel`(
        searchProductModel: SearchProductModel
    ) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given keyword from view`() {
        every { productListView.queryKey } returns keyword
    }

    private fun `Given Mechanism to save and get product position from cache`() {
        val lastProductPositionSlot = slot<Int>()

        every { productListView.lastProductItemPositionFromCache }.answers {
            if (lastProductPositionSlot.isCaptured) lastProductPositionSlot.captured else 0
        }

        every { productListView.saveLastProductItemPositionToCache(capture(lastProductPositionSlot)) } just runs
    }

    private fun `When Load Data`(searchParams: Map<String, Any>) {
        productListPresenter.loadData(searchParams)
    }

    private fun `Then verify view set product list`() {
        verifyOrder {
            productListView.setProductList(capture(visitableListSlot))
        }
    }

    private fun `Given viewUpdater itemList return visitableList`() {
        every { viewUpdater.itemList } returns visitableList
    }

    @Test
    fun `Inspiration coupon no coupon widget should not show CouponDataView`() {
        val searchProductModel: SearchProductModel = inspirationProductCoupon.jsonToObject()
        val searchCouponModel = SearchCouponModel()
        val requestParamsSlot = slot<RequestParams>()
        // Setup Call Coupon UseCase
        `Given couponusecase success`(searchCouponModel, requestParamsSlot)

        `When Load Data Product Search With Data`(searchProductModel)
        /**
         * 1 Address, 8 Products, carousel coupon should be deleted from visitable list
         */
        visitableList.size shouldBe 9

        /**
         * Should not contain CouponDataView
         */
        visitableList.filterIsInstance<CouponDataView>().size shouldBe 0
    }

    @Test
    fun `Inspiration coupon all coupon habis should not show CouponDataView`() {
        val searchProductModel: SearchProductModel = inspirationProductCoupon.jsonToObject()
        val searchCouponModel =
            inspirationProductGetCouponDataHabis.jsonToObject<SearchCouponModel>()
        val requestParamsSlot = slot<RequestParams>()
        // Setup Call Coupon UseCase
        `Given couponusecase success`(searchCouponModel, requestParamsSlot)

        `When Load Data Product Search With Data`(searchProductModel)
        /**
         * 1 Address, 8 Products, carousel coupon should be deleted from visitable list
         */
        visitableList.size shouldBe 9

        /**
         * Should not contain CouponDataView
         */
        visitableList.filterIsInstance<CouponDataView>().size shouldBe 0
    }

    @Test
    fun `Coupon first position habis second position valid should show one coupon`() {
        val searchProductModel: SearchProductModel = inspirationProductCoupon.jsonToObject()
        val searchCouponModel =
            inspirationProductGetCouponDataHabisFirstPosition.jsonToObject<SearchCouponModel>()
        val requestParamsSlot = slot<RequestParams>()
        // Setup Call Coupon UseCase
        `Given couponusecase success`(searchCouponModel, requestParamsSlot)

        `When Load Data Product Search With Data`(searchProductModel)
        /**
         * 1 Address, 8 Products, carousel coupon should show second coupon
         */
        visitableList.size shouldBe 10

        /**
         * Should not contain CouponDataView
         */
        val couponDataView = visitableList.filterIsInstance<CouponDataView>()

        couponDataView.size shouldBe 1

        `Then assert actual coupon in coupondataView position 0 is search coupon widget position 1`(
            searchCouponModel,
            couponDataView
        )
    }

    private fun `Then assert actual coupon in coupondataView position 0 is search coupon widget position 1`(
        searchCouponModel: SearchCouponModel,
        couponDataView: List<CouponDataView>
    ) {
        val expectedCoupon1 =
            searchCouponModel
                .promoCatalogGetCouponListWidget
                ?.couponListWidget
                ?.getOrNull(1)?.widgetInfo
        val actualCoupon1 = couponDataView[0].couponModel1 as AutomateCouponModel.Grid
        actualCoupon1.type.value shouldBe
            (expectedCoupon1?.headerList?.getOrNull(0)?.parent?.text ?: "")
        actualCoupon1.benefit.value shouldBe
            (expectedCoupon1?.titleList?.getOrNull(0)?.parent?.text ?: "")
        actualCoupon1.tnc.value shouldBe
            (expectedCoupon1?.subtitleList?.getOrNull(0)?.parent?.text ?: "")
        actualCoupon1.backgroundUrl shouldBe
            (expectedCoupon1?.backgroundInfo?.imageURL ?: "")
        actualCoupon1.iconUrl shouldBe
            (expectedCoupon1?.iconURL ?: "")
        actualCoupon1.badgeText shouldBe
            (expectedCoupon1?.badgeList?.getOrNull(0)?.parent?.text ?: "")
    }

    @Test
    fun `coupon cta redirect should redirect`() {
        val searchProductModel: SearchProductModel = inspirationProductCoupon.jsonToObject()
        val searchCouponModel: SearchCouponModel =
            inspirationProductGetCouponDataRedirect.jsonToObject<SearchCouponModel>()
        val requestParamsSlot = slot<RequestParams>()
        // Setup Call Coupon UseCase
        `Given couponusecase success`(searchCouponModel, requestParamsSlot)

        `When Load Data Product Search With Data`(searchProductModel)

        // Get CouponDataView from visitable
        val couponList = visitableList.filterIsInstance<CouponDataView>()
        `Then assert coupon list size is search carousel size`(couponList, searchProductModel)
        `Given view openlink success`()
        // Test CTA
        val targetCouponCta = couponList[0]
        `Then Assert Widget Data Exist`(targetCouponCta)

        `When cta coupon clicked`(targetCouponCta)

        val jsonMetaData =
            targetCouponCta.couponWidgetData1?.widgetInfo?.ctaList?.get(0)?.jsonMetadata?.let {
                JSONObject(
                    it
                )
            }
        `Then verify open link called once with correct json metadata`(jsonMetaData)
    }

    private fun `Then verify open link called once with correct json metadata`(
        jsonMetaData: JSONObject?
    ) {
        verify(exactly = 1) {
            inspirationCarouselView.openLink(
                jsonMetaData!!.getString("app_link"),
                jsonMetaData.getString("url")
            )
        }
    }

    private fun `When cta coupon clicked`(targetCouponCta: CouponDataView) {
        productListPresenter.ctaCoupon(targetCouponCta, targetCouponCta.couponWidgetData1!!)
    }

    private fun `Then Assert Widget Data Exist`(targetCouponCta: CouponDataView) {
        assert(targetCouponCta.couponWidgetData1 != null)
    }

    private fun `Given view openlink success`() {
        every { inspirationCarouselView.openLink(any(), any()) } returns Unit
    }

    @Test
    fun `coupon cta claim should claim`() {
        val searchProductModel: SearchProductModel = inspirationProductCoupon.jsonToObject()
        val searchCouponModel: SearchCouponModel =
            inspirationProductGetCouponData.jsonToObject<SearchCouponModel>()
        val redeemCouponModel: SearchRedeemCouponModel =
            inspirationProductRedeemCouponData.jsonToObject<SearchRedeemCouponModel>()
        val requestParamsSlot = slot<RequestParams>()
        // Setup Call Coupon UseCase
        `Given couponusecase success`(searchCouponModel, requestParamsSlot)
        `Given redeem coupon usecase success`(requestParamsSlot, redeemCouponModel)

        `When Load Data Product Search With Data`(searchProductModel)
        // Get CouponDataView from visitable
        val couponList = visitableList.filterIsInstance<CouponDataView>()
        `Then assert coupon list size is search carousel size`(couponList, searchProductModel)
        val targetCouponCta = couponList[0]
        val prevJsonMetaData =
            targetCouponCta.couponWidgetData1?.widgetInfo?.ctaList?.get(0)?.jsonMetadata?.let {
                JSONObject(
                    it
                )
            }

        // Test CTA
        `Then Assert Widget Data Exist`(targetCouponCta)

        `When cta coupon clicked`(targetCouponCta)

        `Then assert redeem coupon is called with correct parameter`(
            prevJsonMetaData,
            requestParamsSlot
        )

        val updatedCouponCta = couponList[0].couponWidgetData1?.widgetInfo?.ctaList?.get(0)
        `Then assert update coupon cta has been updated with redeem cta`(
            updatedCouponCta,
            redeemCouponModel
        )
    }

    private fun `Given redeem coupon usecase success`(
        requestParamsSlot: CapturingSlot<RequestParams>,
        redeemCouponModel: SearchRedeemCouponModel
    ) {
        redeemCouponUseCase.stubExecute(requestParamsSlot) returns redeemCouponModel
    }

    private fun `Then assert redeem coupon is called with correct parameter`(
        prevJsonMetaData: JSONObject?,
        requestParamsSlot: CapturingSlot<RequestParams>
    ) {
        verify { redeemCouponUseCase.execute(any(), any(), any()) }
        val catalogId = prevJsonMetaData?.getLong("catalog_id") ?: 0
        requestParamsSlot.captured.getLong("catalog_id", -1) shouldBe catalogId
    }

    private fun `Then assert update coupon cta has been updated with redeem cta`(
        updatedCouponCta: SearchCouponModel.Cta?,
        redeemCouponModel: SearchRedeemCouponModel
    ) {
        updatedCouponCta?.text shouldBe redeemCouponModel.hachikoRedeem?.ctaList?.getOrNull(0)?.text
        updatedCouponCta?.type shouldBe redeemCouponModel.hachikoRedeem?.ctaList?.getOrNull(0)?.type
        updatedCouponCta?.isDisabled shouldBe redeemCouponModel.hachikoRedeem?.ctaList?.getOrNull(0)?.isDisabled
        updatedCouponCta?.jsonMetadata shouldBe redeemCouponModel.hachikoRedeem?.ctaList?.getOrNull(
            0
        )?.jsonMetadata
        updatedCouponCta?.toasters shouldBe redeemCouponModel.hachikoRedeem?.ctaList?.getOrNull(0)?.toasters
    }
}
