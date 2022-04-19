package com.tokopedia.flight.search.presentation.model.statistics

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseListCheckableTypeFactory
import com.tokopedia.flight.search.presentation.model.filter.DepartureTimeEnum
import kotlinx.android.parcel.Parcelize

/**
 * Created by User on 11/1/2017.
 */
@Parcelize
class DepartureStat(
        var departureTime: DepartureTimeEnum,
        var minPrice: Int = 0,
        var minPriceString: String = ""
) : Parcelable, Visitable<BaseListCheckableTypeFactory<DepartureStat>> {

    override fun type(typeFactory: BaseListCheckableTypeFactory<DepartureStat>): Int {
        return typeFactory.type(this)
    }

}