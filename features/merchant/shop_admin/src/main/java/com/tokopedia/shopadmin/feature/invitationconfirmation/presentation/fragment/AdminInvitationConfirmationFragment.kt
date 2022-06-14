package com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shopadmin.R
import com.tokopedia.shopadmin.common.constants.AdminImageUrl
import com.tokopedia.shopadmin.common.constants.AdminStatus
import com.tokopedia.shopadmin.common.utils.getGlobalErrorType
import com.tokopedia.shopadmin.databinding.FragmentAdminInvitationConfirmationBinding
import com.tokopedia.shopadmin.databinding.ItemAdminConfirmationInvitationBinding
import com.tokopedia.shopadmin.databinding.ItemAdminInvitationExpiredBinding
import com.tokopedia.shopadmin.databinding.ItemAdminInvitationRejectedBinding
import com.tokopedia.shopadmin.feature.invitationconfirmation.di.component.AdminInvitationConfirmationComponent
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.param.InvitationConfirmationParam
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.dialog.AdminInvitationConfirmRejectDialog
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.navigator.InvitationConfirmationNavigator
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.AdminConfirmationRegUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.ShopAdminInfoUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.UserProfileUpdateUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.ValidateAdminEmailUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.viewmodel.InvitationConfirmationViewModel
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
        ).get(InvitationConfirmationViewModel::class.java)
    }

    private val navigator by lazy {
        InvitationConfirmationNavigator(this, invitationConfirmationParam)
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
        observeAdminType()
        observeShopAdminInfo()
        observeConfirmationReg()
        observeValidationEmail()
        observeUpdateUserProfile()
        actionGlobalError()
        checkAfterLogin()
    }

    override fun onResume() {
        super.onResume()
        checkAfterLogin()
    }

    override fun initInjector() {
        getComponent(AdminInvitationConfirmationComponent::class.java).inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == InvitationConfirmationNavigator.REQUEST_LOGIN) {
                checkAfterLogin()
            } else if (requestCode == InvitationConfirmationNavigator.REQUEST_OTP) {
                showLoadingConfirmationCta()
                fetchUpdateUserProfile()
            }
        }
    }

    private fun observeValidationEmail() {
        lifecycleScope.launchWhenResumed {
            viewModel.validateEmail.collect {
                when (it) {
                    is Success -> {
                        setValidateEmailMessage(it.data)
                    }
                    is Fail -> {
                        val message = ErrorHandler.getErrorMessage(context, it.throwable)
                        showToaster(message)
                    }
                }
            }
        }
    }

    private fun fetchUpdateUserProfile() {
        viewModel.updateUserProfile(getEmailFromTextField())
    }

    private fun observeUpdateUserProfile() {
        observe(viewModel.updateUserProfile) {
            hideAllLoadingCta()
            when (it) {
                is Success -> {
                    setActionAfterUpdateUserProfile(it.data)
                }
                is Fail -> {
                    val message = ErrorHandler.getErrorMessage(context, it.throwable)
                    showToaster(message)
                }
            }
        }
    }

    private fun setActionAfterUpdateUserProfile(userProfileUpdate: UserProfileUpdateUiModel) {
        if (userProfileUpdate.isSuccess) {
            adminConfirmationReg(true)
        } else {
            showToaster(userProfileUpdate.errorMessage)
        }
    }

    private fun setValidateEmailMessage(validateAdminEmailUiModel: ValidateAdminEmailUiModel) {
        confirmationBinding?.adminInvitationWithNoEmailSection?.tfuAdminConfirmationEmail?.run {
            if (!validateAdminEmailUiModel.isSuccess && validateAdminEmailUiModel.message.isNotBlank()) {
                setError(true)
                setMessage(validateAdminEmailUiModel.message)
                textFieldWrapper.setHelperTextColor(
                    ContextCompat.getColorStateList(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_R500
                    )
                )
                setAccessAcceptedBtnDisabled()
            } else if (!validateAdminEmailUiModel.isSuccess && validateAdminEmailUiModel.existsUser) {
                setError(true)
                setMessage(getString(R.string.error_message_email_has_been_registered))
                textFieldWrapper.setHelperTextColor(
                    ContextCompat.getColorStateList(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_R500
                    )
                )
                setAccessAcceptedBtnDisabled()
            } else {
                setError(false)
                setMessage("")
                setAccessAcceptedBtnActive()
            }
        }
    }

    private fun observeShopAdminInfo() {
        observe(viewModel.shopAdminInfo) {
            hideLoading()
            when (it) {
                is Success -> {
                    invitationConfirmationParam.setShopManageId(it.data.shopManageId)
                    inflateInvitationShopAdminInfo(it.data)
                }
                is Fail -> {
                    val message = ErrorHandler.getErrorMessage(context, it.throwable)
                    showToaster(message)
                    showGlobalError(it.throwable)
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

    private fun observeAdminType() {
        observe(viewModel.adminType) {
            when (it) {
                is Success -> {
                    invitationConfirmationParam.setShopId(it.data.shopID)
                    showAdminType(it.data.status)
                }
                is Fail -> {
                    hideLoading()
                    showGlobalError(it.throwable)
                    setupToolbar(true)
                }
            }
        }
    }

    private fun fetchAdminType() {
        viewModel.fetchAdminType()
    }

    private fun redirectAfterConfirmReg(adminConfirmationRegUiModel: AdminConfirmationRegUiModel) {
        if (adminConfirmationRegUiModel.acceptBecomeAdmin) {
            navigator.goToInvitationAccepted()
        } else {
            confirmRejectDialog?.dismissDialog()
            inflateInvitationRejected()
        }
    }

    private fun showAdminType(adminStatus: String) {
        when (adminStatus) {
            AdminStatus.ACTIVE -> navigator.goToShopAccount()
            AdminStatus.WAITING_CONFIRMATION -> {
                viewModel.fetchShopAdminInfo(invitationConfirmationParam.getShopId().toLongOrZero())
            }
            AdminStatus.REJECT -> inflateInvitationRejected()
            AdminStatus.EXPIRED -> inflateInvitationExpired()
            else -> activity?.finish()
        }
    }

    private fun inflateInvitationShopAdminInfo(shopAdminInfoUiModel: ShopAdminInfoUiModel) {
        setupToolbar(false)
        expiredBinding?.root?.hide()
        rejectedBinding?.root?.hide()
        binding?.run {
            val invitationActiveVs = root.findViewById<View>(R.id.vsInvitationActive)
            if (invitationActiveVs is ViewStub) {
                invitationActiveVs.inflate()
                setupConfirmationBinding(root)
                actionConfirmationButton()
            } else {
                vsInvitationActive.show()
            }
            setShopAdminInfo(shopAdminInfoUiModel)
        }
    }

    private fun setupConfirmationBinding(root: View) {
        if (confirmationBinding == null) {
            confirmationBinding =
                ItemAdminConfirmationInvitationBinding.bind(root.findViewById(R.id.vsInvitationActive))
        }
    }

    private fun setupToolbar(isError: Boolean) {
        (activity as? AppCompatActivity)?.run {
            binding?.headerInvitationConfirmation?.apply {
                val drawableIcon = if (isError)
                    ContextCompat.getDrawable(this@run, R.drawable.ic_shop_admin_arrow_back)
                else
                    ContextCompat.getDrawable(this@run, R.drawable.ic_shop_admin_close)
                navigationIcon = drawableIcon
                setSupportActionBar(this)
                show()

                binding?.headerInvitationConfirmation?.setOnClickListener {
                    navigator.goToHomeBuyer()
                }
            }
        }
    }

    private fun inflateInvitationExpired() {
        hideLoading()
        setupToolbar(false)
        confirmationBinding?.root?.hide()
        rejectedBinding?.root?.hide()
        binding?.run {
            val vsInvitationExpired = root.findViewById<View>(R.id.vsInvitationExpired)
            if (vsInvitationExpired is ViewStub) {
                vsInvitationExpired.inflate()
                setupExpiredBinding(root)
            } else {
                vsInvitationExpired?.show()
            }
            setInvitationExpired()
        }
    }

    private fun setupExpiredBinding(root: View) {
        if (expiredBinding == null) {
            expiredBinding =
                ItemAdminInvitationExpiredBinding.bind(root.findViewById(R.id.vsInvitationExpired))
        }
    }

    private fun inflateInvitationRejected() {
        hideLoading()
        setupToolbar(false)
        confirmationBinding?.root?.hide()
        expiredBinding?.root?.hide()
        binding?.run {
            val vsInvitationReject = root.findViewById<View>(R.id.vsInvitationReject)
            if (vsInvitationReject is ViewStub) {
                vsInvitationReject.inflate()
                setupRejectBinding(root)
            } else {
                vsInvitationReject?.show()
            }
            setInvitationRejected()
        }
    }

    private fun setupRejectBinding(root: View) {
        if (rejectedBinding == null) {
            rejectedBinding =
                ItemAdminInvitationRejectedBinding.bind(root.findViewById(R.id.vsInvitationReject))
        }
    }

    private fun setInvitationExpired() {
        expiredBinding?.run {
            imgInvitationExpires.setImageUrl(AdminImageUrl.IL_INVITATION_EXPIRES)
            btnInvitationExpires.setOnClickListener {
                navigator.goToHomeBuyer()
            }
        }
    }

    private fun setInvitationRejected() {
        rejectedBinding?.run {
            tvInvitationRejectedDesc.text = getString(
                R.string.desc_invitation_rejected,
                invitationConfirmationParam.getShopName()
            )
            imgInvitationRejected.setImageUrl(AdminImageUrl.IL_INVITATION_REJECTED)
            btnInvitationRejected.setOnClickListener {
                navigator.goToHomeBuyer()
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
                setAccessAcceptedBtnDisabled()
                emailTypingListener()
            } else {
                adminInvitationWithEmailSection.root.show()
                adminInvitationWithNoEmailSection.root.hide()
            }
        }
    }

    private fun emailTypingListener() {
        confirmationBinding?.adminInvitationWithNoEmailSection?.tfuAdminConfirmationEmail?.run {
            textFieldWrapper.editText?.afterTextChanged { s ->
                val email = s.trim()
                viewModel.validateAdminEmail(email)
            }
        }
    }

    private fun checkAfterLogin() {
        if (userSession.isLoggedIn) {
            fetchInvitationIsActive()
        } else {
            navigator.goToLogin()
        }
    }

    private fun fetchInvitationIsActive() {
        showLoading()
        fetchAdminType()
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
                navigator.goToOtp(getEmailFromTextField(), userSession.phoneNumber.orEmpty())
            }
        }
    }

    private fun getEmailFromTextField(): String {
        return confirmationBinding
            ?.adminInvitationWithNoEmailSection
            ?.tfuAdminConfirmationEmail
            ?.textFieldWrapper
            ?.editText?.text?.trim()?.toString().orEmpty()
    }

    private fun adminConfirmationReg(acceptBecomeAdmin: Boolean) {
        val email = getEmailFromTextField().ifEmpty { userSession.email }.orEmpty()
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

    private fun showGlobalError(throwable: Throwable) {
        binding?.globalErrorConfirmationInvitation?.run {
            setType(throwable.getGlobalErrorType())
            show()
        }
    }

    private fun actionGlobalError() {
        binding?.globalErrorConfirmationInvitation?.setActionClickListener {
            showLoading()
            fetchAdminType()
            hideGlobalError()
        }
    }

    private fun setAccessAcceptedBtnDisabled() {
        confirmationBinding?.btnAccessAccept?.run {
            isEnabled = false
            isClickable = false
        }
    }

    private fun setAccessAcceptedBtnActive() {
        confirmationBinding?.btnAccessAccept?.run {
            isEnabled = true
            isClickable = true
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
        if (message.isNotBlank()) {
            view?.let {
                Toaster.build(
                    it,
                    message,
                    Toaster.LENGTH_SHORT,
                    Toaster.TYPE_ERROR
                ).show()
            }
        }
    }

    companion object {
        fun newInstance(): AdminInvitationConfirmationFragment {
            return AdminInvitationConfirmationFragment()
        }
    }
}