package com.tokopedia.wishlist.data.model

data class WishlistCollectionEmptyStateData(
    val img: String = "",
    val desc: String = "",
    val title: String = "",
    val listButton: List<Button> = emptyList(),
    val query: String = ""
) {
    data class Button(
        val text: String = "",
        val action: String = "",
        val url: String = ""
    )
}
