package com.tokopedia.product.manage.feature.campaignstock.ui.util

import com.tokopedia.imageassets.TokopediaImageUrl

import android.content.Context
import com.tokopedia.kotlin.extensions.view.getNumberFormatted
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.R

object CampaignStock {
    const val MAX_STOCK_COUNT = 999999

    const val REDIRECTION_IMAGE_URL = TokopediaImageUrl.REDIRECTION_IMAGE_URL
}

/**
 * @param context
 * @return formatted number of stock string or "{max_stock}+" if stock more than MAX_STOCK_COUNT
 * eg. "999.999+"
 */
fun String.convertCheckMaximumStockLimit(context: Context?): String {
    return toIntOrZero().let { stock ->
        if (stock <= CampaignStock.MAX_STOCK_COUNT) {
            stock.getNumberFormatted()
        } else {
            context?.getString(R.string.product_manage_max_count, CampaignStock.MAX_STOCK_COUNT.getNumberFormatted()).orEmpty()
        }
    }
}
