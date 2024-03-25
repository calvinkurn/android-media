package com.tokopedia.shareexperience.ui.model.chip

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shareexperience.ui.adapter.typefactory.ShareExTypeFactory

class ShareExChipsUiModel(val listChip: List<ShareExChipUiModel>): Visitable<ShareExTypeFactory> {
    override fun type(typeFactory: ShareExTypeFactory): Int {
        return typeFactory.type(this)
    }
}
