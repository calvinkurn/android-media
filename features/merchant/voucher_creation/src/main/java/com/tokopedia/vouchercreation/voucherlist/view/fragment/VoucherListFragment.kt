package com.tokopedia.vouchercreation.voucherlist.view.fragment

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.getBooleanArgs
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.voucherlist.model.*
import com.tokopedia.vouchercreation.voucherlist.model.BaseHeaderChipUiModel.HeaderChip
import com.tokopedia.vouchercreation.voucherlist.model.BaseHeaderChipUiModel.ResetChip
import com.tokopedia.vouchercreation.voucherlist.model.MoreMenuUiModel.EditQuota
import com.tokopedia.vouchercreation.voucherlist.model.MoreMenuUiModel.CancelVoucher
import com.tokopedia.vouchercreation.voucherlist.model.MoreMenuUiModel.StopVoucher
import com.tokopedia.vouchercreation.voucherlist.view.adapter.factory.VoucherListAdapterFactoryImpl
import com.tokopedia.vouchercreation.voucherlist.view.viewmodel.VoucherListViewModel
import com.tokopedia.vouchercreation.voucherlist.view.widget.CancelVoucherDialog
import com.tokopedia.vouchercreation.voucherlist.view.widget.MoreMenuBottomSheet
import com.tokopedia.vouchercreation.voucherlist.view.widget.EditQuotaBottomSheet
import com.tokopedia.vouchercreation.voucherlist.view.widget.StopVoucherDialog
import com.tokopedia.vouchercreation.voucherlist.view.widget.filterbottomsheet.FilterBottomSheet
import com.tokopedia.vouchercreation.voucherlist.view.widget.headerchips.ChipType
import com.tokopedia.vouchercreation.voucherlist.view.widget.sortbottomsheet.SortBottomSheet
import kotlinx.android.synthetic.main.fragment_mvc_voucher_list.view.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 17/04/20
 */

class VoucherListFragment : BaseListFragment<Visitable<*>, VoucherListAdapterFactoryImpl>(),
        VoucherListAdapterFactoryImpl.Listener {

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mvc_voucher_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        setupView()
        showDummyData()
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

    override fun onItemClicked(t: Visitable<*>?) {

    }

    override fun loadData(page: Int) {

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
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMoreClickListener(voucher: VoucherUiModel) {
        moreBottomSheet?.let {
            it.setOnModeClickListener(voucher) { menu ->
                onMoreMenuItemClickListener(menu, voucher)
            }
            it.show(isActiveVoucher, childFragmentManager)
        }
    }

    private fun onMoreMenuItemClickListener(menu: MoreMenuUiModel, voucher: VoucherUiModel) {
        dismissBottomSheet<MoreMenuBottomSheet>(MoreMenuBottomSheet.TAG)
        when (menu) {
            is EditQuota -> showEditQuotaBottomSheet()
            is CancelVoucher -> showCancelVoucherDialog(voucher)
            is StopVoucher -> showStopVoucherDialog(voucher)
        }
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

    private fun showEditQuotaBottomSheet() {
        val parent = view as? ViewGroup ?: return
        if (!isAdded) return
        EditQuotaBottomSheet(parent).show(childFragmentManager)
    }

    private fun setupView() = view?.run {
        setupActionBar()
        setupRecyclerViewVoucherList()

        headerChipMvc.init {
            setOnChipListener(it)
        }

        searchBarMvc.isVisible = !isActiveVoucher
        headerChipMvc.isVisible = !isActiveVoucher
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
        view?.headerChipMvc?.hideResetButton()
    }

    private fun showSortBottomSheet() {
        if (!isAdded) return
        sortBottomSheet
                ?.setOnApplySortListener {
                    showResetChip()
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
                    showResetChip()
                }
                ?.setCancelApplyFilter { previousFilterItems ->
                    filterItems.clear()
                    filterItems.addAll(previousFilterItems)
                }
                ?.show(childFragmentManager, filterItems)
    }

    private fun showResetChip() {
        val canResetFilter = filterItems.filterIsInstance<HeaderChip>().any { it.isActive }
        if (canResetFilter) {
            view?.headerChipMvc?.showResetButton()
        }
    }

    private fun showDummyData() {
        renderList(getDummyData())
        //renderList(getVoucherListShimmer())
    }

    private fun getVoucherListShimmer(): List<Visitable<*>> {
        return listOf(LoadingStateUiModel(isActiveVoucher))
    }

    private fun getDummyData(): List<Visitable<*>> {
        val list = mutableListOf<Visitable<*>>()
        /*list.add(NoResultStateUiModel)
        list.add(ErrorStateUiModel)
        list.add(EmptyStateUiModel(isActiveVoucher))*/
        repeat(10) {
            list.add(VoucherUiModel("Voucher Hura Nyoba Doang", it % 2 == 0))
        }
        return list
    }

    private inline fun <reified T : BottomSheetUnify> dismissBottomSheet(tag: String) {
        val bottomSheet = childFragmentManager.findFragmentByTag(tag)
        if (bottomSheet is T) {
            bottomSheet.dismiss()
        }
    }

    fun setFragmentListener(listener: Listener) {
        this.fragmentListener = listener
    }

    interface Listener {
        fun switchFragment(isActiveVoucher: Boolean)
    }
}