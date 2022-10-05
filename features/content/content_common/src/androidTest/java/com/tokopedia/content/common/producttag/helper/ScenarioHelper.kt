package com.tokopedia.content.common.producttag.helper

import android.content.Context
import com.tokopedia.content.common.R
import com.tokopedia.content.test.util.*

/**
 * Created By : Jonathan Darwin on October 05, 2022
 */

fun openTokopediaSection() {
    click(breadcrumb)
    click(sourceTokopedia)
}

fun openLastPurchasedSection() {
    click(breadcrumb)
    click(sourceLastPurchased)
}

fun openMyShopSection() {
    click(breadcrumb)
    click(sourceMyShop)
}

fun openAutocomplete() {
    click(lastTaggedSearchBar)
}

fun openGlobalSearch(keyword: String) {
    openAutocomplete()
    type(fakeAutocompleteSearchBar, keyword)
    pressActionSoftKeyboard(fakeAutocompleteSearchBar)
}

fun openGlobalSearchShopSection(context: Context, keyword: String) {
    openGlobalSearch(keyword)
    click(context.getString(R.string.content_creation_toko_text))
}

fun openShopSectionFromGlobalSearch(context: Context, keyword: String, positionShopClicked: Int) {
    openGlobalSearchShopSection(context, keyword)
    clickItemRecyclerView(globalSearchShopRv, positionShopClicked)
}