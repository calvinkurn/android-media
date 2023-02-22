package com.tokopedia.feedplus.presentation.model


data class ContentCreationItem(
    var id: Int = 0,
    var name: String = "",
    var type: CreatorType = CreatorType.NONE,
    var image: String = "",
    var hasUsername: Boolean = false,
    var hasAcceptTnC: Boolean = false,
    var items: List<ContentCreationTypeItem> = arrayListOf()
){

}
enum class CreatorType(val value: String) {
    SHOP("content-shop"),
    USER("content-user"),
    NONE("")
}
