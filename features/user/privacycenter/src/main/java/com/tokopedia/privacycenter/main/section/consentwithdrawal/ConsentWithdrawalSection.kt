package com.tokopedia.privacycenter.main.section.consentwithdrawal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.consentwithdrawal.data.ConsentGroupDataModel
import com.tokopedia.privacycenter.consentwithdrawal.data.ConsentGroupListDataModel
import com.tokopedia.privacycenter.databinding.SectionConsentwithdrawalBinding
import com.tokopedia.privacycenter.main.section.BasePrivacyCenterSection
import com.tokopedia.privacycenter.main.section.consentwithdrawal.adapter.ConsentWithdrawalSectionAdapter

class ConsentWithdrawalSection constructor(
    private val context: Context?,
    private val viewModel: ConsentWithdrawalSectionViewModel
) : BasePrivacyCenterSection(context), ConsentWithdrawalSectionAdapter.Listener {

    private val consentWithdrawalSectionAdapter = ConsentWithdrawalSectionAdapter(this)

    override val sectionViewBinding = SectionConsentwithdrawalBinding.inflate(
        LayoutInflater.from(context)
    )

    override val sectionTextTitle: String? = context?.resources?.getString(
        R.string.consent_withdrawal_section_title
    )

    override val sectionTextDescription: String? = context?.resources?.getString(
        R.string.consent_withdrawal_section_description
    )

    override val isShowDirectionButton: Boolean = false

    override fun onButtonDirectionClick(view: View) {
        // no op
    }

    override fun onViewRendered() {
        sectionViewBinding.consentGroupList.apply {
            adapter = consentWithdrawalSectionAdapter
        }

        viewModel.getConsentGroupList()
    }

    override fun initObservers() {
        lifecycleOwner?.run {
            viewModel.getConsentGroupList.observe(this) {
                when(it) {
                    is PrivacyCenterStateResult.Loading -> {
                        showShimmering(true)
                    }
                    is PrivacyCenterStateResult.Success -> {
                        hideLocalLoad()
                        showShimmering(false)
                        onSuccessGetConsentGroup(it.data)
                    }
                    is PrivacyCenterStateResult.Fail -> {
                        showLocalLoad {
                            viewModel.getConsentGroupList()
                        }
                    }
                }
            }
        }
    }

    private fun onSuccessGetConsentGroup(data: ConsentGroupListDataModel) {
        showShimmering(false)
        consentWithdrawalSectionAdapter.clearAllItems()
        data.groups.forEach {
            consentWithdrawalSectionAdapter.addItem(it)
        }

        consentWithdrawalSectionAdapter.notifyItemRangeInserted(0, data.groups.size)
    }

    override fun onItemClicked(data: ConsentGroupDataModel) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.CONSENT_WITHDRAWAL_NEW, data.id)
        context?.startActivity(intent)
    }

    companion object {
        const val TAG = "ConsentWithdrawalSection"
    }
}
