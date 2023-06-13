package com.tokopedia.content.common.producttag.helper

import com.tokopedia.content.common.R
import com.tokopedia.content.common.test.R as testR
import com.tokopedia.unifycomponents.R as unifyR
import com.tokopedia.autocompletecomponent.R as autocompleteR
import com.tokopedia.sortfilter.R as sortFilterR
import com.tokopedia.filter.R as filterR

/**
 * Created By : Jonathan Darwin on October 05, 2022
 */

/** Parent */
internal val backButton = R.id.ic_cc_product_tag_back
internal val pageTitle = R.id.tv_cc_product_tag_page_title
internal val divider = R.id.view_cc_product_tag_divider
internal val breadcrumb = R.id.tv_cc_product_tag_product_source
internal val saveButton = R.id.btn_save

/** Product Tag Source */
internal val sourceTokopedia = R.id.cl_global_search
internal val sourceLastPurchased = R.id.cl_last_purchase
internal val sourceMyShop = R.id.cl_my_shop

internal val sourceMyAccountLabel = R.id.tv_my_account_label
internal val sourceMyShopLabel = R.id.tv_my_shop_label

/** Last Tagged */
internal val lastTaggedSearchBar = R.id.cl_search
internal val lastTaggedRv = R.id.rv_last_tagged_product

/** Last Purchased */
internal val lastPurchasedRv = R.id.rv_last_purchased_product

/** MyShop */
internal val myShopSearchBar = unifyR.id.searchbar_textfield
internal val myShopRv = R.id.rv_my_shop_product

/** Fake Autocomplete */
internal val fakeAutocompleteSearchBar = unifyR.id.searchbar_textfield
internal val fakeAutocompleteBackButton = R.id.ic_back

/** Autocomplete Search Tribe */
internal val autocompleteSearchBar = autocompleteR.id.autocompleteSearchBar

/** Global Search */
internal val globalSearchBar = R.id.cl_search

/** Sort Filter Bottom Sheet */
internal val sortFilterSaveButtonContainer = filterR.id.buttonApplyContainer
internal val sortFilterSaveButton = filterR.id.buttonApplySortFilter
internal val sortFilterOptionRv = filterR.id.optionRecyclerView
internal val sortFilterChip = filterR.id.sortFilterChipsUnify

/** Global Search Product */
internal val globalSearchProductContainer = R.id.cl_global_search_product
internal val globalSearchProductQuickFilterPrefix = sortFilterR.id.sort_filter_prefix
internal val globalSearchProductQuickFilterContainer = sortFilterR.id.sort_filter_items
internal val globalSearchProductRv = R.id.rv_global_search_product

/** Global Search Shop */
internal val globalSearchShopRv = R.id.rv_global_search_shop

/** Shop */
internal val shopSearchBar = R.id.sb_shop_product
internal val shopRv = R.id.rv_shop_product

/** Others */
internal val checkboxProduct = R.id.checkbox_product

/** Instrumentation Test : Activity Container */
internal val testSelectedProductText = testR.id.tv_selected_product