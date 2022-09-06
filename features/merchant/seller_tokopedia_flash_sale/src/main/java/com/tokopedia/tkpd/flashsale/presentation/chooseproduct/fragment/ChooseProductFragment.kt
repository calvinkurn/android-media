package com.tokopedia.tkpd.flashsale.presentation.chooseproduct.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.campaign.components.adapter.ChooseProductAdapter
import com.tokopedia.campaign.components.adapter.ChooseProductDelegateAdapter
import com.tokopedia.campaign.components.adapter.CompositeAdapter
import com.tokopedia.campaign.entity.ChooseProductItem
import com.tokopedia.campaign.utils.extension.showToasterError
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsFragmentChooseProductBinding
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.constant.ChooseProductConstant.MAX_PER_PAGE
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.viewmodel.ChooseProductViewModel
import com.tokopedia.tkpd.flashsale.util.BaseSimpleListFragment
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ChooseProductFragment : BaseSimpleListFragment<CompositeAdapter, ChooseProductItem>(),
    ChooseProductDelegateAdapter.ChooseProductListener {

    @Inject
    lateinit var viewModel: ChooseProductViewModel
    private var binding by autoClearedNullable<StfsFragmentChooseProductBinding>()
    private val chooseProductAdapter: ChooseProductAdapter = ChooseProductAdapter()

    override fun getScreenName(): String = ChooseProductFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerTokopediaFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StfsFragmentChooseProductBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupHeader()
        setupSearchBar()
    }

    private fun setupSearchBar() {
        binding?.searchBar?.searchBarTextField?.apply {
            imeOptions = EditorInfo.IME_ACTION_SEARCH
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) loadInitialData()
                return@setOnEditorActionListener actionId == EditorInfo.IME_ACTION_SEARCH
            }
        }
    }

    private fun setupHeader() {
        binding?.header?.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun setupObservers() {
        viewModel.productList.observe(viewLifecycleOwner) {
            renderList(it, viewModel.hasNextPage)
        }
        viewModel.error.observe(viewLifecycleOwner) {
            showGetListError(it)
        }
    }

    override fun onChooseProductClicked(index: Int, item: ChooseProductItem, selected: Boolean) {
        println(item)
    }

    override fun onDetailClicked(index: Int) {
        println("click $index")
    }

    override fun createAdapter(): CompositeAdapter = chooseProductAdapter.getRecyclerViewAdapter()

    override fun getRecyclerView(view: View): RecyclerView? = binding?.rvBundle

    override fun getPerPage(): Int = MAX_PER_PAGE

    override fun addElementToAdapter(list: List<ChooseProductItem>) {
        chooseProductAdapter.addItems(list)
    }

    override fun loadData(page: Int, offset: Int) {
        viewModel.getProductList(page, getPerPage(), getSearchBarText())
    }

    private fun getSearchBarText(): String {
        return binding?.searchBar?.searchBarTextField?.text?.toString().orEmpty()
    }

    override fun clearAdapterData() {
        chooseProductAdapter.submit(listOf())
    }

    override fun onShowLoading() {
        chooseProductAdapter.setLoading(true)
    }

    override fun onHideLoading() {
        chooseProductAdapter.setLoading(false)
    }

    override fun onDataEmpty() {
        view?.showToasterError("Data Kosong")
    }

    override fun onGetListError(message: String) {
        view?.showToasterError(message)
    }
}