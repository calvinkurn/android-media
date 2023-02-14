package com.tokopedia.content.common.comment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.content.common.comment.ContentCommentFactory
import com.tokopedia.content.common.databinding.FragmentContentCommentBottomSheetBinding
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlin.math.roundToInt
import com.tokopedia.content.common.R
import com.tokopedia.content.common.comment.ContentCommentViewModel
import com.tokopedia.content.common.comment.PageSource
import com.tokopedia.content.common.comment.adapter.CommentAdapter
import com.tokopedia.content.common.comment.adapter.CommentViewHolder
import com.tokopedia.content.common.comment.uimodel.CommentUiModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by astidhiyaa on 09/02/23
 */
class ContentCommentBottomSheet @Inject constructor(
    factory: ContentCommentFactory.Creator,
) : BottomSheetUnify(), CommentViewHolder.Item.Listener {

    private var _binding: FragmentContentCommentBottomSheetBinding? = null
    private val binding: FragmentContentCommentBottomSheetBinding
        get() = _binding!!

    private val newHeight by lazyThreadSafetyNone {
        (getScreenHeight() * HEIGHT_PERCENT).roundToInt()
    }

    private var mSource: EntrySource? = null

    private val viewModel: ContentCommentViewModel by activityViewModels {
        factory.create(this, mSource?.getPageSource() ?: PageSource.Unknown)
    }

    private val commentAdapter by lazyThreadSafetyNone {
        CommentAdapter(this)
    }

    private val scrollListener by lazyThreadSafetyNone {
        object : EndlessRecyclerViewScrollListener(binding.rvComment.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                //TODO("Not yet implemented")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeData()
    }

    private fun setupBottomSheet() {
        _binding =
            FragmentContentCommentBottomSheetBinding.inflate(LayoutInflater.from(requireContext()))
        setChild(binding.root)

        clearContentPadding = true
        isDragable = false
        showKnob = false
        showHeader = false
    }

    private fun setupView() {
        binding.commentHeader.title = getString(R.string.content_comment_header)
        binding.commentHeader.closeListener = View.OnClickListener {
            dismiss()
        }
        binding.rvComment.adapter = commentAdapter
        binding.rvComment.addOnScrollListener(scrollListener)
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.comments.collectLatest {
                commentAdapter.setItemsAndAnimateChanges(it)
            }
        }
    }

    fun setEntrySource(source: EntrySource?) {
        mSource = source
    }

    fun show(fragmentManager: FragmentManager) {
        if (isAdded) return
        showNow(fragmentManager, TAG)
    }

    override fun onReplyClicked(item: CommentUiModel) {
        //TODO("Not yet implemented")
    }

    override fun onLongClicked(item: CommentUiModel) {
        //TODO("Not yet implemented")
    }

    override fun dismiss() {
        if (!isAdded) return
        super.dismiss()
    }

    override fun onResume() {
        super.onResume()

        binding.root.layoutParams.height = newHeight
    }

    override fun onDestroyView() {
        super.onDestroyView()
        setEntrySource(null)
        _binding = null
    }

    interface EntrySource {
        fun getPageSource(): PageSource
    }

    companion object {
        private const val TAG = "ContentCommentBottomSheet"

        private const val HEIGHT_PERCENT = 0.8

        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): ContentCommentBottomSheet {
            return fragmentManager.findFragmentByTag(TAG) as? ContentCommentBottomSheet
                ?: fragmentManager.fragmentFactory.instantiate(
                    classLoader,
                    ContentCommentBottomSheet::class.java.name
                ) as ContentCommentBottomSheet
        }
    }
}
