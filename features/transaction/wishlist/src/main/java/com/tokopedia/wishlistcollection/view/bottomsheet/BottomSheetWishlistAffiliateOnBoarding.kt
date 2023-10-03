package com.tokopedia.wishlistcollection.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toDp
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.databinding.BottomsheetWishlistAffiliateOnboardingBinding

class BottomSheetWishlistAffiliateOnBoarding :
    BottomSheetUnify() {
    private var binding by autoClearedNullable<BottomsheetWishlistAffiliateOnboardingBinding>()

    private var isRegistered: Boolean = false

    init {
        showCloseIcon = true
        showHeader = true
        isDragable = true
        customPeekHeight = (getScreenHeight() * CUSTOM_HEIGHT_FACTOR).toDp().toInt()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initLayout() {
        binding = BottomsheetWishlistAffiliateOnboardingBinding.inflate(
            LayoutInflater.from(context),
            null,
            false
        )
        setTitle(getString(R.string.affiliate_onboarding_bottomsheet_title))
        setChild(binding?.root)

        if (isRegistered) {
            binding?.secondBullet?.hide()
            binding?.secondBulletContent?.hide()
            binding?.thirdBullet?.text = BULLET_TWO
            binding?.thirdBulletContent?.text =
                getString(R.string.affiliate_onboarding_second_login)
            binding?.textFooter?.text =
                getString(R.string.affiliate_onboarding_login_footer)
            binding?.buttonOnboarding?.text =
                getString(R.string.affiliate_onboarding_login_action)
        } else {
            binding?.secondBullet?.show()
            binding?.secondBulletContent?.show()
            binding?.thirdBullet?.text = BULLET_THREE
            binding?.thirdBulletContent?.text =
                getString(R.string.affiliate_onboarding_third)
            binding?.textFooter?.text =
                getString(R.string.affiliate_onboarding_register_footer)
            binding?.buttonOnboarding?.text =
                getString(R.string.affiliate_onboarding_register_action)
        }
        binding?.buttonOnboarding?.setOnClickListener {
            RouteManager.route(
                context,
                "${ApplinkConst.AFFILIATE_TOKO_ONBOARDING}?$SOURCE_KEY=$SOURCE_WISHLIST"
            )
        }
    }

    companion object {
        private const val BULLET_TWO = "2"
        private const val BULLET_THREE = "3"
        private const val SOURCE_KEY = "source"
        private const val SOURCE_WISHLIST = "wishlist"
        private const val CUSTOM_HEIGHT_FACTOR = 0.9f

        fun getFragmentInstance(isRegistered: Boolean): BottomSheetWishlistAffiliateOnBoarding {
            return BottomSheetWishlistAffiliateOnBoarding().apply {
                this.isRegistered = isRegistered
            }
        }
    }
}
