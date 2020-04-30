package com.tokopedia.product.manage.feature.list.view.model

import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.constant.ProductManageUrl

object SearchEmptyModel: EmptyModel() {
   init {
       contentRes = R.string.product_manage_list_empty_search
       urlRes = ProductManageUrl.PRODUCT_MANAGE_SEARCH_EMPTY_STATE
   }
}