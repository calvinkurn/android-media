package com.tokopedia.home_component.visitable.shorten

import com.tokopedia.home_component.viewholders.shorten.ShortenVisitable
import com.tokopedia.home_component.viewholders.shorten.factory.ShortenViewFactory

data class DealsUiModel(
    val title: String
) : ShortenVisitable {

    override fun type(typeFactory: ShortenViewFactory) = typeFactory.type(this)

    override fun visitableId() = this.hashCode()

    override fun equalsWith(o: Any?) = o == this
}
