package com.tokopedia.product.manage.oldlist.data.model

import android.os.Parcelable
import com.tokopedia.product.manage.oldlist.constant.option.SortProductOption
import kotlinx.android.parcel.Parcelize

@Parcelize
class ProductManageSortModel(
        @SortProductOption
        var sortId: String = "",
        var titleSort: String = ""
) : Parcelable