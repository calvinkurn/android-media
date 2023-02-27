package com.tokopedia.feedplus.presentation.model


data class ContentCreationItem(
    var id: Int? = null,
    var name: String? = null,
    var type: CreatorType? = null,
    var image: String? = null,
    var hasUsername: Boolean? = null,
    var hasAcceptTnC: Boolean? = null,
    var items: List<ContentCreationTypeItem> = arrayListOf()
){

}
enum class CreatorType(val value: String) {
    SHOP("content-shop"),
    USER("content-user"),
    NONE("")
}
