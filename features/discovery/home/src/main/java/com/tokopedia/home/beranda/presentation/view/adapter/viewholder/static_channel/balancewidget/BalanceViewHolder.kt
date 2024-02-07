package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget

import android.os.Bundle
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
import com.tokopedia.home.beranda.presentation.view.helper.HomeThematicUtil
import com.tokopedia.home.databinding.ItemBalanceWidgetBinding
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.home.R as homeR

/**
 * Created by frenzel
 */
class BalanceViewHolder(
    v: View,
    private val totalItems: Int,
    private val homeThematicUtil: HomeThematicUtil,
) : BaseBalanceViewHolder<BalanceDrawerItemModel>(v) {
    companion object {
        val LAYOUT = R.layout.item_balance_widget
        private const val TITLE_HEADER_WEBSITE = "Tokopedia"
        private const val BALANCE_FILL_WIDTH_THRESHOLD = 2
    }

    private val binding: ItemBalanceWidgetBinding? by viewBinding()
    private var listener: HomeCategoryListener? = null

    private val containerLayoutParams by lazy { binding?.containerBalance?.layoutParams }
    private val successContainerLayoutParams by lazy { binding?.homeContainerBalance?.homeContainerBalance?.layoutParams }
    private val titleLayoutParams by lazy { binding?.homeContainerBalance?.homeTvTitle?.layoutParams as? ConstraintLayout.LayoutParams }
    private val subtitleLayoutParams by lazy { binding?.homeContainerBalance?.homeTvSubtitle?.layoutParams as? ConstraintLayout.LayoutParams }
    private val imageLayoutParams by lazy { binding?.homeContainerBalance?.homeIvLogoBalance?.layoutParams }
    private val loaderImageLayoutParams by lazy { binding?.shimmerItemBalanceWidget?.loaderBalanceImage?.layoutParams }

    override fun bind(
        model: BalanceDrawerItemModel?,
        listener: HomeCategoryListener?
    ) {
        this.listener = listener
        setLayoutParams()
        renderDrawerItem(model)
        this.itemView.tag = String.format(
            itemView.context.getString(R.string.tag_balance_widget),
            model?.drawerItemType.toString()
        )
    }

    override fun bind(
        model: BalanceDrawerItemModel?,
        listener: HomeCategoryListener?,
        payloads: MutableList<Any>
    ) {
        if(payloads.isNotEmpty()) {
            if((payloads[0] as? Bundle)?.getBoolean(HomeThematicUtil.PAYLOAD_APPLY_THEMATIC_COLOR) == true) {
                model?.let { renderTextColor(it) }
            }
        } else {
            bind(model, listener)
        }
    }

    private fun setLayoutParams() {
        if (DeviceScreenInfo.isTablet(itemView.context) || totalItems <= BALANCE_FILL_WIDTH_THRESHOLD) {
            setFillWidth()
        } else {
            setDynamicWidth()
        }
        configureBalanceStyle()
        binding?.containerBalance?.layoutParams = containerLayoutParams
        binding?.homeContainerBalance?.homeContainerBalance?.layoutParams = successContainerLayoutParams
        binding?.homeContainerBalance?.homeTvTitle?.layoutParams = titleLayoutParams
        binding?.homeContainerBalance?.homeTvSubtitle?.layoutParams = subtitleLayoutParams
        binding?.homeContainerBalance?.homeIvLogoBalance?.layoutParams = imageLayoutParams
        binding?.shimmerItemBalanceWidget?.loaderBalanceImage?.layoutParams = loaderImageLayoutParams
    }

    private fun setFillWidth() {
        containerLayoutParams?.width = BalanceUtil.getBalanceItemWidth(itemView.context, totalItems)
        successContainerLayoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT
        titleLayoutParams?.matchConstraintMaxWidth = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
        subtitleLayoutParams?.matchConstraintMaxWidth = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
    }

    private fun setDynamicWidth() {
        containerLayoutParams?.width = ViewGroup.LayoutParams.WRAP_CONTENT
        successContainerLayoutParams?.width = ViewGroup.LayoutParams.WRAP_CONTENT
        val textMaxWidth = BalanceUtil.getBalanceTextWidth(itemView.context)
        titleLayoutParams?.matchConstraintMaxWidth = textMaxWidth
        subtitleLayoutParams?.matchConstraintMaxWidth = textMaxWidth
    }

    private fun configureBalanceStyle() {
        setupAtf3Balance()
    }

    private fun setupAtf3Balance() {
        // container padding
        val leftPadding = itemView.context.resources.getDimensionPixelSize(homeR.dimen.balance_inner_left_padding)
        val rightPadding = itemView.context.resources.getDimensionPixelSize(homeR.dimen.balance_atf3_inner_right_padding)
        val verticalPadding = itemView.context.resources.getDimensionPixelSize(homeR.dimen.balance_vertical_padding)
        binding?.shimmerItemBalanceWidget?.homeContainerBalanceShimmer?.setPadding(leftPadding, verticalPadding, rightPadding, verticalPadding)
        binding?.homeContainerBalance?.homeContainerBalance?.setPadding(leftPadding, verticalPadding, rightPadding, verticalPadding)

        // image size
        val imageSize = itemView.context.resources.getDimensionPixelSize(homeR.dimen.balance_atf3_image_size)
        imageLayoutParams?.width = imageSize
        imageLayoutParams?.height = imageSize

        // title font type
        val fontType = Typography.DISPLAY_3
        binding?.homeContainerBalance?.homeTvTitle?.setType(fontType)
    }

    private fun renderDrawerItem(element: BalanceDrawerItemModel?) {
        binding?.cardContainerBalance?.apply {
            animateOnPress = CardUnify2.ANIMATE_OVERLAY_BOUNCE
            setCardUnifyBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    homeR.color.home_dms_color_transparent
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
            itemView.context.getString(homeR.string.balance_widget_failed_to_load)
        binding?.homeContainerBalance?.homeTvSubtitle?.text =
            itemView.context.getString(homeR.string.text_reload)
        renderTextColor(element)
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
                homeThematicUtil.getThematicDrawable(unifyprinciplesR.color.Unify_NN50, itemView.context)
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
        renderTitle(element)
        renderSubtitle(element)
        renderTextColor(element)
        handleClickSuccess(element)
        binding?.shimmerItemBalanceWidget?.root?.gone()
        binding?.homeContainerBalance?.homeContainerBalance?.show()
    }

    private fun renderTitle(element: BalanceDrawerItemModel) {
        val balanceText = element.balanceTitleTextAttribute?.text ?: ""
        binding?.homeContainerBalance?.homeTvTitle?.text = balanceText
    }

    private fun renderSubtitle(element: BalanceDrawerItemModel) {
        val subtitle = element.balanceSubTitleTextAttribute?.text ?: ""
        binding?.homeContainerBalance?.homeTvSubtitle?.text = subtitle
    }

    private fun renderTextColor(element: BalanceDrawerItemModel) {
        val balanceTextColor = homeThematicUtil.getThematicColor(unifyprinciplesR.color.Unify_NN950, itemView.context)
        binding?.homeContainerBalance?.homeTvTitle?.setTextColor(balanceTextColor)
        when (element.state) {
            BalanceDrawerItemModel.STATE_SUCCESS -> {
                setSuccessFontSubtitle(element)
            }
            BalanceDrawerItemModel.STATE_ERROR -> {
                setErrorFontSubtitle()
            }
        }
    }

    private fun setSuccessFontSubtitle(element: BalanceDrawerItemModel) {
        when (element.drawerItemType) {
            TYPE_SUBSCRIPTION -> {
                val typographyWeight = if (element.balanceSubTitleTextAttribute?.isBold == true) Typography.BOLD else Typography.REGULAR
                binding?.homeContainerBalance?.homeTvSubtitle?.setWeight(typographyWeight)

                binding?.homeContainerBalance?.homeTvSubtitle?.setTextColor(
                    homeThematicUtil.getThematicColor(
                        element.balanceSubTitleTextAttribute?.colourRef
                            ?: unifyprinciplesR.color.Unify_GN500,
                        itemView.context
                    )
                )
            }
            TYPE_REWARDS -> {
                val typographyWeight = if (element.balanceSubTitleTextAttribute?.isBold == true) Typography.BOLD else Typography.REGULAR
                binding?.homeContainerBalance?.homeTvSubtitle?.setWeight(typographyWeight)

                binding?.homeContainerBalance?.homeTvSubtitle?.setTextColor(
                    homeThematicUtil.getThematicColor(
                        element.balanceSubTitleTextAttribute?.colourRef
                            ?: unifyprinciplesR.color.Unify_NN600,
                        itemView.context
                    )
                )
            }
            TYPE_WALLET_APP_NOT_LINKED -> {
                binding?.homeContainerBalance?.homeTvSubtitle?.setWeight(Typography.BOLD)
                binding?.homeContainerBalance?.homeTvSubtitle?.setTextColor(
                    homeThematicUtil.getThematicColor(unifyprinciplesR.color.Unify_GN500, itemView.context)
                )
            }
            else -> {
                binding?.homeContainerBalance?.homeTvSubtitle?.setWeight(Typography.REGULAR)
                binding?.homeContainerBalance?.homeTvSubtitle?.setTextColor(
                    homeThematicUtil.getThematicColor(unifyprinciplesR.color.Unify_NN600, itemView.context)
                )
            }
        }
    }

    private fun setErrorFontSubtitle() {
        binding?.homeContainerBalance?.homeTvSubtitle?.setTextColor(
            homeThematicUtil.getThematicColor(unifyprinciplesR.color.Unify_GN500, itemView.context)
        )
        binding?.homeContainerBalance?.homeTvSubtitle?.setWeight(Typography.BOLD)
    }

    private fun showImageSuccess(element: BalanceDrawerItemModel) {
        if (!element.iconImageUrl.isNullOrBlank()) {
            binding?.homeContainerBalance?.homeIvLogoBalance?.run {
                type = ImageUnify.TYPE_RECT
                loadImage(element.iconImageUrl)
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

    private fun handleClickSuccess(element: BalanceDrawerItemModel) {
        val reserveBalance = element.balanceSubTitleTextAttribute?.text ?: ""
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
