package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalTokopediaNow {

    @JvmField
    val HOST_TOKOPEDIA_NOW = "now"

    @JvmField
    val INTERNAL_TOKOPEDIA_NOW = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_TOKOPEDIA_NOW"

    // TokoNowHomeActivity
    @JvmField
    val HOME = "$INTERNAL_TOKOPEDIA_NOW/home"

    // TokoNowCategoryListActivity
    @JvmField
    val CATEGORY_LIST = "$INTERNAL_TOKOPEDIA_NOW/category-list?warehouse_id={warehouse_id}"

    @JvmField
    val SEE_ALL_CATEGORY = "$INTERNAL_TOKOPEDIA_NOW/see-all-category"

    // TokoNowSortFilterActivity
    val SORT_FILTER = "$INTERNAL_TOKOPEDIA_NOW/sort-filter"

    // TokoNowDateFilterActivity
    val DATE_FILTER = "$INTERNAL_TOKOPEDIA_NOW/date-filter"

    // TokoNowEducationalInfoActivity
    val EDUCATIONAL_INFO = "$INTERNAL_TOKOPEDIA_NOW/educational-info"

    // TokoNowAllAnnotationActivity
    @JvmField
    val ALL_ANNOTATION = "$INTERNAL_TOKOPEDIA_NOW/all-annotation"

    @JvmField
    val SEARCH = "$INTERNAL_TOKOPEDIA_NOW/search"

    /* new category applink - TokoNowCategoryActivity */
    @JvmField
    val CATEGORY_L1 = "$INTERNAL_TOKOPEDIA_NOW/category/l1"

    /* TokoNowCategoryL2Activity */
    @JvmField
    val CATEGORY_L2 = "$INTERNAL_TOKOPEDIA_NOW/category/l2"

    // TokoNowRepurchaseActivity
    @JvmField
    val REPURCHASE = "$INTERNAL_TOKOPEDIA_NOW/repurchase-page"

    // TokoNowCategoryFilterActivity
    @JvmField
    val CATEGORY_FILTER = "$INTERNAL_TOKOPEDIA_NOW/category-filter?warehouse_id={warehouse_id}"

    // TokoNowRecipeDetailActivity
    @JvmField
    val RECIPE_DETAIL = "$INTERNAL_TOKOPEDIA_NOW/recipe/detail"

    // TokoNowRecipeBookmarkActivity
    @JvmField
    val RECIPE_BOOKMARK = "$INTERNAL_TOKOPEDIA_NOW/recipe/bookmark"

    // TokoNowRecipeHomeActivity
    @JvmField
    val RECIPE_HOME = "$INTERNAL_TOKOPEDIA_NOW/recipe/home"

    // TokoNowRecipeSearchActivity
    @JvmField
    val RECIPE_SEARCH = "$INTERNAL_TOKOPEDIA_NOW/recipe/search"

    // TokoNowRecipeAutoCompleteActivity
    @JvmField
    val RECIPE_AUTO_COMPLETE = "$INTERNAL_TOKOPEDIA_NOW/recipe/auto-complete"

    // TokoNowRecipeSimilarProductActivity
    @JvmField
    val RECIPE_SIMILAR_PRODUCT_BOTTOM_SHEET = "$INTERNAL_TOKOPEDIA_NOW/recipe/similar-product-bottomsheet"

    // TokoNowRecipeSearchIngredientActivity
    @JvmField
    val RECIPE_INGREDIENT_BOTTOM_SHEET = "$INTERNAL_TOKOPEDIA_NOW/recipe/ingredient-bottomsheet"

    // TokoNowBuyerCommunicationActivity
    @JvmField
    val BUYER_COMMUNICATION_BOTTOM_SHEET = "$INTERNAL_TOKOPEDIA_NOW/buyer-communication"

    @JvmField
    val SHOPPING_LIST = "$INTERNAL_TOKOPEDIA_NOW/list-belanja"
}
