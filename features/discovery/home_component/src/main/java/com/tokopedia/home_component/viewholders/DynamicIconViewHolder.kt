package com.tokopedia.home_component.viewholders

import android.os.Looper
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
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.home_component.visitable.DynamicIconComponentDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.unifycomponents.ImageUnify
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
class DynamicIconViewHolder(itemView: View, private val listener: DynamicIconComponentListener) :
    AbstractViewHolder<DynamicIconComponentDataModel>(itemView), CoroutineScope {

    private var binding: HomeComponentDynamicIconBinding? by viewBinding()
    private var isUsingMacroInteraction: Boolean = false

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_dynamic_icon
        const val SCROLLABLE_ITEM = 5
    }

    private val adapter = DynamicIconAdapter(listener)
    private val adapterMacro = DynamicIconMacroAdapter(listener)
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
                        DynamicIconsMacroUtil.findMaxHeightTitleText(icons, itemView.context)
                    adapterMacro.setTitleHeight(maximalTitleHeight)
                    adapterMacro.submitList(element)
                    val layoutParams = iconRecyclerView?.layoutParams as RecyclerView.LayoutParams
                    layoutParams.setMargins(0, 6, 0, 4)
                    iconRecyclerView?.layoutParams = layoutParams

                    if (iconRecyclerView?.itemDecorationCount == 0) {
                        iconRecyclerView?.addItemDecoration(
                            CommonSpacingDecoration(
                                0f.toDpInt()
                            )
                        )
                    }
                    iconRecyclerView?.adapter = adapterMacro
                }
            } else {
                adapter.submitList(element)
                adapter.updatePosition(absoluteAdapterPosition)
                adapter.setType(element.type)
                val layoutParams = iconRecyclerView?.layoutParams as RecyclerView.LayoutParams
                layoutParams.setMargins(0, 12, 0, 12)
                iconRecyclerView?.layoutParams = layoutParams
                iconRecyclerView?.addItemDecoration(
                    CommonSpacingDecoration(
                        8f.toDpInt()
                    )
                )
                iconRecyclerView?.adapter = adapter
                setRecyclerView(icons)
            }
//            if (iconRecyclerView?.itemDecorationCount == 0) {
//                if (isUsingMacroInteraction) {
//                    iconRecyclerView?.addItemDecoration(
//                        CommonSpacingDecoration(
//                            0f.toDpInt()
//                        )
//                    )
//                } else {
//                    iconRecyclerView?.addItemDecoration(
//                        CommonSpacingDecoration(
//                            8f.toDpInt()
//                        )
//                    )
//                }
//            }
        }
//        }
    }

    private fun setRecyclerView(icons: List<DynamicIconComponent.DynamicIcon>) {
        setupLayoutManager(
            isScrollItem = icons.size > SCROLLABLE_ITEM,
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

    internal inner class DynamicIconAdapter(private val listener: DynamicIconComponentListener) :
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
                    if (isUsingMacroInteraction) DynamicIconItemViewHolder.LAYOUT else DynamicIconItemViewHolder.LAYOUT_EXPERIMENT,
                    parent,
                    false
                ),
                listener,
                isUsingMacroInteraction
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
        private val listener: DynamicIconComponentListener,
        private val isUsingMicroInteraction: Boolean
    ) : RecyclerView.ViewHolder(itemView) {
        var iconTvName: Typography? = null
        var iconImageView: ImageUnify? = null
        var iconContainer: LinearLayout? = null

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.home_component_dynamic_icon_item
            val LAYOUT_EXPERIMENT = R.layout.home_component_dynamic_icon_item_interaction
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
            iconTvName?.maxLines = if (item.withBackground) 2 else 1
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
