package com.tokopedia.mvc.presentation.intro.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.mvc.presentation.intro.adapter.viewholder.IntroVoucherViewHolder
import com.tokopedia.mvc.presentation.intro.adapter.viewholder.VoucherTypeViewHolder
import com.tokopedia.mvc.presentation.intro.uimodel.IntroVoucherUiModel
import com.tokopedia.mvc.presentation.intro.uimodel.VoucherTypeUiModel

class MvcIntroAdapterFactoryImpl: BaseAdapterTypeFactory(), MvcIntroAdapterFactory {
    override fun type(uimodel: IntroVoucherUiModel): Int {
       return IntroVoucherViewHolder.RES_LAYOUT
    }

    override fun type(voucherTypeUiModel: VoucherTypeUiModel): Int {
        return VoucherTypeViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            IntroVoucherViewHolder.RES_LAYOUT -> IntroVoucherViewHolder(parent)
            VoucherTypeViewHolder.RES_LAYOUT -> VoucherTypeViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}
