package com.tokopedia.power_merchant.subscribe.view.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.model.PMCancellationQuestionnaireModel
import com.tokopedia.power_merchant.subscribe.model.PMCancellationQuestionnaireStepperModel
import com.tokopedia.power_merchant.subscribe.view.activity.PowerMerchantCancellationQuestionnaireActivity
import kotlinx.android.synthetic.main.fragment_power_merchant_cancellation_questionnaire.*
import kotlinx.android.synthetic.main.fragment_power_merchant_cancellation_questionnaire.view.*
import kotlinx.android.synthetic.main.pm_cancellation_questionnaire_button_layout.view.*

class PowerMerchantCancellationQuestionnaireMultipleCheckboxFragment : BaseDaggerFragment() {

    private lateinit var parentActivity: PowerMerchantCancellationQuestionnaireActivity
    private var stepperModel: PMCancellationQuestionnaireStepperModel? = null
    private var modelChecklistQuestionnaire: PMCancellationQuestionnaireModel.MultipleChecklistQuestionnaire? = null
    private var position: Int = -1

    companion object {
        private const val EXTRA_POSITION = "position"
        private const val EXTRA_MODEL_CHECKLIST_QUESTIONNAIRE = "model_checklist_questionnaire"

        fun createInstance(
                position: Int,
                model: PMCancellationQuestionnaireModel.MultipleChecklistQuestionnaire
        ): PowerMerchantCancellationQuestionnaireMultipleCheckboxFragment {
            val fragment = PowerMerchantCancellationQuestionnaireMultipleCheckboxFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_POSITION, position)
            bundle.putParcelable(EXTRA_MODEL_CHECKLIST_QUESTIONNAIRE, model)
            fragment.arguments = bundle
            return fragment
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
        super.onViewCreated(view, savedInstanceState)
        getArgumentData()
        initLayout(view)
        populateCheckbox(view)
    }

    private fun initLayout(view: View) {
        with(view) {
            button_next.isEnabled = false
            button_next.text = if (parentActivity.isFinalPage()) {
                getString(R.string.label_send_answer)
            } else {
                getString(R.string.label_next)
            }
            button_next.setOnClickListener {
                if (parentActivity.isFinalPage()) {

                    parentActivity.finishPage()
                } else {
                    parentActivity.goToNextPage(stepperModel)
                }
            }
            tv_question.text = modelChecklistQuestionnaire?.questionString
        }
    }

    private fun populateCheckbox(view: View) {
        modelChecklistQuestionnaire?.listChecklistOption?.forEachIndexed { index, option ->
            stepperModel?.listChoice?.let { listChoice ->
                val checkBox = LayoutInflater.from(context).inflate(
                        R.layout.checkbox_layout,
                        checkbox_container,
                        false
                ) as CheckBox
                checkBox.tag = index
                checkBox.text = option
                checkBox.setOnCheckedChangeListener { compoundButton, _ ->
                    compoundButton.apply {
                        if (isChecked) {
                            listChoice[position].add(tag as Int)
                        } else {
                            listChoice[position].remove(tag as Int)
                        }
                        with(view){
                            button_next.isEnabled = listChoice[position].isNotEmpty()
                        }
                    }
                }
                checkBox.isChecked = listChoice[position].contains(index)
                checkbox_container.addView(checkBox)
            }
        }
    }

    private fun getArgumentData() {
        arguments?.let {
            stepperModel = it.getParcelable(BaseStepperActivity.STEPPER_MODEL_EXTRA)
            position = it.getInt(EXTRA_POSITION)
            modelChecklistQuestionnaire = it.getParcelable(EXTRA_MODEL_CHECKLIST_QUESTIONNAIRE)
        }
    }


}
