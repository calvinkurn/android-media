package com.tokopedia.content.common.comment.ui

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
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
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.media.loader.loadImage
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collect
import com.tokopedia.kotlin.extensions.view.showWithCondition
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
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
               binding.viewCommentSend.isEnabled = p0?.isNotBlank().orFalse()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                binding.viewCommentSend.isEnabled = p0?.isNotBlank().orFalse()
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

        binding.ivUserPhoto.loadImage(viewModel.userInfo.profilePicture)
        binding.newComment.setOnClickListener {
            viewModel.submitAction(CommentAction.EditTextCLicked)
        }
        binding.ivCommentSend.setOnClickListener {
            viewModel.submitAction(CommentAction.ReplyComment(binding.newComment.text.toString(), CommentType.Parent))// adjust origin
        }
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
                            text = getString(R.string.comment_delete_kembali),
                            actionText = getString(R.string.comment_delete_undo),
                            duration = Toaster.LENGTH_LONG,
                            clickListener = {
                                viewModel.submitAction(CommentAction.DeleteComment(isFromToaster = true))
                                binding.rvComment.scrollToPosition(0)
                            }
                        )
                        toaster.addCallback(toasterCallback)
                        toaster.show()
                    }
                    is CommentEvent.ShowErrorToaster -> {
                        val view = if(sheetMenu.isVisible) sheetMenu.requireView().rootView else requireView().rootView
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
                        router.route(context = requireContext(), appLinkPattern = event.appLink)
                    }
                    CommentEvent.ShowKeyboard -> {
                        binding.newComment.requestFocus()
                        KeyboardHandler.showSoftKeyboard(requireActivity())
                    }
                    CommentEvent.HideKeyboard -> {
                        KeyboardHandler.hideSoftKeyboard(requireActivity())
                    }
                    CommentEvent.OpenReportEvent -> sheetMenu.showReportLayoutWhenLaporkanClicked()
                    CommentEvent.ReportSuccess -> sheetMenu.setFinalView()
                }
            }
        }
    }

    private fun showError(isShown: Boolean) {
        binding.commentGlobalError.showWithCondition(isShown)
        binding.rvComment.showWithCondition(!isShown)
        binding.commentHeader.showWithCondition(!isShown)
        binding.viewCommentSend.showWithCondition(!isShown)
    }

    fun setEntrySource(source: EntrySource?) {
        mSource = source
    }

    fun show(fragmentManager: FragmentManager) {
        if (isAdded) return
        showNow(fragmentManager, TAG)
    }

    override fun onReplyClicked(item: CommentUiModel.Item) {
        viewModel.submitAction(CommentAction.EditTextCLicked)
    }

    override fun onLongClicked(item: CommentUiModel.Item) {
        viewModel.submitAction(CommentAction.SelectComment(item))
        sheetMenu.setListener(this@ContentCommentBottomSheet)
        sheetMenu.setData(getMenuItems(item), item.id)
        sheetMenu.show(childFragmentManager)
    }

    override fun onMentionClicked(userType: String, userId: String) {
        val appLink = when (userType) {
            USER_TYPE_KOL -> ApplinkConst.PROFILE.replace(ApplinkConst.Profile.PARAM_USER_ID, userId)
            USER_TYPE_SELLER -> ApplinkConst.SHOP.replace("{shop_id}", userId)
            else -> ""
        }
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
        viewModel.submitAction(CommentAction.DismissComment)
        super.dismiss()
    }

    override fun onCancel(dialog: DialogInterface) {
        viewModel.submitAction(CommentAction.DismissComment)
        super.onCancel(dialog)
    }

    override fun onResume() {
        super.onResume()

        val window = dialog?.window
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

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
    }

    interface EntrySource {
        fun getPageSource(): PageSource
    }

    companion object {
        private const val TAG = "ContentCommentBottomSheet"

        private const val USER_TYPE_KOL = "user"
        private const val USER_TYPE_SELLER = "seller"

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
