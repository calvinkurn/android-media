package com.tokopedia.power_merchant.subscribe.view.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.activity.PMCancellationQuestionnaireActivity
import com.tokopedia.power_merchant.subscribe.view.adapter.MultipleOptionAdapter
import com.tokopedia.power_merchant.subscribe.view.model.PMCancellationQuestionnaireMultipleOptionModel
import com.tokopedia.power_merchant.subscribe.view.model.PMCancellationQuestionnaireStepperModel
import kotlinx.android.synthetic.main.fragment_power_merchant_cancellation_questionnaire.view.*
import kotlinx.android.synthetic.main.pm_cancellation_questionnaire_button_layout.view.*

class PowerMerchantCancellationQuestionnaireMultipleOptionFragment
    : BaseDaggerFragment(), MultipleOptionAdapter.MultipleOptionAdapterListener {

    private lateinit var parentActivity: PMCancellationQuestionnaireActivity
    private var stepperModel: PMCancellationQuestionnaireStepperModel? = null
    private var modelMultipleOptionQuestionnaire: PMCancellationQuestionnaireMultipleOptionModel? = null
    private var position: Int = -1
    private val multipleOptionAdapter by lazy {
        MultipleOptionAdapter(this)
    }

    companion object {
        private const val EXTRA_POSITION = "position"
        private const val EXTRA_MODEL_CHECKLIST_QUESTIONNAIRE = "model_checklist_questionnaire"

        fun createInstance(
                position: Int,
                model: PMCancellationQuestionnaireMultipleOptionModel
        ): PowerMerchantCancellationQuestionnaireMultipleOptionFragment {
            val fragment = PowerMerchantCancellationQuestionnaireMultipleOptionFragment()
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
        parentActivity = (activity as PMCancellationQuestionnaireActivity)
        return inflater.inflate(R.layout.fragment_power_merchant_cancellation_questionnaire, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgumentData()
        initLayout(view)
        populateOption()
    }

    private fun initLayout(view: View) {
        with(view) {
            button_next.text = if (parentActivity.isFinalPage()) {
                getString(R.string.label_send_answer)
            } else {
                getString(com.tokopedia.abstraction.R.string.label_next)
            }
            button_next.setOnClickListener {
                stepperModel?.let {
                    parentActivity.goToNextPage(it)
                }
            }
            button_back.setOnClickListener {
                parentActivity.goToPreviousPage()
            }
            tv_question.text = modelMultipleOptionQuestionnaire?.question
            recycler_view_option.layoutManager = LinearLayoutManager(context)
            recycler_view_option.adapter = multipleOptionAdapter
        }
    }

    private fun populateOption() {
        modelMultipleOptionQuestionnaire?.listOptionModel?.let { listOptionModel ->
            stepperModel?.listQuestionnaireAnswer?.let { listQuestionnaireAnswer ->
                listOptionModel.forEach {
                    it.isChecked = listQuestionnaireAnswer[position].answers.contains(it.value)
                }
            }
            multipleOptionAdapter.listOption = listOptionModel
            multipleOptionAdapter.notifyDataSetChanged()
        }
    }

    private fun getArgumentData() {
        arguments?.let {
            stepperModel = it.getParcelable(BaseStepperActivity.STEPPER_MODEL_EXTRA)
            position = it.getInt(EXTRA_POSITION)
            modelMultipleOptionQuestionnaire = it.getParcelable(EXTRA_MODEL_CHECKLIST_QUESTIONNAIRE)
        }
    }

    override fun onOptionChecked(isChecked: Boolean, optionValue: String) {
        stepperModel?.listQuestionnaireAnswer?.let {
            if (isChecked) {
                it[position].answers.add(optionValue)
            } else {
                it[position].answers.remove(optionValue)
            }
        }
    }

}
