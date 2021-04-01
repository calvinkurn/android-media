package com.tokopedia.digital_deals.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.core.widget.NestedScrollView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.TouchViewPager;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.design.viewpagerindicator.CirclePageIndicator;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.view.activity.BrandDetailsActivity;
import com.tokopedia.digital_deals.view.activity.DealDetailsActivity;
import com.tokopedia.digital_deals.view.activity.model.DealDetailPassData;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.adapter.SlidingImageAdapterDealDetails;
import com.tokopedia.digital_deals.view.contractor.DealCategoryAdapterContract;
import com.tokopedia.digital_deals.view.contractor.DealDetailsContract;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.model.Media;
import com.tokopedia.digital_deals.view.model.Outlet;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse;
import com.tokopedia.digital_deals.view.model.response.EventContentData;
import com.tokopedia.digital_deals.view.presenter.BrandDetailsPresenter;
import com.tokopedia.digital_deals.view.presenter.DealCategoryAdapterPresenter;
import com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter;
import com.tokopedia.digital_deals.view.utils.CustomWidthToaster;
import com.tokopedia.digital_deals.view.utils.DealFragmentCallbacks;
import com.tokopedia.digital_deals.view.utils.DealsAnalytics;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.unifycomponents.Toaster;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import static com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter.DEFAULT_PARAM_ENABLE;
import static com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter.PARAM_DEAL_PASSDATA;

public class DealDetailsFragment extends BaseDaggerFragment implements DealDetailsContract.View, View.OnClickListener, DealCategoryAdapterContract.View, DealsCategoryAdapter.INavigateToActivityRequest {

    private static final String SCREEN_NAME = "/digital/deals/product";
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView tvExpandableDesc;
    private TextView tvExpandableTC;
    private TextView seeMoreButtonDesc;
    private TextView seeMoreButtonTC;

    private AppBarLayout appBarLayout;

    private RecyclerView recyclerViewDeals;
    @Inject
    public DealDetailsPresenter mPresenter;
    @Inject
    DealCategoryAdapterPresenter mPresenter2;
    @Inject
    DealsAnalytics dealsAnalytics;

    private View progressBarLayout;
    private ProgressBar progBar;
    private CoordinatorLayout mainContent;
    private ConstraintLayout baseMainContent;
    private TextView tvMrp;
    private TextView tvSalesPrice;
    private ImageView ivFavourite;
    private TextView tvLikes;
    private TextView tvExpiryDate;
    private TextView tvNumberOfLocations;
    private TextView tvAllLocations;
    private TextView tvBrandName;
    private TextView tvBrandVenue;
    private TextView tvBrandAddress;
    private TextView tvViewMap;
    private TextView tvOff;
    private TextView tvRecommendedDeals;
    private ImageView ivBrandLogo;
    private NestedScrollView svDetails;
    private TextView buyDealNow;
    private Menu mMenu;
    private ConstraintLayout clHeader;
    private ConstraintLayout clOutlets;
    private ConstraintLayout clDescription;
    private ConstraintLayout clTnc;
    private CardView cardView;
    private Toolbar toolbar;
    private DealFragmentCallbacks fragmentCallbacks;
    private DealsDetailsResponse dealDetail;
    private LinearLayoutManager mLayoutManager;
    private final boolean IS_SHORT_LAYOUT = true;
    private String latLng;
    private TouchViewPager viewPager;
    private CirclePageIndicator circlePageIndicator;
    private final int LIKE_REQUEST_CODE = 1099;
    private ConstraintLayout clRedeemInstuctns;
    private TextView tvDealDetails;
    private View dividerDesc;
    private View dividerTnC;
    private boolean forceRefresh;
    private DealsCategoryAdapter dealsAdapter;
    private UserSession userSession;

    private static final int SALAM_VALUE = 32768;
    private static final int SALAM_INDICATOR = 0;
    private static final String SALAM_REGEX_PATTERN = "<a(?:[^>]+)?>(.*?)<\\/a>";
    private static final int URL_GROUP = 1;
    private static final int FIRST_VALUE = 0;

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new DealDetailsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(com.tokopedia.digital_deals.R.layout.fragment_deal_details, container, false);
        setViewIds(view);
        setHasOptionsMenu(true);
        userSession = new UserSession(getActivity());

        mPresenter.getDealDetails();
        return view;
    }


    private void setViewIds(View view) {
        tvMrp = view.findViewById(com.tokopedia.digital_deals.R.id.tv_mrp);
        tvSalesPrice = view.findViewById(com.tokopedia.digital_deals.R.id.tv_sales_price);
        tvOff = view.findViewById(com.tokopedia.digital_deals.R.id.tv_off);
        ivFavourite = view.findViewById(com.tokopedia.digital_deals.R.id.iv_wish_list);
        tvLikes = view.findViewById(com.tokopedia.digital_deals.R.id.tv_favourite);
        tvExpiryDate = view.findViewById(com.tokopedia.digital_deals.R.id.tv_expiry_date);
        tvNumberOfLocations = view.findViewById(com.tokopedia.digital_deals.R.id.tv_number_of_locations);
        ivBrandLogo = view.findViewById(com.tokopedia.digital_deals.R.id.image_view_brand);
        tvAllLocations = view.findViewById(com.tokopedia.digital_deals.R.id.tv_see_all_locations);
        tvBrandName = view.findViewById(com.tokopedia.digital_deals.R.id.tv_brand_name);
        tvBrandVenue = view.findViewById(com.tokopedia.digital_deals.R.id.tv_brand_venue);
        tvBrandAddress = view.findViewById(com.tokopedia.digital_deals.R.id.tv_brand_address);
        tvViewMap = view.findViewById(com.tokopedia.digital_deals.R.id.tv_view_map);
        tvRecommendedDeals = view.findViewById(com.tokopedia.digital_deals.R.id.tv_recommended_deals);
        clHeader = view.findViewById(com.tokopedia.digital_deals.R.id.cl_header);
        toolbar = view.findViewById(com.tokopedia.digital_deals.R.id.toolbar);
        tvDealDetails = view.findViewById(com.tokopedia.digital_deals.R.id.tv_deal_details);
        viewPager = view.findViewById(com.tokopedia.digital_deals.R.id.deals_images);
        circlePageIndicator = view.findViewById(com.tokopedia.digital_deals.R.id.pager_indicator);
        ((BaseSimpleActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), com.tokopedia.abstraction.R.drawable.ic_action_back));
        buyDealNow = view.findViewById(com.tokopedia.digital_deals.R.id.ll_buynow);
        tvExpandableDesc = view.findViewById(com.tokopedia.digital_deals.R.id.tv_expandable_description);
        seeMoreButtonDesc = view.findViewById(com.tokopedia.digital_deals.R.id.seemorebutton_description);
        clDescription = view.findViewById(com.tokopedia.digital_deals.R.id.cl_description);
        clOutlets = view.findViewById(com.tokopedia.digital_deals.R.id.cl_outlets);
        clTnc = view.findViewById(com.tokopedia.digital_deals.R.id.cl_tnc);
        tvExpandableTC = view.findViewById(com.tokopedia.digital_deals.R.id.tv_expandable_tnc);
        seeMoreButtonTC = view.findViewById(com.tokopedia.digital_deals.R.id.seemorebutton_tnc);
        recyclerViewDeals = view.findViewById(com.tokopedia.digital_deals.R.id.recycler_view);
        circlePageIndicator.setRadius(getResources().getDimension(R.dimen.dp_3));
        collapsingToolbarLayout = view.findViewById(com.tokopedia.digital_deals.R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        appBarLayout = view.findViewById(com.tokopedia.digital_deals.R.id.app_bar_layout);
        mainContent = view.findViewById(com.tokopedia.digital_deals.R.id.main_content);
        baseMainContent = view.findViewById(com.tokopedia.digital_deals.R.id.base_main_content);
        progressBarLayout = view.findViewById(com.tokopedia.digital_deals.R.id.progress_bar_layout);
        cardView = view.findViewById(com.tokopedia.digital_deals.R.id.cv_checkout);
        svDetails = view.findViewById(com.tokopedia.digital_deals.R.id.nestedScroll);
        setCardViewElevation();
        progBar = view.findViewById(com.tokopedia.digital_deals.R.id.prog_bar);
        clRedeemInstuctns = view.findViewById(com.tokopedia.digital_deals.R.id.cl_redeem_instructions);
        dividerDesc = view.findViewById(com.tokopedia.digital_deals.R.id.divider4);
        dividerTnC = view.findViewById(com.tokopedia.digital_deals.R.id.divider5);
        Drawable img = getResources().getDrawable(com.tokopedia.digital_deals.R.drawable.ic_see_location);
        tvViewMap.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        collapsingToolbarLayout.setTitle(" ");
        seeMoreButtonDesc.setOnClickListener(this);
        seeMoreButtonTC.setOnClickListener(this);
        tvAllLocations.setOnClickListener(this);
        buyDealNow.setOnClickListener(this);
        tvViewMap.setOnClickListener(this);
        ivFavourite.setOnClickListener(this);
        clRedeemInstuctns.setOnClickListener(this);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewDeals.setLayoutManager(mLayoutManager);
        dealsAdapter = new DealsCategoryAdapter(null, DealsCategoryAdapter.DETAIL_PAGE, this, IS_SHORT_LAYOUT);
        recyclerViewDeals.setAdapter(dealsAdapter);
        recyclerViewDeals.addOnScrollListener(rvOnScrollListener);
    }

    private void setCardViewElevation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cardView.setCardElevation(getResources().getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_8));
        } else {
            cardView.setCardElevation(getResources().getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0));
        }
    }


    @Override
    protected void initInjector() {
        NetworkClient.init(getActivity());
        getComponent(DealsComponent.class).inject(this);
        mPresenter.attachView(this);
        mPresenter2.attachView(this);
        mPresenter2.initialize();
    }

    @Override
    protected String getScreenName() {
        return SCREEN_NAME;
    }


    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void renderDealDetails(DealsDetailsResponse detailsViewModel) {
        dealsAnalytics.sendScreenNameEvent(getScreenName());
        this.dealDetail = detailsViewModel;
        if (userSession.isLoggedIn()) {
            mPresenter.sendNsqEvent(userSession.getUserId(), detailsViewModel);
            mPresenter.sendNsqTravelEvent(userSession.getUserId(), detailsViewModel);
        }
        collapsingToolbarLayout.setTitle(detailsViewModel.getDisplayName());
        tvDealDetails.setText(detailsViewModel.getDisplayName());
        tvDealDetails.setVisibility(View.VISIBLE);


        if (detailsViewModel.getMrp() != 0 && detailsViewModel.getMrp() != detailsViewModel.getSalesPrice()) {
            tvMrp.setVisibility(View.VISIBLE);
            tvMrp.setText(Utils.convertToCurrencyString(detailsViewModel.getMrp()));
            tvMrp.setPaintFlags(tvMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            tvMrp.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(detailsViewModel.getSavingPercentage())) {
            tvOff.setVisibility(View.GONE);
        } else {
            tvOff.setVisibility(View.VISIBLE);
            tvOff.setText(detailsViewModel.getSavingPercentage());
        }

        tvSalesPrice.setText(Utils.convertToCurrencyString(detailsViewModel.getSalesPrice()));

        tvExpiryDate.setText(String.format(getString(com.tokopedia.digital_deals.R.string.valid_through), Utils.convertEpochToString(detailsViewModel.getSaleEndDate())));


        if (detailsViewModel.getOutlets() != null && detailsViewModel.getOutlets().size() > 0) {
            Outlet outlet = detailsViewModel.getOutlets().get(0);
            if (detailsViewModel.getOutlets().size() == 1) {
                tvAllLocations.setVisibility(View.GONE);
            } else {
                tvAllLocations.setVisibility(View.VISIBLE);
            }
            latLng = outlet.getCoordinates();
            if (latLng != null && latLng != "") {
                tvViewMap.setVisibility(View.VISIBLE);
            } else {
                tvViewMap.setVisibility(View.GONE);
            }
            tvBrandVenue.setText(outlet.getName());
            tvBrandAddress.setText(outlet.getDistrict());
            tvNumberOfLocations.setText(String.format(getString(com.tokopedia.digital_deals.R.string.number_of_items), detailsViewModel.getOutlets().size()));
            tvBrandName.setText(detailsViewModel.getBrand().getTitle());
            ImageHandler.loadImage(getContext(), ivBrandLogo, dealDetail.getBrand().getFeaturedThumbnailImage(), com.tokopedia.unifyprinciples.R.color.Unify_N50, com.tokopedia.unifyprinciples.R.color.Unify_N50);
            if (dealDetail.getBrand().getUrl() != null) {
                ivBrandLogo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent detailsIntent = new Intent(getContext(), BrandDetailsActivity.class);
                        detailsIntent.putExtra(BrandDetailsPresenter.BRAND_DATA, dealDetail.getBrand());
                        getActivity().startActivity(detailsIntent);
                    }
                });
            }

        } else {
            clOutlets.setVisibility(View.GONE);
        }

        if (detailsViewModel.getMediaUrl() != null && detailsViewModel.getMediaUrl().size() > 0) {
            if (detailsViewModel.getMediaUrl().size() == 1)
                circlePageIndicator.setVisibility(View.GONE);
            setViewPagerListener(new SlidingImageAdapterDealDetails(detailsViewModel.getMediaUrl()));
            circlePageIndicator.setViewPager(viewPager);
            mPresenter.startBannerSlide(viewPager);
        } else {

            circlePageIndicator.setVisibility(View.GONE);
            if (detailsViewModel.getImageWeb() != null) {
                List<Media> images = new ArrayList<>();
                Media media = new Media();
                media.setUrl(detailsViewModel.getImageWeb());
                images.add(media);
                setViewPagerListener(new SlidingImageAdapterDealDetails(images));
            }
            circlePageIndicator.setViewPager(viewPager);
            mPresenter.startBannerSlide(viewPager);
        }
        setTnCandDescription();
        seeMoreButtonDesc.setOnClickListener(this);
        seeMoreButtonTC.setOnClickListener(this);
        cardView.setVisibility(View.VISIBLE);
        baseMainContent.setVisibility(View.VISIBLE);

        Date currentTime = Calendar.getInstance().getTime();
        if ((currentTime.getTime())/1000> detailsViewModel.getSaleEndDate())
        {
            buyDealNow.setText(getContext().getResources().getString(com.tokopedia.digital_deals.R.string.deals_disable_buy_now));
            buyDealNow.setClickable(false);
            buyDealNow.setBackgroundColor(getContext().getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_N75));
        } else {
            buyDealNow.setClickable(true);
            buyDealNow.setText(getContext().getResources().getString(com.tokopedia.digital_deals.R.string.buy_now));
            buyDealNow.setBackground(getContext().getResources().getDrawable(com.tokopedia.digital_deals.R.drawable.button_buy_now_background));
        }
        if (detailsViewModel.getBrand() != null)
            dealsAnalytics.sendEcommerceDealDetail(detailsViewModel.getId(), detailsViewModel.getSalesPrice(), detailsViewModel.getDisplayName(), detailsViewModel.getBrand().getTitle());

    }

    private void setTnCandDescription() {
        String text = getExpandableItemText(dealDetail.getLongRichDesc());
        if (TextUtils.isEmpty(text)) {
            clDescription.setVisibility(View.GONE);
        } else {
            tvExpandableDesc.setText(Html.fromHtml(text));
        }
        text = getExpandableItemText(dealDetail.getTnc());
        if (TextUtils.isEmpty(text)) {
            clTnc.setVisibility(View.GONE);
        } else {
            tvExpandableTC.setText(Html.fromHtml(text));
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (tvExpandableTC.getLineCount() >= 10) {
                    seeMoreButtonTC.setVisibility(View.VISIBLE);
                } else {
                    seeMoreButtonTC.setVisibility(View.GONE);
                }
                if (tvExpandableDesc.getLineCount() >= 10) {
                    seeMoreButtonDesc.setVisibility(View.VISIBLE);
                } else {
                    seeMoreButtonDesc.setVisibility(View.GONE);
                }
            }
        }, 100);
    }

    private String getExpandableItemText(String tnc) {
        if (!TextUtils.isEmpty(tnc)) {
            String splitArray[] = tnc.split("~");
            StringBuilder tncBuffer = new StringBuilder();
            for (int i = 0; i < splitArray.length; i++) {
                String line = splitArray[i];
                if (i < splitArray.length - 1)
                    tncBuffer.append(" ").append("\u2022").append("  ").append(line.trim()).append("<br><br>");
                else
                    tncBuffer.append(" ").append("\u2022").append("  ").append(line.trim());
            }
            return tncBuffer.toString();
        } else {
            return null;
        }
    }

    private void setViewPagerListener(SlidingImageAdapterDealDetails adapter) {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                mPresenter.onBannerSlide(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        viewPager.setAdapter(adapter);
    }

    @Override
    public void addDealsToCards(@NotNull ArrayList<ProductItem> productItems) {
        ((DealsCategoryAdapter) Objects.requireNonNull(recyclerViewDeals.getAdapter())).addAll(productItems);
        if (recyclerViewDeals.getAdapter().getItemCount() > 0){
            tvRecommendedDeals.setVisibility(View.VISIBLE);
            recyclerViewDeals.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showProgressBar() {
        progBar.setVisibility(View.VISIBLE);
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progBar.setVisibility(View.GONE);
        progressBarLayout.setVisibility(View.GONE);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(com.tokopedia.digital_deals.R.menu.menu_deal_details, menu);
        mMenu = menu;
        onPrepareOptionsMenu(menu);

    }


    public void onPrepareOptionsMenu(final Menu menu) {
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            MenuItem item = menu.findItem(com.tokopedia.digital_deals.R.id.action_menu_share);

            verticalOffset = Math.abs(verticalOffset);
            verticalOffset = Math.abs(verticalOffset);
            int difference = appBarLayout.getTotalScrollRange() - toolbar.getHeight();
            if (verticalOffset >= difference) {
                if (tvDealDetails.getText() != null) {
                    collapsingToolbarLayout.setTitle(tvDealDetails.getText());
                }
                setDrawableColorFilter(toolbar.getNavigationIcon(), ContextCompat.getColor(getActivity(), com.tokopedia.unifyprinciples.R.color.Unify_N400));
                setDrawableColorFilter(item.getIcon(), ContextCompat.getColor(getActivity(), com.tokopedia.unifyprinciples.R.color.Unify_N400));
            } else {
                collapsingToolbarLayout.setTitle(" ");
                setDrawableColorFilter(toolbar.getNavigationIcon(), ContextCompat.getColor(getActivity(), com.tokopedia.unifyprinciples.R.color.Unify_N0));
                setDrawableColorFilter(item.getIcon(), ContextCompat.getColor(getActivity(), com.tokopedia.unifyprinciples.R.color.Unify_N0));
            }
        });
    }

    public void setDrawableColorFilter(Drawable drawable, int color) {
        if (drawable != null) {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }

    public void startGeneralWebView(String url) {
        RouteManager.route(getActivity(), ApplinkConstInternalGlobal.WEBVIEW, url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return mPresenter.onOptionMenuClick(id);
    }

    @Override
    public void showShareButton() {
        if(mMenu!=null) {
            MenuItem item = mMenu.findItem(com.tokopedia.digital_deals.R.id.action_menu_share);
            item.setVisible(true);
        }
    }

    @Override
    public RequestParams getParams() {
        String url = getArguments().getString(DealDetailsPresenter.HOME_DATA);
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(DealDetailsPresenter.TAG, url);
        Location location = Utils.getSingletonInstance().getLocation(getActivity());
        if (location != null) {
            requestParams.putInt(Utils.QUERY_PARAM_CITY_ID, Utils.getSingletonInstance().getLocation(getActivity()).getId());
        }
        return requestParams;
    }

    @Override
    public void notifyDataSetChanged(int position) {
        setLikes(dealDetail.getLikes(), dealDetail.getIsLiked());
    }

    @SuppressLint("Range")
    @Override
    public void showLoginSnackbar(String message, int position) {
        View rootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        int dimen = (int) getResources().getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_96);
        CustomWidthToaster.showCustomeToaster(dimen, rootView, message,
                getResources().getString(com.tokopedia.digital_deals.R.string.title_activity_login),
                v -> {
                    Intent intent = RouteManager.getIntent(getContext(), ApplinkConst.LOGIN);
                    startActivityForResult(intent, LIKE_REQUEST_CODE);
                });
    }

    @Override
    public void hideCollapsingHeader() {
        clHeader.setVisibility(View.GONE);
    }

    @Override
    public void showCollapsingHeader() {
        showShareButton();
        clHeader.setVisibility(View.VISIBLE);
    }

    @Override
    public void setLikes(int likes, boolean isLiked) {
        dealDetail.setIsLiked(isLiked);
        dealDetail.setLikes(likes);
        if (isLiked) {
            ivFavourite.setImageResource(com.tokopedia.digital_deals.R.drawable.ic_wishlist_filled);
        } else {
            ivFavourite.setImageResource(com.tokopedia.digital_deals.R.drawable.ic_wishlist_unfilled);
        }
        if (likes == 0) {
            tvLikes.setVisibility(View.GONE);
        } else {
            tvLikes.setVisibility(View.VISIBLE);
            tvLikes.setText(String.valueOf(likes));
        }
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    @Override
    public void addFooter() {
        ((DealsCategoryAdapter) recyclerViewDeals.getAdapter()).addFooter();

    }

    @Override
    public void removeFooter() {
        ((DealsCategoryAdapter) recyclerViewDeals.getAdapter()).removeFooter();

    }

    @Override
    public boolean isEnableBuyFromArguments() {
        if (getDealPassData() != null) {
            return getDealPassData().isEnableBuy();
        } else {
            return DEFAULT_PARAM_ENABLE;
        }
    }

    private DealDetailPassData getDealPassData() {
        if (getArguments() != null && getArguments().getParcelable(PARAM_DEAL_PASSDATA) != null) {
            return getArguments().getParcelable(PARAM_DEAL_PASSDATA);
        }
        return null;
    }

    @Override
    public boolean isRecommendationEnableFromArguments() {
        if (getDealPassData() != null) {
            return getDealPassData().isEnableRecommendation();
        } else {
            return DEFAULT_PARAM_ENABLE;
        }
    }

    @Override
    public void hideRecomendationDealsView() {
        recyclerViewDeals.setVisibility(View.GONE);
        tvRecommendedDeals.setVisibility(View.GONE);
    }

    @Override
    public boolean isEnableLikeFromArguments() {
        if (getDealPassData() != null) {
            return getDealPassData().isEnableLike();
        } else {
            return DEFAULT_PARAM_ENABLE;
        }
    }

    @Override
    public void hideLikeButtonView() {
        ivFavourite.setVisibility(View.GONE);
    }

    @Override
    public boolean isEnableShareFromArguments() {
        if (getDealPassData() != null) {
            return getDealPassData().isEnableShare();
        } else {
            return DEFAULT_PARAM_ENABLE;
        }
    }

    @Override
    public void hideCheckoutView() {
        cardView.setVisibility(View.GONE);
        CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(
                CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.MATCH_PARENT
        );
        layoutParams.setBehavior(new AppBarLayout.ScrollingViewBehavior());
        svDetails.setLayoutParams(layoutParams);
        svDetails.setClipToPadding(true);
    }

    @Override
    public View getRootView() {
        return mainContent;
    }

    @Override
    public void onClick(View v) {
        int Id = v.getId();
        if (Id == com.tokopedia.digital_deals.R.id.seemorebutton_description) {
            sendEvent(DealsAnalytics.EVENT_CLICK_CHECK_DESCRIPTION_PRODUCT_DETAIL);
            fragmentCallbacks.replaceFragment(dealDetail.getLongRichDesc(), getString(com.tokopedia.digital_deals.R.string.show_description), 0);
        } else if (Id == com.tokopedia.digital_deals.R.id.seemorebutton_tnc) {
            sendEvent(DealsAnalytics.EVENT_CLICK_CHECK_TNC_PRODUCT_DETAIL);
            fragmentCallbacks.replaceFragment(dealDetail.getTnc(), getString(com.tokopedia.digital_deals.R.string.show_tnc), 0);
        } else if (Id == com.tokopedia.digital_deals.R.id.tv_see_all_locations) {
            sendEvent(DealsAnalytics.EVENT_CLICK_CHECK_LOCATION_PRODUCT_DETAIL);
            fragmentCallbacks.replaceFragment(mPresenter.getAllOutlets(), 0);
        } else if (Id == com.tokopedia.digital_deals.R.id.ll_buynow) {
            dealsAnalytics.sendBuyNowClickEvent(dealDetail, DealsAnalytics.EVENT_CLICK_BELI);
            fragmentCallbacks.replaceFragment(dealDetail, 1);
        } else if (Id == com.tokopedia.digital_deals.R.id.tv_view_map) {
            Utils.getSingletonInstance().openGoogleMapsActivity(getContext(), latLng);
        } else if (v.getId() == com.tokopedia.digital_deals.R.id.iv_wish_list) {
            boolean isLoggedIn = mPresenter2.setDealLike(dealDetail.getId(), dealDetail.getIsLiked(), 0, dealDetail.getLikes());
            if (isLoggedIn) {
                if (dealDetail.getIsLiked()) {
                    setLikes(dealDetail.getLikes() - 1, !dealDetail.getIsLiked());
                } else {
                    setLikes(dealDetail.getLikes() + 1, !dealDetail.getIsLiked());
                }
            }
        } else if (Id == com.tokopedia.digital_deals.R.id.cl_redeem_instructions) {
            sendEvent(DealsAnalytics.EVENT_CLICK_CHECK_REDEEM_INS_PRODUCT_DETAIL);
            if((dealDetail.customText1 & SALAM_VALUE) <= SALAM_INDICATOR){
                startGeneralWebView(DealsUrl.WebUrl.REDEEM_URL);
            } else {
                mPresenter.getEventContent(onSuccessGetEventContent(), onErrorGetEventContent());
            }
        }
    }

    private Function1<? super EventContentData, Unit> onSuccessGetEventContent(){
        return success -> {
            String valueText = success.getEventContentById().getData().getSectionDatas().get(FIRST_VALUE).getContents().get(FIRST_VALUE).getValueText();
            Pattern pattern = Pattern.compile(SALAM_REGEX_PATTERN);
            Matcher matcher = pattern.matcher(valueText);
            if(matcher.find()){
                startGeneralWebView(matcher.group(URL_GROUP));
            }
            return Unit.INSTANCE;
        };
    }

    private Function1<? super Throwable, Unit> onErrorGetEventContent(){
        return error -> showErrorMessage();
    }

    private Unit showErrorMessage(){
        View rootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        Toaster.build(rootView, getResources().getString(R.string.how_to_redeem_error), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show();
        return Unit.INSTANCE;
    }

    void sendEvent(String action) {
        dealsAnalytics.sendEventDealsDigitalClick(action
                , String.format("%s - %s", tvBrandName.getText().toString(), dealDetail.getDisplayName()));

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentCallbacks = (DealDetailsActivity) activity;
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDestroy();
        mPresenter2.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getActivity() == null)
            return;
        if (requestCode == LIKE_REQUEST_CODE) {
            UserSessionInterface userSession = new UserSession(getActivity());
            if (userSession.isLoggedIn()) {
                mPresenter2.setDealLike(dealDetail.getId(), dealDetail.getIsLiked(), 0, dealDetail.getLikes());
                if (dealDetail.getIsLiked()) {
                    setLikes(dealDetail.getLikes() - 1, !dealDetail.getIsLiked());
                } else {
                    setLikes(dealDetail.getLikes() + 1, !dealDetail.getIsLiked());
                }
            }
        }
    }

    private RecyclerView.OnScrollListener rvOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mPresenter.onRecyclerViewScrolled(mLayoutManager);
        }
    };

    @Override
    public void onNavigateToActivityRequest(Intent intent, int requestCode, int position) {
        navigateToActivityRequest(intent, requestCode);
    }

    @Override
    public void onStop() {
        forceRefresh = true;
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (forceRefresh) {
            if (dealsAdapter != null)
                dealsAdapter.notifyDataSetChanged();
            forceRefresh = false;
        }
    }
}
