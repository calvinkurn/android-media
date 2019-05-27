package com.tokopedia.home_recom.model.dataModel

class ProductDataModel(
    val id: Int,
    val name: String,
    val url: String,
    val imageUrl: String,
    val price: String,
    val applink: String,
    val originalPrice: String,
    val discount: String,
    val location: String,
    val reviewCount: Int,
    val rating: Int
){
    companion object{
        fun empty(): ProductDataModel = ProductDataModel(
                -1,
                "Samsung Galaxy S10+",
                "",
                "https://upload.wikimedia.org/wikipedia/id/9/9d/AgumonDigimon.jpg",
                "Rp100.000",
                "",
                "Rp100.000.000",
                "20%",
                "Jakarta",
                4,
                53)
    }
}

