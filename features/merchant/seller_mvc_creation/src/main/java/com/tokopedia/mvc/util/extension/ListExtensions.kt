package com.tokopedia.mvc.util.extension

import com.tokopedia.mvc.presentation.intro.uimodel.IntroCouponUiModel

private const val EMPTY_STRING = ""

fun List<String>.getIndexAtOrEmpty(index: Int): String {
    return try {
        this[index]
    } catch (e: Exception) {
        EMPTY_STRING
    }
}

fun List<IntroCouponUiModel.IntroCouponCardData>.getIndexAtOrEmpty(index: Int): IntroCouponUiModel.IntroCouponCardData {
    return try {
        this[index]
    } catch (e: Exception) {
        return IntroCouponUiModel.IntroCouponCardData()
    }
}
