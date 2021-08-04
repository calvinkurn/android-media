package com.tokopedia.selleronboarding.model

import androidx.annotation.DrawableRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.selleronboarding.adapter.SobAdapterFactory

/**
 * Created By @ilhamsuaib on 20/07/21
 */

data class SobSliderMessageUiModel(
        @DrawableRes override val headerResBg: Int
) : BaseSliderUiModel {

    override fun type(typeFactory: SobAdapterFactory): Int {
        return typeFactory.type(this)
    }
}