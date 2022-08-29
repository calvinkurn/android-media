package com.tokopedia.content.common.producttag.view.uimodel

import java.lang.StringBuilder

/**
 * Created By : Jonathan Darwin on August 29, 2022
 */
class ContentProductTagArgument private constructor(
    private val query: Map<String, String>
) {

    val shopBadge: String
        get() = query[KEY_SHOP_BADGE].orEmpty()

    val authorId: String
        get() = query[KEY_AUTHOR_ID].orEmpty()

    val authorType: String
        get() = query[KEY_AUTHOR_TYPE].orEmpty()

    val productTagSource: String
        get() = query[KEY_PRODUCT_TAG_SOURCE].orEmpty()

    val isMultipleSelectionProduct: Boolean
        get() = query[KEY_IS_MULTIPLE_SELECTION_PRODUCT].toBoolean()

    val isFullPageAutocomplete: Boolean
        get() = query[KEY_IS_FULL_PAGE_AUTOCOMPLETE].toBoolean()

    companion object {
        const val KEY_SHOP_BADGE = "shopBadge"
        const val KEY_AUTHOR_ID = "authorId"
        const val KEY_AUTHOR_TYPE = "authorType"
        const val KEY_PRODUCT_TAG_SOURCE = "productTagSource"
        const val KEY_IS_MULTIPLE_SELECTION_PRODUCT = "isMultipleSelectionProduct"
        const val KEY_IS_FULL_PAGE_AUTOCOMPLETE = "isFullPageAutocomplete"

        const val QUERY_SEPARATOR = "&"

        fun mapFromString(s: String): ContentProductTagArgument {
            val query = s.split(QUERY_SEPARATOR).associate {
                val (key, value) = it.split("=")
                key to value
            }

            return ContentProductTagArgument(query)
        }
    }

    class Builder {

        private var shopBadge: String = ""
        private var authorId: String = ""
        private var authorType: String = ""
        private var productTagSource: String = ""
        private var isMultipleSelectionProduct: Boolean = false
        private var isFullPageAutocomplete: Boolean = false

        fun setShopBadge(shopBadge: String): Builder {
            this.shopBadge = shopBadge
            return this
        }

        fun setAuthorId(authorId: String): Builder {
            this.authorId = authorId
            return this
        }

        fun setAuthorType(authorType: String): Builder {
            this.authorType = authorType
            return this
        }

        fun setProductTagSource(productTagSource: String): Builder {
            this.productTagSource = productTagSource
            return this
        }

        fun setMultipleSelectionProduct(isMultipleSelectionProduct: Boolean): Builder {
            this.isMultipleSelectionProduct = isMultipleSelectionProduct
            return this
        }

        fun setFullPageAutocomplete(isFullPageAutocomplete: Boolean): Builder {
            this.isFullPageAutocomplete = isFullPageAutocomplete
            return this
        }

        fun build(): String {
            return buildString {
                append(KEY_SHOP_BADGE, shopBadge)
                append(QUERY_SEPARATOR)
                append(KEY_AUTHOR_ID, authorId)
                append(QUERY_SEPARATOR)
                append(KEY_AUTHOR_TYPE, authorType)
                append(QUERY_SEPARATOR)
                append(KEY_PRODUCT_TAG_SOURCE, productTagSource)
                append(QUERY_SEPARATOR)
                append(KEY_IS_MULTIPLE_SELECTION_PRODUCT, isMultipleSelectionProduct)
                append(QUERY_SEPARATOR)
                append(KEY_IS_FULL_PAGE_AUTOCOMPLETE, isFullPageAutocomplete)
            }
        }

        private fun StringBuilder.append(key: String, value: Any) {
            append("$key=$value")
        }
    }
}