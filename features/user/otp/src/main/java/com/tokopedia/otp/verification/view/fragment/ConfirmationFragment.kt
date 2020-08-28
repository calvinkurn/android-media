package com.tokopedia.otp.verification.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.otp.R
import com.tokopedia.otp.verification.view.activity.ResultActivity
import kotlinx.android.synthetic.main.fragment_otp_confirmation.*

class ConfirmationFragment : BaseDaggerFragment() {

    override fun getScreenName(): String = ""

    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_otp_confirmation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
    }

    private fun setListener() {
        btn_back?.setOnClickListener {
            activity?.finish()
        }

        btn_yes?.setOnClickListener {
            startActivity(ResultActivity.getCallingIntent(context, Bundle()))
        }
    }

    companion object {
        fun createInstance(bundle: Bundle): ConfirmationFragment {
            val fragment = ConfirmationFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}