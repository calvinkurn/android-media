package com.tokopedia.talk.stub.feature.reading.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.PRODUCT_ID_VALUE
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.SHOP_ID_VALUE
import com.tokopedia.talk.common.constants.TalkConstants.PARAM_SHOP_ID
import com.tokopedia.talk.common.constants.TalkConstants.PRODUCT_ID
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.reading.presentation.activity.TalkReadingActivity
import com.tokopedia.talk.stub.common.di.component.TalkComponentStubInstance
import com.tokopedia.talk.stub.feature.reading.presentation.fragment.TalkReadingFragmentStub

class TalkReadingActivityStub: TalkReadingActivity() {

    companion object {
        fun getCallingIntent(context: Context?): Intent {
            return Intent(context, TalkReadingActivityStub::class.java).apply {
                putExtra(PRODUCT_ID, PRODUCT_ID_VALUE)
                putExtra(PARAM_SHOP_ID, SHOP_ID_VALUE)
            }
        }
    }

    override fun getNewFragment(): Fragment {
        return TalkReadingFragmentStub.createNewInstance(
            productId = intent.extras?.getString(PRODUCT_ID).orEmpty(),
            shopId = intent.extras?.getString(PARAM_SHOP_ID).orEmpty(),
            isVariantSelected = false,
            availableVariants = ""
        )
    }

    override fun getComponent(): TalkComponent {
        return TalkComponentStubInstance.getComponent(application)
    }
}
