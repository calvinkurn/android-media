package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.atf2

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.home.R
import com.tokopedia.home.analytics.v2.BalanceWidgetTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_REWARDS
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_SUBSCRIPTION
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_APP_LINKED
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_APP_NOT_LINKED
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.BaseBalanceViewHolder
import com.tokopedia.home.databinding.ItemBalanceWidgetAtf2Binding
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class BalanceAtf2ViewHolder(v: View, private val totalItems: Int) : BaseBalanceViewHolder<BalanceDrawerItemModel>(v) {
    companion object {
        val LAYOUT = R.layout.item_balance_widget_atf2
        private const val TITLE_HEADER_WEBSITE = "Tokopedia"
        private const val BALANCE_FILL_WIDTH_THRESHOLD = 2
    }

    private val binding: ItemBalanceWidgetAtf2Binding? by viewBinding()
    private var listener: HomeCategoryListener? = null

    private val containerLayoutParams by lazy { binding?.containerBalance?.layoutParams }
    private val successContainerLayoutParams by lazy { binding?.homeContainerBalance?.homeContainerBalance?.layoutParams }
    private val titleLayoutParams by lazy { binding?.homeContainerBalance?.homeTvTitle?.layoutParams as? ConstraintLayout.LayoutParams }
    private val subtitleLayoutParams by lazy { binding?.homeContainerBalance?.homeTvSubtitle?.layoutParams as? ConstraintLayout.LayoutParams }

    override fun bind(
        drawerItem: BalanceDrawerItemModel?,
        listener: HomeCategoryListener?
    ) {
        this.listener = listener
        setWidth()
        renderDrawerItem(drawerItem)
        this.itemView.tag = String.format(
            itemView.context.getString(R.string.tag_balance_widget),
            drawerItem?.drawerItemType.toString()
        )
    }

    private fun setWidth() {
        if (DeviceScreenInfo.isTablet(itemView.context) || totalItems <= BALANCE_FILL_WIDTH_THRESHOLD) {
            setFillWidth()
        } else {
            setDynamicWidth()
        }
        binding?.containerBalance?.layoutParams = containerLayoutParams
        binding?.homeContainerBalance?.homeContainerBalance?.layoutParams = successContainerLayoutParams
        binding?.homeContainerBalance?.homeTvTitle?.layoutParams = titleLayoutParams
        binding?.homeContainerBalance?.homeTvSubtitle?.layoutParams = subtitleLayoutParams
    }

    private fun setFillWidth() {
        containerLayoutParams?.width = BalanceAtf2Util.getBalanceItemWidth(itemView.context, totalItems)
        successContainerLayoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT
        titleLayoutParams?.matchConstraintMaxWidth = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
        subtitleLayoutParams?.matchConstraintMaxWidth = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
    }

    private fun setDynamicWidth() {
        containerLayoutParams?.width = ViewGroup.LayoutParams.WRAP_CONTENT
        successContainerLayoutParams?.width = ViewGroup.LayoutParams.WRAP_CONTENT
        val textMaxWidth = BalanceAtf2Util.getBalanceTextWidth(itemView.context)
        titleLayoutParams?.matchConstraintMaxWidth = textMaxWidth
        subtitleLayoutParams?.matchConstraintMaxWidth = textMaxWidth
    }

    private fun renderDrawerItem(element: BalanceDrawerItemModel?) {
        binding?.cardContainerBalance?.apply {
            animateOnPress = CardUnify2.ANIMATE_OVERLAY_BOUNCE
            setCardUnifyBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    com.tokopedia.home.R.color.home_dms_color_transparent
                )
            )
        }
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
        binding?.homeContainerBalance?.homeTvTitle?.text =
            itemView.context.getString(com.tokopedia.home.R.string.balance_widget_failed_to_load)
        binding?.homeContainerBalance?.homeTvSubtitle?.setTextColor(
            ContextCompat.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_GN500
            )
        )
        binding?.homeContainerBalance?.homeTvSubtitle?.setWeight(Typography.BOLD)
        binding?.homeContainerBalance?.homeTvSubtitle?.text =
            itemView.context.getString(com.tokopedia.home.R.string.text_reload)
        binding?.cardContainerBalance?.handleItemClickType(
            element = element,
            rewardsAction = { showLoading(element) },
            walletAppAction = { showLoading(element) }
        )
    }

    private fun showFailedImage() {
        binding?.homeContainerBalance?.homeIvLogoBalance?.run {
            type = ImageUnify.TYPE_CIRCLE
            setImageDrawable(
                ContextCompat.getDrawable(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN50
                )
            )
        }
    }

    private fun renderItemLoading(element: BalanceDrawerItemModel) {
        binding?.shimmerItemBalanceWidget?.root?.show()
        binding?.homeContainerBalance?.homeContainerBalance?.invisible()
        if (element.drawerItemType == TYPE_WALLET_APP_LINKED) {
            listener?.onRetryWalletApp(element.position, element.headerTitle)
        } else if (element.drawerItemType == TYPE_REWARDS) {
            listener?.onRetryMembership(element.position, element.headerTitle)
        }
    }

    private fun renderItemSuccess(element: BalanceDrawerItemModel) {
        showImageSuccess(element)

        // Load Text
        val balanceText = element.balanceTitleTextAttribute?.text ?: ""
        binding?.homeContainerBalance?.homeTvTitle?.text = balanceText

        // load subtitle
        val subtitle = element.balanceSubTitleTextAttribute?.text ?: ""
        binding?.homeContainerBalance?.homeTvSubtitle?.text = subtitle
        setFontSubtitle(element)

        handleClickSuccess(element, subtitle)
        binding?.shimmerItemBalanceWidget?.root?.gone()
        binding?.homeContainerBalance?.homeContainerBalance?.show()
    }

    private fun setFontSubtitle(element: BalanceDrawerItemModel) {
        when (element.drawerItemType) {
            TYPE_SUBSCRIPTION -> {
                val typographyWeight = if (element.balanceSubTitleTextAttribute?.isBold == true) Typography.BOLD else Typography.REGULAR
                binding?.homeContainerBalance?.homeTvSubtitle?.setWeight(typographyWeight)

                binding?.homeContainerBalance?.homeTvSubtitle?.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        element.balanceSubTitleTextAttribute?.colourRef
                            ?: com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                )
            }
            TYPE_REWARDS -> {
                val typographyWeight = if (element.balanceSubTitleTextAttribute?.isBold == true) Typography.BOLD else Typography.REGULAR
                binding?.homeContainerBalance?.homeTvSubtitle?.setWeight(typographyWeight)

                binding?.homeContainerBalance?.homeTvSubtitle?.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        element.balanceSubTitleTextAttribute?.colourRef
                            ?: com.tokopedia.unifyprinciples.R.color.Unify_NN600
                    )
                )
            }
            TYPE_WALLET_APP_NOT_LINKED -> {
                binding?.homeContainerBalance?.homeTvSubtitle?.setWeight(Typography.BOLD)
                binding?.homeContainerBalance?.homeTvSubtitle?.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                )
            }
            else -> {
                binding?.homeContainerBalance?.homeTvSubtitle?.setWeight(Typography.REGULAR)
                binding?.homeContainerBalance?.homeTvSubtitle?.setTextColor(
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
            binding?.homeContainerBalance?.homeIvLogoBalance?.run {
                type = ImageUnify.TYPE_RECT
                setImageUrl(element.iconImageUrl)
            }
        } else {
            showFailedImage()
        }
    }

    private fun showLoading(element: BalanceDrawerItemModel) {
        binding?.shimmerItemBalanceWidget?.root?.show()
        binding?.homeContainerBalance?.homeContainerBalance?.invisible()
        if (element.drawerItemType == TYPE_WALLET_APP_LINKED) {
            element.state = BalanceDrawerItemModel.STATE_LOADING
            listener?.onRetryWalletApp(element.position, element.headerTitle)
        } else if (element.drawerItemType == TYPE_REWARDS) {
            element.state = BalanceDrawerItemModel.STATE_LOADING
            listener?.onRetryMembership(element.position, element.headerTitle)
        }
    }

    private fun handleClickSuccess(element: BalanceDrawerItemModel, reserveBalance: String) {
        binding?.cardContainerBalance?.handleItemClickType(
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
