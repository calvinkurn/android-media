package com.tokopedia.buyerorder.list.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.buyerorder.list.data.OrderCategory;
import com.tokopedia.buyerorder.list.view.fragment.OrderListFragment;
import com.tokopedia.graphql.data.GraphqlClient;

public class SellerOrderListActivity extends BaseSimpleActivity{
    @Override
    protected Fragment getNewFragment() {
        Bundle arg = new Bundle();
        arg.putString(OrderCategory.KEY_LABEL, OrderCategory.DIGITAL);
        Fragment fragment = new OrderListFragment();
        fragment.setArguments(arg);
        return fragment;
    }

    public static Intent getInstance(Context context) {
        return new Intent(context, SellerOrderListActivity.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GraphqlClient.init(this);
        super.onCreate(savedInstanceState);
    }
}
