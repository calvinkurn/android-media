package com.tokopedia.affiliate.feature.education.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.affiliate.R

/**
 * @author by milhamj on 14/01/19.
 */
class AffiliateEducationFragment : BaseDaggerFragment() {

    companion object {
        fun createInstance() = AffiliateEducationFragment()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_af_education, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
    }

    private fun initView() {

    }
}