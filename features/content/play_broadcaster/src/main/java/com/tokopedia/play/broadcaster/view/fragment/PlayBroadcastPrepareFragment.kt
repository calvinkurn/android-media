package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.ui.model.ChannelInfoUiModel
import com.tokopedia.play.broadcaster.util.extension.showErrorToaster
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupBottomSheet
import com.tokopedia.play.broadcaster.view.contract.SetupResultListener
import com.tokopedia.play.broadcaster.view.custom.PlayShareFollowerView
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.partial.ActionBarViewComponent
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastPrepareViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play_common.detachableview.FragmentViewContainer
import com.tokopedia.play_common.detachableview.FragmentWithDetachableView
import com.tokopedia.play_common.detachableview.detachableView
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.view.updatePadding
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.url.TokopediaUrl
import javax.inject.Inject

/**
 * Created by jegul on 20/05/20
 */
class PlayBroadcastPrepareFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory,
        private val analytic: PlayBroadcastAnalytic
) : PlayBaseBroadcastFragment(), FragmentWithDetachableView {

    private lateinit var viewModel: PlayBroadcastPrepareViewModel
    private lateinit var parentViewModel: PlayBroadcastViewModel

    private val btnSetup: UnifyButton by detachableView(R.id.btn_setup)
    private val followerView: PlayShareFollowerView by detachableView(R.id.follower_view)
    private val tvTermsCondition: TextView by detachableView(R.id.tv_terms_condition)

    private val actionBarView by viewComponent {
        ActionBarViewComponent(it, object : ActionBarViewComponent.Listener {
            override fun onCameraIconClicked() {
                parentViewModel.switchCamera()
                analytic.clickSwitchCameraOnSetupPage()
            }

            override fun onCloseIconClicked() {
                analytic.clickCloseOnSetupPage()
                activity?.onBackPressed()
            }
        })
    }

    private val isFirstStreaming: Boolean
        get() = parentViewModel.isFirstStreaming

    private val fragmentViewContainer = FragmentViewContainer()

    private val setupListener = object : SetupResultListener {
        override fun onSetupCanceled() {

        }

        override suspend fun onSetupCompletedWithData(bottomSheet: BottomSheetDialogFragment, dataStore: PlayBroadcastSetupDataStore): Throwable? {
            viewModel.setDataFromSetupDataStore(dataStore)
            return parentViewModel.getChannelDetail()
        }
    }

    override fun getScreenName(): String = "Play Prepare Page"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PlayBroadcastPrepareViewModel::class.java)
        parentViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_broadcast_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupInsets(view)
        setupObserve()
    }

    override fun onStart() {
        super.onStart()
        requireView().requestApplyInsetsWhenAttached()
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)

        if (childFragment is PlayBroadcastSetupBottomSheet) {
            childFragment.setListener(setupListener)
        }
    }

    override fun getViewContainer(): FragmentViewContainer {
        return fragmentViewContainer
    }

    private fun setupView(view: View) {
        actionBarView.setTitle(getString(R.string.play_action_bar_prepare_title))
        btnSetup.setOnClickListener {
            analytic.clickPrepareBroadcast()
            openBroadcastSetupPage()
        }

        setupTermsCondition()
    }

    private fun setupInsets(view: View) {
        view.doOnApplyWindowInsets { v, insets, padding, _ ->
            v.updatePadding(top = padding.top + insets.systemWindowInsetTop, bottom = padding.bottom + insets.systemWindowInsetBottom)
        }
    }

    private fun setupObserve() {
        observeFollowers()
        observeChannelInfo()
    }

    private fun setupTermsCondition() {
        if (isFirstStreaming) {
            val termsConditionText = getString(R.string.play_terms_condition)
            val fullTermsConditionText = getString(
                    R.string.play_terms_condition_full,
                    btnSetup.text,
                    termsConditionText
            )
            val termsConditionIndex = fullTermsConditionText.indexOf(termsConditionText)
            val spannedTermsConditionText = SpannableString(fullTermsConditionText)
            spannedTermsConditionText.setSpan(
                    ForegroundColorSpan(
                            MethodChecker.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_G500)
                    ), termsConditionIndex, termsConditionIndex + termsConditionText.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )

            tvTermsCondition.text = spannedTermsConditionText

            tvTermsCondition.setOnClickListener {
                analytic.clickTnC()
                openTermsConditionPage()
            }

            analytic.viewTnC()
            tvTermsCondition.visible()
        } else {
            tvTermsCondition.gone()
        }
    }

    private fun openBroadcastSetupPage() {
        val setupClass = PlayBroadcastSetupBottomSheet::class.java
        val fragmentFactory = childFragmentManager.fragmentFactory
        val setupFragment = fragmentFactory.instantiate(requireContext().classLoader, setupClass.name) as PlayBroadcastSetupBottomSheet
        setupFragment.show(childFragmentManager)
    }

    private fun openTermsConditionPage() {
        RouteManager.route(
                context,
                String.format(APPLINK_WEBVIEW_FORMAT, ApplinkConst.WEBVIEW, TERMS_CONDITION_URL)
        )
    }

    private fun openFinalPreparationPage() {
        broadcastCoordinator.navigateToFragment(
                fragmentClass = PlayBeforeLiveFragment::class.java
        )
    }

    //region observe
    /**
     * Observe
     */
    private fun observeFollowers() {
        viewModel.observableFollowers.observe(viewLifecycleOwner) {
            followerView.setFollowersModel(it)
        }
    }

    private fun observeChannelInfo() {
        parentViewModel.observableChannelInfo.observe(viewLifecycleOwner, object: Observer<NetworkResult<ChannelInfoUiModel>> {
            override fun onChanged(t: NetworkResult<ChannelInfoUiModel>?) {
                Log.d("<LOG>", "PlayBroadcastPrepareFragment observableChannelInfo $t")
                when (t) {
                    is NetworkResult.Success -> {
                        parentViewModel.observableChannelInfo.removeObserver(this)
                        openFinalPreparationPage()
                    }
                    NetworkResult.Loading -> {} //showLoading(true)
                    is NetworkResult.Fail -> view?.showErrorToaster(t.error)
                }
            }
        })
    }
    //endregion

    companion object {

        private const val APPLINK_WEBVIEW_FORMAT = "%s?url=%s"
        private val TERMS_CONDITION_URL = "${TokopediaUrl.getInstance().WEB}help/article/tokopedia-play-live-streaming"
    }
}