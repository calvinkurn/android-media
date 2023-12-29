package com.tokopedia.common.topupbills.favoritepage.view.model

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common.topupbills.favoritepage.view.typefactory.PersoFavoriteNumberTypeFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
class TopupBillsPersoFavNumberDataView(
    val title: String,
    val subtitle: String,
    val iconUrl: String,
    val categoryId: String,
    val operatorId: String,
    val productId: String,
    val operatorName: String,
    val token: String,
    val clientNumberHash: String,
): Visitable<PersoFavoriteNumberTypeFactory>, Parcelable {

    override fun type(typeFactory: PersoFavoriteNumberTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun getClientName(): String {
        return if (subtitle.isNotEmpty())
            title else ""
    }

    fun getClientNumber(): String {
        return if (subtitle.isNotEmpty())
            subtitle else title
    }
}
