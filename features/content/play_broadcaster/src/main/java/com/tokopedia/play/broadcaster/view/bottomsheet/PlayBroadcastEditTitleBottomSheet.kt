package com.tokopedia.play.broadcaster.view.bottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.util.PlayBroadcastCoverTitleUtil
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton

/**
 * @author by furqan on 12/06/2020
 */
class PlayBroadcastEditTitleBottomSheet : BottomSheetUnify() {

    private var mListener: Listener? = null

    private val title: String
        get() = etCoverTitle.text.toString()

    private val args: Bundle
        get() {
            if (arguments == null) arguments = Bundle()
            return arguments!!
        }

    private lateinit var etCoverTitle: EditText
    private lateinit var tvTitleCounter: TextView
    private lateinit var btnSave: UnifyButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Style_FloatingBottomSheet)
        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    fun setCoverTitle(title: String) {
        args.putString(EXTRA_CURRENT_TITLE, title)
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    private fun initBottomSheet() {
        isFullpage = false
        isDragable = true
        isHideable = true
        setTitle(getString(R.string.play_broadcast_edit_title_title))
        val view = View.inflate(requireContext(), R.layout.bottom_sheet_play_broadcast_edit_title, null)
        setChild(view)
    }

    private fun initView(view: View) {
        with (view) {
            etCoverTitle = findViewById(R.id.et_cover_title)
            tvTitleCounter = findViewById(R.id.tv_title_counter)
            btnSave = findViewById(R.id.btn_save)
        }
    }

    private fun setupView(view: View) {
        bottomSheetHeader.setPadding(
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2),
                bottomSheetHeader.top,
                bottomSheetWrapper.right,
                bottomSheetHeader.bottom)
        bottomSheetWrapper.setPadding(0,
                bottomSheetWrapper.paddingTop,
                0,
                bottomSheetWrapper.paddingBottom)

        etCoverTitle.setText(arguments?.getString(EXTRA_CURRENT_TITLE).orEmpty())
        etCoverTitle.filters = arrayOf(InputFilter.LengthFilter(PlayBroadcastCoverTitleUtil.MAX_LENGTH_LIVE_TITLE))
        etCoverTitle.setRawInputType(InputType.TYPE_CLASS_TEXT)
        etCoverTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
                setupTitleCounter()
                setupSaveButton()
            }
        })
        etCoverTitle.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) v.clearFocus()
            false
        }

        setupTitleCounter()
        btnSave.setOnClickListener {
            mListener?.onSaveEditedTitle(title)
            dismiss()
        }

        setupSaveButton()
    }

    private fun setupTitleCounter() {
        tvTitleCounter.text = getString(R.string.play_prepare_cover_title_counter,
                title.length, PlayBroadcastCoverTitleUtil.MAX_LENGTH_LIVE_TITLE)
    }

    private fun setupSaveButton() {
        btnSave.isEnabled = title.isNotEmpty() &&
                title.length <= PlayBroadcastCoverTitleUtil.MAX_LENGTH_LIVE_TITLE
    }

    interface Listener {
        fun onSaveEditedTitle(title: String)
    }

    companion object {
        private const val EXTRA_CURRENT_TITLE = "EXTRA_CURRENT_TITLE"

        const val TAG = "TagPlayBroadcastEditTitleBottomSheet"
    }
}