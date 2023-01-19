package com.tokopedia.privacycenter.ui.main.section.privacypolicy

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.common.network.util.NetworkClient
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.data.PrivacyPolicyDataModel
import com.tokopedia.privacycenter.databinding.BottomSheetPrivacyInfoBinding
import com.tokopedia.privacycenter.databinding.SectionPrivacyPolicyBinding
import com.tokopedia.privacycenter.ui.main.analytics.MainPrivacyCenterAnalytics
import com.tokopedia.privacycenter.ui.main.section.BasePrivacyCenterSection
import com.tokopedia.privacycenter.ui.main.section.privacypolicy.PrivacyPolicyConst.DEFAULT_TITLE
import com.tokopedia.privacycenter.ui.main.section.privacypolicy.PrivacyPolicyConst.KEY_TITLE
import com.tokopedia.privacycenter.ui.main.section.privacypolicy.PrivacyPolicyConst.SECTION_ID
import com.tokopedia.privacycenter.ui.main.section.privacypolicy.adapter.PrivacyPolicyAdapter
import com.tokopedia.privacycenter.ui.privacypolicywebview.PrivacyPolicyWebViewActivity
import com.tokopedia.unifycomponents.BottomSheetUnify

class PrivacyPolicySection constructor(
    private val context: Context?,
    private val fragmentManager: FragmentManager?,
    private val viewModel: PrivacyPolicySectionViewModel
) : BasePrivacyCenterSection(context), PrivacyPolicyAdapter.Listener {

    private val privacyPolicyAdapter = PrivacyPolicyAdapter(this)
    private var privacyPolicySectionBottomSheet: PrivacyPolicySectionBottomSheet? = null

    override val sectionViewBinding: SectionPrivacyPolicyBinding =
        SectionPrivacyPolicyBinding.inflate(
            LayoutInflater.from(context)
        )
    override val sectionTextTitle: String =
        context?.resources?.getString(R.string.privacy_policy_section_title).orEmpty()
    override val sectionTextDescription: String =
        context?.resources?.getString(R.string.privacy_policy_section_description).orEmpty()
    override val isFromBottomSheet: Boolean = false

    override fun onViewRendered() {
        context?.let { NetworkClient.init(it) }
        showShimmering(false)
        sectionViewBinding.menuCurrentPrivacyPolicy.setIcon(IconUnify.PROTECTION_CHECK)
        sectionViewBinding.apply {
            listPrivacyPolicy.apply {
                adapter = privacyPolicyAdapter
            }

            menuCurrentPrivacyPolicy.setOnClickListener {
                MainPrivacyCenterAnalytics.sendClickOnButtonBacaKebijakanPrivasiEvent()

                val intent = RouteManager.getIntent(
                    context,
                    ApplinkConstInternalGlobal.WEBVIEW_TITLE,
                    DEFAULT_TITLE,
                    PrivacyPolicyConst.CURRENT_PRIVACY_URL
                )

                context?.startActivity(intent)
            }

            menuListPrivacyPolicy.setOnClickListener {
                viewModel.toggleContentVisibility()
            }

            buttonShowAllList.setOnClickListener {
                MainPrivacyCenterAnalytics.sendClickOnButtonLihatSemuaEvent()

                if (privacyPolicySectionBottomSheet == null) {
                    privacyPolicySectionBottomSheet = PrivacyPolicySectionBottomSheet()
                }

                fragmentManager?.let {
                    privacyPolicySectionBottomSheet?.show(it, PrivacyPolicySectionBottomSheet.TAG)
                }
            }

            sectionViewBinding.informationIcon.setOnClickListener { showInformationBottomSheet() }
        }

        viewModel.getPrivacyPolicyTopFiveList()
    }

    private fun showInformationBottomSheet() {
        fragmentManager?.let {
            BottomSheetUnify().apply {
                showCloseIcon = true
                val view = BottomSheetPrivacyInfoBinding.inflate(LayoutInflater.from(this@PrivacyPolicySection.context), null, false)
                setTitle(this@PrivacyPolicySection.context?.resources?.getString(R.string.privacy_policy_info_title).orEmpty())
                setChild(view.root)
            }.show(it, "")
        }
    }

    override fun initObservers() {
        lifecycleOwner?.run {
            viewModel.state.observe(this) {
                toggleCollapsable(it.expanded)
                if (it.expanded) {
                    when (it.innerState) {
                        is PrivacyPolicyUiModel.InnerState.Success -> showList(it.innerState.list)
                        PrivacyPolicyUiModel.InnerState.Loading -> loadingPrivacyPolicyList()
                        PrivacyPolicyUiModel.InnerState.Error -> showLocalLoad()
                    }
                }
            }
        }
    }

    override fun onItemClicked(item: PrivacyPolicyDataModel) {
        val intent = Intent(context, PrivacyPolicyWebViewActivity::class.java).apply {
            putExtras(
                Bundle().apply {
                    putString(KEY_TITLE, item.sectionTitle)
                    putString(SECTION_ID, item.sectionId)
                }
            )
        }

        context?.startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showList(data: List<PrivacyPolicyDataModel>) {
        sectionViewBinding.run {
            localLoadPrivacyPolicy.hide()
            loaderListPrivacyPolicy.hide()
            listPrivacyPolicy.show()
            buttonShowAllList.show()
        }
        privacyPolicyAdapter.apply {
            clearAllItems()
            addItems(data)
            notifyDataSetChanged()
        }
    }

    private fun toggleCollapsable(shown: Boolean) {
        sectionViewBinding.iconMenu.setImage(
            if (shown) IconUnify.CHEVRON_UP else IconUnify.CHEVRON_DOWN
        )
        sectionViewBinding.privacyPolicyContainer.showWithCondition(shown)
    }

    private fun loadingPrivacyPolicyList() {
        sectionViewBinding.run {
            loaderListPrivacyPolicy.show()
            listPrivacyPolicy.hide()
            localLoadPrivacyPolicy.hide()
            buttonShowAllList.hide()
        }
    }

    private fun showLocalLoad() {
        sectionViewBinding.apply {
            loaderListPrivacyPolicy.hide()
            listPrivacyPolicy.hide()
            buttonShowAllList.hide()
            localLoadPrivacyPolicy.apply {
                localLoadTitle = context.getString(R.string.privacy_center_error_network_title)
                refreshBtn?.setOnClickListener {
                    viewModel.getPrivacyPolicyTopFiveList()
                }
            }.show()
        }
    }

    companion object {
        const val TAG = "PrivacyPolicySection"
    }
}
