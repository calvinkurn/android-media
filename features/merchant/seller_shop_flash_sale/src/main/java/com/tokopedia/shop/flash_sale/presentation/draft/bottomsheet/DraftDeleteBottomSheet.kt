package com.tokopedia.shop.flash_sale.presentation.draft.bottomsheet

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flash_sale.common.customcomponent.ModalBottomSheet
import com.tokopedia.shop.flash_sale.presentation.draft.uimodel.DraftItemModel
import com.tokopedia.unifycomponents.TextAreaUnify2
import android.opengl.ETC1.getHeight
import android.view.Gravity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.shop.flash_sale.common.util.KeyboardHandler
import com.tokopedia.shop.flash_sale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flash_sale.presentation.draft.adapter.DraftDeleteQuestionAdapter
import com.tokopedia.shop.flash_sale.presentation.draft.viewmodel.DraftDeleteViewModel
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

        setTitle(getString(R.string.draftdelete_title))
        bottomSheetTitle.gravity = Gravity.CENTER
        bottomSheetClose.hide()

        viewModel.getData("222")
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
        val etQuestionOther: TextAreaUnify2? = contentView?.findViewById(R.id.etQuestionOther)
        val rvQuestionList: RecyclerView? = contentView?.findViewById(R.id.rvQuestionList)
        setChild(contentView)

        etQuestionOther?.minLine = MIN_LINE_TEXTAREA
        etQuestionOther?.addOnFocusChangeListener = { _, hasFocus ->
            if (hasFocus) {
                etQuestionOther?.setLabel(getString(R.string.draftdelete_question_other_placeholder_short))
            } else {
                etQuestionOther?.setLabel(getString(R.string.draftdelete_question_other_placeholder))
            }
        }

        rvQuestionList?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvQuestionList?.adapter = DraftDeleteQuestionAdapter().apply {
            setItems(listOf(
                "Ingin mengubah jadwal campaign",
                "Ingin berpromosi menggunakan fitur lain",
                "Alasan Lain: ",
            ))
            setSelectionChangedListener {
                etQuestionOther?.isEnabled = it == "Alasan Lain: "
            }
        }
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this , TAG)
        }
    }
}