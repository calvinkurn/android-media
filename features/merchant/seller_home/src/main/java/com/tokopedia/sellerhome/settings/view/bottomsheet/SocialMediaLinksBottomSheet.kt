package com.tokopedia.sellerhome.settings.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.databinding.BottomSheetSahSocialMediaBinding
import com.tokopedia.sellerhome.settings.analytics.SocialMediaLinksTracker
import com.tokopedia.sellerhome.settings.view.adapter.socialmedialinks.SocialMediaLinksAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class SocialMediaLinksBottomSheet: BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(): SocialMediaLinksBottomSheet {
            return SocialMediaLinksBottomSheet().apply {
                clearContentPadding = true
                setShowListener {
                    SocialMediaLinksTracker.sendBottomSheetImpressionEvent()
                }
            }
        }

        private const val TAG = "social_media_links"
    }

    private var binding by autoClearedNullable<BottomSheetSahSocialMediaBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetSahSocialMediaBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    fun show(fm: FragmentManager?) {
        fm?.let { show(it, TAG) }
    }

    private fun setupView() {
        context?.let {
            setTitle(it.getString(R.string.sah_social_title))
            binding?.rvSahSocial?.run {
                layoutManager = LinearLayoutManager(it)
                adapter = SocialMediaLinksAdapter(it)
            }
        }
    }

}