package com.tokopedia.sellerhome.settings.view.bottomsheet

import com.tokopedia.sellerhome.databinding.BottomSheetSahSocialMediaBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class SocialMediaLinksBottomSheet: BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(): SocialMediaLinksBottomSheet {
            return SocialMediaLinksBottomSheet()
        }
    }

    private var binding by autoClearedNullable<BottomSheetSahSocialMediaBinding>()



}