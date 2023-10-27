package com.tokopedia.home_component.viewholders

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeComponentDynamicIconBinding
import com.tokopedia.home_component.decoration.DynamicIconBigItemDecoration
import com.tokopedia.home_component.decoration.DynamicIconSmallItemDecoration
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
        if (icons.isNotEmpty()) {
            adapter.updatePosition(absoluteAdapterPosition)
            setRecyclerView(element)
            launch {
                val layoutParams = iconRecyclerView?.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.height = element.dynamicIconUtil.findMaxHeight(icons, itemView.context)
                iconRecyclerView?.layoutParams = layoutParams
            }
            adapter.submitList(element)
            iconRecyclerView?.adapter = adapter
        }
    }

    private fun setRecyclerView(element: DynamicIconComponentDataModel) {
        setupLayoutManager(element)
        if(iconRecyclerView?.itemDecorationCount == 0) {
            val itemDecoration = if(element.type.isSmallIcons())
                DynamicIconSmallItemDecoration()
            else DynamicIconBigItemDecoration()
            iconRecyclerView?.addItemDecoration(itemDecoration)
        }
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

    private fun setupLayoutManager(element: DynamicIconComponentDataModel) {
        binding?.dynamicIconRecyclerView?.layoutManager = if(element.type.isSmallIcons())
            getSmallIconsLayoutManager(element)
        else getBigIconsLayoutManager(element)
    }

    private fun getSmallIconsLayoutManager(element: DynamicIconComponentDataModel): LayoutManager {
        return if(DeviceScreenInfo.isTablet(itemView.context)) {
            LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        } else {
            GridLayoutManager(itemView.context, element.numOfRows, GridLayoutManager.HORIZONTAL, false)
        }
    }

    private fun getBigIconsLayoutManager(element: DynamicIconComponentDataModel): LayoutManager {
        val isScrollItem = element.dynamicIconComponent.dynamicIcon.size > element.type.scrollableItemThreshold
        return if (isScrollItem) {
            LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        } else {
            GridLayoutManager(itemView.context, element.dynamicIconComponent.dynamicIcon.size, GridLayoutManager.VERTICAL, false)
        }
    }
}
