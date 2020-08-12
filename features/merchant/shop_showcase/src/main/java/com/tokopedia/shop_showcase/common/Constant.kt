package com.tokopedia.shop_showcase.common

object ImageAssets {
    const val PRODUCT_EMPTY = "https://ecs7.tokopedia.net/android/others/illustration_product_empty.png"
    const val SEARCH_SHOWCASE_NOT_FOUND = "https://ecs7.tokopedia.net/android/merchant/shop_showcase/search_empty.png"
}

object AppScreen {
    const val ADD_SHOP_SHOWCASE_SCREEN = "Add Shop Showcase Screen"
}

object PageNameConstant {
    const val SHOWCASE_LIST_PAGE = "SHOWCASE_LIST_PAGE"
    const val SHOWCASE_LIST_REORDER_PAGE = "SHOWCASE_LIST_REORDER_PAGE"
}

object ShopShowcaseListParam {
    const val EXTRA_SHOP_ID = "SHOP_ID"
    const val EXTRA_ETALASE_ID = "ETALASE_ID"
    const val EXTRA_ETALASE_NAME = "ETALASE_NAME"
    const val EXTRA_ETALASE_BADGE = "ETALASE_BADGE"
    const val EXTRA_SELECTED_ETALASE_ID = "EXTRA_SELECTED_ETALASE_ID"
    const val EXTRA_EDIT_SHOWCASE_RESULT = "IS_EDIT_SHOWCASE_SUCCESS"
    const val EXTRA_IS_SHOW_DEFAULT = "IS_SHOW_DEFAULT"
    const val EXTRA_IS_SHOW_ZERO_PRODUCT = "IS_SHOW_ZERO_PRODUCT"
    const val EXTRA_IS_MY_SHOP = "IS_MY_SHOP"
    const val EXTRA_SHOP_TYPE = "EXTRA_SHOP_TYPE"
    const val EXTRA_IS_NEED_TO_GOTO_ADD_SHOWCASE = "EXTRA_IS_NEED_TO_GOTO_ADD_SHOWCASE"
    const val EXTRA_IS_NEED_TO_RELOAD_DATA = "EXTRA_IS_NEED_TO_RELOAD_DATA"
}

object ShopShowcaseEditParam {
    const val EXTRA_SHOWCASE_ID = "SHOWCASE_ID"
    const val EXTRA_SHOWCASE_NAME = "SHOWCASE_NAME"
    const val EXTRA_IS_ACTION_EDIT = "IS_ACTION_EDIT"
}

object ShowcaseType {
    const val GENERATED: Int = -1
    const val CUSTOM: Int = 1
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