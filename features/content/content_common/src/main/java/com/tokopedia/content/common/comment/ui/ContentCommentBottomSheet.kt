package com.tokopedia.content.common.comment.ui

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.icu.text.BreakIterator
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.text.toSpanned
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.content.common.R
import com.tokopedia.content.common.comment.CommentAction
import com.tokopedia.content.common.comment.CommentEvent
import com.tokopedia.content.common.comment.CommentException
import com.tokopedia.content.common.comment.ContentCommentFactory
import com.tokopedia.content.common.comment.ContentCommentViewModel
import com.tokopedia.content.common.comment.PageSource
import com.tokopedia.content.common.comment.TagMentionBuilder
import com.tokopedia.content.common.comment.adapter.CommentAdapter
import com.tokopedia.content.common.comment.adapter.CommentViewHolder
import com.tokopedia.content.common.comment.analytic.IContentCommentAnalytics
import com.tokopedia.content.common.comment.uimodel.CommentType
import com.tokopedia.content.common.comment.uimodel.CommentUiModel
import com.tokopedia.content.common.comment.uimodel.isParent
import com.tokopedia.content.common.databinding.FragmentContentCommentBottomSheetBinding
import com.tokopedia.content.common.report_content.bottomsheet.ContentThreeDotsMenuBottomSheet
import com.tokopedia.content.common.report_content.model.FeedMenuIdentifier
import com.tokopedia.content.common.report_content.model.FeedMenuItem
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.content.common.usecase.FeedComplaintSubmitReportUseCase
import com.tokopedia.content.common.util.ConnectionHelper
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.common.view.getImeHeight
import com.tokopedia.content.common.view.isImeVisible
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toPx
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

    private val keyboardThreshold by lazyThreadSafetyNone {
        (getScreenHeight() * KEYBOARD_HEIGHT_PERCENT).roundToInt().plus(16.toPx())
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
        ContentThreeDotsMenuBottomSheet.getOrCreateFragment(
            childFragmentManager,
            requireActivity().classLoader
        )
    }

    private val disabledColor by lazyThreadSafetyNone {
        MethodChecker.getColor(requireContext(), unifyR.color.Unify_NN300)
    }

    private val enabledColor by lazyThreadSafetyNone {
        MethodChecker.getColor(
            requireContext(),
            unifyR.color.Unify_GN500
        )
    }

    private val textWatcher by lazyThreadSafetyNone {
        object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(txt: Editable?) {
                val newLength = txt.toString().getGraphemeLength()
                val isBtnDisabled = txt.isNullOrBlank() || newLength > MAX_CHAR
                binding.ivCommentSend.background.setTint(
                    if (isBtnDisabled) disabledColor else enabledColor
                )
                binding.ivCommentSend.isClickable = !isBtnDisabled
                binding.ivCommentSend.setOnClickListener(
                    if (isBtnDisabled) null else handleSendComment()
                )

                if (txt == null) return
                binding.newComment.removeTextChangedListener(this)

                if (newLength > MAX_CHAR) {
                    Toaster.showError(
                        requireView().rootView,
                        CommentException.createSendCommentFailed().message.orEmpty(),
                        Toaster.LENGTH_SHORT
                    )
                }

                val prevLength = binding.newComment.length()
                val selEnd = binding.newComment.selectionEnd
                val distanceFromEnd =
                    prevLength - selEnd // calculate cursor distance from end of text

                val newText =
                    TagMentionBuilder.spanText(txt.toSpanned(), textLength = newLength.orZero(), ctx = requireContext())
                binding.newComment.text?.clear()
                binding.newComment.append(newText)

                val currentLength = binding.newComment.length()
                if (distanceFromEnd in 1 until currentLength) {
                    binding.newComment.setSelection(selEnd)
                } else {
                    binding.newComment.setSelection(currentLength)
                }

                binding.newComment.addTextChangedListener(this)
            }
        }
    }

    private val toasterCallback by lazyThreadSafetyNone {
        object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                if (event == DISMISS_EVENT_TIMEOUT) viewModel.submitAction(CommentAction.PermanentRemoveComment)
            }
        }
    }

    private var analytics: IContentCommentAnalytics? = null
    private var isFromChild: Boolean = false

    // to escape Emoji length
    private fun String.getGraphemeLength(): Int {
        var count = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val it: BreakIterator = BreakIterator.getCharacterInstance()
            it.setText(this)
            while (it.next() != BreakIterator.DONE) {
                count++
            }
        } else {
            count = binding.newComment?.text?.length.orZero()
        }
        return count
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentContentCommentBottomSheetBinding.inflate(
                LayoutInflater.from(requireContext()),
                container,
                false
            )
        setChild(binding.root)
        setupBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeData()
        observeEvent()
        refreshData()
    }

    private fun setupBottomSheet() {
        clearContentPadding = true
        isDragable = false
        showKnob = false
        showHeader = false
    }

    private fun setupView() {
        binding.commentHeader.title = getString(R.string.content_comment_header)
        binding.commentHeader.closeListener = View.OnClickListener {
            mSource?.onCommentDismissed()
            analytics?.closeCommentSheet()
            dismiss()
        }
        binding.rvComment.adapter = commentAdapter
        binding.rvComment.addOnScrollListener(scrollListener)

        Toaster.toasterCustomBottomHeight =
            context?.resources?.getDimensionPixelSize(unifyR.dimen.unify_space_48).orZero()
        binding.newComment.addTextChangedListener(textWatcher)
        binding.root.setOnApplyWindowInsetsListener { view, windowInsets ->
            val height = view.getImeHeight()
            if (view.isImeVisible(threshold = keyboardThreshold)) {
                binding.root.setPadding(0, 0, 0, height)
            } else {
                binding.root.setPadding(0, 0, 0, 0)
            }
            windowInsets
        }
        binding.newComment.setOnTouchListener { view, motionEvent ->
            view.performClick()
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                analytics?.clickTextBox()
            }
            false
        }
    }

    private fun observeData() {
        lifecycleScope.launchWhenStarted {
            viewModel.comments.collectLatest {
                when (it.state) {
                    ResultState.Success -> {
                        showError(false)
                        scrollListener.updateStateAfterGetData()
                        commentAdapter.setItemsAndAnimateChanges(
                            it.list.ifEmpty {
                                listOf(
                                    CommentUiModel.Empty
                                )
                            }
                        )
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
                            text = if (event.message is UnknownHostException) getString(R.string.content_comment_error_connection) else event.message.message.orEmpty(),
                            actionText = if (!event.message.message?.equals(CommentException.createDeleteFailed().message.orEmpty())
                                    .orFalse()
                            ) "" else getString(R.string.feed_content_coba_lagi_text),
                            duration = Toaster.LENGTH_LONG,
                            clickListener = {
                                run { event.onClick() }
                            },
                            type = if (event.message.message?.equals(CommentException.createLinkNotAllowed().message.orEmpty())
                                    .orFalse()
                            ) {
                                Toaster.TYPE_ERROR
                            } else {
                                Toaster.TYPE_NORMAL
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
                        showKeyboard(true)
                    }
                    CommentEvent.HideKeyboard -> {
                        binding.newComment.setText("")
                        showKeyboard(false)
                    }
                    CommentEvent.OpenReportEvent -> sheetMenu.showReportLayoutWhenLaporkanClicked()
                    CommentEvent.ReportSuccess -> {
                        sheetMenu.setFinalView()
                        analytics?.impressSuccessReport()
                    }
                    is CommentEvent.ReplySuccess -> {
                        binding.newComment.text = null
                        binding.rvComment.scrollToPosition(event.position)
                        isFromChild = false
                    }
                    is CommentEvent.AutoType -> {
                        binding.newComment.setText("")
                        val mention = TagMentionBuilder.createNewMentionTag(
                            event.parentItem
                        )
                        binding.newComment.tag = if (event.parentItem.commentType.isParent) {
                            event.parentItem.id
                        } else {
                            event.parentItem.commentType.parentId
                        }
                        binding.newComment.setText(mention)
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
                getString(R.string.content_global_error_secondary_text)
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
        isFromChild = true
        viewModel.submitAction(CommentAction.EditTextClicked(item))
        analytics?.clickReplyChild()
    }

    override fun onLongClicked(item: CommentUiModel.Item) {
        analytics?.longClickComment()

        sheetMenu.setListener(this@ContentCommentBottomSheet)
        sheetMenu.setData(getMenuItems(item), item.id)
        sheetMenu.show(childFragmentManager)
    }

    override fun onMentionClicked(appLink: String) {
        viewModel.submitAction(CommentAction.OpenAppLinkAction(appLink))
    }

    override fun onProfileClicked(appLink: String) {
        viewModel.submitAction(CommentAction.OpenAppLinkAction(appLink))
        analytics?.clickProfilePicture()
    }

    override fun onUserNameClicked(appLink: String) {
        viewModel.submitAction(CommentAction.OpenAppLinkAction(appLink))
        analytics?.clickCommentName()
    }

    override fun onClicked(item: CommentUiModel.Expandable, position: Int) {
        viewModel.submitAction(CommentAction.ExpandComment(item))
        if (!item.isExpanded) analytics?.clickLihatBalasan() else analytics?.clickSembunyikan()
    }

    override fun onImpressedExpandable() {
        analytics?.impressLihatBalasan()
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

        binding.root.layoutParams = binding.root.layoutParams.apply {
            height = newHeight
        }

        val avatar = if (viewModel.userInfo.isShopAdmin) viewModel.userInfo.shopAvatar else viewModel.userInfo.profilePicture
        binding.ivUserPhoto.loadImage(avatar)
    }

    private fun showKeyboard(needToShow: Boolean) {
        val imm =
            binding.newComment.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (needToShow) {
            binding.newComment.requestFocus()
            imm.showSoftInput(binding.newComment, InputMethodManager.SHOW_IMPLICIT)
        } else {
            binding.newComment.clearFocus()
            imm.hideSoftInputFromWindow(binding.newComment.windowToken, 0)
        }
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

    override fun onMenuItemClick(feedMenuItem: FeedMenuItem, contentId: String) {
        when (feedMenuItem.type) {
            FeedMenuIdentifier.Delete -> deleteCommentChecker(contentId)
            FeedMenuIdentifier.Report -> {
                viewModel.submitAction(CommentAction.RequestReportAction)
                analytics?.clickReportComment()
            }
            else -> {}
        }
    }

    private fun deleteCommentChecker(id: String) {
        requireInternet {
            viewModel.submitAction(CommentAction.SelectComment(id))
            analytics?.clickRemoveComment()
            viewModel.submitAction(CommentAction.DeleteComment(isFromToaster = false))
        }
    }

    override fun onReportPost(feedReportRequestParamModel: FeedComplaintSubmitReportUseCase.Param) {
        analytics?.clickReportReason(feedReportRequestParamModel.reason)
        viewModel.submitAction(
            CommentAction.ReportComment (
                feedReportRequestParamModel.copy(
                    reportType = FeedComplaintSubmitReportUseCase.VALUE_REPORT_TYPE_COMMENT
                )
            )
        )
    }

    override fun onMenuBottomSheetCloseClick(contentId: String) {
        sheetMenu.dismiss()
    }

    private fun getMenuItems(item: CommentUiModel.Item): List<FeedMenuItem> = buildList {
        if (item.isOwner || viewModel.isCreator) {
            add(
                FeedMenuItem(
                    name = R.string.content_common_menu_delete,
                    iconUnify = IconUnify.DELETE,
                    type = FeedMenuIdentifier.Delete
                )
            )
        }
        if (item.isReportAllowed) {
            add(
                FeedMenuItem(
                    iconUnify = IconUnify.WARNING,
                    name = R.string.content_common_menu_report,
                    type = FeedMenuIdentifier.Report
                )
            )
        }
    }

    private fun handleSendComment() = View.OnClickListener {
        showKeyboard(false)
        val newLength = binding.newComment.text.toString().getGraphemeLength()
        if (newLength > MAX_CHAR) {
            Toaster.showError(
                requireView().rootView,
                CommentException.createSendCommentFailed().message.orEmpty(),
                Toaster.LENGTH_SHORT
            )
        } else {
            val convert = TagMentionBuilder.getRawText(binding.newComment.text?.toSpanned())
            val type = TagMentionBuilder.isChildOrParent(
                binding.newComment.text?.toSpanned(),
                binding.newComment.tag.toString()
            )
            viewModel.submitAction(
                CommentAction.ReplyComment(
                    convert,
                    type
                )
            )
            if (type.isParent) analytics?.clickSendParentComment() else analytics?.clickSendChildComment()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        dismiss()
    }

    private fun requireInternet(action: (isAvailable: Boolean) -> Unit) {
        val isInetAvailable =
            ConnectionHelper.isConnectWifi(requireContext()) || ConnectionHelper.isConnectCellular(
                requireContext()
            )
        if (isInetAvailable) {
            action(true)
        } else {
            Toaster.showErrorWithAction(
                requireView().rootView,
                getString(R.string.content_comment_error_connection),
                Toaster.LENGTH_LONG,
                getString(R.string.feed_content_coba_lagi_text)
            ) {
                requireInternet { action(it) }
            }
        }
    }

    fun setAnalytic(tracker: IContentCommentAnalytics) {
        analytics = tracker
    }

    private fun refreshData() {
        viewModel.submitAction(CommentAction.RefreshComment)
    }

    interface EntrySource {
        fun getPageSource(): PageSource

        fun onCommentDismissed()
    }

    companion object {
        private const val TAG = "ContentCommentBottomSheet"

        private const val HEIGHT_PERCENT = 0.8
        private const val KEYBOARD_HEIGHT_PERCENT = 0.35
        private const val SHIMMER_VALUE = 6

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
