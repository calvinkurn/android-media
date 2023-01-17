package com.tokopedia.product_ar.model.state

data class ModifaceViewState(
        val mode: ModifaceViewMode = ModifaceViewMode.LIVE,
        val imageDrawablePath: String = ""
)

enum class ModifaceViewMode {
    LIVE,
    IMAGE
}
