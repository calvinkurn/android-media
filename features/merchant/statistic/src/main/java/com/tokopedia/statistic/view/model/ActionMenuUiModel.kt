package com.tokopedia.statistic.view.model

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.statistic.view.adapter.factory.ActionMenuAdapterFactory
import kotlinx.android.parcel.Parcelize

/**
 * Created By @ilhamsuaib on 14/02/21
 */

@Parcelize
data class ActionMenuUiModel(
        val title: String = "",
        val appLink: String = "",
        val iconUnify: Int? = null,
        val impressHolder: ImpressHolder = ImpressHolder()
) : Visitable<ActionMenuAdapterFactory>, Parcelable {

    override fun type(typeFactory: ActionMenuAdapterFactory): Int {
        return typeFactory.type(this)
    }
}