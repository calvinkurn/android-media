package com.tokopedia.home_account.explicitprofile.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class CategoriesDataModel(
    @SerializedName("explicitprofileGetAllCategories")
    var data: Data = Data()
) {
    data class Data(
        @SerializedName("data")
        var dataCategories: MutableList<CategoryDataModel> = mutableListOf()
    )
}

data class CategoryDataModel(
    @SuppressLint("Invalid Data Type")
    @SerializedName("id")
    var idCategory: Int = 0,
    @SerializedName("name")
    var name: String ="",
    @SerializedName("imageDisabled")
    var imageDisabled: String ="",
    @SerializedName("imageEnabled")
    var imageEnabled: String ="",
    @SerializedName("templateName")
    var templateName: String =""
)
