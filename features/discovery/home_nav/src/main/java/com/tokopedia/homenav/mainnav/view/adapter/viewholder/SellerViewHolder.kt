package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HomeNavItemSellerBinding
import com.tokopedia.homenav.mainnav.view.analytics.TrackingProfileSection
import com.tokopedia.homenav.mainnav.view.datamodel.account.ProfileSellerDataModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sessioncommon.view.admin.dialog.LocationAdminDialog
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class SellerViewHolder(
    itemView: View,
    private val mainNavListener: MainNavListener
) : AbstractViewHolder<ProfileSellerDataModel>(itemView) {

    private val binding: HomeNavItemSellerBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_nav_item_seller
    }

    private fun shopClicked(profileSeller: ProfileSellerDataModel, context: Context) {
        if (profileSeller.isLocationAdmin &&
            profileSeller.adminStatus == ProfileSellerDataModel.ADMIN_ACTIVE
        ) {
            LocationAdminDialog(itemView.context).show()
            return
        }

        if (profileSeller.hasShop) {
            TrackingProfileSection.onClickShopAndAffiliate(TrackingProfileSection.CLICK_SHOP_ACCOUNT)
            RouteManager.route(itemView.context, ApplinkConstInternalSellerapp.SELLER_MENU)
        } else {
            TrackingProfileSection.onClickShopAndAffiliate(TrackingProfileSection.CLICK_OPEN_SHOP)
            RouteManager.route(context, ApplinkConst.CREATE_SHOP)
        }
    }

    private fun setShopInfoBold() {
        binding?.usrShopInfo?.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_NN950
                        )
                    )
        binding?.usrShopInfo?.setWeight(Typography.BOLD)
    }

    override fun bind(element: ProfileSellerDataModel) {
        binding?.run {
            btnTryAgainShopInfo.setOnClickListener{mainNavListener.onErrorShopInfoRefreshClicked(adapterPosition)}
            if (element.isGetShopLoading) {
                usrShopInfo.gone()
                btnTryAgainShopInfo.gone()
                usrShopNotif.gone()
                imageArrowRight.gone()
                shimmerShopInfo.visible()
                shimmerBtnTryAgain.visible()
            } else if (element.isGetShopError) {
                setShopInfoBold()
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
                    setShopInfoBold()
                } else {
                    shopInfo = MethodChecker.fromHtml(element.shopName)
                    setShopInfoBold()
                }
                usrShopInfo.run {
                    visible()
                    text = shopInfo
                }
                containerShop.setOnClickListener {
                    shopClicked(element, it.context)
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
