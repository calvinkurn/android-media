package com.tokopedia.privacycenter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.privacyacenter.databinding.FragmentPrivacyCenterBinding
import com.tokopedia.privacycenter.di.PrivacyCenterComponent
import com.tokopedia.utils.lifecycle.autoClearedNullable

class PrivacyCenterFragment : BaseDaggerFragment() {

    private var viewBinding by autoClearedNullable<FragmentPrivacyCenterBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentPrivacyCenterBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun getScreenName(): String {
        return PrivacyCenterFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(PrivacyCenterComponent::class.java).inject(this)
    }

    companion object {
        fun newInstance() = PrivacyCenterFragment()
    }
}
