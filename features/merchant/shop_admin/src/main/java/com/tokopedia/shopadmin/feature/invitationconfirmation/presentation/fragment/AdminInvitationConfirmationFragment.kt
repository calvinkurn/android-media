package com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.config.GlobalConfig
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
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.dialog.AdminInvitationConfirmRejectDialog
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.model.ShopAdminInfoUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.viewmodel.AdminInvitationConfirmationViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class AdminInvitationConfirmationFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(AdminInvitationConfirmationViewModel::class.java)
    }

    private var binding by autoClearedNullable<FragmentAdminInvitationConfirmationBinding>()

    private var confirmationBinding by autoClearedNullable<ItemAdminConfirmationInvitationBinding>()

    private var expiredBinding by autoClearedNullable<ItemAdminInvitationExpiredBinding>()

    private var rejectedBinding by autoClearedNullable<ItemAdminInvitationRejectedBinding>()

    private var confirmRejectDialog: AdminInvitationConfirmRejectDialog? = null

    private var shopManageID = ""

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
        } else {
            if (resultCode == Activity.RESULT_OK) {
                RouteManager.route(
                    context,
                    ApplinkConstInternalMarketplace.ADMIN_INVITATION_ACCEPTED
                )
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
                }
            }
        }
    }

    private fun observeAdminInfo() {
        observe(viewModel.adminInfo) {
            when (it) {
                is Success -> {
                    showAdminType(it.data.adminTypeUiModel.status)
                    shopManageID = it.data.adminDataUiModel.shopManageID
                }
                is Fail -> {
                    hideLoading()
                    val message = ErrorHandler.getErrorMessage(context, it.throwable)
                    showToaster(message)
                }
            }
        }
    }

    private fun loadAdminInfo() {
        viewModel.getAdminInfo(userSession.shopId.toLongOrZero())
    }

    private fun showAdminType(adminStatus: String) {
        when (adminStatus) {
            AdminStatus.ACTIVE -> goToShopAccount()
            AdminStatus.WAITING_CONFIRMATION -> {
                viewModel.getShopAdminInfo()
            }
            AdminStatus.REJECT -> inflateInvitationRejected()
            AdminStatus.EXPIRED -> inflateInvitationExpired()
        }
    }

    private fun inflateInvitationShopAdminInfo(shopAdminInfoUiModel: ShopAdminInfoUiModel) {
        val vsInvitationActive = binding?.root?.findViewById<View>(R.id.vsInvitationActive)
        if (vsInvitationActive is ViewStub) {
            vsInvitationActive.inflate()
            confirmationBinding = ItemAdminConfirmationInvitationBinding.bind(vsInvitationActive)
            actionButton()
        } else {
            vsInvitationActive?.show()
        }
        setShopAdminInfo(shopAdminInfoUiModel)
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
                goToShopAccount()
            }
        }
    }

    private fun setInvitationRejected() {
        rejectedBinding?.run {
            imgInvitationRejected.setImageUrl(AdminImageUrl.IL_INVITATION_REJECTED)
            btnInvitationRejected.setOnClickListener {
                goToShopAccount()
            }
        }
    }

    private fun setShopAdminInfo(shopAdminInfoUiModel: ShopAdminInfoUiModel) {
        confirmationBinding?.run {
            imgAdminConfirmationInvitation.setImageUrl(
                AdminImageUrl.IL_CONFIRMATION_INVITATION
            )
            tvAdminConfirmationTitle.text = getString(R.string.title_admin_confirmation_invitation,
                shopAdminInfoUiModel.shopName)
            imgShopAdminAvatar.setImageUrl(shopAdminInfoUiModel.iconUrl)
            tvShopTitle.text = shopAdminInfoUiModel.shopName
        }
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

    private fun goToShopAccount() {
        val appLink = if (GlobalConfig.isSellerApp()) {
            ApplinkConstInternalSellerapp.SELLER_HOME
        } else {
            ApplinkConstInternalSellerapp.SELLER_MENU
        }
        RouteManager.route(context, appLink)
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

    private fun actionButton() {
        confirmationBinding?.run {
            btnAccessReject.setOnClickListener {
                showRejectConfirmationDialog()
            }
            btnAccessAccept.setOnClickListener {
                viewModel.adminConfirmationReg()
            }
        }
    }

    private fun showLoadingDialog() {
        confirmRejectDialog?.getDialog()?.dialogPrimaryCTA?.isLoading = true
    }

    private fun hideLoadingDialog() {
        confirmRejectDialog?.getDialog()?.dialogPrimaryCTA?.isLoading = false
    }

    private fun showRejectConfirmationDialog() {
        confirmRejectDialog = getInstanceDialog().value
        confirmRejectDialog?.getDialog()?.run {
            setPrimaryCTAClickListener {
                dialogPrimaryCTA.isLoading = true
                viewModel.adminConfirmationReg()
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
    }
}