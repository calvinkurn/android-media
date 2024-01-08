package com.tokopedia.gamification.giftbox.data.fakeobject

import com.tokopedia.gamification.giftbox.data.entities.AutoApplyResponse
import com.tokopedia.gamification.giftbox.presentation.viewmodels.AutoApplyCallback
import com.tokopedia.gamification.giftbox.util.TestUtil.verifyAssertEquals

class FakeAutoApply: AutoApplyCallback {
    override fun success(response: AutoApplyResponse?) {
        response.verifyAssertEquals(response)
    }
}
