package com.tokopedia.play.broadcaster.view.fragment.edit

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.di.provider.PlayBroadcastComponentProvider
import com.tokopedia.play.broadcaster.di.setup.DaggerPlayBroadcastSetupComponent
import com.tokopedia.play.broadcaster.util.bottomsheet.PlayBroadcastDialogCustomizer
import com.tokopedia.play.broadcaster.util.extension.showToaster
import com.tokopedia.play.broadcaster.view.contract.SetupResultListener
import com.tokopedia.play.broadcaster.view.viewmodel.DataStoreViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.EditCoverTitleViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play_common.util.extension.setTextFieldColor
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * @author by furqan on 12/06/2020
 */
class EditCoverTitleBottomSheet : BottomSheetUnify() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var dispatcher: CoroutineDispatchers

    @Inject
    lateinit var analytic: PlayBroadcastAnalytic

    @Inject
    lateinit var dialogCustomizer: PlayBroadcastDialogCustomizer

    private val job = SupervisorJob()

    private val scope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = dispatcher.main + job
    }

    private var mListener: SetupResultListener? = null

    private var toasterBottomMargin = 0

    private val title: String
        get() = etCoverTitle.text.toString()

    private lateinit var containerEdit: CoordinatorLayout
    private lateinit var etCoverTitle: EditText
    private lateinit var tvTitleCounter: TextView
    private lateinit var llContainerSave: LinearLayout
    private lateinit var btnSave: UnifyButton

    private lateinit var parentViewModel: PlayBroadcastViewModel
    private lateinit var dataStoreViewModel: DataStoreViewModel
    private lateinit var viewModel: EditCoverTitleViewModel

    private val mMaxTitleChars: Int
        get() = viewModel.maxTitleChars

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            dialogCustomizer.customize(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        parentViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
        dataStoreViewModel = ViewModelProvider(this, viewModelFactory).get(DataStoreViewModel::class.java)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EditCoverTitleViewModel::class.java)

        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheet_Setup_Pinned)
        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
        setupObserve()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancelChildren()
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
            containerEdit = findViewById(R.id.container_edit)
            etCoverTitle = findViewById(R.id.et_cover_title)
            tvTitleCounter = findViewById(R.id.tv_title_counter)
            btnSave = findViewById(R.id.btn_save)
            llContainerSave = findViewById(R.id.ll_container_save)
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

        etCoverTitle.filters = arrayOf(InputFilter.LengthFilter(mMaxTitleChars))
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
        etCoverTitle.setTextFieldColor(com.tokopedia.unifyprinciples.R.color.Unify_N150)

        setupTitleCounter()
        setupSaveButton()

        btnSave.setOnClickListener {
            if (btnSave.isLoading) return@setOnClickListener
            viewModel.editTitle(title)
            etCoverTitle.clearFocus()
            analytic.clickSubmitOnEditTitleBottomSheet()
        }
    }

    private fun setupObserve() {
        observeUpdateTitle()
        observeSelectedCover()
    }

    private fun setupTitleCounter() {
        tvTitleCounter.text = getString(R.string.play_prepare_cover_title_counter,
                title.length, mMaxTitleChars)
    }

    private fun setupSaveButton() {
        btnSave.isEnabled = viewModel.isTitleValid(title)
    }

    private fun onUpdateSuccess() {
        scope.launch {
            val error = mListener?.onSetupCompletedWithData(this@EditCoverTitleBottomSheet, dataStoreViewModel.getDataStore())

            if (error != null) {
                yield()
                onUpdateFail(error)
            } else {
                dismiss()
            }
        }
    }

    private fun onUpdateFail(error: Throwable) {
        etCoverTitle.isEnabled = true
        btnSave.isLoading = false
        showToaster(
                message = error.localizedMessage,
                type = Toaster.TYPE_ERROR
        )
    }

    private fun showToaster(
            message: String,
            type: Int = Toaster.TYPE_NORMAL,
            actionLabel: String = ""
    ) {
        if (toasterBottomMargin == 0) {
            val offset8 = resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
            toasterBottomMargin = llContainerSave.height + offset8
        }

        containerEdit.showToaster(
                message = message,
                type = type,
                actionLabel = actionLabel,
                bottomMargin = toasterBottomMargin
        )
    }

    /**
     * Observe
     */
    private fun observeUpdateTitle() {
        viewModel.observableUpdateTitle.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> onUpdateSuccess()
                is NetworkResult.Fail -> onUpdateFail(it.error)
                NetworkResult.Loading -> {
                    etCoverTitle.isEnabled = false
                    btnSave.isLoading = true
                }
            }
        }
    }

    private fun observeSelectedCover() {
        viewModel.observableTitle.observe(viewLifecycleOwner) {
            etCoverTitle.setText(it.title)
        }
    }

    companion object {
        const val TAG = "TagPlayBroadcastEditTitleBottomSheet"
    }
}