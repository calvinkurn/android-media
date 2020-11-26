package com.tokopedia.product.manage.item.price.model

import android.os.Parcelable
import com.tokopedia.product.manage.item.main.base.data.model.ProductWholesaleViewModel
import com.tokopedia.product.manage.item.utils.ProductEditCurrencyType
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
data class ProductPrice(var price: Double = 0.0,
                        var currencyType: Int = ProductEditCurrencyType.RUPIAH,
                        var wholesalePrice: ArrayList<ProductWholesaleViewModel> = ArrayList(),
                        var minOrder: Int = 0,
                        var maxOrder: Int = 0) : Parcelable