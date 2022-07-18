package com.tokopedia.usercomponents.explicit.fake_view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.usercomponents.test.R
import com.tokopedia.usercomponents.test.databinding.FragmentExplicitDebugBinding

class ExplicitDebugFragment : BaseDaggerFragment() {

    private val binding: FragmentExplicitDebugBinding? by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_explicit_debug, container, false)
    }

    override fun getScreenName(): String =
        ExplicitDebugActivity::class.java.simpleName

    override fun initInjector() {}
}