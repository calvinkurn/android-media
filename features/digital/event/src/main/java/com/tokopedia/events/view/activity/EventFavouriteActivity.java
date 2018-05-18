package com.tokopedia.events.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.di.DaggerEventComponent;
import com.tokopedia.events.di.EventComponent;
import com.tokopedia.events.di.EventModule;
import com.tokopedia.events.view.adapter.EventCategoryAdapterRevamp;
import com.tokopedia.events.view.contractor.EventFavouriteContract;
import com.tokopedia.events.view.presenter.EventFavouritePresenter;
import com.tokopedia.events.view.utils.EventsGAConst;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pranaymohapatra on 16/05/18.
 */

public class EventFavouriteActivity extends TActivity implements EventFavouriteContract.EventFavouriteView {

    EventComponent eventComponent;
    @Inject
    public EventFavouritePresenter mPresenter;

    @BindView(R2.id.rv_fav_view)
    RecyclerView favRecyclerView;
    @BindView(R2.id.iv_no_fav)
    View ivNoFav;
    @BindView(R2.id.tv_no_fav)
    View tvNoFav;
    @BindView(R2.id.tv_find_events)
    View tvFindEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setTitle("Favorit Saya");
        executeInjector();
        ButterKnife.bind(this);
        mPresenter.attachView(this);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_favourites;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {

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

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public View getRootView() {
        return null;
    }

    @Override
    public void toggleEmptyLayout(int visibility) {
        ivNoFav.setVisibility(visibility);
        tvNoFav.setVisibility(visibility);
        tvFindEvents.setVisibility(visibility);
        if (visibility == View.VISIBLE)
            favRecyclerView.setVisibility(View.GONE);
        else
            favRecyclerView.setVisibility(View.VISIBLE);
    }

    @OnClick(R2.id.tv_find_events)
    void goToHome() {
        finish();
    }

    private void executeInjector() {
        if (eventComponent == null) initInjector();
        eventComponent.inject(this);
    }

    private void initInjector() {
        eventComponent = DaggerEventComponent.builder()
                .appComponent(getApplicationComponent())
                .eventModule(new EventModule(this))
                .build();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UnifyTracking.eventDigitalEventTracking(EventsGAConst.EVENT_CLICK_BACK, getScreenName());
    }

    @Override
    public String getScreenName() {
        return EventsGAConst.EVENTS_FAV_PAGE;
    }
}
