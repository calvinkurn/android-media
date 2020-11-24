package com.tokopedia.buyerorder.list.view.adapter;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.tokopedia.buyerorder.list.data.OrderCategory;
import com.tokopedia.buyerorder.list.data.OrderLabelList;
import com.tokopedia.buyerorder.list.view.fragment.OrderListFragment;
import com.tokopedia.flight.orderlist.view.fragment.FlightOrderListFragment;

import java.util.List;

public class OrderTabAdapter extends FragmentStatePagerAdapter {
    private static final String ORDER_CATEGORY = OrderCategory.KEY_LABEL;
    private static final String ORDER_TAB_LIST = "TAB_LIST";
    Listener listener;
    List<OrderLabelList> mOrderLabelList;

    public OrderTabAdapter(FragmentManager fragmentManager, Listener listener, List<OrderLabelList> orderLabelList) {
        super(fragmentManager);
        this.listener = listener;
        this.mOrderLabelList = orderLabelList;
    }

    @Override
    public int getCount() {
        return mOrderLabelList.size();
    }

    @Override
    public Fragment getItem(int position) {
        Bundle arg = listener.getBundle();
        String orderCategory = mOrderLabelList.get(position).getOrderCategory();
        OrderLabelList orderLabelList = mOrderLabelList.get(position);
        arg.putParcelable(ORDER_TAB_LIST, orderLabelList);
        arg.putString(ORDER_CATEGORY, orderCategory);
        Fragment fragment = null;
        if(orderCategory.equals(OrderCategory.FLIGHTS)) {
            fragment = FlightOrderListFragment.createInstance();
        }
        if (fragment == null) {
            fragment = new OrderListFragment();
        }
        fragment.setArguments(arg);
        return fragment;

    }

    public interface Listener {
        Bundle getBundle();
    }
}
