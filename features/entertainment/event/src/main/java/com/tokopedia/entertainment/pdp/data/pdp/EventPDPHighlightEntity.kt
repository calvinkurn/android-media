package com.tokopedia.entertainment.pdp.data.pdp

import com.tokopedia.entertainment.pdp.adapter.factory.EventPDPFactory

data class EventPDPHighlightEntity(
        var title_small : String = "",
        var title_big : String = "",
        var list : List<Highlight>  = emptyList()
): EventPDPModel() {
    override fun type(typeFactory: EventPDPFactory): Int {
        return typeFactory.type(this)
    }
}

data class Highlight(
        var icon : String = "",
        var title : String = "",
        var description : String = ""
)