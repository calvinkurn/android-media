package com.tokopedia.selleronboarding.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.selleronboarding.adapter.SobAdapterFactory

/**
 * Created By @ilhamsuaib on 20/07/21
 */

object SobSliderMessageUiModel : Visitable<SobAdapterFactory> {

    override fun type(typeFactory: SobAdapterFactory): Int {
        return typeFactory.type(this)
    }
}