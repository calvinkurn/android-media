package com.tokopedia.product.warehouse.di

import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.di.ProductDetailScope
import com.tokopedia.product.detail.di.ProductRestModule
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@Module(includes = [ProductRestModule::class])
class ProductWarehouseModule {
    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(ProductDetailConstant.PATH_MOVE_TO_WAREHOUSE)
    fun provideMoveToWareHouseURL(): String =
            ProductDetailConstant.BASE_REST_URL + ProductDetailConstant.PATH_PRODUCT_ACTION +
                    ProductDetailConstant.PATH_MOVE_TO_WAREHOUSE

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(ProductDetailConstant.PATH_MOVE_TO_ETALASE)
    fun provideMoveToEtalaseURL(): String =
            ProductDetailConstant.BASE_REST_URL + ProductDetailConstant.PATH_PRODUCT_ACTION +
                    ProductDetailConstant.PATH_MOVE_TO_ETALASE


}