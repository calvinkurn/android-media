package com.tokopedia.sellerhomecommon.presentation.view.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.common.DateFilterUtil
import com.tokopedia.sellerhomecommon.databinding.ShcBottomSheetSelectDateRangeBinding
import com.tokopedia.sellerhomecommon.presentation.adapter.DateFilterAdapter
import com.tokopedia.sellerhomecommon.presentation.adapter.listener.DateFilterListener
import com.tokopedia.sellerhomecommon.presentation.model.DateFilterItem
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * Created by @ilhamsuaib on 09/02/22.
 */

class DateFilterBottomSheet : BaseBottomSheet<ShcBottomSheetSelectDateRangeBinding>(),
    DateFilterListener {

    companion object {
        const val TAG = "DateFilterBottomSheet"
        private const val KEY_DATE_FILTERS = "date_filter_items"

        fun newInstance(
            context: Context
        ): DateFilterBottomSheet {
            val dateFilters = DateFilterUtil.FilterList
                .getCalendarPickerFilterList(context)
            return DateFilterBottomSheet().apply {
                clearContentPadding = true
                arguments = Bundle().apply {
                    putParcelableArrayList(KEY_DATE_FILTERS, ArrayList(dateFilters))
                }
            }
        }
    }

    private var fm by autoClearedNullable<FragmentManager>()
    private var applyChangesCallback: ((DateFilterItem) -> Unit)? = null
    private val mAdapter: DateFilterAdapter? by lazy {
        DateFilterAdapter(this, fm ?: return@lazy null)
    }
    private val items: List<DateFilterItem> by lazy {
        arguments?.getParcelableArrayList<DateFilterItem>(KEY_DATE_FILTERS).orEmpty()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ShcBottomSheetSelectDateRangeBinding.inflate(inflater).apply {
            setChild(root)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(getString(R.string.shc_change_date_range))
        dismissBottomSheet(view)
    }

    override fun setupView() = binding?.run {
        showFilterItems()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemDateRangeClick(model: DateFilterItem) {
        items.forEach {
            if (it != model) {
                it.isSelected = false
            }
        }
        mAdapter?.notifyDataSetChanged()
    }

    override fun onApplyDateFilter() {
        val selectedItem = items.firstOrNull { it.isSelected } ?: return
        applyChangesCallback?.invoke(selectedItem)
        dismissAllowingStateLoss()
    }

    override fun showDateTimePickerBottomSheet(bottomSheet: BottomSheetUnify, tag: String) {
        if (isActivityResumed()) {
            fm?.let {
                bottomSheet.show(it, tag)
            }
        }
    }

    override fun dismissDateFilterBottomSheet() {
        dismiss()
    }

    override fun showDateFilterBottomSheet() {
        show()
    }

    fun setFragmentManager(fm: FragmentManager): DateFilterBottomSheet {
        this.fm = fm
        return this
    }

    fun setOnApplyChanges(callback: (DateFilterItem) -> Unit): DateFilterBottomSheet {
        this.applyChangesCallback = callback
        return this
    }

    fun show() {
        fm?.let {
            show(it, TAG)
        }
    }

    private fun showFilterItems() {
        binding?.run {
            rvShcDateRage.run {
                layoutManager = LinearLayoutManager(context)
                adapter = mAdapter
            }

            mAdapter?.clearAllElements()
            mAdapter?.addElement(items)
        }
    }

    private fun dismissBottomSheet(view: View) {
        view.post {
            if (mAdapter == null && isVisible) {
                dismiss()
            }
        }
    }

    private fun isActivityResumed(): Boolean {
        val state = (activity as? AppCompatActivity)?.lifecycle?.currentState
        return state == Lifecycle.State.STARTED || state == Lifecycle.State.RESUMED
    }
}