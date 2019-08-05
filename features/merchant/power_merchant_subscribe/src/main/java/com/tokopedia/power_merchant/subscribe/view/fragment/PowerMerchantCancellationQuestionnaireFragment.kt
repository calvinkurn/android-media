package com.tokopedia.power_merchant.subscribe.view.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.model.PMCancellationQuestionnaireModel
import com.tokopedia.power_merchant.subscribe.view.activity.PowerMerchantCancellationQuestionnaireActivity
import kotlinx.android.synthetic.main.fragment_power_merchant_cancellation_questionnaire.view.*
import kotlinx.android.synthetic.main.pm_cancellation_questionnaire_button_layout.view.*

class PowerMerchantCancellationQuestionnaireFragment : BaseDaggerFragment() {

    private lateinit var parentActivity: PowerMerchantCancellationQuestionnaireActivity
    companion object {
        private var model: PMCancellationQuestionnaireModel? = null
        fun createInstance(model: PMCancellationQuestionnaireModel): PowerMerchantCancellationQuestionnaireFragment {
            val frag = PowerMerchantCancellationQuestionnaireFragment()
            val bundle = Bundle()
//            bundle.putParcelable("model", model)
//            frag.arguments = bundle
            this.model = model
            return frag
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        parentActivity = (activity as PowerMerchantCancellationQuestionnaireActivity)
        return inflater.inflate(R.layout.fragment_power_merchant_cancellation_questionnaire, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(view) {
            button_next.text = if (parentActivity.isFinalPage()) {
                "Kirim Jawaban"
            } else {
                "Selanjutnya"
            }
            button_next.setOnClickListener {
                if (parentActivity.isFinalPage()) {
                    parentActivity.finishPage()
                } else {
                    parentActivity.goToNextPage(null)
                }
            }
//            arguments?.let {
//                model = it.getParcelable("model")
//                if(model is PMCancellationQuestionnaireModel.LinearScaleQuestionnaire){
//                    tv_page.text = "Linear Scale"
//                }else if(model is PMCancellationQuestionnaireModel.MultipleChecklistQuestionnaire){
//                    tv_page.text = "Multiple Checklist"
//                }
//            }
        }
        super.onViewCreated(view, savedInstanceState)
    }


}
