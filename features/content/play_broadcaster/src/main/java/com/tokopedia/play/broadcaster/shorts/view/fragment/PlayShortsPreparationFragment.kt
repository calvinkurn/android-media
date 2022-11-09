package com.tokopedia.play.broadcaster.shorts.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.content.common.ui.toolbar.ContentColor
import com.tokopedia.content.common.util.hideKeyboard
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.FragmentPlayShortsPreparationBinding
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsToaster
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsTitleFormUiState
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsUiState
import com.tokopedia.play.broadcaster.shorts.view.custom.DynamicPreparationMenu
import com.tokopedia.play.broadcaster.shorts.view.fragment.base.PlayShortsBaseFragment
import com.tokopedia.play.broadcaster.shorts.view.viewmodel.PlayShortsViewModel
import com.tokopedia.play.broadcaster.view.custom.preparation.TitleFormView
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.util.PlayToaster
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
class PlayShortsPreparationFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : PlayShortsBaseFragment() {

    override fun getScreenName(): String = "PlayShortsPreparationFragment"

    private val viewModel by activityViewModels<PlayShortsViewModel> { viewModelFactory }

    private var _binding: FragmentPlayShortsPreparationBinding? = null
    private val binding: FragmentPlayShortsPreparationBinding get() = _binding!!

    private val toaster by viewLifecycleBound(
        creator = { PlayToaster(binding.toasterLayout, it.viewLifecycleOwner) }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayShortsPreparationBinding.inflate(
            inflater,
            container,
            false
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
                viewModel.submitAction(PlayShortsAction.CloseTitleForm)
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

        /** TODO: setup title max character after getConfig */
    }

    private fun setupListener() {
        with(binding) {
            preparationMenu.setOnMenuClickListener {
                when (it.menuId) {
                    DynamicPreparationMenu.TITLE -> {
                        viewModel.submitAction(PlayShortsAction.OpenTitleForm)
                    }
                }
            }

            formTitle.setListener(object : TitleFormView.Listener {
                override fun onCloseTitleForm(view: TitleFormView) {
                    viewModel.submitAction(PlayShortsAction.CloseTitleForm)
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
                renderTitleForm(it.prevValue, it.value)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event ->
                when (val toasterData = event.toaster) {
                    is PlayShortsToaster.ErrorSubmitTitle -> {
                        toaster.showError(
                            toasterData.throwable,
                            duration = Toaster.LENGTH_LONG,
                            actionLabel = getString(R.string.play_broadcast_try_again),
                            actionListener = {
                                toasterData.onRetry()
                            }
                        )
                    }
                    else -> {}
                }
            }
        }
    }

    private fun renderPreparationMenu(
        prev: PlayShortsUiState?,
        curr: PlayShortsUiState
    ) {
        if (prev?.menuList == curr.menuList) return

        binding.preparationMenu.submitMenu(curr.menuList)
    }

    private fun renderTitleForm(
        prev: PlayShortsUiState?,
        curr: PlayShortsUiState
    ) {
        if (prev?.titleForm == curr.titleForm) return

        when (curr.titleForm.state) {
            PlayShortsTitleFormUiState.State.Unknown -> {
                showTitleForm(false)

                binding.formTitle.setLoading(false)
            }
            PlayShortsTitleFormUiState.State.Editing -> {
                showTitleForm(true)

                if (prev?.titleForm?.state == PlayShortsTitleFormUiState.State.Unknown) {
                    binding.formTitle.setTitle(viewModel.title)
                }
                binding.formTitle.setLoading(false)
            }
            PlayShortsTitleFormUiState.State.Loading -> {
                hideKeyboard()
                binding.formTitle.setLoading(true)
            }
        }
    }

    private fun showMainComponent(isShow: Boolean) {
        binding.groupPreparationMain.showWithCondition(isShow)
    }

    private fun showTitleForm(isShow: Boolean) {
        showMainComponent(!isShow)
        binding.formTitle.showWithCondition(isShow)
    }

    companion object {
        private const val TAG = "PlayShortsPreparationFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): PlayShortsPreparationFragment {
            val oldInstance = fragmentManager.findFragmentByTag(PlayShortsPreparationFragment.TAG) as? PlayShortsPreparationFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                PlayShortsPreparationFragment::class.java.name
            ) as PlayShortsPreparationFragment
        }
    }
}
