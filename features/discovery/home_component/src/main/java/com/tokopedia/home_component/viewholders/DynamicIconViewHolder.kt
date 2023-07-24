package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeComponentDynamicIconBinding
import com.tokopedia.home_component.decoration.CommonSpacingDecoration
import com.tokopedia.home_component.listener.DynamicIconComponentListener
import com.tokopedia.home_component.model.DynamicIconComponent
import com.tokopedia.home_component.util.DynamicIconsUtil
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
class DynamicIconViewHolder(itemView: View, private val listener: DynamicIconComponentListener, private val isRevamp: Boolean = false) :
    AbstractViewHolder<DynamicIconComponentDataModel>(itemView), CoroutineScope {

    private var binding: HomeComponentDynamicIconBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_dynamic_icon
        const val SCROLLABLE_ITEM = 6
        const val SCROLLABLE_ITEM_REVAMP = 3
        private const val MARGIN_TOP = 6
        private const val MARGIN_BOTTOM = 8
        private const val MARGIN_TOP_REVAMP = 8
        private const val MARGIN_BOTTOM_REVAMP = 16
        private const val MARGIN_HORIZONTAL_BETWEEN_CARD = 0
        private const val PADDING_HORIZONTAL_REVAMP = 8
        private const val PADDING_HORIZONTAL_OLD = 12
    }

    private val adapter = DynamicIconAdapter(listener, isRevamp)
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
        if (isRevamp) {
            iconRecyclerView?.setPadding(PADDING_HORIZONTAL_REVAMP.toPx(), Int.ZERO, PADDING_HORIZONTAL_REVAMP.toPx(), Int.ZERO)
        } else {
            iconRecyclerView?.setPadding(PADDING_HORIZONTAL_OLD.toPx(), Int.ZERO, PADDING_HORIZONTAL_OLD.toPx(), Int.ZERO)
        }
        if (icons.isNotEmpty()) {
            adapter.updatePosition(absoluteAdapterPosition)
            adapter.setType(element.type)
            setRecyclerView(icons)
            launch {
                val maximalTitleHeight =
                    if (isRevamp) {
                        DynamicIconsUtil.findMaxDynamicIconsRevamp(
                            icons,
                            itemView.context
                        )
                    } else {
                        DynamicIconsUtil.findMaxDynamicIcons(
                            icons,
                            itemView.context
                        )
                    }
                val layoutParams = iconRecyclerView?.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.height = maximalTitleHeight
                iconRecyclerView?.layoutParams = layoutParams
            }
            adapter.submitList(element)
            val layoutParams = iconRecyclerView?.layoutParams as ConstraintLayout.LayoutParams
            if (isRevamp) {
                layoutParams.setMargins(
                    Int.ZERO,
                    MARGIN_TOP_REVAMP.toPx(),
                    Int.ZERO,
                    MARGIN_BOTTOM_REVAMP.toPx()
                )
            } else {
                layoutParams.setMargins(
                    Int.ZERO,
                    MARGIN_TOP.toPx(),
                    Int.ZERO,
                    MARGIN_BOTTOM.toPx()
                )
            }
            iconRecyclerView?.layoutParams = layoutParams

            if (iconRecyclerView?.itemDecorationCount == Int.ZERO) {
                iconRecyclerView?.addItemDecoration(
                    CommonSpacingDecoration(MARGIN_HORIZONTAL_BETWEEN_CARD.toPx())
                )
            }
            iconRecyclerView?.adapter = adapter
        }
    }

    private fun setRecyclerView(icons: List<DynamicIconComponent.DynamicIcon>) {
        val isScrollItem =
            icons.size > if (isRevamp) {
                SCROLLABLE_ITEM_REVAMP
            } else SCROLLABLE_ITEM
        setupLayoutManager(
            isScrollItem = isScrollItem,
            spanCount = icons.size
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

    private fun setupLayoutManager(isScrollItem: Boolean, spanCount: Int) {
        if (isScrollItem) {
            binding?.dynamicIconRecyclerView?.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        } else {
            binding?.dynamicIconRecyclerView?.layoutManager =
                GridLayoutManager(itemView.context, spanCount, GridLayoutManager.VERTICAL, false)
        }
    }
}
