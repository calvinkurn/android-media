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
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.adapter.QuestionnaireAdapterFactoryImpl
import com.tokopedia.power_merchant.subscribe.view.model.DeactivationQuestionnaireUiModel
import com.tokopedia.power_merchant.subscribe.view.model.QuestionnaireUiModel
import com.tokopedia.power_merchant.subscribe.view.viewmodel.DeactivationViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.bottom_sheet_pm_deactivation_questionnaire.view.*
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 05/03/21
 */

class DeactivationQuestionnaireBottomSheet : BaseBottomSheet() {

    companion object {
        private const val TAG = "DeactivationBottomSheet"
        private const val KEY_PM_TIRE_TYPE = "key_pm_tier"

        fun createInstance(pmTireType: Int): DeactivationQuestionnaireBottomSheet {
            return DeactivationQuestionnaireBottomSheet().apply {
                overlayClickDismiss = false
                isFullpage = true
                arguments = Bundle().apply {
                    putInt(KEY_PM_TIRE_TYPE, pmTireType)
                }
            }
        }
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val mViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
                .get(DeactivationViewModel::class.java)
    }

    private val questionnaireAdapter by lazy {
        BaseListAdapter<QuestionnaireUiModel, QuestionnaireAdapterFactoryImpl>(QuestionnaireAdapterFactoryImpl())
    }
    private var onDeactivationSuccess: (() -> Unit)? = null

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

    override fun setupView() = childView?.run {
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

    private fun setupQuestionnaireList() = childView?.rvPmQuestionnaireList?.run {
        layoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean = false
        }
        adapter = questionnaireAdapter
    }

    private fun observeQuestions() {
        getDeactivationQuestionnaire()
        mViewModel.pmCancellationQuestionnaireData.observe(viewLifecycleOwner, Observer {
            hideProgress()
            when (it) {
                is Success -> showQuestionList(it.data)
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                    val ctaText = getString(R.string.error_cancellation_tryagain)
                    showToaster(errorMessage, ctaText, Snackbar.LENGTH_INDEFINITE) {
                        getDeactivationQuestionnaire()
                    }
                }
            }
        })
    }

    private fun observePmDeactivationStatus() {
        mViewModel.isSuccessDeactivate.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> setOnDeactivationSuccess(it.data)
                is Fail -> setOnDeactivationFail(it.throwable)
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

    private fun setOnDeactivationSuccess(isSuccess: Boolean) {
        if (isSuccess) {
            hideButtonProgress()
            onDeactivationSuccess?.invoke()
            dismiss()
        } else {
            setOnDeactivationFail(RuntimeException())
        }
    }

    private fun hideProgress() {
        childView?.progressPmDeactivation?.gone()
    }

    private fun getDeactivationQuestionnaire() {
        childView?.progressPmDeactivation?.visible()
        val pmTireType = arguments?.getInt(KEY_PM_TIRE_TYPE, PMConstant.ShopTierType.POWER_MERCHANT) ?: PMConstant.ShopTierType.POWER_MERCHANT
        mViewModel.getPMCancellationQuestionnaireData(userSession.shopId, pmTireType)
    }

    private fun showToaster(message: String, ctaText: String, duration: Int, action: () -> Unit = {}) {
        view?.let {
            Toaster.toasterCustomBottomHeight = it.context.resources.getDimensionPixelSize(R.dimen.pm_spacing_100dp)
            Toaster.build(it.rootView, message, duration, Toaster.TYPE_ERROR, ctaText,
                    View.OnClickListener {
                        action()
                    }
            ).show()
        }
    }

    private fun showQuestionList(data: DeactivationQuestionnaireUiModel) {
        questionnaireAdapter.data.clear()
        questionnaireAdapter.addElement(data.listQuestion)

        childView?.run {
            val expiredDateFmt = DateFormatUtils.formatDate(
                    DateFormatUtils.FORMAT_YYYY_MM_DD,
                    DateFormatUtils.FORMAT_D_MMMM_YYYY,
                    data.expiredDate
            )
            tvPmDeactivationInfo?.text = context.getString(R.string.pm_label_deactivation_questionnaire_intro_desc, expiredDateFmt).parseAsHtml()
            nestedScrollPmDeactivation.visible()
            containerPmFooterDeactivation.visible()
        }
    }

    private fun submitDeactivationPm() {
        val answers = mutableListOf<PMCancellationQuestionnaireAnswerModel>()
        var isNoAnswer = false
        questionnaireAdapter.data.forEach {
            when (it) {
                is QuestionnaireUiModel.QuestionnaireRatingUiModel -> {
                    val answer = PMCancellationQuestionnaireAnswerModel(
                            question = it.question,
                            answers = mutableListOf(it.givenRating.toString())
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

            if (!isNoAnswer) {
                isNoAnswer = it.isNoAnswer()
            }
        }

        if (isNoAnswer) {
            val errorMessage = getString(R.string.pm_all_questionnaire_must_be_answered)
            val ctaText = getString(R.string.power_merchant_ok_label)
            showToaster(errorMessage, ctaText, Snackbar.LENGTH_LONG) {
                getDeactivationQuestionnaire()
            }
            isNoAnswer = false
            return
        }

        showButtonProgress()
        mViewModel.submitPmDeactivation(answers)
    }

    private fun showButtonProgress() = childView?.run {
        btnPmDeactivationCancel.isEnabled = false
        btnPmDeactivationSubmit.isLoading = true
    }

    private fun hideButtonProgress() = childView?.run {
        btnPmDeactivationCancel.isEnabled = true
        btnPmDeactivationSubmit.isLoading = false
    }
}