package com.tokopedia.seller.menu.common.view.uimodel.base

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuTypeFactory

interface SettingUiModel: Visitable<OtherMenuTypeFactory> {
    val onClickApplink: String?
}