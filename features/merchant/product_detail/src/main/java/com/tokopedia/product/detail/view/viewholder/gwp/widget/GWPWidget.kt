package com.tokopedia.product.detail.view.viewholder.gwp.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.setLayoutHeight
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.common.utils.extensions.addOnImpressionListener
import com.tokopedia.product.detail.databinding.GwpCardListBinding
import com.tokopedia.product.detail.databinding.GwpWidgetBinding
import com.tokopedia.product.detail.view.util.isInflated
import com.tokopedia.product.detail.view.viewholder.bmgm.adapter.BMGMProductItemAdapter
import com.tokopedia.product.detail.view.viewholder.bmgm.model.BMGMWidgetUiModel
import com.tokopedia.product.detail.view.viewholder.bmgm.model.BMGMWidgetUiState
import com.tokopedia.product.detail.view.viewholder.bmgm.widget.BMGMWidgetRouter
import com.tokopedia.product.detail.view.viewholder.bmgm.widget.BMGMWidgetTracker
import com.tokopedia.unifyprinciples.stringToUnifyColor
import com.tokopedia.product.detail.R as productdetailR
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
        private const val SPAN_COUNT = 3

        private const val MIN_GRADIENT_COLOR = 2
    }

    private val binding by lazyThreadSafetyNone {
        val view = inflate(context, productdetailR.layout.gwp_widget, this, true)
        GwpWidgetBinding.bind(view)
    }

    private val cardListBinding by lazyThreadSafetyNone {
        val productListStub = binding.gwpCardList.inflate()
        GwpCardListBinding.bind(productListStub)
    }

    private val productAdapter by lazyThreadSafetyNone {
        BMGMProductItemAdapter().apply {
            cardListBinding.bmgmProductList.layoutManager = GridLayoutManager(
                context,
                SPAN_COUNT,
                GridLayoutManager.VERTICAL,
                false
            )
            cardListBinding.bmgmProductList.adapter = this
            cardListBinding.bmgmProductList.itemAnimator = null
        }
    }

    private var previousState: BMGMWidgetUiState? = null
    // endregion

    // region expose function
    fun setData(uiState: BMGMWidgetUiState, router: BMGMWidgetRouter, tracker: BMGMWidgetTracker) {
        if (previousState?.hasSame(newUiModel = uiState) == true) {
            return
        }
        previousState = uiState

        when (uiState) {
            is BMGMWidgetUiState.Loading -> {
                // no - ops
            }

            is BMGMWidgetUiState.Hide -> {
                hideContent()
            }

            is BMGMWidgetUiState.Show -> {
                showContent(uiModel = uiState.uiModel, router = router, tracker = tracker)
            }
        }
    }

    private fun BMGMWidgetUiState?.hasSame(newUiModel: BMGMWidgetUiState): Boolean {
        return this?.hashCode() == newUiModel.hashCode()
    }
    // endregion

    // region hide state
    private fun hideContent() {
        if (height > Int.ZERO) { // prevent inflate
            binding.root.setLayoutHeight(0)
        }
    }
    // endregion

    // region show content
    private fun showContent(uiModel: BMGMWidgetUiModel, router: BMGMWidgetRouter, tracker: BMGMWidgetTracker) {
        binding.root.setLayoutHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
        binding.root.addOnImpressionListener(
            holder = tracker.getImpressionHolder(),
            holders = tracker.getImpressionHolders(),
            name = uiModel.title,
            useHolders = tracker.isCacheable()
        ) {
            tracker.onImpressed()
        }

        binding.bmgmImageLeft.loadImage(url = uiModel.iconUrl)
        setTitle(title = uiModel.title, color = uiModel.titleColor)
        setBackgroundGradient(colors = uiModel.backgroundColor)
        setProductList(uiModel = uiModel, router = router)
        setSeparator(uiModel = uiModel)
        setEvent(uiModel = uiModel, router = router, tracker = tracker)
    }
    // endregion

    // region title
    private fun setTitle(title: String, color: String) {
        binding.bmgmTitle.text = title

        val default = unifyprinciplesR.color.Unify_TN500
        val unifyColor = getStringUnifyColor(color = color, default = default)
        binding.bmgmTitle.setTextColor(unifyColor)
    }
    // endregion

    // region set product list
    private fun setProductList(uiModel: BMGMWidgetUiModel, router: BMGMWidgetRouter) {
        if (uiModel.products.isNotEmpty()) {
            cardListBinding.root.show()

            productAdapter.submitList(uiModel.products)
        } else if (binding.bmgmProductList.isInflated()) { // stub has already inflated
            cardListBinding.root.gone()
        }
    }
    // endregion

    // region background
    private fun setBackgroundGradient(colors: String) {
        val mColors = getGradientColors(colors = colors)

        if (mColors.size < MIN_GRADIENT_COLOR) {
            setBackgroundColor(mColors.firstOrNull().orZero())
        } else {
            background = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, mColors)
        }
    }

    private fun getGradientColors(colors: String) = colors.split(",").map {
        val color = it.trim()
        getStringUnifyColor(color = color, default = Int.ZERO)
    }.toIntArray()
    // endregion

    // region separator
    private fun setSeparator(uiModel: BMGMWidgetUiModel) {
        binding.bmgmSeparatorBottom.isInvisible = !uiModel.showSeparatorBottom
    }
    // endregion

    // region event
    @SuppressLint("ClickableViewAccessibility")
    private fun setEvent(uiModel: BMGMWidgetUiModel, router: BMGMWidgetRouter, tracker: BMGMWidgetTracker) {
        fun onClick() {
            setRouting(action = uiModel.action, router = router)
            tracker.onClick(data = uiModel)
        }

        binding.root.setOnClickListener { onClick() }

        if (binding.bmgmProductList.isInflated()) {
            cardListBinding.bmgmProductList.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    onClick()
                }
                false
            }
        }
    }

    private fun setRouting(
        action: BMGMWidgetUiModel.Action,
        router: BMGMWidgetRouter
    ) {
        when (action.type) {
            BMGMWidgetUiModel.Action.APPLINK -> router.goToAppLink(action.link)
            BMGMWidgetUiModel.Action.WEBVIEW -> router.goToWebView(action.link)
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
