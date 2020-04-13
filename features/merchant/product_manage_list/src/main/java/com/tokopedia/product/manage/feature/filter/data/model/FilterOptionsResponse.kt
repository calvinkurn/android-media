package com.tokopedia.product.manage.feature.filter.data.model

import com.tokopedia.core.common.category.domain.model.CategoriesResponse
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel

class FilterOptionsResponse(
        val productListMetaResponse: ProductListMetaResponse,
        val shopEtalase: ArrayList<ShopEtalaseModel>,
        val categoriesResponse: CategoriesResponse
)