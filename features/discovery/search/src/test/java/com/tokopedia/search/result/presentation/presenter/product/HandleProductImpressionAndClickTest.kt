package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val topAdsImpressionUrl = "https://ta.tokopedia.com/promo/v1/views/8a-2rOYDQfPwgcV7yZUEHZFiy3zwq3ei6sedHsypH_1fbm-xgVY789CBUsthbm-FQRo2PcB5QiUEHZFiPcBWgZUEH_1do_KhoAyRoiFircV7qmUEUMusrprXPcY0QRCBgcBxbMNBPmY2Q3r5yfVsqc15HsnFb9ohP3VagZYFrMYjP3o7b_J5HsnaHm4pbpJRbpKdopJFH_K56AKRH_nh6VYig_Upg_uMyi7NyfeOb_ef63eW6ceOyZOBHfoBopUOHpehHpxwopnF9prFHmUDUMVj9RosQR-BUsta6_UXopjdHAeaHAeRHpnpH_rDUSHp9fh5gaUEUMuNZM2jZJ2M33NGPMep_Mh-qMY2_Mj7H1NkqR26zJOR_M2uHjY1__Co8ju2_JoGqMzUZMgsHBgtyfO6Q7BkQfBoq_eO_Ozg81N11_-vzJ1a_jzso1NJe92-q9P2y_-3o3ea69BqzsBE3_UN8u2_Z_g-qjV2_JoGP3Uao32q17jfZ3O3qJNI_MOqz717_VPgoV2I_9z6zJjF_3jhq1NEZMOHu71p_3OqqVjau_oo8BJa_7o-r7BW69BxufzFyMFNqO2yeMgxuOV2_fB-P7B2PfBiH72F3s-DPuKpeSBiHBUh3_oZgMV913Bvq1BRZ3BRq3UpZSCqHMhO3Ao6QfUpeMgxuOV2_fB-P7B2PfBs3VgDyfNDgMzIzMNs81jfZ3OR8cgU8I2jzpJdgjPR83gU8Ioo81BpZ3N6qMUpZMhyHj2Nysoj8B2_Z_g-qjOd_BH7HjO1z_uo8jVE_S2-P7Y1Z_z6e7BpZ3N6qMUpZMhyH7ND3uxGqMVAZ_g-qjjO_3j7H7Y1gRP6zJBR_jzs8jOJ_9x681tNUiFiP9oBrBY2gmUEUsnibm-pg9opq3YX9fBjUsti_7VzrfYAH3KpZBukPsg7qOu3QVCu9pzzZuz3yMjF9R203jWwq7g9r3VGzAVgPczErcOq8ICqQuxxZ7P7ZugEgMzMHjoqgjVpzfYo_7Po8uC_614h9fHhoOVRZICC63OIuMow3Sxer1FdqAHWPMgR3Ax_8_1py3-jQjYDr1hXqAyibm-xQcri6i-pPcOwQAowQA-wQAJibm-XP3Oig9-wQfgwy3zpUstfbm-XP3Oig9-wy3zp9R-BrZUEoiFiQBYsy3Njq3zxPcuwy3zpUsthHAyDUMVi9RzBrRei6i-6UiFircYpPVYxQcri6BDiyRCs9RotQRCwP3NhUB7DUSgBrSo2Qfdi6i-fHi-Y?src=search&page=1&ob=23&keywords=sepatu&sid=OAQsoC1h3JUjv6tkUVlPU_4QITVbi0_zkZK_kFWqaJD1YtdzpmZxpZmXaKGtIVzfdf2CZfAsGoMOGMyPS9O1_c17QwHpA9mGVc_ZxPqL8h3-vfwX8Sy53abdnOlqLnh6&t=android&management_type=1&is_search=1"
private const val topAdsClickUrl = "https://ta.tokopedia.com/promo/v1/clicks/8a-xgVY2gmUEoAKaosHho_yDUMVj9RzNrc1i6sJDUSC5rfB7q3YXUsthbm-7q3OBUstho_KO6AJ7osrfbm-srcHi6sJFHAnDUMVj9RosQR-BUsta6_UXopjdHAeaHAeRHpnpH_rDUSHp9fh5gaUEUMuNZM2jZJ2M33NGPMep_Mh-qMY2_Mj7H1NkqR26zJOR_M2uHjY1__Co8ju2_JoGqMzUZMgsHBgtyfO6Q7BkQfBoq_eO_Ozg81N11_-vzJ1a_jzso1NJe92-q9P2y_-3o3ea69BqzsBE3_UN8u2_Z_g-qjV2_JoGP3Uao32q17jfZ3O3qJNI_MOqz717_VPgoV2I_9z6zJjF_3jhq1NEZMOHu71p_3OqqVjau_oo8BJa_7o-r7BW69BxufzFyMFNqO2yeMgxuOV2_fB-P7B2PfBiH72F3s-DPuKpeSBiHBUh3_oZgMV913Bvq1BRZ3BRq3UpZSCqHMhO3Ao6QfUpeMgxuOV2_fB-P7B2PfBs3VgDyfNDgMzIzMNs81jfZ3OR8cgU8I2jzpJdgjPR83gU8Ioo81BpZ3N6qMUpZMhyHj2Nysoj8B2_Z_g-qjOd_BH7HjO1z_uo8jVE_S2-P7Y1Z_z6e7BpZ3N6qMUpZMhyH7ND3uxGqMVAZ_g-qjjO_3j7H7Y1gRP6zJBR_jzs8jOJ_9x681tNUiFiP9oBrBY2gmUEUsnibm-pg9opq3YX9fBjUsti_7VzrfYAH3KpZBukPsg7qOu3QVCu9pzzZuz3yMjF9R203jWwq7g9r3VGzAVgPczErcOq8ICqQuxxZ7P7ZugEgMzMHjoqgjVpzfYo_7Po8uC_614h9fHhoOVRZICC63OIuMow3Sxer1FdqAHWPMgR3Ax_8_1py3-jQjYDr1hXqAyibm-xQcri6i-pPcOwQAowQA-wQAJibm-XP3Oig9-wQfgwy3zpUstfbm-XP3Oig9-wy3zp9R-BrZUEoiFiyfV79fBjraUE3pJRo_jDH_KFHmFh6AeO9ZFiQBYsy3Njq3zxPcuwy3zpUsthHAyDUMVi9RzBrRei6i-6UiFircYpPVYxQcri6i-srcowrfx5rVYOQSJibm-fg9-pq3YXUstiPsUiwe?t=android&sid=OAQsoC1h3JUjv6tkUVlPU_4QITVbi0_zkZK_kFWqaJD1YtdzpmZxpZmXaKGtIVzfdf2CZfAsGoMOGMyPS9O1_c17QwHpA9mGVc_ZxPqL8h3-vfwX8Sy53abdnOlqLnh6&src=search&is_search=1&ob=23&r=https%3A%2F%2Fwww.tokopedia.com%2Findoglowdark%2Fcat-sepatu-midsole-boost-adidas-velle-no-angelus-acrylic-leather-paint-20-ml%3Fsrc%3Dtopads&keywords=sepatu&page=1&management_type=1"

internal class HandleProductImpressionAndClickTest: Spek({

    Feature("Handle onProductImpressed") {
        createTestInstance()

        Scenario("Handle onProductImpression with null ProductItemViewModel") {
            lateinit var productListPresenter: ProductListPresenter

            Given("Product list presenter") {
                productListPresenter = createProductListPresenter()
            }

            When("handle on product impressed") {
                productListPresenter.onProductImpressed(null, -1)
            }

            Then("Verify view does not do anything") {
                val productListView by memoized<ProductListSectionContract.View>()
                confirmVerified(productListView)
            }
        }

        Scenario("Handle onProductImpressed for Top Ads Product") {
            val productItemViewModel = ProductItemViewModel().also {
                it.productID = "12345"
                it.productName = "Hp Samsung"
                it.price = "Rp100.000"
                it.categoryID = 13
                it.isTopAds = true
                it.topadsImpressionUrl = topAdsImpressionUrl
                it.topadsClickUrl = topAdsClickUrl
            }
            val position = 0;

            lateinit var productListPresenter: ProductListPresenter

            Given("Product list presenter") {
                productListPresenter = createProductListPresenter()
            }

            When("handle on product impressed") {
                productListPresenter.onProductImpressed(productItemViewModel, position)
            }

            Then("Verify view interaction for Top Ads Product") {
                val productListView by memoized<ProductListSectionContract.View>()

                verify {
                    productListView.sendTopAdsTrackingUrl(productItemViewModel.topadsImpressionUrl)
                    productListView.sendTopAdsGTMTrackingProductImpression(productItemViewModel, position)
                }

                confirmVerified(productListView)
            }
        }

        Scenario("Handle onProductImpressed for non Top Ads Product") {
            val productItemViewModel = ProductItemViewModel().also {
                it.productID = "12345"
                it.productName = "Hp Samsung"
                it.price = "Rp100.000"
                it.categoryID = 13
                it.isTopAds = false
            }

            lateinit var productListPresenter: ProductListPresenter

            Given("Product list presenter") {
                productListPresenter = createProductListPresenter()
            }

            When("handle on product impressed") {
                productListPresenter.onProductImpressed(productItemViewModel, 0)
            }

            Then("Verify no view interaction for non TopAds product") {
                val productListView by memoized<ProductListSectionContract.View>()
                confirmVerified(productListView)
            }
        }
    }

    Feature("Handle onProductClick") {
        createTestInstance()

        Scenario("Handle onProductClick with null ProductItemViewModel") {
            lateinit var productListPresenter: ProductListPresenter

            Given("Product List Presenter") {
                productListPresenter = createProductListPresenter()
            }

            When("Handle onProductClick") {
                productListPresenter.onProductClick(null, -1)
            }

            Then("Verify view does not do anything") {
                val productListView by memoized<ProductListSectionContract.View>()
                confirmVerified(productListView)
            }
        }

        Scenario("Handle onProductClick for Top Ads Product") {
            val productItemViewModel = ProductItemViewModel().also {
                it.productID = "12345"
                it.productName = "Pixel 4"
                it.price = "Rp100.000.000"
                it.categoryID = 13
                it.isTopAds = true
                it.topadsImpressionUrl = topAdsImpressionUrl
                it.topadsClickUrl = topAdsClickUrl
            }
            val position = 0

            lateinit var productListPresenter: ProductListPresenter

            Given("Product List Presenter") {
                productListPresenter = createProductListPresenter()
            }

            When("Handle onProductClick") {
                productListPresenter.onProductClick(productItemViewModel, position)
            }

            Then("verify view interaction is correct for Top Ads Product") {
                val productListView by memoized<ProductListSectionContract.View>()

                verify {
                    productListView.sendTopAdsTrackingUrl(productItemViewModel.topadsClickUrl)
                    productListView.sendTopAdsGTMTrackingProductClick(productItemViewModel, position)
                    productListView.routeToProductDetail(productItemViewModel, position)
                }

                confirmVerified(productListView)
            }
        }

        Scenario("Handle onProductClick for non Top Ads Product") {
            val productItemViewModel = ProductItemViewModel().also {
                it.productID = "12345"
                it.productName = "Pixel 4"
                it.price = "Rp100.000.000"
                it.categoryID = 13
                it.isTopAds = false
            }
            val position = 0
            val userId = "12345678"

            lateinit var productListPresenter: ProductListPresenter

            Given("Product List Presenter") {
                productListPresenter = createProductListPresenter()
            }

            Given("Mock user session data") {
                val userSession by memoized<UserSessionInterface>()
                every { userSession.isLoggedIn } returns true
                every { userSession.userId } returns userId
            }

            When("Handle onProductClick") {
                productListPresenter.onProductClick(productItemViewModel, position)
            }

            Then("verify view interaction is correct for non Top Ads Product") {
                val productListView by memoized<ProductListSectionContract.View>()

                verify {
                    productListView.sendGTMTrackingProductClick(productItemViewModel, position, userId)
                    productListView.routeToProductDetail(productItemViewModel, position)
                }

                confirmVerified(productListView)
            }
        }
    }
})