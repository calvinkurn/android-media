package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel

interface CanLoadPartially {
    fun copyWithLoading(isLoading: Boolean): CanLoadPartially
}