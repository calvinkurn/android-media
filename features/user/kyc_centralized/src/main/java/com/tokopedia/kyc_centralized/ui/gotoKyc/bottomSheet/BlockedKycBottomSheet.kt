package com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.databinding.LayoutGotoKycBlockedBinding
import com.tokopedia.kyc_centralized.di.DaggerGoToKycComponent
import com.tokopedia.kyc_centralized.util.KycSharedPreference
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class BlockedKycBottomSheet : BottomSheetUnify() {

    @Inject
    lateinit var kycSharedPreference: KycSharedPreference

    private var binding by autoClearedNullable<LayoutGotoKycBlockedBinding>()

    private var isBlockedMultipleAccount = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()

        arguments?.let {
            isBlockedMultipleAccount = it.getBoolean(IS_BLOCKED_MULTIPLE_ACCOUNT).orFalse()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutGotoKycBlockedBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initListener() {
        binding?.btnPrimary?.setOnClickListener {
            if (isBlockedMultipleAccount) {
                goToTokopediaCare()
            } else {
                dismiss()
            }
        }

        binding?.btnSecondary?.setOnClickListener {
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        finishWithResultCancelled()
    }

    private fun goToTokopediaCare() {
        RouteManager.route(
            context,
            ApplinkConstInternalGlobal.WEBVIEW,
            TokopediaUrl.getInstance().WEB.plus(PATH_TOKOPEDIA_CARE)
        )
    }

    private fun initView() {
        if (isBlockedMultipleAccount) {
            setUpViewBlockedMultipleAccount()
        } else {
            setUpViewBlockedGeneral()
        }
    }

    private fun setUpViewBlockedMultipleAccount() {
        binding?.apply {
            ivBlocked.loadImageWithoutPlaceholder(getString(R.string.img_url_goto_kyc_blocked_multiple_account))
            tvTitle.text = getString(R.string.goto_kyc_blocked_title_multiple_account)
            tvDescription.text = getString(R.string.goto_kyc_blocked_subtitle_multiple_account)
            btnPrimary.text = getString(R.string.goto_kyc_contact_tokopedia_care)
            btnSecondary.show()
        }
    }

    private fun setUpViewBlockedGeneral() {
        binding?.apply {
            ivBlocked.loadImageWithoutPlaceholder(getString(R.string.img_url_goto_kyc_blocked_general))
            tvTitle.text = getString(R.string.goto_kyc_blocked_title_general)
            tvDescription.text = getString(R.string.goto_kyc_blocked_subtitle_general)
            btnPrimary.text = getString(R.string.goto_kyc_oke)
            btnSecondary.hide()
        }
    }

    private fun finishWithResultCancelled() {
        kycSharedPreference.removeProjectId()
        activity?.setResult(Activity.RESULT_CANCELED)
        activity?.finish()
    }

    private fun initInjector() {
        DaggerGoToKycComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    companion object {
        private const val PATH_TOKOPEDIA_CARE = "help"

        private const val IS_BLOCKED_MULTIPLE_ACCOUNT = "is_blocked_multiple_account"
        fun newInstance(blockedMultipleAccount: Boolean) =
            BlockedKycBottomSheet().apply {
                arguments = Bundle().apply {
                    putBoolean(IS_BLOCKED_MULTIPLE_ACCOUNT, blockedMultipleAccount)
                }
            }
    }
}
