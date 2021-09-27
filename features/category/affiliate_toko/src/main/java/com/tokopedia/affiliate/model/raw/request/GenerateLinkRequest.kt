package com.tokopedia.affiliate.model.raw.request

data class GenerateLinkRequest(
        val input: Input
) {
    data class Input(
            val channel: List<Int?>,
            val link: List<Link>
    ) {
        data class Link(
                val Type: String,
                val URL: String,
                val Identifier: String,
                val IdentifierType: Int
        )
    }
}
