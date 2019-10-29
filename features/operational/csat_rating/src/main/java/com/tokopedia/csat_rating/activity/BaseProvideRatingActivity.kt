package com.tokopedia.csat_rating.activity

import androidx.fragment.app.Fragment
import android.os.Bundle

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.csat_rating.fragment.BaseFragmentProvideRating
import com.tokopedia.kotlin.extensions.view.hide

open class BaseProvideRatingActivity : BaseSimpleActivity() {

    companion object {
       const val CLICKED_EMOJI = "clicked_emoji"
       const val PARAM_OPTIONS_CSAT = "options_csat"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.hide()
    }

    override fun getNewFragment(): Fragment {
        return BaseFragmentProvideRating.newInstance(intent.extras)
    }

}
