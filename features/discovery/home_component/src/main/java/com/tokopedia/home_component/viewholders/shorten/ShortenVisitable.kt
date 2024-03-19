package com.tokopedia.home_component.viewholders.shorten

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.viewholders.shorten.factory.ShortenViewFactory

interface ShortenVisitable : Visitable<ShortenViewFactory> {
    fun visitableId(): Int
    fun equalsWith(o: Any?): Boolean
}
