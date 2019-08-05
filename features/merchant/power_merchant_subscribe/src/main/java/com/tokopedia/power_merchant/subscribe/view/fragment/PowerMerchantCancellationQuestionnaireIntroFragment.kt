package com.tokopedia.power_merchant.subscribe.view.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.model.PMCancellationQuestionnaireStepperModel
import com.tokopedia.power_merchant.subscribe.view.activity.PowerMerchantCancellationQuestionnaireActivity
import kotlinx.android.synthetic.main.fragment_power_merchant_cancellation_questionnaire_intro.*
import kotlinx.android.synthetic.main.fragment_power_merchant_cancellation_questionnaire_intro.view.*
import kotlinx.android.synthetic.main.pm_cancellation_questionnaire_button_layout.view.*

class PowerMerchantCancellationQuestionnaireIntroFragment : BaseDaggerFragment() {

    private lateinit var parentActivity: PowerMerchantCancellationQuestionnaireActivity
    private var stepperModel: PMCancellationQuestionnaireStepperModel? = null
    private var deactivateDate = ""

    companion object {
        private const val EXTRA_DEACTIVATE_DATE = "deactivate_date"
        fun createInstance(deactivateDate: String): PowerMerchantCancellationQuestionnaireIntroFragment {
            val fragment = PowerMerchantCancellationQuestionnaireIntroFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_DEACTIVATE_DATE,deactivateDate)
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
        return inflater.inflate(
                R.layout.fragment_power_merchant_cancellation_questionnaire_intro,
                container,
                false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgumentData()
        initLayout(view)
        populateViewBasedOnModelData()
    }

    private fun populateViewBasedOnModelData() {
        stepperModel?.let {
            questionnaire_rating.rating = it.star.toFloat()
        }
    }

    private fun getArgumentData() {
        arguments?.let {
            stepperModel = it.getParcelable(BaseStepperActivity.STEPPER_MODEL_EXTRA)
            deactivateDate = it.getString(EXTRA_DEACTIVATE_DATE,"")
        }
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
            tv_description.text = getString(
                    R.string.pm_label_cancellation_questionnaire_intro_desc,
                    deactivateDate
            )
            questionnaire_rating.setOnRatingBarChangeListener { _, rating, _ ->
                button_next.isEnabled = isRatingNotZero(rating.toInt())
                stepperModel?.star = rating.toInt()
                mapRatingToTextView(rating.toInt())
            }
        }
    }

    private fun isRatingNotZero(rating: Int): Boolean = rating != 0

    private fun mapRatingToTextView(rating: Int) {
        when (rating) {
            1 -> {
                tv_rating.text = getString(
                        R.string.pm_cancellation_questionnaire_intro_rating_1_star
                )
            }
            2 -> {
                tv_rating.text = getString(
                        R.string.pm_cancellation_questionnaire_intro_rating_2_star
                )
            }
            3 -> {
                tv_rating.text = getString(
                        R.string.pm_cancellation_questionnaire_intro_rating_3_star
                )
            }
            4 -> {
                tv_rating.text = getString(
                        R.string.pm_cancellation_questionnaire_intro_rating_4_star
                )
            }
            5 -> {
                tv_rating.text = getString(
                        R.string.pm_cancellation_questionnaire_intro_rating_5_star
                )
            }
        }

    }


}
