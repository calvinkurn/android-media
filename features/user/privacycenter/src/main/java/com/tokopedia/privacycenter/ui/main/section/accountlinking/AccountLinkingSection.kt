package com.tokopedia.privacycenter.ui.main.section.accountlinking

import android.content.Context
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.databinding.ItemAccountLinkingBinding
import com.tokopedia.privacycenter.ui.main.analytics.MainPrivacyCenterAnalytics
import com.tokopedia.privacycenter.ui.main.section.BasePrivacyCenterSection
import com.tokopedia.privacycenter.utils.getMessage
@Deprecated("Remove this class after integrating SCP Login to Tokopedia")
class AccountLinkingSection(
    context: Context?,
    private val viewModel: AccountLinkingViewModel,
    private val listener: Listener
) : BasePrivacyCenterSection(context) {
    @Deprecated("Remove this class after integrating SCP Login to Tokopedia")
    interface Listener {
        @Deprecated("Remove this class after integrating SCP Login to Tokopedia")
        fun onItemAccountLinkingClicked(isLinked: Boolean)
    }

    override val sectionViewBinding: ItemAccountLinkingBinding = ItemAccountLinkingBinding.inflate(
        LayoutInflater.from(context)
    )
    override val sectionTextTitle: String = context?.getString(R.string.account_linking_title).orEmpty()
    override val sectionTextDescription: String = context?.getString(R.string.account_linking_subtitle).orEmpty()
    private var isLinked: Boolean = false

    override fun initObservers() {
        lifecycleOwner?.let { lifecycleOwner ->
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
            listener.onItemAccountLinkingClicked(isLinked)

            MainPrivacyCenterAnalytics.sendClickOnButtonAccountLinkingEvent(
                if (isLinked) {
                    MainPrivacyCenterAnalytics.LABEL_UNLINK
                } else {
                    MainPrivacyCenterAnalytics.LABEL_LINK
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

    companion object {
        const val TAG = "AccountLinkingSection"
    }
}
