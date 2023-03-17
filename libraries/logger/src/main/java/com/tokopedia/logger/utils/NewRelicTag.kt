package com.tokopedia.logger.utils

class NewRelicTag(
    override val postPriority: Int,
    val newRelicKey: String,
    val newRelicTable: String
) : Tag(postPriority)
