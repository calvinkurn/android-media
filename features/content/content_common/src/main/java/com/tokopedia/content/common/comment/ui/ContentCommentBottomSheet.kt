package com.tokopedia.content.common.comment.ui

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.content.common.R
import com.tokopedia.content.common.comment.CommentAction
import com.tokopedia.content.common.comment.ContentCommentFactory
import com.tokopedia.content.common.comment.ContentCommentViewModel
import com.tokopedia.content.common.comment.PageSource
import com.tokopedia.content.common.comment.adapter.CommentAdapter
import com.tokopedia.content.common.comment.adapter.CommentViewHolder
import com.tokopedia.content.common.comment.uimodel.CommentType
import com.tokopedia.content.common.comment.uimodel.CommentUiModel
import com.tokopedia.content.common.databinding.FragmentContentCommentBottomSheetBinding
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.content.common.util.Router
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * @author by astidhiyaa on 09/02/23
 */
class ContentCommentBottomSheet @Inject constructor(
    factory: ContentCommentFactory.Creator,
    private val router: Router
) : BottomSheetUnify(), CommentViewHolder.Item.Listener, CommentViewHolder.Expandable.Listener {

    private var _binding: FragmentContentCommentBottomSheetBinding? = null
    private val binding: FragmentContentCommentBottomSheetBinding
        get() = _binding!!

    private val newHeight by lazyThreadSafetyNone {
        (getScreenHeight() * HEIGHT_PERCENT).roundToInt()
    }

    private var mSource: EntrySource? = null

    private val viewModel: ContentCommentViewModel by viewModels {
        factory.create(mSource?.getPageSource() ?: PageSource.Unknown)
    }

    private val commentAdapter by lazyThreadSafetyNone {
        CommentAdapter(this, this)
    }

    private val scrollListener by lazyThreadSafetyNone {
        object : EndlessRecyclerViewScrollListener(binding.rvComment.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                viewModel.submitAction(CommentAction.LoadNextPage(CommentType.Parent))
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
                when (it.state) {
                    ResultState.Success -> {
                        showError(false)
                        commentAdapter.setItemsAndAnimateChanges(it.list)
                    }
                    ResultState.Loading -> {
                        showError(false)
                        commentAdapter.setItemsAndAnimateChanges(getCommentShimmering)
                    }
                    is ResultState.Fail -> {
                        showError(true, throwable = it.state.error)
                    }
                }
            }
        }
    }

    private fun showError(isShown: Boolean, throwable: Throwable? = null) {
        binding.commentGlobalError.showWithCondition(isShown)
        binding.rvComment.showWithCondition(!isShown)
        binding.commentHeader.showWithCondition(!isShown)
        binding.viewCommentSend.showWithCondition(!isShown)

        if (throwable is UnknownHostException) {
            binding.commentGlobalError.setType(GlobalError.NO_CONNECTION)
            binding.commentGlobalError.errorSecondaryAction.show()
            binding.commentGlobalError.errorSecondaryAction.text = getString(R.string.content_comment_error_secondary)
            binding.commentGlobalError.setSecondaryActionClickListener {
                val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                router.route(requireActivity(), intent)
            }
        } else {
            binding.commentGlobalError.errorSecondaryAction.hide()
            binding.commentGlobalError.setType(GlobalError.SERVER_ERROR)
        }

        binding.commentGlobalError.setActionClickListener {
            viewModel.submitAction(CommentAction.RefreshComment)
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
        // TODO("Not yet implemented")
    }

    override fun onLongClicked(item: CommentUiModel) {
        // TODO("Not yet implemented")
    }

    override fun onClicked(item: CommentUiModel.Expandable, position: Int) {
        viewModel.submitAction(CommentAction.ExpandComment(item))
    }

    override fun dismiss() {
        if (!isAdded) return
        viewModel.submitAction(CommentAction.DismissComment)
        super.dismiss()
    }

    override fun onCancel(dialog: DialogInterface) {
        viewModel.submitAction(CommentAction.DismissComment)
        super.onCancel(dialog)
    }

    override fun onResume() {
        super.onResume()

        binding.root.layoutParams.height = newHeight
        viewModel.submitAction(CommentAction.RefreshComment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        setEntrySource(null)
        binding.rvComment.removeOnScrollListener(scrollListener)
        _binding = null
    }

    private val getCommentShimmering: List<CommentUiModel.Shimmer> = List(SHIMMER_VALUE) {
        CommentUiModel.Shimmer
    }

    interface EntrySource {
        fun getPageSource(): PageSource
    }

    companion object {
        private const val TAG = "ContentCommentBottomSheet"

        private const val HEIGHT_PERCENT = 0.8
        private const val SHIMMER_VALUE = 10

        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): ContentCommentBottomSheet {
            return fragmentManager.findFragmentByTag(TAG) as? ContentCommentBottomSheet
                ?: fragmentManager.fragmentFactory.instantiate(
                    classLoader,
                    ContentCommentBottomSheet::class.java.name
                ) as ContentCommentBottomSheet
        }
    }
}
