package com.tokopedia.product.detail.view.viewholder.gwp.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RotateDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isInvisible
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
import com.tokopedia.product.detail.common.utils.ItemSpaceDecorator
import com.tokopedia.product.detail.common.utils.extensions.addOnImpressionListener
import com.tokopedia.product.detail.common.utils.extensions.updateLayoutParams
import com.tokopedia.product.detail.databinding.GwpCardListBinding
import com.tokopedia.product.detail.databinding.GwpWidgetBinding
import com.tokopedia.product.detail.view.util.isInflated
import com.tokopedia.product.detail.view.viewholder.ActionUiModel
import com.tokopedia.product.detail.view.viewholder.gwp.adapter.GWPCardAdapter
import com.tokopedia.product.detail.view.viewholder.gwp.model.GWPWidgetUiModel
import com.tokopedia.product.detail.view.viewholder.gwp.model.GWPWidgetUiState
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.stringToUnifyColor
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

class GWPWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val MIN_GRADIENT_COLOR = 2
        private val CARD_ITEM_SPACING = 8.toPx()
    }

    private val binding by lazyThreadSafetyNone {
        val inflater = LayoutInflater.from(context)
        GwpWidgetBinding.inflate(inflater, this, true)
    }

    private val cardListBinding by lazyThreadSafetyNone {
        val productListStub = binding.gwpCardList.inflate()
        GwpCardListBinding.bind(productListStub)
    }

    private val cardAdapter by lazyThreadSafetyNone {
        GWPCardAdapter().also(::setupCardRecyclerView)
    }

    private fun setupCardRecyclerView(cardAdapter: GWPCardAdapter) = with(cardListBinding.gwpCardList) {
        layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        adapter = cardAdapter
        addItemDecoration(ItemSpaceDecorator(space = CARD_ITEM_SPACING))
        setHasFixedSize(true)
    }

    private var router: GWPWidgetRouter? = null

    fun init(router: GWPWidgetRouter) {
        this.router = router
    }

    fun setData(state: GWPWidgetUiState, tracker: GWPWidgetTracker) {
        when (state) {
            is GWPWidgetUiState.Loading -> {
                // no-ops
            }

            is GWPWidgetUiState.Hide -> {
                hideContent()
            }

            is GWPWidgetUiState.Show -> {
                showContent(uiModel = state.uiModel, tracker = tracker)
            }
        }
    }

    // region hide state
    private fun hideContent() {
        if (height > Int.ZERO) { // prevent inflate
            binding.root.setLayoutHeight(0)
        }
    }
    // endregion

    // region show content
    private fun showContent(uiModel: GWPWidgetUiModel, tracker: GWPWidgetTracker) {
        binding.root.setLayoutHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
        setWidgetImpression(uiModel = uiModel, tracker = tracker)
        setHeaderData(uiModel = uiModel)
        setBackgroundGradient(colors = uiModel.backgroundColor)
        setCardList(uiModel = uiModel)
        setSeparator(uiModel = uiModel)
        setEvent(uiModel = uiModel, tracker = tracker)
    }

    private fun setWidgetImpression(uiModel: GWPWidgetUiModel, tracker: GWPWidgetTracker) {
        binding.root.addOnImpressionListener(
            holder = tracker.getImpressionHolder(),
            holders = tracker.getImpressionHolders(),
            name = uiModel.title,
            useHolders = tracker.isCacheable()
        ) { tracker.onImpressed() }
    }

    private fun setHeaderData(uiModel: GWPWidgetUiModel) {
        binding.gwpImageLeft.loadImage(url = uiModel.iconUrl)
        setTitle(title = uiModel.title, color = uiModel.titleColor)
    }

    private fun setTitle(title: String, color: String) {
        binding.gwpTitle.text = title

        val default = unifyprinciplesR.color.Unify_TN500
        val unifyColor = getStringUnifyColor(color = color, default = default)
        binding.gwpTitle.setTextColor(unifyColor)
    }

    private fun setCardList(uiModel: GWPWidgetUiModel) {
        if (uiModel.cards.isNotEmpty()) {
            cardListBinding.root.show()
            cardAdapter.submitList(uiModel.cards)
        } else if (binding.gwpCardList.isInflated()) { // stub has already inflated
            cardListBinding.root.gone()
        }
    }

    // region background
    private fun setBackgroundGradient(colors: String) {
        val mColors = getGradientColors(colors = colors)

        if (mColors.size < MIN_GRADIENT_COLOR) {
            setBackgroundColor(mColors.firstOrNull().orZero())
        } else {
            val gradient = RotatableGradientDrawable(GradientDrawable.Orientation.TL_BR, mColors)
            gradient.shape = GradientDrawable.RECTANGLE
            background = gradient
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
    @SuppressLint("ClickableViewAccessibility")
    private fun setEvent(uiModel: GWPWidgetUiModel, tracker: GWPWidgetTracker) {
        binding.root.setOnClickListener { onClick(uiModel = uiModel, tracker = tracker) }

        if (binding.gwpCardList.isInflated()) {
            cardListBinding.gwpCardList.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    onClick(uiModel = uiModel, tracker = tracker)
                }
                false
            }
        }
    }

    private fun onClick(uiModel: GWPWidgetUiModel, tracker: GWPWidgetTracker) {
        setRouting(action = uiModel.action)
        tracker.onClickTracking(data = uiModel)
    }

    private fun setRouting(action: ActionUiModel) {
        when (action.type) {
            ActionUiModel.APPLINK -> router?.goToAppLink(appLink = action.link)
            ActionUiModel.WEBVIEW -> router?.goToWebView(link = action.link)
            else -> {
                // no-ops
            }
        }
    }
    // endregion

    // region common utils
    private fun getStringUnifyColor(color: String, default: Int) = runCatching {
        stringToUnifyColor(context, color).unifyColor
    }.getOrNull() ?: runCatching {
        Color.parseColor(color)
    }.getOrNull() ?: default
    // endregion
}
