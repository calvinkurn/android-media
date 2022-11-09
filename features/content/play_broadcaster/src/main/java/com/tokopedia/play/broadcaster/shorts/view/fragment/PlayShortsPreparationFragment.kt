package com.tokopedia.play.broadcaster.shorts.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.content.common.producttag.view.fragment.LastTaggedProductFragment
import com.tokopedia.content.common.ui.toolbar.ContentColor
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.play.broadcaster.databinding.FragmentPlayShortsPreparationBinding
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsUiState
import com.tokopedia.play.broadcaster.shorts.view.viewmodel.PlayShortsViewModel
import com.tokopedia.play_common.util.extension.withCache
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
class PlayShortsPreparationFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
) : TkpdBaseV4Fragment() {

    override fun getScreenName(): String = "PlayShortsPreparationFragment"

    private val viewModel by activityViewModels<PlayShortsViewModel> { viewModelFactory }

    private var _binding: FragmentPlayShortsPreparationBinding? = null
    private val binding: FragmentPlayShortsPreparationBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayShortsPreparationBinding.inflate(
            inflater, container, false
        )
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObserver()
    }

    private fun setupView() {
        binding.toolbar.apply {
            navIcon = IconUnify.CLOSE
            setCustomizeContentColor(ContentColor.TRANSPARENT, false)

            setOnBackClickListener {
                activity?.onBackPressed()
            }
        }

        binding.preparationMenu.setOnClickListener {

        }
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderPreparationMenu(it.prevValue, it.value)
            }
        }
    }

    private fun renderPreparationMenu(
        prev: PlayShortsUiState?,
        curr: PlayShortsUiState
    ) {
        if(prev?.menuList == curr.menuList) return

        binding.preparationMenu.submitMenu(curr.menuList)
    }

    companion object {
        private const val TAG = "PlayShortsPreparationFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): PlayShortsPreparationFragment {
            val oldInstance = fragmentManager.findFragmentByTag(PlayShortsPreparationFragment.TAG) as? PlayShortsPreparationFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                PlayShortsPreparationFragment::class.java.name
            ) as PlayShortsPreparationFragment
        }
    }
}
