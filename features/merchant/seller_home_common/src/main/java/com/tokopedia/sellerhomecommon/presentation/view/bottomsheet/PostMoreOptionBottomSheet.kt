package com.tokopedia.sellerhomecommon.presentation.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcBottomSheetPostMoreOptionBinding
import com.tokopedia.sellerhomecommon.presentation.view.adapter.PostMoreOptionAdapter

/**
 * Created by @ilhamsuaib on 23/08/22.
 */

class PostMoreOptionBottomSheet : BaseBottomSheet<ShcBottomSheetPostMoreOptionBinding>() {

    companion object {
        private const val TAG = "PostMoreOptionBottomSheet"

        fun createInstance(): PostMoreOptionBottomSheet {
            return PostMoreOptionBottomSheet()
        }
    }

    private var onRemoveArticleOptionCallback: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ShcBottomSheetPostMoreOptionBinding.inflate(inflater, container, false).apply {
            setChild(root)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setupView() = binding?.run {
        setTitle(root.context.getString(R.string.shc_post_more_title))
        setupOptions()
    }

    fun setOnRemoveArticleOptionClicked(onRemoveArticleOptionCallback: () -> Unit) {
        this.onRemoveArticleOptionCallback = onRemoveArticleOptionCallback
    }

    fun show(fm: FragmentManager) {
        if (fm.isStateSaved || isVisible) {
            return
        }

        show(fm, TAG)
    }

    private fun setupOptions() {
        binding?.rvShcPostMoreOption?.run {
            val options = listOf(
                context.getString(R.string.shc_post_more_option_1)
            )

            val optionAdapter = PostMoreOptionAdapter(options, onRemoveArticleOptionCallback)

            adapter = optionAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}