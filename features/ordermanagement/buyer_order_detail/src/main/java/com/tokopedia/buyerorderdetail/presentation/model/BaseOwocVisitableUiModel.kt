package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.OwocTypeFactoryImpl

interface BaseOwocVisitableUiModel: Visitable<OwocTypeFactoryImpl> {
    fun shouldShow(context: Context): Boolean
}
