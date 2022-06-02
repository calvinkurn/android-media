package com.tokopedia.shop.flash_sale.presentation.draft.bottomsheet

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flash_sale.common.customcomponent.ModalBottomSheet
import com.tokopedia.shop.flash_sale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flash_sale.presentation.draft.adapter.DraftDeleteQuestionAdapter
import com.tokopedia.shop.flash_sale.presentation.draft.uimodel.DraftItemModel
import com.tokopedia.shop.flash_sale.presentation.draft.viewmodel.DraftDeleteViewModel
import com.tokopedia.unifycomponents.TextAreaUnify2
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject


class DraftDeleteBottomSheet(
    private val draftItemModel: DraftItemModel? = null
): ModalBottomSheet() {

    companion object {
        const val TAG = "Tag DraftDeleteBottomSheet"
        const val MIN_LINE_TEXTAREA = 3
    }


    @Inject
    lateinit var viewModel: DraftDeleteViewModel
    private var typographyDraftDeleteDesc: Typography? = null
    private var etQuestionOther: TextAreaUnify2? = null
    private var rvQuestionList: RecyclerView? = null
    private var questionListAdapter = DraftDeleteQuestionAdapter()
    private var otherReasonText: String = ""

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
        typographyDraftDeleteDesc = contentView?.findViewById(R.id.typographyDraftDeleteDesc)
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
                etQuestionOther?.isEnabled = it == otherReasonText
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
        etQuestionOther?.isEnabled = false
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this , TAG)
        }
    }
}