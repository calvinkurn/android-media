package com.tokopedia.home_component.viewholders.shorten

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.GlobalComponentContainerStatic2squareBinding
import com.tokopedia.home_component.decoration.StaticMissionWidgetItemDecoration
import com.tokopedia.home_component.viewholders.shorten.factory.ShortenViewFactoryImpl
import com.tokopedia.home_component.viewholders.shorten.internal.ShortenStaticSquaresAdapter
import com.tokopedia.home_component.viewholders.shorten.internal.ShortenVisitable
import com.tokopedia.home_component.viewholders.shorten.internal.TWO_SQUARE_LIMIT
import com.tokopedia.home_component.visitable.shorten.DealsAndMissionWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ContainerStaticSquaresViewHolder(
    view: View,
    private val recyclerRecycledViewPool: RecycledViewPool?
) : AbstractViewHolder<DealsAndMissionWidgetUiModel>(view) {

    private val binding: GlobalComponentContainerStatic2squareBinding? by viewBinding()

    private var mAdapter: ShortenStaticSquaresAdapter? = null
    private val visitableList = mutableListOf<ShortenVisitable>()

    init {
        if (recyclerRecycledViewPool != null) {
            binding?.lstComponent?.setRecycledViewPool(recyclerRecycledViewPool)
        }

        setupRecyclerView()
    }

    override fun bind(element: DealsAndMissionWidgetUiModel?) {
        if (element == null) return

        renderWidgets(element)
        mAdapter?.submitList(visitableList)
    }

    private fun renderWidgets(element: DealsAndMissionWidgetUiModel) {
        visitableList.clear()

        visitableList.add(element.mission.position, element.mission)
        visitableList.add(element.deals.position, element.deals)
    }

    private fun setupRecyclerView() {
        mAdapter = if (binding?.lstComponent?.adapter == null) {
            ShortenStaticSquaresAdapter(ShortenViewFactoryImpl(recyclerRecycledViewPool))
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
        val LAYOUT = R.layout.global_component_container_static_2square
    }
}
