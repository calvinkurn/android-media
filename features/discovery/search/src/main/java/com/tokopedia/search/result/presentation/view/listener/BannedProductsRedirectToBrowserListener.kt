package com.tokopedia.search.result.presentation.view.listener

interface BannedProductsRedirectToBrowserListener {

    fun onGoToBrowserClicked(isEmptySearch: Boolean, encriptedLiteUrl: String)
}