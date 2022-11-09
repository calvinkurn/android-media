package com.tokopedia.play.broadcaster.shorts.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.content.common.ui.toolbar.ContentColor
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkConfig
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkManager
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkSharedPref
import com.tokopedia.content.common.util.hideKeyboard
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.FragmentPlayShortsPreparationBinding
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsToaster
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsCoverFormUiState
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsTitleFormUiState
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsUiState
import com.tokopedia.play.broadcaster.shorts.view.custom.DynamicPreparationMenu
import com.tokopedia.play.broadcaster.shorts.view.fragment.base.PlayShortsBaseFragment
import com.tokopedia.play.broadcaster.shorts.view.viewmodel.PlayShortsViewModel
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupBottomSheet
import com.tokopedia.play.broadcaster.view.custom.preparation.CoverFormView
import com.tokopedia.play.broadcaster.view.custom.preparation.TitleFormView
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.util.PlayToaster
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
class PlayShortsPreparationFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val userSession: UserSessionInterface,
    private val coachMarkManager: ContentCoachMarkManager
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
        setupCoachMark()
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        when (childFragment) {
            is PlayBroadcastSetupBottomSheet -> {
                childFragment.setListener(object : PlayBroadcastSetupBottomSheet.Listener {
                    override fun onCoverChanged(cover: PlayCoverUiModel) {
                        viewModel.submitAction(PlayShortsAction.SetCover(cover.croppedCover))
                    }
                })
                childFragment.setDataSource(object : PlayBroadcastSetupBottomSheet.DataSource {
                    override fun getProductList(): List<ProductUiModel> {
                        /** TODO: send product here */
                        return emptyList()
//                        return parentViewModel.productSectionList.flatMap { it.products }
                    }

                    override fun getAuthorId(): String {
                        return viewModel.authorId
                    }

                    override fun getChannelId(): String {
                        return viewModel.shortsId
                    }
                })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        coachMarkManager.dismissAllCoachMark()
    }

    override fun onBackPressed(): Boolean {
        return when {
            binding.formTitle.visibility == View.VISIBLE -> {
                hideKeyboard()
                viewModel.submitAction(PlayShortsAction.CloseTitleForm)
                true
            }
            binding.formCover.visibility == View.VISIBLE -> {
                viewModel.submitAction(PlayShortsAction.CloseCoverForm)
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
                coachMarkManager.hasBeenShown(binding.preparationMenu)

                when (it.menuId) {
                    DynamicPreparationMenu.TITLE -> {
                        viewModel.submitAction(PlayShortsAction.OpenTitleForm)
                    }
                    DynamicPreparationMenu.PRODUCT -> {
                        /** TODO: open product picker */
                    }
                    DynamicPreparationMenu.COVER -> {
                        viewModel.submitAction(PlayShortsAction.OpenCoverForm)
                    }
                }
            }

            formTitle.setListener(object : TitleFormView.Listener {
                override fun onCloseTitleForm(view: TitleFormView) {
                    hideKeyboard()
                    viewModel.submitAction(PlayShortsAction.CloseTitleForm)
                }

                override fun onTitleSaved(view: TitleFormView, title: String) {
                    viewModel.submitAction(PlayShortsAction.SubmitTitle(title))
                }
            })

            formCover.setListener(object : CoverFormView.Listener {
                override fun onCloseCoverForm() {
                    viewModel.submitAction(PlayShortsAction.CloseCoverForm)
                }

                override fun onClickCoverPreview(isEditCover: Boolean) {
                    openCoverSetupFragment()
                }
            })

            btnNext.setOnClickListener {
                viewModel.submitAction(PlayShortsAction.ClickNext)
            }
        }
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderPreparationMenu(it.prevValue, it.value)
                renderTitleForm(it.prevValue, it.value)
                renderCoverForm(it.prevValue, it.value)
                renderNextButton(it.prevValue, it.value)
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

    private fun setupCoachMark() {
        coachMarkManager.setupCoachMark(
            ContentCoachMarkConfig(binding.preparationMenu).apply {
                title = getString(R.string.play_shorts_preparation_coachmark_title)
                subtitle = getString(R.string.play_shorts_preparation_coachmark_description)
                position = CoachMark2.POSITION_TOP
                setCoachmarkPrefKey(ContentCoachMarkSharedPref.Key.PlayShortsPreparation, userSession.userId)
            }
        )
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

    private fun renderCoverForm(
        prev: PlayShortsUiState?,
        curr: PlayShortsUiState
    ) {
        if (prev?.coverForm == curr.coverForm) return

        when (curr.coverForm.state) {
            PlayShortsCoverFormUiState.State.Unknown -> {
                showCoverForm(false)
            }
            PlayShortsCoverFormUiState.State.Editing -> {
                showCoverForm(true)

                binding.formCover.setTitle(viewModel.title)
                binding.formCover.setAuthorName(viewModel.authorName)

                val coverUri = curr.coverForm.coverUri
                if (coverUri.isNotEmpty()) {
                    binding.formCover.setCover(coverUri)
                } else {
                    binding.formCover.setInitialCover()
                }
            }
        }
    }

    private fun renderNextButton(
        prev: PlayShortsUiState?,
        curr: PlayShortsUiState
    ) {
        if (prev?.menuList == curr.menuList) return

        binding.btnNext.isEnabled = viewModel.isAllMandatoryMenuChecked
    }

    private fun showMainComponent(isShow: Boolean) {
        binding.groupPreparationMain.showWithCondition(isShow)
    }

    private fun showTitleForm(isShow: Boolean) {
        showMainComponent(!isShow)
        binding.formTitle.showWithCondition(isShow)
    }

    private fun showCoverForm(isShow: Boolean) {
        showMainComponent(!isShow)
        binding.formCover.showWithCondition(isShow)
    }

    private fun openCoverSetupFragment() {
        val setupClass = PlayBroadcastSetupBottomSheet::class.java
        val fragmentFactory = childFragmentManager.fragmentFactory
        val setupFragment = fragmentFactory.instantiate(requireContext().classLoader, setupClass.name) as PlayBroadcastSetupBottomSheet
        setupFragment.show(childFragmentManager)
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
