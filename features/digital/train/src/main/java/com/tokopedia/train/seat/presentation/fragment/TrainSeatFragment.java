package com.tokopedia.train.seat.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.design.component.Menus;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.seat.di.TrainSeatComponent;
import com.tokopedia.train.seat.presentation.contract.TrainSeatContract;
import com.tokopedia.train.seat.presentation.fragment.viewpager.TrainWagonsPagerAdapter;
import com.tokopedia.train.seat.presentation.presenter.TrainSeatPresenter;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatPassengerViewModel;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatViewModel;
import com.tokopedia.train.seat.presentation.viewmodel.TrainWagonViewModel;
import com.tokopedia.train.seat.presentation.widget.CountdownTimeView;
import com.tokopedia.train.seat.presentation.widget.TrainSeatPassengerAndWagonView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class TrainSeatFragment extends BaseDaggerFragment implements TrainSeatContract.View, TrainSeatPassengerAndWagonView.TrainSeatActionListener {

    @Inject
    TrainSeatPresenter presenter;

    private CountdownTimeView countdownTimeView;
    private TrainSeatPassengerAndWagonView trainSeatHeader;
    private ViewPager wagonViewPager;
    private Button submitButton;
    private List<TrainSeatPassengerViewModel> passengers;
    private List<TrainWagonViewModel> wagons;


    public static Fragment newInstance() {
        return new TrainSeatFragment();
    }

    @Override
    protected void initInjector() {
        getComponent(TrainSeatComponent.class).inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_train_seat, container, false);
        findViews(view);
        return view;
    }

    private void findViews(View view) {
        countdownTimeView = (CountdownTimeView) view.findViewById(R.id.ct_countdown);
        trainSeatHeader = (TrainSeatPassengerAndWagonView) view.findViewById(R.id.train_seat_header);
        wagonViewPager = (ViewPager) view.findViewById(R.id.vp_wagon);
        submitButton = (Button) view.findViewById(R.id.btn_submit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        presenter.getSeatMaps();
    }

    @Override
    public void showGetSeatMapLoading() {

    }

    @Override
    public void hideGetSeatMapLoading() {

    }

    @Override
    public void renderWagon(List<TrainWagonViewModel> trainWagonViewModels) {
        wagons = trainWagonViewModels;
        trainSeatHeader.renderWagon(trainWagonViewModels.get(0).getWagonCode());
        trainSeatHeader.renderPassenger(passengers);

        TrainWagonsPagerAdapter adapter = new TrainWagonsPagerAdapter(getChildFragmentManager(), trainWagonViewModels, new TrainWagonFragment.OnFragmentInteraction() {
            @Override
            public List<TrainSeatPassengerViewModel> getPassengers() {
                return passengers;
            }

            @Override
            public void onPassengerSeatChange(TrainSeatPassengerViewModel passenger, TrainSeatViewModel seat) {

            }
        });
        wagonViewPager.setOffscreenPageLimit(1);
        wagonViewPager.setAdapter(adapter);
    }

    @Override
    public void onWagonClicked() {
        Menus menus = new Menus(getActivity());
        String[] wagonsTitle = new String[wagons.size()];
        for (int i = 0; i < wagons.size(); i++) {
            wagonsTitle[i] = wagons.get(i).getWagonCode();
        }

        menus.setItemMenuList(wagonsTitle);
        menus.setActionText(getString(R.string.train_seat_choose_wagon_title));
        menus.setOnActionClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menus.dismiss();
            }
        });
        menus.setOnItemMenuClickListener(new Menus.OnItemMenuClickListener() {
            @Override
            public void onClick(Menus.ItemMenus itemMenus, int pos) {
                wagonViewPager.setCurrentItem(pos);
                menus.dismiss();
            }
        });
        menus.show();
    }
}
