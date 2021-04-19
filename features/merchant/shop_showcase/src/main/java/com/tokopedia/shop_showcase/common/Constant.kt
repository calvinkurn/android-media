package com.tokopedia.shop_showcase.common

object ImageAssets {
    const val PRODUCT_EMPTY = "https://ecs7.tokopedia.net/android/others/illustration_product_empty.png"
    const val SEARCH_SHOWCASE_NOT_FOUND = "https://ecs7.tokopedia.net/android/merchant/shop_showcase/search_empty.png"
    const val PICKER_LIST_EMPTY = "https://ecs7.tokopedia.net/android/shop/showcase_picker_empty_state.png"
}

object AppScreen {
    const val ADD_SHOP_SHOWCASE_SCREEN = "Add Shop Showcase Screen"
    const val SHOP_SHOWCASE_PICKER_SCREEN = "Shop Showcase Picker Screen"
}

object PageNameConstant {
    const val SHOWCASE_LIST_PAGE = "SHOWCASE_LIST_PAGE"
    const val SHOWCASE_LIST_REORDER_PAGE = "SHOWCASE_LIST_REORDER_PAGE"
}

object ShopShowcaseListParam {
    const val EXTRA_SHOP_ID = "SHOP_ID"
    const val EXTRA_ETALASE_ID = "ETALASE_ID"
    const val EXTRA_ETALASE_NAME = "ETALASE_NAME"
    const val EXTRA_ETALASE_TYPE = "ETALASE_TYPE"
    const val EXTRA_ETALASE_BADGE = "ETALASE_BADGE"
    const val EXTRA_SELECTED_ETALASE_ID = "EXTRA_SELECTED_ETALASE_ID"
    const val EXTRA_EDIT_SHOWCASE_RESULT = "IS_EDIT_SHOWCASE_SUCCESS"
    const val EXTRA_IS_SHOW_DEFAULT = "IS_SHOW_DEFAULT"
    const val EXTRA_IS_SHOW_ZERO_PRODUCT = "IS_SHOW_ZERO_PRODUCT"
    const val EXTRA_IS_MY_SHOP = "IS_MY_SHOP"
    const val EXTRA_SHOP_TYPE = "EXTRA_SHOP_TYPE"
    const val EXTRA_IS_NEED_TO_GOTO_ADD_SHOWCASE = "EXTRA_IS_NEED_TO_GOTO_ADD_SHOWCASE"
    const val EXTRA_IS_NEED_TO_RELOAD_DATA = "EXTRA_IS_NEED_TO_RELOAD_DATA"
    const val EXTRA_IS_SELLER_NEED_TO_HIDE_SHOWCASE_GROUP_VALUE = "EXTRA_IS_SELLER_NEED_TO_HIDE_SHOWCASE_GROUP_VALUE"
}

object ShopShowcaseEditParam {
    const val EXTRA_SHOWCASE_ID = "SHOWCASE_ID"
    const val EXTRA_SHOWCASE_NAME = "SHOWCASE_NAME"
    const val EXTRA_IS_ACTION_EDIT = "IS_ACTION_EDIT"
}

object TextConstant {
    const val TEXT_TITLE_DIALOG_DELETE = "Yakin hapus etalase ini?"
    const val TEXT_DESCRIPTION_DIALOG_DELETE = "Dengan menghapus etalase, produk \nakan tersimpan di Semua Produk"
}

object ShopType {
    const val REGULAR = "REGULAR"
    const val GOLD_MERCHANT = "GOLD_MERCHANT"
    const val OFFICIAL_STORE = "OFFICIAL_STORE"
}

object PageType {
    const val ADD_SHOWCASE_PAGE = "ADD_SHOWCASE_PAGE"
    const val ADD_SHOWCASE_FROM_SHOWCASE_LIST = "ADD_SHOWCASE_FROM_SHOWCASE_LIST"
    const val REORDER_SHOWCASE_PAGE = "REORDER_SHOWCASE_PAGE"
}

const val TOTAL_GENERATED_ID: Int = 10
const val MAX_TOTAL_SHOWCASE_REGULAR_MERCHANT = 10
const val MAX_TOTAL_SHOWCASE_PM_AND_OS = 200

const val AB_TEST_ROLLOUT_ETALASE_REVAMP = "etalase_revamp_new"