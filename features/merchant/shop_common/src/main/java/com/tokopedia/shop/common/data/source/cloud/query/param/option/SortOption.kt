package com.tokopedia.shop.common.data.source.cloud.query.param.option

import androidx.annotation.Keep
import com.tokopedia.shop.common.data.source.cloud.query.param.option.SortOption.SortId.*

sealed class SortOption(open val id: SortId, open val option: SortOrderOption) {

    data class SortByDefault(override val option: SortOrderOption): SortOption(DEFAULT, option)
    data class SortByUpdateTime(override val option: SortOrderOption): SortOption(UPDATE_TIME, option)
    data class SortBySold(override val option: SortOrderOption): SortOption(SOLD, option)
    data class SortByPrice(override val option: SortOrderOption): SortOption(PRICE, option)
    data class SortByName(override val option: SortOrderOption): SortOption(NAME, option)
    data class SortByStock(override val option: SortOrderOption): SortOption(STOCK, option)

    @Keep
    enum class SortId {
        DEFAULT,
        UPDATE_TIME,
        SOLD,
        PRICE,
        NAME,
        STOCK
    }
}