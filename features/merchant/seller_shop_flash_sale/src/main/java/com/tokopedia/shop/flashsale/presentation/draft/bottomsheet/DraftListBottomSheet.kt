package com.tokopedia.shop.flashsale.presentation.draft.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.presentation.draft.adapter.DraftListAdapter
import com.tokopedia.shop.flashsale.presentation.draft.mapper.DraftUiModelMapper
import com.tokopedia.shop.flashsale.presentation.draft.uimodel.DraftItemModel
import com.tokopedia.shop.flashsale.presentation.draft.uimodel.DraftUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class DraftListBottomSheet(
    private val draftItemModel: DraftUiModel = DraftUiModel(),
    private val onDeleteDraftSuccess: () -> Unit = {},
    private val onDraftClicked : (DraftItemModel) -> Unit = {}
): BottomSheetUnify() {

    companion object {
        const val TAG = "Tag DraftListBottomSheet"

        fun showUsingCampaignUiModel(manager: FragmentManager?,
            campaignUiModelList: List<CampaignUiModel>,
            onDeleteDraftSuccess: () -> Unit = {},
            onDraftClicked : (DraftItemModel) -> Unit = {}
        ) {
            DraftListBottomSheet(
                DraftUiModelMapper.convertFromCampaignUiModel(campaignUiModelList),
                onDeleteDraftSuccess,
                onDraftClicked
            ).show(manager)
        }
    }

    private var rvDraft: RecyclerView? = null
    private var typographyDescDraft: Typography? = null
    private var btnBack: UnifyButton? = null

    init {
        setCloseClickListener { dismiss() }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupPageTitleAndDescription()
        setupDraftList()
        setupBtnBack()
    }

    private fun setupBtnBack() {
        btnBack?.isVisible = !draftItemModel.isFull
        btnBack?.setOnClickListener {
            dismiss()
        }
    }

    private fun setupPageTitleAndDescription() {
        if (draftItemModel.isFull) {
            setTitle(getString(R.string.draftlist_title_full))
            typographyDescDraft?.text = getString(R.string.draftlist_description_full)
        } else {
            setTitle(getString(R.string.draftlist_title_available))
            typographyDescDraft?.text = getString(R.string.draftlist_description_available)
        }
    }

    private fun setupDraftList() {
        rvDraft?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvDraft?.adapter = DraftListAdapter().apply {
            setItems(draftItemModel.list)
            setDeleteIconClickListener(::onDeleteIconClick)
            setOnDraftClick { selectedDraft ->
                onDraftClicked(selectedDraft)
                dismiss()
            }
        }
    }

    private fun initChildLayout() {
        overlayClickDismiss = true
        val contentView: View? = View.inflate(context, R.layout.ssfs_bottomsheet_draft_list , null)
        rvDraft = contentView?.findViewById(R.id.rvDraft)
        typographyDescDraft = contentView?.findViewById(R.id.typographyDescDraft)
        btnBack = contentView?.findViewById(R.id.btnBack)
        setChild(contentView)
    }

    private fun onDeleteIconClick(draftItemModel: DraftItemModel) {
        DraftDeleteBottomSheet(draftItemModel, ::onDeleteDraftSuccess).show(childFragmentManager)
    }

    private fun onDeleteDraftSuccess() {
        onDeleteDraftSuccess.invoke()
        dismiss()
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this , TAG)
        }
    }
}