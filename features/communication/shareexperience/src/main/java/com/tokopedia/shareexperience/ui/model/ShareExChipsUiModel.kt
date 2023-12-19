package com.tokopedia.shareexperience.ui.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shareexperience.domain.model.property.ShareExChipModel
import com.tokopedia.shareexperience.ui.adapter.typefactory.ShareExTypeFactory

class ShareExChipsUiModel(val listChip: List<ShareExChipModel>): Visitable<ShareExTypeFactory> {
    override fun type(typeFactory: ShareExTypeFactory): Int {
        return typeFactory.type(this)
    }
}
