package com.tokopedia.discovery2.viewcontrollers.adapter

import android.provider.Settings.Global
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.config.GlobalConfig
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.discoveryext.UIWidgetUninitializedException
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryListViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.DiscoveryHomeFactory
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

class DiscoveryRecycleAdapter(
    private val fragment: Fragment,
    private val parentComponent: AbstractViewHolder? = null
) :
    ListAdapter<ComponentsItem, AbstractViewHolder>(ComponentsDiffCallBacks()) {

    companion object {
        private var noOfObject = 0
    }

    private var mCurrentHeader: Pair<Int, RecyclerView.ViewHolder>? = null
    private var _componentList: ArrayList<ComponentsItem> = ArrayList()
    private var viewHolderListModel = ViewModelProviders.of(fragment).get(
        (
            DiscoveryListViewModel::class.java.canonicalName
                ?: ""
            ) + noOfObject++,
        DiscoveryListViewModel::class.java
    )
    val componentList: ArrayList<ComponentsItem>
        get() = _componentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context)
                .inflate(ComponentsList.values()[viewType].id, parent, false)
        return DiscoveryHomeFactory.createViewHolder(
            itemView,
            viewType,
            fragment
        ) as AbstractViewHolder
    }

    override fun onBindViewHolder(holder: AbstractViewHolder, position: Int) {
        try {
            if (_componentList.size <= position) {
                // tmp code need this handling to handle multithread enviorment
                return
            }
            if (mCurrentHeader?.first == position && mCurrentHeader?.second?.itemViewType == getItemViewType(
                    position
                ) &&
                (mCurrentHeader?.second as AbstractViewHolder).discoveryBaseViewModel != null
            ) {
                holder.bindView(
                    (mCurrentHeader?.second as AbstractViewHolder).discoveryBaseViewModel!!,
                    parentComponent
                )
            } else {
                with(
                    viewHolderListModel.getViewHolderModel(
                        DiscoveryHomeFactory.createViewModel(getItemViewType(position)),
                        _componentList[position],
                        position
                    )
                ) {
                    // id to help tracking automation, only to debug apk only
                    holder.bindView(this, parentComponent)
                    if (GlobalConfig.isAllowDebuggingTools()) {
                        if (this.components.id.isNotEmpty()) {
                            holder.itemView.contentDescription = "componentID-" + this.components.id
                        }
                    }
                }
            }
        } catch (e: UIWidgetUninitializedException) {
            e.componentName =
                "${_componentList[position].name ?: ""} - ${_componentList[position].pageEndPoint ?: ""}"
            val map = mutableMapOf<String, String>()
            map["type"] = "log"
            map["err"] = "uiWidgetComponent not initialized"
            map["page"] = _componentList[position].pageEndPoint
            map["compName"] = _componentList[position].name.orEmpty()
            ServerLogger.log(Priority.P2, "DISCO_DAGGER_VALIDATION", map)
            Utils.logException(e)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (_componentList.size <= position) {
            return 0
        }
        val id = DiscoveryHomeFactory.getComponentId(_componentList[position].name)
        return id ?: 0
    }

    fun <T : DiscoveryBaseViewModel> getFirstViewModel(
        viewModelClass: Class<T>
    ): DiscoveryBaseViewModel? {
        return viewHolderListModel.getFirstViewModel(viewModelClass)
    }

    fun addDataList(dataList: List<ComponentsItem>) {
        _componentList.clear()
        clearListViewModel()
        _componentList.addAll(dataList)
        submitList(dataList)
    }

    fun setDataList(dataList: ArrayList<ComponentsItem>?) {
        dataList?.let { componentItemsList ->
            addDataList(componentItemsList as List<ComponentsItem>)
        }
    }

    fun getTabItem(): DiscoveryBaseViewModel? {
        var tabsIndex = 0
        _componentList.forEachIndexed { index, item ->
            if (item.name == ComponentNames.Tabs.componentName) {
                tabsIndex = index
                return@forEachIndexed
            }
        }
        return getViewModelAtPosition(tabsIndex)
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder) {
        super.onViewAttachedToWindow(holder)
        try {
            holder.onViewAttachedToWindow()
        } catch (e: UninitializedPropertyAccessException) {
            Utils.logException(e)
        }
    }

    override fun onViewDetachedFromWindow(holder: AbstractViewHolder) {
        holder.onViewDetachedToWindow()
        try {
            super.onViewDetachedFromWindow(holder)
        } catch (e: UninitializedPropertyAccessException) {
            Utils.logException(e)
        }
    }

    private fun clearListViewModel() {
        viewHolderListModel.clearList()
    }

    fun getViewModelAtPosition(position: Int): DiscoveryBaseViewModel? {
        return viewHolderListModel.getViewModelAtPosition(position)
    }

    fun isStickyHeaderView(it: Int): Boolean {
        return DiscoveryHomeFactory.isStickyHeader(getItemViewType(it)) || (componentList.size > it && componentList[it].isSticky)
    }

    fun setCurrentHeader(currentHeader: Pair<Int, RecyclerView.ViewHolder>?) {
        mCurrentHeader = currentHeader
        if (currentHeader == null) {
            (fragment as DiscoveryFragment).stickyHeaderIsHidden()
        } else {
            (fragment as DiscoveryFragment).showingStickyHeader()
        }
    }

    fun getCurrentHeader() = mCurrentHeader

    fun notifySectionId(it: String) {
        (fragment as? DiscoveryFragment)?.updateSelectedSection(it)
    }

    fun notifyFestiveSectionId(sectionId: String, childComponentId: String) {
        (fragment as? DiscoveryFragment)?.rebindSelectedSection(sectionId, childComponentId)
    }
}

class ComponentsDiffCallBacks : DiffUtil.ItemCallback<ComponentsItem>() {
    override fun areItemsTheSame(oldItem: ComponentsItem, newItem: ComponentsItem): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: ComponentsItem, newItem: ComponentsItem): Boolean {
        return newItem == oldItem && oldItem.shouldRefreshComponent?.equals(false) ?: true
    }
}
