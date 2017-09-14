package com.tokopedia.sellerapp.dashboard.view.fragment;

import android.content.Intent;
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
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.common.ticker.model.Ticker;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.inboxreputation.activity.InboxReputationActivity;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.shopinfo.models.shopmodel.Info;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.design.reputation.ShopReputationView;
import com.tokopedia.design.ticker.TickerView;
import com.tokopedia.seller.common.constant.ShopStatusDef;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.seller.goldmerchant.statistic.utils.KMNumbers;
import com.tokopedia.seller.reputation.view.activity.SellerReputationActivity;
import com.tokopedia.seller.shopscore.view.activity.ShopScoreDetailActivity;
import com.tokopedia.seller.shopsettings.ManageShopActivity;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.dashboard.di.DaggerSellerDashboardComponent;
import com.tokopedia.sellerapp.dashboard.di.SellerDashboardComponent;
import com.tokopedia.sellerapp.dashboard.presenter.SellerDashboardPresenter;
import com.tokopedia.sellerapp.dashboard.view.listener.SellerDashboardView;
import com.tokopedia.sellerapp.home.view.model.ShopScoreViewModel;
import com.tokopedia.sellerapp.home.view.widget.ShopScoreWidget;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by nathan on 9/6/17.
 */

public class DashboardFragment extends BaseDaggerFragment implements SellerDashboardView {

    private ViewGroup vgHeaderLabelLayout;
    private SwipeToRefresh swipeRefreshLayout;
    private View ivSettingIcon;
    private View reputationLabelLayout;
    private View transactionlabelLayout;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Inject
    public SellerDashboardPresenter sellerDashboardPresenter;

    private TickerView tickerView;
    private LoadingStateView headerShopInfoLoadingStateView;
    private ImageView shopIconImageView;
    private TextView shopNameTextView;
    private ImageView gmIconImageView;
    private TextView gmStatusTextView;

    private ShopScoreWidget shopScoreWidget;

    private TextView reputationPointTextView;
    private ShopReputationView shopReputationView;
    private TextView transactionSuccessTextView;
    private View viewShopStatus;
    private LabelView newOrderLabelView;
    private LabelView deliveryConfirmationLabelView;
    private LabelView deliveryStatusLabelView;
    private LabelView opportunityLabelView;
    private LabelView messageLabelView;
    private LabelView discussionLabelView;
    private LabelView reviewLabelView;

    private ShopWarningTickerView shopWarningTickerView;

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
        vgHeaderLabelLayout = (ViewGroup) view.findViewById(R.id.label_layout_header);
        shopIconImageView = (ImageView) view.findViewById(R.id.image_view_shop_icon);
        shopNameTextView = (TextView) view.findViewById(R.id.text_view_shop_name);
        gmIconImageView = (ImageView) view.findViewById(R.id.image_view_gm_icon);
        gmStatusTextView = (TextView) view.findViewById(R.id.text_view_gm_status);
        ivSettingIcon = view.findViewById(R.id.iv_setting);
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

        viewShopStatus = vgHeaderLabelLayout.findViewById(R.id.vg_shop_close);

        ivSettingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ManageShopActivity.class);
                startActivity(intent);
            }
        });
        newOrderLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SellerRouter.getActivitySellingTransactionNewOrder(getActivity());
                startActivity(intent);
            }
        });
        deliveryConfirmationLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SellerRouter.getActivitySellingTransactionConfirmShipping(getActivity());
                startActivity(intent);
            }
        });
        deliveryStatusLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SellerRouter.getActivitySellingTransactionShippingStatus(getActivity());
                startActivity(intent);
            }
        });
        opportunityLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SellerRouter.getActivitySellingTransactionOpportunity(getActivity());
                startActivity(intent);
            }
        });
        messageLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = InboxRouter.getInboxMessageActivityIntent(getActivity());
                startActivity(intent);
            }
        });
        discussionLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = InboxRouter.getInboxTalkActivityIntent(getActivity());
                startActivity(intent);
            }
        });
        reviewLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = InboxRouter.getInboxTalkActivityIntent(getActivity());
                startActivity(intent);
            }
        });
        shopScoreWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShopScoreDetailActivity.class);
                startActivity(intent);
            }
        });

        swipeRefreshLayout = (SwipeToRefresh) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                headerShopInfoLoadingStateView.setViewState(LoadingStateView.VIEW_LOADING);
                sellerDashboardPresenter.refreshShopInfo();
            }
        });
        headerShopInfoLoadingStateView.setViewState(LoadingStateView.VIEW_LOADING);

        sellerDashboardPresenter.getTicker();
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
        swipeRefreshLayout.setRefreshing(false);
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
    }

    private void updateReputation(final ShopModel shopModel) {
        reputationLabelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), InboxReputationActivity.class);
                intent.putExtra(InboxReputationActivity.GO_TO_REPUTATION_HISTORY, true);
                startActivity(intent);
            }
        });
        shopReputationView.setValue(shopModel.getStats().getShopBadgeLevel().getSet(),
                shopModel.getStats().getShopBadgeLevel().getLevel(), shopModel.getStats().getShopReputationScore());
        String formattedScore = KMNumbers.formatDecimalString(shopModel.getStats().getShopReputationScore(), false);
        reputationPointTextView.setText(getString(R.string.dashboard_x_points, formattedScore));
    }

    private void updateTransaction(final ShopModel shopModel) {
        transactionlabelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shopInfo = CacheUtil.convertModelToString(shopModel,
                        new TypeToken<ShopModel>() {
                        }.getType());
                Intent intent = new Intent(getContext(), ShopStatisticDetail.class);
                intent.putExtra(ShopStatisticDetail.EXTRA_SHOP_INFO, shopInfo);
                getContext().startActivity(intent);
            }
        });
        transactionSuccessTextView.setText(getString(R.string.dashboard_shop_success_rate,
                String.valueOf(shopModel.getStats().getRateSuccess())));
    }

    private void updateShopInfo(ShopModel shopModel) {
        Info shopModelInfo = shopModel.info;
        shopNameTextView.setText(shopModelInfo.getShopName());
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
        //TODO shopModel.info.shopLucky
    }

    private void updateViewShopOpen(ShopModel shopModel) {
            case ShopStatusDef.CLOSED:
                showShopClosed(shopModel);
                break;
            case ShopStatusDef.MODERATED:
                shopWarningTickerView.setVisibility(View.GONE);
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
        shopWarningTickerView.setIcon(R.drawable.icon_closed);
        shopWarningTickerView.setTitle(getString(R.string.dashboard_your_shop_is_closed_until_xx, shopCloseUntilString));
        shopWarningTickerView.setDescription(shopModel.closedInfo.note);
        shopWarningTickerView.setAction(getString(R.string.open_shop), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShopEditorActivity.class);
                intent.putExtra(ShopSettingView.FRAGMENT_TO_SHOW, ShopSettingView.EDIT_SHOP_FRAGMENT_TAG);
                UnifyTracking.eventManageShopInfo();
                startActivityForResult(intent, 0);
            }
        });
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
        for (Ticker.Tickers ticker : tickers) {
            messages.add(ticker.getBasicMessage());
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
        tickerView.buildView();
    }

    @Override
    public void onErrorGetNotifiction(String message) {
        // TODO on Error get notification
    }

    @Override
    public void onSuccessGetNotification(DrawerNotification drawerNotification) {
        int newOrderCount = drawerNotification.getSellingNewOrder();
        int shippingConfirmation = drawerNotification.getSellingShippingConfirmation();
        int shippingStatus = drawerNotification.getSellingShippingStatus();

        int inboxCount = drawerNotification.getInboxMessage();
        int discussCount = drawerNotification.getInboxTalk();
        int reviewCount = drawerNotification.getInboxReview();

        setCounterIfNotEmpty(newOrderLabelView, newOrderCount);
        setCounterIfNotEmpty(deliveryConfirmationLabelView, shippingConfirmation);
        setCounterIfNotEmpty(deliveryStatusLabelView, shippingStatus);

        setCounterIfNotEmpty(messageLabelView, inboxCount);
        setCounterIfNotEmpty(discussionLabelView, discussCount);
        setCounterIfNotEmpty(reviewLabelView, reviewCount);

    }

    private void setCounterIfNotEmpty(LabelView labelView, int counter){
        labelView.setContent(counter > 0? String.valueOf(counter) : null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sellerDashboardPresenter.detachView();
    }
}