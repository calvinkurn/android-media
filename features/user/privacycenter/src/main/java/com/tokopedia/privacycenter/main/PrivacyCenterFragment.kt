package com.tokopedia.privacycenter.main

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.accountlinking.LinkAccountWebviewFragment
import com.tokopedia.privacycenter.common.di.PrivacyCenterComponent
import com.tokopedia.privacycenter.common.getDynamicColorStatusBar
import com.tokopedia.privacycenter.common.getIconBackWithColor
import com.tokopedia.privacycenter.common.getIdColor
import com.tokopedia.privacycenter.common.setFitToWindows
import com.tokopedia.privacycenter.common.setTextStatusBar
import com.tokopedia.privacycenter.databinding.FragmentPrivacyCenterBinding
import com.tokopedia.privacycenter.main.section.accountlinking.AccountLinkingSection
import com.tokopedia.privacycenter.main.section.accountlinking.AccountLinkingViewModel
import com.tokopedia.privacycenter.main.section.consentwithdrawal.ConsentWithdrawalSection
import com.tokopedia.privacycenter.main.section.consentwithdrawal.ConsentWithdrawalSectionViewModel
import com.tokopedia.privacycenter.main.section.dummy.DummySection
import com.tokopedia.privacycenter.main.section.recommendation.RecommendationSection
import com.tokopedia.privacycenter.main.section.recommendation.RecommendationViewModel
import com.tokopedia.unifycomponents.isUsingNightModeResources
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class PrivacyCenterFragment : BaseDaggerFragment(),
    AppBarLayout.OnOffsetChangedListener,
    AccountLinkingSection.Listener,
    RecommendationSection.Listener
{

    private var binding by autoClearedNullable<FragmentPrivacyCenterBinding>()
    private var privacyCenterSection: PrivacyCenterSection? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            PrivacyCenterViewModel::class.java
        )
    }

    private val viewModelAccountLinkingSection by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            AccountLinkingViewModel::class.java
        )
    }

    private val viewModelConsentWithdrawalSection by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            ConsentWithdrawalSectionViewModel::class.java
        )
    }

    private val viewModelRecommendationSection by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            RecommendationViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPrivacyCenterBinding.inflate(inflater, container, false)
        privacyCenterSection = PrivacyCenterSection(binding?.rootContent, PrivacyCenterSectionDelegateImpl())
        privacyCenterSection?.renderSections()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
    }

    override fun onStart() {
        super.onStart()
        binding?.appbar?.addOnOffsetChangedListener(this)

        //refresh toggle section recommendation and promo
        viewModelRecommendationSection.refreshGeolocationPermission()
    }

    private fun initToolbar() {
        requireActivity().apply {
            setFitToWindows(false)
            window.statusBarColor = Color.TRANSPARENT
            binding?.unifyToolbar?.apply {
                title = resources.getString(R.string.title_privacy_center)
                setBackgroundColor(Color.TRANSPARENT)
                setNavigationOnClickListener {
                    onBackPressed()
                }
            }
        }
        binding?.textName?.text = viewModel.getUserName()
    }

    override fun onStop() {
        super.onStop()
        binding?.appbar?.removeOnOffsetChangedListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        privacyCenterSection?.removeAllViews()
        privacyCenterSection = null
    }

    override fun getScreenName(): String {
        return PrivacyCenterFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(PrivacyCenterComponent::class.java).inject(this)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        setUpCollapseToolbar(verticalOffset < OFFSET_CHANGE_COLOR_STATUS_BAR)
    }

    private fun setUpCollapseToolbar(isCollapsed: Boolean) {
        val isExpand = if (isCollapsed) {
            isUsingNightModeResources()
        } else {
            true
        }

        requireActivity().apply {
            val textColor = getIdColor(getWhite = isExpand)
            val backIcon = getIconBackWithColor(getWhite = isExpand)

            window.statusBarColor = if (isExpand) {
                Color.TRANSPARENT
            } else {
                getDynamicColorStatusBar()
            }
            setTextStatusBar(setToWhite = isExpand)

            binding?.unifyToolbar?.apply {
                headerView?.setTextColor(textColor)
                navigationIcon = backIcon
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_ACCOUNT_WEBVIEW_REQUEST -> {
                viewModelAccountLinkingSection.getAccountLinkingStatus()
            }
            REQUEST_LOCATION_PERMISSION -> {
                val isAllowed = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED

                //change toggle geolocation
                viewModelRecommendationSection.setGeolocationChange(isAllowed)
            }
        }
    }

    override fun onRequestLocationPermission() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            REQUEST_LOCATION_PERMISSION
        )
    }

    override fun onItemAccountLinkingClicked() {
        goToAccountLinkingWebview()
    }

    private fun goToAccountLinkingWebview() {
        val intent = RouteManager.getIntent(
            activity,
            ApplinkConstInternalUserPlatform.ACCOUNT_LINKING_WEBVIEW).apply {
            putExtra(
                ApplinkConstInternalGlobal.PARAM_LD,
                LinkAccountWebviewFragment.BACK_BTN_APPLINK
            )
        }
        startActivityForResult(intent, REQUEST_ACCOUNT_WEBVIEW_REQUEST)
    }

    inner class PrivacyCenterSectionDelegateImpl: PrivacyCenterSectionDelegate {
        override val recommendationSection: RecommendationSection = RecommendationSection(
            context,
            viewModelRecommendationSection,
            this@PrivacyCenterFragment
        )
        override val accountLinkingSection: AccountLinkingSection =
            AccountLinkingSection(context, viewModelAccountLinkingSection, this@PrivacyCenterFragment)
        override val dummySection: DummySection = DummySection(context)
        override val consentWithdrawalSection: ConsentWithdrawalSection = ConsentWithdrawalSection(
            context,
            viewModelConsentWithdrawalSection
        )
    }

    companion object {
        fun newInstance() = PrivacyCenterFragment()
        private const val REQUEST_LOCATION_PERMISSION = 100
        private const val OFFSET_CHANGE_COLOR_STATUS_BAR = -136
        private const val REQUEST_ACCOUNT_WEBVIEW_REQUEST = 101
    }
}
