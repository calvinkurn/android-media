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
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.broadcaster.databinding.FragmentPlayShortsPreparationBinding
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsUiState
import com.tokopedia.play.broadcaster.shorts.view.custom.DynamicPreparationMenu
import com.tokopedia.play.broadcaster.shorts.view.fragment.base.PlayShortsBaseFragment
import com.tokopedia.play.broadcaster.shorts.view.viewmodel.PlayShortsViewModel
import com.tokopedia.play.broadcaster.view.custom.preparation.TitleFormView
import com.tokopedia.play_common.util.extension.withCache
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
class PlayShortsPreparationFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
) : PlayShortsBaseFragment() {

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
        setupListener()
        setupObserver()
    }

    override fun onBackPressed(): Boolean {
        return when {
            binding.formTitle.visibility == View.VISIBLE -> {
                showTitleForm(false)
                true
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    private fun setupView() {
        binding.toolbar.apply {
            navIcon = IconUnify.CLOSE
            setCustomizeContentColor(ContentColor.TRANSPARENT, false)

            setOnBackClickListener {
                activity?.onBackPressed()
            }
        }

        binding.preparationMenu.setOnMenuClickListener {
            when(it.menuId) {
                DynamicPreparationMenu.TITLE -> {
                    showTitleForm(true)
                }
            }
        }
    }

    private fun setupListener() {
        with(binding) {
            formTitle.setListener(object : TitleFormView.Listener {
                override fun onCloseTitleForm(view: TitleFormView) {
                    activity?.onBackPressed()
                }

                override fun onTitleSaved(view: TitleFormView, title: String) {
                    viewModel.submitAction(PlayShortsAction.SubmitTitle(title))
                }
            })
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

    private fun showMainComponent(isShow: Boolean) {
        binding.groupPreparationMain.showWithCondition(isShow)
    }

    private fun showTitleForm(isShow: Boolean) {
        showMainComponent(!isShow)
        binding.formTitle.showWithCondition(isShow)

        if(isShow) {
            binding.formTitle.setTitle(viewModel.title)
            binding.formTitle.setLoading(false)
        }
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
