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
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
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

    fun bind(
        drawerItem: BalanceDrawerItemModel?,
        listener: HomeCategoryListener?
    ) {
        this.listener = listener
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
                renderItemSuccess(element)
            }
            BalanceDrawerItemModel.STATE_LOADING -> {
                renderItemLoading(element)
            }
            BalanceDrawerItemModel.STATE_ERROR -> {
                renderItemError(element)
            }
        }
    }

    private fun renderItemError(element: BalanceDrawerItemModel) {
        binding?.shimmerItemBalanceWidget?.root?.gone()
        binding?.homeContainerBalance?.homeContainerBalance?.show()
        showFailedImage()
        binding?.homeContainerBalance?.homeHeaderTitleBalance?.text = element.headerTitle
        binding?.homeContainerBalance?.homeTvBalance?.text =
            itemView.context.getString(com.tokopedia.home.R.string.balance_widget_failed_to_load)
        binding?.homeContainerBalance?.homeTvReserveBalance?.setTextColor(
            ContextCompat.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_GN500
            )
        )
        binding?.homeContainerBalance?.homeTvReserveBalance?.setWeight(Typography.BOLD)
        binding?.homeContainerBalance?.homeTvReserveBalance?.text =
            itemView.context.getString(com.tokopedia.home.R.string.text_reload)
        binding?.homeContainerBalance?.homeContainerBalance?.handleItemClickType(
            element = element,
            rewardsAction = { showLoading(element) },
            walletAppAction = { showLoading(element) }
        )
    }

    private fun showFailedImage() {
        binding?.homeContainerBalance?.homeIvLogoBalance?.setImageDrawable(
            ContextCompat.getDrawable(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN50
            )
        )
    }

    private fun renderItemLoading(element: BalanceDrawerItemModel) {
        binding?.shimmerItemBalanceWidget?.root?.show()
        binding?.homeContainerBalance?.homeContainerBalance?.invisible()
        if (element.drawerItemType == TYPE_WALLET_APP_LINKED) {
            listener?.onRetryWalletApp(adapterPosition, element.headerTitle)
        } else if (element.drawerItemType == TYPE_REWARDS) {
            listener?.onRetryMembership(adapterPosition, element.headerTitle)
        }
    }

    private fun renderItemSuccess(element: BalanceDrawerItemModel) {
        setPaddingCard()
        showImageSuccess(element)

        // Load Text
        val balanceText = element.balanceTitleTextAttribute?.text ?: ""
        binding?.homeContainerBalance?.homeTvBalance?.text = balanceText
        binding?.homeContainerBalance?.homeHeaderTitleBalance?.text = element.headerTitle

        // load reserve balance
        val reserveBalance = element.balanceSubTitleTextAttribute?.text ?: ""
        binding?.homeContainerBalance?.homeTvReserveBalance?.text = reserveBalance
        setFontReserveBalance(element)

        handleClickSuccess(element, reserveBalance)
        binding?.shimmerItemBalanceWidget?.root?.gone()
        binding?.homeContainerBalance?.homeContainerBalance?.show()
    }

    private fun setFontReserveBalance(element: BalanceDrawerItemModel) {
        when (element.drawerItemType) {
            TYPE_SUBSCRIPTION -> {
                val typographyWeight = if (element.balanceSubTitleTextAttribute?.isBold == true) Typography.BOLD else Typography.REGULAR
                binding?.homeContainerBalance?.homeTvReserveBalance?.setWeight(typographyWeight)

                binding?.homeContainerBalance?.homeTvReserveBalance?.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        element.balanceSubTitleTextAttribute?.colourRef
                            ?: com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                )
            }
            TYPE_REWARDS -> {
                val typographyWeight = if (element.balanceSubTitleTextAttribute?.isBold == true) Typography.BOLD else Typography.REGULAR
                binding?.homeContainerBalance?.homeTvReserveBalance?.setWeight(typographyWeight)

                binding?.homeContainerBalance?.homeTvReserveBalance?.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        element.balanceSubTitleTextAttribute?.colourRef
                            ?: com.tokopedia.unifyprinciples.R.color.Unify_NN600
                    )
                )
            }
            TYPE_WALLET_APP_NOT_LINKED -> {
                binding?.homeContainerBalance?.homeTvReserveBalance?.setWeight(Typography.BOLD)
                binding?.homeContainerBalance?.homeTvReserveBalance?.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                )
            }
            else -> {
                binding?.homeContainerBalance?.homeTvReserveBalance?.setWeight(Typography.REGULAR)
                binding?.homeContainerBalance?.homeTvReserveBalance?.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN600
                    )
                )
            }
        }
    }

    private fun showImageSuccess(element: BalanceDrawerItemModel) {
        if (!element.iconImageUrl.isNullOrBlank()) {
            binding?.homeContainerBalance?.homeIvLogoBalance?.setImageUrl(element.iconImageUrl)
        } else {
            showFailedImage()
        }
    }

    private fun setPaddingCard() {
        if (totalItems == BALANCE_WIDGET_2_ITEMS && adapterPosition == POSITION_2) {
            binding?.homeContainerBalance?.homeContainerBalance?.setPadding(
                paddingLeftPosition2Size2,
                paddingBalanceWidget,
                paddingBalanceWidget,
                paddingBalanceWidget
            )
        } else {
            binding?.homeContainerBalance?.homeContainerBalance?.setPadding(
                paddingBalanceWidget,
                paddingBalanceWidget,
                paddingBalanceWidget,
                paddingBalanceWidget
            )
        }
    }

    private fun showLoading(element: BalanceDrawerItemModel) {
        binding?.shimmerItemBalanceWidget?.root?.show()
        binding?.homeContainerBalance?.homeContainerBalance?.invisible()
        if (element.drawerItemType == TYPE_WALLET_APP_LINKED) {
            element.state = BalanceDrawerItemModel.STATE_LOADING
            listener?.onRetryWalletApp(adapterPosition, element.headerTitle)
        } else if (element.drawerItemType == TYPE_REWARDS) {
            element.state = BalanceDrawerItemModel.STATE_LOADING
            listener?.onRetryMembership(adapterPosition, element.headerTitle)
        }
    }

    private fun handleClickSuccess(element: BalanceDrawerItemModel, reserveBalance: String) {
        binding?.homeContainerBalance?.homeContainerBalance?.handleItemClickType(
            element = element,
            rewardsAction = {
                // handle click for type rewards
                BalanceWidgetTracking.sendClickOnRewardsBalanceWidgetTracker(
                    listener?.userId ?: ""
                )
                listener?.actionTokoPointClicked(
                    element.applinkContainer,
                    element.redirectUrl,
                    element.mainPageTitle.ifEmpty { TITLE_HEADER_WEBSITE }
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
