package com.tokopedia.topchat.chatsetting.view.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatsetting.view.adapter.BubbleActivationGuideAdapter
import com.tokopedia.topchat.chatsetting.view.uimodel.BubbleActivationGuideUiModel
import com.tokopedia.topchat.common.util.BubbleChat
import com.tokopedia.topchat.common.util.Utils.isBubbleChatEnabled
import com.tokopedia.topchat.common.util.Utils.setTextMakeHyperlink
import com.tokopedia.topchat.databinding.FragmentBubbleChatActivationGuideBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable
import timber.log.Timber
import java.util.*

class BubbleChatActivationGuideFragment: Fragment() {

    private var binding by autoClearedNullable<FragmentBubbleChatActivationGuideBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBubbleChatActivationGuideBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBubbleChatSettings()
        setBubbleActivationAdapter()
        setBubbleChatActivationSellerEdu()
    }

    private fun setBubbleActivationAdapter() {
        val bubbleActivationGuideAdapter = BubbleActivationGuideAdapter()
        binding?.topchatRvActivationGuide?.run {
            layoutManager = LinearLayoutManager(context)
            adapter = bubbleActivationGuideAdapter
            hasFixedSize()
        }
        bubbleActivationGuideAdapter.setDetailPerformanceData(getBubbleActivationGuideList())
    }

    private fun setupBubbleChatSettings() {
        binding?.topchatBtnActivationGuideSellerEdu?.setOnClickListener {
            bubbleSettingsIntent()
        }
    }

    private fun setBubbleChatActivationSellerEdu() {
        binding?.topchatTvActivationGuideSellerEdu?.run {
            setTextMakeHyperlink(
                getString(R.string.topchat_bubble_activation_guide_seller_edu_label)
            ) {
                val bubbleChatHelpPageUrl = String.format(
                    Locale.getDefault(),
                    "%s?url=%s",
                    ApplinkConst.WEBVIEW,
                    BubbleChat.Url.BUBBLE_CHAT_HELP_PAGE_URL,
                )
                RouteManager.route(context, bubbleChatHelpPageUrl)
            }
        }
    }

    private fun bubbleSettingsIntent() {
        context?.let {
            if (isBubbleChatEnabled()) {
                try {
                    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_BUBBLE_SETTINGS)
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, it.packageName)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
    }

    private fun getBubbleActivationGuideList(): List<BubbleActivationGuideUiModel> {
        return listOf(
            BubbleActivationGuideUiModel(
                imageUrl = BubbleChat.Url.BUBBLE_ACTIVATION_GUIDE_1,
                desc = getString(com.tokopedia.topchat.R.string.topchat_bubble_activation_guide_1_desc),
                detailInformationUrl = BubbleChat.Url.BUBBLE_CHAT_ENTRY_POINT,
                descDetailInformation = getString(com.tokopedia.topchat.R.string.topchat_bubble_activation_guide_1_desc_sample_icon)
            ),
            BubbleActivationGuideUiModel(
                imageUrl = BubbleChat.Url.BUBBLE_ACTIVATION_GUIDE_2,
                desc = getString(com.tokopedia.topchat.R.string.topchat_bubble_activation_guide_2_desc),
                detailInformationUrl = String.EMPTY,
                descDetailInformation = String.EMPTY
            ),
            BubbleActivationGuideUiModel(
                imageUrl = BubbleChat.Url.BUBBLE_ACTIVATION_GUIDE_3,
                desc = getString(com.tokopedia.topchat.R.string.topchat_bubble_activation_guide_3_desc),
                detailInformationUrl = String.EMPTY,
                descDetailInformation = String.EMPTY
            )
        )
    }
}
