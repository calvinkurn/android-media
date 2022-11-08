package com.tokopedia.campaign.components.bottomsheet.selection.instant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.components.bottomsheet.selection.entity.SingleSelectionItem
import com.tokopedia.campaign.components.bottomsheet.selection.single.SingleSelectionHelper
import com.tokopedia.campaign.databinding.BottomsheetInstantSelectionItemBinding
import com.tokopedia.campaign.utils.extension.attachDividerItemDecoration
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class InstantSelectionBottomSheet : BottomSheetUnify() {

    companion object {
        private const val BUNDLE_KEY_SELECTED_ITEM_ID = "selected_item_id"
        private const val BUNDLE_KEY_SINGLE_SELECTION_ITEMS = "single_selection_items"
        private const val ITEM_DIVIDER_INSET = 16

        @JvmStatic
        fun newInstance(
            selectedItemId: String,
            items: List<SingleSelectionItem>
        ): InstantSelectionBottomSheet {
            return InstantSelectionBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(BUNDLE_KEY_SELECTED_ITEM_ID, selectedItemId)
                    putParcelableArrayList(BUNDLE_KEY_SINGLE_SELECTION_ITEMS, ArrayList(items))
                }
            }
        }
    }

    data class Config(val recyclerView: RecyclerView)

    private var binding by autoClearedNullable<BottomsheetInstantSelectionItemBinding>()
    private val instantSelectionAdapter = InstantSelectionAdapter()
    private var onItemClicked: (SingleSelectionItem) -> Unit = {}
    private val selectedItemId by lazy {
        arguments?.getString(BUNDLE_KEY_SELECTED_ITEM_ID).orEmpty()
    }
    private val singleSelectionItems by lazy {
        arguments?.getParcelableArrayList<SingleSelectionItem>(
            BUNDLE_KEY_SINGLE_SELECTION_ITEMS
        )?.toList().orEmpty()
    }
    private var displayedTitle = ""
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
        binding = BottomsheetInstantSelectionItemBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        setTitle(displayedTitle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        setupRecyclerView()
        setupCustomAppearance()
    }

    private fun setupCustomAppearance() {
        binding?.run {
            val property = Config(recyclerView)
            customAppearance.invoke(property)
        }
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = instantSelectionAdapter
            attachDividerItemDecoration(
                insetLeft = ITEM_DIVIDER_INSET,
                insetRight = ITEM_DIVIDER_INSET
            )
        }

        populateSingleSelectionItems()

        instantSelectionAdapter.setOnItemClicked { selectedItem -> handleSelection(selectedItem) }
    }

    private fun populateSingleSelectionItems() {
        val updatedItems = helper.markAsSelected(selectedItemId, singleSelectionItems)
        instantSelectionAdapter.submit(updatedItems)
    }

    fun setOnItemClicked(onItemClicked: (SingleSelectionItem) -> Unit) {
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

    fun getSelectedItem(): SingleSelectionItem? {
        val items = instantSelectionAdapter.snapshot()
        return helper.findSelectedItem(items)
    }

    private fun handleSelection(selectedPackage: SingleSelectionItem) {
        val items = instantSelectionAdapter.snapshot()
        val updatedItems = helper.markAsSelected(selectedPackage.id, items)

        instantSelectionAdapter.submit(updatedItems)

        onItemClicked(selectedPackage)
        dismiss()
    }

}
