package com.tokopedia.shop_showcase.shop_showcase_product_add.domain.model

import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.model.GoodsSortInput.Companion.SORT_ID_DEFAULT
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.model.GoodsSortInput.Companion.SORT_VALUE_DEFAULT

/**
 * @author by Rafli Syam on 2020-03-09
 */

data class GetProductListFilter(
    var page: Int = 1,
    var perPage: Int = 10,
    var fkeyword: String = "",
    var fmenu: String? = "",
    var fmenuExclude: String? = "",
    var fcategory: Int = 0,
    var fcondition: Int = 0,
    var fcatalog: Int = 0,
    var fpicture: Int = 0,
    var sortId: String = SORT_ID_DEFAULT,
    var sortValue: String = SORT_VALUE_DEFAULT
) {

    /**
     * Set back filter to default value, for now its just for keyword and page
     */
    fun setDefaultFilter() {
        page = 1
        fkeyword = ""
    }
}
