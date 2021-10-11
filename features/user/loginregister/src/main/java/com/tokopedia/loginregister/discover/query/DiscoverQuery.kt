package com.tokopedia.loginregister.discover.query

object DiscoverQuery {

    private const val type = "\$type"

    val query: String = """
        query discover($type: String!){
            discover(type: $type) {
                providers {
                    id
                    name
                    image
                    url
                    scope
                    color
                }
                url_background_seller
            }
        }
    """.trimIndent()
}