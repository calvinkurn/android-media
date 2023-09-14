package com.tokopedia.tokochat.common.view.chatroom.customview.bottomsheet.bubbleawareness

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.setClickableUrlHtml
import com.tokopedia.tokochat_common.databinding.TokochatBubblesAwarenessTickerFragmentBinding
import java.util.*

class TokoChatBubblesAwarenessTickerFragment: Fragment() {

    private var binding: TokochatBubblesAwarenessTickerFragmentBinding? = null

    private var onEduClickListener: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewBinding = TokochatBubblesAwarenessTickerFragmentBinding.inflate(inflater)
        binding = viewBinding
        return viewBinding.root
    }

    fun setEduClickListener(onClick: (() -> Unit)) {
        onEduClickListener = onClick
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        setupImage()
        setupDescriptions()
    }

    private fun setupImage() {
        arguments?.getString(IMAGE_URL_KEY)?.takeIf { it.isNotBlank() }?.let { imageUrl ->
            binding?.imgTickerTokochatBubbleAwareness?.setImageUrl(imageUrl)
        }
    }

    private fun setupDescriptions() {
        setupFirstDescription()
        setupSecondDescription()
    }

    private fun setupFirstDescription() {
        arguments?.getString(FIRST_DESC_KEY)?.takeIf { it.isNotBlank() }?.let { firstDesc ->
            binding?.tvTickerTokochatBubbleAwareness?.text = firstDesc.parseAsHtml()
        }
    }

    private fun setupSecondDescription() {
        arguments?.getString(SECOND_DESC_KEY)?.takeIf { it.isNotBlank() }?.let { secondDesc ->
            context?.let { ctx ->
                binding?.tvTickerTokochatBubbleAwareness2?.setClickableUrlHtml(
                    htmlText = secondDesc,
                    applyCustomStyling = {
                        isUnderlineText = false
                        typeface = Typeface.DEFAULT_BOLD
                        color = MethodChecker.getColor(ctx, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
                    },
                    onUrlClicked = { _,_ ->
                        val bubbleChatHelpPageUrl = String.format(
                            Locale.getDefault(),
                            "%s?url=%s",
                            ApplinkConst.WEBVIEW,
                            BUBBLE_CHAT_HELP_PAGE_URL,
                        )
                        RouteManager.route(context, bubbleChatHelpPageUrl)
                        onEduClickListener?.invoke()
                    }
                )
            }
        }
    }

    companion object {
        private const val IMAGE_URL_KEY = "image_url"
        private const val FIRST_DESC_KEY = "first_desc"
        private const val SECOND_DESC_KEY = "second_desc"

        private const val BUBBLE_CHAT_HELP_PAGE_URL = "https://www.tokopedia.com/help/article/cara-pakai-bubble-chat"

        @JvmStatic
        fun createInstance(imageUrl: String,
                           firstDesc: String,
                           secondDesc: String): TokoChatBubblesAwarenessTickerFragment {
            return TokoChatBubblesAwarenessTickerFragment().apply {
                arguments = Bundle().apply {
                    putString(IMAGE_URL_KEY, imageUrl)
                    putString(FIRST_DESC_KEY, firstDesc)
                    putString(SECOND_DESC_KEY, secondDesc)
                }
            }
        }
    }

}
