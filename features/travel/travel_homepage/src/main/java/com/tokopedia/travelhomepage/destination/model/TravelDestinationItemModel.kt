package com.tokopedia.travelhomepage.destination.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.travelhomepage.destination.factory.TravelDestinationAdapterTypeFactory

/**
 * @author by jessica on 2019-08-09
 */

abstract class TravelDestinationItemModel(var isLoaded: Boolean = false,
                                          var isSuccess: Boolean = false,
                                          var isLoadFromCloud: Boolean = true): Visitable<TravelDestinationAdapterTypeFactory> {
    abstract override fun type(typeFactory: TravelDestinationAdapterTypeFactory): Int
}