package com.tokopedia.digital_deals.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.TouchViewPager;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.design.viewpagerindicator.CirclePageIndicator;
import com.tokopedia.digital_deals.DealsModuleRouter;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.view.activity.BrandDetailsActivity;
import com.tokopedia.digital_deals.view.activity.DealDetailsActivity;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.adapter.SlidingImageAdapterDealDetails;
import com.tokopedia.digital_deals.view.contractor.DealCategoryAdapterContract;
import com.tokopedia.digital_deals.view.contractor.DealDetailsContract;
import com.tokopedia.digital_deals.view.customview.ExpandableTextView;
import com.tokopedia.digital_deals.view.model.Media;
import com.tokopedia.digital_deals.view.model.Outlet;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse;
import com.tokopedia.digital_deals.view.presenter.BrandDetailsPresenter;
import com.tokopedia.digital_deals.view.presenter.DealCategoryAdapterPresenter;
import com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter;
import com.tokopedia.digital_deals.view.utils.DealFragmentCallbacks;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DealDetailsFragment extends BaseDaggerFragment implements DealDetailsContract.View, View.OnClickListener, DealCategoryAdapterContract.View, DealsCategoryAdapter.INavigateToActivityRequest {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ExpandableTextView tvExpandableDesc;
    private ExpandableTextView tvExpandableTC;
    private LinearLayout seeMoreButtonDesc;
    private LinearLayout seeMoreButtonTC;

    private AppBarLayout appBarLayout;

    private RecyclerView recyclerViewDeals;
    private TextView tvSeeMoreTextDesc;
    private TextView tvSeeMoreTextTC;
    private ImageView ivArrowSeeMoreDesc;
    private ImageView ivArrowSeeMoreTC;
    @Inject
    public DealDetailsPresenter mPresenter;
    @Inject
    DealCategoryAdapterPresenter mPresenter2;

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
    private TextView buyDealNow;
    private Menu mMenu;
    private ConstraintLayout clHeader;
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

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new DealDetailsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deal_details, container, false);
        setViewIds(view);
        setHasOptionsMenu(true);


        mPresenter.getDealDetails();
        return view;
    }


    private void setViewIds(View view) {
        tvMrp = view.findViewById(R.id.tv_mrp);
        tvSalesPrice = view.findViewById(R.id.tv_sales_price);
        tvOff = view.findViewById(R.id.tv_off);
        ivFavourite = view.findViewById(R.id.iv_wish_list);
        tvLikes = view.findViewById(R.id.tv_favourite);
        tvExpiryDate = view.findViewById(R.id.tv_expiry_date);
        tvNumberOfLocations = view.findViewById(R.id.tv_number_of_locations);
        ivBrandLogo = view.findViewById(R.id.image_view_brand);
        tvAllLocations = view.findViewById(R.id.tv_see_all_locations);
        tvBrandName = view.findViewById(R.id.tv_brand_name);
        tvBrandVenue = view.findViewById(R.id.tv_brand_venue);
        tvBrandAddress = view.findViewById(R.id.tv_brand_address);
        tvViewMap = view.findViewById(R.id.tv_view_map);
        tvRecommendedDeals = view.findViewById(R.id.tv_recommended_deals);
        clHeader = view.findViewById(R.id.cl_header);
        toolbar = view.findViewById(R.id.toolbar);
        tvDealDetails = view.findViewById(R.id.tv_deal_details);
        viewPager = view.findViewById(R.id.deals_images);
        circlePageIndicator = view.findViewById(R.id.pager_indicator);
        ((BaseSimpleActivity) getActivity()).setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_back));

        buyDealNow = view.findViewById(R.id.ll_buynow);
        tvExpandableDesc = view.findViewById(R.id.tv_expandable_description);
        tvSeeMoreTextDesc = view.findViewById(R.id.seemorebutton_description);
        seeMoreButtonDesc = view.findViewById(R.id.expand_view_description);
        ivArrowSeeMoreDesc = view.findViewById(R.id.down_arrow_description);


        tvExpandableTC = view.findViewById(R.id.tv_expandable_tnc);
        tvSeeMoreTextTC = view.findViewById(R.id.seemorebutton_tnc);
        seeMoreButtonTC = view.findViewById(R.id.expand_view_tnc);
        ivArrowSeeMoreTC = view.findViewById(R.id.down_arrow_tnc);

        recyclerViewDeals = view.findViewById(R.id.recycler_view);
        circlePageIndicator.setRadius(getResources().getDimension(R.dimen.dp_3));
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        appBarLayout = view.findViewById(R.id.app_bar_layout);
        mainContent = view.findViewById(R.id.main_content);
        baseMainContent = view.findViewById(R.id.base_main_content);
        progressBarLayout = view.findViewById(R.id.progress_bar_layout);
        cardView = view.findViewById(R.id.cv_checkout);
        setCardViewElevation();
        progBar = view.findViewById(R.id.prog_bar);
        clRedeemInstuctns = view.findViewById(R.id.cl_redeem_instructions);
        Drawable img = getResources().getDrawable(R.drawable.ic_see_location);
        tvViewMap.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        collapsingToolbarLayout.setTitle(" ");
        seeMoreButtonDesc.setOnClickListener(this);
        seeMoreButtonTC.setOnClickListener(this);
        tvAllLocations.setOnClickListener(this);
        buyDealNow.setOnClickListener(this);
        tvViewMap.setOnClickListener(this);
        ivFavourite.setOnClickListener(this);
        clRedeemInstuctns.setOnClickListener(this);
        tvExpandableDesc.setInterpolator(new OvershootInterpolator());
        tvExpandableTC.setInterpolator(new OvershootInterpolator());
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewDeals.setLayoutManager(mLayoutManager);
        recyclerViewDeals.setAdapter(new DealsCategoryAdapter(new ArrayList<ProductItem>(), this, IS_SHORT_LAYOUT));
        recyclerViewDeals.addOnScrollListener(rvOnScrollListener);
    }

    private void setCardViewElevation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cardView.setCardElevation(getResources().getDimension(R.dimen.dp_8));
        } else {
            cardView.setCardElevation(getResources().getDimension(R.dimen.dp_0));
        }
    }


    @Override
    protected void initInjector() {
        getComponent(DealsComponent.class).inject(this);
        mPresenter.attachView(this);
        mPresenter2.attachView(this);
        mPresenter2.initialize();
    }

    @Override
    protected String getScreenName() {
        return null;
    }


    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void renderDealDetails(DealsDetailsResponse detailsViewModel) {
        this.dealDetail = detailsViewModel;
        collapsingToolbarLayout.setTitle(detailsViewModel.getDisplayName());
        tvDealDetails.setText(detailsViewModel.getDisplayName());
        tvDealDetails.setVisibility(View.VISIBLE);


        if (detailsViewModel.getMrp() != 0) {
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

        tvExpiryDate.setText(String.format(getString(R.string.valid_through), Utils.convertEpochToString(detailsViewModel.getSaleEndDate())));


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
            tvNumberOfLocations.setText(String.format(getString(R.string.number_of_items), detailsViewModel.getOutlets().size()));
            tvBrandName.setText(detailsViewModel.getBrand().getTitle());
            ImageHandler.loadImage(getContext(), ivBrandLogo, dealDetail.getBrand().getFeaturedThumbnailImage(), R.color.grey_1100, R.color.grey_1100);
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


        tvExpandableDesc.setText(detailsViewModel.getLongRichDesc());
        setExpandableItemText(detailsViewModel.getTnc());

        seeMoreButtonDesc.setOnClickListener(this);
        seeMoreButtonTC.setOnClickListener(this);
        tvExpandableDesc.setInterpolator(new OvershootInterpolator());
        tvExpandableTC.setInterpolator(new OvershootInterpolator());

        cardView.setVisibility(View.VISIBLE);
        baseMainContent.setVisibility(View.VISIBLE);

    }

    private void setExpandableItemText(String tnc) {
        if (tnc != null) {
            String splitArray[] = tnc.split("~");
            StringBuilder tncBuffer = new StringBuilder();


            for (int i = 0; i < splitArray.length; i++) {
                String line = splitArray[i];
                tncBuffer.append((i + 1) + ". <i>").append(line).append("</i>");
                if (i != splitArray.length - 1)
                    tncBuffer.append("<br>");

            }

            tvExpandableTC.setText(Html.fromHtml(tncBuffer.toString()));
        } else {
            tvExpandableTC.setText("");
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
    public void addDealsToCards(List<ProductItem> productItems) {
        ((DealsCategoryAdapter) recyclerViewDeals.getAdapter()).addAll(productItems);
        if (((DealsCategoryAdapter) recyclerViewDeals.getAdapter()).getItemCount() > 0)
            tvRecommendedDeals.setVisibility(View.VISIBLE);
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

        inflater.inflate(R.menu.menu_deal_details, menu);
        mMenu = menu;
        onPrepareOptionsMenu(menu);

    }


    public void onPrepareOptionsMenu(final Menu menu) {
        hideShareButton();
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            MenuItem item = menu.findItem(R.id.action_menu_share);

            verticalOffset = Math.abs(verticalOffset);
            int difference = appBarLayout.getTotalScrollRange() - toolbar.getHeight();
            if (verticalOffset >= difference) {
                if (tvDealDetails.getText() != null) {
                    collapsingToolbarLayout.setTitle(tvDealDetails.getText());
                }
                setDrawableColorFilter(toolbar.getNavigationIcon(), ContextCompat.getColor(getActivity(), R.color.tkpd_dark_gray_toolbar));
                setDrawableColorFilter(item.getIcon(), ContextCompat.getColor(getActivity(), R.color.tkpd_dark_gray_toolbar));
            } else {
                collapsingToolbarLayout.setTitle(" ");
                setDrawableColorFilter(toolbar.getNavigationIcon(), ContextCompat.getColor(getActivity(), R.color.white));
                setDrawableColorFilter(item.getIcon(), ContextCompat.getColor(getActivity(), R.color.white));
            }
        });
    }

    public void setDrawableColorFilter(Drawable drawable, int color) {
        if (drawable != null) {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }

    public void startGeneralWebView(String url) {

        ((DealsModuleRouter) getActivity().getApplication())
                .actionOpenGeneralWebView(getActivity(), url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return mPresenter.onOptionMenuClick(id);
    }

    @Override
    public void hideShareButton() {
        MenuItem item = mMenu.findItem(R.id.action_menu_share);
        item.setVisible(false);
    }

    @Override
    public void showShareButton() {
        MenuItem item = mMenu.findItem(R.id.action_menu_share);
        item.setVisible(true);
    }

    @Override
    public RequestParams getParams() {
        String url = getArguments().getString(DealDetailsPresenter.HOME_DATA);
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(DealDetailsPresenter.TAG, url);
        return requestParams;
    }

    @Override
    public void notifyDataSetChanged(int position) {
        setLikes(dealDetail.getLikes(), dealDetail.getIsLiked());
    }

    @Override
    public void showLoginSnackbar(String message) {

        SnackbarManager.make(getActivity(), message, Snackbar.LENGTH_LONG).setAction(
                getResources().getString(R.string.title_activity_login), (View.OnClickListener) v -> {
                    Intent intent = ((DealsModuleRouter) getActivity().getApplication()).
                            getLoginIntent(getActivity());
                    getActivity().startActivityForResult(intent, LIKE_REQUEST_CODE);
                }
        ).show();

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
            ivFavourite.setBackgroundResource(R.drawable.ic_wishlist_filled);
        } else {
            ivFavourite.setBackgroundResource(R.drawable.ic_wishlist_unfilled);
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
    public View getRootView() {
        return mainContent;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.expand_view_description) {
            if (tvExpandableDesc.isExpanded()) {
                tvSeeMoreTextDesc.setText(R.string.expand);
                ivArrowSeeMoreDesc.animate().rotation(0f);

            } else {
                tvSeeMoreTextDesc.setText(R.string.collapse);
                ivArrowSeeMoreDesc.animate().rotation(180f);

            }
            tvExpandableDesc.toggle();
        } else if (v.getId() == R.id.expand_view_tnc) {
            if (tvExpandableTC.isExpanded()) {
                tvSeeMoreTextTC.setText(R.string.expand);
                ivArrowSeeMoreTC.animate().rotation(0f);

            } else {
                tvSeeMoreTextTC.setText(R.string.collapse);
                ivArrowSeeMoreTC.animate().rotation(180f);

            }
            tvExpandableTC.toggle();
        } else if (v.getId() == R.id.tv_see_all_locations) {
            fragmentCallbacks.replaceFragment(mPresenter.getAllOutlets(), 0);
        } else if (v.getId() == R.id.ll_buynow) {
            fragmentCallbacks.replaceFragment(dealDetail, 1);
        } else if (v.getId() == R.id.tv_view_map) {
            Utils.getSingletonInstance().openGoogleMapsActivity(getContext(), latLng);
        } else if (v.getId() == R.id.iv_wish_list) {
            boolean isLoggedIn = mPresenter2.setDealLike(dealDetail, 0);
            if (isLoggedIn) {
                if (dealDetail.getIsLiked()) {
                    setLikes(dealDetail.getLikes() - 1, !dealDetail.getIsLiked());
                } else {
                    setLikes(dealDetail.getLikes() + 1, !dealDetail.getIsLiked());
                }
            }
        } else if (v.getId() == R.id.cl_redeem_instructions) {
            startGeneralWebView(DealsUrl.WebUrl.REDEEM_URL);

        }
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
        if (requestCode == LIKE_REQUEST_CODE) {
            UserSession userSession = ((AbstractionRouter) getActivity().getApplication()).getSession();
            if (userSession.isLoggedIn()) {
                mPresenter2.setDealLike(dealDetail, 0);
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
    public void onNavigateToActivityRequest(Intent intent, int requestCode) {
        navigateToActivityRequest(intent, requestCode);
    }
}
