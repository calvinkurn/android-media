package com.tokopedia.play.view.bottomsheet

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.applink.RouteManager
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytic
import com.tokopedia.play.util.observer.DistinctObserver
import com.tokopedia.play.view.fragment.PlayFragment
import com.tokopedia.play.view.fragment.PlayUserInteractionFragment
import com.tokopedia.play.view.type.BottomInsetsState
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.uimodel.PlayUserReportReasoningUiModel
import com.tokopedia.play.view.viewcomponent.KebabMenuSheetViewComponent
import com.tokopedia.play.view.viewcomponent.PlayUserReportSheetViewComponent
import com.tokopedia.play.view.viewcomponent.PlayUserReportSubmissionViewComponent
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

/**
 * Created by jegul on 10/12/19
 */
class PlayMoreActionBottomSheet @Inject constructor(
    private val analytic: PlayAnalytic
    ) : BottomSheetUnify(), KebabMenuSheetViewComponent.Listener,
    PlayUserReportSheetViewComponent.Listener,
    PlayUserReportSubmissionViewComponent.Listener {

    //TODO = handle login event

    private val userReportSheetHeight: Int
        get() = (requireView().height * 0.9).toInt()

    companion object {
        private const val TAG = "PlayMoreActionBottomSheet"
    }

    private var listener: Listener? = null
    private var childView: View? = null

    private val kebabMenuSheetView by viewComponent { KebabMenuSheetViewComponent(it, this) }
    private val userReportSheetView by viewComponent { PlayUserReportSheetViewComponent(it, this) }
    private val userReportSubmissionSheetView by viewComponent {
        PlayUserReportSubmissionViewComponent(
            it,
            this
        )
    }

    private lateinit var playViewModel: PlayViewModel

    private var userReportTimeMillis: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val grandParentActivity = ((requireParentFragment() as PlayUserInteractionFragment).parentFragment) as PlayFragment

        playViewModel = ViewModelProvider(
            grandParentActivity, grandParentActivity.viewModelProviderFactory
        ).get(PlayViewModel::class.java)

        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setObserve()
    }

    fun setState(isFreeze: Boolean) {}

    private fun setupView(view: View) {
        kebabMenuSheetView.hide()
        userReportSheetView.hide()
        userReportSubmissionSheetView.hide()
    }

    fun show(manager: FragmentManager) {
        show(manager, TAG)
    }

    private fun initBottomSheet() {
        this.clearContentPadding = true
        this.isFullpage = true
        this.showHeader = false
        this.listener = listener //TODO = setup listener
        this.childView =
            View.inflate(requireContext(), R.layout.bottom_sheet_play_more_action, null)
        setChild(this.childView)
        setCloseClickListener { this.dismiss() }
    }

    private fun setObserve(){
        observeBottomInsets()
        observeUserReport()
        observeUserReportSubmission()
    }

    private fun observeBottomInsets() {
        playViewModel.observableBottomInsetsState.observe(viewLifecycleOwner, DistinctObserver {
            it[BottomInsetsType.KebabMenuSheet]?.let { state ->
                if (state is BottomInsetsState.Shown) kebabMenuSheetView.showWithHeight(state.estimatedInsetsHeight)
                else kebabMenuSheetView.hide()
            }

            it[BottomInsetsType.UserReportSheet]?.let { state ->
                if (state is BottomInsetsState.Shown) userReportSheetView.showWithHeight(state.estimatedInsetsHeight)
                else userReportSheetView.hide()
            }

            it[BottomInsetsType.UserReportSubmissionSheet]?.let { state ->
                if (state is BottomInsetsState.Shown) userReportSubmissionSheetView.showWithHeight(
                    state.estimatedInsetsHeight
                )
                else userReportSubmissionSheetView.hide()
            }
        })
    }

    private fun observeUserReport() {
        playViewModel.observableUserReportReasoning.observe(viewLifecycleOwner, DistinctObserver {
            when (it) {
                is PlayResult.Loading -> if (it.showPlaceholder) userReportSheetView.showPlaceholder()
                is PlayResult.Success -> userReportSheetView.setReportSheet(it.data)
                is PlayResult.Failure -> userReportSheetView.showError(
                    isConnectionError = it.error is ConnectException || it.error is UnknownHostException,
                    onError = it.onRetry
                )
            }
        })
    }

    private fun observeUserReportSubmission() {
        playViewModel.observableUserReportSubmission.observe(viewLifecycleOwner, DistinctObserver {
            when (it) {
                is PlayResult.Success -> {
                    this.dismiss()
                }
                is PlayResult.Failure -> ""
                //TODO = provide toaster
//                    doShowToaster(
//                    bottomSheetType = BottomInsetsType.UserReportSubmissionSheet,
//                    toasterType = Toaster.TYPE_ERROR,
//                    message = ErrorHandler.getErrorMessage(requireContext(), it.error)
//                )
            }
        })
    }

    interface Listener {
        fun onWatchModeClicked(bottomSheet: PlayMoreActionBottomSheet)
        fun onNoAction(bottomSheet: PlayMoreActionBottomSheet)
    }

    override fun onReportClick(view: KebabMenuSheetViewComponent) {
        playViewModel.onShowUserReportSheet(userReportSheetHeight)
        playViewModel.getUserReportList()
    }

    override fun onCloseButtonClicked(view: KebabMenuSheetViewComponent) {
        this.dismiss()
    }

    override fun onCloseButtonClicked(view: PlayUserReportSheetViewComponent) {
       playViewModel.hideUserReportSheet()
    }

    override fun onItemReportClick(
        view: PlayUserReportSheetViewComponent,
        item: PlayUserReportReasoningUiModel.Reasoning
    ) {
        userReportTimeMillis = Calendar.getInstance().timeInMillis
        playViewModel.onShowUserReportSubmissionSheet(userReportSheetHeight)
        userReportSubmissionSheetView.setView(item)
    }

    override fun onFooterClicked(view: PlayUserReportSheetViewComponent) {
        //TODO = need to improve code
        RouteManager.route(context, getString(R.string.play_user_report_footer_weblink))
    }

    override fun onCloseButtonClicked(view: PlayUserReportSubmissionViewComponent) {
        playViewModel.hideUserReportSubmissionSheet()
    }

    override fun onFooterClicked(view: PlayUserReportSubmissionViewComponent) {
        //TODO = need to improve code
        RouteManager.route(context, getString(R.string.play_user_report_footer_weblink))
    }

    override fun onShowVerificationDialog(
        view: PlayUserReportSubmissionViewComponent,
        reasonId: Int,
        description: String
    ) {
        //TODO("Not yet implemented")
    }
}