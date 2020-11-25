package com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch

abstract class ItemSellerSearchUiModel {
    open val id: String? = ""
    open val title: String? = ""
    open val desc: String? = ""
    open val imageUrl: String? = ""
    open val appUrl: String? = ""
    open val url: String? = ""
    open val keyword: String? = ""
    open val section: String? = ""
}