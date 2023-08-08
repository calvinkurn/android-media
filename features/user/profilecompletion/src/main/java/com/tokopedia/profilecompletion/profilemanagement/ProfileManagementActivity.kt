package com.tokopedia.profilecompletion.profilemanagement

import android.os.Bundle
import android.view.Menu
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.databinding.ActivityProfileManagementBinding
import com.tokopedia.profilecompletion.di.ActivityComponentFactory
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.domain.GetUrlProfileManagementResult
import com.tokopedia.utils.view.binding.internal.findRootView
import com.tokopedia.webview.BaseSessionWebViewFragment
import com.tokopedia.webview.BaseSimpleWebViewActivity
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ProfileManagementActivity: BaseSimpleWebViewActivity(), HasComponent<ProfileCompletionSettingComponent> {

    @Inject
    lateinit var viewModel: ProfileManagementViewModel

    private var binding : ActivityProfileManagementBinding? = null

    override fun getParentViewResourceID(): Int {
        return binding?.parentView?.id.orZero()
    }

    override fun setupFragment(savedInstance: Bundle?) {
        if (viewModel.url.isNotEmpty()) {
            super.setupFragment(savedInstance)
        }
    }

    override fun getNewFragment(): Fragment {
        return BaseSessionWebViewFragment.newInstance(
            viewModel.url
        )
    }

    override fun getComponent(): ProfileCompletionSettingComponent {
        return ActivityComponentFactory.instance.createProfileCompletionComponent(
            this,
            application as BaseMainApplication
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        supportGotoTheme()

        initObserver()
        initListener()
        viewModel.getProfileManagementData()
    }

    private fun supportGotoTheme() {
        window.statusBarColor = getResColor(R.color.dms_profile_management_toolbar)
        val staticNavigationIcon = getIconUnifyDrawable(
            this,
            IconUnify.ARROW_BACK,
            ContextCompat.getColor(
                this,
                R.color.dms_profile_management_black_static
            )
        )
        binding?.toolbar?.navigationIcon = staticNavigationIcon
        binding?.toolbar?.headerView?.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.dms_profile_management_black_static
            )
        )
        WindowInsetsControllerCompat(window, findRootView(this)).isAppearanceLightStatusBars = true

        binding?.globalError?.errorTitle?.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.dms_profile_management_black_static
            )
        )

        binding?.globalError?.errorDescription?.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.dms_profile_management_black_static
            )
        )
    }

    private fun initObserver() {
        viewModel.getUrlProfileManagement.observe(this) {
            when(it) {
                is GetUrlProfileManagementResult.Loading -> {
                    setupLayout(it)
                }
                is GetUrlProfileManagementResult.Success -> {
                    setupLayout(it)
                    setupFragment(savedInstance = null)
                }
                is GetUrlProfileManagementResult.Failed -> {
                    setupErrorLayout(it.throwable)
                    setupLayout(it)
                }
            }
        }
    }

    private fun initListener() {
        binding?.globalError?.setActionClickListener {
            if (binding?.globalError?.getErrorType() == GlobalError.PAGE_NOT_FOUND) {
                finish()
            } else {
                viewModel.getProfileManagementData()
            }
        }
    }

    private fun setupLayout(state: GetUrlProfileManagementResult) {
        binding?.apply {
            globalError.showWithCondition(state is GetUrlProfileManagementResult.Failed)
            loader.showWithCondition(state is GetUrlProfileManagementResult.Loading)
            tvLoader.showWithCondition(state is GetUrlProfileManagementResult.Loading)
            parentView.showWithCondition(state is GetUrlProfileManagementResult.Success)
        }
    }

    private fun setupErrorLayout(throwable: Throwable) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                binding?.globalError?.setType(GlobalError.NO_CONNECTION)
            }
            is RuntimeException -> {
                when (throwable.localizedMessage?.toIntOrNull()) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> {
                        binding?.globalError?.setType(GlobalError.NO_CONNECTION)
                    }
                    ReponseStatus.NOT_FOUND -> {
                        binding?.globalError?.setType(GlobalError.PAGE_NOT_FOUND)
                    }
                    ReponseStatus.INTERNAL_SERVER_ERROR -> {
                        binding?.globalError?.setType(GlobalError.SERVER_ERROR)
                    }
                    else -> {
                        sendLogAndShowError(throwable)
                    }
                }
            }
            else -> {
                sendLogAndShowError(throwable)
            }
        }
    }

    private fun sendLogAndShowError(throwable: Throwable) {
        binding?.globalError?.setType(GlobalError.SERVER_ERROR)

        val errorHandler = ErrorHandler.getErrorMessagePair(
            context = this,
            e = throwable,
            builder = ErrorHandler.Builder().build()
        )

        val description = "${binding?.globalError?.errorDescription?.text} (${errorHandler.second})"
        binding?.globalError?.errorDescription?.text = description
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return false
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        binding = ActivityProfileManagementBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        toolbar = binding?.toolbar
        setUpActionBar(toolbar)
    }

}
