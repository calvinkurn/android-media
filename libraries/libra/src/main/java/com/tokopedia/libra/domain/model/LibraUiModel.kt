package com.tokopedia.libra.domain.model

import com.tokopedia.libra.LibraResult

data class LibraUiModel(
    val experiments: List<LibraResult>
) {

    companion object {
        fun default() = LibraUiModel(listOf())
    }
}
