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
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.bottmsheet.StopVoucherDialog
import com.tokopedia.vouchercreation.common.bottmsheet.downloadvoucher.DownloadVoucherBottomSheet
import com.tokopedia.vouchercreation.common.bottmsheet.voucherperiodbottomsheet.VoucherPeriodBottomSheet
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherTargetType
import com.tokopedia.vouchercreation.detail.view.activity.VoucherDetailActivity
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
import com.tokopedia.vouchercreation.voucherlist.view.widget.filterbottomsheet.FilterBottomSheet
import com.tokopedia.vouchercreation.voucherlist.view.widget.filterbottomsheet.FilterBy
import com.tokopedia.vouchercreation.voucherlist.view.widget.headerchips.ChipType
import com.tokopedia.vouchercreation.voucherlist.view.widget.sharebottomsheet.ShareVoucherBottomSheet
import com.tokopedia.vouchercreation.voucherlist.view.widget.sortbottomsheet.SortBottomSheet
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
    lateinit var viewModelFactory: ViewModelFactory

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

    private var isToolbarAlreadyLoaded = false

    @VoucherTypeConst
    private var voucherType: Int? = null
    private var voucherTarget: List<Int>? = null
    @VoucherSort
    private var voucherSort: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mvc_voucher_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

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
            mViewModel.getActiveVoucherList()
        } else {
            mViewModel.getVoucherListHistory(voucherType, voucherTarget, voucherSort, page)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> activity?.finish()
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

    private fun duplicateVoucher(voucher: VoucherUiModel) {
        activity?.let {
            startActivity(VoucherDetailActivity.createDuplicateIntent(it, VoucherDetailActivity.DUPLICATE_PAGE))
        }
    }

    private fun viewVoucherDetail(voucherId: Int) {
        activity?.let {
            startActivity(
                    VoucherDetailActivity.createDetailIntent(it, VoucherDetailActivity.DETAIL_PAGE)
                            .putExtra(VoucherDetailActivity.VOUCHER_ID, voucherId))
        }
    }

    private fun showEditPeriodBottomSheet(voucher: VoucherUiModel) {
        if (!isAdded) return
        val parent = view as? ViewGroup ?: return
        VoucherPeriodBottomSheet.createInstance(parent, voucher, ::onSuccessUpdateVoucherPeriod)
                .setOnSaveClickListener {

                }
                .show(childFragmentManager)
    }

    private fun showShareBottomSheet(voucher: VoucherUiModel) {
        if (!isAdded) return
        val parent = view as? ViewGroup ?: return
        ShareVoucherBottomSheet(parent)
                .setOnItemClickListener {

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

                }
                .show(voucher)
    }

    private fun showCancelVoucherDialog(voucher: VoucherUiModel) {
        CancelVoucherDialog(context ?: return)
                .setOnPrimaryClickListener {

                }
                .show(voucher)
    }

    private fun showEditQuotaBottomSheet(voucher: VoucherUiModel) {
        if (!isAdded) return
        val parent = view as? ViewGroup ?: return
        EditQuotaBottomSheet(parent, voucher).show(childFragmentManager)
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

        voucherTarget = null
        voucherSort = null
        voucherType = null

        view?.headerChipMvc?.resetFilter()
    }

    private fun showSortBottomSheet() {
        if (!isAdded) return
        sortBottomSheet
                ?.setOnApplySortListener {
                    applyFilter()
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
        if (isToolbarAlreadyLoaded) {
            renderList(vouchers, vouchers.isNotEmpty())
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
                renderList(vouchers, vouchers.isNotEmpty())
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
    }

    private fun observeVoucherList() {
        observe(mViewModel.voucherList) {
            when (it) {
                is Success -> setOnSuccessGetVoucherList(it.data)
                is Fail -> setOnErrorGetVoucherList(it.throwable)
            }
        }
    }

    private fun onSeeHistoryClicked() {
        fragmentListener?.switchFragment(false)
    }

    private fun getEmptyStateUiModel() = EmptyStateUiModel(isActiveVoucher, ::onSeeHistoryClicked)

    fun setFragmentListener(listener: Listener) {
        this.fragmentListener = listener
    }

    interface Listener {
        fun switchFragment(isActiveVoucher: Boolean)
    }
}