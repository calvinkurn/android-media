package com.tokopedia.play.broadcaster.view.fragment.beautification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.play.broadcaster.analytic.beautification.PlayBroadcastBeautificationAnalytic
import com.tokopedia.play.broadcaster.analytic.beautification.PlayBroadcastBeautificationAnalyticStateHolder
import com.tokopedia.play.broadcaster.databinding.FragmentBeautificationTabBinding
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.itemdecoration.BeautificationOptionItemDecoration
import com.tokopedia.play.broadcaster.ui.model.beautification.BeautificationAssetStatus
import com.tokopedia.play.broadcaster.ui.model.beautification.BeautificationConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.beautification.FaceFilterUiModel
import com.tokopedia.play.broadcaster.ui.model.beautification.PresetFilterUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.BeautificationFilterOptionViewHolder
import com.tokopedia.play.broadcaster.view.adapter.BeautificationFilterOptionAdapter
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import com.tokopedia.play_common.util.extension.withCache
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on February 27, 2023
 */
class BeautificationTabFragment @Inject constructor(
    private val viewModelFactoryCreator: PlayBroadcastViewModelFactory.Creator,
    private val beautificationAnalytic: PlayBroadcastBeautificationAnalytic,
    private val beautificationAnalyticStateHolder: PlayBroadcastBeautificationAnalyticStateHolder,
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentBeautificationTabBinding? = null
    private val binding: FragmentBeautificationTabBinding
        get() = _binding!!

    private val viewModel: PlayBroadcastViewModel by activityViewModels {
        viewModelFactoryCreator.create(
            requireActivity()
        )
    }

    private val adapter by lazyThreadSafetyNone {
        BeautificationFilterOptionAdapter(
            faceFilterListener = object : BeautificationFilterOptionViewHolder.FaceFilter.Listener {
                override fun onClick(item: FaceFilterUiModel) {
                    if(item.isRemoveEffect) {
                        beautificationAnalytic.clickNoneCustomFace(
                            viewModel.selectedAccount,
                            beautificationAnalyticStateHolder.getPageSourceForAnalytic(),
                        )
                    }
                    else {
                        beautificationAnalytic.clickCustomFace(
                            viewModel.selectedAccount,
                            beautificationAnalyticStateHolder.getPageSourceForAnalytic(),
                            item.id,
                        )
                    }

                    viewModel.submitAction(PlayBroadcastAction.SelectFaceFilterOption(item))
                }
            },
            presetListener = object : BeautificationFilterOptionViewHolder.Preset.Listener {
                override fun onClick(item: PresetFilterUiModel) {
                    if(item.isRemoveEffect) {
                        beautificationAnalytic.clickNonePreset(viewModel.selectedAccount, beautificationAnalyticStateHolder.getPageSourceForAnalytic())
                    } else {
                        when(item.assetStatus) {
                            BeautificationAssetStatus.NotDownloaded -> {
                                beautificationAnalytic.clickDownloadPreset(viewModel.selectedAccount, beautificationAnalyticStateHolder.getPageSourceForAnalytic(), item.id)
                            }
                            BeautificationAssetStatus.Available -> {
                                beautificationAnalytic.clickPresetMakeup(viewModel.selectedAccount, beautificationAnalyticStateHolder.getPageSourceForAnalytic(), item.id)
                            }
                            else -> {}
                        }
                    }

                    viewModel.submitAction(PlayBroadcastAction.SelectPresetOption(item))
                }
            }
        )
    }

    private val type: Type
        get() = Type.getByValue(arguments?.getInt(KEY_TYPE, Type.Unknown.value) ?: Type.Unknown.value)

    override fun getScreenName(): String = TAG

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBeautificationTabBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.addItemDecoration(BeautificationOptionItemDecoration(requireContext()))
        binding.recyclerView.adapter = adapter
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderBeautificationOption(it.prevValue?.beautificationConfig, it.value.beautificationConfig)
            }
        }
    }

    private fun renderBeautificationOption(
        prev: BeautificationConfigUiModel?,
        curr: BeautificationConfigUiModel
    ) {
        if (prev == curr) return

        val finalList = when (type) {
            Type.FaceFilter -> curr.faceFilters.map { BeautificationFilterOptionAdapter.Model.FaceFilter(it) }
            Type.Preset -> curr.presets.map { BeautificationFilterOptionAdapter.Model.Preset(it) }
            else -> emptyList()
        }

        adapter.setItemsAndAnimateChanges(finalList)
    }

    companion object {
        private const val TAG = "PlayBroMakeupTabFragment"
        private const val KEY_TYPE = "type"

        fun getUnknownFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): BeautificationTabFragment {
            return getFragment(fragmentManager, classLoader, Type.Unknown)
        }

        fun getFaceFilterFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): BeautificationTabFragment {
            return getFragment(fragmentManager, classLoader, Type.FaceFilter)
        }

        fun getPresetFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): BeautificationTabFragment {
            return getFragment(fragmentManager, classLoader, Type.Preset)
        }

        private fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            type: Type
        ): BeautificationTabFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? BeautificationTabFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                BeautificationTabFragment::class.java.name
            ).apply {
                arguments = Bundle().apply {
                    putInt(KEY_TYPE, type.value)
                }
            } as BeautificationTabFragment
        }

        enum class Type(val value: Int) {
            Unknown(-1),
            FaceFilter(0),
            Preset(1);

            fun mapToAnalytic(): PlayBroadcastBeautificationAnalytic.Tab {
                return when(this) {
                    Unknown -> PlayBroadcastBeautificationAnalytic.Tab.Unknown
                    FaceFilter -> PlayBroadcastBeautificationAnalytic.Tab.FaceShaping
                    Preset -> PlayBroadcastBeautificationAnalytic.Tab.Makeup
                }
            }

            companion object {
                fun getByValue(value: Int): Type {
                    return values().firstOrNull {
                        it.value == value
                    } ?: Unknown
                }
            }
        }
    }
}
