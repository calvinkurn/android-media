package com.tokopedia.seller.menu.common.view.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.constant.AdminFeature
import com.tokopedia.seller.menu.common.constant.SellerBaseUrl
import com.tokopedia.seller.menu.common.di.DaggerSellerMenuCommonComponent
import com.tokopedia.seller.menu.common.view.viewmodel.AdminRoleAuthorizeViewModel
import com.tokopedia.seller.menu.common.view.activity.AdminRoleAuthorizeActivity
import com.tokopedia.seller.menu.common.view.mapper.AdminPermissionMapper
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class AdminRoleAuthorizeFragment: BaseDaggerFragment() {

    companion object {
        private const val TOKOPEDIA_CARE_PATH = "help"

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
    lateinit var remoteConfig: RemoteConfig

    @Inject
    lateinit var adminPermissionMapper: AdminPermissionMapper

    private val viewModel: AdminRoleAuthorizeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(AdminRoleAuthorizeViewModel::class.java)
    }

    private val adminFeature: String by lazy {
        arguments?.getString(AdminRoleAuthorizeActivity.KEY_ADMIN_FEATURE).orEmpty()
    }

    private var adminErrorView: GlobalError? = null
    private var adminLoadingView: LoaderUnify? = null
    private var adminHelpText: Typography? = null

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
        DaggerSellerMenuCommonComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun View.setupAdminView() {
        adminErrorView = findViewById(R.id.error_admin_role)
        adminLoadingView = findViewById(R.id.loader_admin_role)
        adminHelpText = findViewById(R.id.tv_admin_role_help)

        viewModel.checkAccess(adminFeature)
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
                            adminErrorView?.visibility = View.GONE
                            goToDestination()
                        } else {
                            adminErrorView?.show(true)
                        }
                    }
                }
                is Fail -> {
                    setToolbarTitle(adminPermissionMapper.mapFeatureToToolbarTitle(context, adminFeature))
                    adminErrorView?.show(false)
                }
            }
        }
    }

    private fun observeIsLoading() {
        viewModel.isLoadingLiveData.observe(viewLifecycleOwner) { isLoading ->
            adminLoadingView?.showWithCondition(isLoading)
            adminErrorView?.showWithCondition(!isLoading)
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
        errorSecondaryAction.visibility = View.GONE
        setButtonFull(true)

        setActionClickListener {
            activity?.finish()
        }
    }

    private fun GlobalError.showAdminError() {
        setType(GlobalError.NO_CONNECTION)
        errorSecondaryAction.visibility = View.GONE
        setButtonFull(true)

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

    // TODO: Delete later if unused
    private fun Typography.showHelpMessage() {
        context?.getString(R.string.admin_no_permission_need_help)?.let { helpMessage ->
            context?.getString(R.string.admin_no_permission_contact_tocare)?.let { clickableMessage ->
                helpMessage.indexOf(clickableMessage).let { clickableIndex ->
                    SpannableString(helpMessage).apply {
                        setSpan(
                                object : ClickableSpan() {
                                    override fun onClick(widget: View) {
                                        RouteManager.route(activity, String.format("%s?url=%s", ApplinkConst.WEBVIEW,
                                                TokopediaUrl.getInstance().MOBILEWEB + TOKOPEDIA_CARE_PATH))
                                    }

                                    override fun updateDrawState(ds: TextPaint) {
                                        ds.color = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
                                        ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                                    }
                                },
                                clickableIndex,
                                clickableIndex + clickableMessage.length,
                                0)
                    }.let { spannableString ->
                        movementMethod = LinkMovementMethod.getInstance()
                        setText(spannableString, TextView.BufferType.SPANNABLE)
                        visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}