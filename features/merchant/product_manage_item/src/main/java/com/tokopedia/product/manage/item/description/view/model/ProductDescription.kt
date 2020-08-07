package com.tokopedia.product.manage.item.description.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ProductDescription(var description: String = "",
                              var feature: String = "",
                              var isNew: Boolean = true,
                              var videoIDs: ArrayList<String> = ArrayList()) : Parcelable