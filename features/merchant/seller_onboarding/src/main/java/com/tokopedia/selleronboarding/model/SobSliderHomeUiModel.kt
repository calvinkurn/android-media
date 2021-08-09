package com.tokopedia.selleronboarding.model

import androidx.annotation.DrawableRes
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.selleronboarding.adapter.SobAdapterFactory

/**
 * Created By @ilhamsuaib on 20/07/21
 */

data class SobSliderHomeUiModel(
        @DrawableRes override val headerResBg: Int,
        val impressionHolder: ImpressHolder = ImpressHolder()
) : BaseSliderUiModel {

    override fun type(typeFactory: SobAdapterFactory): Int {
        return typeFactory.type(this)
    }
}