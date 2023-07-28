package com.tokopedia.product.detail.common.bmgm.ui

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.common.bmgm.ui.adapter.BMGMProductAdapter
import com.tokopedia.product.detail.common.bmgm.ui.model.BMGMUiModel
import com.tokopedia.product.detail.common.bmgm.ui.model.BMGMUiState
import com.tokopedia.product.detail.common.databinding.BmgmWidgetBinding
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
    }

    private val binding by lazyThreadSafetyNone {
        val view = inflate(context, com.tokopedia.product.detail.common.R.layout.bmgm_widget, this)
        BmgmWidgetBinding.bind(view).apply {
            bmgmProductList.layoutManager =
                GridLayoutManager(context, SPAN_COUNT, GridLayoutManager.VERTICAL, false)
            bmgmProductList.adapter = productAdapter
        }
    }

    private val productAdapter by lazyThreadSafetyNone {
        BMGMProductAdapter()
    }

    fun setData(uiState: BMGMUiState, router: BMGMRouter) {
        when (uiState) {
            is BMGMUiState.Loading -> {
                // no - ops
            }

            is BMGMUiState.Loaded -> {
                onLoaded(uiModel = uiState.uiModel, router = router)
            }
        }
    }

    private fun onLoaded(uiModel: BMGMUiModel, router: BMGMRouter) {
        binding.bmgmImageLeft.loadImage(uiModel.iconUrl)

        setTitle(titles = uiModel.titles, titleColor = uiModel.titleColor)

        if (uiModel.loadMoreText.isNotBlank()) {
            setEvent(action = uiModel.action, router = router)
        }

        setBackgroundGradient(colors = uiModel.backgroundColor)

        productAdapter.submit(uiModel.products, uiModel.loadMoreText)
    }

    private fun setTitle(titles: List<String>, titleColor: String) {
        binding.bmgmTitle.text = titles.firstOrNull()

        if (titleColor.isNotBlank()) {
            val default = com.tokopedia.unifyprinciples.R.color.Unify_TN500
            val unifyColor = runCatching {
                stringToUnifyColor(context, titleColor).unifyColor
            }.getOrNull() ?: default
            binding.bmgmTitle.setTextColor(unifyColor)
        }
    }

    private fun setBackgroundGradient(colors: String) {
        val mColors = colors.split(",").map {
            runCatching {
                stringToUnifyColor(context, it).unifyColor
            }.getOrNull() ?: Int.ZERO
        }.toIntArray()

        val gradient = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, mColors)
        background = gradient
    }

    private fun setEvent(action: BMGMUiModel.Action, router: BMGMRouter) {
        binding.root.setOnClickListener {
            router.goToAppLink(action.link)
        }
    }
}
