package com.tokopedia.statistic.view.bottomsheet

import android.annotation.SuppressLint
import android.graphics.Color
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
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.presentation.adapter.DateFilterAdapter
import com.tokopedia.sellerhomecommon.presentation.adapter.listener.DateFilterListener
import com.tokopedia.sellerhomecommon.presentation.model.DateFilterItem
import com.tokopedia.statistic.R
import com.tokopedia.statistic.analytics.StatisticTracker
import com.tokopedia.statistic.databinding.BottomsheetStcSelectDateRangeBinding
import com.tokopedia.statistic.view.activity.StatisticActivity
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created By @ilhamsuaib on 15/06/20
 */

class DateFilterBottomSheet : BaseBottomSheet<BottomsheetStcSelectDateRangeBinding>(),
    DateFilterListener {

    companion object {
        const val TAG = "DateFilterBottomSheet"
        private const val KEY_DATE_FILTERS = "date_filter_items"
        private const val KEY_IDENTIFIER_DESCRIPTION = "date_filter_identifier_desctiption"
        private const val KEY_PAGE_SOURCE = "page_source"
        private const val TOP_MARGIN = 36

        fun newInstance(
            dateFilters: List<DateFilterItem>,
            identifierDescription: String,
            pageSource: String
        ): DateFilterBottomSheet {
            return DateFilterBottomSheet().apply {
                clearContentPadding = true
                showHeader = false
                showCloseIcon = false
                arguments = Bundle().apply {
                    putParcelableArrayList(KEY_DATE_FILTERS, ArrayList(dateFilters))
                    putString(KEY_IDENTIFIER_DESCRIPTION, identifierDescription)
                    putString(KEY_PAGE_SOURCE, pageSource)
                }
            }
        }
    }

    private var applyChangesCallback: ((DateFilterItem) -> Unit)? = null
    private var mAdapter: DateFilterAdapter? = null
    private var items: List<DateFilterItem> = emptyList()
    private var pageSource = String.EMPTY

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetStcSelectDateRangeBinding.inflate(inflater).apply {
            setChild(root)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initVars()

        super.onViewCreated(view, savedInstanceState)
        dismissBottomSheet(view)
    }

    override fun setupView() = binding?.run {
        root.setBackgroundColor(Color.TRANSPARENT)
        view?.setBackgroundColor(Color.TRANSPARENT)
        showFilterItems()

        icStcCloseDateFilter.setOnClickListener {
            dismiss()
        }

        showPaywallAccess()
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
            bottomSheet.show(childFragmentManager, tag)
        }
    }

    override fun dismissDateFilterBottomSheet() {
        view?.gone()
    }

    override fun showDateFilterBottomSheet() {
        view?.visible()
    }

    fun setOnApplyChanges(callback: (DateFilterItem) -> Unit): DateFilterBottomSheet {
        this.applyChangesCallback = callback
        return this
    }

    fun show(fm: FragmentManager) {
        if (!fm.isStateSaved) {
            show(fm, TAG)
        }
    }

    private fun initVars() {
        this.mAdapter = DateFilterAdapter(this, childFragmentManager)
        this.pageSource = arguments?.getString(KEY_PAGE_SOURCE, String.EMPTY).orEmpty()
        this.items = arguments?.getParcelableArrayList<DateFilterItem>(KEY_DATE_FILTERS).orEmpty()
    }

    private fun showFilterItems() {
        binding?.run {
            rvStcDateRage.run {
                layoutManager = LinearLayoutManager(context)
                adapter = mAdapter
            }

            mAdapter?.clearAllElements()
            mAdapter?.addElement(items)
        }
    }

    private fun showPaywallAccess() = binding?.run {
        (activity as? StatisticActivity)?.let {
            val userSession: UserSessionInterface = UserSession(it.applicationContext)
            val hasAccess = it.isPaywallAccessGranted
            rvStcDateRage.addOnItemTouchListener(object :
                RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    return !hasAccess
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

                }

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

                }
            })
            tvStcExclusiveFeature.isVisible = hasAccess
            icStcExclusiveFeature.isVisible = hasAccess
            stcFilterExclusiveIdentifier.isVisible = !hasAccess
            stcFilterExclusiveIdentifier.setOnCtaClickListener {
                StatisticTracker.sendClickEventOnCloseDateFilter(pageSource)
                dismiss()
            }
            if (!hasAccess) {
                val identifierDescription = arguments
                    ?.getString(KEY_IDENTIFIER_DESCRIPTION).orEmpty()
                stcFilterExclusiveIdentifier.setDescription(identifierDescription)
                stcFilterExclusiveIdentifier.setPageSource(pageSource)
                viewStcFilterHeader.setMargin(Int.ZERO, Int.ZERO, Int.ZERO, Int.ZERO)
                viewStcFilterHeader.setBackgroundResource(R.drawable.bg_stc_filter_header_rm)
            } else {
                val topMargin = it.dpToPx(TOP_MARGIN).toInt()
                viewStcFilterHeader.setMargin(Int.ZERO, topMargin, Int.ZERO, Int.ZERO)
                viewStcFilterHeader.setBackgroundResource(R.drawable.bg_stc_filter_header)
            }

            if (tvStcExclusiveFeature.isVisible) {
                StatisticTracker.sendImpressionExclusiveFeatureDateFilter(userSession.userId)
            }
            if (stcFilterExclusiveIdentifier.isVisible) {
                StatisticTracker.sendImpressionExclusiveIdentifierDateFilter(pageSource)
            }
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
