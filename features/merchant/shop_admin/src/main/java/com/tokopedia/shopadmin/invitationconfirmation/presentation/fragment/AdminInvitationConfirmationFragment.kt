package com.tokopedia.shopadmin.invitationconfirmation.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shopadmin.R
import com.tokopedia.shopadmin.databinding.FragmentAdminInvitationConfirmationBinding
import com.tokopedia.shopadmin.invitationconfirmation.di.component.AdminInvitationConfirmationComponent
import com.tokopedia.shopadmin.invitationconfirmation.presentation.dialog.AdminInvitationConfirmRejectDialog
import com.tokopedia.shopadmin.invitationconfirmation.presentation.viewmodel.AdminInvitationConfirmationViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class AdminInvitationConfirmationFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(AdminInvitationConfirmationViewModel::class.java)
    }

    private var binding by autoClearedNullable<FragmentAdminInvitationConfirmationBinding>()

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

    override fun initInjector() {
        getComponent(AdminInvitationConfirmationComponent::class.java).inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            RouteManager.route(context, ApplinkConstInternalMarketplace.ADMIN_INVITATION_ACCEPTED)
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

    private fun hideLoadingDialog() {
        confirmRejectDialog?.getDialog()?.dialogPrimaryCTA.isLoading = false
    }

    private fun showRejectConfirmationDialog() {
        confirmRejectDialog = getInstanceDialog().value
        confirmRejectDialog?.getDialog()?.run {
            setPrimaryCTAClickListener {
                dialogPrimaryCTA.isLoading = true
                //todo
            }
            setSecondaryCTAClickListener {
                dismiss()
            }
            show()
        }
    }
    companion object {
        fun newInstance(): AdminInvitationConfirmationFragment {
            return AdminInvitationConfirmationFragment()
        }
    }
}