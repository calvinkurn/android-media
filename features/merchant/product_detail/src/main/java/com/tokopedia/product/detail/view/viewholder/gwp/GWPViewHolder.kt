package com.tokopedia.product.detail.view.viewholder.gwp

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.setLayoutHeight
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.utils.ItemSpaceDecorator
import com.tokopedia.product.detail.common.utils.extensions.addOnPdpImpressionListener
import com.tokopedia.product.detail.databinding.ItemDynamicProductGwpBinding
import com.tokopedia.product.detail.view.fragment.delegate.BasicComponentEvent
import com.tokopedia.product.detail.view.viewholder.ProductDetailPageViewHolder
import com.tokopedia.product.detail.view.viewholder.gwp.adapter.GWPCardAdapter
import com.tokopedia.product.detail.view.viewholder.gwp.callback.GWPCallback
import com.tokopedia.product.detail.view.viewholder.gwp.event.GWPEvent
import com.tokopedia.product.detail.view.viewholder.gwp.model.GWPWidgetUiModel
import com.tokopedia.product.detail.view.viewholder.gwp.model.GWPWidgetUiState
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.stringToUnifyColor
import com.tokopedia.unifyprinciples.R as unifyPrinciplesR

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

class GWPViewHolder(
    private val view: View,
    private val callback: GWPCallback
) : ProductDetailPageViewHolder<GWPUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_product_gwp

        private const val MIN_GRADIENT_COLOR = 2
        private val CARD_ITEM_SPACING = 8.toPx()
    }

    private val binding by lazyThreadSafetyNone {
        ItemDynamicProductGwpBinding.bind(view)
    }

    private val rootView get() = binding.root

    private val cardAdapter by lazyThreadSafetyNone {
        GWPCardAdapter(callback = callback, getParentTrackData = {
            getComponentTrackData(element = mElement)
        }).also(::setupCardRecyclerView)
    }

    private fun setupCardRecyclerView(cardAdapter: GWPCardAdapter) =
        with(binding.gwpCardList) {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = cardAdapter
            addItemDecoration(ItemSpaceDecorator(space = CARD_ITEM_SPACING))
            itemAnimator = null
            optimizeNestedRecyclerView()
        }

    private var mElement: GWPUiModel = GWPUiModel()

    override fun bind(element: GWPUiModel) {
        mElement = element

        when (val state = element.state) {
            is GWPWidgetUiState.Hide -> {
                hideContent()
            }

            is GWPWidgetUiState.Show -> {
                showContent(uiModel = state.uiModel)
            }
        }
    }

    override fun bind(element: GWPUiModel?, payloads: MutableList<Any>) {
        val data = element ?: return
        if (payloads.isEmpty()) return

        bind(element = data)
    }

    private fun RecyclerView.optimizeNestedRecyclerView() {
        setRecycledViewPool(callback.parentRecyclerViewPool)
        setHasFixedSize(true)
    }

    // region hide state
    private fun hideContent() {
        rootView.setLayoutHeight(Int.ZERO)
    }
    // endregion

    // region show content
    private fun showContent(uiModel: GWPWidgetUiModel) {
        rootView.setLayoutHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
        setComponentImpression()
        setHeaderData(uiModel = uiModel)
        setBackgroundGradient(colors = uiModel.backgroundColor)
        setCardList(uiModel = uiModel)
        setSeparator(uiModel = uiModel)
        setEvent(uiModel = uiModel)
    }

    private fun setComponentImpression() {
        binding.root.addOnPdpImpressionListener(
            holders = callback.impressionHolders,
            name = mElement.impressionKey()
        ) {
            val trackerData = getComponentTrackData(element = mElement)
            callback.event(BasicComponentEvent.OnImpressComponent(trackData = trackerData))
        }
    }

    private fun setHeaderData(uiModel: GWPWidgetUiModel) {
        binding.gwpImageLeft.loadImage(url = uiModel.iconUrl)
        setTitle(title = uiModel.title, color = uiModel.titleColor)
    }

    private fun setTitle(title: String, color: String) {
        binding.gwpTitle.text = title

        val default = unifyPrinciplesR.color.Unify_TN500
        val unifyColor = getStringUnifyColor(color = color, default = default)
        binding.gwpTitle.setTextColor(unifyColor)
    }

    private fun setCardList(uiModel: GWPWidgetUiModel) = with(binding) {
        if (uiModel.cards.isNotEmpty()) {
            gwpCardList.show()
            cardAdapter.submitList(uiModel.cards)
        } else {
            gwpCardList.gone()
        }
    }

    // region background
    private fun setBackgroundGradient(colors: String) {
        val mColors = getGradientColors(colors = colors)

        if (mColors.size < MIN_GRADIENT_COLOR) {
            rootView.setBackgroundColor(mColors.firstOrNull().orZero())
        } else {
            val gradient = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, mColors)
            rootView.background = gradient
        }
    }

    private fun getGradientColors(colors: String) = colors.split(",").map {
        val color = it.trim()
        getStringUnifyColor(color = color, default = Int.ZERO)
    }.toIntArray()
    // endregion

    private fun setSeparator(uiModel: GWPWidgetUiModel) {
        binding.gwpSeparatorBottom.isVisible = uiModel.showSeparatorBottom
    }

    // region event
    private fun setEvent(uiModel: GWPWidgetUiModel) {
        binding.root.setOnClickListener {
            uiModel.trackData = getComponentTrackData(mElement)
            callback.event(GWPEvent.OnClickComponent(data = uiModel))
        }
    }
    // endregion

    // region common utils
    private fun getStringUnifyColor(color: String, default: Int) = runCatching {
        stringToUnifyColor(rootView.context, color).unifyColor
    }.getOrNull() ?: runCatching {
        Color.parseColor(color)
    }.getOrNull() ?: default
    // endregion
}
