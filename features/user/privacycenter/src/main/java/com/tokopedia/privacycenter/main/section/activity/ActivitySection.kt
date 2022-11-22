package com.tokopedia.privacycenter.main.section.activity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.databinding.SectionActivityBinding
import com.tokopedia.privacycenter.main.section.BasePrivacyCenterSection

class ActivitySection(private val context: Context?) : BasePrivacyCenterSection(context) {
    override val sectionViewBinding: SectionActivityBinding = SectionActivityBinding.inflate(
        LayoutInflater.from(context)
    )
    override val sectionTextTitle: String =
        context?.getString(R.string.privacy_center_activity_title).orEmpty()
    override val sectionTextDescription: String =
        context?.getString(R.string.privacy_center_activity_subtitle).orEmpty()
    override val isShowDirectionButton: Boolean = false

    override fun initObservers() {
        // none
    }

    override fun onViewRendered() {
        showShimmering(false)
        setUpView()
        initListener()
    }

    override fun onButtonDirectionClick(view: View) {
        // none
    }

    private fun setUpView() {
        sectionViewBinding.itemShareWishlist.itemViewPrivacyCenter.apply {
            setIcon(IconUnify.HEART)
            title = context?.getString(R.string.privacy_center_activity_share_wishlist_title).orEmpty()
        }

        sectionViewBinding.itemSearchHistory.itemViewPrivacyCenter.apply {
            setIcon(IconUnify.SEARCH)
            title = context?.getString(R.string.privacy_center_activity_search_history_title).orEmpty()
        }
    }

    private fun initListener() {
        sectionViewBinding.itemShareWishlist.root.setOnClickListener {
            goToShareWishlist()
        }

        sectionViewBinding.itemSearchHistory.root.setOnClickListener {
            goToHistorySearch()
        }
    }

    private fun goToShareWishlist() {
        RouteManager.route(
            context,
            ApplinkConstInternalUserPlatform.SHARING_WISHLIST
        )
    }

    private fun goToHistorySearch() {
        RouteManager.route(
            context,
            ApplinkConstInternalUserPlatform.SEARCH_HISTORY
        )
    }

    companion object {
        const val TAG = "ActivitySection"
    }
}
