package com.tokopedia.product.detail.common.bmgm.ui

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.product.detail.common.bmgm.model.BMGMUiModel
import com.tokopedia.product.detail.common.bmgm.ui.adapter.BMGMProductAdapter
import com.tokopedia.product.detail.common.databinding.BmgmWidgetBinding
import com.tokopedia.unifyprinciples.microinteraction.toPx

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
        val inflater = LayoutInflater.from(context)
        BmgmWidgetBinding.inflate(inflater)
    }

    private val productAdapter by lazyThreadSafetyNone {
        BMGMProductAdapter()
    }

    init {
        prepareUI()
    }

    private fun prepareUI() {
        with(binding.bmgmProductList) {
            layoutManager = GridLayoutManager(context, SPAN_COUNT, GridLayoutManager.VERTICAL, false)
            addItemDecoration(GridSpacingItemDecorator())
            adapter = productAdapter
        }
    }

    class GridSpacingItemDecorator : ItemDecoration() {

        private val offset = 16.toPx()

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            if (position == 0) {
                outRect.left = 0
            } else {
                outRect.left = offset
            }
        }
    }

    fun setData(uiModel: BMGMUiModel, router: BMGMRouter) {
        productAdapter.submit(uiModel.products, uiModel.loadMoreText)

        setTitle(titles = uiModel.titles)

        if (uiModel.loadMoreText.isNotBlank()) {
            setEvent(action = uiModel.action, router = router)
        }
    }

    private fun setTitle(titles: List<String>) {
        binding.bmgmTitle.text = titles.firstOrNull()
    }

    private fun setEvent(action: BMGMUiModel.Action, router: BMGMRouter) {
        binding.root.setOnClickListener {
            router.goToAppLink(action.link)
        }
    }
}
