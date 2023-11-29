package com.tokopedia.search.result.product.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.search.databinding.SearchResultProductInappropriateBottomsheetBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class BottomSheetInappropriate: BottomSheetUnify() {

    companion object {
        const val IS_ADULT = "is_adult"
        const val DESC_INAPPROPRIATE = "desc_inappropriate"
        fun newInstance(
            isAdult : Boolean,
            descMessage : String,
        ) = BottomSheetInappropriate().also {
            it.arguments = Bundle().apply {
                putBoolean(IS_ADULT, isAdult)
                putString(DESC_INAPPROPRIATE, descMessage)
            }
        }
    }

    private var binding by autoClearedNullable<SearchResultProductInappropriateBottomsheetBinding>()
    private var onButtonConfirmationClicked: () -> Unit = {}
    private val isAdult by lazy { arguments?.getBoolean(IS_ADULT).orFalse() }
    private val descMessage by lazy { arguments?.getString(DESC_INAPPROPRIATE).orEmpty() }

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
            searchInappropriateNonActiveButton.showWithCondition(isAdult)
            searchInappropriateDescription.text = descMessage
            searchInappropriateNonActiveButton.setOnClickListener {
                onButtonConfirmationClicked()
                dismiss()
            }
        }
    }

    fun setOnButtonConfirmationClicked(onButtonConfirmationClicked: () -> Unit = {}) {
        this.onButtonConfirmationClicked = onButtonConfirmationClicked
    }

}
