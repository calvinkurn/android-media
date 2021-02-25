package com.tokopedia.power_merchant.subscribe.view_old.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.abstraction.base.view.model.StepperModel
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.gm.common.data.source.cloud.model.PMCancellationQuestionnaireAnswerModel
import com.tokopedia.gm.common.utils.PowerMerchantTracking
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view_old.fragment.PowerMerchantCancellationQuestionnaireIntroFragment
import com.tokopedia.power_merchant.subscribe.view_old.fragment.PowerMerchantCancellationQuestionnaireMultipleOptionFragment
import com.tokopedia.power_merchant.subscribe.view_old.model.PMCancellationQuestionnaireData
import com.tokopedia.power_merchant.subscribe.view_old.model.PMCancellationQuestionnaireMultipleOptionModel
import com.tokopedia.power_merchant.subscribe.view_old.model.PMCancellationQuestionnaireRateModel
import com.tokopedia.power_merchant.subscribe.view_old.model.PMCancellationQuestionnaireStepperModel
import com.tokopedia.power_merchant.subscribe.view_old.viewmodel.PMCancellationQuestionnaireViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.activity_pm_cancellation_questionnaire.*
import javax.inject.Inject

class PMCancellationQuestionnaireActivity : BaseStepperActivity(), HasComponent<BaseAppComponent?> {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var userSessionInterface: UserSessionInterface
    @Inject
    lateinit var powerMerchantTracking: PowerMerchantTracking

    private lateinit var viewModel: PMCancellationQuestionnaireViewModel

    lateinit var pmCancellationQuestionnaireData: PMCancellationQuestionnaireData

    private val listFragment by lazy {
        ArrayList<Fragment>()
    }

    private val pmCancellationQuestionnaireStepperModel by lazy {
        stepperModel as PMCancellationQuestionnaireStepperModel
    }

    companion object {
        const val QUESTIONNAIRE_DATA_EXTRA = "questionnaire_data_extra"
        fun newInstance(context: Context): Intent {
            return Intent(context, PMCancellationQuestionnaireActivity::class.java)
        }
    }
    override fun getListFragment(): MutableList<Fragment> {
        return listFragment
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_pm_cancellation_questionnaire
    }

    /**
     * DF PURPOSE
     */
    override fun getProgressBar(): Int {
        return R.id.stepper_progress_pm
    }

    override fun getParentView(): Int {
        return R.id.parent_view_pm
    }

    override fun getToolbarResourceID(): Int {
        return R.id.toolbarPm
    }

    private fun generateFragment() {
        val formattedExpiredDate = DateFormatUtils.formatDate(
                DateFormatUtils.FORMAT_YYYY_MM_DD,
                DateFormatUtils.FORMAT_D_MMMM_YYYY,
                pmCancellationQuestionnaireData.expiredDate
        )
        pmCancellationQuestionnaireData.listQuestion.forEachIndexed { index, questionModel ->
            if (index == 0 && questionModel is PMCancellationQuestionnaireRateModel) {
                listFragment.add(PowerMerchantCancellationQuestionnaireIntroFragment.createInstance(
                        index,
                        formattedExpiredDate,
                        questionModel
                ))
                if (isIndexNotExistsInListQuestionnaireAnswer(index)) {
                    pmCancellationQuestionnaireStepperModel.listQuestionnaireAnswer.add(
                            PMCancellationQuestionnaireAnswerModel(
                                    questionModel.question
                            )
                    )
                }

            } else if (questionModel is PMCancellationQuestionnaireMultipleOptionModel) {
                listFragment.add(PowerMerchantCancellationQuestionnaireMultipleOptionFragment.createInstance(
                        index,
                        questionModel
                ))
                if (isIndexNotExistsInListQuestionnaireAnswer(index)) {
                    pmCancellationQuestionnaireStepperModel.listQuestionnaireAnswer.add(
                            PMCancellationQuestionnaireAnswerModel(
                                    questionModel.question
                            )
                    )
                }
            }
        }
        setMaxProgressStepper(listFragment.size.toFloat())
        setupFragment(null)
        refreshCurrentProgressStepper()
    }

    private fun isIndexNotExistsInListQuestionnaireAnswer(index: Int): Boolean {
        return index >= pmCancellationQuestionnaireStepperModel.listQuestionnaireAnswer.size
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        initViewModel()
        initModelFromSavedInstanceState(savedInstanceState)
        super.onCreate(savedInstanceState)
        if (pmCancellationQuestionnaireData.listQuestion.isEmpty())
            getPMCancellationQuestionnaireData()
        else
            generateFragment()
        observePMCancellationQuestionnaireData()
        observeIsSuccessUnsubscribe()
    }

    private fun observeIsSuccessUnsubscribe() {
        viewModel.isSuccessUnsubscribe.observe(this, Observer {
            when (it) {
                is Success -> {
                    handleIsSuccessUnsubscribeResult(it.data)
                }
                is Fail -> {
                    hideProgressBarLoading()
                    showToasterRequestError(it.throwable, View.OnClickListener {
                        sendAnswer()
                    })
                }
            }
        })
    }

    private fun handleIsSuccessUnsubscribeResult(isSuccessUnsubscribe: Boolean) {
        if (isSuccessUnsubscribe) {
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            hideProgressBarLoading()
            showToasterSuccessUnsubscribeReturnFalse()
        }
    }

    private fun getPMCancellationQuestionnaireData() {
        showProgressBarLoading()
        viewModel.getPMCancellationQuestionnaireData(userSessionInterface.shopId)
    }

    private fun showProgressBarLoading() {
        layout_stepper_container.hide()
        progress_bar_loading.show()
    }

    private fun hideProgressBarLoading() {
        layout_stepper_container.show()
        progress_bar_loading.hide()
    }

    private fun initModelFromSavedInstanceState(data: Bundle?) {
        stepperModel = data?.let {
            data.getParcelable(STEPPER_MODEL_EXTRA) ?: PMCancellationQuestionnaireStepperModel()
        } ?: PMCancellationQuestionnaireStepperModel()
        pmCancellationQuestionnaireData = data?.let {
            data.getParcelable(QUESTIONNAIRE_DATA_EXTRA) ?: PMCancellationQuestionnaireData()
        } ?: PMCancellationQuestionnaireData()
    }

    private fun observePMCancellationQuestionnaireData() {
        viewModel.pmCancellationQuestionnaireData.observe(this, Observer {
            hideProgressBarLoading()
            when (it) {
                is Success -> {
                    pmCancellationQuestionnaireData = it.data
                    generateFragment()
                }
                is Fail -> {
                    showToasterRequestError(it.throwable, View.OnClickListener {
                        getPMCancellationQuestionnaireData()
                    })
                }
            }
        })
    }

    private fun showToasterRequestError(throwable: Throwable, onClickListener: View.OnClickListener) {
        Toaster.showErrorWithAction(
                findViewById(android.R.id.content),
                ErrorHandler.getErrorMessage(this, throwable),
                Snackbar.LENGTH_INDEFINITE,
                getString(R.string.error_cancellation_tryagain),
                onClickListener
        )
    }

    private fun showToasterCurrentQuestionNotAnswered() {
        Toaster.showErrorWithAction(
                findViewById(android.R.id.content),
                getString(R.string.pm_cancellation_questionnaire_some_question_not_answered_label),
                Snackbar.LENGTH_LONG,
                getString(com.tokopedia.design.R.string.close),
                View.OnClickListener {}
        )
    }

    private fun showToasterSuccessUnsubscribeReturnFalse() {
        Toaster.showErrorWithAction(
                findViewById(android.R.id.content),
                getString(com.tokopedia.abstraction.R.string.default_request_error_unknown),
                Snackbar.LENGTH_LONG,
                getString(com.tokopedia.design.R.string.close),
                View.OnClickListener {}
        )
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PMCancellationQuestionnaireViewModel::class.java)
    }

    private fun initInjector() {
        component?.let {
            DaggerPowerMerchantSubscribeComponent.builder()
                    .baseAppComponent(it)
                    .build().inject(this)
        }
    }

    fun isFinalPage(): Boolean = currentPosition == listFragment.size

    override fun updateToolbarTitle() {}

    override fun updateToolbarTitle(title: String?) {}

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(STEPPER_MODEL_EXTRA, stepperModel)
        outState.putParcelable(QUESTIONNAIRE_DATA_EXTRA, pmCancellationQuestionnaireData)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        sendClickOptionBackButtonTrackingEvent()
        val itemId = item.itemId
        return if (itemId == android.R.id.home) {
            finish()
            false
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun sendClickOptionBackButtonTrackingEvent() {
        when {
            isFirstPage() -> powerMerchantTracking.eventPMCancellationClickOptionBackButtonFirstPage()
            isFinalPage() -> powerMerchantTracking.eventPMCancellationClickOptionBackButtonLastPage()
            else -> powerMerchantTracking.eventPMCancellationClickOptionBackButtonMiddlePage()
        }
    }

    fun goToPreviousPage() {
        sendClickBackButtonTrackingEvent()
        onBackEvent()
    }

    override fun goToNextPage(stepperModel: StepperModel) {
        if (pmCancellationQuestionnaireStepperModel.isCurrentQuestionAnswered(currentPosition)) {
            if (isFinalPage()) {
                sendClickSendAnswerButtonTrackingEvent()
                sendAnswer()
            } else {
                sendClickNextQuestionButtonTrackingEvent()
                super.goToNextPage(stepperModel)
            }
        } else
            showToasterCurrentQuestionNotAnswered()
    }

    private fun sendClickSendAnswerButtonTrackingEvent() {
        val options = pmCancellationQuestionnaireStepperModel
            .listQuestionnaireAnswer[currentPosition - 1].answers
        powerMerchantTracking.eventClickCancellationQuestionnaireSendAnswer(options)
    }

    private fun sendClickNextQuestionButtonTrackingEvent() {
        when {
            isFirstPage() -> {
                val rating = pmCancellationQuestionnaireStepperModel
                        .listQuestionnaireAnswer[currentPosition - 1].answers[0].toIntOrZero()
                powerMerchantTracking.eventClickCancellationQuestionnaireSubmitRating(rating)
            }
            else -> {
                val options = pmCancellationQuestionnaireStepperModel
                    .listQuestionnaireAnswer[currentPosition - 1].answers
                powerMerchantTracking.eventClickCancellationQuestionnaireSubmitReason(options)
            }
        }
    }

    private fun sendClickBackButtonTrackingEvent() {
        when {
            isFirstPage() -> powerMerchantTracking.eventPMCancellationClickBackButtonFirstPage()
            isFinalPage() -> powerMerchantTracking.eventPMCancellationClickBackButtonLastPage()
            else -> powerMerchantTracking.eventPMCancellationClickBackButtonMiddlePage()
        }
    }

    private fun isFirstPage() = currentPosition == 1

    private fun sendAnswer() {
        showProgressBarLoading()
        viewModel.sendQuestionAnswerDataAndTurnOffAutoExtend(
                pmCancellationQuestionnaireStepperModel.listQuestionnaireAnswer
        )
    }

    override fun getComponent(): BaseAppComponent? {
        application?.let {
            return (it as BaseMainApplication).baseAppComponent
        }
        return null
    }
}
