package com.tokopedia.loginregister.shopcreation.view.landingshop

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.databinding.LayoutShopCreationKycStatusBinding
import com.tokopedia.loginregister.shopcreation.common.ShopCreationConstant
import com.tokopedia.loginregister.shopcreation.di.ShopCreationComponent
import com.tokopedia.loginregister.shopcreation.domain.ProjectInfoResult
import com.tokopedia.loginregister.shopcreation.view.KycBridgingViewModel
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

class ShopCreationKycStatusFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<LayoutShopCreationKycStatusBinding>()

    @Inject
    lateinit var viewModel: KycBridgingViewModel

    private val startReVerifyKycForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        activity?.setResult(result.resultCode)
        activity?.finish()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutShopCreationKycStatusBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initObserver()
        onRefreshStatus()
    }

    private fun initObserver() {
        viewModel.projectInfo.observe(viewLifecycleOwner) {
            binding?.loader?.invisible()

            when (it) {
                is ProjectInfoResult.Verified -> {
                    launchShopCreationProcess()
                }
                is ProjectInfoResult.Pending -> {
                    onPending()
                }
                is ProjectInfoResult.Blacklisted -> {
//                    onBlacklisted()
                }
                is ProjectInfoResult.Rejected -> {
                    onRejected(it.rejectionReason)
                }
                is ProjectInfoResult.NotVerified -> {
                    launchKyc()
                }
                is ProjectInfoResult.Failed -> {
                    handleGlobalError(it.throwable)
                }
            }
        }
    }

    private fun onPending() {
        context?.getString(R.string.shop_creation_kyc_pending)?.let { loadImage(it) }

        binding?.apply {
            tvHeader.text = context?.getString(R.string.shop_creation_title_pending)
            tvHeader.show()
            ivStatusSubmission.show()
            layoutStatusPending.root.show()
            layoutStatusRejected.root.hide()
            globalError.hide()
            loader.hide()
        }
    }

    private fun onRejected(rejectionReason: String) {
        context?.getString(R.string.shop_creation_kyc_rejected)?.let { loadImage(it) }

        binding?.apply {
            tvHeader.text = context?.getString(R.string.shop_creation_title_rejected)
            tvHeader.show()
            ivStatusSubmission.show()
            layoutStatusPending.root.hide()
            layoutStatusRejected.root.show()
            layoutStatusRejected.tvReason.text = rejectionReason
            globalError.hide()
            loader.hide()
        }
    }

    private fun launchShopCreationProcess() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.OPEN_SHOP)
        activity?.startActivity(intent)
        activity?.finish()
    }

    private fun initListener() {
        binding?.layoutStatusPending?.btnPrimary?.setOnClickListener {
            onRefreshStatus()
        }

        binding?.layoutStatusRejected?.btnPrimary?.setOnClickListener {
            launchKyc()
        }

        binding?.layoutStatusRejected?.btnSecondary?.setOnClickListener {
            goToTokopediaCare()
        }

        binding?.globalError?.setActionClickListener {
            onRefreshStatus()
        }

        binding?.globalError?.setSecondaryActionClickListener {
            val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            startActivity(intent)
        }
    }

    private fun launchKyc() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.GOTO_KYC).apply {
            putExtra(ApplinkConstInternalUserPlatform.PARAM_SOURCE, "")
            putExtra(ApplinkConstInternalUserPlatform.PARAM_CALL_BACK, "")
            putExtra(ShopCreationConstant.IS_RE_VERIFY, true)
            putExtra(ApplinkConstInternalUserPlatform.PARAM_PROJECT_ID, ShopCreationConstant.OPEN_SHOP_KYC_PROJECT_ID)
        }
        startReVerifyKycForResult.launch(intent)
    }

    private fun handleGlobalError(throwable: Throwable?) {
        binding?.apply {
            loader.hide()
            tvHeader.hide()
            ivStatusSubmission.hide()
            layoutStatusPending.root.hide()
            ivStatusSubmission.hide()
            globalError.show()
        }

        if (throwable != null) {
            when (throwable) {
                is UnknownHostException, is ConnectException -> {
                    binding?.globalError?.setType(GlobalError.NO_CONNECTION)
                }
                else -> {
                    binding?.globalError?.apply {
                        setType(GlobalError.MAINTENANCE)
                    }
                }
            }
        } else {
            binding?.globalError?.setType(GlobalError.MAINTENANCE)
        }
    }

    private fun onRefreshStatus() {
        binding?.apply {
            loader.show()
            tvHeader.hide()
            ivStatusSubmission.hide()
            layoutStatusPending.root.hide()
            ivStatusSubmission.hide()
        }
        viewModel.checkKycStatus()
    }

    private fun loadImage(imageUrl: String) {
        binding?.ivStatusSubmission?.loadImageWithoutPlaceholder(imageUrl)
    }

    private fun goToTokopediaCare() {
        val articleId = if (TokopediaUrl.getInstance().TYPE == Env.STAGING) ARTICLE_ID_STAGING else ARTICLE_ID_PRODUCTION
        RouteManager.route(
            context,
            ApplinkConstInternalGlobal.WEBVIEW,
            TokopediaUrl.getInstance().WEB.plus(PATH_TOKOPEDIA_CARE).plus(articleId).plus(
                PARAM_TOKOPEDIA_CARE
            )
        )
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ShopCreationComponent::class.java).inject(this)
    }

    companion object {
        private const val ARTICLE_ID_STAGING = "2443"
        private const val ARTICLE_ID_PRODUCTION = "3881"
        private const val PARAM_TOKOPEDIA_CARE = "?nref='goto-kyc'"
        private const val PATH_TOKOPEDIA_CARE = "help/article/a-"

        fun createInstance() = ShopCreationKycStatusFragment()
    }
}
