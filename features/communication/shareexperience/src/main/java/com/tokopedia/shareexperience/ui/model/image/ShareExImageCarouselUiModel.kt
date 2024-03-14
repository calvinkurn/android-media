package com.tokopedia.shareexperience.ui.model.image

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shareexperience.ui.adapter.typefactory.ShareExTypeFactory

class ShareExImageCarouselUiModel(
    val imageUrlList: List<ShareExImageUiModel>
): Visitable<ShareExTypeFactory> {
    override fun type(typeFactory: ShareExTypeFactory): Int {
        return typeFactory.type(this)
    }
}
