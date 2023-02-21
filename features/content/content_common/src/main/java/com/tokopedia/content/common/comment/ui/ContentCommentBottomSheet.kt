package com.tokopedia.content.common.comment.ui

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.content.common.R
import com.tokopedia.content.common.comment.*
import com.tokopedia.content.common.comment.adapter.CommentAdapter
import com.tokopedia.content.common.comment.adapter.CommentViewHolder
import com.tokopedia.content.common.comment.uimodel.CommentType
import com.tokopedia.content.common.comment.uimodel.CommentUiModel
import com.tokopedia.content.common.databinding.FragmentContentCommentBottomSheetBinding
import com.tokopedia.content.common.report_content.bottomsheet.ContentThreeDotsMenuBottomSheet
import com.tokopedia.content.common.report_content.model.FeedMenuIdentifier
import com.tokopedia.content.common.report_content.model.FeedMenuItem
import com.tokopedia.content.common.report_content.model.FeedReportRequestParamModel
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.content.common.usecase.FeedComplaintSubmitReportUseCase
import com.tokopedia.content.common.util.Router
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * @author by astidhiyaa on 09/02/23
 */
class ContentCommentBottomSheet @Inject constructor(
    factory: ContentCommentFactory.Creator,
    private val router: Router
) : BottomSheetUnify(),
    CommentViewHolder.Item.Listener,
    CommentViewHolder.Expandable.Listener,
    ContentThreeDotsMenuBottomSheet.Listener {

    private var _binding: FragmentContentCommentBottomSheetBinding? = null
    private val binding: FragmentContentCommentBottomSheetBinding
        get() = _binding!!

    private val newHeight by lazyThreadSafetyNone {
        (getScreenHeight() * HEIGHT_PERCENT).roundToInt()
    }

    private var mSource: EntrySource? = null

    private val viewModel: ContentCommentViewModel by viewModels {
        factory.create(this, mSource?.getPageSource() ?: PageSource.Unknown)
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

    private val sheetMenu by lazyThreadSafetyNone {
        ContentThreeDotsMenuBottomSheet.getFragment(
            childFragmentManager,
            requireActivity().classLoader
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeData()
        observeEvent()
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

        Toaster.toasterCustomBottomHeight = resources.getDimensionPixelSize(R.dimen.unify_space_48)
    }

    private fun observeData() {
        lifecycleScope.launchWhenStarted {
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
                        binding.commentGlobalError.setType(
                            if (it.state.error is UnknownHostException) GlobalError.NO_CONNECTION else GlobalError.SERVER_ERROR
                        )
                        binding.commentGlobalError.setActionClickListener {
                            viewModel.submitAction(CommentAction.RefreshComment)
                        }
                        showError(true)
                    }
                }
            }
        }
    }

    private fun observeEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {
                    is CommentEvent.ShowSuccessToaster -> {
                        val toaster = Toaster.build(
                            requireView().rootView,
                            text = event.message.orEmpty(),
                            actionText = getString(R.string.comment_delete_undo),
                            duration = Toaster.LENGTH_LONG,
                            clickListener = { event.onClick }
                        )
                        toaster.show()

                        toaster.view.addOneTimeGlobalLayoutListener {
                            if (!isVisible) viewModel.submitAction(CommentAction.DeleteComment(isFromToaster = false))
                        }
                    }
                    is CommentEvent.ShowErrorToaster -> {
                        Toaster.build(
                            requireView().rootView,
                            text = event.message.message.orEmpty(),
                            actionText = getString(R.string.feed_content_coba_lagi_text),
                            duration = Toaster.LENGTH_LONG,
                            clickListener = { event.onClick }
                        ).show()
                    }
                    is CommentEvent.OpenAppLink -> {
                        router.route(context = requireContext(), appLinkPattern = event.appLink)
                    }
                    CommentEvent.OpenReportEvent -> sheetMenu.showReportLayoutWhenLaporkanClicked()
                }
            }
        }
    }

    private fun showError(isShown: Boolean) {
        if (isShown) {
            binding.commentGlobalError.show()
            binding.rvComment.hide()
            binding.commentHeader.hide()
            binding.viewCommentSend.hide()
        } else {
            binding.commentGlobalError.hide()
            binding.rvComment.show()
            binding.commentHeader.show()
            binding.viewCommentSend.show()
        }
    }

    fun setEntrySource(source: EntrySource?) {
        mSource = source
    }

    fun show(fragmentManager: FragmentManager) {
        if (isAdded) return
        showNow(fragmentManager, TAG)
        viewModel.submitAction(CommentAction.RefreshComment)
    }

    override fun onReplyClicked(item: CommentUiModel.Item) {
        // TODO("Not yet implemented")
    }

    override fun onLongClicked(item: CommentUiModel.Item) {
        viewModel.submitAction(CommentAction.SelectComment(item))
        sheetMenu.setListener(this@ContentCommentBottomSheet)
        sheetMenu.setData(getMenuItems(item), item.id)
        sheetMenu.show(childFragmentManager)
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        setEntrySource(null)
        _binding = null
    }

    private val getCommentShimmering: List<CommentUiModel.Shimmer> = List(6) {
        CommentUiModel.Shimmer
    }

    override fun onMenuItemClick(feedMenuItem: FeedMenuItem, contentId: String) {
        when (feedMenuItem.type) {
            FeedMenuIdentifier.DELETE -> {
                viewModel.submitAction(CommentAction.DeleteComment(isFromToaster = false))
            }
            FeedMenuIdentifier.LAPORKAN -> viewModel.submitAction(CommentAction.RequestReportAction)
        }
    }

    override fun onReportPost(feedReportRequestParamModel: FeedReportRequestParamModel) {
        viewModel.submitAction(
            CommentAction.ReportComment(
                feedReportRequestParamModel.copy(
                    reportType = FeedComplaintSubmitReportUseCase.VALUE_REPORT_TYPE_COMMENT
                )
            )
        )
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun getMenuItems(item: CommentUiModel.Item): List<FeedMenuItem> = buildList {
        if (item.isOwner) {
            add(
                FeedMenuItem(
                    name = getString(R.string.content_common_menu_delete),
                    drawable = getIconUnifyDrawable(
                        context = requireContext(),
                        iconId = IconUnify.DELETE
                    ),
                    type = FeedMenuIdentifier.DELETE
                )
            )
        }
        if (item.isReportAllowed && !item.isOwner) {
            add(
                FeedMenuItem(
                    drawable = getIconUnifyDrawable(
                        requireContext(),
                        IconUnify.WARNING,
                        MethodChecker.getColor(
                            context,
                            R.color.Unify_RN500
                        )
                    ),
                    name = getString(R.string.content_common_menu_report),
                    type = FeedMenuIdentifier.LAPORKAN
                )
            )
        }
        Toast.LENGTH_LONG
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) return

        viewModel.submitAction(CommentAction.ResultAction(requestCode))
    }

    interface EntrySource {
        fun getPageSource(): PageSource
    }

    companion object {
        private const val TAG = "ContentCommentBottomSheet"

        private const val HEIGHT_PERCENT = 0.8

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
