package com.tokopedia.mvc.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.campaign.utils.extension.attachDividerItemDecoration
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mvc.databinding.SmvcSelectRepeatPeriodBottomSheetBinding
import com.tokopedia.mvc.presentation.bottomsheet.adapter.SelectRepeatPeriodAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.mvc.R

class SelectRepeatPeriodBottomSheet : BottomSheetUnify(),
    SelectRepeatPeriodAdapter.OnPeriodClickedListener {

    companion object {
        private const val BUNDLE_KEY_SELECTED_REPEAT_PERIOD = "selected_repeat_period"
        private const val BUNDLE_KEY_IS_SHOW_TICKER = "is_show_ticker"

        fun newInstance(selectedRepeatPeriod: Int = Int.ONE, isShowTicker: Boolean): SelectRepeatPeriodBottomSheet {
            return SelectRepeatPeriodBottomSheet().apply {
                arguments = Bundle().apply {
                    putInt(BUNDLE_KEY_SELECTED_REPEAT_PERIOD, selectedRepeatPeriod)
                    putBoolean(BUNDLE_KEY_IS_SHOW_TICKER, isShowTicker)
                }
            }
        }
    }

    private var binding by autoClearedNullable<SmvcSelectRepeatPeriodBottomSheetBinding>()
    private val selectRepeatPeriodAdapter by lazy {
        SelectRepeatPeriodAdapter(this)
    }
    private var onPeriodClick: (Int) -> Unit = {}

    init {
        clearContentPadding = true
        isSkipCollapseState = true
        isKeyboardOverlap = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onPeriodClicked(period: Int) {
        onPeriodClick.invoke(period)
        dismiss()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        configTicker()
        setupRecyclerView()
    }

    private fun configTicker() {
        val isShowTicker = arguments?.getBoolean(BUNDLE_KEY_IS_SHOW_TICKER).orFalse()
        binding?.tickerInformation?.isVisible = isShowTicker
    }

    fun show(fragmentManager: FragmentManager, onPeriodClick: (Int) -> Unit) {
        this.onPeriodClick = onPeriodClick
        show(fragmentManager, tag)
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding = SmvcSelectRepeatPeriodBottomSheetBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        setTitle(getString(R.string.smvc_bottomsheet_select_repeat_period_title))
    }

    private fun setupRecyclerView() {
        val selectedRepeatPeriod = arguments?.getInt(BUNDLE_KEY_SELECTED_REPEAT_PERIOD).orZero()
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(activity)
            selectRepeatPeriodAdapter.setSelectedRepeatPeriod(selectedRepeatPeriod)
            adapter = selectRepeatPeriodAdapter
            itemAnimator = null
            attachDividerItemDecoration()
        }
    }
}
