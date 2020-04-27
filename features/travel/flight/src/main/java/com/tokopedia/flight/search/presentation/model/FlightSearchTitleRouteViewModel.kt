package com.tokopedia.flight.search.presentation.model

import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.search.presentation.adapter.FlightSearchAdapterTypeFactory

/**
 * @author by furqan on 18/01/19
 */

class FlightSearchTitleRouteViewModel(@StringRes val titleRes: Int) :
        Visitable<FlightSearchAdapterTypeFactory> {

    override fun type(typeFactory: FlightSearchAdapterTypeFactory?): Int =
            typeFactory!!.type(this)

}