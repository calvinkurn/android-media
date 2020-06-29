package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import com.tokopedia.play.broadcaster.util.showToaster
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupBottomSheet
import com.tokopedia.play.broadcaster.view.contract.SetupResultListener
import com.tokopedia.play.broadcaster.view.custom.PlayShareFollowerView
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.fragment.loading.LoadingDialogFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastPrepareViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.unifycomponents.UnifyButton
import javax.inject.Inject

/**
 * Created by jegul on 20/05/20
 */
class PlayBroadcastPrepareFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory
) : PlayBaseBroadcastFragment() {

    private lateinit var viewModel: PlayBroadcastPrepareViewModel
    private lateinit var parentViewModel: PlayBroadcastViewModel

    private lateinit var btnSetup: UnifyButton
    private lateinit var followerView: PlayShareFollowerView
    private lateinit var tvTermsCondition: TextView

    private lateinit var loadingFragment: LoadingDialogFragment

    private val setupListener = object : SetupResultListener {
        override fun onSetupCanceled() {

        }

        override fun onSetupCompletedWithData(dataStore: PlayBroadcastSetupDataStore) {
            viewModel.setDataFromSetupDataStore(dataStore)
            parentViewModel.fetchChannelData()
        }
    }

    override fun getScreenName(): String = "Play Prepare Page"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastPrepareViewModel::class.java)
        parentViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_broadcast_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeFollowers()
        observeChannelInfo()
    }

    private fun initView(view: View) {
        with (view) {
            btnSetup = findViewById(R.id.btn_setup)
            followerView = findViewById(R.id.follower_view)
            tvTermsCondition = findViewById(R.id.tv_terms_condition)
        }
    }

    private fun setupView(view: View) {
        broadcastCoordinator.setupTitle(getString(R.string.play_action_bar_prepare_title))
        btnSetup.setOnClickListener {
             openBroadcastSetupPage()
        }

        setupTermsCondition()
    }

    private fun setupTermsCondition() {
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
                        MethodChecker.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.dark_G500)
                ), termsConditionIndex, termsConditionIndex + termsConditionText.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )

        tvTermsCondition.text = spannedTermsConditionText

        tvTermsCondition.setOnClickListener {
            openTermsConditionPage()
        }
    }

    private fun openBroadcastSetupPage() {
        val setupClass = PlayBroadcastSetupBottomSheet::class.java
        val fragmentFactory = childFragmentManager.fragmentFactory
        val setupFragment = fragmentFactory.instantiate(requireContext().classLoader, setupClass.name) as PlayBroadcastSetupBottomSheet
        setupFragment.setListener(setupListener)
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

    private fun showBeforeLiveCountDown() {

    }

    private fun getLoadingFragment(): LoadingDialogFragment {
        if (!::loadingFragment.isInitialized) {
            val setupClass = LoadingDialogFragment::class.java
            val fragmentFactory = childFragmentManager.fragmentFactory
            loadingFragment = fragmentFactory.instantiate(requireContext().classLoader, setupClass.name) as LoadingDialogFragment
        }
        return loadingFragment
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) getLoadingFragment().show(childFragmentManager)
        else getLoadingFragment().dismiss()
    }

    //region observe
    /**
     * Observe
     */
    private fun observeFollowers() {
        viewModel.observableFollowers.observe(viewLifecycleOwner, Observer {
            followerView.setFollowersModel(it)
        })
    }

    private fun observeChannelInfo() {
        parentViewModel.observableChannelInfo.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> openFinalPreparationPage()
                NetworkResult.Loading -> showLoading(true)
                is NetworkResult.Fail -> requireView().showToaster(it.error.localizedMessage)
            }
        })
    }
    //endregion

    companion object {

        private const val APPLINK_WEBVIEW_FORMAT = "%s?url=%s"
        private const val TERMS_CONDITION_URL = "https://www.tokopedia.com/help/article/syarat-dan-ketentuan-tokopedia-play"
    }
}