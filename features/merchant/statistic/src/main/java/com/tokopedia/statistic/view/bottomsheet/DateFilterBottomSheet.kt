package com.tokopedia.statistic.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.statistic.R
import com.tokopedia.statistic.common.StatisticPageHelper
import com.tokopedia.statistic.databinding.BottomsheetStcSelectDateRangeBinding
import com.tokopedia.statistic.view.adapter.DateFilterAdapter
import com.tokopedia.statistic.view.adapter.listener.DateFilterListener
import com.tokopedia.statistic.view.model.DateFilterItem
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created By @ilhamsuaib on 15/06/20
 */

class DateFilterBottomSheet : BaseBottomSheet<BottomsheetStcSelectDateRangeBinding>(),
    DateFilterListener {

    companion object {
        const val TAG = "DateFilterBottomSheet"
        private const val KEY_DATE_FILTERS = "date_filter_items"
        private const val KEY_IDENTIFIER_DESCRIPTION = "date_filter_identifier_desctiption"

        fun newInstance(
            dateFilters: List<DateFilterItem>,
            identifierDescription: String
        ): DateFilterBottomSheet {
            return DateFilterBottomSheet().apply {
                clearContentPadding = true
                arguments = Bundle().apply {
                    putParcelableArrayList(KEY_DATE_FILTERS, ArrayList(dateFilters))
                    putString(KEY_IDENTIFIER_DESCRIPTION, identifierDescription)
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
        binding = BottomsheetStcSelectDateRangeBinding.inflate(inflater).apply {
            setChild(root)
            setTitle(root.context.getString(R.string.stc_change_date_range))
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dismissBottomSheet(view)
    }

    override fun setupView() = binding?.run {
        rvStcDateRage.run {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }

        mAdapter?.clearAllElements()
        mAdapter?.addElement(items)

        showExclusiveIdentifier()
    }

    private fun showExclusiveIdentifier() {
        activity?.let {
            val userSession: UserSessionInterface = UserSession(it.applicationContext)
            val isRegularMerchant = StatisticPageHelper.getRegularMerchantStatus(userSession)
            binding?.rvStcDateRage?.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener{
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    return isRegularMerchant
                }
                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

                }
                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

                }
            })
            binding?.stcFilterExclusiveIdentifier?.isVisible = isRegularMerchant
            if (isRegularMerchant) {
                val identifierDescription = arguments
                    ?.getString(KEY_IDENTIFIER_DESCRIPTION).orEmpty()
                binding?.stcFilterExclusiveIdentifier?.setDescription(identifierDescription)
            }
        }
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