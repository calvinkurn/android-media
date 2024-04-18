package com.tokopedia.loginregister.shopcreation.view.landingshop

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.provider.Settings
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.analytics.ShopCreationAnalytics
import com.tokopedia.loginregister.databinding.ItemKycRejectionReasonBinding
import com.tokopedia.loginregister.databinding.LayoutShopCreationKycStatusBinding
import com.tokopedia.loginregister.shopcreation.common.ShopCreationConstant
import com.tokopedia.loginregister.shopcreation.di.ShopCreationComponent
import com.tokopedia.loginregister.shopcreation.domain.ProjectInfoResult
import com.tokopedia.loginregister.shopcreation.view.KycBridgingViewModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

class ShopCreationKycStatusFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<LayoutShopCreationKycStatusBinding>()

    @Inject
    lateinit var shopCreationAnalytics: ShopCreationAnalytics

    @Inject
    lateinit var viewModel: KycBridgingViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    private val startReVerifyKycForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _: ActivityResult ->
        onRefreshStatus()
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
        initToolbar()
        initGuidelineShopSpannable()
    }

    private fun initToolbar() {
        binding?.unifyToolbar?.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun initGuidelineShopSpannable() {
        val message = getString(R.string.shop_creation_guideline)
        val spannable = SpannableString(message)
        spannable.setSpan(
            object : ClickableSpan() {
                override fun onClick(view: View) {
                    goToGuidelineOpenShop()
                    shopCreationAnalytics.sendSellerClickToSellerEducationMaterialsEvent(shopId = userSession.shopId, userId = userSession.userId)
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                    ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
            },
            message.indexOf(getString(R.string.shop_creation_guideline_spannable)),
            message.indexOf(getString(R.string.shop_creation_guideline_spannable)) + getString(R.string.shop_creation_guideline_spannable).length,
            0
        )
        binding?.layoutStatusPending?.tvGuidelineOpenShop?.apply {
            movementMethod = LinkMovementMethod.getInstance()
            setText(spannable, TextView.BufferType.SPANNABLE)
        }
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
                    onBlacklisted()
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
        binding?.apply {
            unifyToolbar.show()
            layoutStatusPending.ivStatusSubmission.loadImageWithoutPlaceholder(getString(R.string.shop_creation_kyc_pending))
            layoutStatusPending.btnPrimary.isLoading = false
            layoutStatusPending.layoutBenefit.iconBenefit1.loadImage(getString(R.string.shop_creation_icon_benefit_1))
            layoutStatusPending.layoutBenefit.iconBenefit2.loadImage(getString(R.string.shop_creation_icon_benefit_2))
            layoutStatusPending.layoutBenefit.iconBenefit3.loadImage(getString(R.string.shop_creation_icon_benefit_3))
            layoutStatusPending.root.show()
            layoutStatusRejected.root.hide()
            layoutStatusBlacklisted.root.hide()
            globalError.hide()
            loader.hide()
        }
    }

    private fun onRejected(rejectionReason: List<String>) {

        rejectionReason.forEach {
            val item = ItemKycRejectionReasonBinding.inflate(layoutInflater)
            item.txtReason.text = it

            binding?.layoutStatusRejected?.listReason?.addView(item.root)
        }

        binding?.apply {
            unifyToolbar.show()
            layoutStatusPending.root.hide()
            layoutStatusRejected.ivStatusSubmission.loadImageWithoutPlaceholder(getString(R.string.shop_creation_kyc_rejected))
            layoutStatusRejected.root.show()
            layoutStatusBlacklisted.root.hide()
            globalError.hide()
            loader.hide()
        }
    }

    private fun onBlacklisted() {
        binding?.apply {
            unifyToolbar.show()
            layoutStatusPending.root.hide()
            layoutStatusBlacklisted.ivStatusSubmission.loadImageWithoutPlaceholder(getString(R.string.shop_creation_kyc_rejected))
            layoutStatusRejected.root.hide()
            layoutStatusBlacklisted.root.show()
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
            binding?.layoutStatusPending?.btnPrimary?.isLoading = true
            viewModel.checkKycStatus()
        }

        binding?.layoutStatusRejected?.btnPrimary?.setOnClickListener {
            shopCreationAnalytics.sendSellerClickVerifikasiUlangEvent(shopId = userSession.shopId, userId = userSession.userId)
            launchKyc()
        }

        binding?.layoutStatusRejected?.btnSecondary?.setOnClickListener {
            goToTokopediaCare()
        }

        binding?.layoutStatusBlacklisted?.btnSecondary?.setOnClickListener {
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
            layoutStatusPending.root.hide()
            layoutStatusRejected.root.hide()
            layoutStatusBlacklisted.root.hide()
            globalError.show()
            unifyToolbar.show()
        }

        if (throwable != null) {
            when (throwable) {
                is UnknownHostException, is ConnectException -> {
                    binding?.globalError?.setType(GlobalError.NO_CONNECTION)
                }
                else -> {
                    binding?.globalError?.apply {
                        setType(GlobalError.SERVER_ERROR)
                    }
                }
            }
        } else {
            binding?.globalError?.setType(GlobalError.SERVER_ERROR)
        }
    }

    private fun onRefreshStatus() {
        shopCreationAnalytics.sendSellerClickRefreshStatusEvent(userId = userSession.userId, shopId = userSession.shopId)
        binding?.apply {
            unifyToolbar.hide()
            loader.show()
            layoutStatusPending.root.hide()
            layoutStatusRejected.root.hide()
            layoutStatusBlacklisted.root.hide()
            globalError.hide()
        }
        viewModel.checkKycStatus()
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

    private fun goToGuidelineOpenShop() {
        RouteManager.route(
            context,
            ApplinkConstInternalGlobal.WEBVIEW,
            getString(R.string.shop_creation_guideline_open_shop)
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
