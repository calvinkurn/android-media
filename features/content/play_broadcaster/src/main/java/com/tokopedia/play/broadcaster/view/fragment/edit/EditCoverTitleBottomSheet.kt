package com.tokopedia.play.broadcaster.view.fragment.edit

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.di.provider.PlayBroadcastComponentProvider
import com.tokopedia.play.broadcaster.di.setup.DaggerPlayBroadcastSetupComponent
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import com.tokopedia.play.broadcaster.util.PlayBroadcastCoverTitleUtil
import com.tokopedia.play.broadcaster.util.setTextFieldColor
import com.tokopedia.play.broadcaster.util.showToaster
import com.tokopedia.play.broadcaster.view.contract.SetupResultListener
import com.tokopedia.play.broadcaster.view.viewmodel.DataStoreViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.EditCoverTitleViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import javax.inject.Inject

/**
 * @author by furqan on 12/06/2020
 */
class EditCoverTitleBottomSheet : BottomSheetUnify() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var mListener: SetupResultListener? = null

    private val title: String
        get() = etCoverTitle.text.toString()

    private lateinit var etCoverTitle: EditText
    private lateinit var tvTitleCounter: TextView
    private lateinit var btnSave: UnifyButton

    private lateinit var parentViewModel: PlayBroadcastViewModel
    private lateinit var dataStoreViewModel: DataStoreViewModel
    private lateinit var viewModel: EditCoverTitleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        parentViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
        dataStoreViewModel = ViewModelProviders.of(this, viewModelFactory).get(DataStoreViewModel::class.java)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(EditCoverTitleViewModel::class.java)

        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Style_FloatingBottomSheet)
        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeUpdateTitle()
        observeSelectedCover()
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    fun setListener(listener: SetupResultListener) {
        mListener = listener
    }

    private fun inject() {
        DaggerPlayBroadcastSetupComponent.builder()
                .setBroadcastComponent((requireActivity() as PlayBroadcastComponentProvider).getBroadcastComponent())
                .build()
                .inject(this)
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
        setOnDismissListener { mListener?.onSetupCanceled() }
        dataStoreViewModel.setDataStore(parentViewModel.getCurrentSetupDataStore())

        bottomSheetHeader.setPadding(
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2),
                bottomSheetHeader.top,
                bottomSheetWrapper.right,
                bottomSheetHeader.bottom)
        bottomSheetWrapper.setPadding(0,
                bottomSheetWrapper.paddingTop,
                0,
                bottomSheetWrapper.paddingBottom)

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
        etCoverTitle.setTextFieldColor(com.tokopedia.unifyprinciples.R.color.Neutral_N150)

        setupTitleCounter()
        setupSaveButton()

        btnSave.setOnClickListener {
            if (btnSave.isLoading) return@setOnClickListener
            viewModel.editTitle(title, parentViewModel.channelId)
            etCoverTitle.clearFocus()
        }
    }

    private fun setupTitleCounter() {
        tvTitleCounter.text = getString(R.string.play_prepare_cover_title_counter,
                title.length, PlayBroadcastCoverTitleUtil.MAX_LENGTH_LIVE_TITLE)
    }

    private fun setupSaveButton() {
        btnSave.isEnabled = title.isNotEmpty() &&
                title.length <= PlayBroadcastCoverTitleUtil.MAX_LENGTH_LIVE_TITLE
    }

    /**
     * Observe
     */
    private fun observeUpdateTitle() {
        viewModel.observableUpdateTitle.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    etCoverTitle.isEnabled = true
                    btnSave.isLoading = false
                    mListener?.onSetupCompletedWithData(dataStoreViewModel.getDataStore())
                    dismiss()
                }
                is NetworkResult.Fail -> {
                    etCoverTitle.isEnabled = true
                    btnSave.isLoading = false
                    requireView().showToaster(
                            message = it.error.localizedMessage,
                            type = Toaster.TYPE_ERROR
                    )
                }
                NetworkResult.Loading -> {
                    etCoverTitle.isEnabled = false
                    btnSave.isLoading = true
                }
            }
        })
    }

    private fun observeSelectedCover() {
        viewModel.observableCurrentTitle.observe(viewLifecycleOwner, Observer {
            etCoverTitle.setText(it.orEmpty())
        })
    }

    companion object {
        const val TAG = "TagPlayBroadcastEditTitleBottomSheet"
    }
}