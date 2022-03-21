package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HomeNavItemSellerBinding
import com.tokopedia.homenav.mainnav.view.datamodel.account.ProfileSellerDataModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class SellerViewHolder(
    itemView: View
) : AbstractViewHolder<ProfileSellerDataModel>(itemView) {

    private val binding: HomeNavItemSellerBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_nav_item_seller
    }

    override fun bind(element: ProfileSellerDataModel) {
        binding?.run {
            if (element.isGetShopLoading) {
                usrShopInfo.gone()
                btnTryAgainShopInfo.gone()
                usrShopNotif.gone()
                imageArrowRight.gone()
                shimmerShopInfo.visible()
                shimmerBtnTryAgain.visible()
            } else if (element.isGetShopError) {
                btnTryAgainShopInfo.visible()
                usrShopInfo.visible()
                shimmerShopInfo.gone()
                shimmerBtnTryAgain.gone()
                usrShopNotif.gone()
                imageArrowRight.gone()
                shimmerBtnTryAgain.gone()
                usrShopInfo.text = getString(R.string.error_state_shop_info)
            } else if (!element.isGetShopError) {
                btnTryAgainShopInfo.gone()
                shimmerShopInfo.gone()
                btnTryAgainShopInfo.gone()
                usrShopNotif.visible()
                imageArrowRight.visible()
                shimmerBtnTryAgain.gone()

                val shopInfo: CharSequence
                if (!element.hasShop) {
                    shopInfo =
                        itemView.context?.getString(R.string.account_header_register_store)
                            .orEmpty()
                    usrShopInfo.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_N700
                        )
                    )
                    usrShopInfo.setWeight(Typography.REGULAR)
                } else {
                    shopInfo = MethodChecker.fromHtml(element.shopName)
                    usrShopInfo.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_NN950
                        )
                    )
                    usrShopInfo.setWeight(Typography.BOLD)
                }
                usrShopInfo.run {
                    visible()
                    text = shopInfo
                }
                containerShop.setOnClickListener {
//                    shopClicked(profileSeller, it.context)
                }
                if (element.shopOrderCount > 0) {
                    usrShopNotif.visible()
                    usrShopNotif.setNotification(
                        element.shopOrderCount.toString(),
                        NotificationUnify.COUNTER_TYPE,
                        NotificationUnify.COLOR_PRIMARY
                    )
                } else {
                    usrShopNotif.gone()
                }
            }
        }
    }
}