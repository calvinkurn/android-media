package com.tokopedia.product.manage.feature.list.view.model

import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.constant.ProductManageUrl

object ProductEmptyModel: EmptyModel() {
    init {
        contentRes = R.string.product_manage_list_empty_product
        urlRes = ProductManageUrl.PRODUCT_MANAGE_LIST_EMPTY_STATE
    }
}