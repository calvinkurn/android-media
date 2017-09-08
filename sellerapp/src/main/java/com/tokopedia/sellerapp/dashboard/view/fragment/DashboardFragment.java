package com.tokopedia.sellerapp.dashboard.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.shopinfo.models.shopmodel.Info;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopTxStats;
import com.tokopedia.design.ticker.TickerView;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.seller.home.view.ReputationView;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.dashboard.di.DaggerSellerDashboardComponent;
import com.tokopedia.sellerapp.dashboard.di.SellerDashboardComponent;
import com.tokopedia.sellerapp.dashboard.presenter.SellerDashboardPresenter;
import com.tokopedia.sellerapp.dashboard.view.SellerDashboardView;
import com.tokopedia.sellerapp.home.view.model.ShopScoreViewModel;

import javax.inject.Inject;

/**
 * Created by nathan on 9/6/17.
 */

public class DashboardFragment extends BaseDaggerFragment implements SellerDashboardView {

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Inject
    public SellerDashboardPresenter sellerDashboardPresenter;

    private TickerView tickerView;

    private LabelView newOrderLabelView;
    private LabelView deliveryConfirmationLabelView;
    private LabelView deliveryStatusLabelView;
    private LabelView opportunityLabelView;
    private LabelView messageLabelView;
    private LabelView discussionLabelView;
    private LabelView reviewLabelView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tickerView = (TickerView) view.findViewById(R.id.ticker_view);
        newOrderLabelView = (LabelView) view.findViewById(R.id.label_view_new_order);
        deliveryConfirmationLabelView = (LabelView) view.findViewById(R.id.label_view_delivery_confirmation);
        deliveryStatusLabelView = (LabelView) view.findViewById(R.id.label_view_delivery_status);
        opportunityLabelView = (LabelView) view.findViewById(R.id.label_view_opportunity);
        messageLabelView = (LabelView) view.findViewById(R.id.label_view_message);
        discussionLabelView = (LabelView) view.findViewById(R.id.label_view_discussion);
        reviewLabelView = (LabelView) view.findViewById(R.id.label_view_review);
        newOrderLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        deliveryConfirmationLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        deliveryStatusLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        opportunityLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        messageLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        discussionLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        reviewLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        SellerDashboardComponent sellerDashboardComponent =
                DaggerSellerDashboardComponent.builder().appComponent(
                        MainApplication.getInstance().getApplicationComponent()).build();
        sellerDashboardComponent.inject(this);

        sellerDashboardPresenter.attachView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        sellerDashboardPresenter.getShopInfo();
        sellerDashboardPresenter.getShopScoreMainData();
    }

    @Override
    public void onErrorGetShopInfo(Throwable e) {
        Log.i("Test", e.getMessage());
        //TODO snackbar retry
    }

    @Override
    public void onSuccessGetShopInfo(Info shopModelInfo) {
        //TODO render shop name, logo, cover, location, goldmerchant or not, etc
    }

    @Override
    public void onSuccessGetShopOpenInfo(boolean isOpen) {
        //TODO is Shop open
    }

    @Override
    public void onSuccessGetShopTransaction(ShopTxStats shopTxStats) {
        //TODO render transation shop stat
    }

    @Override
    public void onSuccessGetReputation(ReputationView.ReputationViewModel reputationViewModel) {
        // TODO render reputation
        //reputationview.setseller(...);
    }

    @Override
    public void renderShopScore(ShopScoreViewModel shopScoreViewModel) {
        // TODO render shop score
        // shopScoreWidget.renderView(shopScoreViewModel);
    }

    @Override
    public void onErrorShopScore() {
        //TODO snackbar shop score
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sellerDashboardPresenter.detachView();
    }
}
