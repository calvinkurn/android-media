package com.tokopedia.home_component.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.HomeComponentRollenceController
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeComponentDynamicIconBinding
import com.tokopedia.home_component.decoration.CommonSpacingDecoration
import com.tokopedia.home_component.listener.DynamicIconComponentListener
import com.tokopedia.home_component.model.DynamicIconComponent
import com.tokopedia.home_component.util.DynamicIconsMacroUtil
import com.tokopedia.home_component.visitable.DynamicIconComponentDataModel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
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
    private var isUsingMacroInteraction: Boolean = false

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_dynamic_icon
        const val SCROLLABLE_ITEM = 5
        const val SCROLLABLE_ITEM_MACRO = 6
        private const val MARGIN_TOP_MACRO = 6
        private const val MARGIN_BOTTOM_MACRO = 4
        private const val MARGIN_VERTICAL_DEFAULT = 12
        private const val MARGIN_HORIZONTAL_BETWEEN_CARD_MACRO = 0
        private const val MARGIN_HORIZONTAL_BETWEEN_CARD = 8
    }

    private val adapter = DynamicIconAdapter(listener, isRevamp)
    private val adapterMacro = DynamicIconMacroAdapter(listener, isRevamp)
    private var iconRecyclerView: RecyclerView? = null
    private val masterJob = SupervisorJob()

    override val coroutineContext: CoroutineContext = masterJob + Dispatchers.Main

    override fun bind(element: DynamicIconComponentDataModel) {
        isUsingMacroInteraction =
            HomeComponentRollenceController.isHomeComponentDynamicIconsUsingRollenceVariant()
        setupDynamicIcon(element)
        setupImpression(element)
    }

    private fun setupDynamicIcon(element: DynamicIconComponentDataModel) {
        val icons = element.dynamicIconComponent.dynamicIcon
        iconRecyclerView = itemView.findViewById(R.id.dynamic_icon_recycler_view)
        if (icons.isNotEmpty()) {
            if (isUsingMacroInteraction) {
                adapterMacro.updatePosition(absoluteAdapterPosition)
                adapterMacro.setType(element.type)
                setRecyclerView(icons)
                launch {
                    val maximalTitleHeight =
                        DynamicIconsMacroUtil.findMaxDynamicIconsMacro(icons, itemView.context)
                    val layoutParams = iconRecyclerView?.layoutParams as RecyclerView.LayoutParams
                    layoutParams.height = maximalTitleHeight
                    iconRecyclerView?.layoutParams = layoutParams
                }
                adapterMacro.submitList(element)
                val layoutParams = iconRecyclerView?.layoutParams as RecyclerView.LayoutParams
                layoutParams.setMargins(Int.ZERO, MARGIN_TOP_MACRO, Int.ZERO, MARGIN_BOTTOM_MACRO)
                iconRecyclerView?.layoutParams = layoutParams

                if (iconRecyclerView?.itemDecorationCount == Int.ZERO) {
                    iconRecyclerView?.addItemDecoration(
                        CommonSpacingDecoration(
                            MARGIN_HORIZONTAL_BETWEEN_CARD_MACRO.toPx()
                        )
                    )
                }
                iconRecyclerView?.adapter = adapterMacro
            } else {
                adapter.submitList(element)
                adapter.updatePosition(absoluteAdapterPosition)
                adapter.setType(element.type)
                val layoutParams = iconRecyclerView?.layoutParams as RecyclerView.LayoutParams
                layoutParams.setMargins(Int.ZERO, MARGIN_VERTICAL_DEFAULT, Int.ZERO, MARGIN_VERTICAL_DEFAULT)
                iconRecyclerView?.layoutParams = layoutParams
                iconRecyclerView?.addItemDecoration(
                    CommonSpacingDecoration(
                        MARGIN_HORIZONTAL_BETWEEN_CARD.toPx()
                    )
                )
                iconRecyclerView?.adapter = adapter
                setRecyclerView(icons)
            }
        }
    }

    private fun setRecyclerView(icons: List<DynamicIconComponent.DynamicIcon>) {
        setupLayoutManager(
            isScrollItem = icons.size > if (isUsingMacroInteraction) SCROLLABLE_ITEM_MACRO else SCROLLABLE_ITEM,
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

    internal inner class DynamicIconAdapter(private val listener: DynamicIconComponentListener, private val isRevamp: Boolean = false) :
        RecyclerView.Adapter<DynamicIconItemViewHolder>() {
        private val categoryList = mutableListOf<DynamicIconComponent.DynamicIcon>()
        private var position: Int = 0
        private var type: Int = 1
        private var isCache: Boolean = false

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): DynamicIconItemViewHolder {
            return DynamicIconItemViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    if (isRevamp) DynamicIconItemViewHolder.LAYOUT_REVAMP else DynamicIconItemViewHolder.LAYOUT,
                    parent,
                    false
                ),
                listener
            )
        }

        override fun onBindViewHolder(holder: DynamicIconItemViewHolder, position: Int) {
            categoryList.getOrNull(position)?.let {
                holder.bind(
                    it,
                    categoryList.size > SCROLLABLE_ITEM,
                    this.position,
                    type,
                    isCache
                )
            }
        }

        override fun getItemCount(): Int {
            return categoryList.size
        }

        fun setType(type: Int) {
            this.type = type
        }

        fun updatePosition(position: Int) {
            this.position = position
        }

        fun submitList(list: DynamicIconComponentDataModel) {
            categoryList.clear()
            categoryList.addAll(list.dynamicIconComponent.dynamicIcon)
            this.isCache = list.isCache
        }
    }

    internal class DynamicIconItemViewHolder(
        itemView: View,
        private val listener: DynamicIconComponentListener
    ) : RecyclerView.ViewHolder(itemView) {
        var iconTvName: Typography? = null
        var iconImageView: ImageUnify? = null
        var iconContainer: LinearLayout? = null

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.home_component_dynamic_icon_item
            val LAYOUT_REVAMP = R.layout.home_component_dynamic_icon_revamp_item
            private const val ONE_LINE = 1
            private const val TWO_LINES = 2
        }

        fun bind(
            item: DynamicIconComponent.DynamicIcon,
            isScrollable: Boolean,
            parentPosition: Int,
            type: Int,
            isCache: Boolean
        ) {
            iconTvName = itemView.findViewById(R.id.dynamic_icon_typography)
            iconImageView = itemView.findViewById(R.id.dynamic_icon_image_view)
            iconContainer = itemView.findViewById(R.id.dynamic_icon_container)

            iconTvName?.text = item.name
            iconImageView?.setImageUrl(item.imageUrl)
            iconContainer?.layoutParams = ViewGroup.LayoutParams(
                if (isScrollable) ViewGroup.LayoutParams.WRAP_CONTENT else ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            iconTvName?.maxLines = if (item.withBackground) TWO_LINES else ONE_LINE
            itemView.setOnClickListener {
                listener.onClickIcon(item, parentPosition, absoluteAdapterPosition, type)
            }

            if (!isCache) {
                itemView.addOnImpressionListener(item) {
                    listener.onImpressIcon(
                        dynamicIcon = item,
                        iconPosition = absoluteAdapterPosition,
                        parentPosition = parentPosition,
                        type = type,
                        view = itemView
                    )
                }
            }
        }
    }
}
