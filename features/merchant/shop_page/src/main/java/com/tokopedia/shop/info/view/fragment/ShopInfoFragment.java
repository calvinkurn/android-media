package com.tokopedia.shop.info.view.fragment;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.shop.R;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.address.view.activity.ShopAddressListActivity;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.common.util.TextApiUtils;
import com.tokopedia.shop.common.util.TextHtmlUtils;
import com.tokopedia.shop.info.di.component.DaggerShopInfoComponent;
import com.tokopedia.shop.info.di.module.ShopInfoModule;
import com.tokopedia.shop.info.view.adapter.ShopInfoLogisticAdapter;
import com.tokopedia.shop.info.view.adapter.ShopInfoLogisticAdapterTypeFactory;
import com.tokopedia.shop.info.view.listener.ShopInfoDetailView;
import com.tokopedia.shop.info.view.mapper.ShopInfoLogisticViewModelMapper;
import com.tokopedia.shop.info.view.presenter.ShopInfoDetailPresenter;

import javax.inject.Inject;

/**
 * Created by nathan on 2/5/18.
 */

public class ShopInfoFragment extends BaseDaggerFragment implements ShopInfoDetailView {

    @Inject
    ShopInfoDetailPresenter shopInfoDetailPresenter;
    private LinearLayout shopInfoStatisticLinearLayout;
    private LinearLayout shopInfoSatisfiedLinearLayout;
    private LabelView transactionSuccessLabelView;
    private LabelView totalTransactionLabelView;
    private LabelView productSoldLabelView;
    private LabelView totalReviewLabelView;
    private LabelView favoriteLabelView;
    private LabelView lastOnlineLabelView;
    private LabelView openSinceLabelView;
    private LabelView totalProductLabelView;
    private LabelView totalEtalaseLabelView;
    private LabelView physicalShopLabelView;
    private LabelView shopOwnerLabelView;
    private TextView scoreGoodTextView;
    private TextView scoreNeutralTextView;
    private TextView scoreBadTextView;
    private TextView taglineTextView;
    private TextView descriptionTextView;
    private RecyclerView recyclerView;
    private ShopInfoLogisticAdapter shopInfoLogisticAdapter;

    public static ShopInfoFragment createInstance() {
        ShopInfoFragment shopInfoFragment = new ShopInfoFragment();
        return shopInfoFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopInfoDetailPresenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_info, container, false);

        shopInfoStatisticLinearLayout = view.findViewById(R.id.linear_layout_shop_info_statistic);
        shopInfoSatisfiedLinearLayout = view.findViewById(R.id.linear_layout_shop_info_satisfied);

        transactionSuccessLabelView = view.findViewById(R.id.label_view_transaction_success);
        totalTransactionLabelView = view.findViewById(R.id.label_view_total_transaction);
        productSoldLabelView = view.findViewById(R.id.label_view_product_sold);
        totalReviewLabelView = view.findViewById(R.id.label_view_total_review);
        favoriteLabelView = view.findViewById(R.id.label_view_favorite);
        lastOnlineLabelView = view.findViewById(R.id.label_view_last_online);
        openSinceLabelView = view.findViewById(R.id.label_view_open_since);
        totalProductLabelView = view.findViewById(R.id.label_view_total_product);
        totalEtalaseLabelView = view.findViewById(R.id.label_view_total_etalase);

        physicalShopLabelView = view.findViewById(R.id.label_view_physical_shop);
        shopOwnerLabelView = view.findViewById(R.id.label_view_shop_owner);

        scoreGoodTextView = view.findViewById(R.id.text_view_score_good);
        scoreNeutralTextView = view.findViewById(R.id.text_view_score_neutral);
        scoreBadTextView = view.findViewById(R.id.text_view_score_bad);

        taglineTextView = view.findViewById(R.id.text_view_tagline);
        descriptionTextView = view.findViewById(R.id.text_view_description);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);

        return view;
    }

    public void updateShopInfo(ShopInfo shopInfo) {
        if (!TextApiUtils.isValueTrue(shopInfo.getInfo().getShopIsOfficial())) {
            displayBasicShopInfo(shopInfo);
        }
        displayAboutStoreInfo(shopInfo);
        displayLogisticShopInfo(shopInfo);
    }

    private void displayAboutStoreInfo(final ShopInfo shopInfo) {
        taglineTextView.setText(TextHtmlUtils.getTextFromHtml(shopInfo.getInfo().getShopTagline()));
        descriptionTextView.setText(TextHtmlUtils.getTextFromHtml(shopInfo.getInfo().getShopDescription()));

        String physicalAddressContent = getString(R.string.shop_info_physical_shop_location_only_online);
        if (shopInfo.getAddress().size() > 0) {
            physicalAddressContent = getString(R.string.shop_info_physical_shop_location_count, shopInfo.getAddress().size());
            physicalShopLabelView.setContentColorValue(ContextCompat.getColor(getActivity(), R.color.tkpd_main_green));
            physicalShopLabelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = ShopAddressListActivity.createIntent(getActivity(), shopInfo.getInfo().getShopId());
                    startActivity(intent);
                }
            });
        }
        physicalShopLabelView.setContent(physicalAddressContent);
        shopOwnerLabelView.setTitle(shopInfo.getOwner().getOwnerName());
        ImageHandler.loadImageCircle2(shopOwnerLabelView.getImageView().getContext(), shopOwnerLabelView.getImageView(), shopInfo.getOwner().getOwnerImage());
    }

    private void displayBasicShopInfo(final ShopInfo shopInfo) {
        shopInfoStatisticLinearLayout.setVisibility(View.VISIBLE);
        shopInfoSatisfiedLinearLayout.setVisibility(View.VISIBLE);
        physicalShopLabelView.setVisibility(View.VISIBLE);

        transactionSuccessLabelView.setContent(getString(R.string.shop_info_success_percentage, shopInfo.getShopTxStats().getShopTxSuccessRate1Year()));
        totalTransactionLabelView.setContent(shopInfo.getStats().getShopTotalTransaction());
        productSoldLabelView.setContent(shopInfo.getStats().getShopItemSold());
        totalReviewLabelView.setContent(shopInfo.getRatings().getQuality().getCountTotal());
        favoriteLabelView.setContent(String.valueOf(shopInfo.getStats().getFavoriteCount()));
        lastOnlineLabelView.setContent(shopInfo.getInfo().getShopOwnerLastLogin());
        openSinceLabelView.setContent(shopInfo.getInfo().getShopOpenSince());
        totalProductLabelView.setContent(shopInfo.getStats().getShopTotalProduct());
        totalEtalaseLabelView.setContent(shopInfo.getStats().getShopTotalEtalase());

        scoreGoodTextView.setText(shopInfo.getStats().getShopLastTwelveMonths().getCountScoreGood());
        scoreNeutralTextView.setText(shopInfo.getStats().getShopLastTwelveMonths().getCountScoreNeutral());
        scoreBadTextView.setText(shopInfo.getStats().getShopLastTwelveMonths().getCountScoreBad());

        shopOwnerLabelView.setContent(getString(R.string.see_label));
        shopOwnerLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Application application = ShopInfoFragment.this.getActivity().getApplication();
                if (application instanceof ShopModuleRouter) {
                    ((ShopModuleRouter) application).goToProfileShop(getActivity(), Long.toString(shopInfo.getOwner().getOwnerId()));
                }
            }
        });
    }

    private void displayLogisticShopInfo(ShopInfo shopInfo) {
        ShopInfoLogisticViewModelMapper mapper = new ShopInfoLogisticViewModelMapper();
        shopInfoLogisticAdapter = new ShopInfoLogisticAdapter(new ShopInfoLogisticAdapterTypeFactory(), mapper.transform(shopInfo.getShipment()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(shopInfoLogisticAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
    }

    @Override
    protected void initInjector() {
        DaggerShopInfoComponent
                .builder()
                .shopInfoModule(new ShopInfoModule())
                .shopComponent(getComponent(ShopComponent.class))
                .build()
                .inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shopInfoDetailPresenter != null) {
            shopInfoDetailPresenter.detachView();
        }
    }
}