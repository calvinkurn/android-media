package com.tokopedia.minicart.common.domain.usecase

typealias MiniCartSourceValue = String

sealed interface MiniCartSource {
    val value: MiniCartSourceValue
    val usecase: String?

    object PDP : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "pdp"

        override val usecase: String?
            get() = null
    }

    object TokonowHome : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "tokonow_home"

        override val usecase: String
            get() = "tokonow"
    }

    object TokonowSRP : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "tokonow_srp"

        override val usecase: String
            get() = "tokonow"
    }

    object TokonowCategoryPage : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "tokonow_category_page"

        override val usecase: String
            get() = "tokonow"
    }

    object TokonowRepurchasePage : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "tokonow_repurchase_page"

        override val usecase: String
            get() = "tokonow"
    }

    object TokonowDiscoveryPage : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "tokonow_disco_page"

        override val usecase: String
            get() = "tokonow"
    }

    object TokonowRecommendationPage : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "tokonow_recom_page"

        override val usecase: String
            get() = "tokonow"
    }

    object TokonowRecipe : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "tokonow_recipe"

        override val usecase: String
            get() = "tokonow"
    }

    object TokonowShoppingList : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "tokonow_shopping_list"
    }

    object PDPRecommendationWidget : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "pdp_recom_widget"

        override val usecase: String?
            get() = null
    }

    object VariantBottomSheet : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "variant_bottom_sheet"

        override val usecase: String?
            get() = null
    }

    object MVC : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "shop_mvc_page"

        override val usecase: String?
            get() = null
    }

    object MiniCartBottomSheet : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "mini_cart_bottom_sheet"

        override val usecase: String?
            get() = null
    }

    object ShopPage : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "shop_page"

        override val usecase: String?
            get() = null
    }

    object DiscoOfferPage : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "offer_page"

        override val usecase: String?
            get() = null
    }

    object BmsmOLP : MiniCartSource {
        override val value: MiniCartSourceValue
            get() = "offer_page"

        override val usecase: String?
            get() = null
    }

    object OfferPageBottomSheet : MiniCartSource {

        override val value: MiniCartSourceValue
            get() = "bottomsheet_offer_page"

        override val usecase: String?
            get() = null
    }

    object DiscoveryPage : MiniCartSource {

        override val value: MiniCartSourceValue
            get() = "disco_page"

        override val usecase: String?
            get() = null
    }
}
