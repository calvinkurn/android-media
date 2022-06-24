package com.tokopedia.home_account.linkaccount.view

import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.FragmentPrivacyAccountBinding
import com.tokopedia.home_account.linkaccount.di.LinkAccountComponent
import com.tokopedia.home_account.linkaccount.view.bottomsheet.ClarificationDataUsageBottomSheet
import com.tokopedia.home_account.linkaccount.view.bottomsheet.VerificationEnabledDataUsageBottomSheet
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.utils.view.binding.viewBinding

class PrivacyAccountFragment : BaseDaggerFragment() {

    private val binding : FragmentPrivacyAccountBinding? by viewBinding()

    private var clarificationDataUsageBottomSheet: ClarificationDataUsageBottomSheet? = null
    private var verificationEnabledDataUsageBottomSheet: VerificationEnabledDataUsageBottomSheet? = null
    private var verificationDisabledDataUsageDialog: DialogUnify? = null
    private var loaderDialog: LoaderDialog? = null

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
        setViewDescPrivacyAccount()

        binding?.switchPermissionDataUsage?.setOnCheckedChangeListener { buttonView, isChecked ->
            buttonView.isChecked = !isChecked

            if (!isChecked)
                showVerificationDisabledDataUsage()
            else
                showVerificationEnabledDataUsage()
        }
    }

    private fun setViewDescPrivacyAccount() {
        binding?.txtDescPrivacyAccount?.show()
        val message = getString(R.string.opt_desc)
        val spannable = SpannableString(message)
        spannable.setSpan(
            object : ClickableSpan() {
                override fun onClick(view: View) {
                    showClarificationDataUsage()
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
                }
            },
            message.indexOf(TEXT_LINK_DESC_PRIVACY_ACCOUNT),
            message.length,
            0
        )
        binding?.txtDescPrivacyAccount?.movementMethod = LinkMovementMethod.getInstance()
        binding?.txtDescPrivacyAccount?.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    private fun showClarificationDataUsage() {
        if (clarificationDataUsageBottomSheet == null){
            clarificationDataUsageBottomSheet = ClarificationDataUsageBottomSheet()
        }
        clarificationDataUsageBottomSheet?.show(childFragmentManager, TAG_BOTTOM_SHEET_CLARIFICATION)
    }

    private fun showVerificationEnabledDataUsage() {
        if (verificationEnabledDataUsageBottomSheet == null) {
            verificationEnabledDataUsageBottomSheet = VerificationEnabledDataUsageBottomSheet()

            verificationEnabledDataUsageBottomSheet?.setOnVerificationClickedListener {
                verificationEnabledDataUsageBottomSheet?.dismiss()
                showLoaderDialog(true)
            }
        }
        verificationEnabledDataUsageBottomSheet?.show(childFragmentManager, TAG_BOTTOM_SHEET_VERIFICATION)
    }

    private fun showVerificationDisabledDataUsage() {
        if (verificationDisabledDataUsageDialog == null) {
            verificationDisabledDataUsageDialog = DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            verificationDisabledDataUsageDialog?.setTitle(getString(R.string.opt_dialog_disabled_title))
            verificationDisabledDataUsageDialog?.setDescription(getString(R.string.opt_dialog_disabled_sub_title))
            verificationDisabledDataUsageDialog?.setPrimaryCTAText(getString(R.string.opt_ya_matikan))
            verificationDisabledDataUsageDialog?.setSecondaryCTAText(getString(R.string.opt_batal))

            verificationDisabledDataUsageDialog?.setPrimaryCTAClickListener {
                verificationDisabledDataUsageDialog?.dismiss()
                showLoaderDialog(true)
            }

            verificationDisabledDataUsageDialog?.setSecondaryCTAClickListener {
                verificationDisabledDataUsageDialog?.dismiss()
            }
        }

        verificationDisabledDataUsageDialog?.show()
    }

    private fun showLoaderDialog(isShow: Boolean) {
        if (loaderDialog == null) {
            loaderDialog = LoaderDialog(requireContext())
            loaderDialog?.setLoadingText(EMPTY_STRING)
        }

        if (isShow)
            loaderDialog?.show()
        else
            loaderDialog?.dismiss()
    }

    companion object {

        private const val EMPTY_STRING = ""

        private const val TAG_BOTTOM_SHEET_CLARIFICATION = "TAG BOTTOM SHEET CLARIFICATION"
        private const val TAG_BOTTOM_SHEET_VERIFICATION = "TAG BOTTOM SHEET VERIFICATION"

        private const val TEXT_LINK_DESC_PRIVACY_ACCOUNT = "Cek Data yang Dipakai"
        private val SCREEN_NAME = PrivacyAccountFragment::class.java.simpleName

        @JvmStatic
        fun newInstance() = PrivacyAccountFragment()
    }
}