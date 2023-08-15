package com.tokopedia.home_account.privacy_account.view

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.home_account.R
import com.tokopedia.home_account.analytics.HomeAccountAnalytics
import com.tokopedia.home_account.consentWithdrawal.data.ConsentGroupListDataModel
import com.tokopedia.home_account.databinding.FragmentPrivacyAccountBinding
import com.tokopedia.home_account.privacy_account.di.LinkAccountComponent
import com.tokopedia.home_account.privacy_account.listener.PrivacyAccountListener
import com.tokopedia.home_account.privacy_account.view.adapter.PrivacyAccountAdapter
import com.tokopedia.home_account.privacy_account.view.bottomsheet.ClarificationDataUsageBottomSheet
import com.tokopedia.home_account.privacy_account.view.bottomsheet.VerificationEnabledDataUsageBottomSheet
import com.tokopedia.home_account.privacy_account.viewmodel.PrivacyAccountViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visibleWithCondition
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class PrivacyAccountFragment : BaseDaggerFragment(), PrivacyAccountListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            PrivacyAccountViewModel::class.java
        )
    }

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    @Inject
    lateinit var homeAccountAnalytics: HomeAccountAnalytics

    private var remoteConfigInstance: RemoteConfigInstance = RemoteConfigInstance.getInstance()
    private var viewBinding by autoClearedNullable<FragmentPrivacyAccountBinding>()
    private val privacyAccountAdapter by lazy(LazyThreadSafetyMode.NONE) {
        PrivacyAccountAdapter(this)
    }

    private var clarificationDataUsageBottomSheet: ClarificationDataUsageBottomSheet? = null
    private var verificationEnabledDataUsageBottomSheet: VerificationEnabledDataUsageBottomSheet? =
        null
    private var verificationDisabledDataUsageDialog: DialogUnify? = null
    private var loaderDialog: LoaderDialog? = null

    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentPrivacyAccountBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun getScreenName(): String = SCREEN_NAME

    override fun initInjector() {
        getComponent(LinkAccountComponent::class.java).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        setViewDescSocialNetworkConsent()
        setViewDataUsageLoading()
        renderConsentWithdrawal()
    }

    private fun initObserver() {
        viewModel.getConsentSocialNetwork.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    setViewDataUsage(it.data)
                    setVisibilityDataUsage(true)
                    switchListener()
                }

                is Fail -> {
                    setVisibilityDataUsage(false)
                }
            }
        }

        viewModel.setConsentSocialNetwork.observe(viewLifecycleOwner) {
            showLoaderDialog(false)
            val isChecked = viewBinding?.switchPermissionDataUsage?.isChecked == true
            when (it) {
                is Success -> {
                    if (it.data.isSuccess == SET_CONSENT_SUCCESS) {
                        viewBinding?.switchPermissionDataUsage?.isChecked = !isChecked
                    } else {
                        showToasterFailedSetConsent(isChecked)
                    }
                }

                is Fail -> {
                    showToasterFailedSetConsent(isChecked)
                }
            }
        }

        viewModel.getConsentGroupList.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onSuccessGetConsentGroupList(it.data)
                }

                is Fail -> {
                    onFailedGetConsentGroupList(it.throwable)
                }
            }
        }
    }

    private fun renderConsentWithdrawal() {
        if (isShowConsentWithdrawal()) {
            viewBinding?.layoutConsentWithdrawal?.root?.show()
            setViewConsentWithdrawalLoading()
            viewModel.getConsentGroupList()
        } else {
            viewBinding?.layoutConsentWithdrawal?.root?.hide()
        }
    }

    private fun switchListener() {
        viewBinding?.switchPermissionDataUsage?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isLoading) {
                buttonView.isChecked = !isChecked

                if (!isChecked) {
                    showVerificationDisabledDataUsage()
                } else {
                    showVerificationEnabledDataUsage()
                }
            } else {
                isLoading = false
            }
        }
    }

    private fun showVerificationEnabledDataUsage() {
        verificationEnabledDataUsageBottomSheet = VerificationEnabledDataUsageBottomSheet()

        verificationEnabledDataUsageBottomSheet?.setOnVerificationClickedListener {
            verificationEnabledDataUsageBottomSheet?.dismiss()
            verificationEnabledDataUsageBottomSheet = null
            showLoaderDialog(true)
            viewModel.setConsentSocialNetwork(true)
        }

        verificationEnabledDataUsageBottomSheet?.show(
            parentFragmentManager,
            TAG_BOTTOM_SHEET_VERIFICATION
        )
    }

    private fun setViewDescSocialNetworkConsent() {
        val message = getString(R.string.opt_desc)
        val spannable = SpannableString(message)
        spannable.setSpan(
            object : ClickableSpan() {
                override fun onClick(view: View) {
                    showClarificationDataUsage()
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.isFakeBoldText = true
                    ds.color = MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                }
            },
            message.indexOf(TEXT_LINK_DESC_CONSENT_SOCIAL_NETWORK),
            message.length,
            0
        )
        viewBinding?.txtDescConsentSocialNetwork?.apply {
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
            setText(spannable, TextView.BufferType.SPANNABLE)
        }
    }

    private fun showVerificationDisabledDataUsage() {
        verificationDisabledDataUsageDialog =
            DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)

        verificationDisabledDataUsageDialog?.apply {
            setTitle(getString(R.string.opt_dialog_disabled_title))
            setDescription(getString(R.string.opt_dialog_disabled_sub_title))
            setPrimaryCTAText(getString(R.string.opt_ya_matikan))
            setSecondaryCTAText(getString(R.string.opt_batal))
        }

        verificationDisabledDataUsageDialog?.setPrimaryCTAClickListener {
            verificationDisabledDataUsageDialog?.dismiss()
            verificationDisabledDataUsageDialog = null
            showLoaderDialog(true)
            viewModel.setConsentSocialNetwork(false)
        }

        verificationDisabledDataUsageDialog?.setSecondaryCTAClickListener {
            verificationDisabledDataUsageDialog?.dismiss()
            verificationDisabledDataUsageDialog = null
        }

        verificationDisabledDataUsageDialog?.show()
    }

    private fun showLoaderDialog(isShow: Boolean) {
        if (isShow) {
            loaderDialog = LoaderDialog(requireContext())
            loaderDialog?.setLoadingText("")
            loaderDialog?.show()
            isLoading = true
        } else {
            loaderDialog?.dismiss()
            loaderDialog = null
        }
    }

    private fun showClarificationDataUsage() {
        clarificationDataUsageBottomSheet = ClarificationDataUsageBottomSheet()
        clarificationDataUsageBottomSheet?.show(
            parentFragmentManager,
            TAG_BOTTOM_SHEET_CLARIFICATION
        )
    }

    private fun setViewDataUsage(isActive: Boolean) {
        viewBinding?.switchPermissionDataUsage?.isChecked = isActive
    }

    private fun setVisibilityDataUsage(isVisible: Boolean) {
        viewBinding?.apply {
            incLoaderDataUsage.loaderHeader.gone()
            incLoaderDataUsage.loaderDesc.gone()
            incLoaderDataUsage.cardLoad.showWithCondition(!isVisible)
            switchPermissionDataUsage.visibleWithCondition(isVisible)
            txtHeaderConsentSocialNetwork.visibleWithCondition(isVisible)
            txtDescConsentSocialNetwork.visibleWithCondition(isVisible)
            dividerUnify.visibleWithCondition(isVisible)
        }

        if (!isVisible) {
            viewBinding?.incLoaderDataUsage?.cardLoad?.apply {
                title?.text = getString(R.string.opt_text_header_failed)
                description?.text = getString(R.string.opt_text_desc_failed)
            }
            reloadDataUsageListener()
        }
    }

    private fun reloadDataUsageListener() {
        viewBinding?.incLoaderDataUsage?.cardLoad?.setOnClickListener {
            setViewDataUsageLoading()
            viewModel.getConsentSocialNetwork()
        }
    }

    private fun setViewDataUsageLoading() {
        viewBinding?.apply {
            incLoaderDataUsage.loaderHeader.show()
            incLoaderDataUsage.loaderDesc.show()
            incLoaderDataUsage.cardLoad.gone()
            switchPermissionDataUsage.invisible()
            txtHeaderConsentSocialNetwork.invisible()
            txtDescConsentSocialNetwork.invisible()
            dividerUnify.invisible()
        }
    }

    private fun showToasterFailedSetConsent(isChecked: Boolean) {
        isLoading = false
        showToasterError(
            getString(
                if (isChecked) {
                    R.string.opt_failed_disabled_consent
                } else {
                    R.string.opt_failed_enabled_consent
                }
            )
        )
    }

    private fun onFailedGetConsentGroupList(throwable: Throwable) {
        viewBinding?.layoutConsentWithdrawal?.root?.hide()

        if (throwable !is MessageErrorException) {
            ErrorHandler.getErrorMessage(context, throwable)
        } else {
            showToasterError(throwable.message.toString())
        }
    }

    private fun onSuccessGetConsentGroupList(data: ConsentGroupListDataModel) {
        viewBinding?.layoutConsentWithdrawal?.apply {
            mainLayout.show()
            loaderShimmering.hide()

            consentGroupList.apply {
                adapter = privacyAccountAdapter

                data.groups.sortedBy { it.priority }
                privacyAccountAdapter.setItems(data.groups)
            }
        }
    }

    private fun setViewConsentWithdrawalLoading() {
        viewBinding?.layoutConsentWithdrawal?.apply {
            mainLayout.hide()
            loaderShimmering.show()
        }
    }

    private fun showToasterError(errorMsg: String) {
        Toaster.build(requireView(), errorMsg, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
    }

    override fun onFragmentBackPressed(): Boolean {
        homeAccountAnalytics.trackClickBackLinkAccount()
        return super.onFragmentBackPressed()
    }

    override fun onConsentGroupClicked(id: String) {
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalUserPlatform.CONSENT_WITHDRAWAL,
            id
        )

        startActivity(intent)
    }

    private fun isShowConsentWithdrawal(): Boolean = remoteConfigInstance
        .abTestPlatform
        .getString(ROLLENCE_KEY_CONSENT_WITHDRAWAL) == ROLLENCE_KEY_CONSENT_WITHDRAWAL

    companion object {
        private const val TAG_BOTTOM_SHEET_CLARIFICATION = "TAG BOTTOM SHEET CLARIFICATION"
        private const val TAG_BOTTOM_SHEET_VERIFICATION = "TAG BOTTOM SHEET VERIFICATION"
        private const val TEXT_LINK_DESC_CONSENT_SOCIAL_NETWORK = "Cek Data yang Dipakai"
        private const val SET_CONSENT_SUCCESS = 1

        private const val ROLLENCE_KEY_CONSENT_WITHDRAWAL = "cpcw_and"

        private val SCREEN_NAME = PrivacyAccountFragment::class.java.simpleName

        @JvmStatic
        fun newInstance() = PrivacyAccountFragment()
    }
}
