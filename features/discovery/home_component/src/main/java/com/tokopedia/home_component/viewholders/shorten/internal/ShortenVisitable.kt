package com.tokopedia.home_component.viewholders.shorten.internal

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.viewholders.shorten.factory.ShortenViewFactory

interface ShortenVisitable : Visitable<ShortenViewFactory> {
    fun visitableId(): Int
    fun equalsWith(o: Any?): Boolean

    class ItemShortenVisitable(
        private val id: Int
    ) : ShortenVisitable {

        override fun visitableId() = id
        override fun equalsWith(o: Any?) = o == this
        override fun type(typeFactory: ShortenViewFactory?) = Int.MIN_VALUE
    }
}
