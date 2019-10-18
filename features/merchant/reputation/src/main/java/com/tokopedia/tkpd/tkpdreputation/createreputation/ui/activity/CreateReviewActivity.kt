package com.tokopedia.tkpd.tkpdreputation.createreputation.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.tkpd.tkpdreputation.createreputation.ui.fragment.CreateReviewFragment


// ApplinkConstInternalMarketPlace.CREATE_REVIEW
class CreateReviewActivity : BaseSimpleActivity() {

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, CreateReviewActivity::class.java)
        }
    }

    override fun getNewFragment(): Fragment = CreateReviewFragment.createInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.let {
            it.elevation = 0f
        }
    }
}