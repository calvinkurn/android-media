package com.tokopedia.tokochat_common.view.customview.bottomsheet.bubble_awareness

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.tokochat_common.databinding.TokochatBubblesAwarenessBottomsheetBinding
import com.tokopedia.tokochat_common.view.customview.bottomsheet.bubble_awareness.adapter.TokoChatBubblesAwarenessAdapter
import com.tokopedia.tokochat_common.view.customview.bottomsheet.bubble_awareness.fragment.TokoChatBubblesAwarenessFragment
import com.tokopedia.tokochat_common.view.customview.bottomsheet.bubble_awareness.fragment.TokoChatBubblesAwarenessTickerFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class TokoChatBubblesAwarenessBottomSheet: BottomSheetUnify() {

    private var binding by autoClearedNullable<TokochatBubblesAwarenessBottomsheetBinding>()

    private var fragmentAdapter: TokoChatBubblesAwarenessAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            com.tokopedia.tokochat_common.R.layout.tokochat_bubbles_awareness_bottomsheet,
            container,
            false
        )
        binding = TokochatBubblesAwarenessBottomsheetBinding.bind(view)
        clearContentPadding = true
        setChild(view)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    fun show(fm: FragmentManager) {
        if (!isVisible) {
            show(fm, TAG)
        }
    }

    private fun setupViews() {
        setBottomSheetTitle()
        setupAwarenessViewPager()
    }

    private fun setBottomSheetTitle() {
        setTitle(getString(com.tokopedia.tokochat_common.R.string.tokochat_bubbles_awareness_bottomsheet_title).orEmpty())
    }

    private fun setupAwarenessViewPager() {
        val awarenessFragments = getFragmentList()
        val fragmentSize = awarenessFragments.size
        fragmentAdapter =
            TokoChatBubblesAwarenessAdapter(childFragmentManager, lifecycle, awarenessFragments)
        fragmentAdapter?.let {
            binding?.vpTokochatBubbleAwareness?.adapter = it
            binding?.pageControlTokochatBubbleAwareness?.setIndicator(fragmentSize)
            binding?.vpTokochatBubbleAwareness?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setPageControl(position)
                    setButtonText(position, fragmentSize - Int.ONE)
                }
            })
        }
    }

    private fun setPageControl(position: Int) {
        binding?.pageControlTokochatBubbleAwareness?.setCurrentIndicator(position)
    }

    private fun setButtonText(position: Int, maxPosition: Int) {
        if (position >= maxPosition) {
            binding?.btnTokochatBubbleAwareness?.run {
                text = getString(com.tokopedia.tokochat_common.R.string.tokochat_bubbles_awareness_activate_setting_button)
                setOnClickListener {
                    goToSettingsPage()
                }
            }
        } else {
            binding?.btnTokochatBubbleAwareness?.run {
                text = getString(com.tokopedia.tokochat_common.R.string.tokochat_bubbles_awareness_next_button)
                setOnClickListener {
                    binding?.vpTokochatBubbleAwareness?.currentItem = position + Int.ONE
                }
            }
        }
    }

    private fun getFragmentList(): List<Fragment> {
        return listOf(
            TokoChatBubblesAwarenessFragment.createInstance(
                imageUrl = TokopediaImageUrl.IMG_TOKOCHAT_BUBBLES_AWARENESS_BIG,
                desc = getString(com.tokopedia.tokochat_common.R.string.tokochat_bubbles_awareness_bottomsheet_desc_1)
            ),
            TokoChatBubblesAwarenessTickerFragment.createInstance(
                imageUrl = TokopediaImageUrl.IMG_TOKOCHAT_BUBBLES_AWARENESS_1,
                firstDesc = getString(com.tokopedia.tokochat_common.R.string.tokochat_bubbles_awareness_bottomsheet_desc_2),
                secondDesc = String.EMPTY
            ),
            TokoChatBubblesAwarenessTickerFragment.createInstance(
                imageUrl = TokopediaImageUrl.IMG_TOKOCHAT_BUBBLES_AWARENESS_2,
                firstDesc = getString(com.tokopedia.tokochat_common.R.string.tokochat_bubbles_awareness_bottomsheet_desc_3),
                secondDesc = getString(com.tokopedia.tokochat_common.R.string.tokochat_bubbles_awareness_bottomsheet_desc_info_3)
            )
        )
    }

    private fun goToSettingsPage() {
        context?.let {
            try {
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_BUBBLE_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, it.packageName)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
            }
        }
    }

    companion object {

        private const val TAG = "TokoChatBubblesAwarenessBottomSheet"

        @JvmStatic
        fun createInstance(): TokoChatBubblesAwarenessBottomSheet {
            return TokoChatBubblesAwarenessBottomSheet()
        }
    }

}
