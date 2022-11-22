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
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
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

    private var isPrivacyPolicyListOpened: Boolean = false
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
                isPrivacyPolicyListOpened = !isPrivacyPolicyListOpened
                iconMenu.setImage(
                    if (isPrivacyPolicyListOpened)
                        IconUnify.CHEVRON_UP
                    else
                        IconUnify.CHEVRON_DOWN
                )
                listPrivacyPolicy.showWithCondition(isPrivacyPolicyListOpened)
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
            viewModel.privacyPolicyTopFiveList.observe(this) {
                when (it) {
                    is PrivacyCenterStateResult.Fail -> showLocalLoad()
                    is PrivacyCenterStateResult.Loading -> loadingPrivacyPolicyList(true)
                    is PrivacyCenterStateResult.Success -> onSuccessGetPrivacyPolicyTopFiveList(it.data)
                }
            }
        }
    }

    override fun onItemClicked(item: PrivacyPolicyDataModel) {
        openDetailPrivacyPolicy(item.sectionTitle, item.sectionId)
    }

    private fun onSuccessGetPrivacyPolicyTopFiveList(data: List<PrivacyPolicyDataModel>) {
        loadingPrivacyPolicyList(false)

        privacyPolicyAdapter.apply {
            clearAllItems()
            addItems(data)
            notifyDataSetChanged()
        }
    }

    private fun openDetailPrivacyPolicy(title: String, sectionId: String) {
        val intent = Intent(context, PrivacyPolicyWebViewActivity::class.java).apply {
            putExtras(Bundle().apply {
                putString(KEY_TITLE, title)
                putString(SECTION_ID, sectionId)
            })
        }

        context?.startActivity(intent)
    }

    private fun loadingPrivacyPolicyList(isLoading: Boolean) {
        if (isPrivacyPolicyListOpened) {
            sectionViewBinding.apply {
                loaderListPrivacyPolicy.showWithCondition(isLoading)
                listPrivacyPolicy.showWithCondition(!isLoading)
            }
        }
    }

    private fun showLocalLoad() {
        sectionViewBinding.apply {
            loaderListPrivacyPolicy.hide()
            listPrivacyPolicy.hide()
            localLoadPrivacyPolicy.apply {
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
