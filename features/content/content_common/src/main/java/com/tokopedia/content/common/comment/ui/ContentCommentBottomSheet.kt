package com.tokopedia.content.common.comment.ui

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.text.toSpanned
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
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
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.math.roundToInt
import com.tokopedia.unifyprinciples.R as unifyR

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

    private val sheetMenu by lazyThreadSafetyNone {
        ContentThreeDotsMenuBottomSheet.getFragment(
            childFragmentManager,
            requireActivity().classLoader
        )
    }

    private val textWatcher by lazyThreadSafetyNone {
        object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                binding.ivCommentSend.background.setTint(
                    if (!p0.isNullOrBlank()) MethodChecker.getColor(
                        requireContext(),
                        unifyR.color.Unify_GN500
                    ) else MethodChecker.getColor(requireContext(), unifyR.color.Unify_NN300)
                )

                if (p0 == null) return
                binding.newComment.removeTextChangedListener(this)

                if (p0.length > MAX_CHAR) {
                    Toaster.showError(
                        requireView().rootView,
                        getString(R.string.content_comment_error_max),
                        Toaster.LENGTH_SHORT
                    )
                }

                val newText =
                    TagMentionBuilder.spanText(p0.toSpanned(), textLength = p0.length.orZero())
                binding.newComment.setText(newText)

                binding.newComment.setSelection(binding.newComment.length())
                binding.newComment.addTextChangedListener(this)
            }
        }
    }

    private val toasterCallback by lazyThreadSafetyNone {
        object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)

                viewModel.submitAction(CommentAction.PermanentRemoveComment)
            }
        }
    }


    private val adapterObserver by lazyThreadSafetyNone {
        object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (binding.rvComment.childCount > 0) binding.rvComment.scrollToPosition(0)
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
            mSource?.onCommentDismissed()
            dismiss()
        }
        binding.rvComment.adapter = commentAdapter
        binding.rvComment.addOnScrollListener(scrollListener)
        commentAdapter.registerAdapterDataObserver(adapterObserver)

        binding.ivCommentSend.setOnClickListener {
            handleSendComment()
        }
        Toaster.toasterCustomBottomHeight = resources.getDimensionPixelSize(R.dimen.unify_space_48)
        binding.newComment.addTextChangedListener(textWatcher)
    }

    private fun observeData() {
        lifecycleScope.launchWhenStarted {
            viewModel.comments.collectLatest {
                when (it.state) {
                    ResultState.Success -> {
                        showError(false)
                        commentAdapter.setItemsAndAnimateChanges(it.list.ifEmpty { listOf(CommentUiModel.Empty) })
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

    private fun observeEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {
                    is CommentEvent.ShowSuccessToaster -> {
                        val toaster = Toaster.build(
                            requireView().rootView,
                            text = getString(R.string.comment_delete_kembali),
                            actionText = getString(R.string.comment_delete_undo),
                            duration = Toaster.LENGTH_LONG,
                            clickListener = {
                                viewModel.submitAction(CommentAction.DeleteComment(isFromToaster = true))
                            }
                        )
                        toaster.addCallback(toasterCallback)
                        toaster.show()
                    }
                    is CommentEvent.ShowErrorToaster -> {
                        val view =
                            if (sheetMenu.isVisible) sheetMenu.requireView().rootView else requireView().rootView
                        Toaster.build(
                            view,
                            text = event.message.message.orEmpty(),
                            actionText = getString(R.string.feed_content_coba_lagi_text),
                            duration = Toaster.LENGTH_LONG,
                            clickListener = {
                                run { event.onClick() }
                            }
                        ).show()
                    }
                    is CommentEvent.OpenAppLink -> {
                        router.route(
                            context = requireContext(),
                            appLinkPattern = event.appLink
                        )
                    }
                    CommentEvent.ShowKeyboard -> {
                        binding.newComment.requestFocus()
                        showKeyboard(true)
                    }
                    CommentEvent.HideKeyboard -> {
                        binding.newComment.setText("")
                        showKeyboard(false)
                    }
                    CommentEvent.OpenReportEvent -> sheetMenu.showReportLayoutWhenLaporkanClicked()
                    CommentEvent.ReportSuccess -> sheetMenu.setFinalView()
                    CommentEvent.ReportSuccess -> {
                        binding.newComment.text = null
                        binding.rvComment.scrollToPosition(0)
                    }
                    is CommentEvent.AutoType -> {
                        binding.newComment.setText("")
                        val mention = TagMentionBuilder.createNewMentionTag(
                            event.parentItem
                        )
                        binding.newComment.tag = event.parentItem.id
                        binding.newComment.setText(mention)
                        binding.newComment.requestFocus()
                        showKeyboard(true)
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
            binding.commentGlobalError.errorSecondaryAction.text =
                getString(R.string.content_comment_error_secondary)
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

    override fun onReplyClicked(item: CommentUiModel.Item) {
        viewModel.submitAction(CommentAction.EditTextClicked(item))
    }

    override fun onLongClicked(item: CommentUiModel.Item) {
        viewModel.submitAction(CommentAction.SelectComment(item))
        sheetMenu.setListener(this@ContentCommentBottomSheet)
        sheetMenu.setData(getMenuItems(item), item.id)
        sheetMenu.show(childFragmentManager)
    }

    override fun onMentionClicked(appLink: String) {
        viewModel.submitAction(CommentAction.OpenAppLinkAction(appLink))
    }

    override fun onProfileClicked(appLink: String) {
        viewModel.submitAction(CommentAction.OpenAppLinkAction(appLink))
    }

    override fun onUserNameClicked(appLink: String) {
        viewModel.submitAction(CommentAction.OpenAppLinkAction(appLink))
    }

    override fun onClicked(item: CommentUiModel.Expandable, position: Int) {
        viewModel.submitAction(CommentAction.ExpandComment(item))
    }

    override fun dismiss() {
        if (!isAdded) return
        mSource?.onCommentDismissed()
        viewModel.submitAction(CommentAction.DismissComment)
        super.dismiss()
    }

    override fun onCancel(dialog: DialogInterface) {
        mSource?.onCommentDismissed()
        viewModel.submitAction(CommentAction.DismissComment)
        super.onCancel(dialog)
    }

    override fun onResume() {
        super.onResume()

        val window = dialog?.window
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        binding.root.layoutParams.height = newHeight
        binding.ivUserPhoto.loadImage(viewModel.userInfo.profilePicture)
        viewModel.submitAction(CommentAction.RefreshComment)
    }

    private fun showKeyboard(needToShow: Boolean) {
        val imm =
            binding.newComment.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (needToShow) imm.showSoftInput(binding.newComment, InputMethodManager.SHOW_IMPLICIT)
        else imm.hideSoftInputFromWindow(binding.newComment.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        setEntrySource(null)
        binding.rvComment.removeOnScrollListener(scrollListener)
        commentAdapter.unregisterAdapterDataObserver(adapterObserver)
        _binding = null
    }

    private val getCommentShimmering: List<CommentUiModel.Shimmer> = List(SHIMMER_VALUE) {
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

    override fun onMenuBottomSheetCloseClick(contentId: String) {
        sheetMenu.dismiss()
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun getMenuItems(item: CommentUiModel.Item): List<FeedMenuItem> = buildList {
        if (item.isOwner || viewModel.isCreator) {
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
    }

    private fun handleSendComment() {
        if (binding.newComment.length() >= MAX_CHAR) {
            Toaster.showError(
                requireView().rootView,
                getString(R.string.content_comment_error_max),
                Toaster.LENGTH_SHORT
            )
        } else {
            val convert = TagMentionBuilder.getRawText(binding.newComment.text?.toSpanned())
            viewModel.submitAction(
                CommentAction.ReplyComment(
                    convert, TagMentionBuilder.isChildOrParent(
                        binding.newComment.text?.toSpanned(), binding.newComment.tag.toString()
                    )
                )
            )
        }
    }

    interface EntrySource {
        fun getPageSource(): PageSource

        fun onCommentDismissed()
    }

    companion object {
        private const val TAG = "ContentCommentBottomSheet"

        private const val HEIGHT_PERCENT = 0.8
        private const val SHIMMER_VALUE = 10

        private const val MAX_CHAR = 140

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
