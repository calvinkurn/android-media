package com.tokopedia.talk.stub.feature.reading.presentation.fragment

import android.os.Bundle
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.feature.reading.presentation.fragment.TalkReadingFragment
import com.tokopedia.talk.stub.common.di.component.TalkComponentStubInstance
import com.tokopedia.talk.stub.feature.reading.di.DaggerTalkReadingComponentStub
import com.tokopedia.talk.stub.feature.reading.di.TalkReadingComponentStub

class TalkReadingFragmentStub: TalkReadingFragment() {
    companion object {
        @JvmStatic
        fun createNewInstance(productId: String, shopId: String, isVariantSelected: Boolean, availableVariants: String): TalkReadingFragmentStub =
            TalkReadingFragmentStub().apply {
                arguments = Bundle()
                arguments?.putString(TalkConstants.PARAM_PRODUCT_ID, productId)
                arguments?.putString(TalkConstants.PARAM_SHOP_ID, shopId)
                arguments?.putBoolean(TalkConstants.PARAM_APPLINK_IS_VARIANT_SELECTED, isVariantSelected)
                arguments?.putString(TalkConstants.PARAM_APPLINK_AVAILABLE_VARIANT, availableVariants)
            }
    }

    override fun getComponent(): TalkReadingComponentStub? {
        return activity?.let {
            DaggerTalkReadingComponentStub
                .builder()
                .talkComponentStub(TalkComponentStubInstance.getComponent(it.application))
                .build()
        }
    }
}
