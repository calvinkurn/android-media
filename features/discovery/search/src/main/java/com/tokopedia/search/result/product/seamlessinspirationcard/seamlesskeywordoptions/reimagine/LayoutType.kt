package com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.reimagine

enum class LayoutType {
    ICON_FUNNELING {
        override fun isFunneling(): Boolean = true
        override fun isIconKeyword(): Boolean = true
    },
    ICON_DRIFTING {
        override fun isIconKeyword(): Boolean = true
    },
    IMAGE_FUNNELING {
        override fun isFunneling(): Boolean = true
        override fun isSingleLine(): Boolean = true
    },
    IMAGE_DRIFTING {
        override fun isSingleLine(): Boolean = true
    },
    DEFAULT_SEAMLESS {
        override fun isGridLayout(): Boolean = false
    };

    open fun isGridLayout(): Boolean = true
    open fun isFunneling(): Boolean = false
    open fun isSingleLine(): Boolean = false
    open fun isIconKeyword(): Boolean = false

    companion object {
        fun getLayoutType(variant: String): LayoutType {
            return when {
                 variant.isContainsIcon()-> {
                     return if(variant.isContainsFunneling()) ICON_FUNNELING else ICON_DRIFTING
                 }
                variant.isContainsImage()-> {
                    return if(variant.isContainsFunneling()) IMAGE_FUNNELING else IMAGE_DRIFTING
                }
                else -> DEFAULT_SEAMLESS
            }
        }

        private fun String.isContainsIcon() : Boolean = this.contains("icon", true)
        private fun String.isContainsImage() : Boolean = this.contains("image", true)
        private fun String.isContainsFunneling() : Boolean = this.contains("funneling", true)
    }
}
