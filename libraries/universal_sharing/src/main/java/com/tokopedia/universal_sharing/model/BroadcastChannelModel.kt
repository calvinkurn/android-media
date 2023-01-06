package com.tokopedia.universal_sharing.model

import com.tokopedia.universal_sharing.constants.BroadcastChannelType

data class BroadcastChannelModel(
    override  val title: String = "",
    override  val description: String = "",
    override  val imageResDrawable: Int = -1,
    val type: BroadcastChannelType = BroadcastChannelType.BLAST_PROMO,
    val id: String = ""
) : TickerShareModel
