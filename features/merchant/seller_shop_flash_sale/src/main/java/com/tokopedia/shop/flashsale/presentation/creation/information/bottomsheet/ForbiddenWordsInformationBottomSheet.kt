package com.tokopedia.shop.flashsale.presentation.creation.information.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

class ForbiddenWordsInformationBottomSheet: BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(context: Context): ForbiddenWordsInformationBottomSheet =
            ForbiddenWordsInformationBottomSheet().apply {
                val view = View.inflate(
                    context,
                    R.layout.ssfs_bottom_sheet_forbidden_words_info,
                    null
                )
                setChild(view)
            }

        private const val TAG = "ForbiddenWordsBottomSheet"
    }

    private var mForbiddenWords: Typography? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView(view)
        setupContent()
        super.onViewCreated(view, savedInstanceState)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun setupView(view: View) {
        setTitle(getString(R.string.forbidden_words_info_title))

        mForbiddenWords = view.findViewById(R.id.tg_forbidden_words)
        showCloseIcon = true
    }

    @SuppressLint("ResourcePackage")
    private fun setupContent() {
        mForbiddenWords?.text = getString(R.string.forbidden_words_info_content)
    }

}