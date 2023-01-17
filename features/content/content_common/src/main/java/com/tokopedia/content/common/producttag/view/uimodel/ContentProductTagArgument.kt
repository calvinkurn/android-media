package com.tokopedia.content.common.producttag.view.uimodel

import com.tokopedia.content.common.producttag.util.AUTHOR_TYPE
import com.tokopedia.content.common.producttag.view.uimodel.config.ContentProductTagConfig
import com.tokopedia.content.common.util.forEachIndexed
import com.tokopedia.kotlin.extensions.view.toIntOrZero
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

    val maxSelectedProduct: Int
        get() = query[KEY_MAX_SELECTED_PRODUCT].toIntOrZero()

    val backButton: ContentProductTagConfig.BackButton
        get() = ContentProductTagConfig.BackButton.mapFromValue(query[KEY_BACK_BUTTON].toIntOrZero())

    val isShowActionBarDivider: Boolean
        get() = query[KEY_IS_SHOW_ACTION_BAR_DIVIDER].toBoolean()

    val isAutoHandleBackPressed: Boolean
        get() = query[KEY_IS_AUTO_HANDLE_BACK_PRESSED].toBoolean()

    val appLinkAfterAutocomplete: String
        get() = query[KEY_APPLINK_AFTER_AUTOCOMPLETE].orEmpty()

    companion object {
        const val KEY_SHOP_BADGE = "shopBadge"
        const val KEY_AUTHOR_ID = "authorId"
        const val KEY_AUTHOR_TYPE = "authorType"
        const val KEY_PRODUCT_TAG_SOURCE = "productTagSource"
        const val KEY_IS_MULTIPLE_SELECTION_PRODUCT = "isMultipleSelectionProduct"
        const val KEY_IS_FULL_PAGE_AUTOCOMPLETE = "isFullPageAutocomplete"
        const val KEY_MAX_SELECTED_PRODUCT = "maxSelectedProduct"
        const val KEY_BACK_BUTTON = "backButton"
        const val KEY_IS_SHOW_ACTION_BAR_DIVIDER = "isShowActionBarDivider"
        const val KEY_IS_AUTO_HANDLE_BACK_PRESSED = "isAutoHandleBackPressed"
        const val KEY_APPLINK_AFTER_AUTOCOMPLETE = "appLinkAfterAutocomplete"

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

        private val argumentMap = mutableMapOf<String, Any>(
            KEY_SHOP_BADGE to "",
            KEY_AUTHOR_ID to "",
            KEY_AUTHOR_TYPE to "",
            KEY_PRODUCT_TAG_SOURCE to "",
            KEY_IS_MULTIPLE_SELECTION_PRODUCT to false,
            KEY_IS_FULL_PAGE_AUTOCOMPLETE to false,
            KEY_MAX_SELECTED_PRODUCT to 0,
            KEY_BACK_BUTTON to ContentProductTagConfig.BackButton.Back,
            KEY_IS_SHOW_ACTION_BAR_DIVIDER to true,
            KEY_IS_AUTO_HANDLE_BACK_PRESSED to false,
        )

        fun setShopBadge(shopBadge: String): Builder {
            argumentMap[KEY_SHOP_BADGE] = shopBadge
            return this
        }

        fun setAuthorId(authorId: String): Builder {
            argumentMap[KEY_AUTHOR_ID] = authorId
            return this
        }

        fun setAuthorType(authorType: String): Builder {
            argumentMap[AUTHOR_TYPE] = authorType
            return this
        }

        fun setProductTagSource(productTagSource: String): Builder {
            argumentMap[KEY_PRODUCT_TAG_SOURCE] = productTagSource
            return this
        }

        fun setMultipleSelectionProduct(
            isMultipleSelectionProduct: Boolean,
            maxSelectedProduct: Int,
        ): Builder {
            /**
             * if isMultipleSelectionProduct is false,
             * maxSelectedProduct will be ignored.
             */
            argumentMap[KEY_IS_MULTIPLE_SELECTION_PRODUCT] = isMultipleSelectionProduct
            argumentMap[KEY_MAX_SELECTED_PRODUCT] = maxSelectedProduct
            return this
        }

        fun setFullPageAutocomplete(
            isFullPageAutocomplete: Boolean,
            appLinkAfterAutocomplete: String,
        ): Builder {
            /**
             * if isFullPageAutocomplete is false,
             * applinkAfterAutocomplete will be ignored.
             */
            argumentMap[KEY_IS_FULL_PAGE_AUTOCOMPLETE] = isFullPageAutocomplete
            argumentMap[KEY_APPLINK_AFTER_AUTOCOMPLETE] = appLinkAfterAutocomplete
            return this
        }

        fun setBackButton(backButton: ContentProductTagConfig.BackButton): Builder {
            argumentMap[KEY_BACK_BUTTON] = backButton.value
            return this
        }

        fun setIsShowActionBarDivider(isShowActionBarDivider: Boolean): Builder {
            argumentMap[KEY_IS_SHOW_ACTION_BAR_DIVIDER] = isShowActionBarDivider
            return this
        }

        fun setIsAutoHandleBackPressed(isAutoHandleBackPressed: Boolean): Builder {
            argumentMap[KEY_IS_AUTO_HANDLE_BACK_PRESSED] = isAutoHandleBackPressed
            return this
        }

        fun build(): String {
            return buildString {
                val size = argumentMap.count()

                argumentMap.forEachIndexed { idx, entry ->
                    val key = entry.key
                    val value = entry.value

                    append(key, value)
                    if(idx < size-1) append(QUERY_SEPARATOR)
                }
            }
        }

        private fun StringBuilder.append(key: String, value: Any) {
            append("$key=$value")
        }
    }
}