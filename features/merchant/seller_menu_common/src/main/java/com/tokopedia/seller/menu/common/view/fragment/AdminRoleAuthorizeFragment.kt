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
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.constant.SellerBaseUrl
import com.tokopedia.seller.menu.common.di.DaggerSellerMenuCommonComponent
import com.tokopedia.seller.menu.common.view.viewmodel.AdminRoleAuthorizeViewModel
import com.tokopedia.seller.menu.common.view.activity.AdminRoleAuthorizeActivity
import com.tokopedia.seller.menu.common.view.mapper.AdminPermissionMapper
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.url.TokopediaUrl
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
        observeAdminAuthorize()
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

    private fun observeAdminAuthorize() {
        viewModel.isRoleAuthorizedLiveData.observe(viewLifecycleOwner) { result ->
            adminLoadingView?.visibility = View.GONE
            if ((result as? Success)?.data == true) {
                adminErrorView?.visibility = View.GONE
                adminHelpText?.visibility = View.GONE
                goToDestination()
            } else {
                adminErrorView?.showAdminError()
                adminHelpText?.showHelpMessage()
            }
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

    private fun GlobalError.showAdminError() {
        ImageHandler.loadImageAndCache(errorIllustration, SellerBaseUrl.ADMIN_ERROR_ILLUSTRATION)
        errorTitle.text = context?.getString(R.string.admin_no_permission_oops)
        errorDescription.text = context?.getString(R.string.admin_no_permission_contact_shop_owner)
        errorAction.text = context?.getString(R.string.admin_no_permission_general_next)
        errorSecondaryAction.visibility = View.GONE

        setActionClickListener {
            activity?.finish()
        }
        visibility = View.VISIBLE
    }

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