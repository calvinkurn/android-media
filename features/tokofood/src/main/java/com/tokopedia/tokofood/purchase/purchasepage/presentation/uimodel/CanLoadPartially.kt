package com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel

interface CanLoadPartially {
    fun copyWithLoading(isLoading: Boolean): CanLoadPartially
}