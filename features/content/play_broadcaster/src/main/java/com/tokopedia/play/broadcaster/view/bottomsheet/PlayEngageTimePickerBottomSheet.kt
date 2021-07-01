package com.tokopedia.play.broadcaster.view.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.play.broadcaster.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.picker.PickerUnify


/**
 * Created by mzennis on 28/06/21.
 */
class PlayEngageTimePickerBottomSheet : BottomSheetUnify() {

    private lateinit var pickerTime: PickerUnify

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupObserve()
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun setupBottomSheet() {
        setChild(getContentView())
    }

    private fun getContentView(): View {
        val view = View.inflate(requireContext(), R.layout.bottom_sheet_play_engagement_time_picker, null)
        pickerTime = view.findViewById(R.id.picker_time)
        return view
    }

    private fun setupView(view: View) {
        setTitle(getString(R.string.play_engage_time_picker_title))
        pickerTime.infiniteMode = false
        pickerTime.stringData = mutableListOf("3 Menit", "5 Menit", "10 Menit")
    }

    private fun setupObserve() {

    }

    companion object {

        private const val TAG = "PlayEngageTimePickerBottomSheet"
    }

}