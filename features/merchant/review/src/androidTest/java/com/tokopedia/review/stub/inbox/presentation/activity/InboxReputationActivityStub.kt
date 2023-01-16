package com.tokopedia.review.stub.inbox.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.review.stub.reviewlist.view.fragment.RatingProductFragmentStub

class InboxReputationActivityStub: BaseSimpleActivity() {

    companion object {
        fun getCallingIntent(context: Context?): Intent {
            return Intent(context, InboxReputationActivityStub::class.java)
        }
    }

    override fun getNewFragment(): Fragment? {
        return RatingProductFragmentStub.createInstance()
    }
}
