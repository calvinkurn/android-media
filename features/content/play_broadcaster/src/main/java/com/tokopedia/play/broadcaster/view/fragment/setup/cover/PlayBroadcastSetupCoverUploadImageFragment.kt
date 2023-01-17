package com.tokopedia.play.broadcaster.view.fragment.setup.cover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.databinding.FragmentSetupCoverUploadImageBinding
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupBottomSheet
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupCoverBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import javax.inject.Inject

/**
 * Created by fachrizalmrsln on 11/01/23
 */
class PlayBroadcastSetupCoverUploadImageFragment @Inject constructor(
    private val parentViewModelFactoryCreator: PlayBroadcastViewModelFactory.Creator,
    private val analytic: PlayBroadcastAnalytic,
) : PlayBaseBroadcastFragment() {

    private var _binding: FragmentSetupCoverUploadImageBinding? = null
    private val binding: FragmentSetupCoverUploadImageBinding
        get() = _binding!!

    private val parentViewModel by activityViewModels<PlayBroadcastViewModel> {
        parentViewModelFactoryCreator.create(requireActivity())
    }

    private var mListener: PlayBroadcastSetupCoverBottomSheet.Listener? = null

    override fun getScreenName(): String = TAG

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetupCoverUploadImageBinding.inflate(
            LayoutInflater.from(requireContext())
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeCover()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        mListener = null
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        when (childFragment) {
            is PlayBroadcastSetupBottomSheet -> {
                childFragment.setListener(object : PlayBroadcastSetupBottomSheet.Listener {
                    override fun onCoverChanged(cover: PlayCoverUiModel) {
                        parentViewModel.submitAction(PlayBroadcastAction.SetCover(cover))
                    }
                })
                childFragment.setDataSource(object : PlayBroadcastSetupBottomSheet.DataSource {
                    override fun getProductList(): List<ProductUiModel> {
                        return parentViewModel.productSectionList.flatMap { it.products }
                    }

                    override fun getAuthorId(): String {
                        return parentViewModel.authorId
                    }

                    override fun getChannelId(): String {
                        return parentViewModel.channelId
                    }
                })
            }
        }
    }

    fun setupListener(listener: PlayBroadcastSetupCoverBottomSheet.Listener?) {
        mListener = listener
    }

    private fun setupView() = with(binding) {
        clCoverFormPreview.setAuthorName(parentViewModel.authorName)
        clCoverFormPreview.setTitle(parentViewModel.channelTitle)
        clCoverFormPreview.setOnClickListener {
            if (clCoverFormPreview.isCoverAvailable) analytic.clickEditCover()
            else analytic.clickAddNewCover()
            openCoverSetupFragment()
        }
        btnSetupCoverUploadImage.setOnClickListener {
            if (btnSetupCoverUploadImage.isEnabled) mListener?.setupCoverButtonSaveClicked()
        }
    }

    private fun observeCover() {
        parentViewModel.observableCover.observe(viewLifecycleOwner) {
            when (val croppedCover = it.croppedCover) {
                is CoverSetupState.Cropped.Uploaded -> {
                    if (croppedCover.coverImage.toString().isNotEmpty() &&
                        croppedCover.coverImage.toString().contains(HTTP)
                    ) {
                        binding.clCoverFormPreview.setCover(croppedCover.coverImage.toString())
                        binding.btnSetupCoverUploadImage.isEnabled = true
                    } else if (!croppedCover.localImage?.toString().isNullOrEmpty()) {
                        binding.clCoverFormPreview.setCover(croppedCover.localImage.toString())
                        binding.btnSetupCoverUploadImage.isEnabled = true
                    } else {
                        binding.clCoverFormPreview.setInitialCover()
                        binding.btnSetupCoverUploadImage.isEnabled = false
                    }
                }
                else -> {}
            }
        }
    }

    private fun openCoverSetupFragment() {
        val setupClass = PlayBroadcastSetupBottomSheet::class.java
        val fragmentFactory = childFragmentManager.fragmentFactory
        val setupFragment = fragmentFactory.instantiate(
            requireContext().classLoader,
            setupClass.name
        ) as PlayBroadcastSetupBottomSheet
        setupFragment.show(childFragmentManager)
    }

    companion object {
        const val TAG = "PlayBroadcastSetupCoverUploadImageFragment"
        private const val HTTP = "http"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): PlayBroadcastSetupCoverUploadImageFragment {
            val oldInstance =
                fragmentManager.findFragmentByTag(TAG) as? PlayBroadcastSetupCoverUploadImageFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                PlayBroadcastSetupCoverUploadImageFragment::class.java.name
            ) as PlayBroadcastSetupCoverUploadImageFragment
        }
    }

}
