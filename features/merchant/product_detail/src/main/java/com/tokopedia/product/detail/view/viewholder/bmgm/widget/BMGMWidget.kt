package com.tokopedia.product.detail.view.viewholder.bmgm.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.setLayoutHeight
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.databinding.BmgmProductListBinding
import com.tokopedia.product.detail.databinding.BmgmWidgetBinding
import com.tokopedia.product.detail.view.util.isInflated
import com.tokopedia.product.detail.view.viewholder.bmgm.adapter.BMGMProductItemAdapter
import com.tokopedia.product.detail.view.viewholder.bmgm.model.BMGMWidgetUiModel
import com.tokopedia.product.detail.view.viewholder.bmgm.model.BMGMWidgetUiState
import com.tokopedia.unifyprinciples.stringToUnifyColor

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

class BMGMWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val SPAN_COUNT = 3

        private const val MIN_GRADIENT_COLOR = 2
    }

    private val binding by lazyThreadSafetyNone {
        val view = inflate(context, com.tokopedia.product.detail.R.layout.bmgm_widget, this)
        BmgmWidgetBinding.bind(view)
    }

    private val productListBinding by lazyThreadSafetyNone {
        val productListStub = binding.bmgmProductListStub.inflate()
        BmgmProductListBinding.bind(productListStub)
    }

    private val productAdapter by lazyThreadSafetyNone {
        BMGMProductItemAdapter().apply {
            productListBinding.bmgmProductList.layoutManager = GridLayoutManager(
                context,
                SPAN_COUNT,
                GridLayoutManager.VERTICAL,
                false
            )
            productListBinding.bmgmProductList.adapter = this
            productListBinding.bmgmProductList.itemAnimator = null
        }
    }

    // endregion

    // region expose function
    fun setData(uiState: BMGMWidgetUiState, router: BMGMWidgetRouter) {
        when (uiState) {
            is BMGMWidgetUiState.Loading -> {
                // no - ops
            }

            is BMGMWidgetUiState.Hide -> {
                hideContent()
            }

            is BMGMWidgetUiState.Show -> {
                showContent(uiModel = uiState.uiModel, router = router)
            }
        }
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
    private fun showContent(uiModel: BMGMWidgetUiModel, router: BMGMWidgetRouter) {
        binding.root.setLayoutHeight(ViewGroup.LayoutParams.WRAP_CONTENT)

        binding.bmgmImageLeft.loadImage(url = uiModel.iconUrl)
        setTitle(title = uiModel.title, color = uiModel.titleColor)
        setEvent(action = uiModel.action, router = router)
        setBackgroundGradient(colors = uiModel.backgroundColor)
        setProductList(uiModel = uiModel, router = router)
    }
    // endregion

    // region title
    private fun setTitle(title: String, color: String) {
        binding.bmgmTitle.text = title

        val default = com.tokopedia.unifyprinciples.R.color.Unify_TN500
        val unifyColor = getStringUnifyColor(color = color, default = default)
        binding.bmgmTitle.setTextColor(unifyColor)
    }
    // endregion

    // region set product list
    private fun setProductList(uiModel: BMGMWidgetUiModel, router: BMGMWidgetRouter) {
        if (uiModel.products.isNotEmpty()) {
            productListBinding.root.show()

            productAdapter.submit(uiModel.products) {
                setRouting(action = uiModel.action, router = router)
            }
        } else if (binding.bmgmProductListStub.isInflated()) { // stub has already inflated
            productListBinding.root.gone()
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

    // region event
    private fun setEvent(action: BMGMWidgetUiModel.Action, router: BMGMWidgetRouter) {
        binding.bmgmComponent.setOnClickListener {
            setRouting(action = action, router = router)
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
