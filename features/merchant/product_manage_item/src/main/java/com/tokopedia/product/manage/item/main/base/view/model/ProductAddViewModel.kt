package com.tokopedia.product.manage.item.main.base.view.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.product.manage.item.catalog.view.model.ProductCatalog
import com.tokopedia.product.manage.item.category.view.model.ProductCategory
import com.tokopedia.product.manage.item.description.view.model.ProductDescription
import com.tokopedia.product.manage.item.logistic.view.model.ProductLogistic
import com.tokopedia.product.manage.item.main.base.data.model.ProductPictureViewModel
import com.tokopedia.product.manage.item.name.view.model.ProductName
import com.tokopedia.product.manage.item.price.model.*
import com.tokopedia.product.manage.item.stock.view.model.ProductStock
import com.tokopedia.product.manage.item.variant.data.model.variantbycat.ProductVariantByCatModel
import com.tokopedia.product.manage.item.variant.data.model.variantbyprd.ProductVariantViewModel
import java.util.ArrayList

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

    constructor(parcel: Parcel) : this(
            parcel.readParcelable(ProductCatalog::class.java.classLoader),
            parcel.readParcelable(ProductCategory::class.java.classLoader),
            parcel.readParcelable(ProductName::class.java.classLoader),
            parcel.readParcelable(ProductPrice::class.java.classLoader),
            parcel.readParcelable(ProductDescription::class.java.classLoader),
            parcel.readParcelable(ProductVariantViewModel::class.java.classLoader),
            arrayListOf<ProductPictureViewModel>().apply {
                parcel.readList(this, ProductPictureViewModel::class.java.classLoader)
            },
            parcel.readParcelable(ProductStock::class.java.classLoader),
            parcel.readParcelable(ProductPictureViewModel::class.java.classLoader),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readParcelable(ProductLogistic::class.java.classLoader),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            arrayListOf<ProductVariantByCatModel>().apply {
                parcel.readList(this, ProductVariantByCatModel::class.java.classLoader)
            },
            parcel.readByte() != 0.toByte(),
            parcel.readString()) {
    }

    fun getPrdPriceOrMinVariantProductPrice(): Double {
        return if (productVariantViewModel?.hasSelectedVariant()?:false) {
            return productVariantViewModel?.getMinVariantProductPrice()?:0.0
        } else productPrice?.price?:0.0
    }

    fun changePriceTo(value: Double) {
        if (productVariantViewModel?.hasSelectedVariant()?:false) {
            productVariantViewModel?.changePriceTo(value)
        }
        productPrice?.price = value
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(productCatalog, flags)
        parcel.writeParcelable(productCategory, flags)
        parcel.writeParcelable(productName, flags)
        parcel.writeParcelable(productPrice, flags)
        parcel.writeParcelable(productDescription, flags)
        parcel.writeParcelable(productVariantViewModel, flags)
        parcel.writeList(productPictureList)
        parcel.writeParcelable(productStock, flags)
        parcel.writeParcelable(productSizeChart, flags)
        parcel.writeValue(hasOriginalVariantLevel1)
        parcel.writeValue(hasOriginalVariantLevel2)
        parcel.writeParcelable(productLogistic, flags)
        parcel.writeValue(etalaseId)
        parcel.writeString(etalaseName)
        parcel.writeList(productVariantByCatModelList)
        parcel.writeByte(if (isProductNameEditable) 1 else 0)
        parcel.writeString(productId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductAddViewModel> {
        override fun createFromParcel(parcel: Parcel): ProductAddViewModel {
            return ProductAddViewModel(parcel)
        }

        override fun newArray(size: Int): Array<ProductAddViewModel?> {
            return arrayOfNulls(size)
        }
    }

}