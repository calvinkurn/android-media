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
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.sellerhomecommon.presentation.adapter.DateFilterAdapter
import com.tokopedia.sellerhomecommon.presentation.adapter.listener.DateFilterListener
import com.tokopedia.sellerhomecommon.presentation.model.DateFilterItem
import com.tokopedia.statistic.R
import com.tokopedia.statistic.analytics.StatisticTracker
import com.tokopedia.statistic.common.StatisticPageHelper
import com.tokopedia.statistic.databinding.BottomsheetStcSelectDateRangeBinding
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

        fun newInstance(
            dateFilters: List<DateFilterItem>,
            identifierDescription: String
        ): DateFilterBottomSheet {
            return DateFilterBottomSheet().apply {
                clearContentPadding = true
                showHeader = false
                showCloseIcon = false
                arguments = Bundle().apply {
                    putParcelableArrayList(KEY_DATE_FILTERS, ArrayList(dateFilters))
                    putString(KEY_IDENTIFIER_DESCRIPTION, identifierDescription)
                }
            }
        }
    }

    private var applyChangesCallback: ((DateFilterItem) -> Unit)? = null
    private val mAdapter: DateFilterAdapter? by lazy {
        DateFilterAdapter(this, childFragmentManager)
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
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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

        showExclusiveIdentifier()
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

    private fun showExclusiveIdentifier() = binding?.run {
        activity?.let {
            val userSession: UserSessionInterface = UserSession(it.applicationContext)
            val isRegularMerchant = StatisticPageHelper.getRegularMerchantStatus(userSession)
            rvStcDateRage.addOnItemTouchListener(object :
                RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    return isRegularMerchant
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

                }

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

                }
            })
            tvStcExclusiveFeature.isVisible = !isRegularMerchant
            icStcExclusiveFeature.isVisible = !isRegularMerchant
            stcFilterExclusiveIdentifier.isVisible = isRegularMerchant
            stcFilterExclusiveIdentifier.setOnCtaClickListener {
                StatisticTracker.sendClickEventOnCloseDateFilter(userSession.userId)
                dismiss()
            }
            if (isRegularMerchant) {
                val identifierDescription = arguments
                    ?.getString(KEY_IDENTIFIER_DESCRIPTION).orEmpty()
                stcFilterExclusiveIdentifier.setDescription(identifierDescription)
                viewStcFilterHeader.setMargin(0, 0, 0, 0)
                viewStcFilterHeader.setBackgroundResource(R.drawable.bg_stc_filter_header_rm)
            } else {
                val topMargin = it.dpToPx(36).toInt()
                viewStcFilterHeader.setMargin(0, topMargin, 0, 0)
                viewStcFilterHeader.setBackgroundResource(R.drawable.bg_stc_filter_header)
            }

            if (tvStcExclusiveFeature.isVisible) {
                StatisticTracker.sendImpressionExclusiveFeatureDateFilter(userSession.userId)
            }
            if (stcFilterExclusiveIdentifier.isVisible) {
                StatisticTracker.sendImpressionExclusiveIdentifierDateFilter(userSession.userId)
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
