package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.R
import com.tokopedia.home.analytics.v2.BalanceWidgetTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_REWARDS
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_SUBSCRIPTION
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_APP_LINKED
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_APP_NOT_LINKED
import com.tokopedia.home.databinding.ItemBalanceWidgetNewBinding
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class BalanceViewHolder(v: View, private val totalItems: Int) : RecyclerView.ViewHolder(v) {
    companion object {
        private const val TITLE_HEADER_WEBSITE = "Tokopedia"
        private const val BALANCE_WIDGET_2_ITEMS = 2
        private const val POSITION_2 = 1
        private val paddingLeftPosition2Size2 = 12f.toDpInt()
        private val paddingBalanceWidget = 8f.toDpInt()
    }

    private val binding: ItemBalanceWidgetNewBinding? by viewBinding()
    private var listener: HomeCategoryListener? = null
    var subscriptionViewCoachMark: View? = null

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
        binding?.cardContainerBalance?.setCardUnifyBackgroundColor(
            ContextCompat.getColor(
                itemView.context,
                com.tokopedia.home.R.color.home_dms_color_transparent
            )
        )
        when (element?.state) {
            BalanceDrawerItemModel.STATE_SUCCESS -> {
                //load image
                if (totalItems == BALANCE_WIDGET_2_ITEMS && adapterPosition == POSITION_2) {
                    binding?.homeContainerBalance?.setPadding(
                        paddingLeftPosition2Size2,
                        paddingBalanceWidget,
                        paddingBalanceWidget,
                        paddingBalanceWidget
                    )
                } else  {
                    binding?.homeContainerBalance?.setPadding(
                        paddingBalanceWidget,
                        paddingBalanceWidget,
                        paddingBalanceWidget,
                        paddingBalanceWidget
                    )
                }
                element.defaultIconRes?.let {
                    binding?.homeIvLogoBalance?.setImageDrawable(itemView.context.getDrawable(it))
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
                if ((element.drawerItemType == TYPE_SUBSCRIPTION && !element.isSubscriberGoToPlus) || element.drawerItemType == TYPE_WALLET_APP_NOT_LINKED) {
                    binding?.homeTvReserveBalance?.setWeight(Typography.BOLD)
                    binding?.homeTvReserveBalance?.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_GN500
                        )
                    )
                    subscriptionViewCoachMark = binding?.homeTvReserveBalance
                } else {
                    binding?.homeTvReserveBalance?.setWeight(Typography.REGULAR)
                    binding?.homeTvReserveBalance?.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_NN600
                        )
                    )
                    if (element.drawerItemType == TYPE_SUBSCRIPTION) {
                        subscriptionViewCoachMark = binding?.homeTvReserveBalance
                    }
                }
                handleClickSuccess(element, reserveBalance)
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
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                )
                binding?.homeTvReserveBalance?.setWeight(Typography.BOLD)
                binding?.homeTvReserveBalance?.text =
                    itemView.context.getString(com.tokopedia.home.R.string.text_reload)
                binding?.homeContainerBalance?.handleItemClickType(
                    element = element,
                    rewardsAction = { listener?.onRetryMembership(adapterPosition, element.headerTitle) },
                    walletAppAction = { listener?.onRetryWalletApp(adapterPosition, element.headerTitle) }
                )
            }
        }
    }

    private fun handleClickSuccess(element: BalanceDrawerItemModel, reserveBalance: String) {
        binding?.homeContainerBalance?.handleItemClickType(
            element = element,
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
                BalanceWidgetTracking.sendClickOnRewardsBalanceWidgetTracker(
                    listener?.userId ?: ""
                )
            },
            walletAppAction = { isLinked ->
                if (isLinked) {
                    BalanceWidgetTracking.sendClickGopayLinkedWidgetTracker(
                        balancePoints = reserveBalance,
                        userId = listener?.userId ?: ""
                    )
                } else {
                    BalanceWidgetTracking.sendClickGopayNotLinkedWidgetTracker(
                        userId = listener?.userId ?: ""
                    )
                }
                listener?.onSectionItemClicked(element.redirectUrl)
            },
            subscriptionAction = { isSubscriber ->
                BalanceWidgetTracking.sendClickOnGoToPlusSectionSubscriptionStatusEvent(
                    isSubscriber = isSubscriber,
                    userId = listener?.userId ?: ""
                )
                listener?.onSectionItemClicked(element.redirectUrl)
            }
        )
    }

    private fun View.handleItemClickType(
        element: BalanceDrawerItemModel,
        rewardsAction: () -> Unit = {},
        walletAppAction: (isLinked: Boolean) -> Unit = {},
        subscriptionAction: (isSubscriber: Boolean) -> Unit = {}
    ) {
        setOnClickListener {
            when (element.drawerItemType) {
                TYPE_REWARDS -> rewardsAction.invoke()
                TYPE_WALLET_APP_LINKED -> walletAppAction.invoke(true)
                TYPE_WALLET_APP_NOT_LINKED -> walletAppAction.invoke(false)
                TYPE_SUBSCRIPTION -> subscriptionAction.invoke(element.isSubscriberGoToPlus)
            }
        }
    }
}