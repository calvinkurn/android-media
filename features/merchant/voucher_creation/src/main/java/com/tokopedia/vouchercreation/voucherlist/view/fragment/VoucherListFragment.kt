package com.tokopedia.vouchercreation.voucherlist.view.fragment

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.getBooleanArgs
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.voucherlist.model.*
import com.tokopedia.vouchercreation.voucherlist.view.adapter.factory.VoucherListAdapterFactoryImpl
import com.tokopedia.vouchercreation.voucherlist.view.viewholder.MenuViewHolder
import com.tokopedia.vouchercreation.voucherlist.view.viewholder.VoucherViewHolder
import com.tokopedia.vouchercreation.voucherlist.view.viewmodel.VoucherListViewModel
import com.tokopedia.vouchercreation.voucherlist.view.widget.filterbottomsheet.FilterBottomSheet
import com.tokopedia.vouchercreation.voucherlist.view.widget.VoucherListBottomSheet
import com.tokopedia.vouchercreation.voucherlist.view.widget.headerchips.ChipType
import com.tokopedia.vouchercreation.voucherlist.view.widget.sortbottomsheet.SortBottomSheet
import kotlinx.android.synthetic.main.fragment_mvc_voucher_list.view.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 17/04/20
 */

class VoucherListFragment : BaseListFragment<BaseVoucherListUiModel, VoucherListAdapterFactoryImpl>(),
        VoucherViewHolder.Listener, MenuViewHolder.Listener {

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

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val mViewModel: VoucherListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(VoucherListViewModel::class.java)
    }

    private val isActiveVoucher by lazy { getBooleanArgs(KEY_IS_ACTIVE_VOUCHER, true) }

    private val sortItems: List<SortUiModel> by lazy {
        return@lazy if (context == null) {
            emptyList()
        } else {
            SortBottomSheet.getMvcSortItems(requireContext())
        }
    }
    private val filterItems: List<BaseFilterUiModel> by lazy {
        return@lazy if (context == null) {
            emptyList()
        } else {
            FilterBottomSheet.getMvcFilterItems(requireContext())
        }
    }

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
        return VoucherListAdapterFactoryImpl(this, isActiveVoucher)
    }

    override fun getScreenName(): String = VoucherListFragment::class.java.simpleName

    override fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onItemClicked(t: BaseVoucherListUiModel?) {

    }

    override fun loadData(page: Int) {

    }

    override fun onMoreClickListener(voucher: VoucherUiModel) {
        val parent = (view as? ViewGroup) ?: return
        VoucherListBottomSheet(parent, this)
                .show(voucher, childFragmentManager)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> activity?.finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMenuClickListener(menu: BottomSheetMenuUiModel) {
        dismissBottomSheet()
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

    private fun setOnChipListener(chip: HeaderChipUiModel) {
        when (chip.type) {
            ChipType.CHIP_SORT -> showSortBottomSheet()
            ChipType.CHIP_FILTER -> showFilterBottomSheet()
        }
    }

    private fun showSortBottomSheet() {
        val parent = view as? ViewGroup ?: return
        SortBottomSheet(parent)
                .setOnApplySortListener {

                }
                .show(childFragmentManager, sortItems)
    }

    private fun showFilterBottomSheet() {
        val parent = view as? ViewGroup ?: return
        FilterBottomSheet(parent)
                .setOnApplyClickListener {

                }
                .show(childFragmentManager, filterItems)
    }

    private fun showDummyData() {
        renderList(getDummyData())
    }

    private fun getDummyData(): List<VoucherUiModel> {
        val list = mutableListOf<VoucherUiModel>()
        repeat(10) {
            list.add(VoucherUiModel("Voucher Hura Nyoba Doang", it % 2 == 0))
        }
        return list
    }

    private fun dismissBottomSheet() {
        val bottomSheet = childFragmentManager.findFragmentByTag(VoucherListBottomSheet.TAG)
        if (bottomSheet is VoucherListBottomSheet) {
            bottomSheet.dismiss()
        }
    }
}