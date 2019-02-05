package com.tokopedia.events.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.widget.TouchViewPager;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.events.EventModuleRouter;
import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.data.source.EventsUrl;
import com.tokopedia.events.di.EventComponent;
import com.tokopedia.events.view.adapter.CardPagerAdapter;
import com.tokopedia.events.view.adapter.CategoryFragmentPagerAdapter;
import com.tokopedia.events.view.adapter.SlidingImageAdapter;
import com.tokopedia.events.view.contractor.EventsContract;
import com.tokopedia.events.view.fragment.CategoryFragment;
import com.tokopedia.events.view.presenter.EventHomePresenter;
import com.tokopedia.events.view.utils.CirclePageIndicator;
import com.tokopedia.events.view.utils.EventsAnalytics;
import com.tokopedia.events.view.utils.EventsGAConst;
import com.tokopedia.events.view.utils.IFragmentLifecycleCallback;
import com.tokopedia.events.view.utils.ShadowTransformer;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by ashwanityagi on 02/11/17.
 */
public class EventsHomeActivity extends EventBaseActivity
        implements
        EventsContract.EventHomeView {

    private Unbinder unbinder;
    public static final int REQUEST_CODE_EVENTLOCATIONACTIVITY = 101;
    public static final int REQUEST_CODE_EVENTSEARCHACTIVITY = 901;

    private Menu mMenu;

    public EventHomePresenter eventHomePresenter;

    @BindView(R2.id.event_bannerpager)
    TouchViewPager viewPager;
    @BindView(R2.id.pager_indicator)
    CirclePageIndicator tabLayout;

    @BindView(R2.id.category_view_pager)
    ViewPager categoryViewPager;
    @BindView(R2.id.tabs)
    TabLayout tabs;
    @BindView(R2.id.main_content)
    FrameLayout mainContent;

    @BindView(R2.id.progress_bar_layout)
    View progressBarLayout;
    @BindView(R2.id.prog_bar)
    ProgressBar progBar;
    @BindView(R2.id.tv_addtocalendar)
    TextView addToCalendar;
    @BindView(R2.id.promo_event)
    TextView promoEvent;
    @BindView(R2.id.viewpager_top_events)
    ViewPager topEventsViewPager;
    @BindView(R2.id.htab_maincontent)
    View hTabMainContent;
    @BindView(R2.id.search_input_view)
    TextView searchView;
    @BindView(R2.id.event_app_bar_layout)
    AppBarLayout appBarLayout;


    private int mBannnerPos;
    private int defaultViewPagerPos = -1;
    private String defaultSection;
    private final static String THEMEPARK = "hiburan";
    private final static String TOP = "top";

    private EventsAnalytics eventsAnalytics;


    public final static String EXTRA_SECTION = "extra_section";

    private SlidingImageAdapter adapter;
    private CategoryFragmentPagerAdapter categoryTabsPagerAdapter;

    @DeepLink({EventsUrl.AppLink.EVENTS, EventsUrl.AppLink.EVENTS_HIBURAN})
    public static Intent getCallingApplinksTaskStask(Context context, Bundle extras) {
        String deepLink = extras.getString(DeepLink.URI);
        Uri.Builder uri = Uri.parse(deepLink).buildUpon();
        Intent destination = new Intent(context, EventsHomeActivity.class)
                .setData(uri.build())
                .putExtras(extras);
        destination.putExtra(EventsUrl.AppLink.EXTRA_FROM_PUSH, true);
        if (EventsUrl.AppLink.EVENTS.equals(deepLink)) {
            destination.putExtra(EXTRA_SECTION, TOP);
        } else if (EventsUrl.AppLink.EVENTS_HIBURAN.equals(deepLink)) {
            destination.putExtra(EXTRA_SECTION, Utils.Constants.THEMEPARK);
        }
        return destination;
    }

    @Override
    void initPresenter() {
        initInjector();
        mPresenter = eventComponent.getEventHomePresenter();
        eventHomePresenter = (EventHomePresenter) mPresenter;
    }

    @Override
    View getProgressBar() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defaultSection = getIntent().getStringExtra(EXTRA_SECTION);
        if (defaultSection == null || defaultSection.length() <= 1)
            defaultSection = TOP;
        unbinder = ButterKnife.bind(this);
        eventsAnalytics = new EventsAnalytics(getApplicationContext());
        addToCalendar.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_event_calendar_green), null,
                null, null);
        searchView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_search_icon),
                null, null, null);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset < -1440) {
                    showSearchButton();
                } else if (verticalOffset > -100) {
                    hideSearchButton();
                }
                Log.d("Offest Changed", "Offset : " + verticalOffset);
            }
        });

        setLightToolbarStyle();

    }

    protected void setLightToolbarStyle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10);
            toolbar.setBackgroundResource(R.color.white);
        } else {
            toolbar.setBackgroundResource(R.drawable.bg_white_toolbar_drop_shadow);
        }
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_toolbar_overflow_level_two_black);
        if (drawable != null)
            drawable.setBounds(5, 5, 5, 5);

        toolbar.setOverflowIcon(drawable);

        if (getSupportActionBar() != null)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black);

        toolbar.setTitleTextAppearance(this, R.style.Toolbar_TitleText);
        toolbar.setSubtitleTextAppearance(this, R.style.SubTitleTextAppearance);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_events_home_revamp;
    }

    @Override
    public void showProgressBar() {
        super.showProgressBar();
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        super.hideProgressBar();
        progressBarLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideSearchButton() {
        if (mMenu != null) {
            MenuItem item = mMenu.findItem(R.id.action_menu_search);
            item.setVisible(false);
            item.setEnabled(false);
        }
    }

    @Override
    public void showSearchButton() {
        if (mMenu != null) {
            MenuItem item = mMenu.findItem(R.id.action_menu_search);
            item.setVisible(true);
            item.setEnabled(true);
        }
    }

    @Override
    public void showLoginSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(getRootView(), message, Snackbar.LENGTH_LONG).setAction(getResources().
                getString(R.string.label_login_button), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar();
                Intent intent = ((EventModuleRouter) getApplication()).
                        getLoginIntent(getActivity());
                navigateToActivityRequest(intent, 1099);
            }
        });
        snackbar.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_home, menu);
        mMenu = menu;
        for (int i = 0; i < mMenu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString s = new SpannableString(item.getTitle());
            s.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, s.length(), 0);
            s.setSpan(new StyleSpan(Typeface.NORMAL), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            item.setTitle(s);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return eventHomePresenter.onOptionMenuClick(id);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void renderCategoryList(List<CategoryViewModel> categoryList) {
        int pos = -1;
        List<CategoryViewModel> tempCategoryList = new ArrayList<>();
        for (CategoryViewModel categoryViewModel : categoryList) {
            if (categoryViewModel.getItems() != null && categoryViewModel.getItems().size() != 0) {
                if ("carousel".equalsIgnoreCase(categoryViewModel.getName())) {
                    adapter = new SlidingImageAdapter(EventsHomeActivity.this, eventHomePresenter.getCarouselImages(categoryViewModel.getItems()), eventHomePresenter);
                    setViewPagerListener();
                    tabLayout.setViewPager(viewPager);
                    eventHomePresenter.startBannerSlide(viewPager);
                    continue;
                } else if ("top".equalsIgnoreCase(categoryViewModel.getName())) {
                    CardPagerAdapter cardPagerAdapter = new CardPagerAdapter(eventHomePresenter);
                    cardPagerAdapter.addData(categoryViewModel.getItems());
                    Utils.getSingletonInstance().setTopEvents(categoryViewModel.getItems());
                    ShadowTransformer cardShadowTransformer = new ShadowTransformer(topEventsViewPager, cardPagerAdapter);
                    cardShadowTransformer.enableScaling(true);
                    topEventsViewPager.setAdapter(cardPagerAdapter);
                    topEventsViewPager.setPageTransformer(false, cardShadowTransformer);
                    topEventsViewPager.setOffscreenPageLimit(1);
                    topEventsViewPager.setPageMargin(Utils.getSingletonInstance().convertDip2Pixels(this, 8));
                    topEventsViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {

                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                    continue;
                }
                tempCategoryList.add(categoryViewModel);
                if (defaultViewPagerPos <= 0) {
                    pos++;
                    if (defaultSection.equalsIgnoreCase(categoryViewModel.getName()))
                        defaultViewPagerPos = pos;
                    else
                        defaultViewPagerPos = 0;
                }
            }
        }

        categoryTabsPagerAdapter =
                new CategoryFragmentPagerAdapter(getSupportFragmentManager(), categoryList);
        categoryViewPager.setAdapter(categoryTabsPagerAdapter);
        setCategoryViewPagerListener();
        tabs.setupWithViewPager(categoryViewPager);
        for (int i = 0; i < tabs.getTabCount(); i++) {
            ImageView iconView = tabs.getTabAt(i).setCustomView(R.layout.tab_icon_view)
                    .getCustomView().findViewById(R.id.category_icon);
            TextView textView = tabs.getTabAt(i).getCustomView().findViewById(R.id.category_name);
            textView.setText(tempCategoryList.get(i).getTitle());
            ImageHandler.loadImageCover2(iconView, tempCategoryList.get(i).getMediaURL());
            if (i == 0) {
                try {
                    View view = tabs.getTabAt(i).getCustomView();
                    ImageView icon = view.findViewById(R.id.category_icon);
                    TextView tv = view.findViewById(R.id.category_name);
                    tv.setTextColor(getResources().getColor(R.color.green_nob));
                    icon.setColorFilter(getResources().getColor(R.color.transparent_green_nob));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
        categoryViewPager.setSaveFromParentEnabled(false);
        if (defaultViewPagerPos == 0) {
            IFragmentLifecycleCallback fragmentToShow = (CategoryFragment) categoryTabsPagerAdapter.getItem(defaultViewPagerPos);
            fragmentToShow.fragmentResume();
        }
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try {
                    View customeView = tab.getCustomView();
                    ImageView icon = customeView.findViewById(R.id.category_icon);
                    icon.setColorFilter(getResources().getColor(R.color.transparent_green_nob));
                    TextView tv = customeView.findViewById(R.id.category_name);
                    tv.setTextColor(getResources().getColor(R.color.green_nob));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                try {
                    View customeView = tab.getCustomView();
                    ImageView icon = customeView.findViewById(R.id.category_icon);
                    icon.setColorFilter(getResources().getColor(R.color.transparent));
                    TextView tv = customeView.findViewById(R.id.category_name);
                    tv.setTextColor(getResources().getColor(R.color.black_38));

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                try {
                    View customeView = tab.getCustomView();
                    ImageView icon = customeView.findViewById(R.id.category_icon);
                    icon.setColorFilter(getResources().getColor(R.color.transparent_green_nob));
                    TextView tv = customeView.findViewById(R.id.category_name);
                    tv.setTextColor(getResources().getColor(R.color.green_nob));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
        hTabMainContent.setVisibility(View.VISIBLE);
        categoryViewPager.setCurrentItem(defaultViewPagerPos);
    }


    private void setViewPagerListener() {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                eventHomePresenter.onBannerSlide(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

        viewPager.setAdapter(adapter);
    }

    private void setCategoryViewPagerListener() {
        categoryViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currentPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int newPosition) {

                eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_CLICK_TAB, categoryViewPager.getAdapter().getPageTitle(newPosition) + "-"
                        + String.valueOf(newPosition));

                IFragmentLifecycleCallback fragmentToShow = (CategoryFragment) categoryTabsPagerAdapter.getItem(newPosition);
                fragmentToShow.fragmentResume();

                IFragmentLifecycleCallback fragmentToHide = (CategoryFragment) categoryTabsPagerAdapter.getItem(currentPosition);
                fragmentToHide.fragmentPause();

                currentPosition = newPosition;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_CLICK_BACK, getScreenName());
    }

    @Override
    public String getScreenName() {
        return eventHomePresenter.getSCREEN_NAME();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        eventHomePresenter.onActivityResult(requestCode);
    }

    @OnClick(R2.id.tv_addtocalendar)
    void onClickCalendar() {
        eventHomePresenter.onClickEventCalendar();
    }

    @OnClick(R2.id.search_input_view)
    void onClickSearch() {
        eventHomePresenter.onOptionMenuClick(R.id.action_menu_search);
    }

    @OnClick(R2.id.promo_event)
    void goToPromo() {
        eventHomePresenter.onOptionMenuClick(R.id.action_promo);
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }
}
