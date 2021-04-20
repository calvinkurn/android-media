package com.tokopedia.top_ads_headline.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity.STEPPER_MODEL_EXTRA
import com.tokopedia.top_ads_headline.view.fragment.TopAdsHeadlineKeyFragment

class EditTopAdsHeadlineKeywordActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        val fragment = TopAdsHeadlineKeyFragment.createInstance()
        val bundle = Bundle()
        bundle.putParcelable(STEPPER_MODEL_EXTRA, intent.getParcelableExtra(STEPPER_MODEL_EXTRA))
        fragment.arguments = bundle
        return fragment
    }

    fun updateToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

}