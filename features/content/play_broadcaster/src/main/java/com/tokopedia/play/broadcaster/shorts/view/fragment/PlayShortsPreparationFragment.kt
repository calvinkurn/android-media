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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.content.common.ui.bottomsheet.ContentAccountTypeBottomSheet
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.ui.toolbar.ContentColor
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkSharedPref
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkSharedPref.Key
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.databinding.FragmentPlayShortsPreparationBinding
import com.tokopedia.play.broadcaster.setup.product.view.ProductSetupFragment
import com.tokopedia.play.broadcaster.shorts.analytic.PlayShortsAnalytic
import com.tokopedia.play.broadcaster.shorts.factory.PlayShortsMediaSourceFactory
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsUiEvent
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsTitleFormUiState
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsUiState
import com.tokopedia.play.broadcaster.shorts.util.animateGone
import com.tokopedia.play.broadcaster.shorts.util.animateShow
import com.tokopedia.play.broadcaster.shorts.view.custom.DynamicPreparationMenu
import com.tokopedia.play.broadcaster.shorts.view.fragment.base.PlayShortsBaseFragment
import com.tokopedia.play.broadcaster.shorts.view.manager.idle.PlayShortsIdleManager
import com.tokopedia.play.broadcaster.shorts.view.viewmodel.PlayShortsViewModel
import com.tokopedia.play.broadcaster.ui.itemdecoration.PlayBroadcastPreparationBannerItemDecoration
import com.tokopedia.play.broadcaster.ui.model.PlayBroadcastPreparationBannerModel
import com.tokopedia.play.broadcaster.ui.model.PlayBroadcastPreparationBannerModel.Companion.TYPE_SHORTS_AFFILIATE
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.page.PlayBroPageSource
import com.tokopedia.play.broadcaster.view.adapter.PlayBroadcastPreparationBannerAdapter
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupCoverBottomSheet
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupCoverBottomSheet.DataSource
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupTitleBottomSheet
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayShortsAffiliateSuccessBottomSheet
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayShortsAffiliateTnCBottomSheet
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.util.PlayToaster
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
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
) : PlayShortsBaseFragment(),
    PlayBroadcastSetupTitleBottomSheet.Listener,
    PlayBroadcastSetupCoverBottomSheet.Listener,
    PlayBroadcastPreparationBannerAdapter.BannerListener {

    override fun getScreenName(): String = "PlayShortsPreparationFragment"

    private val viewModel by activityViewModels<PlayShortsViewModel> { viewModelFactory }

    private var _binding: FragmentPlayShortsPreparationBinding? = null
    private val binding: FragmentPlayShortsPreparationBinding get() = _binding!!

    private val adapterBanner: PlayBroadcastPreparationBannerAdapter by lazyThreadSafetyNone {
        PlayBroadcastPreparationBannerAdapter(this)
    }
    private var mLayoutManager: LinearLayoutManager? = null
    private val snapHelper = PagerSnapHelper()
    private val toaster by viewLifecycleBound(
        creator = { PlayToaster(binding.toasterLayout, it.viewLifecycleOwner) }
    )

    private var productSetupPendingToaster: String? = null
    private var exitConfirmationDialog: DialogUnify? = null
    private var switchAccountConfirmationDialog: DialogUnify? = null

    private var coachMarkItems = mutableListOf<CoachMark2Item>()
    private var coachMark: CoachMark2? = null

    private val scrollListener by lazyThreadSafetyNone {
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val snappedView = snapHelper.findSnapView(mLayoutManager) ?: return

                val position = mLayoutManager?.getPosition(snappedView)
                binding.pcBannerPreparation.setCurrentIndicator(position.orZero())
            }
        }
    }

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
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding.rvBannerPreparation.removeOnScrollListener(scrollListener)
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

                    override fun getPageSource(): PlayBroPageSource {
                        return PlayBroPageSource.Shorts
                    }

                    override fun fetchCommissionProduct(): Boolean {
                        return viewModel.isSelectedAccountAffiliate
                    }
                })

                childFragment.setListener(object : ProductSetupFragment.Listener {
                    override fun onProductChanged(productTagSectionList: List<ProductTagSectionUiModel>) {
                        viewModel.submitAction(PlayShortsAction.SetProduct(productTagSectionList))
                    }
                    override fun onProductSetupDismissed() {
                        if (productSetupPendingToaster == null) return
                        viewModel.submitAction(
                            PlayShortsAction.ResetUploadState
                        )
                        toaster.showToaster(productSetupPendingToaster.orEmpty())
                        productSetupPendingToaster = null
                    }

                    override fun onProductSummaryCommissionShown() {
                        analytic.sendImpressionTagProductCommissionEvent(viewModel.selectedAccount.id)
                    }
                })
            }
            is PlayBroadcastSetupTitleBottomSheet -> {
                childFragment.setupListener(this)
                childFragment.setupData(viewModel.title, viewModel.maxTitleCharacter)
            }
            is PlayBroadcastSetupCoverBottomSheet -> {
                childFragment.setupListener(listener = this)
                childFragment.setupDataSource(dataSource = object : DataSource {
                    override fun getEntryPoint(): String {
                        return PAGE_NAME
                    }

                    override fun getContentAccount(): ContentAccountUiModel {
                        return viewModel.selectedAccount
                    }

                    override fun getChannelId(): String {
                        return viewModel.shortsId
                    }

                    override fun getChannelTitle(): String {
                        return viewModel.title
                    }

                    override fun getDataStore(): PlayBroadcastDataStore {
                        return viewModel.mDataStore
                    }

                })

                val isShowCoachMark = viewModel.isShowSetupCoverCoachMark
                childFragment.needToShowCoachMark(isShowCoachMark)
                if (isShowCoachMark) viewModel.submitAction(PlayShortsAction.SetShowSetupCoverCoachMark)
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
            is PlayShortsAffiliateTnCBottomSheet -> {
                childFragment.setListener(object: PlayShortsAffiliateTnCBottomSheet.Listener {
                    override fun onCheckBoxChecked() {
                        analytic.sendClickAcceptTcAffiliateEvent(viewModel.selectedAccount.id)
                    }
                    override fun onSubmitTnc() {
                        analytic.sendClickNextRegisterAffiliateEvent(viewModel.selectedAccount.id)
                        viewModel.submitAction(PlayShortsAction.SubmitOnboardAffiliateTnc)
                    }
                })
            }
            is PlayShortsAffiliateSuccessBottomSheet -> {
                childFragment.setupData(viewModel.selectedAccount.name)
                childFragment.setListener(object: PlayShortsAffiliateSuccessBottomSheet.Listener{
                    override fun onClickNext() {
                        analytic.sendClickNextCreateContentEvent(viewModel.selectedAccount.id)
                    }
                })
            }
        }
    }

    override fun onBackPressed(): Boolean {
        showExitConfirmationDialog()
        return true
    }

    override fun onBannerClick(data: PlayBroadcastPreparationBannerModel) {
        when (data.type) {
            TYPE_SHORTS_AFFILIATE -> {
                analytic.sendClickRegisterAffiliateCardEvent(viewModel.selectedAccount.id)
                openShortsAffiliateTncBottomSheet()
            }
            else -> return
        }
    }

    private fun setupView() {
        mLayoutManager = LinearLayoutManager(requireContext())
        binding.toolbar.apply {
            navIcon = IconUnify.ARROW_BACK
            setCustomizeContentColor(ContentColor.TRANSPARENT, false)
        }
        binding.rvBannerPreparation.apply {
            layoutManager = mLayoutManager
            adapter = adapterBanner
            if (itemDecorationCount == 0) addItemDecoration(
                PlayBroadcastPreparationBannerItemDecoration(context)
            )
            addOnScrollListener(scrollListener)
        }
        snapHelper.attachToRecyclerView(binding.rvBannerPreparation)

        if (!coachMarkSharedPref.hasBeenShown(Key.PlayShortsPreparation, userSession.userId)) {
            setupCoachMark(
                CoachMark2Item(
                    anchorView = binding.preparationMenu,
                    title = getString(R.string.play_shorts_preparation_coachmark_title),
                    description = getString(R.string.play_shorts_preparation_coachmark_description),
                    position = CoachMark2.POSITION_TOP
                )
            )
            coachMarkSharedPref.setHasBeenShown(Key.PlayShortsPreparation, userSession.userId)
        }

        if (viewModel.isAllowChangeAccount && !coachMarkSharedPref.hasBeenShown(
                Key.SwitchAccount,
                userSession.userId
            )
        ) {
            setupCoachMark(
                CoachMark2Item(
                    anchorView = binding.toolbar,
                    title = requireContext().getString(contentCommonR.string.sa_coach_mark_title),
                    description = requireContext().getString(contentCommonR.string.sa_shorts_coach_mark_subtitle),
                    position = CoachMark2.POSITION_BOTTOM
                )
            )
            coachMarkSharedPref.setHasBeenShown(Key.SwitchAccount, userSession.userId)
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

                when (it.menu) {
                    DynamicPreparationMenu.Menu.Title -> {
                        viewModel.submitAction(PlayShortsAction.OpenTitleForm)
                        analytic.clickMenuTitle(viewModel.selectedAccount)
                        openSetupTitleBottomSheet()
                    }
                    DynamicPreparationMenu.Menu.Product -> {
                        analytic.clickMenuProduct(viewModel.selectedAccount)
                        openProductPicker()
                    }
                    DynamicPreparationMenu.Menu.Cover -> {
                        viewModel.submitAction(PlayShortsAction.OpenCoverForm)
                        analytic.clickMenuCover(viewModel.selectedAccount)
                        openSetupCoverBottomSheet()
                    }
                    else -> {}
                }
            }

            btnNext.setOnClickListener {
                analytic.clickNextOnPreparationPage(viewModel.selectedAccount)

                viewModel.submitAction(PlayShortsAction.ClickNext)
            }
        }
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderTitleForm(it.prevValue, it.value)
                renderMedia(it.prevValue, it.value)
                renderToolbar(it.prevValue, it.value)
                renderPreparationMenu(it.prevValue, it.value)
                renderNextButton(it.prevValue, it.value)
                renderBannerPreparationPage(it.prevValue?.bannerPreparation, it.value.bannerPreparation)
                renderCoachMarkProductCommission(it.prevValue?.isAffiliate, it.value.isAffiliate)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is PlayShortsUiEvent.ErrorUploadTitle -> {
                        getSetupTitleBottomSheet().failSubmit(event.throwable.message)
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
                    is PlayShortsUiEvent.ErrorOnboardAffiliate -> {
                        if (event.error == null) return@collect
                        val currentBottomSheet = getShortsAffiliateTncBottomSheet()
                        currentBottomSheet.showErrorToast(event.error)
                    }
                    is PlayShortsUiEvent.SuccessOnboardAffiliate -> {
                        val currentBottomSheet = getShortsAffiliateTncBottomSheet()
                        currentBottomSheet.dismiss()
                        openShortsAffiliateSuccessBottomSheet()
                    }
                    is PlayShortsUiEvent.AutoGeneratedCoverToaster -> {
                        productSetupPendingToaster = if (event.isToasterUpdate) {
                            getString(R.string.play_setup_cover_auto_generated_toaster_update_cover_from_product)
                        } else {
                            getString(R.string.play_setup_cover_auto_generated_toaster_delete_cover_from_product)
                        }
                    }
                    is PlayShortsUiEvent.ResetForm -> {
                        coachMark = null
                        coachMarkItems.clear()
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
                    else -> return@collectLatest
                }
            }
        }
    }

    private fun renderTitleForm(
        prev: PlayShortsUiState?,
        curr: PlayShortsUiState
    ) {
        if (prev?.titleForm == curr.titleForm) return

        if (curr.titleForm.state == PlayShortsTitleFormUiState.State.Unknown
            && getSetupTitleBottomSheet().isAdded && getSetupTitleBottomSheet().isVisible) {
            getSetupTitleBottomSheet().dismiss()
        }
    }

    private fun renderBannerPreparationPage(
        prev: List<PlayBroadcastPreparationBannerModel>?,
        curr: List<PlayBroadcastPreparationBannerModel>
    ) {
        if (prev == curr) return

        adapterBanner.setItemsAndAnimateChanges(curr)
        if (curr.size > 1) binding.pcBannerPreparation.setIndicator(curr.size)
        else binding.pcBannerPreparation.setIndicator(0)
    }

    private fun renderCoachMarkProductCommission(prev: Boolean?, curr: Boolean) {
        if (prev == curr || !curr || coachMarkSharedPref.hasBeenShown(Key.ProductCommission)) return

        analytic.sendImpressionCekProductCommissionEvent(viewModel.selectedAccount.id)
        binding.preparationMenu.getMenuView(
            menu = DynamicPreparationMenu.Menu.Product,
            menuView = { menuView ->
                setupCoachMark(
                    CoachMark2Item(
                        menuView,
                        getString(R.string.play_shorts_affiliate_coach_mark_product_commission_title),
                        getString(R.string.play_shorts_affiliate_coach_mark_product_commission_subtitle),
                        CoachMark2.POSITION_TOP,
                    )
                )
            }
        )
        coachMarkSharedPref.setHasBeenShown(Key.ProductCommission)
    }

    private fun setupCoachMark(coachMarkItem: CoachMark2Item) {
        fun onDismissCoachMark() {
            analytic.clickCloseCoachMarkOnPreparationPage(viewModel.selectedAccount)
            coachMark?.dismissCoachMark()
        }

        if (coachMarkItems.contains(coachMarkItem)) return

        coachMarkItems.add(coachMarkItem)

        if (coachMark == null) coachMark = CoachMark2(requireContext())
        coachMark?.showCoachMark(ArrayList(coachMarkItems))

        if (coachMarkItems.size == 1) {
            coachMark?.simpleCloseIcon?.setOnClickListener { onDismissCoachMark() }
        } else {
            coachMark?.stepCloseIcon?.setOnClickListener { onDismissCoachMark() }
        }
    }

    private fun setupUiStandby() {
        binding.preparationMenu.showMenuText(true)
        binding.flBottomBackground.animateShow()
        binding.flTopBackground.animateShow()
        binding.flSideBackground.animateShow()
    }

    private fun setupUiIdle() {
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

    private fun renderNextButton(
        prev: PlayShortsUiState?,
        curr: PlayShortsUiState
    ) {
        if (prev?.menuList == curr.menuList) return

        binding.btnNext.isEnabled = viewModel.isAllMandatoryMenuChecked
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

    private fun getShortsAffiliateTncBottomSheet() = PlayShortsAffiliateTnCBottomSheet
        .getFragment(childFragmentManager, requireActivity().classLoader)

    private fun openShortsAffiliateTncBottomSheet() {
        getShortsAffiliateTncBottomSheet().show(childFragmentManager)
    }

    private fun openShortsAffiliateSuccessBottomSheet() {
        PlayShortsAffiliateSuccessBottomSheet
            .getFragment(childFragmentManager, requireActivity().classLoader)
            .show(childFragmentManager)
    }

    private fun openProductPicker() {
        childFragmentManager.beginTransaction()
            .add(ProductSetupFragment::class.java, null, null)
            .commit()
    }

    private fun openSetupTitleBottomSheet() {
        childFragmentManager.executePendingTransactions()
        val existingFragment =
            childFragmentManager.findFragmentByTag(PlayBroadcastSetupTitleBottomSheet.TAG)
        if (existingFragment is PlayBroadcastSetupTitleBottomSheet && existingFragment.isVisible) return
        getSetupTitleBottomSheet().show(childFragmentManager)
    }

    private fun getSetupTitleBottomSheet() = PlayBroadcastSetupTitleBottomSheet
        .getFragment(childFragmentManager, requireActivity().classLoader)

    private fun openSetupCoverBottomSheet() {
        childFragmentManager.executePendingTransactions()
        val existingFragment = childFragmentManager.findFragmentByTag(
            PlayBroadcastSetupCoverBottomSheet.TAG)
        if (existingFragment is PlayBroadcastSetupCoverBottomSheet && existingFragment.isVisible) return
        getSetupCoverBottomSheet()?.show(childFragmentManager)
    }

    private fun getSetupCoverBottomSheet() = PlayBroadcastSetupCoverBottomSheet
        .getFragment(childFragmentManager, requireActivity().classLoader)

    override fun submitTitle(title: String) {
        analytic.clickSaveOnTitleForm(viewModel.selectedAccount)
        viewModel.submitAction(PlayShortsAction.UploadTitle(title))
    }

    override fun onTitleFormOpen() {
        analytic.openScreenTitleForm(viewModel.selectedAccount)
    }

    override fun onBackPressedTitleForm() {
        analytic.clickBackOnTitleForm(viewModel.selectedAccount)
    }

    override fun onTextFieldTitleFormClicked() {
        analytic.clickTextFieldOnTitleForm(viewModel.selectedAccount)
    }

    override fun onTextFieldTitleFormCleared() {
        analytic.clickClearTextBoxOnTitleForm(viewModel.selectedAccount)
    }

    override fun onUploadCoverSuccess() {
        viewModel.submitAction(PlayShortsAction.UpdateCover)
    }

    override fun onOpenCoverForm() {
        analytic.openScreenCoverForm(viewModel.selectedAccount)
    }

    override fun onCloseCoverForm() {
        analytic.clickCloseOnCoverForm(viewModel.selectedAccount)
    }

    override fun onClickSelectCoverOnCoverForm() {
        analytic.clickSelectCoverOnCoverForm(viewModel.selectedAccount)
    }

    override fun dismissSetupCover(source: Int) {
        if (getSetupCoverBottomSheet()?.isAdded == true) getSetupCoverBottomSheet()?.dismiss()

        if (viewModel.uploadedCoverSource == PlayBroadcastSetupCoverBottomSheet.TAB_AUTO_GENERATED && source != PlayBroadcastSetupCoverBottomSheet.TAB_AUTO_GENERATED) {
            toaster.showToaster(getString(R.string.play_setup_cover_upload_image_toaster))
        } else if (viewModel.uploadedCoverSource == PlayBroadcastSetupCoverBottomSheet.TAB_UPLOAD_IMAGE && source != PlayBroadcastSetupCoverBottomSheet.TAB_UPLOAD_IMAGE) {
            toaster.showToaster(getString(R.string.play_setup_cover_auto_generated_toaster))
        }
        viewModel.submitAction(PlayShortsAction.SetCoverUploadedSource(source))
    }

    override fun onDeleteAutoGeneratedCover() {
        viewModel.submitAction(PlayShortsAction.UpdateCover)
    }

    companion object {
        private const val TAG = "PlayShortsPreparationFragment"
        private const val PAGE_NAME = "shorts"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): PlayShortsPreparationFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? PlayShortsPreparationFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                PlayShortsPreparationFragment::class.java.name
            ) as PlayShortsPreparationFragment
        }
    }
}
