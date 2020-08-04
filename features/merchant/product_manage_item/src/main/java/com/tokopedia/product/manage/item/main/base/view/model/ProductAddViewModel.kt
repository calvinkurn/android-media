package com.tokopedia.product.manage.item.main.base.view.model

import android.os.Parcelable
import com.tokopedia.product.manage.item.catalog.view.model.ProductCatalog
import com.tokopedia.product.manage.item.category.view.model.ProductCategory
import com.tokopedia.product.manage.item.description.view.model.ProductDescription
import com.tokopedia.product.manage.item.logistic.view.model.ProductLogistic
import com.tokopedia.product.manage.item.main.base.data.model.ProductPictureViewModel
import com.tokopedia.product.manage.item.name.view.model.ProductName
import com.tokopedia.product.manage.item.price.model.ProductPrice
import com.tokopedia.product.manage.item.stock.view.model.ProductStock
import com.tokopedia.product.manage.item.variant.data.model.variantbycat.ProductVariantByCatModel
import com.tokopedia.product.manage.item.variant.data.model.variantbyprd.ProductVariantViewModel
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ProductAddViewModel(var productCatalog: ProductCatalog? = ProductCatalog(),
                               var productCategory: ProductCategory? = ProductCategory(),
                               var productName: ProductName? = ProductName(),
                               var productPrice: ProductPrice? = ProductPrice(),
                               var productDescription: ProductDescription? = ProductDescription(),
                               var productVariantViewModel: ProductVariantViewModel? = ProductVariantViewModel(),
                               var productPictureList: ArrayList<ProductPictureViewModel>? = ArrayList(),
                               var productStock: ProductStock? = ProductStock(),
                               var productSizeChart: ProductPictureViewModel? = null,
                               var hasOriginalVariantLevel1: Boolean? = false,
                               var hasOriginalVariantLevel2: Boolean? = false,
                               var productLogistic: ProductLogistic? = ProductLogistic(),
                               var etalaseId: Int? = 0,
                               var etalaseName: String? = "",
                               var productVariantByCatModelList: ArrayList<ProductVariantByCatModel> = ArrayList(),
                               var isProductNameEditable: Boolean = true,
                               var productId: String = "") : Parcelable {

    fun getPrdPriceOrMinVariantProductPrice(): Double {
        return if (productVariantViewModel?.hasSelectedVariant() ?: false) {
            return productVariantViewModel?.getMinVariantProductPrice() ?: 0.0
        } else productPrice?.price ?: 0.0
    }

    fun changePriceTo(value: Double) {
        if (productVariantViewModel?.hasSelectedVariant() ?: false) {
            productVariantViewModel?.changePriceTo(value)
        }
        productPrice?.price = value
    }

}