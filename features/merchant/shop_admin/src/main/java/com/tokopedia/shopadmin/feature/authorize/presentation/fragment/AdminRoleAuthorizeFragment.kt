package com.tokopedia.shopadmin.feature.authorize.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.seller.menu.common.constant.SellerBaseUrl
import com.tokopedia.shopadmin.R
import com.tokopedia.shopadmin.common.util.AdminFeature
import com.tokopedia.shopadmin.common.util.AdminPermissionMapper
import com.tokopedia.shopadmin.common.utils.ShopAdminErrorLogger
import com.tokopedia.shopadmin.databinding.FragmentAdminRoleAuthorizeBinding
import com.tokopedia.shopadmin.feature.authorize.di.component.AdminRoleAuthorizeComponent
import com.tokopedia.shopadmin.feature.authorize.presentation.activity.AdminRoleAuthorizeActivity
import com.tokopedia.shopadmin.feature.authorize.presentation.viewmodel.AdminRoleAuthorizeViewModel
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class AdminRoleAuthorizeFragment: BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun createInstance(adminFeature: String): AdminRoleAuthorizeFragment = AdminRoleAuthorizeFragment().apply {
            Bundle().apply {
                putString(AdminRoleAuthorizeActivity.KEY_ADMIN_FEATURE, adminFeature)
            }.let {
                arguments = it
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var adminPermissionMapper: AdminPermissionMapper

    private val viewModel: AdminRoleAuthorizeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(AdminRoleAuthorizeViewModel::class.java)
    }

    private val adminFeature: String by lazy {
        arguments?.getString(AdminRoleAuthorizeActivity.KEY_ADMIN_FEATURE).orEmpty()
    }

    private var binding by autoClearedNullable<FragmentAdminRoleAuthorizeBinding>()

    private var adminErrorView: GlobalError? = null
    private var adminLoadingView: LoaderUnify? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        observeLiveData()
        binding = FragmentAdminRoleAuthorizeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setupAdminView()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(AdminRoleAuthorizeComponent::class.java).inject(this)
    }

    private fun View.setupAdminView() {
        adminErrorView = binding?.errorAdminRole
        adminLoadingView = binding?.loaderAdminRole

        setupGlobalError()
        setToolbarTitle(adminPermissionMapper.mapFeatureToToolbarTitle(context, adminFeature))

        viewModel.checkAccess(adminFeature)
    }

    private fun setupGlobalError() {
        adminErrorView?.run {
            errorSecondaryAction.visibility = View.GONE
            setButtonFull(true)
        }
    }

    private fun observeLiveData() {
        observeAdminAuthorize()
        observeIsLoading()
    }

    private fun observeAdminAuthorize() {
        viewModel.isRoleAuthorizedLiveData.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Success -> {
                    result.data.let { isEligible ->
                        if (isEligible) {
                            adminErrorView?.gone()
                            goToDestination()
                        } else {
                            adminErrorView?.show(true)
                        }
                    }
                }
                is Fail -> {
                    ShopAdminErrorLogger.logToCrashlytic(ShopAdminErrorLogger.ERROR_GET_ADMIN_ACCESS_ROLE, result.throwable)
                    adminErrorView?.show(false)
                }
            }
        }
    }

    private fun observeIsLoading() {
        viewModel.isLoadingLiveData.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                adminErrorView?.gone()
            }
            adminLoadingView?.showWithCondition(isLoading)
        }
    }

    private fun goToDestination() {
        context?.run {
            adminPermissionMapper.mapFeatureToDestination(this, adminFeature)?.let {
                activity?.finish()
                startActivity(it)
            }
        }
    }

    private fun setToolbarTitle(toolbarTitle: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = toolbarTitle
    }

    private fun GlobalError.show(isEligibilityError: Boolean) {
        if (isEligibilityError) {
            showAdminIneligible()
        } else {
            showAdminError()
        }
        visibility = View.VISIBLE
    }

    private fun GlobalError.showAdminIneligible() {
        ImageHandler.loadImageAndCache(errorIllustration, SellerBaseUrl.ADMIN_ERROR_ILLUSTRATION)
        getNoPermissionText(adminFeature).let { (title, desc) ->
            errorTitle.text = title
            errorDescription.text = desc
        }
        errorAction.text = context?.getString(com.tokopedia.shopadmin.common.R.string.admin_no_permission_back_to_seller_account)

        setActionClickListener {
            activity?.finish()
        }
    }

    private fun GlobalError.showAdminError() {
        setType(GlobalError.SERVER_ERROR)
        errorAction.text = context?.getString(R.string.admin_error_refresh_page)

        setActionClickListener {
            viewModel.checkAccess(adminFeature)
        }
    }

    private fun getNoPermissionText(@AdminFeature featureName: String): Pair<String, String> {
        val title: String
        val desc: String
        when(featureName) {
            AdminFeature.ADD_PRODUCT -> {
                title = context?.getString(com.tokopedia.shopadmin.common.R.string.admin_no_permission_product_add_title).orEmpty()
                desc = context?.getString(com.tokopedia.shopadmin.common.R.string.admin_no_permission_product_add_desc).orEmpty()
            }
            AdminFeature.MANAGE_PRODUCT -> {
                title = context?.getString(com.tokopedia.shopadmin.common.R.string.admin_no_permission_product_list_title).orEmpty()
                desc = context?.getString(com.tokopedia.shopadmin.common.R.string.admin_no_permission_contact_shop_owner).orEmpty()
            }
            AdminFeature.ORDER_HISTORY, AdminFeature.READY_TO_SHIP_ORDER, AdminFeature.NEW_ORDER -> {
                title = context?.getString(com.tokopedia.shopadmin.common.R.string.admin_no_permission_order_title).orEmpty()
                desc = context?.getString(com.tokopedia.shopadmin.common.R.string.admin_no_permission_contact_shop_owner).orEmpty()
            }
            else -> {
                title = context?.getString(com.tokopedia.shopadmin.common.R.string.admin_no_permission_oops).orEmpty()
                desc = context?.getString(com.tokopedia.shopadmin.common.R.string.admin_no_permission_contact_shop_owner).orEmpty()
            }
        }
        return Pair(title, desc)
    }

}
