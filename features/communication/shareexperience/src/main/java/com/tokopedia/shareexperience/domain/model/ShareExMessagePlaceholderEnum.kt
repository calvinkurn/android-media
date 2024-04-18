package com.tokopedia.shareexperience.domain.model

enum class ShareExMessagePlaceholderEnum(val placeholder: String) {
    BRANCH_LINK("{{.branch_link}}");

    companion object {
        fun fromValue(value: String): ShareExMessagePlaceholderEnum? {
            return values().firstOrNull { it.placeholder == value }
        }
    }
}
