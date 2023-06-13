package com.tokopedia.privacycenter.ui.consentwithdrawal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.common.PrivacyCenterConst.ERROR_NETWORK_IMAGE
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.data.ConsentPurposeDataModel
import com.tokopedia.privacycenter.data.ConsentPurposeItemDataModel
import com.tokopedia.privacycenter.data.SubmitConsentDataModel
import com.tokopedia.privacycenter.databinding.FragmentConsentWithdrawalPrivacyCenterBinding
import com.tokopedia.privacycenter.di.PrivacyCenterComponent
import com.tokopedia.privacycenter.ui.consentwithdrawal.adapter.ConsentWithdrawalAdapter
import com.tokopedia.privacycenter.ui.consentwithdrawal.adapter.uimodel.PurposeUiModel
import com.tokopedia.privacycenter.ui.consentwithdrawal.adapter.uimodel.TitleDividerUiModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ConsentWithdrawalFragment : BaseDaggerFragment(), ConsentWithdrawalListener.Mandatory {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            ConsentWithdrawalViewModel::class.java
        )
    }

    private var viewBinding by autoClearedNullable<FragmentConsentWithdrawalPrivacyCenterBinding>()
    private val consentWithdrawalAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ConsentWithdrawalAdapter(this)
    }

    private var groupId: Int = 0

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(PrivacyCenterComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentConsentWithdrawalPrivacyCenterBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initRecyclerView()
        arguments?.let {
            updateToolbarTitle(it.getString(KEY_HEADER_TITLE, ""))
            groupId = arguments?.getInt(ApplinkConstInternalUserPlatform.GROUP_ID).orZero()
        }
        viewModel.getConsentPurposeByGroup(groupId)
    }

    private fun updateToolbarTitle(title: String) {
        if(activity is ConsentWithdrawalActivity && title.isNotEmpty()) {
            (activity as ConsentWithdrawalActivity).updateTitle(title)
        }
    }

    private fun initObserver() {
        viewModel.consentPurpose.observe(viewLifecycleOwner) {
            when (it) {
                is PrivacyCenterStateResult.Fail -> showErrorPage {
                    viewModel.getConsentPurposeByGroup(groupId)
                }
                is PrivacyCenterStateResult.Loading -> showShimmering(true)
                is PrivacyCenterStateResult.Success -> {
                    onSuccessGetConsentPurposes(it.data.consents)
                }
            }
        }

        viewModel.submitConsentPreference.observe(viewLifecycleOwner) {
            when (it) {
                is PrivacyCenterStateResult.Fail -> {
                    view?.let { v -> Toaster.build(v, it.error.message.toString()).show() }
                }
                is PrivacyCenterStateResult.Loading -> {}
                is PrivacyCenterStateResult.Success -> {
                    onSuccessSubmitConsentPreference(it.data)
                }
            }
        }
    }

    private fun initRecyclerView() {
        viewBinding?.consentPurposeList?.apply {
            overScrollMode = View.OVER_SCROLL_NEVER
            adapter = consentWithdrawalAdapter
        }
    }

    private fun onSuccessGetConsentPurposes(data: ConsentPurposeDataModel) {
        showShimmering(false)
        consentWithdrawalAdapter.clearAllItems()
        if (data.mandatory.isEmpty() && data.optional.isEmpty()) {
            showErrorPage {
                viewModel.getConsentPurposeByGroup(groupId)
            }
        } else {
            if (data.mandatory.isNotEmpty()) {
                renderMandatoryPurpose(data)

                consentWithdrawalAdapter.addItem(
                    TitleDividerUiModel(
                        isDivider = true,
                        isSmallDivider = false
                    )
                )
            }

            if (data.optional.isNotEmpty()) {
                renderOptionalPurpose(data)

                consentWithdrawalAdapter.addItem(
                    TitleDividerUiModel(
                        isDivider = true,
                        isSmallDivider = false
                    )
                )
            }
        }

        consentWithdrawalAdapter.notifyNewItems()
    }

    private fun renderMandatoryPurpose(data: ConsentPurposeDataModel) {
        consentWithdrawalAdapter.addItem(
            TitleDividerUiModel(
                isDivider = false,
                isSmallDivider = false,
                title = context?.getString(R.string.consent_withdrawal_primary_data_usage).orEmpty()
            )
        )

        data.mandatory.sortedBy { it.priority }
        data.mandatory.forEachIndexed { index, consentPurposeItemDataModel ->
            consentWithdrawalAdapter.addItem(
                PurposeUiModel(
                    isMandatoryPurpose = true,
                    data = consentPurposeItemDataModel
                )
            )

            if (index != data.mandatory.size - 1) {
                consentWithdrawalAdapter.addItem(
                    TitleDividerUiModel(
                        isDivider = true,
                        isSmallDivider = true
                    )
                )
            }
        }
    }

    private fun renderOptionalPurpose(data: ConsentPurposeDataModel) {
        consentWithdrawalAdapter.addItem(
            TitleDividerUiModel(
                isDivider = false,
                isSmallDivider = false,
                title = context?.getString(R.string.consent_withdrawal_secondary_data_usage).orEmpty()
            )
        )

        data.optional.sortedBy { it.priority }
        data.optional.forEachIndexed { index, consentPurposeItemDataModel ->
            consentWithdrawalAdapter.addItem(
                PurposeUiModel(
                    isMandatoryPurpose = false,
                    data = consentPurposeItemDataModel
                )
            )

            if (index != data.mandatory.size - 1) {
                consentWithdrawalAdapter.addItem(
                    TitleDividerUiModel(
                        isDivider = true,
                        isSmallDivider = true
                    )
                )
            }
        }
    }

    private fun onSuccessSubmitConsentPreference(data: SubmitConsentDataModel) {
        viewBinding?.consentPurposeList?.show()

        val purpose = data.receipts.first()
        consentWithdrawalAdapter.updatePurposeState(purpose.purposeId, purpose.transactionType)
    }

    override fun onActivationButtonClicked(
        position: Int,
        isActive: Boolean,
        data: ConsentPurposeItemDataModel
    ) {
        val intent = if (data.consentStatus == ConsentWithdrawalConst.OPT_IN) {
            RouteManager.getIntent(context, data.optIntAppLink)
        } else {
            RouteManager.getIntent(context, data.optIntAppLink)
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
        val transactionType = if (data.consentStatus == ConsentWithdrawalConst.OPT_IN) {
            ConsentWithdrawalConst.OPT_OUT
        } else {
            ConsentWithdrawalConst.OPT_IN
        }

        if (isActive) {
            showDialog {
                viewModel.submitConsentPreference(
                    data.purposeId,
                    transactionType
                )
            }
        } else {
            viewModel.submitConsentPreference(
                data.purposeId,
                transactionType
            )
        }
    }

    private fun showShimmering(isLoading: Boolean) {
        viewBinding?.apply {
            consentPurposeList.showWithCondition(!isLoading)
            consentWithdrawalLoading.root.showWithCondition(isLoading)
            consentWithdrawalErrorPage.root.hide()
        }
    }

    private fun showErrorPage(action: () -> Unit) {
        viewBinding?.apply {
            consentPurposeList.hide()
            consentWithdrawalLoading.root.hide()
            consentWithdrawalErrorPage.apply {
                consentErrorPageImage.loadImage(ERROR_NETWORK_IMAGE) {
                    useCache(true)
                }

                consentErrorPageBackHome.setOnClickListener {
                    val intent = RouteManager.getIntent(context, ApplinkConst.HOME)
                    startActivity(intent)
                }

                consentErrorPageTryAgain.setOnClickListener {
                    action.invoke()
                }
            }.root.show()
        }
    }

    private fun showDialog(onPrimaryButtonClicked: () -> Unit) {
        val dialog = context?.let {
            DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(it.getString(R.string.consent_withdrawal_dialog_title))
                setDescription(it.getString(R.string.consent_withdrawal_dialog_body))
                setPrimaryCTAText(it.getString(R.string.consent_withdrawal_dialog_primary_cta))
                setPrimaryCTAClickListener {
                    dismiss()
                    onPrimaryButtonClicked.invoke()
                }
                setSecondaryCTAText(it.getString(R.string.consent_withdrawal_dialog_secondary_cta))
                setSecondaryCTAClickListener {
                    dismiss()
                }
            }
        }
        dialog?.show()
    }

    override fun onDestroyView() {
        viewModel.consentPurpose.removeObservers(viewLifecycleOwner)
        viewModel.submitConsentPreference.removeObservers(viewLifecycleOwner)

        super.onDestroyView()
    }

    companion object {
        const val KEY_HEADER_TITLE = "toolbarTitle"
        fun createInstance(bundle: Bundle?): ConsentWithdrawalFragment {
            return ConsentWithdrawalFragment().apply {
                arguments = bundle
            }
        }
    }
}
