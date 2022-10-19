package com.tokopedia.review.feature.createreputation.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.databinding.BottomsheetCreateReviewAnonymousInfoBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.view.binding.noreflection.viewBinding

class CreateReviewAnonymousInfoBottomSheet : BottomSheetUnify() {

    companion object {
        const val TAG = "CreateReviewAnonymousInfoBottomSheet"

        private const val ILLUSTRATION_URL = "https://upload.wikimedia.org/wikipedia/en/thumb/3/33/Patrick_Star.svg/1200px-Patrick_Star.svg.png"
    }

    private var binding by viewBinding(BottomsheetCreateReviewAnonymousInfoBinding::bind)
    private var listener: Listener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupListeners()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetCreateReviewAnonymousInfoBinding.inflate(inflater).also {
            setChild(it.root)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    private fun setupListeners() {
        setupOnDismissListener()
    }

    private fun setupOnDismissListener() {
        setOnDismissListener { listener?.onDismissCreateReviewAnonymousInfoBottomSheet() }
    }

    private fun setupViews() {
        setupIllustration()
    }

    private fun setupIllustration() {
        binding?.ivCreateReviewAnonymousInfoIllustration?.loadImage(ILLUSTRATION_URL)
    }

    interface Listener {
        fun onDismissCreateReviewAnonymousInfoBottomSheet()
    }
}
