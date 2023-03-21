package com.tokopedia.play.broadcaster.view.fragment.setup.cover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.databinding.FragmentSetupCoverUploadImageBinding
import com.tokopedia.play.broadcaster.setup.cover.PlayBroSetupCoverAction
import com.tokopedia.play.broadcaster.setup.cover.PlayBroSetupCoverViewModel
import com.tokopedia.play.broadcaster.setup.product.viewmodel.ViewModelFactoryProvider
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.page.PlayBroPageSource
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupBottomSheet
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupCoverBottomSheet
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupCoverBottomSheet.Companion.TAB_UPLOAD_IMAGE
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import javax.inject.Inject

/**
 * Created by fachrizalmrsln on 11/01/23
 */
class PlayBroadcastSetupCoverUploadImageFragment @Inject constructor(
    private val analytic: PlayBroadcastAnalytic,
) : PlayBaseBroadcastFragment() {

    private val viewModel: PlayBroSetupCoverViewModel by viewModels(
        ownerProducer = { requireParentFragment() }) {
        (parentFragment as ViewModelFactoryProvider).getFactory()
    }

    private var _binding: FragmentSetupCoverUploadImageBinding? = null
    private val binding: FragmentSetupCoverUploadImageBinding
        get() = _binding!!

    private var mListener: PlayBroadcastSetupCoverBottomSheet.Listener? = null

    override fun getScreenName(): String = TAG

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetupCoverUploadImageBinding.inflate(inflater, container, false)
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
                        viewModel.submitAction(PlayBroSetupCoverAction.SetUploadImageCover(cover))
                    }
                })
                childFragment.setDataSource(object : PlayBroadcastSetupBottomSheet.DataSource {
                    override fun getProductList(): List<ProductUiModel> {
                        return viewModel.productSectionList.flatMap { it.products }
                    }

                    override fun getSelectedAccount(): ContentAccountUiModel {
                        return viewModel.contentAccount
                    }

                    override fun getChannelId(): String {
                        return viewModel.channelId
                    }

                    override fun getPageSource(): PlayBroPageSource {
                        return PlayBroPageSource.Live
                    }
                })
            }
        }
    }

    fun setupData(listener: PlayBroadcastSetupCoverBottomSheet.Listener?) {
        mListener = listener
    }

    private fun setupView() = with(binding) {
        clCoverFormPreview.setAuthorName(viewModel.contentAccount.name)
        clCoverFormPreview.setTitle(viewModel.channelTitle)
        clCoverFormPreview.setOnClickListener {
            if (clCoverFormPreview.isCoverAvailable) analytic.clickEditCover()
            else analytic.clickAddNewCover()
            openCoverSetupFragment()
        }
        btnSetupCoverUploadImage.setOnClickListener {
            if (btnSetupCoverUploadImage.isEnabled) mListener?.dismissSetupCover(TAB_UPLOAD_IMAGE)
        }
    }

    private fun observeCover() {
        viewModel.observableCover.observe(viewLifecycleOwner) {
            when (val croppedCover = it.croppedCover) {
                is CoverSetupState.Cropped.Uploaded -> {
                    if (croppedCover.coverImage.toString().isNotEmpty() &&
                        croppedCover.coverImage.toString().contains(HTTP)
                    ) {
                        binding.clCoverFormPreview.setCover(croppedCover.coverImage.toString())
                    } else if (!croppedCover.localImage?.toString().isNullOrEmpty()) {
                        binding.clCoverFormPreview.setCover(croppedCover.localImage.toString())
                        binding.btnSetupCoverUploadImage.isEnabled = true
                        mListener?.onUploadCoverSuccess()
                    } else {
                        binding.clCoverFormPreview.setInitialCover()
                        binding.btnSetupCoverUploadImage.isEnabled = false
                    }
                }
                is CoverSetupState.GeneratedCover -> {
                    if (croppedCover.coverImage.isEmpty()) return@observe
                    binding.clCoverFormPreview.setCover(croppedCover.coverImage)
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
