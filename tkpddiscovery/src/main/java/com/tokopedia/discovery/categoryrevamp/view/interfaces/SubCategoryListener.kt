package com.tokopedia.discovery.categoryrevamp.view.interfaces

interface SubCategoryListener {

    fun OnSubCategoryClicked(id: String, categoryName: String, redirectionURL: String)
    fun OnDefaultItemClicked()

}