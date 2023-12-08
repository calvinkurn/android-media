package com.tokopedia.search.result.product.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductInappropriateBottomsheetBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class BottomSheetInappropriate : BottomSheetUnify() {

    companion object {
        const val IS_ADULT = "is_adult"
        private const val INAPPROPRIATE_SUCCESS_IMAGE_URL = "https://images.tokopedia.net/img/android/search-result/i/icon_warning_safe_produc_xhdpi.png"

        fun newInstance(
            isAdult: Boolean,
        ) = BottomSheetInappropriate().also {
            it.arguments = Bundle().apply {
                putBoolean(IS_ADULT, isAdult)
            }
        }
    }

    private var binding by autoClearedNullable<SearchResultProductInappropriateBottomsheetBinding>()
    private var onButtonConfirmationClicked: () -> Unit = {}
    private val isAdult by lazy { arguments?.getBoolean(IS_ADULT).orFalse() }

    init {
        clearContentPadding = true
        isSkipCollapseState = true
        isKeyboardOverlap = false
        showCloseIcon = true
        overlayClickDismiss = false
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
        binding = SearchResultProductInappropriateBottomsheetBinding.inflate(inflater, container, false)
        setChild(binding?.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        binding?.run {
            val textDesc = generateDescMessageInappropriate(isAdult)
            searchInappropriateImage.loadImage(INAPPROPRIATE_SUCCESS_IMAGE_URL)
            searchInappropriateNonActiveButton.showWithCondition(isAdult)
            searchInappropriateDescription.text = MethodChecker.fromHtml(textDesc)
            searchInappropriateNonActiveButton.setOnClickListener {
                onButtonConfirmationClicked()
                dismiss()
            }
        }
    }

    fun setOnButtonConfirmationClicked(onButtonConfirmationClicked: () -> Unit = {}) {
        this.onButtonConfirmationClicked = onButtonConfirmationClicked
    }

    private fun generateDescMessageInappropriate(isAdult: Boolean): String {
        return if (isAdult) {
           context?.getString(R.string.search_result_product_inappropriate_description_twenty_one)
        } else {
            context?.getString(R.string.search_result_product_inappropriate_description_under_twenty_one)
        }.orEmpty()
    }

}
