package com.tokopedia.home_component.viewholders

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeComponentDynamicIconBinding
import com.tokopedia.home_component.listener.DynamicIconComponentListener
import com.tokopedia.home_component.visitable.DynamicIconComponentDataModel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Created by Lukas on 1/8/21.
 */
class DynamicIconViewHolder(itemView: View, private val listener: DynamicIconComponentListener) :
    AbstractViewHolder<DynamicIconComponentDataModel>(itemView), CoroutineScope {

    private var binding: HomeComponentDynamicIconBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_dynamic_icon
        private const val MARGIN_TOP = 8
        private const val MARGIN_BOTTOM_BIG = 16
        private const val MARGIN_BOTTOM_SMALL = 12
        private const val PADDING_HORIZONTAL = 8
    }

    private val adapter = DynamicIconAdapter(listener)
    private var iconRecyclerView: RecyclerView? = null
    private val masterJob = SupervisorJob()

    override val coroutineContext: CoroutineContext = masterJob + Dispatchers.Main

    override fun bind(element: DynamicIconComponentDataModel) {
        setupDynamicIcon(element)
        setupImpression(element)
    }

    private fun setupDynamicIcon(element: DynamicIconComponentDataModel) {
        val icons = element.dynamicIconComponent.dynamicIcon
        iconRecyclerView = itemView.findViewById(R.id.dynamic_icon_recycler_view)
        iconRecyclerView?.setPadding(PADDING_HORIZONTAL.toPx(), Int.ZERO, PADDING_HORIZONTAL.toPx(), Int.ZERO)
        if (icons.isNotEmpty()) {
            adapter.updatePosition(absoluteAdapterPosition)
            setRecyclerView(element)
            launch {
                val maxHeight = if(element.type.isSmallIcons()) {
                    ViewGroup.LayoutParams.WRAP_CONTENT
                } else {
                    element.dynamicIconUtil.findMaxHeight(icons, itemView.context)
                }
                val layoutParams = iconRecyclerView?.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.height = maxHeight
                iconRecyclerView?.layoutParams = layoutParams
            }
            adapter.submitList(element)
            val layoutParams = iconRecyclerView?.layoutParams as ConstraintLayout.LayoutParams
            val marginBottom = if(element.type.isSmallIcons()) MARGIN_BOTTOM_SMALL else MARGIN_BOTTOM_BIG
            layoutParams.setMargins(
                Int.ZERO,
                MARGIN_TOP.toPx(),
                Int.ZERO,
                marginBottom.toPx()
            )
            iconRecyclerView?.layoutParams = layoutParams
            iconRecyclerView?.adapter = adapter
        }
    }

    private fun setRecyclerView(element: DynamicIconComponentDataModel) {
        val icons = element.dynamicIconComponent.dynamicIcon
        val isScrollItem = icons.size > element.scrollableItemThreshold
        setupLayoutManager(
            isScrollItem = isScrollItem,
            spanCount = icons.size,
            element
        )
        iconRecyclerView?.clearOnScrollListeners()
        iconRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                listener.onIconScroll(absoluteAdapterPosition)
            }
        })
    }

    private fun setupImpression(element: DynamicIconComponentDataModel) {
        itemView.addOnImpressionListener(element) {
            if (!element.isCache) listener.onIconChannelImpressed(element, absoluteAdapterPosition)
        }
    }

    private fun setupLayoutManager(isScrollItem: Boolean, spanCount: Int, element: DynamicIconComponentDataModel) {
        binding?.dynamicIconRecyclerView?.layoutManager = if (isScrollItem) {
            if(element.type.isSmallIcons()) {
                GridLayoutManager(itemView.context, element.numOfRows, GridLayoutManager.HORIZONTAL, false)
            } else {
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            }
        } else {
            GridLayoutManager(itemView.context, spanCount, GridLayoutManager.VERTICAL, false)
        }
    }
}
