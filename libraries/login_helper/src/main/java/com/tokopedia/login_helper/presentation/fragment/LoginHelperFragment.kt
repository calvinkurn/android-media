package com.tokopedia.login_helper.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.login_helper.databinding.FragmentLoginHelperBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable

class LoginHelperFragment: BaseDaggerFragment(){

    private var binding by autoClearedNullable<FragmentLoginHelperBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginHelperBinding.inflate(LayoutInflater.from(context))
        initInjector()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Toast.makeText(context,"My soldiers push forward", Toast.LENGTH_SHORT).show()

    }

    override fun getScreenName(): String {
        return context?.resources?.getString(com.tokopedia.login_helper.R.string.login_helper_header_title).toBlankOrString()
    }

    override fun initInjector() {

    }

    companion object {
        fun newInstance() = LoginHelperFragment()
    }
}
