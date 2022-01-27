package com.tokopedia.play.broadcaster.setup.product.view.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.BottomSheetPlayBroEtalaseListBinding
import com.tokopedia.play.broadcaster.setup.product.view.model.EtalaseListModel
import com.tokopedia.play.broadcaster.setup.product.view.viewcomponent.EtalaseListViewComponent
import com.tokopedia.play.broadcaster.setup.product.viewmodel.PlayBroProductSetupViewModel
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.util.bottomsheet.PlayBroadcastDialogCustomizer
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayProductLiveBottomSheet
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
class PlayBroEtalaseListBottomSheet @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val dialogCustomizer: PlayBroadcastDialogCustomizer
) : BottomSheetUnify() {

    private lateinit var viewModel: PlayBroProductSetupViewModel

    private var _binding: BottomSheetPlayBroEtalaseListBinding? = null
    private val binding: BottomSheetPlayBroEtalaseListBinding
        get() = _binding!!

    private val etalaseListView by viewComponent { EtalaseListViewComponent(binding.rvEtalase) }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            dialogCustomizer.customize(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireParentFragment(), viewModelFactory)
            .get(PlayBroProductSetupViewModel::class.java)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupObserve()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun setupBottomSheet() {
        _binding = BottomSheetPlayBroEtalaseListBinding.inflate(
            LayoutInflater.from(requireContext()),
        )
        setChild(binding.root)
    }

    private fun setupView() {
        binding.root.layoutParams = binding.root.layoutParams.apply {
            height = (getScreenHeight() * 0.8f).toInt()
        }
    }

    private fun setupObserve() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest { (prevState, state) ->
                renderBottomSheetTitle(state.campaignList)
                renderEtalaseList(prevState?.campaignList, state.campaignList)
            }
        }
    }

    private fun renderBottomSheetTitle(
        campaignList: List<CampaignUiModel>,
    ) {
        val title = buildString {
            if (campaignList.isNotEmpty()) {
                append("Campaign")
                append(" & ")
            }
            append("Etalase")
        }
        setTitle(title)
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun renderEtalaseList(
        prevCampaignList: List<CampaignUiModel>?,
        campaignList: List<CampaignUiModel>
    ) {
        if (prevCampaignList == campaignList) return

        val etalaseList = buildList {
            if (campaignList.isNotEmpty()) {
                add(EtalaseListModel.Header("Campaign"))
                addAll(campaignList.map(EtalaseListModel::Body))
            }
        }

        etalaseListView.setEtalaseList(etalaseList)
    }

    companion object {
        private const val TAG = "PlayBroEtalaseAndCampaignListBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): PlayBroEtalaseListBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? PlayBroEtalaseListBottomSheet
            return if (oldInstance != null) oldInstance
            else {
                val fragmentFactory = fragmentManager.fragmentFactory
                fragmentFactory.instantiate(
                    classLoader,
                    PlayBroEtalaseListBottomSheet::class.java.name
                ) as PlayBroEtalaseListBottomSheet
            }
        }
    }
}