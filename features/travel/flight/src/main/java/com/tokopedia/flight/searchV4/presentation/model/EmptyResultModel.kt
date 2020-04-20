package com.tokopedia.flight.searchV4.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.searchV4.presentation.adapter.viewholder.EmptyResultViewHolder
import com.tokopedia.flight.searchV4.presentation.adapter.viewholder.FlightSearchAdapterTypeFactory

/**
 * @author by furqan on 20/04/2020
 */
class EmptyResultModel(@DrawableRes var iconRes: Int = 0,
                       var title: String = "",
                       @StringRes var contentRes: Int = 0,
                       var content: String = "",
                       var description: String = "",
                       @StringRes var buttonTitleRes: Int = 0,
                       var buttonTitle: String = "",
                       var callback: EmptyResultViewHolder.Callback? = null)
    : Visitable<FlightSearchAdapterTypeFactory> {

    override fun type(typeFactory: FlightSearchAdapterTypeFactory): Int =
            typeFactory.type(this)


}