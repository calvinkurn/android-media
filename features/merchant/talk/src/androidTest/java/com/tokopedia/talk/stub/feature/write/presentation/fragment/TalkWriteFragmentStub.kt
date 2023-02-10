package com.tokopedia.talk.stub.feature.write.presentation.fragment

import android.os.Bundle
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.feature.write.di.TalkWriteComponent
import com.tokopedia.talk.feature.write.presentation.fragment.TalkWriteFragment
import com.tokopedia.talk.stub.common.di.component.TalkComponentStub
import com.tokopedia.talk.stub.feature.write.di.component.DaggerTalkWriteComponentStub

class TalkWriteFragmentStub : TalkWriteFragment() {
    companion object {
        @JvmStatic
        fun createNewInstance(
            productId: String,
            isVariantSelected: Boolean,
            availableVariants: String
        ): TalkWriteFragmentStub {
            return TalkWriteFragmentStub().apply {
                arguments = Bundle()
                arguments?.putString(TalkConstants.PARAM_PRODUCT_ID, productId)
                arguments?.putBoolean(
                    TalkConstants.PARAM_APPLINK_IS_VARIANT_SELECTED,
                    isVariantSelected
                )
                arguments?.putString(
                    TalkConstants.PARAM_APPLINK_AVAILABLE_VARIANT,
                    availableVariants
                )
            }
        }
    }

    override fun getComponent(): TalkWriteComponent {
        return DaggerTalkWriteComponentStub.builder()
            .talkComponentStub(getComponent(TalkComponentStub::class.java)).build()
    }
}
