package com.tokopedia.events.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.di.EventComponent;
import com.tokopedia.events.view.adapter.EventLocationAdapter;
import com.tokopedia.events.view.contractor.EventsLocationContract;
import com.tokopedia.events.view.presenter.EventLocationsPresenter;
import com.tokopedia.events.view.viewmodel.EventLocationViewModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

public class EventLocationActivity extends EventBaseActivity implements SearchInputView.Listener, EventsLocationContract.EventLocationsView, EventLocationAdapter.ActionListener {
    protected static final long DEFAULT_DELAY_TEXT_CHANGED = TimeUnit.MILLISECONDS.toMillis(300);
    public static final String EXTRA_CALLBACK_LOCATION = "EXTRA_CALLBACK_LOCATION";

    @BindView(R2.id.search_input_view)
    SearchInputView searchInputView;
    @BindView(R2.id.recyclerview_city_List)
    RecyclerView recyclerview;

    EventComponent eventComponent;
    public EventLocationsPresenter eventLocationsPresenter;

    private EventLocationAdapter eventLocationAdapter;
    private List<EventLocationViewModel> eventLocationViewModels;

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, EventLocationActivity.class);
    }

    @Override
    void initPresenter() {
        initInjector();
        mPresenter = eventComponent.getEventLocationPresenter();
        eventLocationsPresenter = (EventLocationsPresenter) mPresenter;
    }

    @Override
    View getProgressBar() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchInputView.setListener(this);
        searchInputView.setDelayTextChanged(DEFAULT_DELAY_TEXT_CHANGED);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_event_location;
    }

    @Override
    public void onSearchSubmitted(String text) {
        Log.d("onSearchSubmitted", text);
    }

    @Override
    public void onSearchTextChanged(String text) {
        Log.d("onSearchTextChanged", text);
        filter(text);
    }

    @Override
    public void renderLocationList(List<EventLocationViewModel> eventLocationViewModels) {
        this.eventLocationViewModels = eventLocationViewModels;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        eventLocationAdapter = new EventLocationAdapter(this, eventLocationViewModels, this);
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setAdapter(eventLocationAdapter);
    }

    void filter(String text) {
        eventLocationAdapter.updateList(eventLocationViewModels, text);
    }

    @Override
    public void onLocationItemSelected(EventLocationViewModel locationViewModel) {
        setResult(RESULT_OK, new Intent().putExtra(EXTRA_CALLBACK_LOCATION, locationViewModel));
        finish();
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }
}
