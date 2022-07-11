package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.R
import com.tokopedia.home.analytics.v2.OvoWidgetTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_SUBSCRIPTION
import com.tokopedia.home.databinding.ItemBalanceWidgetNewBinding
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class BalanceViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    companion object {
        private const val TITLE_HEADER_WEBSITE = "Tokopedia"
    }

    private val binding: ItemBalanceWidgetNewBinding? by viewBinding()
    private var listener: HomeCategoryListener? = null

    fun bind(
        drawerItem: BalanceDrawerItemModel?,
        listener: HomeCategoryListener?
    ) {
        this.listener = listener
        binding?.homeTvBalance?.setWeight(Typography.BOLD)
        renderDrawerItem(drawerItem)
        this.itemView.tag = String.format(
            itemView.context.getString(R.string.tag_balance_widget),
            drawerItem?.drawerItemType.toString()
        )
    }

    private fun renderDrawerItem(element: BalanceDrawerItemModel?) {
        /**
         * Initial state
         */

        when (element?.state) {
            BalanceDrawerItemModel.STATE_LOADING -> {
                binding?.homeIvLogoBalance?.invisible()
                binding?.homeTvBalance?.invisible()

            }
            BalanceDrawerItemModel.STATE_SUCCESS -> {
                //load image
                binding?.homeIvLogoBalance?.show()
                element.defaultIconRes?.let {
                    if (element.drawerItemType == BalanceDrawerItemModel.TYPE_WALLET_PENDING_CASHBACK ||
                        element.drawerItemType == BalanceDrawerItemModel.TYPE_WALLET_WITH_TOPUP ||
                        element.drawerItemType == BalanceDrawerItemModel.TYPE_WALLET_OTHER
                    ) {
                        binding?.homeIvLogoBalance?.visible()
                        binding?.homeIvLogoBalance?.setImageDrawable(itemView.context.getDrawable(it))
                    } else {
                        binding?.homeIvLogoBalance?.invisible()
                    }
                }
                element.iconImageUrl?.let {
                    binding?.homeIvLogoBalance?.visible()
                    if (it.isNotEmpty()) binding?.homeIvLogoBalance?.setImageUrl(it)
                }

                //Load Text
                val balanceText = element.balanceTitleTextAttribute?.text ?: ""
                binding?.homeTvBalance?.text = balanceText
                binding?.homeHeaderTitleBalance?.text = element.headerTitle

                //load reserve balance
                val reserveBalance = element.balanceSubTitleTextAttribute?.text ?: ""
                binding?.homeTvReserveBalance?.text = reserveBalance
                if (element.drawerItemType == TYPE_SUBSCRIPTION && !element.isSubscriberGoToPlus) {
                    binding?.homeTvReserveBalance?.setWeight(Typography.BOLD)
                    binding?.homeTvReserveBalance?.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_GN400
                        )
                    )
                } else {
                    binding?.homeTvReserveBalance?.setWeight(Typography.REGULAR)
                    binding?.homeTvReserveBalance?.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_NN600
                        )
                    )
                }

                binding?.homeContainerBalance?.handleItemCLickType(
                    element = element,
                    tokopointsAction = {
                        //handle click for type tokopoints
                        listener?.actionTokoPointClicked(
                            element.applinkContainer,
                            element.redirectUrl,
                            if (element.mainPageTitle.isEmpty())
                                TITLE_HEADER_WEBSITE
                            else
                                element.mainPageTitle
                        )
                        OvoWidgetTracking.sendClickOnTokopointsBalanceWidgetTracker(
                            listener?.userId ?: ""
                        )

                    },
                    ovoWalletAction = {
                        //handle click for type ovo
                        if (RouteManager.isSupportApplink(
                                itemView.context,
                                element.applinkContainer
                            )
                        ) {
                            OvoWidgetTracking.sendClickOnOVOBalanceWidgetTracker(
                                listener?.userId ?: ""
                            )
                            val intentBalanceWallet =
                                RouteManager.getIntent(itemView.context, element.applinkContainer)
                            itemView.context.startActivity(intentBalanceWallet)
                        }
                    },
                    rewardsAction = {
                        //handle click for type rewards
                        listener?.actionTokoPointClicked(
                            element.applinkContainer,
                            element.redirectUrl,
                            if (element.mainPageTitle.isEmpty())
                                TITLE_HEADER_WEBSITE
                            else
                                element.mainPageTitle
                        )
                        OvoWidgetTracking.sendClickOnRewardsBalanceWidgetTracker(
                            listener?.userId ?: ""
                        )
                    },
                    couponsAction = {
                        //handle click for type coupon
                        listener?.actionTokoPointClicked(
                            element.applinkContainer,
                            element.redirectUrl,
                            if (element.mainPageTitle.isEmpty())
                                TITLE_HEADER_WEBSITE
                            else
                                element.mainPageTitle
                        )
                        OvoWidgetTracking.sendClickOnCouponBalanceWidgetTracker(
                            listener?.userId ?: ""
                        )
                    },
                    bboAction = {
                        //handle click for type bbo
                        listener?.actionTokoPointClicked(
                            element.applinkContainer,
                            element.redirectUrl,
                            if (element.mainPageTitle.isEmpty())
                                TITLE_HEADER_WEBSITE
                            else
                                element.mainPageTitle
                        )
                        OvoWidgetTracking.sendClickOnBBOBalanceWidgetTracker(
                            listener?.userId ?: "0"
                        )
                        //uncomment when we use new tracker
                        //OvoWidgetTracking.sendClickOnBBONewTokopointsWidget(isOvoAvailable, listener?.userId ?: "")
                    },
                    walletTopupAction = {
                        //handle click for type wallet topup
                        if (RouteManager.isSupportApplink(
                                itemView.context,
                                element.applinkContainer
                            )
                        ) {
                            OvoWidgetTracking.sendClickOnOVOBalanceWidgetTracker(
                                listener?.userId ?: ""
                            )
                            OvoWidgetTracking.eventTopUpOvo(listener?.userId)
                            val intentBalanceWallet =
                                RouteManager.getIntent(itemView.context, element.applinkContainer)
                            itemView.context.startActivity(intentBalanceWallet)
                        }
                    },
                    walletOtherAction = {
                        //handle click for type wallet other

                    },
                    walletPendingAction = {
                        //handle click for type wallet pending

                    },
                    walletAppAction = {
                        OvoWidgetTracking.sendClickOnNewWalletAppBalanceWidgetTracker(
                            subtitle = element.balanceSubTitleTextAttribute?.text ?: "",
                            userId = listener?.userId ?: ""
                        )
                        listener?.onSectionItemClicked(element.redirectUrl)
                    }
                )


            }
            BalanceDrawerItemModel.STATE_ERROR -> {
                binding?.homeIvLogoBalance?.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_N75
                    )
                )
                binding?.homeHeaderTitleBalance?.text = element.headerTitle
                binding?.homeTvBalance?.text =
                    itemView.context.getString(com.tokopedia.home.R.string.balance_widget_failed_to_load)
                binding?.homeTvReserveBalance?.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN400
                    )
                )
                binding?.homeTvReserveBalance?.setWeight(Typography.BOLD)
                binding?.homeTvReserveBalance?.text =
                    itemView.context.getString(com.tokopedia.home.R.string.text_reload)
                binding?.homeContainerBalance?.handleItemCLickType(
                    element = element,
                    rewardsAction = { listener?.onRetryMembership(adapterPosition, element.headerTitle) },
                    walletAppAction = { listener?.onRetryWalletApp() }
                )
            }
        }
    }

    private fun View.handleItemCLickType(
        element: BalanceDrawerItemModel,
        tokopointsAction: () -> Unit = {},
        ovoWalletAction: () -> Unit = {},
        rewardsAction: () -> Unit = {},
        couponsAction: () -> Unit = {},
        bboAction: () -> Unit = {},
        walletTopupAction: () -> Unit = {},
        walletOtherAction: () -> Unit = {},
        walletPendingAction: () -> Unit = {},
        walletAppAction: (isLinked: Boolean) -> Unit = {}
    ) {
        setOnClickListener {
            when (element.drawerItemType) {
                BalanceDrawerItemModel.TYPE_TOKOPOINT -> tokopointsAction.invoke()
                BalanceDrawerItemModel.TYPE_FREE_ONGKIR -> bboAction.invoke()
                BalanceDrawerItemModel.TYPE_COUPON -> couponsAction.invoke()
                BalanceDrawerItemModel.TYPE_REWARDS -> rewardsAction.invoke()
                BalanceDrawerItemModel.TYPE_WALLET_WITH_TOPUP -> walletTopupAction.invoke()
                BalanceDrawerItemModel.TYPE_WALLET_OTHER -> walletOtherAction.invoke()
                BalanceDrawerItemModel.TYPE_WALLET_PENDING_CASHBACK -> walletPendingAction.invoke()
                BalanceDrawerItemModel.TYPE_WALLET_APP_LINKED -> walletAppAction.invoke(true)
                BalanceDrawerItemModel.TYPE_WALLET_APP_NOT_LINKED -> walletAppAction.invoke(false)
            }
        }
    }
}