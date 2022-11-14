package com.tokopedia.privacycenter.main.section.consentwithdrawal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.consentwithdrawal.data.ConsentGroupDataModel
import com.tokopedia.privacycenter.consentwithdrawal.data.ConsentGroupListDataModel
import com.tokopedia.privacycenter.databinding.SectionConsentwithdrawalBinding
import com.tokopedia.privacycenter.main.section.BasePrivacyCenterSection

class ConsentWithdrawalSection constructor(
    context: Context?,
//    private val viewModel: ConsentWithdrawalViewModel
) : BasePrivacyCenterSection(context), ConsentWithdrawalSectionAdapter.Listener {

    private val consentWithdrawalSectionAdapter = ConsentWithdrawalSectionAdapter(this)

    override val sectionViewBinding = SectionConsentwithdrawalBinding.inflate(
        LayoutInflater.from(context)
    )

    override val sectionTextTitle: String? = context?.resources?.getString(
        R.string.section_title_consentWithdrawal
    )

    override val sectionTextDescription: String? = context?.resources?.getString(
        R.string.section_description_consentWithdrawal
    )

    override val isShowDirectionButton: Boolean = false

    override fun onButtonDirectionClick(view: View) {
        // no op
    }

    override fun onViewRendered() {
        sectionViewBinding.consentGroupList.apply {
            adapter = consentWithdrawalSectionAdapter
        }

//        viewModel.getConsentGroupList()

        onSuccessGetConsentGroup(ConsentGroupListDataModel(groups = listOf(
            ConsentGroupDataModel(
                groupTitle = "Consent Group 1",
                groupSubtitle = "Description",
                groupImage = "https://images.tokopedia.net/img/android/user/profile_page/Group3082@3x.png"
            ),
            ConsentGroupDataModel(
                groupTitle = "Consent Group 2",
                groupSubtitle = "Description",
                groupImage = "https://images.tokopedia.net/img/android/user/profile_page/Group3082@3x.png"
            ),
            ConsentGroupDataModel(
                groupTitle = "Consent Group 3",
                groupSubtitle = "Description",
                groupImage = "https://images.tokopedia.net/img/android/user/profile_page/Group3082@3x.png"
            ),
        )))
    }

    override fun initObservers() {
        lifecycleOwner?.run {
//            viewModel.getConsentGroupList.observe(this) {
//                when(it) {
//                    is PrivacyCenterStateResult.Loading -> {
//                        showShimmering(true)
//                    }
//                    is PrivacyCenterStateResult.Success -> {
//                        showShimmering(false)
//                        onSuccessGetConsentGroup(it.data)
//                    }
//                    is PrivacyCenterStateResult.Fail -> {
//                        showLocalLoad("Terjadi kesalahan", it.error.message.toString()) {
//                            viewModel.getConsentGroupList()
//                        }
//                    }
//                }
//            }
        }
    }

    private fun onSuccessGetConsentGroup(data: ConsentGroupListDataModel) {
        showShimmering(false)
        data.groups.forEach {
            consentWithdrawalSectionAdapter.addItem(it)
        }

        consentWithdrawalSectionAdapter.notifyItemRangeInserted(0, data.groups.size)
    }

    override fun onItemClicked(data: ConsentGroupDataModel) {

    }

    companion object {
        const val TAG = "ConsentWithdrawalSection"
    }
}
