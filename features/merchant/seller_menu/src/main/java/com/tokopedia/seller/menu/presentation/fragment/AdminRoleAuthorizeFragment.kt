package com.tokopedia.seller.menu.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.seller.menu.R
import com.tokopedia.seller.menu.common.constant.AdminFeature
import com.tokopedia.seller.menu.common.constant.SellerBaseUrl
import com.tokopedia.seller.menu.common.errorhandler.SellerMenuErrorHandler
import com.tokopedia.seller.menu.di.component.DaggerSellerMenuComponent
import com.tokopedia.seller.menu.presentation.viewmodel.AdminRoleAuthorizeViewModel
import com.tokopedia.seller.menu.presentation.util.AdminPermissionMapper
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class AdminRoleAuthorizeFragment: BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun createInstance(adminFeature: String): AdminRoleAuthorizeFragment = AdminRoleAuthorizeFragment().apply {
            Bundle().apply {
                putString(com.tokopedia.seller.menu.presentation.activity.AdminRoleAuthorizeActivity.KEY_ADMIN_FEATURE, adminFeature)
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
        arguments?.getString(com.tokopedia.seller.menu.presentation.activity.AdminRoleAuthorizeActivity.KEY_ADMIN_FEATURE).orEmpty()
    }

    private var adminErrorView: GlobalError? = null
    private var adminLoadingView: LoaderUnify? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        observeLiveData()
        return inflater.inflate(R.layout.fragment_admin_role_authorize, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setupAdminView()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerSellerMenuComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun View.setupAdminView() {
        adminErrorView = findViewById(R.id.error_admin_role)
        adminLoadingView = findViewById(R.id.loader_admin_role)

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
                    SellerMenuErrorHandler.logExceptionToCrashlytics(result.throwable, SellerMenuErrorHandler.ERROR_GET_ADMIN_ACCESS_ROLE)
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
        errorAction.text = context?.getString(R.string.admin_no_permission_back_to_seller_account)

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
                title = context?.getString(R.string.admin_no_permission_product_add_title).orEmpty()
                desc = context?.getString(R.string.admin_no_permission_product_add_desc).orEmpty()
            }
            AdminFeature.MANAGE_PRODUCT -> {
                title = context?.getString(R.string.admin_no_permission_product_list_title).orEmpty()
                desc = context?.getString(R.string.admin_no_permission_contact_shop_owner).orEmpty()
            }
            AdminFeature.ORDER_HISTORY, AdminFeature.READY_TO_SHIP_ORDER, AdminFeature.NEW_ORDER -> {
                title = context?.getString(R.string.admin_no_permission_order_title).orEmpty()
                desc = context?.getString(R.string.admin_no_permission_contact_shop_owner).orEmpty()
            }
            else -> {
                title = context?.getString(R.string.admin_no_permission_oops).orEmpty()
                desc = context?.getString(R.string.admin_no_permission_contact_shop_owner).orEmpty()
            }
        }
        return Pair(title, desc)
    }

}