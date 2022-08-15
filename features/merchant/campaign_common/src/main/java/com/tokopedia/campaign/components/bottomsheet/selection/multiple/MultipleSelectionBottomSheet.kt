package com.tokopedia.campaign.components.bottomsheet.selection.multiple

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.campaign.databinding.BottomsheetMultipleSelectionItemBinding
import com.tokopedia.campaign.entity.MultipleSelectionItem
import com.tokopedia.campaign.utils.extension.attachDividerItemDecoration
import com.tokopedia.campaign.utils.extension.enable
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class MultipleSelectionBottomSheet : BottomSheetUnify() {

    companion object {
        private const val BUNDLE_KEY_SELECTED_ITEM_IDS = "selected_item_ids"
        private const val BUNDLE_KEY_MULTIPLE_SELECTION_ITEMS = "multiple_selection_items"
        private const val ITEM_DIVIDER_INSET = 16

        @JvmStatic
        fun newInstance(
            selectedItemIds: ArrayList<String>,
            items: ArrayList<MultipleSelectionItem>
        ): MultipleSelectionBottomSheet {
            return MultipleSelectionBottomSheet().apply {
                arguments = Bundle().apply {
                    putStringArrayList(BUNDLE_KEY_SELECTED_ITEM_IDS, selectedItemIds)
                    putParcelableArrayList(BUNDLE_KEY_MULTIPLE_SELECTION_ITEMS, items)
                }
            }
        }
    }


    private var binding by autoClearedNullable<BottomsheetMultipleSelectionItemBinding>()
    private val multipleSelectionAdapter = MultipleSelectionAdapter()
    private var onApplyButtonClick : (List<MultipleSelectionItem>) -> Unit = {}
    private val selectedItemIds by lazy { arguments?.getStringArrayList(BUNDLE_KEY_SELECTED_ITEM_IDS)?.toList().orEmpty() }
    private val multipleSelectionItems by lazy { arguments?.getParcelableArrayList<MultipleSelectionItem>(
        BUNDLE_KEY_MULTIPLE_SELECTION_ITEMS
    )?.toList().orEmpty() }

    private var displayedTitle = ""
    private var buttonTitle = ""
    private val helper = MultipleSelectionHelper()

    init {
        clearContentPadding = true
        isSkipCollapseState = true
        isKeyboardOverlap = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding = BottomsheetMultipleSelectionItemBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        setTitle(displayedTitle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        setupClickListeners()
        setupRecyclerView()
    }

    private fun setupClickListeners() {
        binding?.btnApply?.setOnClickListener {
            onApplyButtonClick(getAllSelectedItems())
            dismiss()
        }
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = multipleSelectionAdapter
            attachDividerItemDecoration(insetLeft = ITEM_DIVIDER_INSET, insetRight = ITEM_DIVIDER_INSET)
        }

        populateMultipleSelectionItems()

        multipleSelectionAdapter.setOnItemClicked { selectedItem, isChecked ->
            handleSelection(selectedItem, isChecked)
        }
    }

    private fun populateMultipleSelectionItems() {
        selectedItemIds.forEach { selectedItemId -> helper.markAsSelected(selectedItemId) }
        val updatedItems = helper.refresh(multipleSelectionItems)
        multipleSelectionAdapter.submit(updatedItems)
    }

    fun setOnApplyButtonClick(onApplyButtonClick : (List<MultipleSelectionItem>) -> Unit) {
        this.onApplyButtonClick = onApplyButtonClick
    }

    fun setBottomSheetTitle(title: String) {
        this.displayedTitle = title
    }

    fun setBottomSheetButtonTitle(buttonTitle: String) {
        this.buttonTitle = buttonTitle
    }

    fun getBottomSheetView() : View? {
        return binding?.root
    }

    fun getAllSelectedItems(): List<MultipleSelectionItem> {
        return helper.findSelectedItems(multipleSelectionAdapter.snapshot())
    }

    private fun handleSelection(selectedPackage: MultipleSelectionItem, isChecked: Boolean) {
        binding?.btnApply?.enable()


        if (isChecked) {
            helper.markAsSelected(selectedPackage.id)
        } else {
            helper.markAsUnselected(selectedPackage.id)
        }

        val items = multipleSelectionAdapter.snapshot()
        val updatedItems = helper.refresh(items)

        multipleSelectionAdapter.submit(updatedItems)
    }
}