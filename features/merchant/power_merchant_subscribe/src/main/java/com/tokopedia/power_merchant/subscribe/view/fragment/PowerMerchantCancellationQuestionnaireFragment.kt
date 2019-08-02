package com.tokopedia.power_merchant.subscribe.view.fragment


import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.activity.PowerMerchantCancellationQuestionnaireActivity
import kotlinx.android.synthetic.main.fragment_power_merchant_cancellation_questionnaire.view.*

class PowerMerchantCancellationQuestionnaireFragment : BaseDaggerFragment() {

    private lateinit var parentActivity: PowerMerchantCancellationQuestionnaireActivity

    companion object {
        fun createInstance(textParam: String): PowerMerchantCancellationQuestionnaireFragment {
            val frag = PowerMerchantCancellationQuestionnaireFragment()
            val bundle = Bundle()
            bundle.putString("text", textParam)
            frag.arguments = bundle
            return frag
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        parentActivity = (activity as PowerMerchantCancellationQuestionnaireActivity)
        return inflater.inflate(R.layout.fragment_power_merchant_cancellation_questionnaire, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(view) {
            button_next.text = if (parentActivity.isFinalPage()) {
                "Finish"
            } else {
                "Next"
            }
            button_next.setOnClickListener {
                if (parentActivity.isFinalPage()) {
                    parentActivity.finishPage()
                } else {
                    parentActivity.goToNextPage(null)
                }
            }
            arguments?.let {
                tv_page.text = it.getString("text")
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }


}
