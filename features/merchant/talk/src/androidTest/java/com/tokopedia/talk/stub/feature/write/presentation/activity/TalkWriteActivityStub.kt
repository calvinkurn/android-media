package com.tokopedia.talk.stub.feature.write.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.write.presentation.activity.TalkWriteActivity
import com.tokopedia.talk.stub.common.di.component.TalkComponentStubInstance
import com.tokopedia.talk.stub.feature.write.presentation.fragment.TalkWriteFragmentStub

class TalkWriteActivityStub : TalkWriteActivity() {
    companion object {
        fun createIntent(
            context: Context,
            productId: String,
            isVariantSelected: Boolean,
            availableVariants: String
        ): Intent {
            val intent = Intent(context, TalkWriteActivityStub::class.java)
            intent.putExtra(TalkConstants.PARAM_PRODUCT_ID, productId)
            intent.putExtra(TalkConstants.PARAM_APPLINK_IS_VARIANT_SELECTED, isVariantSelected)
            intent.putExtra(TalkConstants.PARAM_APPLINK_AVAILABLE_VARIANT, availableVariants)
            return intent
        }
    }

    override fun getComponent(): TalkComponent {
        return TalkComponentStubInstance.getComponent(application)
    }

    override fun getNewFragment(): Fragment? {
        return TalkWriteFragmentStub.createNewInstance(productId, isVariantSelected, availableVariants)
    }
}
