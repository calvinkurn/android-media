package com.tokopedia.play.broadcaster.shorts.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.content.common.ui.bottomsheet.ContentAccountTypeBottomSheet
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.ui.toolbar.ContentColor
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkSharedPref
import com.tokopedia.content.common.util.hideKeyboard
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.FragmentPlayShortsPreparationBinding
import com.tokopedia.play.broadcaster.setup.product.view.ProductSetupFragment
import com.tokopedia.play.broadcaster.shorts.analytic.PlayShortsAnalytic
import com.tokopedia.play.broadcaster.shorts.factory.PlayShortsMediaSourceFactory
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsUiEvent
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsCoverFormUiState
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsTitleFormUiState
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsUiState
import com.tokopedia.play.broadcaster.shorts.util.animateGone
import com.tokopedia.play.broadcaster.shorts.util.animateShow
import com.tokopedia.play.broadcaster.shorts.view.custom.DynamicPreparationMenu
import com.tokopedia.play.broadcaster.shorts.view.fragment.base.PlayShortsBaseFragment
import com.tokopedia.play.broadcaster.shorts.view.manager.idle.PlayShortsIdleManager
import com.tokopedia.play.broadcaster.shorts.view.viewmodel.PlayShortsViewModel
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.page.PlayBroPageSource
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayAffiliateTnCBottomSheet
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
import com.tokopedia.content.common.R as contentCommonR

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
class PlayShortsPreparationFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val userSession: UserSessionInterface,
    private val exoPlayer: ExoPlayer,
    private val mediaSourceFactory: PlayShortsMediaSourceFactory,
    private val idleManager: PlayShortsIdleManager,
    private val coachMarkSharedPref: ContentCoachMarkSharedPref,
    private val analytic: PlayShortsAnalytic,
) : PlayShortsBaseFragment() {

    override fun getScreenName(): String = "PlayShortsPreparationFragment"

    private val viewModel by activityViewModels<PlayShortsViewModel> { viewModelFactory }

    private var _binding: FragmentPlayShortsPreparationBinding? = null
    private val binding: FragmentPlayShortsPreparationBinding get() = _binding!!

    private val toaster by viewLifecycleBound(
        creator = { PlayToaster(binding.toasterLayout, it.viewLifecycleOwner) }
    )

    private var exitConfirmationDialog: DialogUnify? = null
    private var switchAccountConfirmationDialog: DialogUnify? = null

    private var coachMark: CoachMark2? = null

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

    override fun onStart() {
        super.onStart()
        exoPlayer.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()
        exoPlayer.playWhenReady = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        analytic.viewPreparationPage(viewModel.selectedAccount)

        setupView()
        setupListener()
        setupObserver()
        setupCoachMark()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        coachMark?.dismissCoachMark()
        coachMark = null

        idleManager.clear()

        exitConfirmationDialog = null
        switchAccountConfirmationDialog = null

        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.apply {
            stop()
            release()
        }
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        when (childFragment) {
            is ProductSetupFragment -> {
                childFragment.setDataSource(object : ProductSetupFragment.DataSource {
                    override fun getProductSectionList(): List<ProductTagSectionUiModel> {
                        // TODO("Use uiState directly when uiState already return StateFlow")
                        return viewModel.productSectionList
                    }

                    override fun isEligibleForPin(): Boolean = false

                    override fun getSelectedAccount(): ContentAccountUiModel {
                        return viewModel.selectedAccount
                    }

                    override fun creationId(): String {
                        return viewModel.shortsId
                    }

                    override fun maxProduct(): Int {
                        return viewModel.maxProduct
                    }
                })

                childFragment.setListener(object : ProductSetupFragment.Listener {
                    override fun onProductChanged(productTagSectionList: List<ProductTagSectionUiModel>) {
                        viewModel.submitAction(PlayShortsAction.SetProduct(productTagSectionList))
                    }
                })
            }
            is PlayBroadcastSetupBottomSheet -> {
                childFragment.setListener(object : PlayBroadcastSetupBottomSheet.Listener {
                    override fun onCoverChanged(cover: PlayCoverUiModel) {
                        viewModel.submitAction(PlayShortsAction.SetCover(cover.croppedCover))
                    }
                })
                childFragment.setDataSource(object : PlayBroadcastSetupBottomSheet.DataSource {
                    override fun getProductList(): List<ProductUiModel> {
                        return viewModel.productSectionList.flatMap { it.products }
                    }

                    override fun getSelectedAccount(): ContentAccountUiModel {
                        return viewModel.selectedAccount
                    }

                    override fun getChannelId(): String {
                        return viewModel.shortsId
                    }

                    override fun getPageSource(): PlayBroPageSource {
                        return PlayBroPageSource.Shorts
                    }
                })
            }
            is ContentAccountTypeBottomSheet -> {
                childFragment.setData(viewModel.accountList)
                childFragment.setListener(object : ContentAccountTypeBottomSheet.Listener {
                    override fun onAccountClick(contentAccount: ContentAccountUiModel) {
                        when {
                            contentAccount.isShop -> analytic.clickShopAccount(viewModel.selectedAccount)
                            contentAccount.isUser -> analytic.clickUserAccount(viewModel.selectedAccount)
                            else -> {}
                        }

                        if (contentAccount.id == viewModel.selectedAccount.id) return

                        if (viewModel.isFormFilled) {
                            showSwitchAccountConfirmationDialog(contentAccount)
                        } else {
                            viewModel.submitAction(PlayShortsAction.SwitchAccount(isRefreshAccountList = false))
                        }
                    }

                    override fun onClickClose() {
                        analytic.clickCloseSwitchAccount(viewModel.selectedAccount)
                    }
                })
            }
        }
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
                showExitConfirmationDialog()
                true
            }
        }
    }

    private fun setupView() {
        binding.toolbar.apply {
            navIcon = IconUnify.ARROW_BACK
            setCustomizeContentColor(ContentColor.TRANSPARENT, false)
        }
    }

    private fun setupListener() {
        with(binding) {
            toolbar.apply {
                setOnBackClickListener {
                    analytic.clickBackOnPreparationPage(viewModel.selectedAccount)
                    activity?.onBackPressed()
                }

                setOnAccountClickListener {
                    if (viewModel.isAllowChangeAccount) {
                        analytic.clickSwitchAccount(viewModel.selectedAccount)

                        coachMark?.dismissCoachMark()
                        viewModel.submitAction(PlayShortsAction.ClickSwitchAccount)
                    }
                }
            }

            root.setOnClickListener {
                idleManager.toggleState(viewLifecycleOwner.lifecycleScope)
            }

            preparationMenu.setOnMenuClickListener {
                coachMark?.dismissCoachMark()

                when (it.menuId) {
                    DynamicPreparationMenu.TITLE -> {
                        analytic.clickMenuTitle(viewModel.selectedAccount)
                        viewModel.submitAction(PlayShortsAction.OpenTitleForm)
                    }
                    DynamicPreparationMenu.PRODUCT -> {
                        analytic.clickMenuProduct(viewModel.selectedAccount)
                        openProductPicker()
                    }
                    DynamicPreparationMenu.COVER -> {
                        analytic.clickMenuCover(viewModel.selectedAccount)
                        viewModel.submitAction(PlayShortsAction.OpenCoverForm)
                    }
                }
            }

            formTitle.setListener(object : TitleFormView.Listener {
                override fun onClearTitle() {
                    analytic.clickClearTextBoxOnTitleForm(viewModel.selectedAccount)
                }

                override fun onClickTextField() {
                    analytic.clickTextFieldOnTitleForm(viewModel.selectedAccount)
                }

                override fun onCloseTitleForm(view: TitleFormView) {
                    analytic.clickBackOnTitleForm(viewModel.selectedAccount)
                    hideKeyboard()
                    viewModel.submitAction(PlayShortsAction.CloseTitleForm)
                }

                override fun onTitleSaved(view: TitleFormView, title: String) {
                    analytic.clickSaveOnTitleForm(viewModel.selectedAccount)
                    viewModel.submitAction(PlayShortsAction.UploadTitle(title))
                }
            })

            formCover.setListener(object : CoverFormView.Listener {
                override fun onCloseCoverForm() {
                    analytic.clickCloseOnCoverForm(viewModel.selectedAccount)

                    viewModel.submitAction(PlayShortsAction.CloseCoverForm)
                }

                override fun onClickCoverPreview(isEditCover: Boolean) {
                    analytic.clickSelectCoverOnCoverForm(viewModel.selectedAccount)

                    openCoverSetupFragment()
                }
            })

            btnNext.setOnClickListener {
                analytic.clickNextOnPreparationPage(viewModel.selectedAccount)

                viewModel.submitAction(PlayShortsAction.ClickNext)
            }
        }
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderMedia(it.prevValue, it.value)
                renderToolbar(it.prevValue, it.value)
                renderPreparationMenu(it.prevValue, it.value)
                renderTitleForm(it.prevValue, it.value)
                renderCoverForm(it.prevValue, it.value)
                renderNextButton(it.prevValue, it.value)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is PlayShortsUiEvent.ErrorUploadTitle -> {
                        toaster.showError(
                            event.throwable,
                            duration = Toaster.LENGTH_LONG,
                            actionLabel = getString(R.string.play_broadcast_try_again),
                            actionListener = {
                                event.onRetry()
                            }
                        )
                    }
                    is PlayShortsUiEvent.ErrorSwitchAccount -> {
                        toaster.showError(
                            event.throwable,
                            duration = Toaster.LENGTH_SHORT
                        )
                    }
                    is PlayShortsUiEvent.SwitchAccount -> {
                        showSwitchAccountBottomSheet()
                    }
                    else -> {}
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            idleManager.state.collectLatest {
                when (it) {
                    PlayShortsIdleManager.State.StandBy -> setupUiStandby()
                    PlayShortsIdleManager.State.Idle -> setupUiIdle()
                }
            }
        }
    }

    private fun setupCoachMark() {

        fun onCloseCoachMark() {
            analytic.clickCloseCoachMarkOnPreparationPage(viewModel.selectedAccount)
            coachMark?.dismissCoachMark()
        }

        if(coachMark != null) return

        val coachMarkItems = mutableListOf<CoachMark2Item>().apply {
            if(!coachMarkSharedPref.hasBeenShown(ContentCoachMarkSharedPref.Key.PlayShortsPreparation, userSession.userId)) {
                add(
                    CoachMark2Item(
                        anchorView = binding.preparationMenu,
                        title = getString(R.string.play_shorts_preparation_coachmark_title),
                        description = getString(R.string.play_shorts_preparation_coachmark_description),
                        position = CoachMark2.POSITION_TOP
                    )
                )
                coachMarkSharedPref.setHasBeenShown(ContentCoachMarkSharedPref.Key.PlayShortsPreparation, userSession.userId)
            }

            if(viewModel.isAllowChangeAccount && viewModel.isFirstSwitchAccount) {
                add(
                    CoachMark2Item(
                        anchorView = binding.toolbar,
                        title = requireContext().getString(contentCommonR.string.sa_coach_mark_title),
                        description = requireContext().getString(contentCommonR.string.sa_shorts_coach_mark_subtitle),
                        position = CoachMark2.POSITION_BOTTOM
                    )
                )
                viewModel.submitAction(PlayShortsAction.SetNotFirstSwitchAccount)
            }
        }

        if(coachMarkItems.isNotEmpty()) {
            if(coachMark == null) {
                coachMark = CoachMark2(requireContext())
            }

            coachMark?.showCoachMark(ArrayList(coachMarkItems))

            if(coachMarkItems.size == 1) {
                coachMark?.simpleCloseIcon?.setOnClickListener { onCloseCoachMark() }
            }
            else {
                coachMark?.stepCloseIcon?.setOnClickListener { onCloseCoachMark() }
            }
        }
    }

    private fun setupUiStandby() {
        Log.d("<LOG>", "Standby")
        binding.preparationMenu.showMenuText(true)
        binding.flBottomBackground.animateShow()
        binding.flTopBackground.animateShow()
        binding.flSideBackground.animateShow()
    }

    private fun setupUiIdle() {
        Log.d("<LOG>", "Idle")
        binding.preparationMenu.showMenuText(false)
        binding.flBottomBackground.animateGone()
        binding.flTopBackground.animateGone()
        binding.flSideBackground.animateGone()
    }

    private fun renderMedia(
        prev: PlayShortsUiState?,
        curr: PlayShortsUiState
    ) {
        if (binding.exoPlayer.player == null || prev?.media?.mediaUri != curr.media.mediaUri) {
            val mediaSource = mediaSourceFactory.create(curr.media.mediaUri)
            exoPlayer.prepare(mediaSource)
            binding.exoPlayer.player = exoPlayer

            /** TODO
             * some logic to check whether to show bottom sheet affiliate or not
             * but BE team is not ready yet
             */
            openShortsAffiliateTncBottomSheet()
        }
    }

    private fun renderToolbar(
        prev: PlayShortsUiState?,
        curr: PlayShortsUiState
    ) {
        if (prev?.selectedAccount == curr.selectedAccount) return

        with(binding.toolbar) {
            title = getString(R.string.play_shorts_toolbar_title)
            subtitle = curr.selectedAccount.name
            icon = curr.selectedAccount.iconUrl

            showHideExpandIcon(viewModel.isAllowChangeAccount)
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
                binding.formTitle.setMaxCharacter(viewModel.maxTitleCharacter)
                binding.formTitle.setPlaceholder(getString(R.string.play_shorts_title_form_hint))
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
                binding.formCover.setAuthorName(viewModel.selectedAccount.name)

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

        if (isShow) {
            idleManager.startIdleTimer(viewLifecycleOwner.lifecycleScope)
        } else {
            idleManager.forceStandByMode()
        }
    }

    private fun showTitleForm(isShow: Boolean) {
        showMainComponent(!isShow)
        binding.formTitle.showWithCondition(isShow)

        if(isShow) {
            analytic.openScreenTitleForm(viewModel.selectedAccount)
        }
    }

    private fun showCoverForm(isShow: Boolean) {
        showMainComponent(!isShow)
        binding.formCover.showWithCondition(isShow)

        if(isShow) {
            analytic.openScreenCoverForm(viewModel.selectedAccount)
        }
    }

    private fun showExitConfirmationDialog() {
        if (exitConfirmationDialog == null) {
            exitConfirmationDialog = DialogUnify(requireContext(), DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.play_shorts_exit_confirmation_title))
                setDescription(getString(R.string.play_shorts_exit_confirmation_description))
                setPrimaryCTAText(getString(R.string.play_shorts_exit_confirmation_continue))
                setPrimaryCTAClickListener {
                    analytic.clickContinueOnLeaveConfirmationPopup(viewModel.selectedAccount)

                    dismiss()
                }
                setSecondaryCTAText(getString(R.string.play_shorts_exit_confirmation_exit))
                setSecondaryCTAClickListener {
                    dismiss()
                    activity?.finish()
                }
            }
        }

        if (exitConfirmationDialog?.isShowing == false) {
            analytic.viewLeavePreparationConfirmationPopup(viewModel.selectedAccount)

            exitConfirmationDialog?.show()
        }
    }

    private fun showSwitchAccountConfirmationDialog(selectedAccount: ContentAccountUiModel) {
        if (switchAccountConfirmationDialog == null) {
            switchAccountConfirmationDialog = DialogUnify(requireContext(), DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
        }

        switchAccountConfirmationDialog?.apply {
            setTitle(
                getString(
                    if (selectedAccount.isShop) {
                        R.string.play_shorts_switch_account_to_shop_title
                    } else {
                        R.string.play_shorts_switch_account_to_user_title
                    }
                )
            )
            setDescription(
                getString(
                    if (selectedAccount.isShop) {
                        R.string.play_shorts_switch_account_to_shop_description
                    } else {
                        R.string.play_shorts_switch_account_to_user_description
                    }
                )
            )
            setPrimaryCTAText(getString(R.string.play_shorts_switch_account_cancel))
            setPrimaryCTAClickListener {
                when {
                    selectedAccount.isShop -> analytic.clickCancelSwitchAccountToShop(viewModel.selectedAccount)
                    selectedAccount.isUser -> analytic.clickCancelSwitchAccountToUser(viewModel.selectedAccount)
                    else -> {}
                }

                dismiss()
            }
            setSecondaryCTAText(
                getString(
                    if (selectedAccount.isShop) {
                        R.string.play_shorts_switch_account_to_shop_confirm
                    } else {
                        R.string.play_shorts_switch_account_to_user_confirm
                    }
                )
            )
            setSecondaryCTAClickListener {
                dismiss()
                viewModel.submitAction(PlayShortsAction.SwitchAccount(isRefreshAccountList = false))
            }
        }

        if (switchAccountConfirmationDialog?.isShowing == false) {
            when {
                selectedAccount.isShop -> analytic.viewSwitchAccountToShopConfirmation(viewModel.selectedAccount)
                selectedAccount.isUser -> analytic.viewSwitchAccountToUserConfirmation(viewModel.selectedAccount)
                else -> {}
            }

            switchAccountConfirmationDialog?.show()
        }
    }

    private fun showSwitchAccountBottomSheet() {
        analytic.viewSwitchAccountBottomSheet(viewModel.selectedAccount)

        ContentAccountTypeBottomSheet
            .getFragment(childFragmentManager, requireActivity().classLoader)
            .show(childFragmentManager)
    }

    private fun openShortsAffiliateTncBottomSheet() {
        PlayAffiliateTnCBottomSheet
            .getFragment(childFragmentManager, requireActivity().classLoader)
            .show(childFragmentManager)
    }

    private fun openProductPicker() {
        childFragmentManager.beginTransaction()
            .add(ProductSetupFragment::class.java, null, null)
            .commit()
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
