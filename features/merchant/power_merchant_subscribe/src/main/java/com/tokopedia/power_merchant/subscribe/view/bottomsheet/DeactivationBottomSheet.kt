package com.tokopedia.power_merchant.subscribe.view.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
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
import kotlinx.android.synthetic.main.bottom_sheet_pm_deactivation.view.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 05/03/21
 */

class DeactivationBottomSheet : BaseBottomSheet() {

    companion object {
        private const val TAG = "DeactivationBottomSheet"

        fun createInstance(): DeactivationBottomSheet {
            return DeactivationBottomSheet().apply {
                overlayClickDismiss = false
                isFullpage = true
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

    override fun getChildResLayout(): Int = R.layout.bottom_sheet_pm_deactivation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeQuestions()
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
                is Fail -> showToaster(it.throwable) {
                    getDeactivationQuestionnaire()
                }
            }
        })
    }

    private fun hideProgress() {
        childView?.progressPmDeactivation?.gone()
    }

    private fun getDeactivationQuestionnaire() {
        childView?.progressPmDeactivation?.visible()
        mViewModel.getPMCancellationQuestionnaireData(userSession.shopId)
    }

    private fun showToaster(throwable: Throwable, action: () -> Unit) {
        childView?.containerPmDeactivation?.let {
            Toaster.build(it, ErrorHandler.getErrorMessage(context, throwable),
                    Snackbar.LENGTH_INDEFINITE, Toaster.TYPE_ERROR,
                    getString(R.string.error_cancellation_tryagain),
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
}