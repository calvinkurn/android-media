package com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shopadmin.R
import com.tokopedia.shopadmin.common.constants.AdminImageUrl
import com.tokopedia.shopadmin.common.constants.AdminStatus
import com.tokopedia.shopadmin.databinding.FragmentAdminInvitationConfirmationBinding
import com.tokopedia.shopadmin.databinding.ItemAdminConfirmationInvitationBinding
import com.tokopedia.shopadmin.databinding.ItemAdminInvitationExpiredBinding
import com.tokopedia.shopadmin.databinding.ItemAdminInvitationRejectedBinding
import com.tokopedia.shopadmin.feature.invitationconfirmation.di.component.AdminInvitationConfirmationComponent
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.param.InvitationConfirmationParam
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.dialog.AdminInvitationConfirmRejectDialog
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.model.AdminConfirmationRegUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.model.ShopAdminInfoUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.model.ValidateAdminEmailEvent
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.model.ValidateEmailUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.navigator.InvitationConfirmationNavigator
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.viewmodel.AdminInvitationConfirmationViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class AdminInvitationConfirmationFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var invitationConfirmationParam: InvitationConfirmationParam

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(AdminInvitationConfirmationViewModel::class.java)
    }

    private val navigator by lazy {
        context?.let { InvitationConfirmationNavigator(it, invitationConfirmationParam) }
    }

    private var binding by autoClearedNullable<FragmentAdminInvitationConfirmationBinding>()
    private var confirmationBinding by autoClearedNullable<ItemAdminConfirmationInvitationBinding>()
    private var expiredBinding by autoClearedNullable<ItemAdminInvitationExpiredBinding>()
    private var rejectedBinding by autoClearedNullable<ItemAdminInvitationRejectedBinding>()

    private var confirmRejectDialog: AdminInvitationConfirmRejectDialog? = null

    override fun getScreenName(): String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminInvitationConfirmationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAdminInfo()
        observeShopAdminInfo()
        observeConfirmationReg()
        observeValidationEmail()
        actionGlobalError()
        loadAdminInfo()
    }

    override fun initInjector() {
        getComponent(AdminInvitationConfirmationComponent::class.java).inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                handleAfterLogin()
            }
        } else if (requestCode == REQUEST_OTP) {
            if (resultCode == Activity.RESULT_OK) {
                showLoadingConfirmationCta()
                invitationConfirmationParam.setOtpToken(
                    data?.getStringExtra(
                        ApplinkConstInternalGlobal.PARAM_TOKEN
                    ).orEmpty()
                )
                adminConfirmationReg(true)
            }
        }
    }

    private fun observeValidationEmail() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.validateEmail.collect {
                when (it) {
                    is ValidateAdminEmailEvent.Success -> {
                        setValidateEmailMessage(it.validateEmailUiModel)
                    }
                    is ValidateAdminEmailEvent.Error -> {
                        val message = ErrorHandler.getErrorMessage(context, it.error)
                        showToaster(message)
                    }
                }
            }
        }
    }

    private fun setValidateEmailMessage(validateEmailUiModel: ValidateEmailUiModel) {
        confirmationBinding?.adminInvitationWithNoEmailSection?.tfuAdminConfirmationEmail?.run {
            if (!validateEmailUiModel.isSuccess && validateEmailUiModel.message.isNotEmpty()) {
                setMessage(validateEmailUiModel.message)
                setError(true)
            } else if (!validateEmailUiModel.isSuccess && validateEmailUiModel.existsUser) {
                setMessage(getString(R.string.error_message_email_has_been_registered))
                setError(true)
            } else {
                setMessage("")
                setError(false)
            }
        }
    }

    private fun observeShopAdminInfo() {
        observe(viewModel.shopAdminInfo) {
            hideLoading()
            when (it) {
                is Success -> {
                    inflateInvitationShopAdminInfo(it.data)
                }
                is Fail -> {
                    val message = ErrorHandler.getErrorMessage(context, it.throwable)
                    showToaster(message)
                    showGlobalError()
                }
            }
        }
    }

    private fun observeConfirmationReg() {
        observe(viewModel.confirmationReg) {
            hideAllLoadingCta()
            when (it) {
                is Success -> {
                    if (it.data.isSuccess) {
                        redirectAfterConfirmReg(it.data)
                    } else {
                        val message = it.data.message.ifEmpty {
                            getString(R.string.error_message_confirmation_rejected)
                        }
                        showToaster(message)
                    }
                }
                is Fail -> {
                    val message = ErrorHandler.getErrorMessage(context, it.throwable)
                    showToaster(message)
                }
            }
        }
    }

    private fun observeAdminInfo() {
        observe(viewModel.adminInfo) {
            when (it) {
                is Success -> {
                    showAdminType(it.data.adminTypeUiModel.status)
                    invitationConfirmationParam.setShopId(it.data.adminTypeUiModel.shopID)
                    invitationConfirmationParam.setShopManageId(it.data.adminDataUiModel.shopManageID)
                }
                is Fail -> {
                    hideLoading()
                    showGlobalError()
                }
            }
        }
    }

    private fun loadAdminInfo() {
        viewModel.getAdminInfo(userSession.shopId.toLongOrZero())
    }

    private fun redirectAfterConfirmReg(adminConfirmationRegUiModel: AdminConfirmationRegUiModel) {
        if (adminConfirmationRegUiModel.acceptBecomeAdmin) {
            navigator?.goToInvitationAccepted()
        } else {
            inflateInvitationRejected()
        }
    }

    private fun showAdminType(adminStatus: String) {
        when (adminStatus) {
            AdminStatus.ACTIVE -> navigator?.goToShopAccount()
            AdminStatus.WAITING_CONFIRMATION -> {
                viewModel.getShopAdminInfo(userSession.shopId)
            }
            AdminStatus.REJECT -> inflateInvitationRejected()
            AdminStatus.EXPIRED -> inflateInvitationExpired()
        }
    }

    private fun inflateInvitationShopAdminInfo(shopAdminInfoUiModel: ShopAdminInfoUiModel) {
        setupToolbar()
        val vsInvitationActive = binding?.root?.findViewById<View>(R.id.vsInvitationActive)
        if (vsInvitationActive is ViewStub) {
            vsInvitationActive.inflate()
            confirmationBinding = ItemAdminConfirmationInvitationBinding.bind(vsInvitationActive)
            actionConfirmationButton()
        } else {
            vsInvitationActive?.show()
        }
        setShopAdminInfo(shopAdminInfoUiModel)
    }

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.run {
            supportActionBar?.hide()
            binding?.headerInvitationConfirmation?.navigationIcon =
                getIconUnifyDrawable(this, IconUnify.CLOSE)
            setSupportActionBar(binding?.headerInvitationConfirmation)
            binding?.headerInvitationConfirmation?.setOnClickListener {
                navigator?.goToHomeBuyer()
            }
        }
    }

    private fun inflateInvitationExpired() {
        val vsInvitationExpired = binding?.root?.findViewById<View>(R.id.vsInvitationExpired)
        if (vsInvitationExpired is ViewStub) {
            vsInvitationExpired.inflate()
            expiredBinding = ItemAdminInvitationExpiredBinding.bind(vsInvitationExpired)
        } else {
            vsInvitationExpired?.show()
        }
        setInvitationExpired()
    }

    private fun inflateInvitationRejected() {
        val vsInvitationReject = binding?.root?.findViewById<View>(R.id.vsInvitationReject)
        if (vsInvitationReject is ViewStub) {
            vsInvitationReject.inflate()
            expiredBinding = ItemAdminInvitationExpiredBinding.bind(vsInvitationReject)
        } else {
            vsInvitationReject?.show()
        }
        setInvitationRejected()
    }

    private fun setInvitationExpired() {
        expiredBinding?.run {
            imgInvitationExpires.setImageUrl(AdminImageUrl.IL_INVITATION_EXPIRES)
            btnInvitationExpires.setOnClickListener {
                navigator?.goToHomeBuyer()
            }
        }
    }

    private fun setInvitationRejected() {
        rejectedBinding?.run {
            imgInvitationRejected.setImageUrl(AdminImageUrl.IL_INVITATION_REJECTED)
            btnInvitationRejected.setOnClickListener {
                navigator?.goToHomeBuyer()
            }
        }
    }

    private fun setShopAdminInfo(shopAdminInfoUiModel: ShopAdminInfoUiModel) {
        invitationConfirmationParam.setShopName(shopAdminInfoUiModel.shopName)
        confirmationBinding?.run {
            imgAdminConfirmationInvitation.setImageUrl(
                AdminImageUrl.IL_CONFIRMATION_INVITATION
            )
            tvAdminConfirmationTitle.text = getString(
                R.string.title_admin_confirmation_invitation,
                shopAdminInfoUiModel.shopName
            )
            imgShopAdminAvatar.setImageUrl(shopAdminInfoUiModel.iconUrl)
            tvShopTitle.text = invitationConfirmationParam.getShopName()

            if (userSession.email.isNullOrEmpty()) {
                adminInvitationWithEmailSection.root.hide()
                adminInvitationWithNoEmailSection.root.show()
                emailTypingListener()
            } else {
                adminInvitationWithEmailSection.root.show()
                adminInvitationWithNoEmailSection.root.hide()
            }
        }
    }

    private fun emailTypingListener() {
        confirmationBinding?.adminInvitationWithNoEmailSection?.tfuAdminConfirmationEmail?.run {
            textFieldWrapper.editText?.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    showKeyboard(this)
                }
            }
            textFieldWrapper.editText?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    val email = s.trim().toString()
                    viewModel.validateAdminEmail(email)
                }
            })
        }
    }

    private fun showKeyboard(view: View) {
        view.requestFocus()
        val imm =
            view.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(view, 0)
    }

    private fun handleAfterLogin() {
        if (userSession.isLoggedIn) {
            getInvitationIsActive()
        } else {
            activity?.setResult(Activity.RESULT_CANCELED)
            activity?.finish()
        }
    }

    private fun getInvitationIsActive() {
        showLoading()
        if (userSession.isLoggedIn) {
            loadAdminInfo()
        } else {
            activity?.let {
                val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
                it.startActivityForResult(intent, REQUEST_LOGIN)
            }
        }
    }

    private fun showLoading() {
        binding?.loaderInvitationConfirmation?.show()
    }

    private fun hideLoading() {
        binding?.loaderInvitationConfirmation?.hide()
    }

    private fun getInstanceDialog(): Lazy<AdminInvitationConfirmRejectDialog?> {
        return lazy {
            context?.let {
                AdminInvitationConfirmRejectDialog(it).apply {
                    setTitle(getString(R.string.title_admin_confirmation_reject))
                    setDescription(getString(R.string.desc_admin_confirmation_reject))
                    setPrimaryTitle(getString(R.string.primary_btn_admin_confirmation_reject))
                    setSecondaryTitle(getString(R.string.secondary_btn_admin_confirmation_reject))
                }
            }
        }
    }

    private fun actionConfirmationButton() {
        confirmationBinding?.run {
            btnAccessReject.setOnClickListener {
                showRejectConfirmationDialog()
            }
            btnAccessAccept.setOnClickListener {
                goToOtp()
            }
        }
    }

    private fun goToOtp() {
        context?.let {
            val intent = RouteManager.getIntent(it, ApplinkConstInternalGlobal.COTP)
            intent.apply {
                putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, getEmailFromTextField())
                putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_TYPE_EMAIL)
                putExtra(ApplinkConstInternalGlobal.PARAM_REQUEST_OTP_MODE, MODE_EMAIL)
                putExtra(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, false)
                putExtra(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, false)
            }
            startActivityForResult(intent, REQUEST_OTP)
        }
    }

    private fun getEmailFromTextField(): String {
        return confirmationBinding?.adminInvitationWithNoEmailSection?.tfuAdminConfirmationEmail?.textFieldWrapper?.editText.toString()
            .trim()
    }

    private fun adminConfirmationReg(acceptBecomeAdmin: Boolean) {
        val email = getEmailFromTextField().ifEmpty { userSession.email }
        viewModel.adminConfirmationReg(userSession.userId, email, acceptBecomeAdmin)
    }

    private fun showLoadingDialog() {
        confirmRejectDialog?.getDialog()?.dialogPrimaryCTA?.isLoading = true
    }

    private fun hideAllLoadingCta() {
        hideLoadingConfirmationCta()
        hideLoadingDialog()
    }

    private fun hideLoadingDialog() {
        confirmRejectDialog?.getDialog()?.dialogPrimaryCTA?.isLoading = false
    }

    private fun showLoadingConfirmationCta() {
        confirmationBinding?.btnAccessAccept?.isLoading = true
    }

    private fun hideLoadingConfirmationCta() {
        confirmationBinding?.btnAccessAccept?.isLoading = false
    }

    private fun hideGlobalError() {
        binding?.globalErrorConfirmationInvitation?.hide()
    }

    private fun showGlobalError() {
        binding?.globalErrorConfirmationInvitation?.show()
    }

    private fun actionGlobalError() {
        binding?.globalErrorConfirmationInvitation?.setActionClickListener {
            loadAdminInfo()
            hideGlobalError()
        }
    }

    private fun showRejectConfirmationDialog() {
        confirmRejectDialog = getInstanceDialog().value
        confirmRejectDialog?.dismissDialog()
        confirmRejectDialog?.getDialog()?.run {
            setPrimaryCTAClickListener {
                showLoadingDialog()
                adminConfirmationReg(false)
            }
            setSecondaryCTAClickListener {
                dismiss()
            }
            show()
        }
    }

    private fun showToaster(message: String) {
        view?.let {
            Toaster.build(
                it,
                message,
                Toaster.LENGTH_SHORT,
                Toaster.TYPE_ERROR
            ).show()
        }
    }

    companion object {
        fun newInstance(): AdminInvitationConfirmationFragment {
            return AdminInvitationConfirmationFragment()
        }

        private const val REQUEST_LOGIN = 459
        private const val REQUEST_OTP = 659
        private const val MODE_EMAIL = "email"
        private const val OTP_TYPE_EMAIL = 150
    }
}