package com.tokopedia.shop.flashsale.presentation.creation.information.bottomsheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsBottomSheetForbiddenWordsInfoBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ForbiddenWordsInformationBottomSheet: BottomSheetUnify() {

    companion object {
        private const val TAG = "ForbiddenWordsBottomSheet"
    }

    private var binding by autoClearedNullable<SsfsBottomSheetForbiddenWordsInfoBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SsfsBottomSheetForbiddenWordsInfoBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        setupContent()
        super.onViewCreated(view, savedInstanceState)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun setupView() {
        setTitle(getString(R.string.forbidden_words_info_title))
        showCloseIcon = true
    }

    @SuppressLint("ResourcePackage")
    private fun setupContent() {
        binding?.run {
            tgForbiddenWords.text = getString(R.string.forbidden_words_info_content)
        }
    }

}