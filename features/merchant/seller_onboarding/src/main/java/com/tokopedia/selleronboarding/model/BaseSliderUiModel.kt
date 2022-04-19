package com.tokopedia.selleronboarding.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.selleronboarding.adapter.SobAdapterFactory

/**
 * Created By @ilhamsuaib on 22/07/21
 */

interface BaseSliderUiModel : Visitable<SobAdapterFactory> {
    val headerResBg: Int
}