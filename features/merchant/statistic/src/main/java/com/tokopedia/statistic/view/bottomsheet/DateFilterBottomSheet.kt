package com.tokopedia.statistic.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.statistic.R
import com.tokopedia.statistic.view.adapter.DateFilterAdapter
import com.tokopedia.statistic.view.adapter.listener.DateFilterListener
import com.tokopedia.statistic.view.model.DateFilterItem
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottomsheet_stc_select_date_range.view.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created By @ilhamsuaib on 15/06/20
 */

class DateFilterBottomSheet : BottomSheetUnify(), DateFilterListener {

    companion object {
        const val TAG = "DateFilterBottomSheet"
        private const val KEY_DATE_FILTERS = "date_filter_items"

        fun newInstance(dateFilters: List<DateFilterItem>): DateFilterBottomSheet {
            return DateFilterBottomSheet().apply {
                clearContentPadding = true
                arguments = Bundle().apply {
                    putParcelableArrayList(KEY_DATE_FILTERS, ArrayList(dateFilters))
                }
            }
        }
    }

    private var fm: FragmentManager? = null
    private var applyChangesCallback: ((DateFilterItem) -> Unit)? = null
    private val mAdapter: DateFilterAdapter? by lazy {
        DateFilterAdapter(this, fm
                ?: return@lazy null)
    }
    private val items: List<DateFilterItem> by lazy {
        arguments?.getParcelableArrayList<DateFilterItem>(KEY_DATE_FILTERS).orEmpty()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, com.tokopedia.unifycomponents.R.style.UnifyBottomSheetNotOverlapStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setChild(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        dismissBottomSheet(view)
    }

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

    private fun setupView() = view?.run {
        rvStcDateRage.run {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }

        mAdapter?.clearAllElements()
        mAdapter?.addElement(items)
    }

    private fun setChild(inflater: LayoutInflater, container: ViewGroup?) {
        val child: View = inflater.inflate(R.layout.bottomsheet_stc_select_date_range, container, false)
        setTitle(child.context.getString(R.string.stc_change_date_range))
        setChild(child)
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