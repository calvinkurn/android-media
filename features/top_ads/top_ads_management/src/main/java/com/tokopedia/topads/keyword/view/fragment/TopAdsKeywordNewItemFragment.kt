package com.tokopedia.topads.keyword.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.topads.R

class TopAdsKeywordNewItemFragment: BaseDaggerFragment() {

    companion object {

        fun newInstance(): Fragment {
            return TopAdsKeywordNewItemFragment()
        }
    }

    override fun initInjector() {

    }

    override fun getScreenName(): String? {
        return null
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_top_ads_keyword_new_item, container, false)
    }

}