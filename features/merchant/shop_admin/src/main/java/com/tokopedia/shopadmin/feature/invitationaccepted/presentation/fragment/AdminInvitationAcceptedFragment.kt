package com.tokopedia.shopadmin.feature.invitationaccepted.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.shopadmin.ShopAdminDeepLinkMapper
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shopadmin.R
import com.tokopedia.shopadmin.common.analytics.ShopAdminTrackers
import com.tokopedia.shopadmin.common.constants.AdminImageUrl
import com.tokopedia.shopadmin.common.presentation.navigator.goToPlayStoreOrSellerApp
import com.tokopedia.shopadmin.common.utils.ShopAdminErrorLogger
import com.tokopedia.shopadmin.common.utils.getGlobalErrorType
import com.tokopedia.shopadmin.common.utils.setTextMakeHyperlink
import com.tokopedia.shopadmin.databinding.FragmentAdminInvitationAcceptedBinding
import com.tokopedia.shopadmin.feature.invitationaccepted.di.component.AdminInvitationAcceptedComponent
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.adapter.ItemFeatureAccessAdapter
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.bottomsheet.TncAdminBottomSheet
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.model.AdminPermissionUiModel
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.viewmodel.InvitationAcceptedViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class AdminInvitationAcceptedFragment : BaseDaggerFragment() {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var shopAdminTrackers: ShopAdminTrackers

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(InvitationAcceptedViewModel::class.java)
    }

    private var binding by autoClearedNullable<FragmentAdminInvitationAcceptedBinding>()

    private var shopName = ""

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
        setShopAdminBackground()
        setShopNameFromArgs()
        hideViewGroup()
        setupActionButton()
        observeAdminPermission()
        loadAdminPermission()
    }

    override fun initInjector() {
        getComponent(AdminInvitationAcceptedComponent::class.java).inject(this)
    }

    override fun onResume() {
        super.onResume()
        shopAdminTrackers.impressionAcceptedPage()
    }

    private fun setShopAdminBackground() {
        context?.let {
            activity?.window?.decorView?.setBackgroundColor(
                ContextCompat.getColor(
                    it,
                    com.tokopedia.unifyprinciples.R.color.Unify_Background
                )
            )
        }
    }

    private fun setShopNameFromArgs() {
        this.shopName = arguments?.getString(ShopAdminDeepLinkMapper.SHOP_NAME).orEmpty()
    }

    private fun observeAdminPermission() {
        observe(viewModel.adminPermission) {
            hideLoading()
            when (it) {
                is Success -> {
                    setupViews(it.data)
                }
                is Fail -> {
                    val message = ErrorHandler.getErrorMessage(context, it.throwable)
                    showToaster(message)
                    showGlobalError(it.throwable)
                    ShopAdminErrorLogger.logToCrashlytic(
                        ShopAdminErrorLogger.PERMISSION_LIST_ERROR,
                        it.throwable
                    )
                }
            }
        }
    }

    private fun loadAdminPermission() {
        showLoading()
        viewModel.fetchAdminPermission(userSession.shopId)
    }

    private fun setupViews(item: List<AdminPermissionUiModel>) {
        binding?.run {
            showViewGroup()
            imgHeaderInvitationAccepted.setImageUrl(AdminImageUrl.IL_WELCOME_INVITATION_ACCEPTED)
            tvAdminSuccessInvitation.text = getString(
                R.string.title_admin_success_invitation_accepted,
                userSession.name, shopName
            )
            tvTnc.setTextMakeHyperlink(getString(R.string.label_tnc_invitation_accepted)) {
                showTncBottomSheet()
            }
            setupFeatureAccessList(item)
        }
    }

    private fun showTncBottomSheet() {
        val tncAdminBottomSheet = TncAdminBottomSheet()
        tncAdminBottomSheet.show(childFragmentManager)
    }

    private fun setupFeatureAccessList(featureAccessList: List<AdminPermissionUiModel>) {
        binding?.rvFeatureAccess?.run {
            layoutManager = object : GridLayoutManager(
                context, COLUMN_TWO,
                VERTICAL, false
            ) {
                override fun canScrollVertically(): Boolean = false
            }
            adapter = ItemFeatureAccessAdapter(featureAccessList)
        }
    }

    private fun setupActionButton() {
        binding?.btnGoToShop?.setOnClickListener {
            val isChecked = binding?.cbTnc?.isChecked == true
            if (isChecked) {
                activity?.goToPlayStoreOrSellerApp()
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

    private fun showGlobalError(throwable: Throwable) {
        binding?.globalErrorInvitationAccepted?.run {
            setType(throwable.getGlobalErrorType())
            show()
        }
    }

    companion object {
        fun newInstance(bundle: Bundle?): AdminInvitationAcceptedFragment {
            return if (bundle == null) {
                AdminInvitationAcceptedFragment()
            } else {
                AdminInvitationAcceptedFragment().apply {
                    arguments = bundle
                }
            }
        }

        const val COLUMN_TWO = 2
    }
}