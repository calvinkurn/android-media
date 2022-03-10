package com.tokopedia.play.broadcaster.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.transition.*
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.databinding.FragmentPlayBroadcastPostVideoBinding
import com.tokopedia.play.broadcaster.ui.model.ChannelInfoUiModel
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play.broadcaster.util.extension.showErrorToaster
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.partial.TagListViewComponent
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastSummaryViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayTitleAndTagsSetupViewModel
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.updateMargins
import com.tokopedia.play_common.view.updatePadding
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 07, 2022
 */
class PlayBroadcastPostVideoFragment @Inject constructor(
    private val viewModelFactory: ViewModelFactory,
    private val analytic: PlayBroadcastAnalytic,
    private val userSession: UserSessionInterface
) : PlayBaseBroadcastFragment(), TagListViewComponent.Listener {

    private var mListener: Listener? = null

    private var _binding: FragmentPlayBroadcastPostVideoBinding? = null
    private val binding: FragmentPlayBroadcastPostVideoBinding
        get() = _binding!!

    private val tagListView by viewComponent { TagListViewComponent(it, binding.rvTagsRecommendation.id, this) }

    private lateinit var viewModel: PlayBroadcastSummaryViewModel
    private lateinit var parentViewModel: PlayBroadcastViewModel

    override fun getScreenName(): String = "Play Post Video Page"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(
            PlayBroadcastSummaryViewModel::class.java)
        parentViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(
            PlayBroadcastViewModel::class.java)

        setupTransition()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayBroadcastPostVideoBinding.inflate(
            LayoutInflater.from(requireContext()),
        )
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObservable()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.icBroSummaryBack.setOnClickListener {
            mListener?.onClickBackButton()
        }

        binding.clCoverPreview.setOnClickListener {
            openCoverSetupFragment()
        }

        binding.btnPostVideo.setOnClickListener {
            analytic.clickSaveVodOnReportPage(parentViewModel.channelId)
            viewModel.saveVideo()
        }
    }

    private fun setupObservable() {
        observeChannelInfo()
        observeTags()
        observeSaveVideo()
    }

    private fun observeChannelInfo() {
        parentViewModel.observableChannelInfo.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    with(it.data) {
                        binding.ivBroSummaryCoverPreview.setImageUrl(coverUrl)
                        binding.tvBroSummaryCoverTitle.text = title
                        binding.tvBroSummaryCoverShopName.text = parentViewModel.getShopName()
                    }
                }
                else -> {}
            }
        }
    }

    private fun observeTags() {
        viewModel.observableRecommendedTagsModel.observe(viewLifecycleOwner) {
            tagListView.setTags(it.toList())
        }
    }

    private fun observeSaveVideo() {
        viewModel.observableSaveVideo.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> binding.btnPostVideo.isLoading = true
                is NetworkResult.Success -> {
                    openShopPageWithBroadcastStatus(true)
                }
                is NetworkResult.Fail -> {
                    binding.btnPostVideo.isLoading = false
                    /** TODO("should be change later with ErrorFragment from RE") */
                    view?.showErrorToaster(
                        err = it.error,
                        customErrMessage = it.error.localizedMessage
                            ?: getString(R.string.play_broadcaster_default_error),
                        actionLabel = getString(R.string.play_broadcast_try_again),
                        actionListener = { _ -> it.onRetry() }
                    )
                }
            }
        }
    }

    private fun openShopPageWithBroadcastStatus(isSaved: Boolean) {
        if (activity?.callingActivity == null) {
            val intent = RouteManager.getIntent(context, ApplinkConst.SHOP, userSession.shopId)
                .putExtra(NEWLY_BROADCAST_CHANNEL_SAVED, isSaved)
            startActivity(intent)
            activity?.finish()
        } else {
            activity?.setResult(
                Activity.RESULT_OK,
                Intent().putExtra(NEWLY_BROADCAST_CHANNEL_SAVED, isSaved)
            )
            activity?.finish()
        }
    }

    private fun openCoverSetupFragment() {
        val setupClass = PlayBroadcastSetupBottomSheet::class.java
        val fragmentFactory = childFragmentManager.fragmentFactory
        val setupFragment = fragmentFactory.instantiate(requireContext().classLoader, setupClass.name) as PlayBroadcastSetupBottomSheet
        setupFragment.show(childFragmentManager)
    }

    /**
     * Listener
     */
    override fun onTagClicked(view: TagListViewComponent, tag: PlayTagUiModel) {
        viewModel.toggleTag(tag.tag)
        analytic.selectRecommendedTag(parentViewModel.channelId, tag.tag, tag.isChosen)
    }

    /**
     * Transition
     */
    private fun setupTransition() {
        setupEnterTransition()
        setupReturnTransition()
    }

    private fun setupEnterTransition() {
        enterTransition = TransitionSet()
            .addTransition(Slide(Gravity.END))
            .addTransition(Fade(Fade.IN))
            .setDuration(300)

        sharedElementEnterTransition = TransitionSet()
            .addTransition(ChangeTransform())
            .addTransition(ChangeBounds())
            .setDuration(450)
    }

    private fun setupReturnTransition() {
        returnTransition = TransitionSet()
            .addTransition(Slide(Gravity.END))
            .addTransition(Fade(Fade.OUT))
            .setDuration(250)

        sharedElementReturnTransition = TransitionSet()
            .addTransition(ChangeTransform())
            .addTransition(ChangeBounds())
            .setDuration(450)
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    companion object {
        private const val NEWLY_BROADCAST_CHANNEL_SAVED = "EXTRA_NEWLY_BROADCAST_SAVED"
    }

    interface Listener {
        fun onClickPostButton()
        fun onClickBackButton()
    }
}