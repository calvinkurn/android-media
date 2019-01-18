package com.tokopedia.flight.searchV3.presentation.model

import android.support.annotation.StringRes
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