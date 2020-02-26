package com.tokopedia.thankyou_native.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.domain.OrderList
import com.tokopedia.thankyou_native.presentation.adapter.OrderAdapterTypeFactory
import com.tokopedia.thankyou_native.presentation.adapter.OrderListAdapter
import com.tokopedia.thankyou_native.presentation.viewModel.OrderDetailListMapperViewModel
import com.tokopedia.unifycomponents.bottomsheet.RoundedBottomSheetDialogFragment
import kotlinx.android.synthetic.main.thank_fragment_order_list.*
import javax.inject.Inject

class OrderDetailListFragment : RoundedBottomSheetDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var orderDetailListMapperViewModel: OrderDetailListMapperViewModel

    lateinit var adapter: OrderListAdapter

    private lateinit var orderList: ArrayList<OrderList>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            if (it.containsKey(ARG_ORDER_LIST))
                orderList = it.getParcelableArrayList(ARG_ORDER_LIST)
        }
        initViewModels()
    }

    private fun initViewModels() {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        orderDetailListMapperViewModel = viewModelProvider.get(OrderDetailListMapperViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.thank_fragment_order_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeViewModel()
        mapOrderListForAdapter()
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = OrderListAdapter(OrderAdapterTypeFactory(), arrayListOf<Visitable<*>>())
    }

    private fun observeViewModel() {
        orderDetailListMapperViewModel.adapterListMutableLiveData.observe(this, Observer {
            if (it.isNotEmpty()) {
                addItemToAdapter(it)
            } else {
                closeBottomSheet()
            }
        })
    }

    private fun addItemToAdapter(items: ArrayList<Visitable<*>>) {
        if (::adapter.isInitialized) {
            adapter.addItem(items)
            adapter.notifyDataSetChanged()
        }
    }

    private fun closeBottomSheet() {
        dismiss()
    }

    private fun mapOrderListForAdapter() {
        orderDetailListMapperViewModel.mapOrderListTOAdapterVisitable(orderList)
    }

    companion object {

        private const val ARG_ORDER_LIST = "arg_order_list"

        fun getInstance(orderList: ArrayList<OrderList>): OrderDetailListFragment = OrderDetailListFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(ARG_ORDER_LIST, orderList)
            }
        }
    }

}