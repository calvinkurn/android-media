package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.partial.ActionBarViewComponent
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastPrepareViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play_common.detachableview.FragmentViewContainer
import com.tokopedia.play_common.detachableview.FragmentWithDetachableView
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.updatePadding
import com.tokopedia.play_common.viewcomponent.viewComponent
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on January 24, 2022
 */
class PlayBroadcastPreparationFragment @Inject constructor(
    private val viewModelFactory: ViewModelFactory,
    private val analytic: PlayBroadcastAnalytic
) : PlayBaseBroadcastFragment(), FragmentWithDetachableView {

    /** ViewModel */
    private lateinit var viewModel: PlayBroadcastPrepareViewModel
    private lateinit var parentViewModel: PlayBroadcastViewModel

    /** View Component */
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

    private val fragmentViewContainer = FragmentViewContainer()

    override fun getScreenName(): String = "Play Prepare Page"

    override fun getViewContainer(): FragmentViewContainer = fragmentViewContainer

    /** Lifecycle */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PlayBroadcastPrepareViewModel::class.java)
        parentViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_play_broadcaster_preparation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupInsets(view)
    }

    /** Setup */
    private fun setupView(view: View) {
        actionBarView.setTitle(parentViewModel.getShopName())
        actionBarView.setShopIcon(parentViewModel.getShopIconUrl())
    }

    private fun setupInsets(view: View) {
        view.doOnApplyWindowInsets { v, insets, padding, _ ->
            v.updatePadding(top = padding.top + insets.systemWindowInsetTop, bottom = padding.bottom + insets.systemWindowInsetBottom)
        }
    }
}