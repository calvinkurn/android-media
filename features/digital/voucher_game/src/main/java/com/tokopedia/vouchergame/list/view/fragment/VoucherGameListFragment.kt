package com.tokopedia.vouchergame.list.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common_digital.common.constant.DigitalExtraParam.EXTRA_PARAM_TELCO
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchergame.R
import com.tokopedia.vouchergame.common.view.BaseVoucherGameActivity
import com.tokopedia.vouchergame.common.view.model.VoucherGameExtraParam
import com.tokopedia.vouchergame.detail.view.activity.VoucherGameDetailActivity
import com.tokopedia.vouchergame.list.data.VoucherGameListData
import com.tokopedia.vouchergame.list.data.VoucherGameOperator
import com.tokopedia.vouchergame.list.di.VoucherGameListComponent
import com.tokopedia.vouchergame.list.view.adapter.VoucherGameListAdapterFactory
import com.tokopedia.vouchergame.list.view.adapter.VoucherGameListDecorator
import com.tokopedia.vouchergame.list.view.adapter.viewholder.VoucherGameListViewHolder
import com.tokopedia.vouchergame.list.view.viewmodel.VoucherGameListViewModel
import kotlinx.android.synthetic.main.fragment_voucher_game_list.*
import javax.inject.Inject

/**
 * Created by resakemal on 12/08/19.
 */
class VoucherGameListFragment: BaseSearchListFragment<Visitable<*>,
        VoucherGameListAdapterFactory>(),
        SearchInputView.ResetListener,
        VoucherGameListViewHolder.OnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var voucherGameViewModel: VoucherGameListViewModel

    lateinit var voucherGameExtraParam: VoucherGameExtraParam

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            voucherGameViewModel = viewModelProvider.get(VoucherGameListViewModel::class.java)
        }

        arguments?.let {
            voucherGameExtraParam = it.getParcelable(EXTRA_PARAM_TELCO)

            checkAutoSelectOperator()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        voucherGameViewModel.voucherGameList.observe(this, Observer {
            it.run {
                when(it) {
                    is Success -> {
                        renderOperators(it.data)
                    }
                    is Fail -> {
                        showGetListError(it.throwable)
                    }
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_voucher_game_list, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::voucherGameExtraParam.isInitialized) outState.putParcelable(EXTRA_PARAM_TELCO, voucherGameExtraParam)
    }

    private fun checkAutoSelectOperator() {
        if (voucherGameExtraParam.operatorId.isNotEmpty()) navigateToProductList()
    }

    private fun initView() {
        searchInputView.setResetListener(this)

        recycler_view.addItemDecoration(VoucherGameListDecorator(ITEM_DECORATOR_SIZE, resources))

//        promo_banner.setPagerAdapter(object: BannerViewPagerAdapter())
    }

    private fun togglePromoBanner(state: Boolean) {
        promo_banner.visibility = if (state) View.VISIBLE else View.GONE
        promo_banner_shimmering.visibility = if (state) View.GONE else View.VISIBLE
    }

    private fun renderOperators(data: VoucherGameListData) {
        (activity as BaseVoucherGameActivity).updateTitle(data.text)

        if (data.operators.isEmpty()) {
            adapter.clearAllElements()
            showEmpty()
        }
        else renderList(data.operators)
    }

    override fun getAdapterTypeFactory(): VoucherGameListAdapterFactory {
        return VoucherGameListAdapterFactory(this)
    }

    override fun getScreenName(): String = getString(R.string.app_label)

    override fun initInjector() {
        getComponent(VoucherGameListComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {
        voucherGameExtraParam.menuId.toIntOrNull()?.let {
            voucherGameViewModel.getVoucherGameList(GraphqlHelper.loadRawString(resources, R.raw.query_voucher_game_product_list),
                    voucherGameViewModel.createParams(it), "", true)
        }
    }

    override fun onItemClicked(item: Visitable<*>) { }

    override fun onItemClicked(operator: VoucherGameOperator) {
        voucherGameExtraParam.operatorId = operator.id.toString()
        navigateToProductList()
    }

    private fun navigateToProductList() {
        context?.run {
            val intent = VoucherGameDetailActivity.newInstance(this,
                    voucherGameExtraParam.categoryId,
                    voucherGameExtraParam.menuId,
                    voucherGameExtraParam.operatorId,
                    voucherGameExtraParam.productId)
            startActivityForResult(intent, REQUEST_VOUCHER_GAME_DETAIL)
        }
    }

    override fun hasInitialSwipeRefresh(): Boolean {
        return true
    }

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        val layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(p0: Int): Int {
                return when (adapter.getItemViewType(p0)) {
                    VoucherGameListViewHolder.LAYOUT -> 1
                    else -> 3
                }
            }
        }
        return layoutManager
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        val model = EmptyModel()
        model.title = "Data kosong"
        model.description = "Tidak ada data yang ditemukan :("
        return model
    }

    override fun onSearchSubmitted(text: String?) {
        text?.run {
            searchVoucherGame(text)
        }
    }

    override fun onSearchTextChanged(text: String?) {

    }

    override fun onSearchReset() {
        searchVoucherGame("")
    }

    private fun searchVoucherGame(query: String) {
        voucherGameViewModel.getVoucherGameList(GraphqlHelper.loadRawString(resources, R.raw.query_voucher_game_product_list),
                voucherGameViewModel.createParams(voucherGameExtraParam.menuId.toInt()), query)
    }

    companion object {

        const val ITEM_DECORATOR_SIZE = 9

        const val REQUEST_VOUCHER_GAME_DETAIL = 300

        fun newInstance(voucherGameExtraParam: VoucherGameExtraParam): Fragment {
            val fragment = VoucherGameListFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_PARAM_TELCO, voucherGameExtraParam)
            fragment.arguments = bundle
            return fragment
        }
    }
}