package com.tokopedia.tokopoints.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.TokopointRouter;
import com.tokopedia.tokopoints.view.model.section.ImageList;
import com.tokopedia.tokopoints.view.model.section.SectionContent;
import com.tokopedia.tokopoints.view.presenter.TokoPointsHomePresenterNew;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.List;

public class ExploreSectionPagerAdapter extends PagerAdapter {
    public static final int TAB_EXPLORE = 0;
    public static final int TAB_MY_COUPON = 1;
    private LayoutInflater mLayoutInflater;
    private List<SectionContent> mSections;
    private SectionContent mCouponSection;
    private TokoPointsHomePresenterNew mPresenter;
    private SwipeToRefresh swipeToRefresh[] = new SwipeToRefresh[2];

    public ExploreSectionPagerAdapter(Context context, TokoPointsHomePresenterNew presenter, List<SectionContent> sections, SectionContent couponSection) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mSections = sections;
        this.mCouponSection = couponSection;
        this.mPresenter = presenter;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null;
        if (position == TAB_EXPLORE) {
            view = mLayoutInflater.inflate(R.layout.tp_view_section_explore, container, false);
            setUpExploreTab(view);
        } else if (position == TAB_MY_COUPON) {
            view = mLayoutInflater.inflate(R.layout.tp_layout_promos_list_container, container, false);
            setTabMyCoupon(view);
        }

        view.setTag(position);
        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return CommonConstant.HOMEPAGE_TAB_COUNT;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    public void setRefreshing(boolean refresh) {
        for (SwipeToRefresh swipeToRefrsh : swipeToRefresh) {
            if (swipeToRefrsh != null)
                swipeToRefrsh.setRefreshing(refresh);

        }
    }

    void setTabMyCoupon(View view) {
        if (view == null) {
            return;
        }

        ViewFlipper containerInner = view.findViewById(R.id.container);

        if (mCouponSection == null
                || mCouponSection.getLayoutCouponAttr() == null
                || mCouponSection.getLayoutCouponAttr().getCouponList() == null
                || mCouponSection.getLayoutCouponAttr().getCouponList().isEmpty()) {
            //TODO ask from Gulfikar for empty message
            containerInner.setDisplayedChild(1);
            ((ImageView) view.findViewById(R.id.img_error2)).setImageResource(R.drawable.ic_tp_empty_pages);
//            ((TextView) view.findViewById(R.id.text_title_error2)).setText(mEmptyMessages.get(CommonConstant.CouponMapKeys.TITLE));
//            ((TextView) view.findViewById(R.id.text_label_error2)).setText(mEmptyMessages.get(CommonConstant.CouponMapKeys.SUB_TITLE));
            view.findViewById(R.id.button_continue).setVisibility(View.VISIBLE);
            view.findViewById(R.id.button_continue).setOnClickListener(view12 -> mPresenter.getView().gotoCatalog());
            view.findViewById(R.id.text_empty_action).setOnClickListener(v ->
                    mPresenter.getView().openWebView(CommonConstant.WebLink.INFO));
        }


        containerInner.setDisplayedChild(0);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_promos);
        recyclerView.addItemDecoration(new SpacesItemDecoration(view.getResources().getDimensionPixelOffset(R.dimen.dp_14),
                view.getResources().getDimensionPixelOffset(R.dimen.dp_16),
                view.getResources().getDimensionPixelOffset(R.dimen.dp_16)));
        recyclerView.setAdapter(new CouponListAdapter(mPresenter, mCouponSection.getLayoutCouponAttr().getCouponList(), true));

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.findViewById(R.id.view_dummy).getLayoutParams();     //margin for bottom view
        params.height = view.getResources().getDimensionPixelSize(R.dimen.tp_margin_bottom_egg);

        view.findViewById(R.id.text_link_first).setOnClickListener(v -> {
            mPresenter.getView().gotoCoupons();

            AnalyticsTrackerUtil.sendEvent(view.getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_LIHAT_SEMUA,
                    "");
        });
    }

    void setUpExploreTab(View view) {
        //TODO setup other section
        if (view == null || mSections == null) {
            return;
        }

        LinearLayout container = view.findViewById(R.id.ll_sections);

        for (SectionContent sectionContent : mSections) {
            if (sectionContent == null) {
                continue;
            }

            if (sectionContent.getLayoutCatalogAttr() != null
                    && sectionContent.getLayoutCatalogAttr().getCatalogList() != null
                    && !sectionContent.getLayoutCatalogAttr().getCatalogList().isEmpty()) {
                container.addView(getCatalogCarousel(sectionContent));
                continue;
            }

            if (sectionContent.getLayoutBannerAttr() == null
                    || sectionContent.getLayoutBannerAttr().getBannerType() == null) {
                continue;
            }

            switch (sectionContent.getLayoutBannerAttr().getBannerType()) {
                case CommonConstant.BannerType.BANNER_1_1:
                    container.addView(getBanner1on1(sectionContent));
                    break;
                case CommonConstant.BannerType.BANNER_2_1:
                    container.addView(getBanner2on1(sectionContent));
                    break;
                case CommonConstant.BannerType.BANNER_3_1:
                    container.addView(getBanner3on1(sectionContent));
                    break;
                case CommonConstant.BannerType.COLUMN_3_1_BY_1:
                    container.addView(getCaulmn311(sectionContent));
                    break;
                case CommonConstant.BannerType.COLUMN_2_1_BY_1:
                    container.addView(getCaulmn211(sectionContent));
                    break;
                case CommonConstant.BannerType.COLUMN_2_3_BY_4:
                    container.addView(getCaulmn234(sectionContent));
                    break;
                case CommonConstant.BannerType.CAROUSEL_1_1:
                    container.addView(getCarousel1on1(sectionContent));
                    break;
                case CommonConstant.BannerType.CAROUSEL_2_1:
                    container.addView(getCarousel2on1(sectionContent));
                    break;
                case CommonConstant.BannerType.CAROUSEL_3_1:
                    container.addView(getCarousel3on1(sectionContent));
                    break;
                default:
                    break;

            }
        }
    }

    View getCatalogCarousel(SectionContent content) {
        View view = mLayoutInflater.inflate(R.layout.tp_layout_generic_carousal, null, false);

        if (content == null ||
                content.getSectionTitle() == null ||
                content.getLayoutCatalogAttr() == null) {
            view.setVisibility(View.GONE);
            return view;
        }

        if (!content.getCta().isEmpty()) {
            TextView btnSeeAll = view.findViewById(R.id.text_see_all);
            btnSeeAll.setVisibility(View.VISIBLE);
            btnSeeAll.setText(content.getCta().getText());
            btnSeeAll.setOnClickListener(v -> handledClick(content.getCta().getAppLink(), content.getCta().getUrl()));
        }

        ((TextView) view.findViewById(R.id.text_title)).setText(content.getSectionTitle());
        ((TextView) view.findViewById(R.id.text_sub_title)).setText(content.getSectionSubTitle());

        if (content.getLayoutCatalogAttr().getCatalogList() != null) {
            RecyclerView rvCarousel = view.findViewById(R.id.rv_carousel);
            rvCarousel.addItemDecoration(new CarouselItemDecoration(mLayoutInflater.getContext().getResources().getDimensionPixelSize(R.dimen.dp_4)));
            rvCarousel.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
            CatalogListCarouselAdapter adapter = new CatalogListCarouselAdapter(mPresenter, content.getLayoutCatalogAttr().getCatalogList(), false);
            rvCarousel.setAdapter(adapter);
        }

        return view;
    }

    View getBanner1on1(SectionContent content) {
        View view = mLayoutInflater.inflate(R.layout.tp_layout_banner_1_on_1, null, false);

        if (content == null ||
                content.getSectionTitle() == null ||
                content.getLayoutBannerAttr() == null) {
            view.setVisibility(View.GONE);
            return view;
        }

        if (!content.getCta().isEmpty()) {
            TextView btnSeeAll = view.findViewById(R.id.text_see_all);
            btnSeeAll.setVisibility(View.VISIBLE);
            btnSeeAll.setText(content.getCta().getText());
            btnSeeAll.setOnClickListener(v -> handledClick(content.getCta().getAppLink(), content.getCta().getUrl()));
        }

        ((TextView) view.findViewById(R.id.text_title)).setText(content.getSectionTitle());
        ((TextView) view.findViewById(R.id.text_sub_title)).setText(content.getSectionSubTitle());

        if (content.getLayoutBannerAttr().getImageList() != null
                && !content.getLayoutBannerAttr().getImageList().isEmpty()
                && URLUtil.isValidUrl(content.getLayoutBannerAttr().getImageList().get(0).getImageURLMobile())) {
            ImageView imgBanner = view.findViewById(R.id.img_banner);
            ImageList data = content.getLayoutBannerAttr().getImageList().get(0);
            ImageHandler.loadImageFitCenter(imgBanner.getContext(), imgBanner, data.getImageURLMobile());
            imgBanner.setOnClickListener(v -> handledClick(data.getRedirectAppLink(), data.getRedirectURL()));

            ((TextView) view.findViewById(R.id.text_title_banner)).setText(data.getInBannerTitle());
            ((TextView) view.findViewById(R.id.text_sub_title_banner)).setText(data.getInBannerSubTitle());
        }

        return view;
    }

    View getBanner2on1(SectionContent content) {
        View view = mLayoutInflater.inflate(R.layout.tp_layout_banner_2_on_1, null, false);

        if (content == null ||
                content.getSectionTitle() == null ||
                content.getLayoutBannerAttr() == null) {
            view.setVisibility(View.GONE);
            return view;
        }

        if (!content.getCta().isEmpty()) {
            TextView btnSeeAll = view.findViewById(R.id.text_see_all);
            btnSeeAll.setVisibility(View.VISIBLE);
            btnSeeAll.setText(content.getCta().getText());
            btnSeeAll.setOnClickListener(v -> handledClick(content.getCta().getAppLink(), content.getCta().getUrl()));
        }

        ((TextView) view.findViewById(R.id.text_title)).setText(content.getSectionTitle());
        ((TextView) view.findViewById(R.id.text_sub_title)).setText(content.getSectionTitle());

        if (content.getLayoutBannerAttr().getImageList() != null
                && !content.getLayoutBannerAttr().getImageList().isEmpty()
                && URLUtil.isValidUrl(content.getLayoutBannerAttr().getImageList().get(0).getImageURLMobile())) {
            ImageView imgBanner = view.findViewById(R.id.img_banner);
            ImageList data = content.getLayoutBannerAttr().getImageList().get(0);
            ImageHandler.loadImageFitCenter(imgBanner.getContext(), imgBanner, data.getImageURLMobile());
            imgBanner.setOnClickListener(v -> handledClick(data.getRedirectAppLink(), data.getRedirectURL()));

            ((TextView) view.findViewById(R.id.text_title_banner)).setText(data.getInBannerTitle());
            ((TextView) view.findViewById(R.id.text_sub_title_banner)).setText(data.getInBannerSubTitle());
        }

        return view;
    }

    View getBanner3on1(SectionContent content) {
        View view = mLayoutInflater.inflate(R.layout.tp_layout_banner_3_on_1, null, false);

        if (content == null ||
                content.getSectionTitle() == null ||
                content.getLayoutBannerAttr() == null) {
            view.setVisibility(View.GONE);
            return view;
        }

        if (!content.getCta().isEmpty()) {
            TextView btnSeeAll = view.findViewById(R.id.text_see_all);
            btnSeeAll.setVisibility(View.VISIBLE);
            btnSeeAll.setText(content.getCta().getText());
            btnSeeAll.setOnClickListener(v -> handledClick(content.getCta().getAppLink(), content.getCta().getUrl()));
        }

        ((TextView) view.findViewById(R.id.text_title)).setText(content.getSectionTitle());
        ((TextView) view.findViewById(R.id.text_sub_title)).setText(content.getSectionSubTitle());

        if (content.getLayoutBannerAttr().getImageList() != null
                && !content.getLayoutBannerAttr().getImageList().isEmpty()
                && URLUtil.isValidUrl(content.getLayoutBannerAttr().getImageList().get(0).getImageURLMobile())) {
            ImageView imgBanner = view.findViewById(R.id.img_banner);
            ImageList data = content.getLayoutBannerAttr().getImageList().get(0);
            ImageHandler.loadImageFitCenter(imgBanner.getContext(), imgBanner, data.getImageURLMobile());
            imgBanner.setOnClickListener(v -> handledClick(data.getRedirectAppLink(), data.getRedirectURL()));

            ((TextView) view.findViewById(R.id.text_title_banner)).setText(data.getInBannerTitle());
            ((TextView) view.findViewById(R.id.text_sub_title_banner)).setText(data.getInBannerSubTitle());
        }

        return view;
    }

    View getCaulmn311(SectionContent content) {
        View view = mLayoutInflater.inflate(R.layout.tp_layout_column_1_on_1, null, false);

        if (content == null ||
                content.getSectionTitle() == null ||
                content.getLayoutBannerAttr() == null) {
            view.setVisibility(View.GONE);
            return view;
        }

        if (!content.getCta().isEmpty()) {
            TextView btnSeeAll = view.findViewById(R.id.text_see_all);
            btnSeeAll.setVisibility(View.VISIBLE);
            btnSeeAll.setText(content.getCta().getText());
            btnSeeAll.setOnClickListener(v -> handledClick(content.getCta().getAppLink(), content.getCta().getUrl()));
        }

        ((TextView) view.findViewById(R.id.text_title)).setText(content.getSectionTitle());
        ((TextView) view.findViewById(R.id.text_sub_title)).setText(content.getSectionSubTitle());

        if (content.getLayoutBannerAttr().getImageList() != null) {
            ImageList data = null;
            ImageView imgCol = null;

            if (content.getLayoutBannerAttr().getImageList().size() > 0) {
                data = content.getLayoutBannerAttr().getImageList().get(0);
                final String appLink = data.getRedirectAppLink();
                final String webLink = data.getRedirectURL();
                imgCol = view.findViewById(R.id.iv_col_1);
                ImageHandler.loadImageFitCenter(imgCol.getContext(), imgCol, data.getImageURLMobile());
                imgCol.setOnClickListener(v -> handledClick(appLink, webLink));

                ((TextView) view.findViewById(R.id.text_title_banner)).setText(data.getInBannerTitle());
                ((TextView) view.findViewById(R.id.text_sub_title_banner)).setText(data.getInBannerSubTitle());
            }

            if (content.getLayoutBannerAttr().getImageList().size() > 1) {
                data = content.getLayoutBannerAttr().getImageList().get(1);
                final String appLink = data.getRedirectAppLink();
                final String webLink = data.getRedirectURL();
                imgCol = view.findViewById(R.id.iv_col_2);
                ImageHandler.loadImageFitCenter(imgCol.getContext(), imgCol, data.getImageURLMobile());
                imgCol.setOnClickListener(v -> handledClick(appLink, webLink));

                ((TextView) view.findViewById(R.id.text_title_banner2)).setText(data.getInBannerTitle());
                ((TextView) view.findViewById(R.id.text_sub_title_banner2)).setText(data.getInBannerSubTitle());
            }

            if (content.getLayoutBannerAttr().getImageList().size() > 2) {
                data = content.getLayoutBannerAttr().getImageList().get(2);
                final String appLink = data.getRedirectAppLink();
                final String webLink = data.getRedirectURL();
                imgCol = view.findViewById(R.id.iv_col_3);
                ImageHandler.loadImageFitCenter(imgCol.getContext(), imgCol, data.getImageURLMobile());
                imgCol.setOnClickListener(v -> handledClick(appLink, webLink));

                ((TextView) view.findViewById(R.id.text_title_banner3)).setText(data.getInBannerTitle());
                ((TextView) view.findViewById(R.id.text_sub_title_banner3)).setText(data.getInBannerSubTitle());
            }

        }

        return view;
    }

    View getCaulmn211(SectionContent content) {
        View view = mLayoutInflater.inflate(R.layout.tp_layout_column_2_on_1, null, false);

        if (content == null ||
                content.getSectionTitle() == null ||
                content.getLayoutBannerAttr() == null) {
            view.setVisibility(View.GONE);
            return view;
        }

        if (!content.getCta().isEmpty()) {
            TextView btnSeeAll = view.findViewById(R.id.text_see_all);
            btnSeeAll.setVisibility(View.VISIBLE);
            btnSeeAll.setText(content.getCta().getText());
            btnSeeAll.setOnClickListener(v -> handledClick(content.getCta().getAppLink(), content.getCta().getUrl()));
        }

        ((TextView) view.findViewById(R.id.text_title)).setText(content.getSectionTitle());
        ((TextView) view.findViewById(R.id.text_sub_title)).setText(content.getSectionSubTitle());

        if (content.getLayoutBannerAttr().getImageList() != null) {
            ImageList data = null;
            ImageView imgCol = null;

            if (content.getLayoutBannerAttr().getImageList().size() > 0) {
                data = content.getLayoutBannerAttr().getImageList().get(0);
                final String appLink = data.getRedirectAppLink();
                final String webLink = data.getRedirectURL();
                imgCol = view.findViewById(R.id.iv_col_1);
                ImageHandler.loadImageFitCenter(imgCol.getContext(), imgCol, data.getImageURLMobile());
                imgCol.setOnClickListener(v -> handledClick(appLink, webLink));

                ((TextView) view.findViewById(R.id.text_title_banner)).setText(data.getInBannerTitle());
                ((TextView) view.findViewById(R.id.text_sub_title_banner)).setText(data.getInBannerSubTitle());
            }

            if (content.getLayoutBannerAttr().getImageList().size() > 1) {
                data = content.getLayoutBannerAttr().getImageList().get(1);
                final String appLink = data.getRedirectAppLink();
                final String webLink = data.getRedirectURL();
                imgCol = view.findViewById(R.id.iv_col_2);
                ImageHandler.loadImageFitCenter(imgCol.getContext(), imgCol, data.getImageURLMobile());
                imgCol.setOnClickListener(v -> handledClick(appLink, webLink));

                ((TextView) view.findViewById(R.id.text_title_banner2)).setText(data.getInBannerTitle());
                ((TextView) view.findViewById(R.id.text_sub_title_banner2)).setText(data.getInBannerSubTitle());
            }
        }

        return view;
    }

    View getCaulmn234(SectionContent content) {
        View view = mLayoutInflater.inflate(R.layout.tp_layout_column_3_on_1, null, false);

        if (content == null ||
                content.getSectionTitle() == null ||
                content.getLayoutBannerAttr() == null) {
            view.setVisibility(View.GONE);
            return view;
        }

        if (!content.getCta().isEmpty()) {
            TextView btnSeeAll = view.findViewById(R.id.text_see_all);
            btnSeeAll.setVisibility(View.VISIBLE);
            btnSeeAll.setText(content.getCta().getText());
            btnSeeAll.setOnClickListener(v -> handledClick(content.getCta().getAppLink(), content.getCta().getUrl()));
        }

        ((TextView) view.findViewById(R.id.text_title)).setText(content.getSectionTitle());
        ((TextView) view.findViewById(R.id.text_sub_title)).setText(content.getSectionSubTitle());

        if (content.getLayoutBannerAttr().getImageList() != null) {
            ImageList data = null;
            ImageView imgCol = null;

            if (content.getLayoutBannerAttr().getImageList().size() > 0) {
                data = content.getLayoutBannerAttr().getImageList().get(0);
                final String appLink = data.getRedirectAppLink();
                final String webLink = data.getRedirectURL();
                imgCol = view.findViewById(R.id.iv_col_1);
                ImageHandler.loadImageFitCenter(imgCol.getContext(), imgCol, data.getImageURLMobile());
                imgCol.setOnClickListener(v -> handledClick(appLink, webLink));

                ((TextView) view.findViewById(R.id.text_title_banner)).setText(data.getInBannerTitle());
                ((TextView) view.findViewById(R.id.text_sub_title_banner)).setText(data.getInBannerSubTitle());
            }

            if (content.getLayoutBannerAttr().getImageList().size() > 1) {
                data = content.getLayoutBannerAttr().getImageList().get(1);
                imgCol = view.findViewById(R.id.iv_col_2);
                final String appLink = data.getRedirectAppLink();
                final String webLink = data.getRedirectURL();
                ImageHandler.loadImageFitCenter(imgCol.getContext(), imgCol, data.getImageURLMobile());
                imgCol.setOnClickListener(v -> handledClick(appLink, webLink));

                ((TextView) view.findViewById(R.id.text_title_banner2)).setText(data.getInBannerTitle());
                ((TextView) view.findViewById(R.id.text_sub_title_banner2)).setText(data.getInBannerSubTitle());
            }
        }

        return view;
    }

    View getCarousel1on1(SectionContent content) {
        View view = mLayoutInflater.inflate(R.layout.tp_layout_generic_carousal, null, false);

        if (content == null ||
                content.getSectionTitle() == null ||
                content.getLayoutBannerAttr() == null) {
            view.setVisibility(View.GONE);
            return view;
        }

        if (!content.getCta().isEmpty()) {
            TextView btnSeeAll = view.findViewById(R.id.text_see_all);
            btnSeeAll.setVisibility(View.VISIBLE);
            btnSeeAll.setText(content.getCta().getText());
            btnSeeAll.setOnClickListener(v -> handledClick(content.getCta().getAppLink(), content.getCta().getUrl()));
        }

        view.setBackgroundColor(Color.parseColor("#ffe078"));
        ((TextView) view.findViewById(R.id.text_title)).setText(content.getSectionTitle());
        ((TextView) view.findViewById(R.id.text_sub_title)).setText(content.getSectionSubTitle());

        if (content.getLayoutBannerAttr().getImageList() != null) {
            RecyclerView rvCarousel = view.findViewById(R.id.rv_carousel);
            rvCarousel.addItemDecoration(new CarouselItemDecoration(mLayoutInflater.getContext().getResources().getDimensionPixelSize(R.dimen.dp_4)));
            rvCarousel.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
            rvCarousel.setAdapter(new SectionCarouselAdapter(content.getLayoutBannerAttr().getImageList(), CommonConstant.BannerType.CAROUSEL_1_1));
        }

        return view;
    }

    View getCarousel2on1(SectionContent content) {
        View view = mLayoutInflater.inflate(R.layout.tp_layout_generic_carousal, null, false);

        if (content == null ||
                content.getSectionTitle() == null ||
                content.getLayoutBannerAttr() == null) {
            view.setVisibility(View.GONE);
            return view;
        }

        if (!content.getCta().isEmpty()) {
            TextView btnSeeAll = view.findViewById(R.id.text_see_all);
            btnSeeAll.setVisibility(View.VISIBLE);
            btnSeeAll.setText(content.getCta().getText());
            btnSeeAll.setOnClickListener(v -> handledClick(content.getCta().getAppLink(), content.getCta().getUrl()));
        }

        ((TextView) view.findViewById(R.id.text_title)).setText(content.getSectionTitle());
        ((TextView) view.findViewById(R.id.text_sub_title)).setText(content.getSectionSubTitle());

        if (content.getLayoutBannerAttr().getImageList() != null) {
            RecyclerView rvCarousel = view.findViewById(R.id.rv_carousel);
            rvCarousel.addItemDecoration(new CarouselItemDecoration(mLayoutInflater.getContext().getResources().getDimensionPixelSize(R.dimen.dp_4)));
            rvCarousel.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
            rvCarousel.setAdapter(new SectionCarouselAdapter(content.getLayoutBannerAttr().getImageList(), CommonConstant.BannerType.CAROUSEL_2_1));
        }

        return view;
    }

    View getCarousel3on1(SectionContent content) {
        View view = mLayoutInflater.inflate(R.layout.tp_layout_generic_carousal, null, false);

        if (content == null ||
                content.getSectionTitle() == null ||
                content.getLayoutBannerAttr() == null) {
            view.setVisibility(View.GONE);
            return view;
        }

        ((TextView) view.findViewById(R.id.text_title)).setText(content.getSectionTitle());
        ((TextView) view.findViewById(R.id.text_sub_title)).setText(content.getSectionSubTitle());

        if (!content.getCta().isEmpty()) {
            TextView btnSeeAll = view.findViewById(R.id.text_see_all);
            btnSeeAll.setVisibility(View.VISIBLE);
            btnSeeAll.setText(content.getCta().getText());
            btnSeeAll.setOnClickListener(v -> handledClick(content.getCta().getAppLink(), content.getCta().getUrl()));
        }

        if (content.getLayoutBannerAttr().getImageList() != null) {
            RecyclerView rvCarousel = view.findViewById(R.id.rv_carousel);
            rvCarousel.addItemDecoration(new CarouselItemDecoration(mLayoutInflater.getContext().getResources().getDimensionPixelSize(R.dimen.dp_4)));
            rvCarousel.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
            rvCarousel.setAdapter(new SectionCarouselAdapter(content.getLayoutBannerAttr().getImageList(), CommonConstant.BannerType.CAROUSEL_3_1));
        }

        return view;
    }

    void handledClick(String appLink, String webLink) {
        if (TextUtils.isEmpty(appLink)) {
            ((TokopointRouter) mLayoutInflater.getContext().getApplicationContext()).openTokoPoint(mLayoutInflater.getContext(), webLink);
        } else {
            RouteManager.route(mLayoutInflater.getContext(), appLink);
        }
    }
}
