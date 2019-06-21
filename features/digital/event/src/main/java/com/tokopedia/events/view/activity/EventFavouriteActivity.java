package com.tokopedia.events.view.activity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.events.R;
import com.tokopedia.events.view.adapter.EventCategoryAdapterRevamp;
import com.tokopedia.events.view.contractor.EventFavouriteContract;
import com.tokopedia.events.view.presenter.EventFavouritePresenter;
import com.tokopedia.events.view.utils.EventsAnalytics;
import com.tokopedia.events.view.utils.EventsGAConst;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;

import java.util.List;

/**
 * Created by pranaymohapatra on 16/05/18.
 */

public class EventFavouriteActivity extends EventBaseActivity implements EventFavouriteContract.EventFavouriteView, View.OnClickListener {

    public EventFavouritePresenter eventFavouritePresenter;
    private EventsAnalytics eventsAnalytics;

    RecyclerView favRecyclerView;
    View ivNoFav;
    View tvNoFav;
    View tvFindEvents;
    View title;

    @Override
    void initPresenter() {
        initInjector();
        mPresenter = eventComponent.getEventFavoritePresenter();
        eventFavouritePresenter = (EventFavouritePresenter) mPresenter;
    }

    @Override
    View getProgressBar() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventsAnalytics = new EventsAnalytics();
        setLightToolbarStyle();
    }

    @Override
    void setupVariables() {
        favRecyclerView = findViewById(R.id.rv_fav_view);
        ivNoFav = findViewById(R.id.iv_no_fav);
        tvNoFav = findViewById(R.id.tv_no_fav);
        tvFindEvents = findViewById(R.id.tv_find_events);
        title = findViewById(R.id.tv_favorite_saya);
        tvFindEvents.setOnClickListener(this);
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
        return R.layout.activity_favourites;
    }

    @Override
    public void renderFavourites(List<CategoryItemsViewModel> categoryItemsViewModels) {
        EventCategoryAdapterRevamp favAdapter = new EventCategoryAdapterRevamp(this, categoryItemsViewModels, true);
        favRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        favRecyclerView.setAdapter(favAdapter);
        favRecyclerView.setVisibility(View.VISIBLE);
    }


    @Override
    public void showProgressBar() {
        super.showProgressBar();
    }

    @Override
    public void hideProgressBar() {
        super.hideProgressBar();
    }

    @Override
    public void toggleEmptyLayout(int visibility) {
        ivNoFav.setVisibility(visibility);
        tvNoFav.setVisibility(visibility);
        tvFindEvents.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            favRecyclerView.setVisibility(View.GONE);
            title.setVisibility(View.GONE);
        } else {
            favRecyclerView.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_CLICK_BACK, getScreenName());
    }

    @Override
    public String getScreenName() {
        return EventsGAConst.EVENTS_FAV_PAGE;
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        eventFavouritePresenter.onClickOptionMenu(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_find_events) {
            finish();
        }
    }
}
