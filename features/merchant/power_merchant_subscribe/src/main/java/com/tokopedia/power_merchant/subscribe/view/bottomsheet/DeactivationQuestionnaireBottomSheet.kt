package com.tokopedia.power_merchant.subscribe.view.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.data.source.cloud.model.PMCancellationQuestionnaireAnswerModel
import com.tokopedia.gm.common.presentation.model.DeactivationResultUiModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.analytics.tracking.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantErrorLogger
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantSpannableUtil
import com.tokopedia.power_merchant.subscribe.databinding.BottomSheetPmDeactivationQuestionnaireBinding
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.activity.FallbackActivity
import com.tokopedia.power_merchant.subscribe.view.adapter.QuestionnaireAdapterFactoryImpl
import com.tokopedia.power_merchant.subscribe.view.model.DeactivationQuestionnaireUiModel
import com.tokopedia.power_merchant.subscribe.view.model.QuestionnaireUiModel
import com.tokopedia.power_merchant.subscribe.view.viewmodel.DeactivationViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 05/03/21
 */

class DeactivationQuestionnaireBottomSheet :
    BaseBottomSheet<BottomSheetPmDeactivationQuestionnaireBinding>() {

    companion object {
        private const val TAG = "DeactivationBottomSheet"
        private const val KEY_PM_TIRE_TYPE = "key_pm_tier"
        private const val KEY_CURRENT_PM_TIRE_TYPE = "key_current_pm_tier"
        private const val KEY_PM_EXPIRED_DATE = "key_expired_date"

        fun createInstance(
            expiredDate: String,
            currentPmTireType: Int,
            pmTireType: Int
        ): DeactivationQuestionnaireBottomSheet {
            return DeactivationQuestionnaireBottomSheet().apply {
                overlayClickDismiss = false
                isFullpage = true
                arguments = Bundle().apply {
                    putString(KEY_PM_EXPIRED_DATE, expiredDate)
                    putInt(KEY_PM_TIRE_TYPE, pmTireType)
                    putInt(KEY_CURRENT_PM_TIRE_TYPE, currentPmTireType)
                }
            }
        }
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var powerMerchantTracking: PowerMerchantTracking

    private val mViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(DeactivationViewModel::class.java)
    }

    private val questionnaireAdapter by lazy {
        BaseListAdapter<QuestionnaireUiModel, QuestionnaireAdapterFactoryImpl>(
            QuestionnaireAdapterFactoryImpl(::setOnQuestionnaireSelected)
        )
    }
    private var onDeactivationSuccess: (() -> Unit)? = null


    override fun bind(view: View) = BottomSheetPmDeactivationQuestionnaireBinding.bind(view)

    override fun getChildResLayout(): Int = R.layout.bottom_sheet_pm_deactivation_questionnaire

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeQuestions()
        observePmDeactivationStatus()
    }

    override fun initInjector() {
        activity?.let {
            val appComponent = (it.application as BaseMainApplication).baseAppComponent
            DaggerPowerMerchantSubscribeComponent.builder()
                .baseAppComponent(appComponent)
                .build()
                .inject(this)
        }
    }

    override fun setupView() = binding?.run {
        setupQuestionnaireList()

        btnPmDeactivationSubmit.setOnClickListener {
            submitDeactivationPm()
        }

        btnPmDeactivationCancel.setOnClickListener {
            dismiss()
        }
    }

    fun setOnDeactivationSuccess(callback: () -> Unit) {
        this.onDeactivationSuccess = callback
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun setupQuestionnaireList() = binding?.rvPmQuestionnaireList?.run {
        layoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean = false
        }
        adapter = questionnaireAdapter
    }

    private fun observeQuestions() {
        getDeactivationQuestionnaire(true)
        mViewModel.pmCancellationQuestionnaireData.observe(viewLifecycleOwner, Observer {
            hideProgress()
            when (it) {
                is Success -> showQuestionList(it.data)
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                    val ctaText = getString(R.string.error_cancellation_tryagain)
                    showToaster(errorMessage, ctaText, Snackbar.LENGTH_INDEFINITE) {
                        getDeactivationQuestionnaire(false)
                    }
                    logLoCrashlytic(
                        it.throwable,
                        PowerMerchantErrorLogger.PM_DEACTIVATION_QUESTIONNAIRE_ERROR
                    )
                }
            }
        })
    }

    private fun logLoCrashlytic(throwable: Throwable, message: String) {
        PowerMerchantErrorLogger.logToCrashlytic(message, throwable)
    }

    private fun observePmDeactivationStatus() {
        mViewModel.pmDeactivateStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> setOnDeactivationSuccess(it.data)
                is Fail -> {
                    setOnDeactivationFail(it.throwable)
                    logLoCrashlytic(it.throwable, PowerMerchantErrorLogger.PM_DEACTIVATION_ERROR)
                }
            }
        })
    }

    private fun setOnDeactivationFail(throwable: Throwable) {
        hideButtonProgress()
        val message = when (throwable) {
            is UnknownHostException -> getString(R.string.pm_network_error_message)
            else -> getString(R.string.pm_system_error_message)
        }
        val ctaText = getString(R.string.pm_try_again)
        showToaster(message, ctaText, Toaster.LENGTH_LONG) {
            submitDeactivationPm()
        }
    }

    private fun setOnDeactivationSuccess(data: DeactivationResultUiModel) {
        when {
            data.errorCode.isBlank() -> {
                hideButtonProgress()
                onDeactivationSuccess?.invoke()
                dismiss()
            }
            data.shouldUpdateApp() -> openFallbackPage()
            else -> setOnDeactivationFail(RuntimeException())
        }
    }

    private fun openFallbackPage() {
        context?.let {
            FallbackActivity.startActivity(it)
            dismiss()
        }
    }

    private fun hideProgress() {
        binding?.progressPmDeactivation?.gone()
    }

    private fun getDeactivationQuestionnaire(isFirstLoad: Boolean) {
        binding?.progressPmDeactivation?.visible()
        val pmTireType = getPmTireType()
        mViewModel.getPMCancellationQuestionnaireData(pmTireType, isFirstLoad)
    }

    private fun getPmTireType(): Int {
        return arguments?.getInt(KEY_PM_TIRE_TYPE, PMConstant.ShopTierType.POWER_MERCHANT)
            ?: PMConstant.ShopTierType.POWER_MERCHANT
    }

    private fun showToaster(
        message: String,
        ctaText: String,
        duration: Int,
        action: () -> Unit = {}
    ) {
        view?.let {
            Toaster.toasterCustomBottomHeight =
                it.context.resources.getDimensionPixelSize(R.dimen.pm_spacing_100dp)
            Toaster.build(
                it.rootView, message, duration, Toaster.TYPE_ERROR, ctaText
            ) {
                action()
            }.show()
        }
    }

    private fun showQuestionList(data: DeactivationQuestionnaireUiModel) {
        questionnaireAdapter.data.clear()
        questionnaireAdapter.addElement(data.listQuestion)

        binding?.run {
            val expiredDateStr = arguments?.getString(KEY_PM_EXPIRED_DATE).orEmpty()
            val currentFormat = "dd MMMM yyyy hh:mm:ss"
            val newDateFormat = "dd MMMM yyyy, hh:mm"
            val expiredDateFmt = DateFormatUtils.formatDate(
                currentFormat,
                newDateFormat,
                expiredDateStr
            )
            tvPmDeactivationInfo.text = root.context.getString(
                R.string.pm_label_deactivation_questionnaire_intro_desc,
                expiredDateFmt
            ).parseAsHtml()
            nestedScrollPmDeactivation.visible()
            containerPmFooterDeactivation.visible()
            tvPmDeactivateTnc.text = getString(
                R.string.pm_tnc_description_pm_deactivation
            ).parseAsHtml()
            tvPmDeactivateTnc.setOnClickListener {
                showTncBottomSheet()
            }
        }
    }

    private fun showTncBottomSheet() {
        val bottomSheet = PMTermAndConditionBottomSheet.newInstance()
        if (childFragmentManager.isStateSaved || bottomSheet.isAdded) {
            return
        }

        bottomSheet.show(childFragmentManager)
    }

    private fun setOnQuestionnaireSelected() {
        binding?.btnPmDeactivationSubmit?.isEnabled = questionnaireAdapter.data
            .all { !it.isNoAnswer() }
    }

    private fun submitDeactivationPm() {
        val answers = mutableListOf<PMCancellationQuestionnaireAnswerModel>()

        questionnaireAdapter.data.forEach {
            when (it) {
                is QuestionnaireUiModel.QuestionnaireSingleOptionUiModel -> {
                    val answer = PMCancellationQuestionnaireAnswerModel(
                        question = it.question,
                        answers = it.getAnswerList().toMutableList()
                    )
                    answers.add(answer)
                }
                is QuestionnaireUiModel.QuestionnaireMultipleOptionUiModel -> {
                    val answer = PMCancellationQuestionnaireAnswerModel(
                        question = it.question,
                        answers = it.getAnswerList().toMutableList()
                    )
                    answers.add(answer)
                }
            }
        }

        val hasAnswerGiven = questionnaireAdapter.data.all { !it.isNoAnswer() }
        if (!hasAnswerGiven) {
            val errorMessage = getString(R.string.pm_all_questionnaire_must_be_answered)
            val ctaText = getString(R.string.power_merchant_ok_label)
            showToaster(errorMessage, ctaText, Snackbar.LENGTH_LONG) {
                getDeactivationQuestionnaire(false)
            }
            return
        }

        showButtonProgress()
        mViewModel.submitPmDeactivation(answers)
        powerMerchantTracking.sendEventClickSubmitQuestionnaire()
    }

    private fun showButtonProgress() = binding?.run {
        btnPmDeactivationCancel.isEnabled = false
        btnPmDeactivationSubmit.isLoading = true
    }

    private fun hideButtonProgress() = binding?.run {
        btnPmDeactivationCancel.isEnabled = true
        btnPmDeactivationSubmit.isLoading = false
    }
}
