package com.tokopedia.train.station.presentation;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.station.di.TrainStationsComponent;
import com.tokopedia.train.station.presentation.adapter.TrainStationAdapterTypeFactory;
import com.tokopedia.train.station.presentation.adapter.TrainStationTypeFactory;
import com.tokopedia.train.station.presentation.adapter.viewholder.listener.TrainStationActionListener;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationAndCityViewModel;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationCityViewModel;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationViewModel;
import com.tokopedia.train.station.presentation.contract.TrainStationsContract;
import com.tokopedia.train.station.presentation.presenter.TrainStationsPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrainStationsFragment extends BaseSearchListFragment<Visitable, TrainStationTypeFactory> implements TrainStationsContract.View, TrainStationActionListener {

    private static final String EXTRA_HINT = "EXTRA_HINT";
    @Inject
    TrainStationsPresenter presenter;

    private OnFragmentInteractionListener interactionListener;

    private boolean isFirstTime = true;
    private String searchHint;

    public interface OnFragmentInteractionListener {
        void onStationClicked(TrainStationAndCityViewModel viewModel);
    }

    public static TrainStationsFragment newInstance(String hint) {
        TrainStationsFragment fragment = new TrainStationsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_HINT, hint);
        fragment.setArguments(bundle);
        return fragment;
    }

    public TrainStationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_train_stations, container, false);
        view.requestFocus();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchHint = getArguments().getString(EXTRA_HINT);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        presenter.attachView(this);
        super.onViewCreated(view, savedInstanceState);
        presenter.onViewCreated();
    }

    @Override
    public void loadData(int page) {
        if (isFirstTime) {
            presenter.actionOnInitialLoad();
        }
    }

    @Override
    protected TrainStationTypeFactory getAdapterTypeFactory() {
        return new TrainStationAdapterTypeFactory(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(TrainStationsComponent.class).inject(this);
    }

    @Override
    public void onSearchSubmitted(String text) {
        presenter.onKeywordChange(text);
    }

    @Override
    public void onSearchTextChanged(String text) {
        presenter.onKeywordChange(text);
    }

    @Override
    public void onItemClicked(Visitable visitable) {
        if (visitable instanceof TrainStationViewModel) {
            onStationClicked((TrainStationViewModel) visitable);
        } else if (visitable instanceof TrainStationCityViewModel) {
            onCityClicked((TrainStationCityViewModel) visitable);
        }
    }

    @Override
    public void renderStationList(Visitable visitable) {
        List<Visitable> visitables = new ArrayList<>();
        visitables.add(visitable);
        super.renderList(visitables);
    }

    @Override
    public void renderStationList(List<Visitable> visitables) {
        super.renderList(visitables);
    }

    @Override
    public void clearStationList() {
        getAdapter().clearAllElements();
    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    @Override
    public void renderSearchHint() {
        searchInputView.setSearchHint(searchHint);
    }

    @Override
    public void hideSearchView() {
        searchInputView.setVisibility(View.GONE);
    }

    @Override
    public void showSearchView() {
        searchInputView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStationClicked(TrainStationViewModel viewModel) {
        TrainStationAndCityViewModel trainStationAndCityViewModel = new TrainStationAndCityViewModel(
                viewModel.getStationCode(), viewModel.getCityName(), viewModel.getIslandName()
        );
        if (interactionListener != null)
            interactionListener.onStationClicked(trainStationAndCityViewModel);
    }

    @Override
    public void onCityClicked(TrainStationCityViewModel element) {
        TrainStationAndCityViewModel trainStationAndCityViewModel = new TrainStationAndCityViewModel(
                "", element.getCityName(), element.getIslandName()
        );
        if (interactionListener != null)
            interactionListener.onStationClicked(trainStationAndCityViewModel);
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        if (context instanceof OnFragmentInteractionListener) {
            interactionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException("Activity must implement " + OnFragmentInteractionListener.class.getSimpleName());
        }
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}
