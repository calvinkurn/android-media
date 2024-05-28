package com.tokopedia.home_component.viewholders.shorten

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.GlobalComponentContainerMulti2squareBinding
import com.tokopedia.home_component.decoration.StaticMissionWidgetItemDecoration
import com.tokopedia.home_component.util.ShortenUtils.TWO_SQUARE_CONTAINER_LIMIT
import com.tokopedia.home_component.util.ShortenUtils.calculateTallestItemAndSetForAllItems
import com.tokopedia.home_component.util.ShortenUtils.isValidShortenWidget
import com.tokopedia.home_component.util.setGradientBackground
import com.tokopedia.home_component.viewholders.shorten.factory.ShortenViewFactoryImpl
import com.tokopedia.home_component.viewholders.shorten.internal.ShortenStaticSquaresAdapter
import com.tokopedia.home_component.viewholders.shorten.internal.ShortenVisitable
import com.tokopedia.home_component.visitable.shorten.MultiTwoSquareWidgetUiModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.utils.view.binding.viewBinding

class ContainerMultiTwoSquareViewHolder(
    view: View,
    private val listener: ContainerMultiTwoSquareListener,
    private val recyclerRecycledViewPool: RecycledViewPool?
) : AbstractViewHolder<MultiTwoSquareWidgetUiModel>(view) {

    private val binding: GlobalComponentContainerMulti2squareBinding? by viewBinding()
    private var mAdapter: ShortenStaticSquaresAdapter? = null

    init {
        if (recyclerRecycledViewPool != null) {
            binding?.lstComponent?.setRecycledViewPool(recyclerRecycledViewPool)
        }

        setupRecyclerView()
    }

    override fun bind(element: MultiTwoSquareWidgetUiModel?) {
        if (element == null) return

        if (element.backgroundGradientColor.isNotEmpty()) {
            binding?.root?.run {
                setGradientBackground(element.backgroundGradientColor)
            }
        }

        val visitableList = MultiTwoSquareWidgetUiModel.visitableList(element)
        if (shouldWidgetContainValidGrids(visitableList)) {
            mAdapter?.submitList(visitableList) {
                setFixedSubShortenWidget()
            }
        }
    }

    override fun bind(element: MultiTwoSquareWidgetUiModel?, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setFixedSubShortenWidget() {
        calculateTallestItemAndSetForAllItems(binding?.lstComponent)
    }

    private fun shouldWidgetContainValidGrids(visitableList: List<ShortenVisitable>): Boolean {
        val isVisitableNotEmpty = visitableList.isNotEmpty()
        val isVisitableContainAtLeast4Items = visitableList.isValidShortenWidget()

        return (isVisitableNotEmpty && isVisitableContainAtLeast4Items).also {
            if (!it) itemView.gone()
        }
    }

    private fun setupRecyclerView() {
        mAdapter = if (binding?.lstComponent?.adapter == null) {
            ShortenStaticSquaresAdapter(ShortenViewFactoryImpl(recyclerRecycledViewPool, listener))
        } else {
            binding?.lstComponent?.adapter as? ShortenStaticSquaresAdapter
        }

        val layoutManager = GridLayoutManager(itemView.context, TWO_SQUARE_CONTAINER_LIMIT)

        binding?.lstComponent?.addItemDecoration(StaticMissionWidgetItemDecoration.span2())
        binding?.lstComponent?.layoutManager = layoutManager
        binding?.lstComponent?.adapter = mAdapter
        binding?.lstComponent?.setHasFixedSize(true)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.global_component_container_multi_2square
    }
}
