package com.tokopedia.product.addedit.preview.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.addedit.databinding.BottomsheetIneligibleAccessWarningBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class IneligibleAccessWarningBottomSheet : BottomSheetUnify() {

    companion object {
        private const val DEFAULT_IMAGE_URL = "https://assets.tokopedia.net/assets-tokopedia-lite/v2/icarus/kratos/d56aca5b.png "

        @JvmStatic
        fun newInstance() = IneligibleAccessWarningBottomSheet()
    }

    private var binding by autoClearedNullable<BottomsheetIneligibleAccessWarningBinding>()
    private var onButtonBackClicked : () -> Unit = {}
    private var onButtonLearningProblem : () -> Unit = {}

    init {
        clearContentPadding = true
        isSkipCollapseState = true
        isKeyboardOverlap = false
        showCloseIcon = false
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
        binding = BottomsheetIneligibleAccessWarningBinding.inflate(inflater, container, false)
        setChild(binding?.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        binding?.run {
            imageView.loadImage(DEFAULT_IMAGE_URL)
            buttonBack.setOnClickListener {
                onButtonBackClicked()
                dismiss()
            }

            buttonLearnProblem.setOnClickListener {
                onButtonLearningProblem()
            }
        }
    }

    fun setOnButtonBackClicked(onButtonClicked : () -> Unit = {}) {
        this.onButtonBackClicked = onButtonClicked
    }

    fun setOnButtonLearningProblemClicked(onButtonClicked : () -> Unit = {}) {
        this.onButtonLearningProblem = onButtonClicked
    }


}
