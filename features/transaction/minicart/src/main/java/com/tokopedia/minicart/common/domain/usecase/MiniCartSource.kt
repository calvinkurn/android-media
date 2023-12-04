package com.tokopedia.minicart.common.domain.usecase

typealias MiniCartSourceValue = String

sealed interface MiniCartSource {
    val value: MiniCartSourceValue

    object PDP : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "pdp"
    }

    object TokonowHome : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "tokonow_home"
    }

    object TokonowSRP : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "tokonow_srp"
    }

    object TokonowCategoryPage : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "tokonow_category_page"
    }

    object TokonowRepurchasePage : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "tokonow_repurchase_page"
    }

    object TokonowDiscoveryPage : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "tokonow_disco_page"
    }

    object TokonowRecommendationPage : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "tokonow_recom_page"
    }

    object TokonowRecipe : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "tokonow_recipe"
    }

    object PDPRecommendationWidget : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "pdp_recom_widget"
    }

    object VariantBottomSheet : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "variant_bottom_sheet"
    }

    object MVC : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "shop_mvc_page"
    }

    object MiniCartBottomSheet : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "mini_cart_bottom_sheet"
    }

    object ShopPage : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "shop_page"
    }

    object DiscoOfferPage : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "offer_page"
    }
}
