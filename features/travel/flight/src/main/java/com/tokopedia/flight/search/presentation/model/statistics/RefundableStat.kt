package com.tokopedia.flight.search.presentation.model.statistics

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseListCheckableTypeFactory
import com.tokopedia.flight.search.presentation.model.filter.RefundableEnum
import kotlinx.android.parcel.Parcelize

@Parcelize
class RefundableStat(
        var refundableEnum: RefundableEnum,
        var minPrice: Int = 0,
        var minPriceString: String = ""
) : Parcelable, Visitable<BaseListCheckableTypeFactory<RefundableStat>> {

    override fun type(typeFactory: BaseListCheckableTypeFactory<RefundableStat>): Int {
        return typeFactory.type(this)
    }

}