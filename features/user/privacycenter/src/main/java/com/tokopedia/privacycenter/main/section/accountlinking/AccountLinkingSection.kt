package com.tokopedia.privacycenter.main.section.accountlinking

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.common.utils.getMessage
import com.tokopedia.privacycenter.databinding.ItemAccountLinkingBinding
import com.tokopedia.privacycenter.main.analytics.MainPrivacyCenterAnalytics
import com.tokopedia.privacycenter.main.section.BasePrivacyCenterSection

class AccountLinkingSection(
    context: Context?,
    private val viewModel: AccountLinkingViewModel,
    private val listener: Listener
) : BasePrivacyCenterSection(context) {

    interface Listener {
        fun onItemAccountLinkingClicked()
    }

    override val sectionViewBinding: ItemAccountLinkingBinding = ItemAccountLinkingBinding.inflate(
        LayoutInflater.from(context)
    )
    override val sectionTextTitle: String = context?.getString(R.string.account_linking_title).orEmpty()
    override val sectionTextDescription: String = context?.getString(R.string.account_linking_subtitle).orEmpty()
    override val isShowDirectionButton: Boolean = false
    private var isLinked: Boolean = false

    override fun initObservers() {
        lifecycleOwner?.let {
            viewModel.accountLinkingState.observe(lifecycleOwner) {
                when (it) {
                    is PrivacyCenterStateResult.Loading -> {
                        showShimmering(true)
                    }
                    is PrivacyCenterStateResult.Success -> {
                        showShimmering(false)
                        isLinked = it.data.isLinked
                        showStatusAccountLinked(it.data.isLinked, it.data.phoneNumber, it.data.linkedTime)
                        sendStatusAccountTracker(it.data.isLinked)
                    }
                    is PrivacyCenterStateResult.Fail -> {
                        showOnFailed(it.error)
                    }
                }
            }
        }
    }

    override fun onViewRendered() {
        sectionViewBinding.root.setOnClickListener {
            listener.onItemAccountLinkingClicked()

            MainPrivacyCenterAnalytics.sendClickOnButtonAccountLinkingEvent(
                if (isLinked) {
                    MainPrivacyCenterAnalytics.LABEL_LINK
                } else {
                    MainPrivacyCenterAnalytics.LABEL_UNLINK
                }
            )
        }
    }

    private fun sendStatusAccountTracker(isLinked: Boolean) {
        MainPrivacyCenterAnalytics.sendViewAccountLinkingSectionEvent(
            if (isLinked) {
                MainPrivacyCenterAnalytics.LABEL_ACCOUNT_LINKED
            } else {
                MainPrivacyCenterAnalytics.LABEL_ACCOUNT_NOT_LINKED
            }
        )
    }

    private fun showStatusAccountLinked(isLinked: Boolean, phoneNumber: String = "", linkedTime: String = "") {
        sectionViewBinding.apply {
            textDetail.showWithCondition(!isLinked)
            icAccountLinked.showWithCondition(isLinked)

            descriptionAccountLinking.text = if (isLinked) {
                String.format(
                    sectionViewBinding.root.context?.getString(R.string.account_linking_label_account_linked).orEmpty(),
                    phoneNumber,
                    linkedTime
                )
            } else {
                sectionViewBinding.root.context?.getString(R.string.account_linking_label_account_unlinked)
            }
        }
    }

    private fun showOnFailed(throwable: Throwable?) {
        showLocalLoad(
            title = throwable?.getMessage(sectionViewBinding.root.context).orEmpty(),
            description = sectionViewBinding.root.context?.getString(R.string.account_linking_subtitle_failed).orEmpty()
        ) {
            viewModel.getAccountLinkingStatus()
        }
    }

    override fun onButtonDirectionClick(view: View) {
        // none
    }

    companion object {
        const val TAG = "AccountLinkingSection"
    }
}
