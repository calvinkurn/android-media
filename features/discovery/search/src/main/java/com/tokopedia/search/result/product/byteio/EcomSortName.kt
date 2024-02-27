package com.tokopedia.search.result.product.byteio

import com.tokopedia.analytics.byteio.search.AppLogSearch
import com.tokopedia.filter.newdynamicfilter.helper.SortHelper

fun ecomSortName(selectedSortName: String): String? =
    when (selectedSortName) {
        SortHelper.PALING_SESUAI -> AppLogSearch.ParamValue.SORT_RELEVANCE
        SortHelper.ULASAN -> AppLogSearch.ParamValue.SORT_REVIEW
        SortHelper.TERBARU -> AppLogSearch.ParamValue.SORT_NEWEST
        SortHelper.HARGA_TERTINGGI -> AppLogSearch.ParamValue.SORT_PRICE_DESC
        SortHelper.HARGA_TERENDAH -> AppLogSearch.ParamValue.SORT_PRICE_ASC
        else -> null
    }
