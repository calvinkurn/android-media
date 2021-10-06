package com.tokopedia.unifyorderhistory.view.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.tokopedia.unifyorderhistory.view.fragment.UohListFragment
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*

/**
 * Created by fwidjaja on 02/10/21.
 */
class UohFilterOptionsBottomSheet : BottomSheetUnify() {
    private var actionListener: ActionListener? = null
    private var bottomSheetFilterOptions : BottomSheetUnify? = null
    private var binding by autoClearedNullable<BottomsheetOptionUohBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initView() {
        binding = BottomsheetOptionUohBinding.inflate(LayoutInflater.from(context), null, false)
    }

    fun show(context: Context, fragmentManager: FragmentManager, adapter: UohBottomSheetOptionAdapter, title: String){
        bottomSheetFilterOptions = BottomSheetUnify()
        binding?.run {
            rvOption.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvOption.adapter = adapter
            btnApply.setOnClickListener { actionListener?.onClickApply() }
        }
        bottomSheetFilterOptions?.run {
            showCloseIcon = true
            showHeader = true
            setChild(binding?.root)
            setTitle(title)
            setCloseClickListener { dismiss() }
        }
        bottomSheetFilterOptions?.show(fragmentManager, "")
    }

    fun doDismiss() {
        if (bottomSheetFilterOptions?.isVisible == true) bottomSheetFilterOptions?.dismiss()
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
                setOnClickListener { actionListener?.showDatePicker(UohConsts.START_DATE)  }
            }
            tfEndDate.textFieldInput.run {
                setText(UohUtils.calendarToStringFormat(chosenEndDate, DATE_FORMAT_DDMMMYYYY))
                isFocusable = false
                isClickable = true
                setOnClickListener { actionListener?.showDatePicker(UohConsts.END_DATE)  }
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

    interface ActionListener {
        fun onClickApply()
        fun showDatePicker(flag: String)
    }

    fun setActionListener(fragment: UohListFragment) {
        this.actionListener = fragment
    }
}