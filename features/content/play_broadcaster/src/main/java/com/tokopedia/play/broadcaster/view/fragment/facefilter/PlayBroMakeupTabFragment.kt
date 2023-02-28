package com.tokopedia.play.broadcaster.view.fragment.facefilter

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
import com.tokopedia.play.broadcaster.databinding.FragmentPlayBroMakeupTabBinding
import com.tokopedia.play.broadcaster.ui.itemdecoration.FaceFilterOptionItemDecoration
import com.tokopedia.play.broadcaster.ui.model.FaceFilterUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.FaceFilterOptionViewHolder
import com.tokopedia.play.broadcaster.view.adapter.FaceFilterOptionAdapter
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import com.tokopedia.play_common.util.extension.withCache
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on February 27, 2023
 */
class PlayBroMakeupTabFragment @Inject constructor(
    private val viewModelFactoryCreator: PlayBroadcastViewModelFactory.Creator,
): TkpdBaseV4Fragment() {

    private var _binding: FragmentPlayBroMakeupTabBinding? = null
    private val binding: FragmentPlayBroMakeupTabBinding
        get() = _binding!!

    private val viewModel: PlayBroadcastViewModel by activityViewModels {
        viewModelFactoryCreator.create(
            requireActivity()
        )
    }

    private val adapter by lazyThreadSafetyNone {
        FaceFilterOptionAdapter(object : FaceFilterOptionViewHolder.Listener {
            override fun onClick(item: FaceFilterUiModel) {

            }
        })
    }

    override fun getScreenName(): String = TAG

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayBroMakeupTabBinding.inflate(inflater, container, false)
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
        binding.recyclerView.addItemDecoration(FaceFilterOptionItemDecoration(requireContext()))
        binding.recyclerView.adapter = adapter
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderFaceFilterOptions(it.prevValue?.faceFilter, it.value.faceFilter)
            }
        }
    }

    private fun renderFaceFilterOptions(
        prev: List<FaceFilterUiModel>?,
        curr: List<FaceFilterUiModel>,
    ) {
        if(prev == curr) return

        adapter.setItemsAndAnimateChanges(curr)
    }

    companion object {
        private const val TAG = "PlayBroMakeupTabFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): PlayBroMakeupTabFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? PlayBroMakeupTabFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                PlayBroMakeupTabFragment::class.java.name
            ) as PlayBroMakeupTabFragment
        }
    }
}
