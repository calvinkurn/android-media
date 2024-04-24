package com.tokopedia.home_component.viewholders.shorten

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.GlobalComponentContainerMulti2squareBinding
import com.tokopedia.home_component.decoration.StaticMissionWidgetItemDecoration
import com.tokopedia.home_component.util.setGradientBackground
import com.tokopedia.home_component.viewholders.shorten.factory.ShortenViewFactoryImpl
import com.tokopedia.home_component.viewholders.shorten.internal.ShortenStaticSquaresAdapter
import com.tokopedia.home_component.viewholders.shorten.internal.ShortenVisitable
import com.tokopedia.home_component.viewholders.shorten.internal.TWO_SQUARE_LIMIT
import com.tokopedia.home_component.visitable.shorten.MultiTwoSquareWidgetUiModel
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.setLayoutHeight
import com.tokopedia.utils.view.binding.viewBinding

class ContainerMultiTwoSquareViewHolder(
    view: View,
    private val listener: ContainerMultiTwoSquareListener,
    private val recyclerRecycledViewPool: RecycledViewPool?
) : AbstractViewHolder<MultiTwoSquareWidgetUiModel>(view) {

    private val binding: GlobalComponentContainerMulti2squareBinding? by viewBinding()

    private var mAdapter: ShortenStaticSquaresAdapter? = null
    private val visitableList = mutableListOf<ShortenVisitable>()

    // this is an intentional to make it global variables,
    // we can reduce a redundant calculation and set the view height
    // by using this [maxHeight] variable as validation.
    private var maxHeight = 0

    init {
        if (recyclerRecycledViewPool != null) {
            binding?.lstComponent?.setRecycledViewPool(recyclerRecycledViewPool)
        }

        setupRecyclerView()
    }

    override fun bind(element: MultiTwoSquareWidgetUiModel?) {
        if (element == null) return
        binding?.root?.setGradientBackground(element.backgroundGradientColor)

        renderWidgets(element)
        mAdapter?.submitList(visitableList) {
            calculateTallestItemAndSetAllItem()
        }
    }

    private fun renderWidgets(element: MultiTwoSquareWidgetUiModel) {
        visitableList.clear()
        visitableList.addAll(MultiTwoSquareWidgetUiModel.visitableList(element))
    }

    private fun calculateTallestItemAndSetAllItem() {
        if (binding?.lstComponent?.isComputingLayout == true || maxHeight.isMoreThanZero()) return

        val childCount = binding?.lstComponent?.childCount ?: return
        val childViewRef = mutableListOf<View>()

        for (i in 0 until childCount) {
            val view = binding?.lstComponent?.getChildAt(i) ?: return

            val currentSizeHeight = view.measuredHeight
            maxHeight = maxOf(maxHeight, currentSizeHeight)

            childViewRef.add(view)
        }

        childViewRef.forEach {
            it.setLayoutHeight(maxHeight)
            it.requestLayout()
        }
    }

    private fun setupRecyclerView() {
        mAdapter = if (binding?.lstComponent?.adapter == null) {
            ShortenStaticSquaresAdapter(ShortenViewFactoryImpl(recyclerRecycledViewPool, listener))
        } else {
            binding?.lstComponent?.adapter as? ShortenStaticSquaresAdapter
        }

        val layoutManager = GridLayoutManager(itemView.context, TWO_SQUARE_LIMIT)

        binding?.lstComponent?.addItemDecoration(StaticMissionWidgetItemDecoration.span2())
        binding?.lstComponent?.layoutManager = layoutManager
        binding?.lstComponent?.adapter = mAdapter
        binding?.lstComponent?.setHasFixedSize(true)
    }

    companion object {
        val LAYOUT = R.layout.global_component_container_multi_2square
    }
}
