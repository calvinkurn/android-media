package com.tokopedia.events.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.view.adapter.EventCategoryAdapterRevamp;
import com.tokopedia.events.view.utils.EventsAnalytics;
import com.tokopedia.events.view.utils.EventsGAConst;
import com.tokopedia.events.view.utils.IFragmentLifecycleCallback;
import com.tokopedia.events.view.viewmodel.CategoryViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ashwanityagi on 21/11/17.
 */

public class CategoryFragment extends BaseDaggerFragment implements IFragmentLifecycleCallback {

    @BindView(R2.id.recyclerview_event)
    RecyclerView recyclerview;
    LinearLayoutManager linearLayoutManager;
    EventCategoryAdapterRevamp eventCategoryAdapter;

    private Boolean isCreated = false;
    private Boolean isSelected = false;
    private Boolean wasStopped = false;

    private static final String ARG_PARAM_EXTRA_EVENTS_DATA = "ARG_PARAM_EXTRA_EVENTS_DATA";
    private static final String ARG_CATEGORYID = "ARG_CATEGORYID";

    private CategoryViewModel categoryViewModel;
    private String categoryId;
    private EventsAnalytics eventsAnalytics;
    private Context context;

    public static Fragment newInstance(CategoryViewModel categoryViewModel, String categoryId) {
        CategoryFragment categoryFragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_EXTRA_EVENTS_DATA, categoryViewModel);
        args.putString(ARG_CATEGORYID, categoryId);
        categoryFragment.setArguments(args);
        return categoryFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.categoryViewModel = getArguments().getParcelable(ARG_PARAM_EXTRA_EVENTS_DATA);
            this.categoryId = getArguments().getString(ARG_CATEGORYID);
        }
    }

    @Override
    protected void initInjector() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_category_view, container, false);
        ButterKnife.bind(this, view);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        eventCategoryAdapter = new EventCategoryAdapterRevamp(getActivity(), categoryViewModel.getItems(), false);
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setAdapter(eventCategoryAdapter);
        isCreated = true;
        this.context = container.getContext();
        eventsAnalytics = new EventsAnalytics();
        return view;
    }


    @Override
    protected String getScreenName() {
        return "";
    }

    @Override
    public void fragmentResume() {
        isSelected = true;
        sendGAProductImpression();
    }

    @Override
    public void fragmentPause() {
        isSelected = false;
        try {
            eventCategoryAdapter.disableTracking();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void sendGAProductImpression() {
        if (isCreated && isSelected) {
            int lastIndex = linearLayoutManager.findLastCompletelyVisibleItemPosition();
            for (int i = 0; i < lastIndex; i++) {
                if (!categoryViewModel.getItems().get(i).isTrack())
                    eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_PRODUCT_IMPRESSION, categoryViewModel.getItems().get(i).getTitle()
                            + " - " + i);
                categoryViewModel.getItems().get(i).setTrack(true);
            }
            eventCategoryAdapter.enableTracking();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isCreated = false;
    }

    @Override
    public void onResume() {
        if (wasStopped)
            eventCategoryAdapter.notifyDataSetChanged();
        super.onResume();
        sendGAProductImpression();
    }

    @Override
    public void onStop() {
        wasStopped = true;
        super.onStop();
    }
}
