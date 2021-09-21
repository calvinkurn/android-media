package com.tokopedia.play.broadcaster.view.fragment.setup.etalase

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.Slide
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.ui.model.EtalaseLoadingUiModel
import com.tokopedia.play.broadcaster.ui.model.result.PageResultState
import com.tokopedia.play.broadcaster.util.error.DefaultNetworkThrowable
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseEtalaseSetupFragment
import com.tokopedia.play.broadcaster.view.partial.EtalaseListViewComponent
import com.tokopedia.play.broadcaster.view.viewmodel.PlayEtalasePickerViewModel
import com.tokopedia.play_common.util.extension.doOnPreDraw
import com.tokopedia.play_common.viewcomponent.viewComponent
import javax.inject.Inject

class PlayEtalaseListFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory,
        private val analytic: PlayBroadcastAnalytic
) : PlayBaseEtalaseSetupFragment(), EtalaseListViewComponent.Listener {

    private lateinit var viewModel: PlayEtalasePickerViewModel

    private val etalaseListView: EtalaseListViewComponent by viewComponent { EtalaseListViewComponent(it, this) }

    override fun getScreenName(): String = "Etalase List Page"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(etalaseSetupCoordinator.getParent(), viewModelFactory).get(PlayEtalasePickerViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        etalaseSetupCoordinator.showBottomAction(false)
        return inflater.inflate(R.layout.fragment_play_etalase_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTransition()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeEtalase()
        observeEtalaseStatus()
    }

    override fun refresh() {
        etalaseListView.refresh()
    }

    /**
     * Etalase List View Component
     */
    override fun onEtalaseClicked(view: EtalaseListViewComponent, etalaseId: String, etalaseName: String, sharedElements: List<View>) {
        etalaseSetupCoordinator.openEtalaseDetail(
                etalaseId,
                etalaseName,
                sharedElements
        )
    }

    override fun onEtalaseBound(view: EtalaseListViewComponent, etalaseId: String) {
        viewModel.loadEtalaseProductPreview(etalaseId)
    }

    /**
     * Observe
     */
    private fun observeEtalase() {
        viewModel.observableEtalase.observe(viewLifecycleOwner, Observer {
            when (it.state) {
                PageResultState.Loading -> {
                    etalaseSetupCoordinator.hideGlobalError()
                    etalaseListView.setItems(listOf(EtalaseLoadingUiModel))
                }
                is PageResultState.Success -> {
                    etalaseSetupCoordinator.hideGlobalError()
                    etalaseListView.setItems(it.currentValue)
                    startPostponedTransition()
                }
                is PageResultState.Fail -> {
                    etalaseSetupCoordinator.showGlobalError(if (it.state.error is DefaultNetworkThrowable) GlobalError.NO_CONNECTION else GlobalError.SERVER_ERROR) {
                        viewModel.loadEtalaseList()
                    }
                    startPostponedTransition()
                }
            }

        })
    }

    private fun observeEtalaseStatus() {
        viewModel.observableEtalaseProductState.observe(viewLifecycleOwner, Observer {
            if (it.state is PageResultState.Fail) {
                analytic.viewEtalaseError(it.state.error.localizedMessage)
            }
        })
    }

    /**
     * Transition
     */
    private fun setupTransition() {
        setupExitTransition()
        setupReenterTransition()
    }

    private fun setupExitTransition() {
        exitTransition = Slide(Gravity.BOTTOM)
                .setStartDelay(150)
                .setDuration(300)
    }

    private fun setupReenterTransition() {
        reenterTransition = Slide(Gravity.BOTTOM)
                .setStartDelay(150)
                .setDuration(300)
    }

    private fun startPostponedTransition() {
        requireView().doOnPreDraw {
            etalaseSetupCoordinator.startPostponedEnterTransition()
        }
    }
}