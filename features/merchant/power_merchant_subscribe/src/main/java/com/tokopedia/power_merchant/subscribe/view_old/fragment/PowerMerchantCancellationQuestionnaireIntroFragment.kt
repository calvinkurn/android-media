package com.tokopedia.power_merchant.subscribe.view_old.fragment



import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.gm.common.constant.GMParamTracker
import com.tokopedia.gm.common.utils.PowerMerchantTracking
import com.tokopedia.kotlin.extensions.view.toFloatOrZero
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view_old.activity.PMCancellationQuestionnaireActivity
import com.tokopedia.power_merchant.subscribe.view_old.model.PMCancellationQuestionnaireRateModel
import com.tokopedia.power_merchant.subscribe.view_old.model.PMCancellationQuestionnaireStepperModel
import kotlinx.android.synthetic.main.fragment_power_merchant_cancellation_questionnaire_intro.*
import kotlinx.android.synthetic.main.fragment_power_merchant_cancellation_questionnaire_intro.view.*
import kotlinx.android.synthetic.main.pm_cancellation_questionnaire_button_layout.view.*
import javax.inject.Inject

class PowerMerchantCancellationQuestionnaireIntroFragment : BaseDaggerFragment() {

    @Inject
    lateinit var tracker: PowerMerchantTracking

    private lateinit var parentActivity: PMCancellationQuestionnaireActivity
    private var stepperModel: PMCancellationQuestionnaireStepperModel? = null
    private var deactivateDate = ""
    private var position: Int = -1
    private var pmCancellationQuestionnaireRateModel: PMCancellationQuestionnaireRateModel? = null

    companion object {
        private const val EXTRA_POSITION = "position"
        private const val EXTRA_DEACTIVATE_DATE = "deactivate_date"
        private const val EXTRA_RATE_MODEL = "rate_model"

        fun createInstance(position: Int, deactivateDate: String, pmCancellationQuestionnaireRateModel: PMCancellationQuestionnaireRateModel): PowerMerchantCancellationQuestionnaireIntroFragment {
            val fragment = PowerMerchantCancellationQuestionnaireIntroFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_POSITION, position)
            bundle.putString(EXTRA_DEACTIVATE_DATE, deactivateDate)
            bundle.putParcelable(EXTRA_RATE_MODEL,pmCancellationQuestionnaireRateModel)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getScreenName(): String = getQuestionnaireScreenName()

    override fun initInjector() {
        getComponent(BaseAppComponent::class.java)?.let {
            DaggerPowerMerchantSubscribeComponent.builder()
                .baseAppComponent(it)
                .build()
                .inject(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        parentActivity = (activity as PMCancellationQuestionnaireActivity)
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
            if (it.listQuestionnaireAnswer[position].answers.isEmpty()) {
                questionnaire_rating.rating = 0f
            } else {
                questionnaire_rating.rating = it.listQuestionnaireAnswer[position].answers[0].toFloatOrZero()
            }
        }
    }

    private fun getArgumentData() {
        arguments?.let {
            position = it.getInt(EXTRA_POSITION)
            stepperModel = it.getParcelable(BaseStepperActivity.STEPPER_MODEL_EXTRA)
            deactivateDate = it.getString(EXTRA_DEACTIVATE_DATE, "")
            pmCancellationQuestionnaireRateModel = it.getParcelable(EXTRA_RATE_MODEL)
        }
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
            val description = getString(
                    R.string.pm_label_cancellation_questionnaire_intro_desc,
                    deactivateDate
            )
            val spannedDescription = spanBoldText(description, deactivateDate)
            tv_description.text = spannedDescription
            tv_question.text = pmCancellationQuestionnaireRateModel?.question
            questionnaire_rating.setOnRatingBarChangeListener { _, selectedRating, _ ->
                stepperModel?.let {
                    val rating = selectedRating.toInt()
                    if (it.listQuestionnaireAnswer[position].answers.isEmpty()) {
                        it.listQuestionnaireAnswer[position].answers.add(rating.toString())
                    } else {
                        it.listQuestionnaireAnswer[position].answers[0] = rating.toString()
                    }
                    trackClickQuestionnaireRating(rating)
                    mapRatingToTextView(rating)
                }
            }
        }
    }

    private fun trackClickQuestionnaireRating(rating: Int) {
        tracker.eventClickCancellationQuestionnaireRating(rating)
    }

    private fun spanBoldText(stringSource: String, stringToBeSpanned: String): CharSequence {
        return SpannableString(stringSource).apply {
            val startIndex = stringSource.indexOf(stringToBeSpanned)
            val endIndex = startIndex + stringToBeSpanned.length
            if (startIndex == -1)
                return stringSource
            setSpan(
                    StyleSpan(Typeface.BOLD),
                    startIndex,
                    endIndex,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
        }
    }

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

    private fun getQuestionnaireScreenName(): String {
        return "${GMParamTracker.ScreenName.PM_CANCEL_QUESTIONNAIRE}${position+1}"
    }
}
