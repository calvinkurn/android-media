package com.tokopedia.catalog_library.listener

interface CatalogLibraryListener {

    fun onLihatSemuaTextClick(){}
    fun onProductCardClicked(applink: String?){}
    fun changeSortOrderAsc(){}
    fun changeSortOrderDesc(){}
    fun onCategoryItemClicked(categoryName: String?){}

}
