package com.tokopedia.promousage.domain.entity

data class PromoCta(
    val type: String = "",
    val text: String = "",
    val appLink: String = ""
) {
    companion object {
        const val TYPE_REGISTER_GOPAY_LATER_CICIL = "register_gpl_cicil"
    }
}
