package com.tokopedia.kyc_centralized.ui.gotoKyc.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.databinding.FragmentGotoKycDobChallengeBinding
import com.tokopedia.kyc_centralized.di.GoToKycComponent
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*

class DobChallengeFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentGotoKycDobChallengeBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGotoKycDobChallengeBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        binding?.ivDobChallenge?.loadImageWithoutPlaceholder(
            getString(R.string.img_url_goto_kyc_dob_challenge_main)
        )

        binding?.fieldDob?.apply {
            editText.isFocusable = false
            icon1.setImageDrawable(getIconUnifyDrawable(requireContext(), IconUnify.CHEVRON_DOWN))
        }
    }

    private fun initListener() {
        binding?.unifyToolbar?.setNavigationOnClickListener { activity?.finish() }

        binding?.fieldDob?.editText?.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val calMax = Calendar.getInstance()
        calMax.add(Calendar.YEAR, MAX_YEAR)
        val yearMax = calMax.get(Calendar.YEAR)
        val monthMax = calMax.get(Calendar.MONTH)
        val dayMax = calMax.get(Calendar.DAY_OF_MONTH)
        val maxDate = GregorianCalendar(yearMax, monthMax, dayMax)

        val calMin = Calendar.getInstance()
        calMin.add(Calendar.YEAR, MIN_YEAR)
        val yearMin = calMin.get(Calendar.YEAR)
        val monthMin = calMin.get(Calendar.MONTH)
        val dayMin = calMin.get(Calendar.DAY_OF_MONTH)
        val minDate = GregorianCalendar(yearMin, monthMin, dayMin)

        val datePicker = DateTimePickerUnify(
            context = requireContext(), minDate = minDate, defaultDate = maxDate, maxDate = maxDate
        )

        datePicker.setTitle(getString(R.string.goto_kyc_dob_challenge_choose_dob))

        datePicker.datePickerButton.setOnClickListener {
            val selectedDatePicker = datePicker.getDate()
            val selectedDate = formatDateParam(
                selectedDatePicker.get(Calendar.DAY_OF_MONTH),
                selectedDatePicker.get(Calendar.MONTH) + 1,
                selectedDatePicker.get(Calendar.YEAR)
            )
            val date = DateFormatUtils.formatDate(
                DateFormatUtils.FORMAT_YYYY_MM_DD, DateFormatUtils.FORMAT_DD_MMMM_YYYY, selectedDate
            )
            binding?.fieldDob?.editText?.setText(date)
            binding?.btnConfirmation?.isEnabled = true
            datePicker.dismiss()
        }

        datePicker.show(childFragmentManager, TAG_BOTTOM_SHEET_DATE_PICKER)
    }

    private fun formatDateParam(dayOfMonth: Int, month: Int, year: Int): String {
        return String.format("%s-%s-%s", year.toString(), month.toString(), dayOfMonth.toString())
    }

    override fun getScreenName(): String = DobChallengeFragment::class.java.simpleName

    override fun initInjector() {
        getComponent(GoToKycComponent::class.java).inject(this)
    }

    companion object {
        private const val MAX_YEAR = -17
        private const val MIN_YEAR = -100
        private const val TAG_BOTTOM_SHEET_DATE_PICKER = "bottom sheet date picker"
    }
}
