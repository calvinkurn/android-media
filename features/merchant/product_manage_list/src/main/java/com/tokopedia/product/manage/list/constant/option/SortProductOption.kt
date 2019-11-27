package com.tokopedia.product.manage.list.constant.option

import androidx.annotation.StringDef
import com.tokopedia.product.manage.list.constant.option.SortProductOption.Companion.HIGHEST_PRICE
import com.tokopedia.product.manage.list.constant.option.SortProductOption.Companion.LAST_UPDATE
import com.tokopedia.product.manage.list.constant.option.SortProductOption.Companion.LOWEST_PRICE
import com.tokopedia.product.manage.list.constant.option.SortProductOption.Companion.MOST_BUY
import com.tokopedia.product.manage.list.constant.option.SortProductOption.Companion.MOST_REVIEW
import com.tokopedia.product.manage.list.constant.option.SortProductOption.Companion.MOST_TALK
import com.tokopedia.product.manage.list.constant.option.SortProductOption.Companion.MOST_VIEW
import com.tokopedia.product.manage.list.constant.option.SortProductOption.Companion.NEW_PRODUCT
import com.tokopedia.product.manage.list.constant.option.SortProductOption.Companion.POSITION
import com.tokopedia.product.manage.list.constant.option.SortProductOption.Companion.PRODUCT_NAME

@Retention(AnnotationRetention.SOURCE)
@StringDef(POSITION, NEW_PRODUCT, LAST_UPDATE, PRODUCT_NAME, MOST_VIEW, MOST_TALK,
        MOST_REVIEW, MOST_BUY, LOWEST_PRICE, HIGHEST_PRICE)
annotation class SortProductOption {
    companion object {
        const val  POSITION = "1"
        const val NEW_PRODUCT = "2"
        const val LAST_UPDATE = "3"
        const val PRODUCT_NAME = "4"
        const val MOST_VIEW = "5"
        const val MOST_TALK = "6"
        const val MOST_REVIEW = "7"
        const val MOST_BUY = "8"
        const val LOWEST_PRICE = "9"
        const val HIGHEST_PRICE = "10"
    }
}
