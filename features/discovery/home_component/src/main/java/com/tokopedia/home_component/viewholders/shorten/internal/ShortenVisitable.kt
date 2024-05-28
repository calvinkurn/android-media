package com.tokopedia.home_component.viewholders.shorten.internal

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.viewholders.shorten.factory.ShortenViewFactory

interface ShortenVisitable : Visitable<ShortenViewFactory> {
    fun itemCount(): Int

    fun visitableId(): Int
    fun equalsWith(o: Any?): Boolean

    class ItemShortenVisitable(
        private val id: Int
    ) : ShortenVisitable {

        override fun visitableId() = id
        override fun equalsWith(o: Any?) = o == this
        override fun type(typeFactory: ShortenViewFactory?) = Int.MIN_VALUE

        /**
         * This an invalid itemCount, hence don't use it.
         * To calculate the size of grid count, please use from its Parent Shorten Models.
         */
        override fun itemCount() = Int.MIN_VALUE
    }
}
