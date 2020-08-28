package com.tokopedia.otp.verification.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.otp.R
import kotlinx.android.synthetic.main.fragment_confirmation_result.*

class ResultFragment : BaseDaggerFragment() {

    override fun getScreenName(): String = ""

    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_confirmation_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
    }

    private fun setListener() {
        btn_cancel?.setOnClickListener {
            activity?.finish()
        }

        button_main?.setOnClickListener {
            activity?.finish()
        }
    }

    companion object {
        fun createInstance(bundle: Bundle): ResultFragment {
            val fragment = ResultFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}