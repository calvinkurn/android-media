package com.tokopedia.privacycenter.main.section.privacypolicy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.common.network.util.NetworkClient
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.databinding.SectionPrivacyPolicyBinding
import com.tokopedia.privacycenter.main.section.BasePrivacyCenterSection
import com.tokopedia.privacycenter.main.section.privacypolicy.PrivacyPolicyConst.DEFAULT_TITLE
import com.tokopedia.privacycenter.main.section.privacypolicy.PrivacyPolicyConst.KEY_TITLE
import com.tokopedia.privacycenter.main.section.privacypolicy.PrivacyPolicyConst.SECTION_ID
import com.tokopedia.privacycenter.main.section.privacypolicy.adapter.PrivacyPolicyAdapter
import com.tokopedia.privacycenter.main.section.privacypolicy.domain.data.PrivacyPolicyDataModel
import com.tokopedia.privacycenter.main.section.privacypolicy.webview.PrivacyPolicyWebViewActivity

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
    override val sectionTextTitle: String? = context?.resources?.getString(R.string.privacy_policy_section_title)
    override val sectionTextDescription: String? = context?.resources?.getString(R.string.privacy_policy_section_description)
    override val isShowDirectionButton: Boolean = false
    override val isFromBottomSheet: Boolean = false

    override fun onButtonDirectionClick(view: View) {
        // no op
    }

    override fun onViewRendered() {
        context?.let { NetworkClient.init(it) }
        showShimmering(false)

        sectionViewBinding.apply {
            listPrivacyPolicy.apply {
                adapter = privacyPolicyAdapter
            }

            menuCurrentPrivacyPolicy.setOnClickListener {
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
                if (privacyPolicySectionBottomSheet == null) {
                    privacyPolicySectionBottomSheet = PrivacyPolicySectionBottomSheet()
                }

                fragmentManager?.let {
                    privacyPolicySectionBottomSheet?.show(it, PrivacyPolicySectionBottomSheet.TAG)
                }
            }
        }

        viewModel.getPrivacyPolicyTopFiveList()
    }

    override fun initObservers() {
        lifecycleOwner?.run {
            viewModel.state.observe(this) {
                toggleCollapsable(it.shown)
                if (it.shown) {
                    when (it.innerState) {
                        is PrivacyPolicyUiModel.InnerState.Error ->  showLocalLoad()
                        PrivacyPolicyUiModel.InnerState.Loading -> loadingPrivacyPolicyList()
                        is PrivacyPolicyUiModel.InnerState.Success -> showList(it.innerState.list)
                    }
                }
            }
        }
    }

    override fun onItemClicked(item: PrivacyPolicyDataModel) {
        val intent = Intent(context, PrivacyPolicyWebViewActivity::class.java).apply {
            putExtras(Bundle().apply {
                putString(KEY_TITLE, item.sectionTitle)
                putString(SECTION_ID, item.sectionId)
            })
        }

        context?.startActivity(intent)
    }

    private fun showList(data: List<PrivacyPolicyDataModel>) {
        sectionViewBinding.run {
            localLoadPrivacyPolicy.hide()
            loaderListPrivacyPolicy.hide()
            listPrivacyPolicy.show()
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
        }
    }

    private fun showLocalLoad() {
        sectionViewBinding.apply {
            loaderListPrivacyPolicy.hide()
            listPrivacyPolicy.hide()
            localLoadPrivacyPolicy.apply {
                show()
                localLoadTitle = context.getString(R.string.privacy_center_error_network_title)
                refreshBtn?.setOnClickListener {
                    this.hide()
                    viewModel.getPrivacyPolicyTopFiveList()
                }
            }
        }
    }

    companion object {
        const val TAG = "PrivacyPolicy"
    }
}
