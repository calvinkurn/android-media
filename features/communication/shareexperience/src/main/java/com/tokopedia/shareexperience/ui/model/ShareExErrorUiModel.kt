package com.tokopedia.shareexperience.ui.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shareexperience.ui.adapter.typefactory.ShareExTypeFactory

class ShareExErrorUiModel: Visitable<ShareExTypeFactory> {
    override fun type(typeFactory: ShareExTypeFactory): Int {
        return typeFactory.type(this)
    }
}
