package com.tokopedia.filter.quick

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import com.tokopedia.filter.R
import com.tokopedia.filter.databinding.SortFilterQuickLayoutBinding
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifycomponents.toPx

class SortFilter: ConstraintLayout {

    private var binding: SortFilterQuickLayoutBinding? = null
    private var listener: Listener? = null
    private val itemListener = object: SortFilterItemViewHolder.Listener {
        override fun onItemClicked(sortFilterItem: SortFilterItem, position: Int) {
            listener?.onItemClicked(sortFilterItem, position)
        }

        override fun onItemChevronClicked(sortFilterItem: SortFilterItem, position: Int) {
            listener?.onItemChevronClicked(sortFilterItem, position)
        }
    }
    private val adapter = SortFilterAdapter(itemListener)
    private val layoutManager = LinearLayoutManager(context, HORIZONTAL, false)

    constructor(context: Context?): super(context) {
        init(context, null, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?): super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ): super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) {
        initBinding(context)
        initRecyclerView()
        initSortButton()
        initFilterButton()
    }

    private fun initBinding(context: Context?) {
        val view = inflate(
            context,
            R.layout.sort_filter_quick_layout,
            this,
        )

        binding = SortFilterQuickLayoutBinding.bind(view)
    }

    private fun initRecyclerView() {
        binding?.sortFilterQuickRecyclerView?.run {
            layoutManager = this@SortFilter.layoutManager
            adapter = this@SortFilter.adapter

            if (itemDecorationCount == 0)
                addItemDecoration(SortFilterItemDecoration())
        }
    }

    private fun initSortButton() {
        binding?.sortFilterSortChips?.run {
            val chipsLayout = View.inflate(context, R.layout.sort_filter_sort_chips_layout, null)
            addCustomView(chipsLayout)

            setOnClickListener { listener?.onSortClicked() }
        }
    }

    private fun initFilterButton() {
        binding?.sortFilterFilterChips?.run {
            val chipsLayout = View.inflate(context, R.layout.sort_filter_filter_chips_layout, null)
            addCustomView(chipsLayout)

            chipsLayout.findViewById<IconUnify>(R.id.sortFilterFilterIcon)?.visible()
            chipsLayout.findViewById<NotificationUnify>(R.id.sortFilterFilterNotification).gone()

            setOnClickListener { listener?.onFilterClicked() }
        }
    }

    fun addItem(sortFilterItemList: List<SortFilterItem>) {
        adapter.submitList(sortFilterItemList)
    }

    fun setSortFilterIndicatorCounter(showSortedIndicator: Boolean, filterCount: Int) {
        setSortIndicatorCounter(showSortedIndicator)
        setFilterIndicatorCounter(filterCount)
    }

    fun setSortIndicatorCounter(showSortedIndicator: Boolean) {
        binding
            ?.sortFilterSortChips
            ?.findViewById<NotificationUnify>(R.id.sortFilterSortNotification)
            ?.showWithCondition(showSortedIndicator)

        binding
            ?.sortFilterSortChips
            ?.findViewById<IconUnify>(R.id.sortFilterSortIcon)
            ?.isEnabled = showSortedIndicator
    }

    fun setFilterIndicatorCounter(filterCount: Int) {
        val filterIcon = binding
            ?.sortFilterFilterChips
            ?.findViewById<IconUnify>(R.id.sortFilterFilterIcon)

        val filterCountIndicator = binding
            ?.sortFilterFilterChips
            ?.findViewById<NotificationUnify>(R.id.sortFilterFilterNotification)

        filterIcon?.showWithCondition(filterCount == 0)
        filterCountIndicator?.shouldShowWithAction(filterCount > 0) {
            filterCountIndicator.setNotification(
                notif = filterCount.toString(),
                notificationType = NotificationUnify.COUNTER_TYPE,
                colorType = NotificationUnify.COLOR_SECONDARY,
            )
        }
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun scrollToPosition(position: Int) {
        binding?.sortFilterQuickRecyclerView?.post { layoutManager.scrollToPosition(position) }
    }

    interface Listener {
        fun onItemClicked(sortFilterItem: SortFilterItem, position: Int)
        fun onItemChevronClicked(sortFilterItem: SortFilterItem, position: Int)
        fun onFilterClicked()
        fun onSortClicked()
    }
}
