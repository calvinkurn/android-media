package com.tokopedia.search.result.presentation.presenter.product

import android.net.Uri
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.utils.CoachMarkLocalCache
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.search.result.domain.model.InspirationCarouselChipsProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.shouldBe
import com.tokopedia.search.utils.SchedulersProvider
import com.tokopedia.topads.sdk.domain.model.Data
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import org.junit.After
import org.junit.Before
import rx.schedulers.Schedulers

internal open class ProductListPresenterTestFixtures {

    protected val searchProductCommonResponseJSON = "searchproduct/common-response.json"
    protected val searchProductFirstPageJSON = "searchproduct/loaddata/first-page.json"
    protected val searchProductSecondPageJSON = "searchproduct/loaddata/second-page.json"

    protected val imageUrl = "https://ecs7-p.tokopedia.net/img/cache/200-square/product-1/2019/9/24/27099280/27099280_743840e0-a043-4236-af17-c8688412b3b8_700_700"
    protected val topAdsImpressionUrl = "https://ta.tokopedia.com/promo/v1/views/8a-2rOYDQfPwgcV7yZUEHZFiy3zwq3ei6sedHsypH_1fbm-xgVY789CBUsthbm-FQRo2PcB5QiUEHZFiPcBWgZUEH_1do_KhoAyRoiFircV7qmUEUMusrprXPcY0QRCBgcBxbMNBPmY2Q3r5yfVsqc15HsnFb9ohP3VagZYFrMYjP3o7b_J5HsnaHm4pbpJRbpKdopJFH_K56AKRH_nh6VYig_Upg_uMyi7NyfeOb_ef63eW6ceOyZOBHfoBopUOHpehHpxwopnF9prFHmUDUMVj9RosQR-BUsta6_UXopjdHAeaHAeRHpnpH_rDUSHp9fh5gaUEUMuNZM2jZJ2M33NGPMep_Mh-qMY2_Mj7H1NkqR26zJOR_M2uHjY1__Co8ju2_JoGqMzUZMgsHBgtyfO6Q7BkQfBoq_eO_Ozg81N11_-vzJ1a_jzso1NJe92-q9P2y_-3o3ea69BqzsBE3_UN8u2_Z_g-qjV2_JoGP3Uao32q17jfZ3O3qJNI_MOqz717_VPgoV2I_9z6zJjF_3jhq1NEZMOHu71p_3OqqVjau_oo8BJa_7o-r7BW69BxufzFyMFNqO2yeMgxuOV2_fB-P7B2PfBiH72F3s-DPuKpeSBiHBUh3_oZgMV913Bvq1BRZ3BRq3UpZSCqHMhO3Ao6QfUpeMgxuOV2_fB-P7B2PfBs3VgDyfNDgMzIzMNs81jfZ3OR8cgU8I2jzpJdgjPR83gU8Ioo81BpZ3N6qMUpZMhyHj2Nysoj8B2_Z_g-qjOd_BH7HjO1z_uo8jVE_S2-P7Y1Z_z6e7BpZ3N6qMUpZMhyH7ND3uxGqMVAZ_g-qjjO_3j7H7Y1gRP6zJBR_jzs8jOJ_9x681tNUiFiP9oBrBY2gmUEUsnibm-pg9opq3YX9fBjUsti_7VzrfYAH3KpZBukPsg7qOu3QVCu9pzzZuz3yMjF9R203jWwq7g9r3VGzAVgPczErcOq8ICqQuxxZ7P7ZugEgMzMHjoqgjVpzfYo_7Po8uC_614h9fHhoOVRZICC63OIuMow3Sxer1FdqAHWPMgR3Ax_8_1py3-jQjYDr1hXqAyibm-xQcri6i-pPcOwQAowQA-wQAJibm-XP3Oig9-wQfgwy3zpUstfbm-XP3Oig9-wy3zp9R-BrZUEoiFiQBYsy3Njq3zxPcuwy3zpUsthHAyDUMVi9RzBrRei6i-6UiFircYpPVYxQcri6BDiyRCs9RotQRCwP3NhUB7DUSgBrSo2Qfdi6i-fHi-Y?src=search&page=1&ob=23&keywords=sepatu&sid=OAQsoC1h3JUjv6tkUVlPU_4QITVbi0_zkZK_kFWqaJD1YtdzpmZxpZmXaKGtIVzfdf2CZfAsGoMOGMyPS9O1_c17QwHpA9mGVc_ZxPqL8h3-vfwX8Sy53abdnOlqLnh6&t=android&management_type=1&is_search=1"
    protected val topAdsClickUrl = "https://ta.tokopedia.com/promo/v1/clicks/8a-xgVY2gmUEoAKaosHho_yDUMVj9RzNrc1i6sJDUSC5rfB7q3YXUsthbm-7q3OBUstho_KO6AJ7osrfbm-srcHi6sJFHAnDUMVj9RosQR-BUsta6_UXopjdHAeaHAeRHpnpH_rDUSHp9fh5gaUEUMuNZM2jZJ2M33NGPMep_Mh-qMY2_Mj7H1NkqR26zJOR_M2uHjY1__Co8ju2_JoGqMzUZMgsHBgtyfO6Q7BkQfBoq_eO_Ozg81N11_-vzJ1a_jzso1NJe92-q9P2y_-3o3ea69BqzsBE3_UN8u2_Z_g-qjV2_JoGP3Uao32q17jfZ3O3qJNI_MOqz717_VPgoV2I_9z6zJjF_3jhq1NEZMOHu71p_3OqqVjau_oo8BJa_7o-r7BW69BxufzFyMFNqO2yeMgxuOV2_fB-P7B2PfBiH72F3s-DPuKpeSBiHBUh3_oZgMV913Bvq1BRZ3BRq3UpZSCqHMhO3Ao6QfUpeMgxuOV2_fB-P7B2PfBs3VgDyfNDgMzIzMNs81jfZ3OR8cgU8I2jzpJdgjPR83gU8Ioo81BpZ3N6qMUpZMhyHj2Nysoj8B2_Z_g-qjOd_BH7HjO1z_uo8jVE_S2-P7Y1Z_z6e7BpZ3N6qMUpZMhyH7ND3uxGqMVAZ_g-qjjO_3j7H7Y1gRP6zJBR_jzs8jOJ_9x681tNUiFiP9oBrBY2gmUEUsnibm-pg9opq3YX9fBjUsti_7VzrfYAH3KpZBukPsg7qOu3QVCu9pzzZuz3yMjF9R203jWwq7g9r3VGzAVgPczErcOq8ICqQuxxZ7P7ZugEgMzMHjoqgjVpzfYo_7Po8uC_614h9fHhoOVRZICC63OIuMow3Sxer1FdqAHWPMgR3Ax_8_1py3-jQjYDr1hXqAyibm-xQcri6i-pPcOwQAowQA-wQAJibm-XP3Oig9-wQfgwy3zpUstfbm-XP3Oig9-wy3zp9R-BrZUEoiFiyfV79fBjraUE3pJRo_jDH_KFHmFh6AeO9ZFiQBYsy3Njq3zxPcuwy3zpUsthHAyDUMVi9RzBrRei6i-6UiFircYpPVYxQcri6i-srcowrfx5rVYOQSJibm-fg9-pq3YXUstiPsUiwe?t=android&sid=OAQsoC1h3JUjv6tkUVlPU_4QITVbi0_zkZK_kFWqaJD1YtdzpmZxpZmXaKGtIVzfdf2CZfAsGoMOGMyPS9O1_c17QwHpA9mGVc_ZxPqL8h3-vfwX8Sy53abdnOlqLnh6&src=search&is_search=1&ob=23&r=https%3A%2F%2Fwww.tokopedia.com%2Findoglowdark%2Fcat-sepatu-midsole-boost-adidas-velle-no-angelus-acrylic-leather-paint-20-ml%3Fsrc%3Dtopads&keywords=sepatu&page=1&management_type=1"

    protected val warehouseId = "19926"

    protected val productListView = mockk<ProductListSectionContract.View>(relaxed = true)
    protected val searchProductFirstPageUseCase = mockk<UseCase<SearchProductModel>>(relaxed = true)
    protected val searchProductLoadMoreUseCase = mockk<UseCase<SearchProductModel>>(relaxed = true)
    protected val getDynamicFilterUseCase = mockk<UseCase<DynamicFilterModel>>(relaxed = true)
    protected val getProductCountUseCase = mockk<UseCase<String>>(relaxed = true)
    protected val recommendationUseCase = mockk<GetRecommendationUseCase>(relaxed = true)
    protected val getLocalSearchRecommendationUseCase = mockk<UseCase<SearchProductModel>>(relaxed = true)
    protected val getInspirationCarouselChipsProductsUseCase = mockk<UseCase<InspirationCarouselChipsProductModel>>(relaxed = true)
    protected val topAdsUrlHitter = mockk<TopAdsUrlHitter>(relaxed = true)
    protected val userSession = mockk<UserSessionInterface>(relaxed = true)
    protected val remoteConfig = mockk<RemoteConfig>()
    protected val searchCoachMarkLocalCache = mockk<CoachMarkLocalCache>(relaxed = true)
    protected val testSchedulersProvider = object : SchedulersProvider {
        override fun io() = Schedulers.immediate()

        override fun ui() = Schedulers.immediate()

        override fun computation() = Schedulers.immediate()
    }
    protected lateinit var productListPresenter: ProductListPresenter

    @Before
    open fun setUp() {
        productListPresenter = ProductListPresenter(
                searchProductFirstPageUseCase,
                searchProductLoadMoreUseCase,
                recommendationUseCase,
                userSession,
                searchCoachMarkLocalCache,
                { getDynamicFilterUseCase },
                { getProductCountUseCase },
                { getLocalSearchRecommendationUseCase },
                { getInspirationCarouselChipsProductsUseCase },
                topAdsUrlHitter,
                testSchedulersProvider,
                { remoteConfig }
        )
        productListPresenter.attachView(productListView)

        verify {
            productListView.abTestRemoteConfig
            productListView.isChooseAddressWidgetEnabled
        }
    }

    protected fun `Then verify visitable list with product items`(
            visitableListSlot: CapturingSlot<List<Visitable<*>>>,
            searchProductModel: SearchProductModel,
            topAdsPositionStart: Int = 0,
            organicPositionStart: Int = 0
    ) {
        val visitableList = visitableListSlot.captured
        val productItemViewModelList = visitableList.filterIsInstance<ProductItemDataView>()

        val topAdsTemplatePosition = getTopAdsProductPositionByTemplate(searchProductModel)

        val organicProductList = searchProductModel.searchProduct.data.productList
        val topAdsProductList = searchProductModel.topAdsModel.data

        var expectedTopAdsProductPosition = topAdsPositionStart + 1
        var expectedOrganicProductPosition = organicPositionStart + 1

        var topAdsProductListIndex = 0
        var organicProductListIndex = 0

        productItemViewModelList.forEachIndexed { index, productItem ->
            if (topAdsTemplatePosition.contains(index)) {
                productItem.assertTopAdsProduct(topAdsProductList[topAdsProductListIndex], expectedTopAdsProductPosition)
                expectedTopAdsProductPosition++
                topAdsProductListIndex++
            }
            else {
                productItem.assertOrganicProduct(organicProductList[organicProductListIndex], expectedOrganicProductPosition)
                expectedOrganicProductPosition++
                organicProductListIndex++
            }
        }
    }

    private fun getTopAdsProductPositionByTemplate(searchProductModel: SearchProductModel): MutableList<Int> {
        val topAdsTemplatePosition = mutableListOf<Int>()
        searchProductModel.topAdsModel.templates.forEachIndexed { index, template ->
            if (template.isIsAd) topAdsTemplatePosition.add(index)
        }
        return topAdsTemplatePosition
    }

    private fun Visitable<*>.assertTopAdsProduct(topAdsProduct: Data, position: Int) {
        val productItem = this as ProductItemDataView

        productItem.isTopAds shouldBe true
        productItem.topadsClickUrl shouldBe topAdsProduct.productClickUrl
        productItem.topadsImpressionUrl shouldBe topAdsProduct.product.image.s_url
        productItem.topadsWishlistUrl shouldBe topAdsProduct.productWishlistUrl
        productItem.minOrder shouldBe topAdsProduct.product.productMinimumOrder
        productItem.position shouldBe position
    }

    protected fun Visitable<*>.assertOrganicProduct(organicProduct: SearchProductModel.Product, position: Int) {
        val productItem = this as ProductItemDataView

        productItem.isOrganicAds shouldBe organicProduct.isOrganicAds()
        productItem.position shouldBe position

        if (organicProduct.isOrganicAds()) {
            productItem.topadsClickUrl shouldBe organicProduct.ads.productClickUrl
            productItem.topadsImpressionUrl shouldBe organicProduct.ads.productViewUrl
            productItem.topadsWishlistUrl shouldBe organicProduct.ads.productWishlistUrl
        }
        else {
            productItem.topadsClickUrl shouldBe ""
            productItem.topadsImpressionUrl shouldBe ""
            productItem.topadsWishlistUrl shouldBe ""
        }

        productItem.productID shouldBe organicProduct.id
        productItem.productName shouldBe organicProduct.name
        productItem.price shouldBe organicProduct.price
        productItem.minOrder shouldBe organicProduct.minOrder
    }

    @Suppress("UNCHECKED_CAST")
    internal fun RequestParams.getSearchProductParams(): Map<String, Any>
            = parameters[SearchConstant.SearchProduct.SEARCH_PRODUCT_PARAMS] as Map<String, Any>

    protected fun `Given ChooseAddressUtils will return warehouseId`() {
        every { productListView.warehouseId } returns warehouseId
    }

    @After
    open fun tearDown() {
        productListPresenter.detachView()
    }
}