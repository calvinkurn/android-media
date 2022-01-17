package com.tokopedia.buyerorder.detail.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.buyerorder.R;
import com.tokopedia.buyerorder.detail.data.OrderCategory;
import com.tokopedia.buyerorder.detail.di.DaggerOrderDetailsComponent;
import com.tokopedia.buyerorder.detail.di.OrderDetailsComponent;
import com.tokopedia.buyerorder.detail.view.fragment.OmsDetailFragment;
import com.tokopedia.buyerorder.detail.view.fragment.OrderListDetailFragment;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.user.session.UserSession;

/**
 * Created by baghira on 09/05/18.
 */

public class OrderListDetailActivity extends BaseSimpleActivity implements HasComponent<OrderDetailsComponent> {


    private static final String ORDER_ID = "order_id";
    private static final String FROM_PAYMENT = "from_payment";
    private static final String UPSTREAM = "upstream";
    private static final int REQUEST_CODE = 100;
    private String fromPayment = "false";
    private String orderId;
    private OrderDetailsComponent orderListComponent;
    String category = null;
    String upstream = null;

    public static Intent getIntent(Context context, String orderId, Uri data) {
        Intent intent = new Intent(context, OrderListDetailActivity.class);
        intent.putExtra(ORDER_ID, orderId);
        intent.setData(data);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        if (category != null) {
            category = category.toUpperCase();

            if (category.contains(OrderCategory.DIGITAL)) {
                return OrderListDetailFragment.getInstance(orderId, OrderCategory.DIGITAL);
            } else {
                return OmsDetailFragment.getInstance(orderId, "", fromPayment, upstream);
            }
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle arg) {

        if (getIntent() != null && getIntent().getData() != null) {
            category = String.valueOf(getIntent().getData());

            if (getIntent().getExtras() != null) {
                orderId = getIntent().getStringExtra(ORDER_ID);
            }

            Uri uri = getIntent().getData();
            if (uri != null) {
                fromPayment = uri.getQueryParameter(FROM_PAYMENT);
            }
        }

        UserSession userSession = new UserSession(this);
        if (!userSession.isLoggedIn()) {
            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODE);
        } else {
            if (getIntent().getData() != null)
                upstream = getIntent().getData().getQueryParameter(UPSTREAM);
        }
        super.onCreate(arg);
        if (fromPayment != null) {
            if (fromPayment.equalsIgnoreCase("true")) {
                updateTitle(getResources().getString(R.string.thank_you));
            }
        }
    }

    @Override
    public OrderDetailsComponent getComponent() {
        if (orderListComponent == null) initInjector();
        return orderListComponent;
    }

    private void initInjector() {
        orderListComponent = DaggerOrderDetailsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (getIntent() != null && getIntent().getData() != null) {
                    category = String.valueOf(getIntent().getData());
                    upstream = getIntent().getData().getQueryParameter(UPSTREAM);
                }

                if (category != null) {
                    category = category.toUpperCase();

                    if (category.contains(OrderCategory.DIGITAL)) {
                        getSupportFragmentManager().beginTransaction()
                                .add(com.tokopedia.abstraction.R.id.parent_view, OrderListDetailFragment.getInstance(orderId, OrderCategory.DIGITAL)).commit();

                    } else if (category.contains("")) {
                        getSupportFragmentManager().beginTransaction()
                                .add(com.tokopedia.abstraction.R.id.parent_view, OmsDetailFragment.getInstance(orderId, "", fromPayment, upstream)).commit();
                    }
                }
            } else {
                finish();
            }
        }
    }
}
