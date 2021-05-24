package com.tokopedia.flight.common.view.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.common.view.adapter.FlightAdapterTypeFactory
import com.tokopedia.flight.search.presentation.adapter.viewholder.EmptyResultViewHolder

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
    : Visitable<FlightAdapterTypeFactory> {

    override fun type(typeFactory: FlightAdapterTypeFactory): Int =
            typeFactory.type(this)


}