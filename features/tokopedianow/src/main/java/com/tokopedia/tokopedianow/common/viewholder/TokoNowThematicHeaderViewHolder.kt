package com.tokopedia.tokopedianow.common.viewholder

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.home_component.customview.pullrefresh.LayoutIconPullRefreshView
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils.isLocalizingAddressNeedShowCoachMark
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState.Companion.LOADING
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState.Companion.SHOW
import com.tokopedia.tokopedianow.common.model.TokoNowThematicHeaderUiModel
import com.tokopedia.tokopedianow.common.util.TypographyUtil.setRightImageDrawable
import com.tokopedia.tokopedianow.common.util.ViewUtil.safeParseColor
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHeaderBinding
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.unifyprinciples.Typography.Companion.DISPLAY_3
import com.tokopedia.utils.resources.isDarkMode
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifycomponents.R as unifycomponentsR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class TokoNowThematicHeaderViewHolder(
    itemView: View,
    private val listener: TokoNowHeaderListener? = null,
    private val tokoNowView: TokoNowView? = null
): AbstractViewHolder<TokoNowThematicHeaderUiModel>(itemView), TickerPagerCallback {
    companion object {
        private const val ALL_CORNER_SIZES_TITLE = 12f
        private const val ALL_CORNER_SIZES_CTA = 8f
        private const val SOURCE_TRACKING = "tokonow page"
        private const val SOURCE = "tokonow"
        private const val PREFIX_LINK = "tokopedia"

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_header
    }

    private var binding: ItemTokopedianowHeaderBinding? by viewBinding()

    override fun bind(
        data: TokoNowThematicHeaderUiModel
    ) {
        binding?.apply {
            setupChooseAddressWidget(data)
            setupLayout(data)
            setupThematicHeader(data)
        }
    }

    override fun onPageDescriptionViewClick(
        linkUrl: CharSequence,
        itemData: Any?
    ) {
        val url = linkUrl.toString()
        RouteManager.route(itemView.context, if (url.startsWith(PREFIX_LINK)) url else "${ApplinkConst.WEBVIEW}?url=${url}")
    }

    private fun ItemTokopedianowHeaderBinding.setupLayout(
        data: TokoNowThematicHeaderUiModel
    ) {
        viewTopSpacing.layoutParams.height = NavToolbarExt.getFullToolbarHeight(itemView.context)
        layoutIconPullRefresh.setColorPullRefresh(data.iconPullRefreshType)
        listener?.pullRefreshIconCaptured(layoutIconPullRefresh)
    }

    private fun ItemTokopedianowHeaderBinding.setupThematicHeader(
        data: TokoNowThematicHeaderUiModel
    ) {
        when(data.state) {
            SHOW -> {
                setupNormalState()
                setupTitle(data)
                setupCta(data)
                setupBackgroundColor(data)
                setupTicker(data)
            }
            LOADING -> {
                setupLoadingState()
            }
            else -> { /* nothing to do */ }
        }
    }

    private fun ItemTokopedianowHeaderBinding.setupNormalState() {
        tpTitle.show()
        tpCta.show()
        aivSuperGraphic.show()

        loader.hide()
        sivTitle.hide()
        sivCta.hide()

        val constraintSet = ConstraintSet().apply {
            clone(root)
        }
        constraintSet.connect(
            R.id.rounded_top_category_navigation,
            ConstraintSet.TOP,
            R.id.tp_title,
            ConstraintSet.BOTTOM,
            root.getDimens(unifyprinciplesR.dimen.unify_space_8)
        )
        constraintSet.applyTo(root)
    }

    private fun ItemTokopedianowHeaderBinding.setupLoadingState() {
        tpTitle.hide()
        tpCta.hide()
        aivSuperGraphic.hide()

        loader.show()
        sivTitle.show()
        sivCta.show()

        val constraintSet = ConstraintSet().apply {
            clone(root)
        }
        constraintSet.connect(
            R.id.rounded_top_category_navigation,
            ConstraintSet.TOP,
            R.id.siv_title,
            ConstraintSet.BOTTOM,
            root.getDimens(unifyprinciplesR.dimen.unify_space_8)
        )
        constraintSet.applyTo(root)

        sivTitle.shapeAppearanceModel = sivTitle.shapeAppearanceModel
            .toBuilder()
            .setAllCornerSizes(ALL_CORNER_SIZES_TITLE.toPx())
            .build()

        sivCta.shapeAppearanceModel = sivTitle.shapeAppearanceModel
            .toBuilder()
            .setAllCornerSizes(ALL_CORNER_SIZES_CTA.toPx())
            .build()

        root.background = null
    }

    private fun ItemTokopedianowHeaderBinding.setupTitle(
        data: TokoNowThematicHeaderUiModel
    ) {
        tpTitle.text = data.pageTitle

        if (data.pageTitleColor != null) {
            tpTitle.setTextColor(data.pageTitleColor)
        }
    }

    private fun ItemTokopedianowHeaderBinding.setupBackgroundColor(
        data: TokoNowThematicHeaderUiModel
    ) {
        if (data.backgroundGradientColor != null) {
            val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(
                    data.backgroundGradientColor.startColor,
                    data.backgroundGradientColor.endColor
                )
            )
            root.background = LayerDrawable(arrayOf(gradientDrawable))
        } else {
            root.setBackgroundColor(
                safeParseColor(
                    color = if (root.context.isDarkMode()) data.backgroundDarkColor else data.backgroundLightColor,
                    defaultColor = ContextCompat.getColor(
                        itemView.context,
                        R.color.tokopedianow_card_dms_color
                    )
                )
            )
        }
        aivSuperGraphic.showWithCondition(data.isSuperGraphicImageShown)
    }

    private fun ItemTokopedianowHeaderBinding.setupCta(
        data: TokoNowThematicHeaderUiModel
    ) {
        tpCta.showIfWithBlock(data.ctaText.isNotBlank()) {
            text = data.ctaText

            if (data.ctaTextColor != null) {
                tpCta.setTextColor(data.ctaTextColor)
            }

            if (data.ctaChevronColor != null) {
                tpCta.setType(DISPLAY_3)
                tpCta.setRightImageDrawable(
                    drawable = ContextCompat.getDrawable(root.context, unifycomponentsR.drawable.iconunify_chevron_right),
                    width = root.getDimens(R.dimen.tokopedianow_shopping_list_chevron_icon_size),
                    height = root.getDimens(R.dimen.tokopedianow_shopping_list_chevron_icon_size),
                    color = data.ctaChevronColor
                )
            }

            setOnClickListener {
                listener?.onClickCtaHeader()
            }
        }
    }

    private fun ItemTokopedianowHeaderBinding.setupChooseAddressWidget(
        data: TokoNowThematicHeaderUiModel
    ) {
        chooseAddressWidget.showIfWithBlock(data.chosenAddress != null && data.chosenAddress.isShown) {
            bindChooseAddressWidget(data)
            showCoachMark()
            updateWidget()
        }
    }

    private fun ItemTokopedianowHeaderBinding.setupTicker(
        data: TokoNowThematicHeaderUiModel
    ) {
        ticker.showIfWithBlock(data.ticker != null && data.ticker.tickerList.isNotEmpty()) {
            val adapter = TickerPagerAdapter(
                itemView.context,
                data.ticker?.tickerList.orEmpty()
            )

            adapter.setPagerDescriptionClickEvent(
                this@TokoNowThematicHeaderViewHolder
            )

            addPagerView(
                adapter = adapter,
                adapterData = data.ticker?.tickerList.orEmpty()
            )

            setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) { /* do nothing */ }

                override fun onDismiss() {
                    listener?.onCloseTicker()
                }
            })
        }
    }

    private fun ItemTokopedianowHeaderBinding.showCoachMark() {
        val isNeedToShowCoachMark = isLocalizingAddressNeedShowCoachMark(itemView.context)
        if (isNeedToShowCoachMark == true && chooseAddressWidget.isShown) {
            val coachMarkItems = arrayListOf(
                CoachMark2Item(
                    chooseAddressWidget,
                    getString(R.string.tokopedianow_home_choose_address_widget_coachmark_title),
                    getString(R.string.tokopedianow_home_choose_address_widget_coachmark_description)
                )
            )
            val coachMark = CoachMark2(itemView.context)
            coachMark.isOutsideTouchable = true
            coachMark.showCoachMark(coachMarkItems)
        }
    }

    private fun ItemTokopedianowHeaderBinding.bindChooseAddressWidget(
        data: TokoNowThematicHeaderUiModel
    ) {
        tokoNowView?.let {
            chooseAddressWidget.bindChooseAddress(object : ChooseAddressWidget.ChooseAddressWidgetListener {
                override fun onLocalizingAddressUpdatedFromWidget() {
                    chooseAddressWidget.updateWidget()
                    tokoNowView.refreshLayoutPage()
                }

                override fun onClickChooseAddressTokoNowTracker() {
                    listener?.onClickChooseAddressWidgetTracker()
                }

                override fun needToTrackTokoNow(): Boolean = true

                override fun getLocalizingAddressHostFragment(): Fragment = tokoNowView.getFragmentPage()

                override fun getLocalizingAddressHostSourceData(): String = SOURCE

                override fun getLocalizingAddressHostSourceTrackingData(): String = SOURCE_TRACKING

                override fun onLocalizingAddressUpdatedFromBackground() { /* do nothing */ }

                override fun onLocalizingAddressRollOutUser(isRollOutUser: Boolean) { /* do nothing */ }

                override fun onLocalizingAddressLoginSuccess() { /* do nothing */ }

                override fun onLocalizingAddressServerDown() { /* do nothing */ }

                override fun isFromTokonowPage(): Boolean = true

                override fun onChangeTextColor(): Int = data.chosenAddress?.chooseAddressResIntColor ?: unifyprinciplesR.color.Unify_NN950_96
            })
        }
    }

    interface TokoNowHeaderListener {
        fun onClickCtaHeader()
        fun onClickChooseAddressWidgetTracker()
        fun onCloseTicker()
        fun pullRefreshIconCaptured(view: LayoutIconPullRefreshView)
    }
}
