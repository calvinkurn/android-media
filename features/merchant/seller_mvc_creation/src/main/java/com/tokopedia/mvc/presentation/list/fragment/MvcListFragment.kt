package com.tokopedia.mvc.presentation.list.fragment

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.delegates.HasPaginatedList
import com.tokopedia.campaign.delegates.HasPaginatedListImpl
import com.tokopedia.campaign.utils.extension.showToasterError
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcFragmentMvcListBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.presentation.bottomsheet.EduCenterBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.FilterVoucherBottomSheet
import com.tokopedia.mvc.presentation.list.adapter.VoucherAdapterListener
import com.tokopedia.mvc.presentation.list.adapter.VouchersAdapter
import com.tokopedia.mvc.presentation.list.constant.MvcListConstant.INITIAL_PAGE
import com.tokopedia.mvc.presentation.list.constant.MvcListConstant.PAGE_SIZE
import com.tokopedia.mvc.presentation.list.viewmodel.MvcListViewModel
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class MvcListFragment: BaseDaggerFragment(), HasPaginatedList by HasPaginatedListImpl(),
    VoucherAdapterListener {

    private val filterList = ArrayList<SortFilterItem>()
    private val filterItem by lazy { SortFilterItem("AAA") }
    private var binding by autoClearedNullable<SmvcFragmentMvcListBinding>()

    @Inject
    lateinit var viewModel: MvcListViewModel

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerMerchantVoucherCreationComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmvcFragmentMvcListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyUnifyBackgroundColor()
        binding?.setupView()
        setupObservables()
    }

    override fun onVoucherListMoreMenuClicked(voucher: Voucher) {
        println("more menu")
    }

    override fun onVoucherListCopyCodeClicked(voucher: Voucher) {
        println("copy")
    }

    override fun onVoucherListMultiPeriodClicked(voucher: Voucher) {
        println("multiperiod")
    }

    override fun onVoucherListClicked(voucher: Voucher) {
        println("card")
    }

    private fun setupObservables() {
        viewModel.voucherList.observe(viewLifecycleOwner) { vouchers ->
            val adapter = binding?.rvVoucher?.adapter as? VouchersAdapter
            adapter?.addDataList(vouchers)
            notifyLoadResult(vouchers.size >= PAGE_SIZE)
        }
        viewModel.error.observe(viewLifecycleOwner) {
            view?.showToasterError(it)
        }
    }

    private fun SmvcFragmentMvcListBinding.setupView() {
        header.setupHeader()
        searchBar.setupSearchBar()
        rvVoucher.setupRvVoucher()


        filterList.add(filterItem)
        sortFilter.apply {
            addItem(filterList)
            parentListener = {
                filterItem.selectedItem = arrayListOf()
                FilterVoucherBottomSheet().show(childFragmentManager, "")
            }
            dismissListener = parentListener
        }

        filterItem.listener = {  }
        filterItem.refChipUnify.setChevronClickListener {  }
    }

    private fun HeaderUnify.setupHeader() {
        val colorIcon = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950)
        title = context.getString(R.string.smvc_voucherlist_page_title)
        addRightIcon(com.tokopedia.iconunify.R.drawable.iconunify_menu_kebab_horizontal).apply {
            setColorFilter(colorIcon, PorterDuff.Mode.MULTIPLY)
            setOnClickListener {
                EduCenterBottomSheet().show(childFragmentManager, "")
            }
        }
        setNavigationOnClickListener {
            activity?.finish()
        }
    }

    private fun SearchBarUnify.setupSearchBar() {
        clearListener = { loadInitialDataList() }
        searchBarTextField.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                loadInitialDataList(textView.text.toString())
                KeyboardHandler.hideSoftKeyboard(activity)
            }
            return@setOnEditorActionListener false
        }
    }

    private fun RecyclerView.setupRvVoucher() {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = VouchersAdapter(this@MvcListFragment)
        val config = HasPaginatedList.Config(
            pageSize = PAGE_SIZE,
            onLoadNextPage = {
                // TODO: Implement loading
            }, onLoadNextPageFinished = {
                // TODO: Implement loading
            }
        )
        loadInitialDataList()
        attachPaging(this, config, ::getDataList)
    }

    private fun loadInitialDataList(keyword: String = "") {
        val adapter = binding?.rvVoucher?.adapter as? VouchersAdapter
        adapter?.clearDataList()
        viewModel.getVoucherList(keyword, INITIAL_PAGE, PAGE_SIZE)
    }

    private fun getDataList(page: Int, pageSize: Int) {
        viewModel.getVoucherList(binding?.searchBar?.searchBarTextField?.text.toString(), page, pageSize)
    }
}
