package com.tokopedia.shopadmin.invitationaccepted.presentation.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.APPLINK_PLAYSTORE
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.PACKAGE_SELLER_APP
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.SELLER_MIGRATION_KEY_AUTO_LOGIN
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.URL_PLAYSTORE
import com.tokopedia.shopadmin.R
import com.tokopedia.shopadmin.databinding.FragmentAdminInvitationAcceptedBinding
import com.tokopedia.shopadmin.invitationaccepted.di.component.AdminInvitationAcceptedComponent
import com.tokopedia.shopadmin.invitationconfirmation.di.component.AdminInvitationConfirmationComponent
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
    }

    override fun initInjector() {
        getComponent(AdminInvitationAcceptedComponent::class.java).inject(this)
    }

    private fun goToPlayStoreOrSellerApp() {
        try {
            val intent = context?.packageManager?.getLaunchIntentForPackage(PACKAGE_SELLER_APP)
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
        } catch (anfe: ActivityNotFoundException) {
            activity?.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(URL_PLAYSTORE + PACKAGE_SELLER_APP)
                )
            )
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
    }
}