package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.home_component.customview.pullrefresh.LayoutIconPullRefreshView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeHeaderUiModel
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.ViewUtil
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeHeaderBinding
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.presentation.view.listener.HomeChooseAddressWidgetListener
import com.tokopedia.utils.view.binding.viewBinding

class HomeHeaderViewHolder(
    itemView: View,
    private val listener: HomeHeaderListener? = null,
    private val chooseAddressListener: TokoNowChooseAddressWidgetListener? = null,
    private val tokoNowView: TokoNowView? = null
) : AbstractViewHolder<HomeHeaderUiModel>(itemView) {

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_header
    }

    private var binding: ItemTokopedianowHomeHeaderBinding? by viewBinding()

    override fun bind(header: HomeHeaderUiModel) {
        setupLayoutView()
        setupChooseAddressWidget()
        setupThematicHeader(header)
    }

    override fun bind(header: HomeHeaderUiModel, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() == true) {
            setupThematicHeader(header)
        }
    }

    private fun setupThematicHeader(header: HomeHeaderUiModel) {
        when (header.state) {
            HomeLayoutItemState.LOADED -> {
                showHeaderBackground(header)
                showHeaderContent(header)
                hideLoading()
            }
            HomeLayoutItemState.ERROR -> {
                showDefaultContent()
                hideLoading()
            }
            else -> {
                showLoading()
                hideBackground()
                hideContent()
            }
        }
    }

    private fun setupLayoutView() {
        binding?.apply {
            viewTopSpacing.layoutParams.height =
                NavToolbarExt.getFullToolbarHeight(itemView.context)
            listener?.pullRefreshIconCaptured(layoutIconPullRefresh)
        }
    }

    private fun setupChooseAddressWidget() {
        bindChooseAddressWidget()
        showCoachMark()
    }

    private fun bindChooseAddressWidget() {
        binding?.apply {
            tokoNowView?.let {
                val listener = HomeChooseAddressWidgetListener(
                    it,
                    chooseAddressWidget,
                    chooseAddressListener
                )
                chooseAddressWidget.bindChooseAddress(listener)
            }
        }
    }

    private fun showCoachMark() {
        binding?.apply {
            val chooseAddressWidget = binding?.chooseAddressWidget
            val isNeedToShowCoachMark = ChooseAddressUtils
                .isLocalizingAddressNeedShowCoachMark(itemView.context)

            if (isNeedToShowCoachMark == true && chooseAddressWidget?.isShown == true) {
                val coachMarkItems = arrayListOf(chooseAddressWidget.createCoachMarkItem())
                val coachMark = CoachMark2(itemView.context)
                coachMark.isOutsideTouchable = true
                coachMark.showCoachMark(coachMarkItems)
            }
        }
    }

    private fun ChooseAddressWidget.createCoachMarkItem(): CoachMark2Item {
        return CoachMark2Item(
            this,
            getString(R.string.tokopedianow_home_choose_address_widget_coachmark_title),
            getString(R.string.tokopedianow_home_choose_address_widget_coachmark_description)
        )
    }

    private fun showHeaderBackground(header: HomeHeaderUiModel) {
        binding?.apply {
            val backgroundData = header.background
            val defaultColor = ContextCompat.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_GN500
            )
            val backgroundColor = ViewUtil.safeParseColor(backgroundData.color, defaultColor)
            val imageUrl = backgroundData.imageUrl
            val animUrl = backgroundData.animationUrl

            viewBackground.setBackgroundColor(backgroundColor)
            imageHeaderSupergraphic.loadImageWithoutPlaceholder(imageUrl)
            lottieAnimationHeader.apply {
                setAnimationFromUrl(animUrl)
                setFailureListener {  }
            }

            viewBackground.show()
            imageHeaderSupergraphic.show()
            lottieAnimationHeader.show()
        }
    }

    private fun showHeaderContent(header: HomeHeaderUiModel) {
        binding?.apply {
            val shopStatus = header.shopStatus
            val shippingHint = header.shippingHint
            val subTitle = itemView.context
                .getString(R.string.tokopedianow_home_header_subtitle, shopStatus, shippingHint)

            textTitle.text = header.title
            textSubtitle.text = MethodChecker.fromHtml(subTitle)
            imageIcon.loadImage(header.logoUrl)

            imageIcon.show()
            textTitle.show()
            textSubtitle.show()
            imageChevronDown.show()
        }
    }

    private fun showDefaultContent() {
        binding?.apply {
            val defaultColor = ContextCompat.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_GN500
            )

            textTitle.text = itemView.context
                .getString(R.string.tokopedianow_home_title)
            imageIcon.loadImage(R.drawable.tokopedianow_icon)
            viewBackground.setBackgroundColor(defaultColor)

            textTitle.show()
            imageIcon.show()
            viewBackground.show()
            textSubtitle.hide()
            imageChevronDown.hide()
            imageHeaderSupergraphic.hide()
            lottieAnimationHeader.hide()
        }
    }

    private fun showLoading() {
        binding?.apply {
            val color = ContextCompat.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_Static_White
            )

            shimmerIcon.setBackgroundColor(color)
            shimmerTitle.setBackgroundColor(color)
            shimmerSubtitle.setBackgroundColor(color)

            shimmerIcon.show()
            shimmerTitle.show()
            shimmerSubtitle.show()
            headerShimmer.show()
        }
    }

    private fun hideLoading() {
        binding?.apply {
            shimmerIcon.hide()
            shimmerTitle.hide()
            shimmerSubtitle.hide()
            headerShimmer.hide()
        }
    }

    private fun hideContent() {
        binding?.apply {
            textTitle.hide()
            textSubtitle.hide()
            imageIcon.hide()
            imageChevronDown.hide()
        }
    }

    private fun hideBackground() {
        binding?.apply {
            viewBackground.hide()
            imageHeaderSupergraphic.hide()
            lottieAnimationHeader.hide()
        }
    }

    interface HomeHeaderListener {
        fun pullRefreshIconCaptured(view: LayoutIconPullRefreshView?)
    }
}
