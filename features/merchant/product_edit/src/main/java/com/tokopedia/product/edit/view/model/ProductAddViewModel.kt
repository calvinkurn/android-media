package com.tokopedia.product.edit.view.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.product.edit.common.model.edit.ProductPictureViewModel
import com.tokopedia.product.edit.common.model.variantbycat.ProductVariantByCatModel
import com.tokopedia.product.edit.common.model.variantbyprd.ProductVariantViewModel
import com.tokopedia.product.edit.price.model.*
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
                               val productId: String = "") : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(ProductCatalog::class.java.classLoader),
            parcel.readParcelable(ProductCategory::class.java.classLoader),
            parcel.readParcelable(ProductName::class.java.classLoader),
            parcel.readParcelable(ProductPrice::class.java.classLoader),
            parcel.readParcelable(ProductDescription::class.java.classLoader),
            parcel.readParcelable(ProductVariantViewModel::class.java.classLoader),
            parcel.readArrayList(ArrayList::class.java.classLoader) as ArrayList<ProductPictureViewModel>,
            parcel.readParcelable(ProductStock::class.java.classLoader),
            parcel.readParcelable(ProductPictureViewModel::class.java.classLoader),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readParcelable(ProductLogistic::class.java.classLoader),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(productCatalog, flags)
        parcel.writeParcelable(productCategory, flags)
        parcel.writeParcelable(productName, flags)
        parcel.writeParcelable(productPrice, flags)
        parcel.writeParcelable(productDescription, flags)
        parcel.writeParcelable(productVariantViewModel, flags)
        parcel.writeParcelable(productStock, flags)
        parcel.writeParcelable(productSizeChart, flags)
        parcel.writeValue(hasOriginalVariantLevel1)
        parcel.writeValue(hasOriginalVariantLevel2)
        parcel.writeParcelable(productLogistic, flags)
        parcel.writeValue(etalaseId)
        parcel.writeString(etalaseName)
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