package com.tokopedia.feedplus.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.analytic_constant.DataLayer;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.abstraction.common.utils.FindAndReplaceHelper;
import com.tokopedia.abstraction.common.utils.paging.PagingHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker;
import com.tokopedia.feedcomponent.util.util.DataMapper;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.activity.FeedPlusDetailActivity;
import com.tokopedia.feedplus.view.adapter.typefactory.feeddetail.FeedPlusDetailTypeFactory;
import com.tokopedia.feedplus.view.adapter.typefactory.feeddetail.FeedPlusDetailTypeFactoryImpl;
import com.tokopedia.feedplus.view.adapter.viewholder.feeddetail.DetailFeedAdapter;
import com.tokopedia.feedplus.view.analytics.FeedAnalytics;
import com.tokopedia.feedplus.view.analytics.FeedTrackingEventLabel;
import com.tokopedia.feedplus.view.analytics.ProductEcommerce;
import com.tokopedia.feedplus.view.di.DaggerFeedPlusComponent;
import com.tokopedia.feedplus.view.listener.FeedPlusDetail;
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailHeaderViewModel;
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailViewModel;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.linker.LinkerUtils;
import com.tokopedia.linker.interfaces.ShareCallback;
import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.linker.model.LinkerError;
import com.tokopedia.linker.model.LinkerShareData;
import com.tokopedia.linker.model.LinkerShareResult;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * @author by nisie on 5/18/17.
 */

public class FeedPlusDetailFragment extends BaseDaggerFragment
        implements FeedPlusDetail.View, WishListActionListener, ShareCallback {

    private static final String ARGS_DETAIL_ID = "DETAIL_ID";
    private static final int REQUEST_OPEN_PDP = 111;

    public static final String WISHLIST_STATUS_UPDATED_POSITION = "wishlistUpdatedPosition";
    public static final String WIHSLIST_STATUS_IS_WISHLIST = "isWishlist";

    private final String TYPE = "text/plain";
    private final String PLACEHOLDER_LINK = "{{branchlink}}";
    private final String KEY_OTHER = "lainnya";
    private final String TITLE_OTHER = "Lainnya";

    interface Event{
        String CATEGORY_PAGE = "clickKategori";
        String CLICK_APP_SHARE_WHEN_REFERRAL_OFF = "clickAppShare";
        String CLICK_APP_SHARE_REFERRAL = "clickReferral";
        String PRODUCT_DETAIL_PAGE = "clickPDP";
    }

    interface EventLabel{
        String SHARE_TO = "Share - ";
    }

    interface Category{
        String CATEGORY_PAGE = "Category Page";
        String REFERRAL = "Referral";
        String APPSHARE = "App share";
        String PRODUCT_DETAIL = "Product Detail Page";
    }

    interface Action{
        String CATEGORY_SHARE = "Bottom Navigation - Share";
        String SELECT_CHANNEL = "select channel";
        String CLICK = "Click";
    }

    interface MoEngage {
        String CHANNEL = "channel";
    }

    interface EventMoEngage {
        String REFERRAL_SHARE_EVENT = "Share_Event";
    }

    RecyclerView recyclerView;
    TextView shareButton;
    TextView seeShopButon;
    View footer;

    @Inject
    FeedPlusDetail.Presenter presenter;

    @Inject
    FeedAnalytics analytics;

    @Inject
    FeedAnalyticTracker feedAnalytics;

    @Inject
    UserSessionInterface userSession;

    private EndlessRecyclerViewScrollListener recyclerviewScrollListener;
    private LinearLayoutManager layoutManager;
    private DetailFeedAdapter adapter;
    private PagingHandler pagingHandler;
    private ProgressBar progressBar;
    private String detailId;
    private LinkerData shareData;


    public static FeedPlusDetailFragment createInstance(Bundle bundle) {
        FeedPlusDetailFragment fragment = new FeedPlusDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return FeedTrackingEventLabel.SCREEN_FEED_DETAIL;
    }

    @Override
    protected void initInjector() {
        DaggerFeedPlusComponent.builder()
                .baseAppComponent(
                        ((BaseMainApplication) requireContext().getApplicationContext()).getBaseAppComponent()
                )
                .build()
                .inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVar(savedInstanceState);
    }

    @SuppressWarnings("ConstantConditions")
    private void initVar(Bundle savedInstanceState) {
        if (savedInstanceState != null)
            detailId = savedInstanceState.getString(ARGS_DETAIL_ID);
        else if (getArguments() != null)
            detailId = getArguments().getString(FeedPlusDetailActivity.EXTRA_DETAIL_ID);
        else
            detailId = "";

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerviewScrollListener = onRecyclerViewListener();
        FeedPlusDetailTypeFactory typeFactory = new FeedPlusDetailTypeFactoryImpl(this);
        adapter = new DetailFeedAdapter(typeFactory);
        GraphqlClient.init(getContext());
        pagingHandler = new PagingHandler();
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_feed_plus_detail, container, false);
        recyclerView = parentView.findViewById(R.id.detail_list);
        shareButton = parentView.findViewById(R.id.share_button);
        seeShopButon = parentView.findViewById(R.id.see_shop);
        footer = parentView.findViewById(R.id.footer);
        progressBar = parentView.findViewById(R.id.progress_bar);
        prepareView();
        presenter.attachView(this, this);
        return parentView;

    }

    private void prepareView() {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(recyclerviewScrollListener);
    }

    private EndlessRecyclerViewScrollListener onRecyclerViewListener() {
        return new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (!adapter.isLoading() && pagingHandler.CheckNextPage()) {
                    pagingHandler.nextPage();
                    presenter.getFeedDetail(detailId, pagingHandler.getPage());
                }
            }
        };
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.getFeedDetail(detailId, pagingHandler.getPage());
    }

    @Override
    public void onStart() {
        super.onStart();
        analytics.trackScreen(getScreenName());
    }

    private View.OnClickListener onShareClicked(final String url,
                                                final String title,
                                                final String imageUrl,
                                                final String description) {
        return v -> {
            if (getActivity() != null) {
                shareData = LinkerData.Builder.getLinkerBuilder().setId(detailId)
                        .setName(title)
                        .setDescription(description)
                        .setImgUri(imageUrl)
                        .setUri(url)
                        .setType(LinkerData.FEED_TYPE)
                        .build();
                LinkerShareData linkerShareData = new DataMapper().getLinkerShareData(shareData);
                LinkerManager.getInstance().executeShareRequest(LinkerUtils.createShareRequest(
                        0,
                        linkerShareData,
                        this
                ));
            }
        };
    }

    @Override
    public void onWishlistClicked(int adapterPosition, Integer productId, boolean isWishlist) {
        if (getArguments() != null) {
            if (!isWishlist) {
                presenter.addToWishlist(adapterPosition, String.valueOf(productId));
                analytics.eventFeedClickProduct(
                        getScreenName(),
                        String.valueOf(productId),
                        getArguments().getString(FeedPlusDetailActivity
                                .EXTRA_ANALYTICS_PAGE_ROW_NUMBER, "")
                                + FeedTrackingEventLabel.Click.ADD_TO_WISHLIST +
                                FeedTrackingEventLabel.PAGE_PRODUCT_LIST);
            } else {
                presenter.removeFromWishlist(adapterPosition, String.valueOf(productId));
                analytics.eventFeedClickProduct(
                        getScreenName(),
                        String.valueOf(productId),
                        getArguments().getString(FeedPlusDetailActivity
                                .EXTRA_ANALYTICS_PAGE_ROW_NUMBER, "")
                                + FeedTrackingEventLabel.Click.REMOVE_WISHLIST +
                                FeedTrackingEventLabel.PAGE_PRODUCT_LIST);
            }
        }
    }

    @Override
    public void onGoToShopDetail(String activityId, Integer shopId) {
        if (getArguments() != null) {
            goToShopDetail(shopId);
            analytics.eventFeedViewShop(
                    getScreenName(),
                    String.valueOf(shopId),
                    getArguments().getString(FeedPlusDetailActivity.EXTRA_ANALYTICS_PAGE_ROW_NUMBER, "")
                            + FeedTrackingEventLabel.View.PRODUCTLIST_SHOP);
            feedAnalytics.eventClickFeedDetailAvatar(activityId, shopId.toString());
        }

    }

    @Override
    public void onErrorGetFeedDetail(String errorMessage) {
        dismissLoading();
        footer.setVisibility(View.GONE);
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage,
                () -> presenter.getFeedDetail(detailId, pagingHandler.getPage()));
    }

    @Override
    public void onEmptyFeedDetail() {
        adapter.showEmpty();
        footer.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (getActivity() != null) getActivity().onBackPressed();
    }

    @Override
    public void onSuccessGetFeedDetail(
            final FeedDetailHeaderViewModel header,
            ArrayList<Visitable> listDetail,
            boolean hasNextPage) {
        footer.setVisibility(View.VISIBLE);

        if (pagingHandler.getPage() == 1) {
            adapter.add(header);
        }

        adapter.addList(listDetail);

        shareButton.setOnClickListener(onShareClicked(
                header.getShareLinkURL(),
                header.getShopName(),
                header.getShopAvatar(),
                header.getShareLinkDescription()));

        seeShopButon.setOnClickListener(onGoToShopDetailFromButton(header.getShopId()));

        pagingHandler.setHasNext(listDetail.size() > 1 && hasNextPage);

        adapter.notifyDataSetChanged();

        trackImpression(listDetail);
    }

    private View.OnClickListener onGoToShopDetailFromButton(final Integer shopId) {
        return v -> {
            if (getArguments() != null) {
                goToShopDetail(shopId);
                analytics.eventFeedClickShop(
                        getScreenName(),
                        String.valueOf(shopId),
                        getArguments().getString(
                                FeedPlusDetailActivity.EXTRA_ANALYTICS_PAGE_ROW_NUMBER, "")
                                + FeedTrackingEventLabel.Click.VISIT_SHOP);
            }
        };
    }


    private void goToShopDetail(Integer shopId) {
        if (getActivity() != null && getActivity().getApplicationContext() != null) {
            Intent intent = RouteManager.getIntent(getActivity(),
                    ApplinkConst.SHOP,
                    String.valueOf(shopId));
            startActivity(intent);
        }
    }

    @Override
    public void showLoading() {
        footer.setVisibility(View.GONE);
        adapter.showLoading();
    }

    @Override
    public void dismissLoading() {
        footer.setVisibility(View.VISIBLE);
        adapter.dismissLoading();
    }

    @Override
    public void showLoadingMore() {
        adapter.showLoadingMore();
    }

    @Override
    public void dismissLoadingMore() {
        adapter.dismissLoadingMore();
    }

    @Override
    public void showLoadingProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onErrorAddWishList(String errorMessage, String productId) {
        dismissLoadingProgress();
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessAddWishlist(String productID) {
        dismissLoadingProgress();

        for (int i = 0; i < adapter.getList().size(); i++) {
            if (adapter.getList().get(i) instanceof FeedDetailViewModel) {
                FeedDetailViewModel feedDetailViewModel = ((FeedDetailViewModel) adapter.getList().get(i));
                if (productID.equals(String.valueOf(feedDetailViewModel.getProductId()))) {
                    feedDetailViewModel.setWishlist(true);
                    adapter.notifyItemChanged(i);
                    break;
                }
            }
        }

        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.feed_msg_add_wishlist));
    }

    @Override
    public void onErrorRemoveWishlist(String errorMessage, String productId) {
        dismissLoadingProgress();
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);

    }

    @Override
    public void onSuccessRemoveWishlist(String productID) {
        dismissLoadingProgress();

        for (int i = 0; i < adapter.getList().size(); i++) {
            if (adapter.getList().get(i) instanceof FeedDetailViewModel) {
                FeedDetailViewModel feedDetailViewModel = ((FeedDetailViewModel) adapter.getList().get(i));
                if (productID.equals(String.valueOf(feedDetailViewModel.getProductId()))) {
                    feedDetailViewModel.setWishlist(false);
                    adapter.notifyItemChanged(i);
                    break;
                }
            }
        }

        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.feed_msg_remove_wishlist));
    }

    @Override
    public void onGoToProductDetail(FeedDetailViewModel feedDetailViewModel, int adapterPosition) {
        if (getActivity() != null
                && getActivity().getApplicationContext() != null
                && getArguments() != null) {
            getActivity().startActivityForResult(
                    getProductIntent(String.valueOf(feedDetailViewModel.getProductId())),
                    REQUEST_OPEN_PDP
            );

            analytics.eventDetailProductClick(
                    new ProductEcommerce(String.valueOf(feedDetailViewModel.getProductId()),
                            feedDetailViewModel.getName(),
                            feedDetailViewModel.getPrice(),
                            adapterPosition),
                    getUserIdInt()
            );
        }
    }

    private Intent getProductIntent(String productId){
        if (getContext() != null) {
            return RouteManager.getIntent(getContext(),ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId);
        } else {
            return null;
        }
    }

    @Override
    public int getColor(int resId) {
        return MethodChecker.getColor(getActivity(), resId);
    }

    @Override
    public void setHasNextPage(boolean hasNextPage) {
        pagingHandler.setHasNext(hasNextPage);
    }

    private void dismissLoadingProgress() {
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARGS_DETAIL_ID, detailId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_OPEN_PDP
                && data != null
                && data.getExtras() != null
                && data.getExtras().getInt(WISHLIST_STATUS_UPDATED_POSITION, -1) != -1) {
            int position = data.getExtras().getInt(WISHLIST_STATUS_UPDATED_POSITION, -1);
            boolean isWishlist = data.getExtras().getBoolean(WIHSLIST_STATUS_IS_WISHLIST, false);

            updateWishlistFromPDP(position, isWishlist);
        }
    }

    private void updateWishlistFromPDP(int position, boolean isWishlist) {
        if (adapter != null
                && adapter.getList() != null
                && !adapter.getList().isEmpty()
                && position < adapter.getList().size()
                && adapter.getList().get(position) != null
                && adapter.getList().get(position) instanceof FeedDetailViewModel) {
            ((FeedDetailViewModel) adapter.getList().get(position)).setWishlist(isWishlist);
            adapter.notifyItemChanged(position);
        }
    }

    private void trackImpression(ArrayList<Visitable> listDetail) {
        ArrayList<ProductEcommerce> productList = new ArrayList<>();
        for (int position = 0; position < listDetail.size(); position++) {
            if (listDetail.get(position) instanceof FeedDetailViewModel) {
                FeedDetailViewModel model = (FeedDetailViewModel) listDetail.get(position);
                productList.add(new ProductEcommerce(
                        String.valueOf(model.getProductId()),
                        model.getName(),
                        model.getPrice(),
                        position
                ));
            }
        }
        analytics.eventDetailProductImpression(productList, getUserIdInt());
    }

    private int getUserIdInt() {
        try {
            return Integer.valueOf(userSession.getUserId());
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    @Override
    public void urlCreated(LinkerShareResult linkerShareData) {
        Intent intent = getIntent(linkerShareData.getShareContents(), linkerShareData.getUrl());
        if (null != getActivity()) {
            getActivity().startActivity(Intent.createChooser(intent, TITLE_OTHER));
            sendTracker();
        }
    }

    @Override
    public void onError(LinkerError linkerError) {

    }

    private Intent getIntent(String contains, String url) {
        final Intent mIntent = new Intent(Intent.ACTION_SEND);
        mIntent.setType(TYPE);

        String title = "";
        if (shareData != null) {
            title = shareData.getName();
        }

        if(!TextUtils.isEmpty(shareData.getCustmMsg()) && shareData.getCustmMsg().contains(PLACEHOLDER_LINK)) {
            contains = FindAndReplaceHelper.findAndReplacePlaceHolders(shareData.getCustmMsg(), PLACEHOLDER_LINK, url);
        }

        mIntent.putExtra(Intent.EXTRA_TITLE, title);
        mIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        mIntent.putExtra(Intent.EXTRA_TEXT, contains);
        return mIntent;
    }

    private void sendTracker() {
        if (shareData.getType().equals(LinkerData.CATEGORY_TYPE)) {
            shareCategory(shareData);
        } else {
            sendAnalyticsToGtm(shareData.getType());
        }
    }

    private void shareCategory(LinkerData data) {
        String[] shareParam = data.getSplittedDescription(",");
        if (shareParam.length == 2) {
            eventShareCategory(shareParam[0], shareParam[1] + "-" + KEY_OTHER);
        }
    }

    public void eventShareCategory(String parentCat, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                Event.CATEGORY_PAGE,
                Category.CATEGORY_PAGE + "-" + parentCat,
                Action.CATEGORY_SHARE,
                label);
    }

    private void sendAnalyticsToGtm(String type) {
        switch (type) {
            case LinkerData.REFERRAL_TYPE:
                sendEventReferralAndShare(
                        Action.SELECT_CHANNEL,
                        KEY_OTHER
                );
                sendMoEngageReferralShareEvent(KEY_OTHER);
                break;
            case LinkerData.APP_SHARE_TYPE:
                sendEventAppShareWhenReferralOff(
                        Action.SELECT_CHANNEL,
                        KEY_OTHER
                );
                break;
            default:
                sendEventShare(KEY_OTHER);
                break;
        }
    }

    public static void sendMoEngageReferralShareEvent(String channel) {
        Map<String, Object> value = DataLayer.mapOf(
                MoEngage.CHANNEL, channel
        );
        TrackApp.getInstance().getMoEngage().sendTrackEvent(value, EventMoEngage.REFERRAL_SHARE_EVENT);
    }

    private void sendEventReferralAndShare(String action, String label){
        Map<String, Object> eventTracking = new HashMap<>();
        eventTracking.put("event", Event.CLICK_APP_SHARE_REFERRAL);
        eventTracking.put("eventCategory", Category.REFERRAL);
        eventTracking.put("eventAction", action);
        eventTracking.put("eventLabel", label);
        TrackApp.getInstance().getGTM().sendGeneralEvent(eventTracking);
    }

    public static void sendEventAppShareWhenReferralOff(String action, String label) {
        Map<String, Object> eventTracking = new HashMap<>();
        eventTracking.put("event", Event.CLICK_APP_SHARE_WHEN_REFERRAL_OFF);
        eventTracking.put("eventCategory", Category.APPSHARE);
        eventTracking.put("eventAction", action);
        eventTracking.put("eventLabel", label);
        TrackApp.getInstance().getGTM().sendGeneralEvent(eventTracking);
    }

    public static void sendEventShare(String label) {
        Map<String, Object> eventTracking = new HashMap<>();
        eventTracking.put("event", Event.PRODUCT_DETAIL_PAGE);
        eventTracking.put("eventCategory", Category.PRODUCT_DETAIL);
        eventTracking.put("eventAction", Action.CLICK);
        eventTracking.put("eventLabel", EventLabel.SHARE_TO + label);
        TrackApp.getInstance().getGTM().sendGeneralEvent(eventTracking);
    }
}
