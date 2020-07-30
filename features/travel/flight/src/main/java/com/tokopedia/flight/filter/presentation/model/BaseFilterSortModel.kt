package com.tokopedia.flight.filter.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.filter.presentation.adapter.FlightFilterSortAdapterTypeFactory

/**
 * @author by jessica on 2020-02-20
 */

abstract class BaseFilterSortModel(
        var title: String = "",
        var isSelected: Boolean = false
): Visitable<FlightFilterSortAdapterTypeFactory>