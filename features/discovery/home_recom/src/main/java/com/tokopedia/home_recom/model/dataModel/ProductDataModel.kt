package com.tokopedia.home_recom.model.dataModel

class ProductDataModel(
    val id: Int,
    val name: String,
    val url: String,
    val imageUrl: String,
    val description: String,
    val price: String,
    val applink: String
){
    companion object{
        fun empty(): ProductDataModel = ProductDataModel(-1, "Product", "", "", "Description", "Rp 100.000,-", "")
    }
}

