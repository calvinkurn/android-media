package com.tokopedia.tokopedianow.common.viewholder

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.home_component.customview.pullrefresh.LayoutIconPullRefreshView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowHeaderUiModel
import com.tokopedia.tokopedianow.common.util.ViewUtil.safeParseColor
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHeaderBinding
import com.tokopedia.tokopedianow.home.presentation.view.listener.HomeChooseAddressWidgetListener
import com.tokopedia.unifyprinciples.Typography.Companion.DISPLAY_3
import com.tokopedia.utils.resources.isDarkMode
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifycomponents.R as unifycomponentsR

class TokoNowHeaderViewHolder(
    itemView: View,
    private val listener: TokoNowHeaderListener? = null,
    private val chooseAddressListener: TokoNowChooseAddressWidgetListener? = null,
    private val tokoNowView: TokoNowView? = null
): AbstractViewHolder<TokoNowHeaderUiModel>(itemView) {
    companion object {
        private const val DEFAULT_BOUND = 0

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_header
    }

    private var binding: ItemTokopedianowHeaderBinding? by viewBinding()

    override fun bind(data: TokoNowHeaderUiModel) {
        binding?.apply {
            setupTitle(data)
            setupCta(data)
            setupBackgroundColor(data)
            setupChooseAddressWidget()
            listener?.pullRefreshIconCaptured(layoutIconPullRefresh)
        }
    }

    private fun ItemTokopedianowHeaderBinding.setupTitle(
        data: TokoNowHeaderUiModel
    ) {
        tpTitle.text = data.pageTitle
        if (data.pageTitleColor != null) tpTitle.setTextColor(data.pageTitleColor)
    }

    private fun ItemTokopedianowHeaderBinding.setupBackgroundColor(
        data: TokoNowHeaderUiModel
    ) {
        if (data.backgroundGradientColor != null) {
            val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(
                    data.backgroundGradientColor.startColor,
                    data.backgroundGradientColor.endColor
                )
            )
            val layerDrawable = LayerDrawable(arrayOf(gradientDrawable))
            root.background = layerDrawable
            aivSuperGraphic.show()
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
            aivSuperGraphic.hide()
        }
    }

    private fun ItemTokopedianowHeaderBinding.setupCta(
        data: TokoNowHeaderUiModel
    ) {
        tpCta.text = data.ctaText
        tpCta.setOnClickListener {
            listener?.onClickCtaHeader()
        }

        if (data.ctaChevronIsShown) {
            tpCta.setType(DISPLAY_3)
            if (data.ctaTextColor != null) tpCta.setTextColor(data.ctaTextColor)
            ContextCompat.getDrawable(root.context, unifycomponentsR.drawable.iconunify_chevron_right)?.apply {
                val width = root.resources.getDimensionPixelSize(R.dimen.tokopedianow_shopping_list_header_chevron_icon_size)
                val height = root.resources.getDimensionPixelSize(R.dimen.tokopedianow_shopping_list_header_chevron_icon_size)
                setBounds(DEFAULT_BOUND, DEFAULT_BOUND, width, height)
                if (data.ctaChevronColor != null) colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(data.ctaChevronColor, BlendModeCompat.SRC_ATOP)
                tpCta.setCompoundDrawables(null, null, this@apply, null)
            }
        }
    }

    private fun ItemTokopedianowHeaderBinding.setupChooseAddressWidget() {
        chooseAddressWidget.showIfWithBlock(chooseAddressListener != null) {
            bindChooseAddressWidget()
            showCoachMark()
        }
    }

    private fun ItemTokopedianowHeaderBinding.showCoachMark() {
        val chooseAddressWidget = chooseAddressWidget
        val isNeedToShowCoachMark = ChooseAddressUtils.isLocalizingAddressNeedShowCoachMark(itemView.context)

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

    private fun ItemTokopedianowHeaderBinding.bindChooseAddressWidget() {
        tokoNowView?.let {
            val listener = HomeChooseAddressWidgetListener(
                it,
                chooseAddressWidget,
                chooseAddressListener
            )
            chooseAddressWidget.bindChooseAddress(listener)
        }
    }

    interface TokoNowHeaderListener {
        fun onClickCtaHeader()
        fun pullRefreshIconCaptured(view: LayoutIconPullRefreshView)
    }
}
