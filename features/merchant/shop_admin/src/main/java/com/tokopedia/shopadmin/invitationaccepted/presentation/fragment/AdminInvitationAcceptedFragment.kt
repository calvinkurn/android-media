package com.tokopedia.shopadmin.invitationaccepted.presentation.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.APPLINK_PLAYSTORE
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.PACKAGE_SELLER_APP
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.SELLER_MIGRATION_KEY_AUTO_LOGIN
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.URL_PLAYSTORE
import com.tokopedia.shopadmin.R
import com.tokopedia.shopadmin.common.constants.AdminImageUrl
import com.tokopedia.shopadmin.common.utils.setTextMakeHyperlink
import com.tokopedia.shopadmin.databinding.FragmentAdminInvitationAcceptedBinding
import com.tokopedia.shopadmin.invitationaccepted.di.component.AdminInvitationAcceptedComponent
import com.tokopedia.shopadmin.invitationaccepted.presentation.adapter.ItemFeatureAccessAdapter
import com.tokopedia.shopadmin.invitationaccepted.presentation.bottomsheet.TncAdminBottomSheet
import com.tokopedia.shopadmin.invitationaccepted.presentation.model.AdminInfoUiModel
import com.tokopedia.shopadmin.invitationaccepted.presentation.model.FeatureAccessUiModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable

class AdminInvitationAcceptedFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentAdminInvitationAcceptedBinding>()

    override fun getScreenName(): String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminInvitationAcceptedBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideViewGroup()
        setupActionButton()
    }

    override fun initInjector() {
        getComponent(AdminInvitationAcceptedComponent::class.java).inject(this)
    }

    private fun setupViews(item: AdminInfoUiModel) {
        binding?.run {
            showViewGroup()
            imgHeaderInvitationAccepted.setImageUrl(AdminImageUrl.IL_WELCOME_INVITATION_ACCEPTED)
            tvAdminSuccessInvitation.text = getString(
                R.string.title_admin_success_invitation_accepted,
                item.adminName, item.shopName
            )
            tvTnc.setTextMakeHyperlink(getString(R.string.label_tnc_invitation_accepted)) {
                showTncBottomSheet()
            }
        }
    }

    private fun showTncBottomSheet() {
        val tncAdminBottomSheet = TncAdminBottomSheet()
        tncAdminBottomSheet.show(childFragmentManager)
    }

    private fun setupFeatureAccessList(featureAccessList: List<FeatureAccessUiModel>) {
        binding?.rvFeatureAccess?.run {
            layoutManager = GridLayoutManager(
                context, COLUMN_TWO,
                GridLayoutManager.VERTICAL, false
            )
            adapter = ItemFeatureAccessAdapter(featureAccessList)
        }
    }

    private fun goToPlayStoreOrSellerApp() {
        try {
            val intent = activity?.packageManager?.getLaunchIntentForPackage(PACKAGE_SELLER_APP)
            if (intent != null) {
                intent.putExtra(SELLER_MIGRATION_KEY_AUTO_LOGIN, true)
                activity?.startActivity(intent)
            } else {
                activity?.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(APPLINK_PLAYSTORE + PACKAGE_SELLER_APP)
                    )
                )
            }
        } catch (e: ActivityNotFoundException) {
            activity?.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(URL_PLAYSTORE + PACKAGE_SELLER_APP)
                )
            )
        }
    }

    private fun setupActionButton() {
        binding?.btnGoToShop?.setOnClickListener {
            val isChecked = binding?.cbTnc?.isChecked == true
            if (isChecked) {
                goToPlayStoreOrSellerApp()
            } else {
                showRequiredTncToaster()
            }
        }
    }

    private fun showRequiredTncToaster() {
        view?.let {
            Toaster.build(
                it,
                getString(R.string.title_not_yet_checked_tnc),
                Toaster.LENGTH_SHORT,
                Toaster.TYPE_ERROR,
                actionText = getString(R.string.action_not_yet_checked_tnc)
            ).show()
        }
    }

    private fun hideViewGroup() {
        binding?.visibleViewGroup?.hide()
    }

    private fun showViewGroup() {
        binding?.visibleViewGroup?.show()
    }

    private fun showLoading() {
        binding?.loaderInvitationAccepted?.show()
    }

    private fun hideLoading() {
        binding?.loaderInvitationAccepted?.hide()
    }

    companion object {

        fun newInstance(): AdminInvitationAcceptedFragment {
            return AdminInvitationAcceptedFragment()
        }

        const val COLUMN_TWO = 2
    }
}