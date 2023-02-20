package com.tokopedia.topchat.chatsetting.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.topchat.chatsetting.view.adapter.BubbleActivationIntroAdapter
import com.tokopedia.topchat.chatsetting.view.uimodel.BubbleActivationIntroUiModel
import com.tokopedia.topchat.common.util.BubbleChat
import com.tokopedia.topchat.databinding.FragmentBubbleChatActivationIntroBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable

class BubbleChatActivationIntroFragment : Fragment() {

    private var binding by autoClearedNullable<FragmentBubbleChatActivationIntroBinding>()

    private var mListener: Listener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBubbleChatActivationIntroBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        mListener?.onPageLoaded()
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    private fun setupView() {
        setupImage()
        setupRecyclerView()
        setupButton()
    }

    private fun setupImage() {
        binding?.imgTopchatBubbleIntro?.setImageUrl(BubbleChat.Url.BUBBLE_ACTIVATION_INTRO_IMAGE)
    }

    private fun setupRecyclerView() {
        val introAdapter = BubbleActivationIntroAdapter()
        binding?.rvTopchatBubbleIntro?.run {
            layoutManager = LinearLayoutManager(context)
            adapter = introAdapter
        }
        introAdapter.setIntroItemData(getIntroAdapterData())
    }

    private fun setupButton() {
        binding?.btnTopchatBubbleIntro?.setOnClickListener {
            mListener?.onIntroButtonClicked()
        }
    }

    private fun getIntroAdapterData(): List<BubbleActivationIntroUiModel> {
        return listOf(
            BubbleActivationIntroUiModel(
                imageUrl = BubbleChat.Url.BUBBLE_ACTIVATION_INTRO_1,
                benefitText = context?.getString(com.tokopedia.topchat.R.string.topchat_bubble_activation_benefit_1)
                    .orEmpty()
            ),
            BubbleActivationIntroUiModel(
                imageUrl = BubbleChat.Url.BUBBLE_ACTIVATION_INTRO_2,
                benefitText = context?.getString(com.tokopedia.topchat.R.string.topchat_bubble_activation_benefit_2)
                    .orEmpty()
            )
        )
    }

    interface Listener {
        fun onIntroButtonClicked()
        fun onPageLoaded()
    }

    companion object {
        @JvmStatic
        fun createInstance(listener: Listener): BubbleChatActivationIntroFragment {
            return BubbleChatActivationIntroFragment().apply {
                setListener(listener)
            }
        }
    }

}
