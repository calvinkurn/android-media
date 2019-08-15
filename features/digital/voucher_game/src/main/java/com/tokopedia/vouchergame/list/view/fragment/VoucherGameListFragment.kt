package com.tokopedia.vouchergame.list.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchergame.R
import com.tokopedia.vouchergame.list.data.VoucherGameOperator
import com.tokopedia.vouchergame.list.di.VoucherGameListComponent
import com.tokopedia.vouchergame.list.view.adapter.VoucherGameListAdapterFactory
import com.tokopedia.vouchergame.list.view.adapter.VoucherGameListDecorator
import com.tokopedia.vouchergame.list.view.adapter.viewholder.VoucherGameListViewHolder
import com.tokopedia.vouchergame.list.view.viewmodel.VoucherGameListViewModel
import kotlinx.android.synthetic.main.fragment_voucher_game_list.*
import javax.inject.Inject

class VoucherGameListFragment: BaseSearchListFragment<VoucherGameOperator,
        VoucherGameListAdapterFactory>(),
        BaseEmptyViewHolder.Callback,
        SearchInputView.ResetListener,
        VoucherGameListViewHolder.OnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var voucherGameViewModel: VoucherGameListViewModel

    var menuId: Int = 0
    var platformId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            voucherGameViewModel = viewModelProvider.get(VoucherGameListViewModel::class.java)
        }

        arguments?.let {
            menuId = it.getInt(EXTRA_MENU_ID, 0)
            platformId = it.getInt(EXTRA_PLATFORM_ID, 0)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        voucherGameViewModel.searchVoucherGameList.observe(this, Observer {
            it.run {
                when(it) {
                    is Success -> {
                        if (it.data.isEmpty()) showEmpty()
                        else renderList(it.data)
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
    }

    private fun initView() {
        val layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        recycler_view.layoutManager = layoutManager
        recycler_view.addItemDecoration(VoucherGameListDecorator(8, resources))

        searchInputView.setResetListener(this)
    }

    override fun getAdapterTypeFactory(): VoucherGameListAdapterFactory {
        return VoucherGameListAdapterFactory(this, this)
    }

    override fun getScreenName(): String = "Voucher Game"

    override fun initInjector() {
        getComponent(VoucherGameListComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {
        voucherGameViewModel.getVoucherGameList(GraphqlHelper.loadRawString(resources, R.raw.query_voucher_game_product_list),
                voucherGameViewModel.createParams(menuId, platformId))
    }

    override fun onItemClicked(voucherGameOperator: VoucherGameOperator) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onEmptyContentItemTextClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onEmptyButtonClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSearchSubmitted(text: String?) {
        text?.run {
            if (text.isNotEmpty()) voucherGameViewModel.searchVoucherGame(text)
        }
    }

    override fun onSearchTextChanged(text: String?) { }

    override fun onSearchReset() {
        voucherGameViewModel.searchVoucherGame("")
    }

    companion object {

        const val EXTRA_MENU_ID = "EXTRA_MENU_ID"
        const val EXTRA_PLATFORM_ID = "EXTRA_PLATFORM_ID"

        fun createInstance(menuId: Int = 0, platformId: Int = 0): VoucherGameListFragment {
            return VoucherGameListFragment().also {
                it.arguments = Bundle().apply {
                    putInt(EXTRA_MENU_ID, menuId)
                    putInt(EXTRA_PLATFORM_ID, platformId)
                }
            }
        }
    }
}