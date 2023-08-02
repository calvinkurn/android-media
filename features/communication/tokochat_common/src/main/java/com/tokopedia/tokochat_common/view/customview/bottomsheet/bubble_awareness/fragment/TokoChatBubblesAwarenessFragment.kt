package com.tokopedia.tokochat_common.view.customview.bottomsheet.bubble_awareness.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.tokochat_common.databinding.TokochatBubblesAwarenessFragmentBinding

class TokoChatBubblesAwarenessFragment: Fragment() {

    private var binding: TokochatBubblesAwarenessFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewBinding = TokochatBubblesAwarenessFragmentBinding.inflate(inflater)
        binding = viewBinding
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        setupImage()
        setupDesc()
    }

    private fun setupImage() {
        arguments?.getString(IMAGE_URL_KEY)?.takeIf { it.isNotBlank() }?.let { imageUrl ->
            binding?.imgTokochatBubbleAwareness?.setImageUrl(imageUrl)
        }
    }

    private fun setupDesc() {
        arguments?.getString(DESC_KEY)?.takeIf { it.isNotBlank() }?.let { desc ->
            binding?.tvTokochatBubbleAwareness?.text = desc.parseAsHtml()
        }
    }

    companion object {
        private const val IMAGE_URL_KEY = "image_url"
        private const val DESC_KEY = "desc"

        @JvmStatic
        fun createInstance(imageUrl: String, desc: String): TokoChatBubblesAwarenessFragment {
            return TokoChatBubblesAwarenessFragment().apply {
                arguments = Bundle().apply {
                    putString(IMAGE_URL_KEY, imageUrl)
                    putString(DESC_KEY, desc)
                }
            }
        }
    }

}
