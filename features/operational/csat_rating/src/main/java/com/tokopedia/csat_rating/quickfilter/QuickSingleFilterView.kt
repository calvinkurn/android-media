package com.tokopedia.csat_rating.quickfilter

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.csat_rating.R


open class QuickSingleFilterView : FrameLayout {
    private var defaultItem: QuickFilterItem? = null
    protected var recyclerView: RecyclerView? = null
    protected var adapterFilter: BaseQuickSingleFilterAdapter<ItemFilterViewHolder?>? = null
    private var listener: ActionListener? = null
    private var quickFilterAnalytics: QuickFilterAnalytics? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    fun setquickFilterListener(listener: QuickFilterAnalytics?) {
        quickFilterAnalytics = listener
    }

    protected fun init() {
        val rootView = inflate(context, layoutRes, this)
        recyclerView = rootView.findViewById<View>(R.id.rv_filter) as RecyclerView
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = getLayoutManager()
        initialAdapter()
        recyclerView?.adapter = adapterFilter
    }

    fun updateLayoutManager(layoutManager: RecyclerView.LayoutManager?) {
        recyclerView?.layoutManager = layoutManager
    }

    protected open fun getLayoutManager(): RecyclerView.LayoutManager? {
        return LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false)
    }


    @get:LayoutRes
    protected val layoutRes: Int
        protected get() = R.layout.csat_rating_widget_quick_filter

    protected open fun initialAdapter() {
        adapterFilter = QuickSingleFilterAdapter(quickSingleFilterListener())
    }

    fun renderFilter(quickFilterItems: List<QuickFilterItem>?) {
        adapterFilter?.addQuickFilterList(quickFilterItems)
    }

    protected open fun isMultipleSelectionAllowed(): Boolean = false

    protected fun quickSingleFilterListener(): QuickSingleFilterListener {
        return object : QuickSingleFilterListener {
            override fun selectFilter(quickFilterItem: QuickFilterItem?) {
                var totalFalse = 0
                val items = adapterFilter?.dataList
                if (items != null) {
                    for (i in items.indices) {
                        if (quickFilterItem?.type == items[i].type) {
                            if (items[i].isSelected) {
                                items[i].isSelected = false
                                totalFalse++
                            } else {
                                items[i].isSelected = true
                            }
                        } else if (!isMultipleSelectionAllowed()) {
                            totalFalse++
                            items[i].isSelected = false
                        }
                    }
                }

                if (totalFalse == items?.size && defaultItem != null) {
                    val indexOf = items.indexOf(defaultItem)
                    if (indexOf != -1) {
                        items[indexOf].isSelected = true
                        selectedFilter = items[indexOf].type
                        setSelectedFilterName(getselectedFilterName(items[indexOf].name))
                    }
                } else {
                    selectedFilter = getDefaultSelectedFilterType(quickFilterItem)
                    var name: String? = ""
                    if (quickFilterItem?.name != null) {
                        name = getselectedFilterName(quickFilterItem.name)
                    }
                    setSelectedFilterName(name)
                }
                adapterFilter?.notifyDataSetChanged()
            }
        }
    }


    private fun getselectedFilterName(filterName: String?): String? {
        var filterName = filterName
        if (filterName != null && filterName.contains("(")) {
            val i = filterName.indexOf("(")
            filterName = filterName.substring(0, i - 1)
        }
        return filterName
    }

    private fun setSelectedFilterName(name: String?) {
        if (quickFilterAnalytics != null) {
            quickFilterAnalytics?.setSelectFilterName(name)
        }
    }

    protected fun getDefaultSelectedFilterType(quickFilterItem: QuickFilterItem?): String? {
        return quickFilterItem?.type
    }

    fun setDefaultItem(defaultItem: QuickFilterItem?) {
        this.defaultItem = defaultItem
    }

    fun actionSelect(position: Int) {
        Handler().postDelayed({
            if (recyclerView != null) {
                val holder = recyclerView?.findViewHolderForAdapterPosition(position)
                if (holder != null) {
                    val holderItem = holder.itemView
                    holderItem.performClick()
                }
            }
        }, 100)
    }

    var selectedFilter: String?
        get() = if (defaultItem != null && defaultItem?.isSelected == true) {
            defaultItem?.type
        } else {
            var itemSelected = ""
            val list = adapterFilter?.dataList
            if (list != null) {
                for (i in list.indices) {
                    val quickFilterItem = list[i]
                    if (quickFilterItem.isSelected) {
                        itemSelected = quickFilterItem.type ?: ""
                        break
                    }
                }
            }

            itemSelected
        }
        private set(type) {
            if (listener != null) {
                listener?.selectFilter(type)
            }
        }
    val isAnyItemSelected: Boolean
        get() = if (defaultItem != null) {
            true
        } else {
            var isItemSelected = false
            val list = adapterFilter?.dataList
            if (list != null) {
                for (i in list.indices) {
                    val quickFilterItem = list[i]
                    if (quickFilterItem.isSelected) {
                        isItemSelected = true
                        break
                    }
                }
            }

            isItemSelected
        }

    interface ActionListener {
        fun selectFilter(typeFilter: String?)
    }

    interface QuickFilterAnalytics {
        fun setSelectFilterName(selectFilterName: String?)
    }
}