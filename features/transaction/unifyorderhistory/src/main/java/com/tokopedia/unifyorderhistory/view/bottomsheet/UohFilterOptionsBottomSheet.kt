package com.tokopedia.unifyorderhistory.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyorderhistory.databinding.BottomsheetOptionUohBinding
import com.tokopedia.unifyorderhistory.util.UohConsts
import com.tokopedia.unifyorderhistory.util.UohConsts.DATE_FORMAT_DDMMMYYYY
import com.tokopedia.unifyorderhistory.util.UohUtils
import com.tokopedia.unifyorderhistory.view.adapter.UohBottomSheetOptionAdapter
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*

/**
 * Created by fwidjaja on 02/10/21.
 */
class UohFilterOptionsBottomSheet : BottomSheetUnify() {
    private var listener: UohFilterOptionBottomSheetListener? = null
    private var adapterOptionBottomSheet: UohBottomSheetOptionAdapter? = null
    private var binding by autoClearedNullable<BottomsheetOptionUohBinding>()

    companion object {
        private const val TAG: String = "UohFilterOptionsBottomSheet"
        private const val TITLE_BOTTOMSHEET = "title_bottomsheet"
        private const val IS_BUTTON_APPLY_SHOW = "is_button_apply_show"

        @JvmStatic
        fun newInstance(title: String, isButtonApplyShow: Boolean): UohFilterOptionsBottomSheet {
            return UohFilterOptionsBottomSheet().apply {
                val bundle = Bundle()
                bundle.putString(TITLE_BOTTOMSHEET, title)
                bundle.putBoolean(IS_BUTTON_APPLY_SHOW, isButtonApplyShow)
                arguments = bundle
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BottomsheetOptionUohBinding.inflate(LayoutInflater.from(context), null, false)
        binding?.run {
            listener?.let { adapterOptionBottomSheet?.setActionListener(it) }
            rvOption.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = adapterOptionBottomSheet
            }
            val isButtonVisible = arguments?.getBoolean(IS_BUTTON_APPLY_SHOW) ?: false
            if (isButtonVisible) {
                btnApply.visible()
                btnApply.setOnClickListener { listener?.onClickApply() }
            } else {
                btnApply.gone()
            }
        }
        showCloseIcon = true
        showHeader = true
        setChild(binding?.root)
        setTitle(arguments?.getString(TITLE_BOTTOMSHEET) ?: "")
        setCloseClickListener { dismiss() }
    }

    fun hideChooseDate() {
        binding?.run {
            clChooseDate.gone()
        }
    }

    fun showChooseDate(chosenStartDate: GregorianCalendar, chosenEndDate: GregorianCalendar) {
        binding?.run {
            clChooseDate.visible()
            tfStartDate.textFieldInput.run {
                setText(UohUtils.calendarToStringFormat(chosenStartDate, DATE_FORMAT_DDMMMYYYY))
                isFocusable = false
                isClickable = true
                setOnClickListener { listener?.showDatePicker(UohConsts.START_DATE) }
            }
            tfEndDate.textFieldInput.run {
                setText(UohUtils.calendarToStringFormat(chosenEndDate, DATE_FORMAT_DDMMMYYYY))
                isFocusable = false
                isClickable = true
                setOnClickListener { listener?.showDatePicker(UohConsts.END_DATE) }
            }
        }
    }

    fun setStartDate(startDate: GregorianCalendar) {
        binding?.run {
            tfStartDate.textFieldInput.setText(UohUtils.calendarToStringFormat(startDate, DATE_FORMAT_DDMMMYYYY))
        }
    }

    fun setEndDate(endDate: GregorianCalendar) {
        binding?.run {
            tfEndDate.textFieldInput.setText(UohUtils.calendarToStringFormat(endDate, DATE_FORMAT_DDMMMYYYY))
        }
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    fun setListener(listener: UohFilterOptionBottomSheetListener) {
        this.listener = listener
    }

    fun setAdapter(adapter: UohBottomSheetOptionAdapter) {
        this.adapterOptionBottomSheet = adapter
    }

    interface UohFilterOptionBottomSheetListener {
        fun onClickApply()
        fun showDatePicker(flag: String)
        fun onOptionItemClick(label: String, value: String, filterType: Int)
    }
}
