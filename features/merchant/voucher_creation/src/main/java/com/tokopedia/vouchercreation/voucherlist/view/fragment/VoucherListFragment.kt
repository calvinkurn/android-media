package com.tokopedia.vouchercreation.voucherlist.view.fragment

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.bottmsheet.StopVoucherDialog
import com.tokopedia.vouchercreation.common.bottmsheet.downloadvoucher.DownloadVoucherBottomSheet
import com.tokopedia.vouchercreation.common.bottmsheet.voucherperiodbottomsheet.VoucherPeriodBottomSheet
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.exception.VoucherCancellationException
import com.tokopedia.vouchercreation.common.utils.SharingUtil
import com.tokopedia.vouchercreation.common.utils.SocmedPackage
import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherTargetType
import com.tokopedia.vouchercreation.create.view.activity.CreateMerchantVoucherStepsActivity
import com.tokopedia.vouchercreation.detail.view.activity.VoucherDetailActivity
import com.tokopedia.vouchercreation.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.voucherlist.domain.model.VoucherSort
import com.tokopedia.vouchercreation.voucherlist.model.ui.*
import com.tokopedia.vouchercreation.voucherlist.model.ui.BaseHeaderChipUiModel.HeaderChip
import com.tokopedia.vouchercreation.voucherlist.model.ui.BaseHeaderChipUiModel.ResetChip
import com.tokopedia.vouchercreation.voucherlist.model.ui.MoreMenuUiModel.*
import com.tokopedia.vouchercreation.voucherlist.view.adapter.factory.VoucherListAdapterFactoryImpl
import com.tokopedia.vouchercreation.voucherlist.view.viewholder.VoucherViewHolder
import com.tokopedia.vouchercreation.voucherlist.view.viewmodel.VoucherListViewModel
import com.tokopedia.vouchercreation.voucherlist.view.widget.CancelVoucherDialog
import com.tokopedia.vouchercreation.voucherlist.view.widget.EditQuotaBottomSheet
import com.tokopedia.vouchercreation.voucherlist.view.widget.MoreMenuBottomSheet
import com.tokopedia.vouchercreation.voucherlist.view.widget.SuccessCreateBottomSheet
import com.tokopedia.vouchercreation.voucherlist.view.widget.filterbottomsheet.FilterBottomSheet
import com.tokopedia.vouchercreation.voucherlist.view.widget.filterbottomsheet.FilterBy
import com.tokopedia.vouchercreation.voucherlist.view.widget.headerchips.ChipType
import com.tokopedia.vouchercreation.voucherlist.view.widget.sharebottomsheet.ShareVoucherBottomSheet
import com.tokopedia.vouchercreation.voucherlist.view.widget.sharebottomsheet.SocmedType
import com.tokopedia.vouchercreation.voucherlist.view.widget.sortbottomsheet.SortBottomSheet
import com.tokopedia.vouchercreation.voucherlist.view.widget.sortbottomsheet.SortBy
import kotlinx.android.synthetic.main.fragment_mvc_voucher_list.*
import kotlinx.android.synthetic.main.fragment_mvc_voucher_list.view.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 17/04/20
 */

class VoucherListFragment : BaseListFragment<Visitable<*>, VoucherListAdapterFactoryImpl>(),
        VoucherViewHolder.Listener {

    companion object {
        private const val KEY_IS_ACTIVE_VOUCHER = "is_active_voucher"
        private val MENU_VOUCHER_ACTIVE_ID = R.id.menuMvcShowVoucherActive
        private val MENU_VOUCHER_HISTORY_ID = R.id.menuMvcShowVoucherHistory

        private const val COPY_PROMO_CODE_LABEL = "list_promo_code"

        const val IS_SUCCESS_VOUCHER = "is_success"
        const val VOUCHER_ID_KEY = "voucher_id"

        fun newInstance(isActiveVoucher: Boolean): VoucherListFragment {
            return VoucherListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(KEY_IS_ACTIVE_VOUCHER, isActiveVoucher)
                }
            }
        }
    }

    private var fragmentListener: Listener? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val mViewModel: VoucherListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(VoucherListViewModel::class.java)
    }
    private val moreBottomSheet: MoreMenuBottomSheet? by lazy {
        val parent = view as? ViewGroup ?: return@lazy null
        return@lazy MoreMenuBottomSheet(parent)
    }
    private val sortBottomSheet: SortBottomSheet? by lazy {
        val parent = view as? ViewGroup ?: return@lazy null
        return@lazy SortBottomSheet(parent)
    }
    private val filterBottomSheet: FilterBottomSheet? by lazy {
        val parent = view as? ViewGroup ?: return@lazy null
        return@lazy FilterBottomSheet(parent)
    }

    private val sortItems: MutableList<SortUiModel> by lazy {
        val ctx = context ?: return@lazy mutableListOf<SortUiModel>()
        return@lazy SortBottomSheet.getMvcSortItems(ctx)
    }
    private val filterItems: MutableList<BaseFilterUiModel> by lazy {
        val ctx = context ?: return@lazy mutableListOf<BaseFilterUiModel>()
        return@lazy FilterBottomSheet.getMvcFilterItems(ctx)
    }

    private val isActiveVoucher by lazy { getBooleanArgs(KEY_IS_ACTIVE_VOUCHER, true) }

    private val isNeedToShowSuccessDialog by lazy { getBooleanArgs(IS_SUCCESS_VOUCHER, false) }

    private val successVoucherId by lazy { getIntArgs(VOUCHER_ID_KEY, 0) }

    private var isToolbarAlreadyLoaded = false

    private var shopBasicData: ShopBasicDataResult? = null

    @VoucherTypeConst
    private var voucherType: Int? = null
    private var voucherTarget: List<Int>? = null
    @VoucherSort
    private var voucherSort: String? = null
    private var isInverted: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mvc_voucher_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        if (successVoucherId != 0 && isNeedToShowSuccessDialog) {
            showSuccessCreateBottomSheet(successVoucherId)
        }

        setupView()
        observeVoucherList()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_mvc_voucher_active_list, menu)
        if (isActiveVoucher) {
            menu.removeItem(MENU_VOUCHER_ACTIVE_ID)
        } else {
            menu.removeItem(MENU_VOUCHER_HISTORY_ID)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rvVoucherList

    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipeMvcList

    override fun onSwipeRefresh() {
        clearAllData()
        super.onSwipeRefresh()
    }

    override fun getAdapterTypeFactory(): VoucherListAdapterFactoryImpl {
        return VoucherListAdapterFactoryImpl(this)
    }

    override fun getScreenName(): String = VoucherListFragment::class.java.simpleName

    override fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun hasInitialSwipeRefresh(): Boolean = true

    override fun onItemClicked(t: Visitable<*>?) {

    }

    override fun loadData(page: Int) {
        if (!isToolbarAlreadyLoaded) {
            view?.run {
                searchBarMvc.isVisible = false
                headerChipMvc.isVisible = false
            }
            renderList(listOf(LoadingStateUiModel(isActiveVoucher)))
        }
        if (isActiveVoucher) {
            mViewModel.getActiveVoucherList(shopBasicData == null)
        } else {
            mViewModel.getVoucherListHistory(voucherType, voucherTarget, voucherSort, page, isInverted)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> activity?.onBackPressed()
            R.id.menuMvcShowVoucherActive -> {
                fragmentListener?.switchFragment(true)
            }
            R.id.menuMvcShowVoucherHistory -> {
                fragmentListener?.switchFragment(false)
            }
            R.id.menuMvcAddVoucher -> {
                RouteManager.route(context, ApplinkConstInternalSellerapp.CREATE_VOUCHER)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMoreMenuClickListener(voucher: VoucherUiModel) {
        moreBottomSheet?.let {
            it.setOnModeClickListener(voucher) { menu ->
                onMoreMenuItemClickListener(menu, voucher)
            }
            it.show(isActiveVoucher, childFragmentManager)
        }
    }

    override fun onVoucherClickListener(voucherId: Int) {
        context?.run {
            startActivity(
                    VoucherDetailActivity.createDetailIntent(this, VoucherDetailActivity.DETAIL_PAGE)
                            .putExtra(VoucherDetailActivity.VOUCHER_ID, voucherId))
        }
    }

    override fun onShareClickListener(voucher: VoucherUiModel) {
        showShareBottomSheet(voucher)
    }

    override fun onEditQuotaClickListener(voucher: VoucherUiModel) {
        showEditQuotaBottomSheet(voucher)
    }

    private fun onMoreMenuItemClickListener(menu: MoreMenuUiModel, voucher: VoucherUiModel) {
        dismissBottomSheet<MoreMenuBottomSheet>(MoreMenuBottomSheet.TAG)
        when (menu) {
            is EditQuota -> showEditQuotaBottomSheet(voucher)
            is ViewDetail -> viewVoucherDetail(voucher.id)
            is ShareVoucher -> showShareBottomSheet(voucher)
            is EditPeriod -> showEditPeriodBottomSheet(voucher)
            is DownloadVoucher -> showDownloadBottomSheet(voucher)
            is CancelVoucher -> showCancelVoucherDialog(voucher)
            is StopVoucher -> showStopVoucherDialog(voucher)
            is Duplicate -> duplicateVoucher(voucher)
        }
    }

    override fun onDuplicateClickListener(voucher: VoucherUiModel) {
        duplicateVoucher(voucher)
    }

    override fun onErrorTryAgain() {
        loadData(1)
    }

    private fun duplicateVoucher(voucher: VoucherUiModel) {
        activity?.let {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalSellerapp.CREATE_VOUCHER).apply {
                putExtra(CreateMerchantVoucherStepsActivity.DUPLICATE_VOUCHER, voucher)
                putExtra(CreateMerchantVoucherStepsActivity.IS_DUPLICATE, true)
            }
            startActivity(intent)
        }
    }

    private fun viewVoucherDetail(voucherId: Int) {
        activity?.let {
            startActivity(
                    VoucherDetailActivity.createDetailIntent(it, VoucherDetailActivity.DETAIL_PAGE)
                            .putExtra(VoucherDetailActivity.VOUCHER_ID, voucherId))
        }
    }

    private fun showSuccessCreateBottomSheet(voucherId: Int) {
        mViewModel.getSuccessCreatedVoucher(voucherId)
    }

    private fun showEditPeriodBottomSheet(voucher: VoucherUiModel) {
        if (!isAdded) return
        val parent = view as? ViewGroup ?: return
        VoucherPeriodBottomSheet.createInstance(parent, voucher)
                .setOnSuccessClickListener {
                    onSuccessUpdateVoucherPeriod()
                }
                .setOnFailClickListener { errorMessage ->
                    view?.run {
                        Toaster.make(this,
                                errorMessage,
                                Snackbar.LENGTH_SHORT,
                                Toaster.TYPE_ERROR)
                    }
                }
                .show(childFragmentManager)
    }

    private fun showShareBottomSheet(voucher: VoucherUiModel) {
        if (!isAdded) return
        val parent = view as? ViewGroup ?: return
        ShareVoucherBottomSheet(parent)
                .setOnItemClickListener { socmedType ->
                    context?.run {
                        shopBasicData?.let { shopData ->
                            val shareUrl = "${TokopediaUrl.getInstance().WEB}${shopData.shopDomain}"
                            val shareMessage =
                                    if (voucher.isPublic) {
                                        String.format(
                                                getString(R.string.mvc_share_message_public).toBlankOrString(),
                                                voucher.typeFormatted,
                                                shopData.shopName,
                                                shareUrl)
                                    } else {
                                        String.format(
                                                getString(R.string.mvc_share_message_private).toBlankOrString(),
                                                voucher.typeFormatted,
                                                voucher.code,
                                                shopData.shopName,
                                                shareUrl)
                                    }
                            when(socmedType) {
                                SocmedType.COPY_LINK -> {
                                    SharingUtil.copyTextToClipboard(this, COPY_PROMO_CODE_LABEL, shareMessage)
                                }
                                SocmedType.INSTAGRAM -> {
                                    SharingUtil.shareToSocialMedia(SocmedPackage.INSTAGRAM, this, voucher.imageSquare)
                                }
                                SocmedType.FACEBOOK_MESSENGER -> {
                                    SharingUtil.shareToSocialMedia(SocmedPackage.MESSENGER, this, voucher.imageSquare, shareMessage)
                                }
                                SocmedType.WHATSAPP -> {
                                    SharingUtil.shareToSocialMedia(SocmedPackage.WHATSAPP, this, voucher.imageSquare, shareMessage)
                                }
                                SocmedType.LINE -> {
                                    SharingUtil.shareToSocialMedia(SocmedPackage.LINE, this, voucher.imageSquare, shareMessage)
                                }
                                SocmedType.LAINNYA -> {
                                    SharingUtil.otherShare(this, shareMessage)
                                }
                            }
                        }
                    }
                }
                .show(childFragmentManager)
    }

    private fun showDownloadBottomSheet(voucher: VoucherUiModel) {
        if (!isAdded) return
        val parent = view as? ViewGroup ?: return
        DownloadVoucherBottomSheet(parent, voucher.image, voucher.imageSquare)
                .setOnDownloadClickListener {

                }
                .show(childFragmentManager)
    }

    private fun showStopVoucherDialog(voucher: VoucherUiModel) {
        StopVoucherDialog(context ?: return)
                .setOnPrimaryClickListener {
                    mViewModel.cancelVoucher(voucher.id, false)
                }
                .show(voucher)
    }

    private fun showCancelVoucherDialog(voucher: VoucherUiModel) {
        CancelVoucherDialog(context ?: return)
                .setOnPrimaryClickListener {
                    mViewModel.cancelVoucher(voucher.id, true)
                }
                .show(voucher)
    }

    private fun showEditQuotaBottomSheet(voucher: VoucherUiModel) {
        if (!isAdded) return
        val parent = view as? ViewGroup ?: return
        EditQuotaBottomSheet.createInstance(parent, voucher)
                .setOnSuccessUpdateVoucher {
                    view?.run {
                        Toaster.make(this,
                                context?.getString(R.string.mvc_quota_success).toBlankOrString(),
                                Toaster.LENGTH_SHORT,
                                Toaster.TYPE_NORMAL,
                                context?.getString(R.string.mvc_oke).toBlankOrString())
                    }
                    loadData(1)
                }
                .setOnFailUpdateVoucher { errorMessage ->
                    view?.run {
                        Toaster.make(this,
                                errorMessage,
                                Toaster.LENGTH_SHORT,
                                Toaster.TYPE_ERROR)
                    }
                }.show(childFragmentManager)
    }

    private fun setupView() = view?.run {
        setupActionBar()
        setupRecyclerViewVoucherList()

        headerChipMvc.init {
            setOnChipListener(it)
        }
    }

    private fun setupRecyclerViewVoucherList() {
        val rvDirection = -1
        view?.rvVoucherList?.run {
            addItemDecoration(getMvcItemDecoration())
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val status = recyclerView.canScrollVertically(rvDirection)
                    showAppBarElevation(status)
                }
            })
        }
    }

    private fun showAppBarElevation(isShown: Boolean) = view?.run {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val elevation: Float = if (isShown) context.dpToPx(4) else 0f
            appBarMvc?.elevation = elevation
        }
    }

    private fun setupActionBar() = view?.run {
        (activity as? AppCompatActivity)?.let { activity ->
            activity.setSupportActionBar(toolbarMvcList)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

            val title = if (isActiveVoucher) context.getString(R.string.mvc_voucher_active) else context.getString(R.string.mvc_voucher_history)
            activity.supportActionBar?.title = title
        }
        showAppBarElevation(false)
    }

    private fun setupSearchBar() {
        searchBarMvc?.run {
            searchBarTextField.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    clearAllData()
                    val keyword = searchBarTextField.text.toString()
                    if (keyword.isNotEmpty()) {
                        mViewModel.setSearchKeyword(keyword)
                    } else {
                        loadData(1)
                    }

                    return@setOnEditorActionListener true
                }
                false
            }
        }
    }

    private fun getMvcItemDecoration() = object : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            val position = parent.getChildAdapterPosition(view)
            if (position == 0) {
                outRect.top = view.resources.getDimensionPixelSize(R.dimen.mvc_dimen_12dp)
            }
        }
    }

    private fun setOnChipListener(chip: BaseHeaderChipUiModel) {
        when (chip) {
            is HeaderChip -> {
                when (chip.type) {
                    ChipType.CHIP_SORT -> showSortBottomSheet()
                    ChipType.CHIP_FILTER -> showFilterBottomSheet()
                }
            }
            is ResetChip -> setOnResetClick()
        }
    }

    private fun setOnResetClick() {
        sortItems.clear()
        sortItems.addAll(SortBottomSheet.getMvcSortItems(requireContext()))
        filterItems.clear()
        filterItems.addAll(FilterBottomSheet.getMvcFilterItems(requireContext()))

        view?.headerChipMvc?.resetFilter()

        resetFetchValues()
    }

    private fun resetFetchValues() {
        voucherTarget = null
        voucherSort = null
        voucherType = null
        isInverted = false
        loadData(1)
    }

    private fun showSortBottomSheet() {
        if (!isAdded) return
        sortBottomSheet
                ?.setOnApplySortListener {
                    applySort()
                }
                ?.setOnCancelApply { previousSortItems ->
                    sortItems.clear()
                    sortItems.addAll(previousSortItems)
                }
                ?.show(childFragmentManager, sortItems)
    }

    private fun showFilterBottomSheet() {
        if (!isAdded) return
        filterBottomSheet
                ?.setOnApplyClickListener {
                    applyFilter()
                }
                ?.setCancelApplyFilter { previousFilterItems ->
                    filterItems.clear()
                    filterItems.addAll(previousFilterItems)
                }
                ?.show(childFragmentManager, filterItems)
    }

    private fun applySort() {
        clearAllData()

        voucherSort = VoucherSort.FINISH_TIME

        val activeSort = sortItems.first { it.isSelected }
        headerChipMvc?.setActiveSort(activeSort)
        isInverted = activeSort.key == SortBy.OLDEST_DONE_DATE

        loadData(1)
    }

    private fun applyFilter() {
        clearAllData()

        val activeFilterList = filterItems.filterIsInstance<BaseFilterUiModel.FilterItem>().filter { it.isSelected }
        val canResetFilter = activeFilterList.isNotEmpty()
        if (canResetFilter) {
            view?.headerChipMvc?.showResetButton()
        }
        headerChipMvc?.setActiveFilter(activeFilterList)

        val voucherTypeFilter = activeFilterList.filter { it.key == FilterBy.CASHBACK || it.key == FilterBy.FREE_SHIPPING }
        if (voucherTypeFilter.size == 1) {
            voucherTypeFilter.first { it.key == FilterBy.CASHBACK || it.key == FilterBy.FREE_SHIPPING }.key.let { type ->
                voucherType =
                        when(type) {
                            FilterBy.FREE_SHIPPING -> VoucherTypeConst.FREE_ONGKIR
                            FilterBy.CASHBACK -> VoucherTypeConst.CASHBACK
                            else -> VoucherTypeConst.DISCOUNT
                        }
            }
        } else {
            voucherType = null
        }

        val voucherTargetFilter = activeFilterList.filter { it.key == FilterBy.PUBLIC || it.key == FilterBy.SPECIAL }
        voucherTarget =
                if (voucherTargetFilter.size in 1..2) {
                    voucherTargetFilter.map {
                        when(it.key) {
                            FilterBy.PUBLIC -> VoucherTargetType.PUBLIC
                            FilterBy.SPECIAL -> VoucherTargetType.PRIVATE
                            else -> VoucherTargetType.PUBLIC
                        }
                    }
                } else {
                    null
                }

        loadData(1)
    }

    private inline fun <reified T : BottomSheetUnify> dismissBottomSheet(tag: String) {
        val bottomSheet = childFragmentManager.findFragmentByTag(tag)
        if (bottomSheet is T) {
            bottomSheet.dismiss()
        }
    }

    private fun setOnSuccessGetVoucherList(vouchers: List<VoucherUiModel>) {
        if (isToolbarAlreadyLoaded && !isActiveVoucher) {
            renderList(vouchers, vouchers.isNotEmpty())
            if (adapter.data.isEmpty()) {
                renderList(listOf(NoResultStateUiModel))
            }
        } else {
            clearAllData()
            if (vouchers.isEmpty()) {
                renderList(listOf(getEmptyStateUiModel()))
            } else {
                view?.run {
                    searchBarMvc.isVisible = !isActiveVoucher
                    headerChipMvc.isVisible = !isActiveVoucher
                    isToolbarAlreadyLoaded = true
                    setupSearchBar()
                }
                renderList(vouchers, vouchers.isNotEmpty() && !isActiveVoucher)
            }
        }
    }

    private fun setOnErrorGetVoucherList(throwable: Throwable) {
        throwable.printStackTrace()
        clearAllData()
        renderList(listOf(ErrorStateUiModel))
    }

    private fun onSuccessUpdateVoucherPeriod() {
        view?.run {
            Toaster.make(this,
                    context?.getString(R.string.mvc_success_update_period).toBlankOrString(),
                    Snackbar.LENGTH_SHORT,
                    Toaster.TYPE_NORMAL,
                    context?.getString(R.string.mvc_oke).toBlankOrString())
        }
        loadData(1)
    }

    private fun observeVoucherList() {
        viewLifecycleOwner.run {
            observe(mViewModel.voucherList) {
                when (it) {
                    is Success -> setOnSuccessGetVoucherList(it.data)
                    is Fail -> setOnErrorGetVoucherList(it.throwable)
                }
            }
            observe(mViewModel.localVoucherListLiveData) { result ->
                when(result) {
                    is Success -> setOnSuccessGetVoucherList(result.data)
                    is Fail -> setOnErrorGetVoucherList(result.throwable)
                }
            }
            observe(mViewModel.cancelVoucherResponseLiveData) { result ->
                when(result) {
                    is Success -> {
                        val voucherId = result.data
                        loadData(1)
                        showCancellationSuccessToaster(true, voucherId)
                    }
                    is Fail -> {
                        if (result.throwable is VoucherCancellationException) {
                            showCancellationFailToaster(true, (result.throwable as? VoucherCancellationException)?.voucherId.toZeroIfNull())
                        }
                    }
                }
            }
            observe(mViewModel.stopVoucherResponseLiveData) { result ->
                when(result) {
                    is Success -> {
                        val voucherId = result.data
                        loadData(1)
                        showCancellationSuccessToaster(false, voucherId)
                    }
                    is Fail -> {
                        if (result.throwable is VoucherCancellationException) {
                            showCancellationFailToaster(false, (result.throwable as? VoucherCancellationException)?.voucherId.toZeroIfNull())
                        }
                    }
                }
            }
            observe(mViewModel.shopBasicLiveData) { result ->
                if (result is Success) {
                    shopBasicData = result.data
                }
            }
            observe(mViewModel.successVoucherLiveData) { result ->
                if (result is Success) {
                    result.data.let { uiModel ->
                        if (uiModel.isPublic) {
                            view?.run {
                                Toaster.make(this,
                                        context?.getString(R.string.mvc_success_toaster).toBlankOrString(),
                                        Toaster.LENGTH_LONG,
                                        Toaster.TYPE_NORMAL,
                                        context?.getString(R.string.mvc_oke).toBlankOrString(),
                                        View.OnClickListener {})
                            }
                        } else {
                            val parent = view as? ViewGroup ?: return@observe
                            SuccessCreateBottomSheet.createInstance(parent, uiModel)
                                    .setOnShareClickListener {
                                        showShareBottomSheet(uiModel)
                                    }
                                    .setOnDownloadClickListener {
                                        showDownloadBottomSheet(uiModel)
                                    }
                                    .show(childFragmentManager)
                        }
                    }
                }
            }
        }
    }

    private fun onSeeHistoryClicked() {
        fragmentListener?.switchFragment(false)
    }

    private fun getEmptyStateUiModel() = EmptyStateUiModel(isActiveVoucher, ::onSeeHistoryClicked)

    private fun showCancellationSuccessToaster(isCancel: Boolean,
                                               voucherId: Int) {
        val successMessageRes =
                if (isCancel) {
                    R.string.mvc_cancel_success
                } else {
                    R.string.mvc_stop_success
                }
        val successMessage = context?.getString(successMessageRes).toBlankOrString()
        val actionText = context?.getString(R.string.mvc_lihat).toBlankOrString()

        view?.run {
            Toaster.make(this,
                    successMessage,
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_NORMAL,
                    actionText,
                    View.OnClickListener {
                        viewVoucherDetail(voucherId)
                    })
        }
    }

    private fun showCancellationFailToaster(isCancel: Boolean,
                                            voucherId: Int) {
        val errorMessageRes =
                if (isCancel) {
                    R.string.mvc_cancel_fail
                } else {
                    R.string.mvc_stop_fail
                }
        val errorMessage = context?.getString(errorMessageRes).toBlankOrString()
        val actionText = context?.getString(R.string.mvc_retry).toBlankOrString()

        view?.run {
            Toaster.make(this,
                    errorMessage,
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_ERROR,
                    actionText,
                    View.OnClickListener {
                        mViewModel.cancelVoucher(voucherId, isCancel)
                    })
        }
    }

    fun setFragmentListener(listener: Listener) {
        this.fragmentListener = listener
    }

    interface Listener {
        fun switchFragment(isActiveVoucher: Boolean)
    }
}