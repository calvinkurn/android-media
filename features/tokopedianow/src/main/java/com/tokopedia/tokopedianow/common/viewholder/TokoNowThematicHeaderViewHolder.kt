package com.tokopedia.tokopedianow.common.viewholder

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.home_component.customview.pullrefresh.LayoutIconPullRefreshView
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState.Companion.LOADING
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState.Companion.SHOW
import com.tokopedia.tokopedianow.common.model.TokoNowThematicHeaderUiModel
import com.tokopedia.tokopedianow.common.util.ViewUtil.safeParseColor
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHeaderBinding
import com.tokopedia.tokopedianow.home.presentation.view.listener.HomeChooseAddressWidgetListener
import com.tokopedia.unifyprinciples.Typography.Companion.DISPLAY_3
import com.tokopedia.utils.resources.isDarkMode
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifycomponents.R as unifycomponentsR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class TokoNowThematicHeaderViewHolder(
    itemView: View,
    private val listener: TokoNowHeaderListener? = null,
    private val chooseAddressListener: TokoNowChooseAddressWidgetListener? = null,
    private val tokoNowView: TokoNowView? = null
): AbstractViewHolder<TokoNowThematicHeaderUiModel>(itemView) {
    companion object {
        private const val DEFAULT_BOUND = 0
        private const val ALL_CORNER_SIZES_TITLE = 12f
        private const val ALL_CORNER_SIZES_CTA = 8f

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_header
    }

    private var binding: ItemTokopedianowHeaderBinding? by viewBinding()

    override fun bind(data: TokoNowThematicHeaderUiModel) {
        binding?.apply {
            setupChooseAddressWidget()
            setupLayout()
            setupThematicHeader(data)
        }
    }

    private fun ItemTokopedianowHeaderBinding.setupLayout() {
        viewTopSpacing.layoutParams.height = NavToolbarExt.getFullToolbarHeight(itemView.context)
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
    }

    private fun ItemTokopedianowHeaderBinding.setupTitle(
        data: TokoNowThematicHeaderUiModel
    ) {
        tpTitle.text = data.pageTitle
        if (data.pageTitleColor != null) tpTitle.setTextColor(data.pageTitleColor)
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
        data: TokoNowThematicHeaderUiModel
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
            updateWidget()
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
