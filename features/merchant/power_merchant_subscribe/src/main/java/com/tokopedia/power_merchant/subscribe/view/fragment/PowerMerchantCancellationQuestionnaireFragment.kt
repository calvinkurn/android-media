package com.tokopedia.power_merchant.subscribe.view.fragment


import android.os.Bundle
import android.support.v7.widget.AppCompatCheckBox
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.model.PMCancellationQuestionnaireModel
import com.tokopedia.power_merchant.subscribe.model.PMCancellationQuestionnaireStepperModel
import com.tokopedia.power_merchant.subscribe.view.activity.PowerMerchantCancellationQuestionnaireActivity
import kotlinx.android.synthetic.main.fragment_power_merchant_cancellation_questionnaire.*
import kotlinx.android.synthetic.main.fragment_power_merchant_cancellation_questionnaire.view.*
import kotlinx.android.synthetic.main.fragment_power_merchant_terms.*
import kotlinx.android.synthetic.main.pm_cancellation_questionnaire_button_layout.view.*

class PowerMerchantCancellationQuestionnaireFragment : BaseDaggerFragment() {

    private lateinit var parentActivity: PowerMerchantCancellationQuestionnaireActivity
    private var stepperModel: PMCancellationQuestionnaireStepperModel? = null

    companion object {
        private var modelChecklistQuestionnaire: PMCancellationQuestionnaireModel.MultipleChecklistQuestionnaire? = null
        private var position: Int = -1
        fun createInstance(
                position: Int,
                model: PMCancellationQuestionnaireModel.MultipleChecklistQuestionnaire
        ): PowerMerchantCancellationQuestionnaireFragment {
            val frag = PowerMerchantCancellationQuestionnaireFragment()
            this.modelChecklistQuestionnaire = model
            this.position = position
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
        getArgumentData()
        initLayout(view)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initLayout(view: View) {
        with(view) {
            button_next.isEnabled = false
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
            tv_question.text = modelChecklistQuestionnaire?.questionString
            populateCheckbox()
        }
    }

    private fun populateCheckbox() {
        modelChecklistQuestionnaire?.listChecklistOption?.forEachIndexed { index, option ->
            val checkBox = AppCompatCheckBox(context)
            checkBox.tag = index
            checkbox.text = option
            checkbox.setOnClickListener {
                (it as AppCompatCheckBox).apply {
                    stepperModel?.listChoice?.let { listChoice ->
                        if (isChecked) {
                            listChoice[position].add(tag as Int)
                        } else {
                            listChoice[position].remove(tag as Int)
                        }
                        button_next.isEnabled = listChoice[position].isNotEmpty()
                    }
                }
            }
            checkbox_container.addView(checkBox)
        }

    }

    private fun getArgumentData() {
        arguments?.let {
            stepperModel = it.getParcelable(BaseStepperActivity.STEPPER_MODEL_EXTRA)
        }
    }


}
