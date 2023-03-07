package com.tokopedia.campaign.components.bottomsheet.selection.single

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.databinding.BottomsheetSingleSelectionItemBinding
import com.tokopedia.campaign.components.bottomsheet.selection.entity.SingleSelectionItem
import com.tokopedia.campaign.utils.extension.attachDividerItemDecoration
import com.tokopedia.campaign.utils.extension.enable
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.utils.lifecycle.autoClearedNullable

class SingleSelectionBottomSheet : BottomSheetUnify() {

    companion object {
        private const val BUNDLE_KEY_SELECTED_ITEM_ID = "selected_item_id"
        private const val BUNDLE_KEY_SINGLE_SELECTION_ITEMS = "single_selection_items"
        private const val ITEM_DIVIDER_INSET = 16

        @JvmStatic
        fun newInstance(selectedItemId: String, items: List<SingleSelectionItem>): SingleSelectionBottomSheet {
            return SingleSelectionBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(BUNDLE_KEY_SELECTED_ITEM_ID, selectedItemId)
                    putParcelableArrayList(BUNDLE_KEY_SINGLE_SELECTION_ITEMS, ArrayList(items))
                }
            }
        }
    }

    data class Config(val recyclerView: RecyclerView, val button: UnifyButton)

    private var binding by autoClearedNullable<BottomsheetSingleSelectionItemBinding>()
    private val singleSelectionAdapter = SingleSelectionAdapter()
    private var onItemClicked : (SingleSelectionItem) -> Unit = {}
    private val selectedItemId by lazy { arguments?.getString(BUNDLE_KEY_SELECTED_ITEM_ID).orEmpty() }
    private val singleSelectionItems by lazy { arguments?.getParcelableArrayList<SingleSelectionItem>(
        BUNDLE_KEY_SINGLE_SELECTION_ITEMS
    )?.toList().orEmpty() }
    private var displayedTitle = ""
    private var buttonTitle = ""
    private val helper = SingleSelectionHelper()
    private var customAppearance: Config.() -> Unit = {}

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
        binding = BottomsheetSingleSelectionItemBinding.inflate(inflater, container, false)
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
        setupCustomAppearance()
    }

    private fun setupCustomAppearance() {
        binding?.run {
            val property = Config(recyclerView, btnApply)
            customAppearance.invoke(property)
        }
    }

    private fun setupClickListeners() {
        binding?.btnApply?.setOnClickListener {
            val items = singleSelectionAdapter.snapshot()
            val selectedItem = helper.findSelectedItem(items) ?: return@setOnClickListener
            onItemClicked(selectedItem)
            dismiss()
        }
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = singleSelectionAdapter
            attachDividerItemDecoration(insetLeft = ITEM_DIVIDER_INSET, insetRight = ITEM_DIVIDER_INSET)
        }

        populateSingleSelectionItems()

        singleSelectionAdapter.setOnItemClicked { selectedItem -> handleSelection(selectedItem) }
    }

    private fun populateSingleSelectionItems() {
        val updatedItems = helper.markAsSelected(selectedItemId, singleSelectionItems)
        singleSelectionAdapter.submit(updatedItems)
    }

    fun setOnApplyButtonClick(onItemClicked : (SingleSelectionItem) -> Unit) {
        this.onItemClicked = onItemClicked
    }

    fun setBottomSheetTitle(title: String) {
        this.displayedTitle = title
    }

    /**
     * Use this function to override default bottomsheet appearance
     * e.g: Change button style to buttonVariant = UnifyButton.Variant.TEXT_ONLY when bottomsheet displayed
     */
    fun setCustomAppearance(customAppearance: Config.() -> Unit = {}) {
        this.customAppearance = customAppearance
    }

    fun setBottomSheetButtonTitle(buttonTitle: String) {
        this.buttonTitle = buttonTitle
    }

    fun getSelectedItem(): SingleSelectionItem? {
        val items = singleSelectionAdapter.snapshot()
        return helper.findSelectedItem(items)
    }


    private fun handleSelection(selectedPackage: SingleSelectionItem) {
        binding?.btnApply?.enable()

        val items = singleSelectionAdapter.snapshot()
        val updatedItems = helper.markAsSelected(selectedPackage.id, items)

        singleSelectionAdapter.submit(updatedItems)
    }
}
