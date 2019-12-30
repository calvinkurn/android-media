package com.tokopedia.travelhomepage.destination.model

import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * @author by jessica on 2019-12-30
 */

abstract class TravelDestinationItemModel(var isLoaded: Boolean = false,
                                       var isSuccess: Boolean = false)
//    : Visitable<TravelDestinationAdapterTypeFactory> {
//    abstract override fun type(typeFactory: TravelDestinationAdapterTypeFactory): Int
//}