package com.tokopedia.home_account.privacy_account.view

import android.content.Intent
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
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.home_account.R
import com.tokopedia.home_account.analytics.HomeAccountAnalytics
import com.tokopedia.home_account.databinding.FragmentPrivacyAccountBinding
import com.tokopedia.home_account.privacy_account.data.LinkStatus
import com.tokopedia.home_account.privacy_account.data.LinkStatusResponse
import com.tokopedia.home_account.privacy_account.data.UserAccountDataView
import com.tokopedia.home_account.privacy_account.di.LinkAccountComponent
import com.tokopedia.home_account.privacy_account.view.bottomsheet.ClarificationDataUsageBottomSheet
import com.tokopedia.home_account.privacy_account.view.bottomsheet.VerificationEnabledDataUsageBottomSheet
import com.tokopedia.home_account.privacy_account.viewmodel.PrivacyAccountViewModel
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.viewBinding
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class PrivacyAccountFragment : BaseDaggerFragment() {

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

    private val binding: FragmentPrivacyAccountBinding? by viewBinding()

    private var clarificationDataUsageBottomSheet: ClarificationDataUsageBottomSheet? = null
    private var verificationEnabledDataUsageBottomSheet: VerificationEnabledDataUsageBottomSheet? =
        null
    private var verificationDisabledDataUsageDialog: DialogUnify? = null
    private var loaderDialog: LoaderDialog? = null

    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_privacy_account, container, false)
    }

    override fun getScreenName(): String = SCREEN_NAME

    override fun initInjector() {
        getComponent(LinkAccountComponent::class.java).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewDescSocialNetworkConsent()
        initObserver()
    }

    private fun initObserver() {
        setViewDataUsageLoading()
        setViewLinkAccountLoading()
        viewModel.getConsentSocialNetwork()
        viewModel.getLinkStatus()

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

        viewModel.linkStatus.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    setViewLinkAccount(it.data)
                    setVisibilityLinkAccount(true)
                }
                is Fail -> {
                    setVisibilityLinkAccount(false)
                }
            }
        }

        viewModel.setConsentSocialNetwork.observe(viewLifecycleOwner) {
            showLoaderDialog(false)
            val isChecked = binding?.switchPermissionDataUsage?.isChecked == true
            when (it) {
                is Success -> {
                    if (it.data.isSuccess == SET_CONSENT_SUCCESS) {
                        binding?.switchPermissionDataUsage?.isChecked = !isChecked
                    } else {
                        showToasterFailedSetConsent(isChecked)
                    }
                }
                is Fail -> {
                    showToasterFailedSetConsent(isChecked)
                }
            }
        }
    }

    private fun setViewDataUsage(isActive: Boolean) {
        binding?.switchPermissionDataUsage?.isChecked = isActive
    }

    private fun setVisibilityDataUsage(isVisible: Boolean) {
        binding?.apply {
            incLoaderDataUsage.loaderHeader.gone()
            incLoaderDataUsage.loaderDesc.gone()
            incLoaderDataUsage.cardLoad.showWithCondition(!isVisible)
            switchPermissionDataUsage.visibleWithCondition(isVisible)
            txtHeaderConsentSocialNetwork.visibleWithCondition(isVisible)
            txtDescConsentSocialNetwork.visibleWithCondition(isVisible)
            dividerUnify.visibleWithCondition(isVisible)
        }

        if (!isVisible) {
            binding?.incLoaderDataUsage?.cardLoad?.apply {
                title?.text = getString(R.string.opt_text_header_failed)
                description?.text = getString(R.string.opt_text_desc_failed)
            }
            reloadDataUsageListener()
        }
    }

    private fun reloadDataUsageListener() {
        binding?.incLoaderDataUsage?.cardLoad?.setOnClickListener {
            setViewDataUsageLoading()
            viewModel.getConsentSocialNetwork()
        }
    }

    private fun setViewDataUsageLoading() {
        binding?.apply {
            incLoaderDataUsage.loaderHeader.show()
            incLoaderDataUsage.loaderDesc.show()
            incLoaderDataUsage.cardLoad.gone()
            switchPermissionDataUsage.invisible()
            txtHeaderConsentSocialNetwork.invisible()
            txtDescConsentSocialNetwork.invisible()
            dividerUnify.invisible()
        }
    }

    private fun LinkStatus.toUserAccountDataView(): UserAccountDataView {
        val phone = this.phoneNo.ifEmpty {
            userSessionInterface.phoneNumber
        }

        val statusLinked = status == STATUS_LINKED
        return UserAccountDataView(
            isLinked = statusLinked,
            status = if (statusLinked) {
                String.format(context?.getString(R.string.label_link_acc_phone) ?: "", phone)
            } else {
                context?.getString(R.string.label_link_acc_not_linked) ?: ""
            },
            partnerName = context?.getString(R.string.label_link_acc_gojek) ?: "",
            linkDate = String.format(
                context?.getString(R.string.label_link_acc_linked_date) ?: "",
                formatDateLocalTimezone(linkedDate)
            )
        )
    }

    private fun getIndonesiaLocale(): Locale {
        return try {
            Locale(LANGUAGE_CODE, COUNTRY_CODE)
        } catch (e: Exception) {
            Locale.getDefault()
        }
    }

    private fun formatDateLocalTimezone(mDate: String): String {
        return try {
            val date = SimpleDateFormat(SERVER_DATE_FORMAT, getIndonesiaLocale())
            date.timeZone = TimeZone.getTimeZone(TIMEZONE_UTC)
            SimpleDateFormat(
                LOCAL_DATE_FORMAT,
                getIndonesiaLocale()
            ).format(date.parse(mDate) ?: "")
        } catch (e: Exception) {
            mDate
        }
    }

    private fun setViewLinkAccount(data: LinkStatusResponse) {
        if (data.response.linkStatus.isNotEmpty()) {
            val linkStatus = data.response.linkStatus.first()
            val item = linkStatus.toUserAccountDataView()

            binding?.layoutItemLinkAccount?.apply {
                tvHeaderLinkAccount.text = item.partnerName
                tvLinkingAccount.showWithCondition(!item.isLinked)
                icLinkAccount.showWithCondition(item.isLinked)
                tvDescLinkAccount.text = item.status.plus(if (item.isLinked) item.linkDate else "")
            }

            linkedAccountListener(item.isLinked)
        } else {
            binding?.layoutItemLinkAccount?.root?.gone()
        }
    }

    private fun linkedAccountListener(isLinked: Boolean) {
        binding?.layoutItemLinkAccount?.root?.setOnClickListener {
            if (isLinked) {
                onViewAccountClicked()
            } else {
                onLinkAccountClicked()
            }
        }
    }

    private fun setVisibilityLinkAccount(isVisible: Boolean) {
        binding?.apply {
            incLoaderLinkAccount.loaderHeader.gone()
            incLoaderLinkAccount.loaderDesc.gone()
            incLoaderLinkAccount.cardLoad.showWithCondition(!isVisible)
            txtHeaderLinkAccount.visibleWithCondition(isVisible)
            txtDescLinkAccount.visibleWithCondition(isVisible)
            layoutItemLinkAccount.root.visibleWithCondition(isVisible)
        }

        if (!isVisible) {
            binding?.incLoaderLinkAccount?.cardLoad?.apply {
                title?.text = getString(R.string.opt_text_header_failed)
                description?.text = getString(R.string.opt_text_desc_failed)
            }

            reloadLinkAccountListener()
        }
    }

    private fun reloadLinkAccountListener() {
        binding?.incLoaderLinkAccount?.cardLoad?.setOnClickListener {
            setViewLinkAccountLoading()
            viewModel.getLinkStatus()
        }
    }

    private fun setViewLinkAccountLoading() {
        binding?.apply {
            incLoaderLinkAccount.loaderHeader.show()
            incLoaderLinkAccount.loaderDesc.show()
            incLoaderLinkAccount.cardLoad.gone()
            txtHeaderLinkAccount.invisible()
            txtDescLinkAccount.invisible()
            layoutItemLinkAccount.root.invisible()
        }
    }

    private fun onLinkAccountClicked() {
        homeAccountAnalytics.trackClickHubungkanLinkAccountPage()
        val intent =
            RouteManager.getIntent(activity, ApplinkConstInternalUserPlatform.LINK_ACCOUNT_WEBVIEW)
                .apply {
                    putExtra(
                        ApplinkConstInternalGlobal.PARAM_LD,
                        LinkAccountWebviewFragment.BACK_BTN_APPLINK
                    )
                }
        startActivityForResult(intent, LINK_ACCOUNT_WEBVIEW_REQUEST)
    }

    private fun onViewAccountClicked() {
        homeAccountAnalytics.trackClickViewStatusLinkAccountPage()
        LinkAccountWebViewActivity.gotoSuccessPage(activity, ApplinkConst.HOME)
    }

    private fun switchListener() {
        binding?.switchPermissionDataUsage?.setOnCheckedChangeListener { buttonView, isChecked ->
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
                        com.tokopedia.unifyprinciples.R.color.Unify_G500
                    )
                }
            },
            message.indexOf(TEXT_LINK_DESC_CONSENT_SOCIAL_NETWORK),
            message.length,
            0
        )
        binding?.txtDescConsentSocialNetwork?.apply {
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
            setText(spannable, TextView.BufferType.SPANNABLE)
        }
    }

    private fun showClarificationDataUsage() {
        clarificationDataUsageBottomSheet = ClarificationDataUsageBottomSheet()
        clarificationDataUsageBottomSheet?.show(
            parentFragmentManager,
            TAG_BOTTOM_SHEET_CLARIFICATION
        )
    }

    private fun showVerificationEnabledDataUsage() {
        verificationEnabledDataUsageBottomSheet = VerificationEnabledDataUsageBottomSheet()

        verificationEnabledDataUsageBottomSheet?.setOnVerificationClickedListener {
            verificationEnabledDataUsageBottomSheet?.dismiss()
            showLoaderDialog(true)
            viewModel.setConsentSocialNetwork(true)
        }

        verificationEnabledDataUsageBottomSheet?.show(
            parentFragmentManager,
            TAG_BOTTOM_SHEET_VERIFICATION
        )
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
            showLoaderDialog(true)
            viewModel.setConsentSocialNetwork(false)
        }

        verificationDisabledDataUsageDialog?.setSecondaryCTAClickListener {
            verificationDisabledDataUsageDialog?.dismiss()
        }

        verificationDisabledDataUsageDialog?.show()
    }

    private fun showLoaderDialog(isShow: Boolean) {
        if (isShow) {
            loaderDialog = LoaderDialog(requireContext())
            loaderDialog?.setLoadingText(EMPTY_STRING)
            loaderDialog?.show()
            isLoading = true
        } else {
            loaderDialog?.dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == LINK_ACCOUNT_WEBVIEW_REQUEST) {
            setViewLinkAccountLoading()
            viewModel.getLinkStatus(userSessionInterface.phoneNumber.isEmpty())
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

    private fun showToasterError(errorMsg: String) {
        Toaster.build(requireView(), errorMsg, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
    }

    override fun onFragmentBackPressed(): Boolean {
        homeAccountAnalytics.trackClickBackLinkAccount()
        return super.onFragmentBackPressed()
    }

    companion object {

        private const val SET_CONSENT_SUCCESS = 1
        const val LINK_ACCOUNT_WEBVIEW_REQUEST = 100

        private const val SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        private const val LOCAL_DATE_FORMAT = "dd MMM yyyy"
        private const val TIMEZONE_UTC = "UTC"
        private const val LANGUAGE_CODE = "id"
        private const val COUNTRY_CODE = "ID"

        private const val STATUS_LINKED = "linked"
        private const val EMPTY_STRING = ""

        private const val TAG_BOTTOM_SHEET_CLARIFICATION = "TAG BOTTOM SHEET CLARIFICATION"
        private const val TAG_BOTTOM_SHEET_VERIFICATION = "TAG BOTTOM SHEET VERIFICATION"

        private const val TEXT_LINK_DESC_CONSENT_SOCIAL_NETWORK = "Cek Data yang Dipakai"
        private val SCREEN_NAME = PrivacyAccountFragment::class.java.simpleName

        @JvmStatic
        fun newInstance() = PrivacyAccountFragment()
    }
}