package com.tokopedia.product.addedit.description.presentation.model.youtube

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PageInfo {
    @SerializedName("totalResults")
    @Expose
    var totalResults = 0
    @SerializedName("resultsPerPage")
    @Expose
    var resultsPerPage = 0

}