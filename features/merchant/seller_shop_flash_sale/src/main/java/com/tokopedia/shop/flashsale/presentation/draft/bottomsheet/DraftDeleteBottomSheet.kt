package com.tokopedia.shop.flashsale.presentation.draft.bottomsheet

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flashsale.common.constant.DraftConstant.DRAFT_REASON_MINCHAR
import com.tokopedia.shop.flashsale.common.customcomponent.ModalBottomSheet
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.presentation.draft.adapter.DraftDeleteQuestionAdapter
import com.tokopedia.shop.flashsale.presentation.draft.uimodel.DraftItemModel
import com.tokopedia.shop.flashsale.presentation.draft.viewmodel.DraftDeleteViewModel
import com.tokopedia.unifycomponents.TextAreaUnify2
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject


open class DraftDeleteBottomSheet(
    private val draftItemModel: DraftItemModel? = null,
    private val onDeleteDraftSuccess: () -> Unit = {}
): ModalBottomSheet() {

    companion object {
        const val TAG = "Tag DraftDeleteBottomSheet"
        const val MIN_LINE_TEXTAREA = 3
    }

    @Inject
    lateinit var viewModel: DraftDeleteViewModel
    protected var typographyDraftDeleteDesc: Typography? = null
    protected var typographyQuestionTitle: Typography? = null
    protected var btnStop: UnifyButton? = null
    private var etQuestionOther: TextAreaUnify2? = null
    private var rvQuestionList: RecyclerView? = null
    private var questionListAdapter = DraftDeleteQuestionAdapter()
    private var otherReasonText: String = ""
    private var btnBack: View? = null

    init {
        useWideModal = true
        setCloseClickListener {
            dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initInjector()

        setupObservers()
        setupTitle()
        setupEtQuestionOther()
        setupQuestionList()
        setupButtonsView()

        viewModel.getQuestionListData()
    }

    private fun initInjector() {
        DaggerShopFlashSaleComponent.builder()
            .baseAppComponent((requireActivity().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun initChildLayout() {
        overlayClickDismiss = true
        val contentView: View? = View.inflate(context, R.layout.ssfs_bottomsheet_draft_delete, null)
        etQuestionOther = contentView?.findViewById(R.id.etQuestionOther)
        rvQuestionList = contentView?.findViewById(R.id.rvQuestionList)
        btnStop = contentView?.findViewById(R.id.btnStop)
        btnBack = contentView?.findViewById(R.id.btnBack)
        typographyDraftDeleteDesc = contentView?.findViewById(R.id.typographyDraftDeleteDesc)
        typographyQuestionTitle = contentView?.findViewById(R.id.typographyQuestionTitle)
        setChild(contentView)
    }

    private fun setupObservers() {
        viewModel.questionListData.observe(viewLifecycleOwner) {
            if (it is Success) {
                questionListAdapter.setItems(it.data)
                questionListAdapter.addItem(otherReasonText)
                refreshLayout()
            }
        }

        viewModel.cancellationError.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            btnStop?.isLoading = false
        }

        viewModel.cancellationStatus.observe(viewLifecycleOwner) {
            if (it) {
                dismiss()
                onDeleteDraftSuccess.invoke()
            }
        }
    }

    private fun setupTitle() {
        setTitle(getString(R.string.draftdelete_title))
        typographyDraftDeleteDesc?.text = getString(R.string.draftdelete_desc, draftItemModel?.title)
        bottomSheetTitle.gravity = Gravity.CENTER
        bottomSheetClose.hide()
    }

    private fun setupQuestionList() {
        otherReasonText = getString(R.string.draftdelete_question_other_text)
        rvQuestionList?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvQuestionList?.adapter = questionListAdapter.apply {
            setSelectionChangedListener {
                if (it != otherReasonText) {
                    viewModel.setCancellationReason(it)
                }
                etQuestionOther?.editText?.setText("")
                etQuestionOther?.isEnabled = it == otherReasonText
                etQuestionOther?.setMessage("")
                etQuestionOther?.isInputError = false
                view?.post { btnStop?.isEnabled = it != otherReasonText }
            }
        }
    }

    private fun setupEtQuestionOther() {
        etQuestionOther?.minLine = MIN_LINE_TEXTAREA
        etQuestionOther?.addOnFocusChangeListener = { _, hasFocus ->
            if (hasFocus) {
                etQuestionOther?.setLabel(getString(R.string.draftdelete_question_other_placeholder_short))
            } else {
                etQuestionOther?.setLabel(getString(R.string.draftdelete_question_other_placeholder))
            }
        }
        etQuestionOther?.editText?.afterTextChanged {
            val errorMessage = if (it.length < DRAFT_REASON_MINCHAR) {
                getString(R.string.draftdelete_error_minchar, DRAFT_REASON_MINCHAR)
            } else {
                viewModel.setCancellationReason(it)
                ""
            }
            etQuestionOther?.isInputError = errorMessage.isNotEmpty()
            etQuestionOther?.setMessage(errorMessage)
            btnStop?.isEnabled = errorMessage.isEmpty()
        }
        etQuestionOther?.isEnabled = false
    }

    private fun setupButtonsView() {
        btnStop?.isEnabled = false
        btnStop?.setOnClickListener {
            if (btnStop?.isLoading == true) return@setOnClickListener
            viewModel.doCancellation(draftItemModel ?: return@setOnClickListener)
            btnStop?.isLoading = true
        }
        btnBack?.setOnClickListener {
            dismiss()
        }
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this , TAG)
        }
    }
}