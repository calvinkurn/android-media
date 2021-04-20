package com.tokopedia.search.mock

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.search.result.presentation.model.*
import com.tokopedia.topads.sdk.domain.model.CpmModel

object MockSearchProductModel {
    fun getProductItemViewModel(): ProductItemDataView {
        val productItem = ProductItemDataView()
        productItem.productID = "937515116"
        productItem.warehouseID = "0"
        productItem.productName = "Mock Organic 1"
        productItem.imageUrl = "https=//ecs7-p.tokopedia.net/img/cache/200-square/product-1/2020/3/7/628764/628764_fd5b9fc1-0e58-4456-8be4-963486858e82_839_839"
        productItem.imageUrl300 = "https=//ecs7-p.tokopedia.net/img/cache/300-square/product-1/2020/3/7/628764/628764_fd5b9fc1-0e58-4456-8be4-963486858e82_839_839"
        productItem.imageUrl700 = "https=//ecs7-p.tokopedia.net/img/cache/700/product-1/2020/3/7/628764/628764_fd5b9fc1-0e58-4456-8be4-963486858e82_839_839"
        productItem.ratingString = "5.0"
        productItem.discountPercentage = 0
        productItem.originalPrice = ""
        productItem.price = "Rp1.420.000"
        productItem.priceInt = 1420000
        productItem.priceRange = ""
        productItem.shopID = "131646"
        productItem.shopName = "Cellular Mas"
        productItem.shopCity = "Jakarta Pusat"
        productItem.isWishlisted = false
        productItem.badgesList = listOf()
        productItem.position = 1
        productItem.categoryID = 0
        productItem.categoryName = "Handphone"
        productItem.categoryBreadcrumb = "Handphone"
        productItem.labelGroupList = listOf()
        productItem.freeOngkirDataView = FreeOngkirDataView()
        productItem.boosterList = "0"
        productItem.sourceEngine = "0"
        productItem.isOrganicAds = false
        productItem.topadsImpressionUrl = "productModel.ads.productViewUrl"
        productItem.topadsClickUrl = "productModel.ads.productClickUrl"
        productItem.topadsWishlistUrl = "productModel.ads.productWishlistUrl"
        return productItem
    }

    fun getInspirationCardViewModel(): InspirationCardDataView {
        return InspirationCardDataView(
                title = "Coba produk dengan kategori ini :",
                type = "category",
                position = 8,
                optionData = listOf(
                        getInspirationCardOptionViewModel(1), getInspirationCardOptionViewModel(2),
                        getInspirationCardOptionViewModel(3), getInspirationCardOptionViewModel(4)
                )
        )
    }

    private fun getInspirationCardOptionViewModel(position: Int): InspirationCardOptionDataView {
        return InspirationCardOptionDataView(
                text = "Kemeja Pria $position",
                img = "",
                url = "https://www.tokopedia.com/search?q=baju&sc=3579&source=search&st=product",
                hexColor = "",
                applink = "tokopedia://search?q=baju&sc=3579&source=search&st=product"
        )
    }

    fun getInspirationCarouselListViewModel(): InspirationCarouselDataView {
        return InspirationCarouselDataView(
                title = "Cek kategori berikut - should not be shown (position error)",
                type = "category",
                position = 0,
                options = listOf(
                        getInspirationCarouselOptionViewModel(), getInspirationCarouselOptionViewModel(), getInspirationCarouselOptionViewModel()
                )
        )
    }

    private fun getInspirationCarouselOptionViewModel(): InspirationCarouselDataView.Option {
        return InspirationCarouselDataView.Option(
                title = "Android OS",
                url = "tokopedia://search?q=samsung",
                applink = "tokopedia://search?q=samsung",
                product = listOf(
                        InspirationCarouselDataView.Option.Product(
                                id = "12345",
                                name = "Samsung Galaxy A50S 4/64 Ram 4gb Rom 64gb Garansi Resmi",
                                price = 8000000,
                                priceStr = "Rp8.000.000",
                                imgUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/9/28/569641896/569641896_3a21ae42-10ee-45e6-b7ad-14c7a461de3b_1800_1800.jpg",
                                rating = 4,
                                countReview = 100,
                                url = "",
                                applink = "tokopedia://product/12345"
                        )
                )
        )
    }

    fun getBroadMatchViewModel(): BroadMatchDataView {
        return BroadMatchDataView(
                keyword = "baju kaos anak",
                url = "https://staging.tokopedia.com/search?q=baju+kaos+anak&source=related_keyword&st=product",
                applink = "tokopedia://search?q=baju kaos anak&source=related_keyword&st=product",
                broadMatchItemDataViewList = listOf(
                        getBroadMatchItemViewModel(1), getBroadMatchItemViewModel(2),
                        getBroadMatchItemViewModel(3), getBroadMatchItemViewModel(4),
                        getBroadMatchItemViewModel(5), getBroadMatchItemViewModel(6)
                )
        )
    }

    private fun getBroadMatchItemViewModel(position: Int): BroadMatchItemDataView {
        return BroadMatchItemDataView(
                id = "15340985",
                name = "tes hidden category blazer $position",
                price = 150000,
                imageUrl = "",
                url = "https://staging.tokopedia.com/aqua/tes-hidden-category-blazer-1-hitam?refined=true&trkid=f%3DCa83L000P0W0S0Sh%2C%2C%2C%2C%2CCo0Po0Fr0Cb0_src%3Dsearch_page%3D1_ob%3D203_q%3Dbaju+kaos+anak+balapan_bmexp%3D1_po%3D1_catid%3D148_bmexp%3D1&whid=0",
                applink = "tokopedia://product/15340985?",
                priceString = ""
        )
    }

    fun getGlobalNavViewModel(): GlobalNavDataView {
        return GlobalNavDataView(
                "recharge",
                "Beli Pulsa",
                "pulsa xl",
                "pill",
                "https://ecs7.tokopedia.net/img/blue-container.png",
                "tokopedia://digital/form?category_id=1&operator_id=5",
                "https://pulsa.tokopedia.com/pulsa/?action=edit_data&operator_id=5",
                true,
                listOf(
                        getGlobalNavItemViewModel(1), getGlobalNavItemViewModel(2),
                        getGlobalNavItemViewModel(3), getGlobalNavItemViewModel(4),
                        getGlobalNavItemViewModel(5)
                )
        )
    }

    private fun getGlobalNavItemViewModel(position: Int): GlobalNavDataView.Item {
        return GlobalNavDataView.Item(
                "Recharge $position",
                "50.000",
                "Rp 49.500",
                "https://ecs7.tokopedia.net/img/recharge/operator/xl_3.png",
                "tokopedia://digital/form?category_id=1&operator_id=5&product_id=31",
                "https://pulsa.tokopedia.com/pulsa/?action=edit_data&operator_id=5&product_id=31",
                "Mulai dari",
                "",
                "https://ecs7.tokopedia.net/img/autocomplete/image_background_default_widget.png",
                "https://ecs7.tokopedia.net/img/recharge/category/pulsa/widget-active.png",
                position
        )
    }

    fun getCpmViewModel(): CpmDataView {
        val cpmViewModel = CpmDataView()
        cpmViewModel.cpmModel = CpmModel(cpmJSONObject)

        return cpmViewModel
    }

    fun getSuggestionViewModel(): SuggestionDataView {
        val suggestionViewModel = SuggestionDataView()
        suggestionViewModel.suggestionText = "Menampilkan hasil untuk <strong>\"bju batik\"</strong>. </br> Apakah yang anda maksud <strong><font color=\"#42b549\">\"baju batik\"</font></strong> ?"
        suggestionViewModel.suggestion = "baju batik"
        suggestionViewModel.suggestedQuery = "q=baju batik&rf=true"

        return suggestionViewModel
    }

    fun getEmptySearchProductViewModel(): EmptySearchProductDataView {
        val emptySearchProductViewModel = EmptySearchProductDataView()
        emptySearchProductViewModel.isBannerAdsAllowed = true

        return emptySearchProductViewModel
    }

    fun getBannedProductsEmptySearchViewModel(): BannedProductsEmptySearchDataView {
        return BannedProductsEmptySearchDataView("Produk yang kamu cari tidak tersedia di Android. Silakan cari di desktop atau mobile web HP-mu.")
    }

    fun getRecommendationTitleViewModel(): RecommendationTitleDataView {
        return RecommendationTitleDataView("Rekomendasi Untuk Anda", "", "empty_search")
    }

    fun getRecommendationItemViewModel(): RecommendationItemDataView {
        return RecommendationItemDataView(getRecommendationItem())
    }

    private fun getRecommendationItem(): RecommendationItem {
        return RecommendationItem(
                appUrl = "tokopedia://product/1039522597",
                badgesUrl = listOf("https://ecs7-p.tokopedia.net/ta/icon/badge/PM-Badge-80.png"),
                cartId = "",
                categoryBreadcrumbs = "Kesehatan/Perlengkapan Medis/Disposable Consumable",
                clickUrl = "https://ta.tokopedia.com/promo/v1/clicks/8a-xgVY2gmUEosnpHAnOo_yDUMVj9RzNrc1i6sJDUSC5rfB7q3YXUsthbm-7q3OBUstho_jfHAjF6_Hdbm-srcHi6s1FHmFiy3zwrfo5rM1i6sUfomdhoAyOHp1RHAyRHpKfHaFirpowQcYSUstig9BGqMzUZMggQj2fgAo6QJBkQfBo1pzd_Bz081Y1gpCo8MDa_jzzHjNEyR2-q9P23_oZ8uKp_Mhg3J2ky1o-ojBkyRu68Mod_jz0P7O13_Co8MHa_uzsoJN_Z9o-Q9zDguxjPMoW1MgsHjNfyfOuq1Y2Z9P-q9P2yM7NPujau3Bvq1BN3jzoHjOke9Pqq_np_32sP7h11MBqqBB73uzu8JNAHAu6zcD7_32gHBBk3_-qqBu2_JoGPMoWQcNxupuM3jP3POKaQcW-qMY2_1o-r7BW69BxufzFyMFNPfoW63Wju7dF3A-Dq7BkQfBoe7BpZ37N83V9gICiQABEy1rNPOKaQcW-qMY2_1o-r7BXzsVq3JtO3AoZqVtp_3Bvq1B2_JoG8Bja69BqusB2yf7NHfHau3Bvq1BN_M2zP1O11_-6uJ7h_S2CHjNE__z6qjO2_JoG8Bja69BqusBE3BPc8ujagfBvq1BE_uzSP1Y11_uouVVE_BzVP7NEgp-617tNUiFiP9oBrBY2gmUEUsnibm-pg9opq3YX9fBjUstiy3ydofep6ceWypBs6m77yprNb_KFypHWoc1f63Jao_nR6ABMUiFiy3hSUstiyMupPVYpg3hDg9Uibm-XP3Oig9-wQfgwy3zpUstdbm-XP3Oig9-wy3zp9R-BrZUE6mFiyfV79fBjraUE3prhoZFhopeabAUOHsCPbm-X9foxQMz2gcV7guYxgIHi6sJObm-xyBY7g9o7Usti_iUDUSC5rRzwy3hSUstigcuMUiFiPMuarfB5QiUEUSyaUiFiyfhOrRz",
                countReview = 225,
                departmentId = 2520,
                discountPercentage = "",
                discountPercentageInt = 0,
                freeOngkirImageUrl = "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png",
                header = "Rekomendasi Untuk Anda",
                imageUrl = "https://ecs7-p.tokopedia.net/img/cache/200-square/product-1/2020/7/21/194813/194813_9f15902b-6188-4a7f-9f0f-cd2e6030897d_700_700",
                isFreeOngkirActive = true,
                isGold = false,
                isTopAds = true,
                isWishlist = false,
                labelGroupList = listOf(),
                location = "Jakarta Barat",
                minOrder = 0,
                name = "KUKE Face Shield Kacamata - Face Shield Nagita Original Premium - Nagita",
                pageName = "empty_search",
                position = 1,
                price = "Rp7.500",
                priceInt = 7500,
                productId = 1039522597,
                quantity = 1,
                rating = 5,
                recommendationType = "best_seller",
                shopId = 450360,
                shopName = "KUKE",
                shopType = "",
                slashedPrice = "",
                slashedPriceInt = 0,
                stock = 0,
                trackerImageUrl = "https://ta.tokopedia.com/promo/v1/views/8a-2rOYDQfPwgcV7yZUEHZFiy3zwq3ei6syFHpnFo_1fbm-xgVY789CBUsthbm-FQRo2PcB5QiUEHZFiPcBWgZUEH_1NosnNHAjp6mFircV7qmUEUMusrprWrmN7QfW5rcujq3JXQMu7bfBWgaYsy3otgZ4pHAn5rI-5gIusPm7hbpUFHsn5oa4aHZ4h6_edH_H5H_j76AJp9pBMH_1NHA-ib_yh6AKWocJRgi7NgsCMb3ojHM1fHAHF6AjRgV4RHACwopnFUiFiy3zwrfo5rM1i6sUfomdhoAyOHp1RHAyRHpKfHaFirpowQcYSUstig9BGqMzUZMggQj2fgAo6QJBkQfBo1pzd_Bz081Y1gpCo8MDa_jzzHjNEyR2-q9P23_oZ8uKp_Mhg3J2ky1o-ojBkyRu68Mod_jz0P7O13_Co8MHa_uzsoJN_Z9o-Q9zDguxjPMoW1MgsHjNfyfOuq1Y2Z9P-q9P2yM7NPujau3Bvq1BN3jzoHjOke9Pqq_np_32sP7h11MBqqBB73uzu8JNAHAu6zcD7_32gHBBk3_-qqBu2_JoGPMoWQcNxupuM3jP3POKaQcW-qMY2_1o-r7BW69BxufzFyMFNPfoW63Wju7dF3A-Dq7BkQfBoe7BpZ37N83V9gICiQABEy1rNPOKaQcW-qMY2_1o-r7BXzsVq3JtO3AoZqVtp_3Bvq1B2_JoG8Bja69BqusB2yf7NHfHau3Bvq1BN_M2zP1O11_-6uJ7h_S2CHjNE__z6qjO2_JoG8Bja69BqusBE3BPc8ujagfBvq1BE_uzSP1Y11_uouVVE_BzVP7NEgp-617tNUiFiP9oBrBY2gmUEUsnibm-pg9opq3YX9fBjUstiy3ydofep6ceWypBs6m77yprNb_KFypHWoc1f63Jao_nR6ABMUiFiy3hSUstiyMupPVYpg3hDg9Uibm-XP3Oig9-w",
                type = "Infinite",
                url = "https://www.tokopedia.com/kuke/kuke-face-shield-kacamata-face-shield-nagita-original-premium-nagita?src=topads",
                wishlistUrl = "https://ta.tokopedia.com/promo/v1/wishlists/8a-xgVY2gmUEosnpHAnOo_yDUMVj9RzNrc1i6sJDUSC5rfB7q3YXUsthbm-7q3OBUstho_jfHAjF6_Hdbm-srcHi6s1FHmFiy3zwrfo5rM1i6sUfomdhoAyOHp1RHAyRHpKfHaFirpowQcYSUstig9BGqMzUZMggQj2fgAo6QJBkQfBo1pzd_Bz081Y1gpCo8MDa_jzzHjNEyR2-q9P23_oZ8uKp_Mhg3J2ky1o-ojBkyRu68Mod_jz0P7O13_Co8MHa_uzsoJN_Z9o-Q9zDguxjPMoW1MgsHjNfyfOuq1Y2Z9P-q9P2yM7NPujau3Bvq1BN3jzoHjOke9Pqq_np_32sP7h11MBqqBB73uzu8JNAHAu6zcD7_32gHBBk3_-qqBu2_JoGPMoWQcNxupuM3jP3POKaQcW-qMY2_1o-r7BW69BxufzFyMFNPfoW63Wju7dF3A-Dq7BkQfBoe7BpZ37N83V9gICiQABEy1rNPOKaQcW-qMY2_1o-r7BXzsVq3JtO3AoZqVtp_3Bvq1B2_JoG8Bja69BqusB2yf7NHfHau3Bvq1BN_M2zP1O11_-6uJ7h_S2CHjNE__z6qjO2_JoG8Bja69BqusBE3BPc8ujagfBvq1BE_uzSP1Y11_uouVVE_BzVP7NEgp-617tNUiFiP9oBrBY2gmUEUsnibm-pg9opq3YX9fBjUstiy3ydofep6ceWypBs6m77yprNb_KFypHWoc1f63Jao_nR6ABMUiFiy3hSUstiyMupPVYpg3hDg9Uibm-XP3Oig9-wQfgwy3zpUstdbm-XP3Oig9-wy3zp9R-BrZUE6mFiyfV79fBjraUE3prhoZFhopeabAUOHsCPbm-X9foxQMz2gcV7guYxgIHi6sJObm-xyBY7g9o7Usti_iUDUSC5rRzwy3hSUstigcuMUiFiPMuarfB5QiUEUSyaUiFiyfhO"
        )
    }
}