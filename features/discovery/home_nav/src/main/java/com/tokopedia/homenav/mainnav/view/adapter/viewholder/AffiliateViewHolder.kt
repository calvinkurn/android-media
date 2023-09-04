package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HomeNavItemAffiliateBinding
import com.tokopedia.homenav.mainnav.view.analytics.TrackingProfileSection
import com.tokopedia.homenav.mainnav.view.datamodel.account.ProfileAffiliateDataModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class AffiliateViewHolder (
    itemView: View,
    private val mainNavListener: MainNavListener
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

    private fun affiliateClicked(profileAffiliate: ProfileAffiliateDataModel, context: Context) {
        if (profileAffiliate.isRegister) {
            mainNavListener.onProfileSectionClicked(TrackingProfileSection.AFFILIATE_ACCOUNT, profileAffiliate.affiliateAppLink)
        }
        else {
            mainNavListener.onProfileSectionClicked(TrackingProfileSection.CREATE_AFFILIATE, profileAffiliate.affiliateAppLink)
        }
    }

    override fun bind(element: ProfileAffiliateDataModel) {
        binding?.run {
            btnTryAgainAffiliateInfo.setOnClickListener{mainNavListener.onErrorAffiliateInfoRefreshClicked(adapterPosition)}
            if (element.isGetAffiliateLoading) {
                imageAffiliate.gone()
                usrAffiliateInfo.gone()
                btnTryAgainAffiliateInfo.gone()
                imageArrowRight.gone()
                shimmerShopInfo.visible()
                shimmerBtnTryAgain.visible()
            } else if (element.isGetAffiliateError) {
                setShopInfoBold()
                imageAffiliate.gone()
                btnTryAgainAffiliateInfo.visible()
                usrAffiliateInfo.visible()
                shimmerShopInfo.gone()
                shimmerBtnTryAgain.gone()
                imageArrowRight.gone()
                shimmerBtnTryAgain.gone()
                usrAffiliateInfo.text = getString(R.string.error_state_shop_info)
            } else if (!element.isGetAffiliateError) {
                imageAffiliate.visible()
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
                    setShopInfoBold()
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
