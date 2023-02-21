package com.tokopedia.mvc.presentation.product.add.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.campaign.utils.extension.attachDividerItemDecoration
import com.tokopedia.campaign.utils.extension.enable
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheetFilterShowcaseBinding
import com.tokopedia.mvc.domain.entity.ShopShowcase
import com.tokopedia.mvc.presentation.product.add.adapter.filter.ShopShowcaseFilterAdapter
import com.tokopedia.mvc.util.constant.ImageUrlConstant
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ShowcaseFilterBottomSheet: BottomSheetUnify() {

    companion object {
        private const val BUNDLE_KEY_SELECTED_SHOWCASE_IDS = "selected_showcase_ids"
        private const val BUNDLE_KEY_SHOWCASES = "showcases"
        private const val ITEM_DIVIDER_INSET = 16

        @JvmStatic
        fun newInstance(selectedShowcaseIds: List<Long>, items: List<ShopShowcase>): ShowcaseFilterBottomSheet {
            return ShowcaseFilterBottomSheet().apply {
                arguments = Bundle().apply {
                    putLongArray(BUNDLE_KEY_SELECTED_SHOWCASE_IDS, selectedShowcaseIds.toLongArray())
                    putParcelableArrayList(BUNDLE_KEY_SHOWCASES, ArrayList(items))
                }
            }
        }
    }

    private var binding by autoClearedNullable<SmvcBottomsheetFilterShowcaseBinding>()

    private val showcaseAdapter = ShopShowcaseFilterAdapter()
    private var onButtonApplyClick : (List<ShopShowcase>) -> Unit = {}

    private val selectedShowcaseIds by lazy {
        arguments?.getLongArray(BUNDLE_KEY_SELECTED_SHOWCASE_IDS)
    }
    private val showcases by lazy {
        arguments?.getParcelableArrayList<ShopShowcase>(BUNDLE_KEY_SHOWCASES)
    }


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
        binding = SmvcBottomsheetFilterShowcaseBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        setTitle(getString(R.string.smvc_showcase))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val showcases = showcases?.toList().orEmpty()
        setupRecyclerView(showcases)
        setupEmptyState(showcases)
        setupClickListener()
    }

    private fun setupEmptyState(showcases: List<ShopShowcase>) {
        if (showcases.isEmpty()) {
            binding?.groupEmptyState?.visible()
            binding?.imgNoShowcase?.loadImage(ImageUrlConstant.IMAGE_URL_EMPTY_SHOWCASE)
        }

        binding?.btnApply?.isVisible = showcases.isNotEmpty()
    }


    private fun setupRecyclerView(showcases : List<ShopShowcase>) {
        if (showcases.isEmpty()) return

        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = showcaseAdapter
            attachDividerItemDecoration(insetLeft = ITEM_DIVIDER_INSET, insetRight = ITEM_DIVIDER_INSET)
        }

        showcaseAdapter.submit(showcases)
        showcaseAdapter.markAsSelected(selectedShowcaseIds?.toList().orEmpty())
        showcaseAdapter.setOnShowcaseClicked { selectedShopShowcase ->
            showcaseAdapter.markAsSelected(listOf(selectedShopShowcase.id))
            binding?.btnApply?.enable()
        }
    }

    private fun setupClickListener() {
        binding?.btnApply?.setOnClickListener {
            val selectedShowcases = showcaseAdapter.getSelectedItems()
            onButtonApplyClick(selectedShowcases)
            dismiss()
        }
    }

    fun setOnApplyButtonClick(onButtonApplyClick : (List<ShopShowcase>) -> Unit) {
        this.onButtonApplyClick = onButtonApplyClick
    }

}
