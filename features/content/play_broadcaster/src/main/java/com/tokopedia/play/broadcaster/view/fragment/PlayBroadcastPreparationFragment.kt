package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.databinding.FragmentPlayBroadcastPreparationBinding
import com.tokopedia.play.broadcaster.ui.model.title.PlayTitleFormState
import com.tokopedia.play.broadcaster.ui.state.PlayTitleFormUiState
import com.tokopedia.play.broadcaster.view.custom.actionbar.ActionBarView
import com.tokopedia.play.broadcaster.view.custom.preparation.PreparationMenuView
import com.tokopedia.play.broadcaster.view.custom.preparation.TitleFormView
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastPrepareViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayTitleAndTagsSetupViewModel
import com.tokopedia.play_common.detachableview.FragmentViewContainer
import com.tokopedia.play_common.detachableview.FragmentWithDetachableView
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.util.extension.hideKeyboard
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.updatePadding
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on January 24, 2022
 */
class PlayBroadcastPreparationFragment @Inject constructor(
    private val viewModelFactory: ViewModelFactory,
    private val analytic: PlayBroadcastAnalytic
) : PlayBaseBroadcastFragment(), FragmentWithDetachableView,
    ActionBarView.Listener,
    PreparationMenuView.Listener,
    TitleFormView.Listener {

    /** ViewModel */
    private lateinit var viewModel: PlayBroadcastPrepareViewModel
    private lateinit var parentViewModel: PlayBroadcastViewModel
    private lateinit var titleSetupViewModel: PlayTitleAndTagsSetupViewModel

    /** View */
    private var _binding: FragmentPlayBroadcastPreparationBinding? = null
    private val binding get() = _binding!!

    private val fragmentViewContainer = FragmentViewContainer()

    override fun getScreenName(): String = "Play Prepare Page"

    override fun getViewContainer(): FragmentViewContainer = fragmentViewContainer

    /** Lifecycle */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PlayBroadcastPrepareViewModel::class.java)
        parentViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
//        titleSetupViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PlayTitleAndTagsSetupViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayBroadcastPreparationBinding.inflate(
            LayoutInflater.from(requireContext()),
            container,
            false
        )
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupInsets(view)
        setupListener()
        setupObserver()

        if(parentViewModel.channelTitle.isEmpty()) showTitleForm(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onBackPressed(): Boolean {
        return when {
            binding.formTitle.visibility == View.VISIBLE -> {
                return if(parentViewModel.channelTitle.isEmpty()) {
                    analytic.clickCloseOnSetupPage()
                    false
                }
                else {
                    showTitleForm(false)
                    true
                }
            }
            else -> super.onBackPressed()
        }
    }

    /** Setup */
    private fun setupView() {
        binding.viewActionBar.setShopName(parentViewModel.getShopName())
        binding.viewActionBar.setShopIcon(parentViewModel.getShopIconUrl())
    }

    private fun setupInsets(view: View) {
        view.doOnApplyWindowInsets { v, insets, padding, _ ->
            v.updatePadding(top = padding.top + insets.systemWindowInsetTop, bottom = padding.bottom + insets.systemWindowInsetBottom)
        }
    }

    private fun setupListener() {
        binding.viewActionBar.setListener(this)
        binding.viewPreparationMenu.setListener(this)
        binding.formTitle.setListener(this)

        binding.flBroStartLivestream.setOnClickListener {
            /** TODO: start countdown */
        }

        binding.icBroPreparationSwitchCamera.setOnClickListener {
            parentViewModel.switchCamera()
            analytic.clickSwitchCameraOnSetupPage()
        }
    }

    private fun setupObserver() {
        parentViewModel.observableTitle.observe(viewLifecycleOwner) {
            binding.viewPreparationMenu.isSetTitleChecked(true)
        }

//        titleSetupViewModel.observableUploadEvent.observe(viewLifecycleOwner) {
//            when (val content = it.peekContent()) {
////                is NetworkResult.Fail -> onUploadFailed(content.error)
////                is NetworkResult.Success -> {
////                    if (!it.hasBeenHandled) onUploadSuccess()
////                }
//            }
//        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            parentViewModel.preparationUiState.collectLatest {
//                renderTitleFormView(it.titleForm)
            }
        }
    }

    /** Form */
    private fun showTitleForm(isShow: Boolean) {
        if(isShow) {
            showMainComponent(false)

            binding.formTitle.setTitle(parentViewModel.channelTitle)
            binding.formTitle.setLoading(false)
            binding.formTitle.visibility = View.VISIBLE
        }
        else {
            showMainComponent(true)

            binding.formTitle.visibility = View.GONE
        }
    }

    /** Callback Action Bar */
    override fun onClickClosePreparation() {
        analytic.clickCloseOnSetupPage()
        activity?.onBackPressed()
    }

    /** Callback Preparation Menu */
    override fun onClickSetTitle() {
        showTitleForm(true)
    }

    override fun onClickSetCover() {
        TODO("Not yet implemented")
    }

    override fun onClickSetProduct() {
        TODO("Not yet implemented")
    }

    /** Callback Title Form */
    override fun onCloseTitleForm(view: TitleFormView) {
        hideKeyboard()
        activity?.onBackPressed()
    }

    override fun onTitleSaved(view: TitleFormView, title: String) {
        binding.formTitle.setLoading(true)
//        titleSetupViewModel.finishSetup(title)
    }

    /** Helper */
    private fun showMainComponent(isShow: Boolean) {
        binding.groupPreparationMain.visibility = if(isShow) View.VISIBLE else View.GONE
    }
}