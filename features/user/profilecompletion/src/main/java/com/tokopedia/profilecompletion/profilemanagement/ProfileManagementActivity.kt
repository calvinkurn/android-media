package com.tokopedia.profilecompletion.profilemanagement

import android.os.Bundle
import android.view.Menu
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.databinding.ActivityProfileManagementBinding
import com.tokopedia.profilecompletion.di.ActivityComponentFactory
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.domain.GetUrlProfileManagementResult
import com.tokopedia.utils.view.binding.internal.findRootView
import com.tokopedia.webview.BaseSessionWebViewFragment
import com.tokopedia.webview.BaseSimpleWebViewActivity
import javax.inject.Inject

class ProfileManagementActivity: BaseSimpleWebViewActivity(), HasComponent<ProfileCompletionSettingComponent> {

    @Inject
    lateinit var viewModel: ProfileManagementViewModel

    private var binding : ActivityProfileManagementBinding? = null

    override fun getLayoutRes(): Int {
        return R.layout.activity_profile_management
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
        viewModel.getAuth.observe(this) {
            when(it) {
                is GetUrlProfileManagementResult.Loading -> {
                    setUpLayout(it)
                }
                is GetUrlProfileManagementResult.Success -> {
                    setUpLayout(it)
                    setupFragment(savedInstance = null)
                }
                is GetUrlProfileManagementResult.Failed -> {
                    setUpLayout(it)
                }
            }
        }
    }

    private fun initListener() {
        binding?.globalError?.setActionClickListener {
            viewModel.getProfileManagementData()
        }
    }

    private fun setUpLayout(state: GetUrlProfileManagementResult) {
        binding?.apply {
            globalError.showWithCondition(state is GetUrlProfileManagementResult.Failed)
            loader.showWithCondition(state is GetUrlProfileManagementResult.Loading)
            parentView.showWithCondition(state is GetUrlProfileManagementResult.Success)
        }
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
