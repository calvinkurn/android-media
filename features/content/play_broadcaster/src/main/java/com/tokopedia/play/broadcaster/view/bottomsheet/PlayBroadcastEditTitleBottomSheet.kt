package com.tokopedia.play.broadcaster.view.bottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.util.PlayBroadcastCoverTitleUtil
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_play_broadcast_edit_title.*

/**
 * @author by furqan on 12/06/2020
 */
class PlayBroadcastEditTitleBottomSheet : BottomSheetUnify() {

    lateinit var listener: Listener
    private var liveTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        liveTitle = arguments?.getString(EXTRA_CURRENT_TITLE) ?: ""
        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initBottomSheet() {
        isFullpage = false
        isDragable = true
        isHideable = true
        setTitle(getString(R.string.play_broadcast_edit_title_title))
        setChild(View.inflate(requireContext(), R.layout.bottom_sheet_play_broadcast_edit_title, null))
    }

    private fun initView() {
        bottomSheetHeader.setPadding(
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2),
                bottomSheetHeader.top,
                bottomSheetWrapper.right,
                bottomSheetHeader.bottom)
        bottomSheetWrapper.setPadding(0,
                bottomSheetWrapper.paddingTop,
                0,
                bottomSheetWrapper.paddingBottom)

        etPlayCoverTitleText.setText(liveTitle)
        etPlayCoverTitleText.setSingleLine(false)
        etPlayCoverTitleText.filters = arrayOf(InputFilter.LengthFilter(PlayBroadcastCoverTitleUtil.MAX_LENGTH_LIVE_TITLE))
        etPlayCoverTitleText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
                liveTitle = text.toString()
                setupTitleCounter()
                setupSaveButton()
            }
        })
        setupTitleCounter()
        btnPlayPrepareBroadcastSave.setOnClickListener {
            listener.onSaveEditedTitle(etPlayCoverTitleText.text.toString())
            dismiss()
        }

        setupSaveButton()
    }

    private fun setupTitleCounter() {
        tvPlayTitleCounter.text = getString(R.string.play_prepare_cover_title_counter,
                liveTitle.length, PlayBroadcastCoverTitleUtil.MAX_LENGTH_LIVE_TITLE)
    }

    private fun setupSaveButton() {
        btnPlayPrepareBroadcastSave.isEnabled = liveTitle.isNotEmpty() &&
                liveTitle.length <= PlayBroadcastCoverTitleUtil.MAX_LENGTH_LIVE_TITLE
    }

    interface Listener {
        fun onSaveEditedTitle(title: String)
    }

    companion object {
        private const val EXTRA_CURRENT_TITLE = "EXTRA_CURRENT_TITLE"

        const val TAG = "TagPlayBroadcastEditTitleBottomSheet"

        fun getInstance(title: String): PlayBroadcastEditTitleBottomSheet =
                PlayBroadcastEditTitleBottomSheet().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_CURRENT_TITLE, title)
                    }
                }
    }
}