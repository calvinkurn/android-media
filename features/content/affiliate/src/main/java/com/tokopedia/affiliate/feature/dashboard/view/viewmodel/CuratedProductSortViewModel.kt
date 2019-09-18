package com.tokopedia.affiliate.feature.dashboard.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.feature.dashboard.view.adapter.factory.CuratedProductSortTypeFactory

/**
 * Created by jegul on 2019-09-09.
 */
data class CuratedProductSortViewModel(
        val id: Int,
        val text: String,
        val isChecked: Boolean
) : Visitable<CuratedProductSortTypeFactory> {

    override fun type(typeFactory: CuratedProductSortTypeFactory): Int {
        return typeFactory.type(this)
    }
}