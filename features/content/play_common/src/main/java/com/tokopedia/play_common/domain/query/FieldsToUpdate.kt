package com.tokopedia.play_common.domain.query


enum class FieldsToUpdate(val fieldName: String, val gqlType: String) {

    ActiveMediaId("activeMediaID", "String"),
    Status("status", "Int!"),
    AuthorID("authorID", "String"),
    Title("title", "String"),
    Cover("coverURL", "String"),
    Schedule("publishedAt", "String"),
    Interspersing("isInterspersed", "Boolean")
}
