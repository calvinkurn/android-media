package com.tokopedia.shop.info.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.databinding.BottomsheetShopNoteDetailBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ShopNoteDetailBottomSheet : BottomSheetUnify() {

    companion object {
        private const val BUNDLE_KEY_SHOP_TITLE = "title"
        private const val BUNDLE_KEY_SHOP_DESCRIPTION = "description"

        @JvmStatic
        fun newInstance(title: String, description: String): ShopNoteDetailBottomSheet {
            return ShopNoteDetailBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(BUNDLE_KEY_SHOP_TITLE, title)
                    putString(BUNDLE_KEY_SHOP_DESCRIPTION, description)
                }
            }
        }
    }

    private var binding by autoClearedNullable<BottomsheetShopNoteDetailBinding>()

    private val title by lazy {
        arguments?.getString(BUNDLE_KEY_SHOP_TITLE)
    }
    
    private val description by lazy {
        arguments?.getString(BUNDLE_KEY_SHOP_DESCRIPTION)
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
        binding = BottomsheetShopNoteDetailBinding.inflate(inflater, container, false)
        setChild(binding?.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.tpgShopNoteTitle?.text = MethodChecker.fromHtml(title)
        binding?.tpgShopNoteDescription?.text = MethodChecker.fromHtml(description)
    }
    
}
