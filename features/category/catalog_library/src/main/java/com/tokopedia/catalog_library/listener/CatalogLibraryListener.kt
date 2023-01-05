package com.tokopedia.catalog_library.listener

interface CatalogLibraryListener {

    fun onLihatSemuaTextClick(applink : String){}
    fun onProductCardClicked(applink: String?){}
    fun onCategoryItemClicked(categoryName: String?){}

}
