package com.tokopedia.home_account.consentWithdrawal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.home_account.consentWithdrawal.common.TransactionType
import com.tokopedia.home_account.consentWithdrawal.data.ConsentPurposeDataModel
import com.tokopedia.home_account.consentWithdrawal.data.ConsentPurposeItemDataModel
import com.tokopedia.home_account.consentWithdrawal.data.SubmitConsentDataModel
import com.tokopedia.home_account.consentWithdrawal.di.DaggerConsentWithdrawalComponent
import com.tokopedia.home_account.consentWithdrawal.ui.adapter.ConsentWithdrawalAdapter
import com.tokopedia.home_account.consentWithdrawal.ui.adapter.uimodel.MandatoryPurposeUiModel
import com.tokopedia.home_account.consentWithdrawal.ui.adapter.uimodel.OptionalPurposeUiModel
import com.tokopedia.home_account.consentWithdrawal.ui.adapter.uimodel.TitleDividerUiModel
import com.tokopedia.home_account.consentWithdrawal.viewmodel.ConsentWithdrawalViewModel
import com.tokopedia.home_account.databinding.FragmentConsentWithdrawalBinding
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ConsentWithdrawalFragment :
    BaseDaggerFragment(),
    ConsentWithdrawalListener.Mandatory,
    ConsentWithdrawalListener.Optional {

    @JvmField @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null
    private val viewModel by lazy {
        viewModelFactory?.let {
            ViewModelProvider(this, it).get(
                ConsentWithdrawalViewModel::class.java
            )
        }
    }

    private var viewBinding by autoClearedNullable<FragmentConsentWithdrawalBinding>()
    private val consentWithdrawalAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ConsentWithdrawalAdapter(this, this)
    }

    private var groupId: Int = 0

    override fun getScreenName(): String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentConsentWithdrawalBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun initInjector() {
        DaggerConsentWithdrawalComponent.builder()
            .baseAppComponent((context?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initRecyclerView()

        groupId = arguments?.getInt(ApplinkConstInternalUserPlatform.GROUP_ID).orZero()
        viewModel?.getConsentPurposeByGroup(groupId)
    }

    private fun initObserver() {
        viewModel?.consentPurpose?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> onSuccessGetConsentPurposes(it.data.consents)
                is Fail -> onFailed(it.throwable)
            }
        }
        viewModel?.submitConsentPreference?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> onSuccessSubmitConsentPreference(it.data)
                is Fail -> onFailed(it.throwable)
            }
        }
    }

    private fun initRecyclerView() {
        viewBinding?.consentPurposeList?.apply {
            adapter = consentWithdrawalAdapter
        }
    }

    private fun onSuccessGetConsentPurposes(data: ConsentPurposeDataModel) {
        consentWithdrawalAdapter.clearAllItems()
        consentWithdrawalAdapter.addItem(
            TitleDividerUiModel(
                isTitle = true,
                title = context?.getString(com.tokopedia.home_account.R.string.consent_withdrawal_primary_data_usage).orEmpty()
            )
        )

        data.mandatory.sortedBy { it.priority }
        data.mandatory.forEach {
            consentWithdrawalAdapter.addItem(
                MandatoryPurposeUiModel(
                    data = it
                )
            )
        }

        consentWithdrawalAdapter.addItem(
            TitleDividerUiModel(
                isTitle = false
            )
        )

        consentWithdrawalAdapter.addItem(
            TitleDividerUiModel(
                isTitle = true,
                title = context?.getString(com.tokopedia.home_account.R.string.consent_withdrawal_secondary_data_usage).orEmpty()
            )
        )

        data.optional.sortedBy { it.priority }
        data.optional.forEach {
            consentWithdrawalAdapter.addItem(
                OptionalPurposeUiModel(
                    data = it
                )
            )
        }

        consentWithdrawalAdapter.notifyNewItems()
    }

    private fun onSuccessSubmitConsentPreference(data: SubmitConsentDataModel) {
        val purpose = data.receipts.first()
        consentWithdrawalAdapter.updatePurposeState(purpose.purposeId, purpose.transactionType)
    }

    private fun onFailed(throwable: Throwable) {
        if (throwable !is MessageErrorException) {
            ErrorHandler.getErrorMessage(context, throwable)
        } else {
            view?.let { Toaster.build(it, throwable.message.toString()).show() }
        }
    }

    override fun onActivationButtonClicked(
        position: Int,
        isActive: Boolean,
        data: ConsentPurposeItemDataModel
    ) {
        val intent = when (TransactionType.map(data.consentStatus)) {
            TransactionType.OPT_IN -> {
                RouteManager.getIntent(context, data.optIntAppLink)
            }
            TransactionType.OPT_OUT -> {
                RouteManager.getIntent(context, data.optIntAppLink)
            }
            else -> {
                null
            }
        }

        intent?.let {
            startActivity(it)
        }
    }

    override fun onToggleClicked(
        position: Int,
        isActive: Boolean,
        data: ConsentPurposeItemDataModel
    ) {
        val transactionType = if (TransactionType.map(data.consentStatus) == TransactionType.OPT_IN) {
                TransactionType.OPT_OUT
            } else {
                TransactionType.OPT_IN
            }

        if (isActive) {
            showDialog {
                viewModel?.submitConsentPreference(
                    position,
                    data.purposeId,
                    transactionType
                )
            }
        } else {
            viewModel?.submitConsentPreference(
                position,
                data.purposeId,
                transactionType
            )
        }
    }

    private fun showDialog(onPrimaryButtonClicked: () -> Unit) {
        val dialog = context?.let {
            DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(it.getString(com.tokopedia.home_account.R.string.consent_withdrawal_dialog_title))
                setDescription(it.getString(com.tokopedia.home_account.R.string.consent_withdrawal_dialog_body))
                setPrimaryCTAText(it.getString(com.tokopedia.home_account.R.string.consent_withdrawal_dialog_primary_cta))
                setPrimaryCTAClickListener {
                    dismiss()
                    onPrimaryButtonClicked.invoke()
                }
                setSecondaryCTAText(it.getString(com.tokopedia.home_account.R.string.consent_withdrawal_dialog_secondary_cta))
                setSecondaryCTAClickListener {
                    dismiss()
                }
            }
        }
        dialog?.show()
    }

    override fun onDestroyView() {
        viewModel?.consentPurpose?.removeObservers(viewLifecycleOwner)
        viewModel?.submitConsentPreference?.removeObservers(viewLifecycleOwner)

        super.onDestroyView()
    }

    companion object {
        fun createInstance(groupId: Int): ConsentWithdrawalFragment {
            return ConsentWithdrawalFragment().apply {
                arguments = Bundle().apply {
                    putInt(ApplinkConstInternalUserPlatform.GROUP_ID, groupId)
                }
            }
        }
    }
}
