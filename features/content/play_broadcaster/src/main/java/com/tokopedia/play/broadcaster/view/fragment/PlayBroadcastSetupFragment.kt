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
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupBottomSheet
import com.tokopedia.play.broadcaster.view.custom.PlayShareFollowerView
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastSetupViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.unifycomponents.UnifyButton
import javax.inject.Inject

/**
 * Created by jegul on 20/05/20
 */
class PlayBroadcastSetupFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory
) : PlayBaseBroadcastFragment() {

    private lateinit var viewModel: PlayBroadcastSetupViewModel
    private lateinit var parentViewModel: PlayBroadcastViewModel

    private lateinit var btnSetup: UnifyButton
    private lateinit var followerView: PlayShareFollowerView
    private lateinit var tvPrivacyPolicy: TextView

    private val setupListener = object : PlayBroadcastSetupBottomSheet.Listener {
        override fun onSetupCanceled() {

        }

        override fun onSetupCompletedWithData(selectedProducts: List<ProductContentUiModel>, cover: PlayCoverUiModel) {
            populateSetupData(selectedProducts, cover)
            openFinalPreparationPage()
        }
    }

    override fun getScreenName(): String = "Play Prepare Page"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastSetupViewModel::class.java)
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
    }

    private fun initView(view: View) {
        with (view) {
            btnSetup = findViewById(R.id.btn_setup)
            followerView = findViewById(R.id.follower_view)
            tvPrivacyPolicy = findViewById(R.id.tv_privacy_policy)
        }
    }

    private fun setupView(view: View) {
        broadcastCoordinator.setupTitle(getString(R.string.play_action_bar_prepare_title))
        btnSetup.setOnClickListener {
             openBroadcastSetupPage()
        }

        setupPrivacyPolicy()
    }

    private fun setupPrivacyPolicy() {
        val privacyPolicyText = getString(R.string.play_privacy_policy)
        val fullPrivacyPolicyText = getString(
                R.string.play_privacy_policy_full,
                btnSetup.text,
                privacyPolicyText
        )
        val privacyPolicyIndex = fullPrivacyPolicyText.indexOf(privacyPolicyText)
        val spannedPrivacyText = SpannableString(fullPrivacyPolicyText)
        spannedPrivacyText.setSpan(
                ForegroundColorSpan(
                        MethodChecker.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.dark_G500)
                ), privacyPolicyIndex, privacyPolicyIndex + privacyPolicyText.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )

        tvPrivacyPolicy.text = spannedPrivacyText

        tvPrivacyPolicy.setOnClickListener {
            openPrivacyPolicyPage()
        }
    }

    private fun openBroadcastSetupPage() {
        val setupClass = PlayBroadcastSetupBottomSheet::class.java
        val fragmentFactory = childFragmentManager.fragmentFactory
        val setupFragment = fragmentFactory.instantiate(requireContext().classLoader, setupClass.name) as PlayBroadcastSetupBottomSheet
        setupFragment.setListener(setupListener)
        setupFragment.show(childFragmentManager)
    }

    private fun openPrivacyPolicyPage() {
        RouteManager.route(
                context,
                String.format(APPLINK_WEBVIEW_FORMAT, ApplinkConst.WEBVIEW, PRIVACY_POLICY_URL)
        )
    }

    private fun populateSetupData(selectedProducts: List<ProductContentUiModel>, cover: PlayCoverUiModel) {
        viewModel.setupChannelWithData(
                selectedProducts = selectedProducts,
                cover = cover
        )
    }

    private fun openFinalPreparationPage() {
        broadcastCoordinator.navigateToFragment(
                fragmentClass = PlayBeforeLiveFragment::class.java
        )
    }

    private fun showBeforeLiveCountDown() {

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
    //endregion

    companion object {

        private const val APPLINK_WEBVIEW_FORMAT = "%s?url=%s"
        private const val PRIVACY_POLICY_URL = "https://www.tokopedia.com/help/article/syarat-dan-ketentuan-tokopedia-play"
    }
}