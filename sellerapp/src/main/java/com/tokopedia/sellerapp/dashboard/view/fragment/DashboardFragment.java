package com.tokopedia.sellerapp.dashboard.view.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.ShopStatisticDetail;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.common.ticker.model.Ticker;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.shopinfo.models.shopmodel.Info;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.design.card.EmptyCardContentView;
import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.design.reputation.ShopReputationView;
import com.tokopedia.design.ticker.TickerView;
import com.tokopedia.mitratoppers.preapprove.view.fragment.MitraToppersPreApproveLabelFragment;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.common.constant.ShopStatusDef;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.seller.product.edit.utils.ViewUtils;
import com.tokopedia.seller.reputation.view.activity.SellerReputationInfoActivity;
import com.tokopedia.seller.shopscore.view.activity.ShopScoreDetailActivity;
import com.tokopedia.seller.shopscore.view.model.ShopScoreViewModel;
import com.tokopedia.seller.shopscore.view.widget.ShopScoreWidget;
import com.tokopedia.seller.shopsettings.ManageShopActivity;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.dashboard.di.DaggerSellerDashboardComponent;
import com.tokopedia.sellerapp.dashboard.di.SellerDashboardComponent;
import com.tokopedia.sellerapp.dashboard.presenter.SellerDashboardPresenter;
import com.tokopedia.sellerapp.dashboard.view.listener.SellerDashboardView;
import com.tokopedia.sellerapp.dashboard.view.widget.ShopWarningTickerView;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by nathan on 9/6/17.
 */

public class DashboardFragment extends BaseDaggerFragment implements SellerDashboardView {

    private SwipeToRefresh swipeRefreshLayout;
    private View reputationLabelLayout;
    private View transactionlabelLayout;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Inject
    public SellerDashboardPresenter sellerDashboardPresenter;

    private TickerView tickerView;
    private LoadingStateView headerShopInfoLoadingStateView;
    private LoadingStateView footerShopInfoLoadingStateView;
    private ImageView shopIconImageView;
    private TextView shopNameTextView;
    private ImageView gmIconImageView;
    private TextView gmStatusTextView;

    private ShopScoreWidget shopScoreWidget;

    private TextView reputationPointTextView;
    private ShopReputationView shopReputationView;
    private TextView transactionSuccessTextView;
    private LabelView newOrderLabelView;
    private LabelView deliveryConfirmationLabelView;
    private LabelView deliveryStatusLabelView;
    private LabelView opportunityLabelView;
    private LabelView messageLabelView;
    private LabelView discussionLabelView;
    private LabelView reviewLabelView;

    private ShopWarningTickerView shopWarningTickerView;
    private ProgressDialog progressDialog;

    private SnackbarRetry snackBarRetry;

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
        headerShopInfoLoadingStateView = (LoadingStateView) view.findViewById(R.id.loading_state_view_header);
        footerShopInfoLoadingStateView = (LoadingStateView) view.findViewById(R.id.loading_state_view_footer);

        shopIconImageView = (ImageView) view.findViewById(R.id.image_view_shop_icon);
        shopNameTextView = (TextView) view.findViewById(R.id.text_view_shop_name);
        gmIconImageView = (ImageView) view.findViewById(R.id.image_view_gm_icon);
        gmStatusTextView = (TextView) view.findViewById(R.id.text_view_gm_status);
        View ivSettingIcon = view.findViewById(R.id.iv_setting);
        shopWarningTickerView = (ShopWarningTickerView) view.findViewById(R.id.shop_warning_ticker_view);

        reputationLabelLayout = view.findViewById(R.id.reputation_label_layout);
        reputationPointTextView = (TextView) view.findViewById(R.id.text_view_reputation_point);
        shopReputationView = (ShopReputationView) view.findViewById(R.id.shop_reputation_view);

        transactionlabelLayout = view.findViewById(R.id.transaction_label_layout);
        transactionSuccessTextView = (TextView) view.findViewById(R.id.text_view_transaction_success);

        newOrderLabelView = (LabelView) view.findViewById(R.id.label_view_new_order);
        deliveryConfirmationLabelView = (LabelView) view.findViewById(R.id.label_view_delivery_confirmation);
        deliveryStatusLabelView = (LabelView) view.findViewById(R.id.label_view_delivery_status);
        opportunityLabelView = (LabelView) view.findViewById(R.id.label_view_opportunity);
        messageLabelView = (LabelView) view.findViewById(R.id.label_view_message);
        discussionLabelView = (LabelView) view.findViewById(R.id.label_view_discussion);
        reviewLabelView = (LabelView) view.findViewById(R.id.label_view_review);
        shopScoreWidget = (ShopScoreWidget) view.findViewById(R.id.shop_score_widget);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));

        ivSettingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventSellerHomeDashboardClick(AppEventTracking.EventLabel.DASHBOARD_MAIN_SHOP_INFO,
                        AppEventTracking.EventLabel.DASHBOARD_ITEM_SETTINGS);
                Intent intent = new Intent(getContext(), ManageShopActivity.class);
                startActivity(intent);
            }
        });
        newOrderLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventSellerHomeDashboardClick(AppEventTracking.EventLabel.DASHBOARD_MAIN_TRANSACTION,
                        AppEventTracking.EventLabel.DASHBOARD_ITEM_ORDER_BARU);
                Intent intent = SellerRouter.getActivitySellingTransactionNewOrder(getActivity());
                startActivity(intent);
            }
        });
        deliveryConfirmationLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventSellerHomeDashboardClick(AppEventTracking.EventLabel.DASHBOARD_MAIN_TRANSACTION,
                        AppEventTracking.EventLabel.DASHBOARD_ITEM_KONFIRMASI_PENGIRIMAN);
                Intent intent = SellerRouter.getActivitySellingTransactionConfirmShipping(getActivity());
                startActivity(intent);
            }
        });
        deliveryStatusLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventSellerHomeDashboardClick(AppEventTracking.EventLabel.DASHBOARD_MAIN_TRANSACTION,
                        AppEventTracking.EventLabel.DASHBOARD_ITEM_STATUS_PENGIRIMAN);
                Intent intent = SellerRouter.getActivitySellingTransactionShippingStatus(getActivity());
                startActivity(intent);
            }
        });
        opportunityLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventSellerHomeDashboardClick(AppEventTracking.EventLabel.DASHBOARD_MAIN_TRANSACTION,
                        AppEventTracking.EventLabel.DASHBOARD_ITEM_PELUANG);
                Intent intent = SellerRouter.getActivitySellingTransactionOpportunity(getActivity());
                startActivity(intent);
            }
        });
        messageLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().getApplication() instanceof TkpdInboxRouter) {
                    UnifyTracking.eventSellerHomeDashboardClick(AppEventTracking.EventLabel.DASHBOARD_MAIN_INBOX,
                            AppEventTracking.EventLabel.DASHBOARD_ITEM_PESAN);
                    Intent intent = ((TkpdInboxRouter) getActivity().getApplication())
                            .getInboxMessageIntent(getActivity());
                    startActivity(intent);
                }
            }
        });
        discussionLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventSellerHomeDashboardClick(AppEventTracking.EventLabel.DASHBOARD_MAIN_INBOX,
                        AppEventTracking.EventLabel.DASHBOARD_ITEM_DISKUSI_PRODUK);
                Intent intent = InboxRouter.getInboxTalkActivityIntent(getActivity());
                startActivity(intent);
            }
        });
        reviewLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventSellerHomeDashboardClick(AppEventTracking.EventLabel.DASHBOARD_MAIN_INBOX,
                        AppEventTracking.EventLabel.DASHBOARD_ITEM_ULASAN);
                if(MainApplication.getAppContext() instanceof SellerModuleRouter){
                    startActivity(((SellerModuleRouter)MainApplication.getAppContext()).getInboxReputationIntent
                            (getActivity()));
                }

            }
        });
        shopScoreWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventSellerHomeDashboardClick(AppEventTracking.EventLabel.DASHBOARD_MAIN_SHOP_INFO,
                        AppEventTracking.EventLabel.DASHBOARD_ITEM_PERFORMA_TOKO);
                Intent intent = new Intent(getActivity(), ShopScoreDetailActivity.class);
                startActivity(intent);
            }
        });

        swipeRefreshLayout = (SwipeToRefresh) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                DashboardFragment.this.onRefresh();
            }
        });

        headerShopInfoLoadingStateView.setViewState(LoadingStateView.VIEW_LOADING);
        footerShopInfoLoadingStateView.setViewState(LoadingStateView.VIEW_LOADING);

        sellerDashboardPresenter.getTicker();
    }

    void onRefresh() {
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }
        headerShopInfoLoadingStateView.setViewState(LoadingStateView.VIEW_LOADING);
        footerShopInfoLoadingStateView.setViewState(LoadingStateView.VIEW_LOADING);
        sellerDashboardPresenter.refreshShopInfo();
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
        sellerDashboardPresenter.getShopInfoWithScore();
        sellerDashboardPresenter.getNotification();
    }

    @Override
    public void onErrorShopInfoAndScore(Throwable t) {
        headerShopInfoLoadingStateView.setViewState(LoadingStateView.VIEW_ERROR);
        headerShopInfoLoadingStateView.getContentView().setVisibility(View.INVISIBLE);
        View errorView = headerShopInfoLoadingStateView.getErrorView();
        EmptyCardContentView emptyCardContentView = (EmptyCardContentView) errorView.findViewById(R.id.empty_card_content_view);
        emptyCardContentView.setTitleText(getString(R.string.msg_network_error_1));
        emptyCardContentView.setDescriptionText(getString(R.string.msg_network_error_2));
        emptyCardContentView.setContentText(null);
        swipeRefreshLayout.setRefreshing(false);

        showSnackBarRetry(ViewUtils.getErrorMessage(getActivity(), t));
    }

    @Override
    public void onSuccessGetShopInfoAndScore(ShopModel shopModel, ShopScoreViewModel shopScoreViewModel) {
        headerShopInfoLoadingStateView.setViewState(LoadingStateView.VIEW_CONTENT);
        updateShopInfo(shopModel);
        updateReputation(shopModel);
        updateTransaction(shopModel);
        updateViewShopOpen(shopModel);
        shopScoreWidget.renderView(shopScoreViewModel);
        swipeRefreshLayout.setRefreshing(false);
        hideSnackBarRetry();

        setShopInfoToLabelFragment(shopModel.info);
    }

    public void setShopInfoToLabelFragment(Info shopInfo) {
        MitraToppersPreApproveLabelFragment mitraToppersPreApproveLabelFragment =
                (MitraToppersPreApproveLabelFragment) getChildFragmentManager()
                        .findFragmentById(R.id.fragment_preapprove_label);
        if (mitraToppersPreApproveLabelFragment!=null) {
            mitraToppersPreApproveLabelFragment.setUserInfo(shopInfo.isOfficialStore(),
                    shopInfo.isGoldMerchant());
        }
    }

    private void updateReputation(final ShopModel shopModel) {
        reputationLabelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventSellerHomeDashboardClick(AppEventTracking.EventLabel.DASHBOARD_MAIN_SHOP_INFO,
                        AppEventTracking.EventLabel.DASHBOARD_ITEM_REPUTASI_TOKO);
                startActivity(new Intent(getContext(), SellerReputationInfoActivity.class));
            }
        });
        shopReputationView.setValue(shopModel.getStats().getShopBadgeLevel().getSet(),
                shopModel.getStats().getShopBadgeLevel().getLevel(), shopModel.getStats().getShopReputationScore());
        reputationPointTextView.setText(getString(R.string.dashboard_x_points, shopModel.getStats().getShopReputationScore()));
    }

    private void updateTransaction(final ShopModel shopModel) {
        transactionlabelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventSellerHomeDashboardClick(AppEventTracking.EventLabel.DASHBOARD_MAIN_SHOP_INFO,
                        AppEventTracking.EventLabel.DASHBOARD_ITEM_TRANSAKSI_SUKSES);
                String shopInfo = CacheUtil.convertModelToString(shopModel,
                        new TypeToken<ShopModel>() {
                        }.getType());
                Intent intent = new Intent(getContext(), ShopStatisticDetail.class);
                intent.putExtra(ShopStatisticDetail.EXTRA_SHOP_INFO, shopInfo);
                getContext().startActivity(intent);
            }
        });
        if (shopModel.shopTxStats.shopTxHasTransaction1Month == 1) {
            transactionSuccessTextView.setText(getString(R.string.dashboard_shop_success_rate,
                    String.valueOf(shopModel.shopTxStats.shopTxSuccessRate1Month)));
        } else {
            transactionSuccessTextView.setText(getString(R.string.dashboard_shop_success_rate,
                    String.valueOf(0)));
        }

    }

    private void updateShopInfo(ShopModel shopModel) {
        Info shopModelInfo = shopModel.info;
        String shopName = shopModelInfo.getShopName();
        if (!TextUtils.isEmpty(shopName)) {
            shopName = MethodChecker.fromHtml(shopName).toString();
        }
        shopNameTextView.setText(shopName);
        if (shopModelInfo.isGoldMerchant()) {
            gmIconImageView.setVisibility(View.VISIBLE);
            gmStatusTextView.setText(R.string.dashboard_label_gold_merchant);
        } else {
            gmIconImageView.setVisibility(View.GONE);
            gmStatusTextView.setText(R.string.dashboard_label_regular_merchant);
        }
        if (!TextUtils.isEmpty(shopModel.info.shopAvatar)) {
            ImageHandler.LoadImage(shopIconImageView, shopModel.info.shopAvatar);
        } else {
            shopIconImageView.setImageResource(R.drawable.ic_placeholder_shop_with_padding);
        }
    }

    private void updateViewShopOpen(ShopModel shopModel) {
        switch (shopModel.getInfo().getShopStatus()) {
            case ShopStatusDef.CLOSED:
                showShopClosed(shopModel);
                break;
            case ShopStatusDef.MODERATED:
                showShopModerated(shopModel);
                break;
            case ShopStatusDef.NOT_ACTIVE:
                shopWarningTickerView.setVisibility(View.GONE);
                break;
            default:
                shopWarningTickerView.setVisibility(View.GONE);
        }
    }

    private void showShopClosed(ShopModel shopModel) {
        String shopCloseUntilString = DateFormatUtils.formatDate(DateFormatUtils.FORMAT_DD_MM_YYYY,
                DateFormatUtils.FORMAT_D_MMMM_YYYY,
                shopModel.closedInfo.until);
        shopWarningTickerView.setIcon(R.drawable.ic_closed);
        shopWarningTickerView.setTitle(getString(R.string.dashboard_your_shop_is_closed_until_xx, shopCloseUntilString));
        shopWarningTickerView.setDescription(shopModel.closedInfo.note);
        shopWarningTickerView.setAction(getString(R.string.open_shop), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellerDashboardPresenter.openShop();
            }
        });
        shopWarningTickerView.setTickerColor(ContextCompat.getColor(getContext(), R.color.green_ticker));
        shopWarningTickerView.setVisibility(View.VISIBLE);
    }

    private void showShopModerated(ShopModel shopModel) {
        shopWarningTickerView.setIcon(R.drawable.ic_moderasi);
        shopWarningTickerView.setTitle(getString(R.string.dashboard_your_shop_is_in_moderation));
        shopWarningTickerView.setDescription(getString(R.string.dashboard_reason_x, shopModel.closedInfo.reason));
        shopWarningTickerView.setTickerColor(ContextCompat.getColor(getContext(), R.color.yellow_ticker));
        shopWarningTickerView.setAction(null, null);
        shopWarningTickerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onErrorGetTickers(Throwable throwable) {
        tickerView.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessGetTickers(Ticker.Tickers[] tickers) {
        tickerView.setVisibility(View.VISIBLE);
        ArrayList<String> messages = new ArrayList<>();
        final ArrayList<String> backgrounds = new ArrayList<>();
        for (Ticker.Tickers ticker : tickers) {
            messages.add(ticker.getBasicMessage());
            backgrounds.add(ticker.getColor());
        }
        tickerView.setListMessage(messages);
        tickerView.setHighLightColor(ContextCompat.getColor(getContext(), R.color.tkpd_yellow_status));
        tickerView.setOnPartialTextClickListener(new TickerView.OnPartialTextClickListener() {
            @Override
            public void onClick(View view, String messageClick) {
                Intent intent = new Intent(getActivity(), BannerWebView.class);
                intent.putExtra("url", messageClick);
                startActivity(intent);
            }
        });
        tickerView.setOnPageChangeListener(new TickerView.OnPageChangeListener() {
            @Override
            public void onScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onSelected(int position) {
                tickerView.setHighLightColor(Color.parseColor(backgrounds.get(position)));
            }

            @Override
            public void onScrollStateChanged(int state) {

            }
        });
        tickerView.buildView();
    }

    @Override
    public void onErrorGetNotifiction(String message) {
        // just show the content without the count
        footerShopInfoLoadingStateView.setViewState(LoadingStateView.VIEW_CONTENT);
        showSnackBarRetry(message);
    }

    @Override
    public void onSuccessGetNotification(DrawerNotification drawerNotification) {
        footerShopInfoLoadingStateView.setViewState(LoadingStateView.VIEW_CONTENT);

        int newOrderCount = drawerNotification.getSellingNewOrder();
        int shippingConfirmation = drawerNotification.getSellingShippingConfirmation();
        int shippingStatus = drawerNotification.getSellingShippingStatus();

        int discussCount = drawerNotification.getInboxTalk();
        int reviewCount = drawerNotification.getInboxReview();
        int messageCount = drawerNotification.getInboxMessage();

        setCounterIfNotEmpty(newOrderLabelView, newOrderCount);
        setCounterIfNotEmpty(deliveryConfirmationLabelView, shippingConfirmation);
        setCounterIfNotEmpty(deliveryStatusLabelView, shippingStatus);
        setCounterIfNotEmpty(messageLabelView, messageCount);

        setCounterIfNotEmpty(discussionLabelView, discussCount);
        setCounterIfNotEmpty(reviewLabelView, reviewCount);

    }

    private void showSnackBarRetry(String message) {
        if (snackBarRetry == null) {
            snackBarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), message, new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    DashboardFragment.this.onRefresh();
                }
            });
            snackBarRetry.setColorActionRetry(ContextCompat.getColor(getActivity(), com.tokopedia.seller.R.color.green_400));
        }
        snackBarRetry.showRetrySnackbar();
    }

    private void hideSnackBarRetry() {
        if (snackBarRetry != null) {
            snackBarRetry.hideRetrySnackbar();
        }
    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.hide();
    }

    @Override
    public void onSuccessOpenShop() {
        onRefresh();
    }

    @Override
    public void onErrorOpenShop() {
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_network_error));
    }

    private void setCounterIfNotEmpty(LabelView labelView, int counter) {
        labelView.setContent(counter > 0 ? String.valueOf(counter) : null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sellerDashboardPresenter.detachView();
    }
}