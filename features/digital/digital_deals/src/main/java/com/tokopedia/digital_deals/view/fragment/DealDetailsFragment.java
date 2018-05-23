package com.tokopedia.digital_deals.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DaggerDealsComponent;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.di.DealsModule;
import com.tokopedia.digital_deals.view.activity.DealDetailsActivity;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.contractor.DealDetailsContract;
import com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter;
import com.tokopedia.digital_deals.view.utils.DealFragmentCallbacks;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.viewmodel.BrandViewModel;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.DealsDetailsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.OutletViewModel;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

public class DealDetailsFragment extends BaseDaggerFragment implements DealDetailsContract.View, View.OnClickListener {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private at.blogc.android.views.ExpandableTextView tvExpandableDesc;
    private at.blogc.android.views.ExpandableTextView tvExpandableTC;
    private LinearLayout seeMoreButtonDesc;
    private LinearLayout seeMoreButtonTC;

    private AppBarLayout appBarLayout;

    private RecyclerView recyclerViewDeals;
    private TextView seemorebuttonTextDesc;
    private TextView seemorebuttonTextTC;
    private ImageView ivArrowSeeMoreDesc;
    private ImageView ivArrowSeeMoreTC;
    private DealsComponent mdealsComponent;
    @Inject
    public DealDetailsPresenter mPresenter;
    private DealsCategoryAdapter categoryAdapter;

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
    private TextView brandAddress;
    private TextView tvViewMap;
    private ImageView dealImage;
    private TextView tvOff;
    private ImageView brandLogo;
    private BrandViewModel brandViewModel;
    private LinearLayout buyDealNow;
    private Menu mMenu;
    private ConstraintLayout clHeader;
    private CardView cardView;
    private Toolbar toolbar;
    private DealFragmentCallbacks fragmentCallbacks;
    private CategoryItemsViewModel itemsViewModel;

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new DealDetailsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_deals_details, container, false);
        setViewIds(view);
        setHasOptionsMenu(true);


        mPresenter.getDealDetails();
        return view;
    }


    private void setViewIds(View view) {
        tvMrp = view.findViewById(R.id.tvMrp);
        tvSalesPrice = view.findViewById(R.id.tvSalesPrice);
        tvOff = view.findViewById(R.id.tv_off);
        ivFavourite = view.findViewById(R.id.iv_wish_list);
        tvLikes = view.findViewById(R.id.tv_favourite);
        tvExpiryDate = view.findViewById(R.id.tv_expiryDate);
        tvNumberOfLocations = view.findViewById(R.id.tv_number_of_locations);
        brandLogo = view.findViewById(R.id.imageViewBrand);
        tvAllLocations = view.findViewById(R.id.tv_findalllocations);
        tvBrandName = view.findViewById(R.id.tv_brandName);
        tvBrandVenue = view.findViewById(R.id.tv_brandVenue);
        brandAddress = view.findViewById(R.id.tv_brandAddress);
        tvViewMap = view.findViewById(R.id.tv_view_map);
        clHeader = view.findViewById(R.id.cl_header);
        toolbar = view.findViewById(R.id.toolbar);

        ((BaseSimpleActivity) getActivity()).setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_back));

        dealImage=view.findViewById(R.id.deal_image);
        buyDealNow=view.findViewById(R.id.ll_buynow);
        tvExpandableDesc = view.findViewById(R.id.tv_expandable_description);
        seemorebuttonTextDesc = view.findViewById(R.id.seemorebutton_description);
        seeMoreButtonDesc = view.findViewById(R.id.expand_view_description);
        ivArrowSeeMoreDesc = view.findViewById(R.id.down_arrow_description);


        tvExpandableTC = view.findViewById(R.id.tv_expandable_tnc);
        seemorebuttonTextTC = view.findViewById(R.id.seemorebutton_tnc);
        seeMoreButtonTC = view.findViewById(R.id.expand_view_tnc);
        ivArrowSeeMoreTC = view.findViewById(R.id.down_arrow_tnc);

        recyclerViewDeals = view.findViewById(R.id.recyclerView);


        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        appBarLayout = view.findViewById(R.id.app_bar_layout);

        mainContent = view.findViewById(R.id.main_content);
        baseMainContent = view.findViewById(R.id.base_main_content);
        progressBarLayout = view.findViewById(R.id.progress_bar_layout);
        cardView = view.findViewById(R.id.cv_checkout);
        progBar = view.findViewById(R.id.prog_bar);

        collapsingToolbarLayout.setTitle(" ");
        seeMoreButtonDesc.setOnClickListener(this);
        seeMoreButtonTC.setOnClickListener(this);
        tvAllLocations.setOnClickListener(this);
        buyDealNow.setOnClickListener(this);
        tvExpandableDesc.setInterpolator(new OvershootInterpolator());
        tvExpandableTC.setInterpolator(new OvershootInterpolator());

    }


    @Override
    protected void initInjector() {
        DaggerDealsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .dealsModule(new DealsModule(getContext()))
                .build().inject(this);
        mPresenter.attachView(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }


    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {

    }

    @Override
    public void renderDealDetails(DealsDetailsViewModel detailsViewModel) {
        collapsingToolbarLayout.setTitle(detailsViewModel.getDisplayName());

        ImageHandler.loadImage(getContext(), dealImage, detailsViewModel.getImageWeb(), R.color.grey_1100, R.color.grey_1100);

        tvMrp.setText(Utils.convertToCurrencyString(detailsViewModel.getMrp()));

        tvSalesPrice.setText(Utils.convertToCurrencyString(detailsViewModel.getSalesPrice()));
        tvOff.setText(detailsViewModel.getSavingPercentage());

        tvLikes.setText(String.valueOf(detailsViewModel.getLikes()));
        tvExpiryDate.setText(String.format(getString(R.string.valid_through), Utils.convertEpochToString(detailsViewModel.getSaleEndDate())));

        if (detailsViewModel.getOutlets() != null && detailsViewModel.getOutlets().size() != 0) {
            OutletViewModel outletViewModel = detailsViewModel.getOutlets().get(0);
            tvBrandVenue.setText(outletViewModel.getName());
            brandAddress.setText(outletViewModel.getDistrict());
            tvNumberOfLocations.setText(String.format(getString(R.string.number_of_items), detailsViewModel.getOutlets().size()));
            tvBrandName.setText(brandViewModel.getTitle());
            ImageHandler.loadImage(getContext(), brandLogo, brandViewModel.getFeaturedThumbnailImage(), R.color.grey_1100, R.color.grey_1100);

        }

//        tvViewMap

        tvExpandableDesc.setText(detailsViewModel.getLongRichDesc());


//        tvExpandableTC.setText();


        seeMoreButtonDesc.setOnClickListener(this);
        seeMoreButtonTC.setOnClickListener(this);
        tvExpandableDesc.setInterpolator(new OvershootInterpolator());
        tvExpandableTC.setInterpolator(new OvershootInterpolator());

//        categoryAdapter = new DealsCategoryAdapter(getActivity(), categoryItemsViewModels);


//        recyclerViewDeals.setAdapter(categoryAdapter);
        cardView.setVisibility(View.VISIBLE);
        baseMainContent.setVisibility(View.VISIBLE);

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

        Log.d("onCreateOptions1", "aagye");
        inflater.inflate(R.menu.menu_brand_details, menu);
        mMenu = menu;
        onPrepareOptionsMenu(menu);

        Log.d("onCreateOptions2", "aagye");

    }


    public void onPrepareOptionsMenu(final Menu menu) {
        hideShareButton();
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                MenuItem item = menu.findItem(R.id.action_menu_share);

                verticalOffset = Math.abs(verticalOffset);
                int difference = appBarLayout.getTotalScrollRange() - toolbar.getHeight();
                if (verticalOffset >= difference) {
                    DrawableCompat.setTint(toolbar.getNavigationIcon(), ContextCompat.getColor(getActivity(), R.color.tkpd_dark_gray_toolbar));
                    DrawableCompat.setTint(item.getIcon(), ContextCompat.getColor(getActivity(), R.color.tkpd_dark_gray_toolbar));
                } else {
                    DrawableCompat.setTint(toolbar.getNavigationIcon(), ContextCompat.getColor(getActivity(), R.color.white));
                    DrawableCompat.setTint(item.getIcon(), ContextCompat.getColor(getActivity(), R.color.white));
                }
            }
        });
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
        item.setEnabled(false);
    }

    @Override
    public void showShareButton() {
        Log.d("onCreateOptions3", "aagye");
        MenuItem item = mMenu.findItem(R.id.action_menu_share);
        item.setVisible(true);
        item.setEnabled(true);
    }

    @Override
    public RequestParams getParams() {
        itemsViewModel = getArguments().getParcelable(DealDetailsPresenter.HOME_DATA);
        brandViewModel = itemsViewModel.getBrand();
        RequestParams requestParams = RequestParams.create();
        Log.d("Myurllll", " " + itemsViewModel.getUrl());
        requestParams.putString(mPresenter.TAG, itemsViewModel.getUrl());
        return requestParams;
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
    public View getRootView() {
        return mainContent;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.expand_view_description) {
            if (tvExpandableDesc.isExpanded()) {
                seemorebuttonTextDesc.setText(R.string.expand);
                ivArrowSeeMoreDesc.animate().rotation(0f);

            } else {
                seemorebuttonTextDesc.setText(R.string.collapse);
                ivArrowSeeMoreDesc.animate().rotation(180f);

            }
            tvExpandableDesc.toggle();
        } else if (v.getId() == R.id.expand_view_tnc) {
            if (tvExpandableTC.isExpanded()) {
                seemorebuttonTextTC.setText(R.string.expand);
                ivArrowSeeMoreTC.animate().rotation(0f);

            } else {
                seemorebuttonTextTC.setText(R.string.collapse);
                ivArrowSeeMoreTC.animate().rotation(180f);

            }
            tvExpandableTC.toggle();
        } else if (v.getId() == R.id.tv_findalllocations) {
            Log.d("insidebutton click", "true");
            fragmentCallbacks.replaceFragment(mPresenter.getAllOutlets(), 0);
        } else if(v.getId() == R.id.ll_buynow){
            fragmentCallbacks.replaceFragment(itemsViewModel, 1);

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentCallbacks = (DealDetailsActivity)activity;
    }
}
