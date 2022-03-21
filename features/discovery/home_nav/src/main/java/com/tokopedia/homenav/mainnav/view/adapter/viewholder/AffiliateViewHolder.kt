package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HomeNavItemAffiliateBinding
import com.tokopedia.homenav.mainnav.view.analytics.TrackingProfileSection
import com.tokopedia.homenav.mainnav.view.datamodel.account.ProfileAffiliateDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.account.ProfileSellerDataModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class AffiliateViewHolder (
    itemView: View
) : AbstractViewHolder<ProfileAffiliateDataModel>(itemView){

   private val binding: HomeNavItemAffiliateBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_nav_item_affiliate
    }

    private fun setShopInfoBold() {
        binding?.usrAffiliateInfo?.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_NN950
                        )
                    )
        binding?.usrAffiliateInfo?.setWeight(Typography.BOLD)
    }

    private fun setShopInfoRegular() {
        binding?.usrAffiliateInfo?.setTextColor(
            ContextCompat.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_N700
            )
        )
        binding?.usrAffiliateInfo?.setWeight(Typography.REGULAR)
    }

    private fun affiliateClicked(profileAffiliate: ProfileAffiliateDataModel, context: Context) {
        if (profileAffiliate.isRegister)
//            onShopClicked(profileSeller.canGoToSellerAccount)
        else {
            RouteManager.route(context, profileAffiliate.affiliateAppLink)
//            TrackingProfileSection.onClickOpenShopSection(mainNavListener.getUserId())
        }
    }

    override fun bind(element: ProfileAffiliateDataModel) {
        binding?.run {
            if (element.isGetAffiliateLoading) {
                usrAffiliateInfo.gone()
                btnTryAgainAffiliateInfo.gone()
                imageArrowRight.gone()
                shimmerShopInfo.visible()
                shimmerBtnTryAgain.visible()
            } else if (element.isGetAffiliateError) {
                setShopInfoBold()
                btnTryAgainAffiliateInfo.visible()
                usrAffiliateInfo.visible()
                shimmerShopInfo.gone()
                shimmerBtnTryAgain.gone()
                imageArrowRight.gone()
                shimmerBtnTryAgain.gone()
                usrAffiliateInfo.text = getString(R.string.error_state_shop_info)
            } else if (!element.isGetAffiliateError) {
                btnTryAgainAffiliateInfo.gone()
                shimmerShopInfo.gone()
                btnTryAgainAffiliateInfo.gone()
                imageArrowRight.visible()
                shimmerBtnTryAgain.gone()

                val affiliateInfo: CharSequence
                if (!element.isRegister) {
                    imageAffiliate.gone()
                    affiliateInfo =
                        itemView.context?.getString(R.string.account_header_register_affiliate)
                            .orEmpty()
                    setShopInfoRegular()
                } else {
                    imageAffiliate.visible()
                    affiliateInfo = MethodChecker.fromHtml(element.affiliateName)
                    setShopInfoBold()
                }
                usrAffiliateInfo.run {
                    visible()
                    text = affiliateInfo
                }
                containerAffiliate.setOnClickListener {
                    affiliateClicked(element, it.context)
                }
            }
        }
    }
}