package com.tokopedia.minicart.common.domain.usecase

sealed interface MiniCartSource {
    val value: String

    object PDP: MiniCartSource {
        override val value: String
            get() = "pdp"
    }

    object TokonowHome: MiniCartSource {
        override val value: String
            get() = "tokonow_home"
    }

    object TokonowSRP: MiniCartSource {
        override val value: String
            get() = "tokonow_srp"
    }

    object TokonowCategoryPage: MiniCartSource {
        override val value: String
            get() = "tokonow_category_page"
    }

    object TokonowRepurchasePage: MiniCartSource {
        override val value: String
            get() = "tokonow_repurchase_page"
    }

    object TokonowDiscoveryPage: MiniCartSource {
        override val value: String
            get() = "tokonow_disco_page"
    }

    object TokonowRecommendationPage: MiniCartSource {
        override val value: String
            get() = "tokonow_recom_page"
    }

    object TokonowRecipe: MiniCartSource {
        override val value: String
            get() = "tokonow_recipe"
    }

    object PDPRecommendationWidget: MiniCartSource {
        override val value: String
            get() = "pdp_recom_widget"
    }

    object VariantBottomSheet: MiniCartSource {
        override val value: String
            get() = "variant_bottom_sheet"
    }

    object MVC: MiniCartSource {
        override val value: String
            get() = "shop_mvc_page"
    }

    object MiniCartBottomSheet: MiniCartSource {
        override val value: String
            get() = "mini_cart_bottom_sheet"
    }

    object ShopPage: MiniCartSource {
        override val value: String
            get() = "shop_page"
    }
}
