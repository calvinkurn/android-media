package com.tokopedia.train.seat.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.component.Menus;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.seat.di.TrainSeatComponent;
import com.tokopedia.train.seat.presentation.contract.TrainSeatContract;
import com.tokopedia.train.seat.presentation.fragment.listener.TrainSeatListener;
import com.tokopedia.train.seat.presentation.fragment.viewpager.TrainWagonsPagerAdapter;
import com.tokopedia.train.seat.presentation.presenter.TrainSeatPresenter;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatPassengerViewModel;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatViewModel;
import com.tokopedia.train.seat.presentation.viewmodel.TrainWagonViewModel;
import com.tokopedia.train.seat.presentation.widget.CountdownTimeView;
import com.tokopedia.train.seat.presentation.widget.TrainSeatPassengerAndWagonView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class TrainSeatFragment extends BaseDaggerFragment implements TrainSeatContract.View, TrainSeatPassengerAndWagonView.TrainSeatActionListener {

    @Inject
    TrainSeatPresenter presenter;

    private CountdownTimeView countdownTimeView;
    private TrainSeatPassengerAndWagonView trainSeatHeader;
    private ViewPager wagonViewPager;
    private LinearLayout container;
    private ProgressBar progressBar;
    private Button submitButton;

    private TrainWagonsPagerAdapter adapter;

    private List<TrainSeatPassengerViewModel> passengers;
    private List<TrainSeatPassengerViewModel> originPassengers;
    private List<TrainWagonViewModel> wagons;
    private String expiredTime;

    public static Fragment newInstance() {
        return new TrainSeatFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO : assign passengers from soft book responses
        passengers = new ArrayList<>();
        originPassengers = passengers;
        expiredTime = "2019-02-20T17:35:00Z";
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
        container = (LinearLayout) view.findViewById(R.id.container);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
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
        countdownTimeView.setListener(new CountdownTimeView.OnActionListener() {
            @Override
            public void onFinished() {
                presenter.onRunningOutOfTime();
            }
        });
    }

    @Override
    public void showGetSeatMapLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideGetSeatMapLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void renderWagon(List<TrainWagonViewModel> trainWagonViewModels) {
        wagons = trainWagonViewModels;
        trainSeatHeader.renderWagon(trainWagonViewModels.get(0).getWagonCode());
        trainSeatHeader.renderPassenger(passengers);

        adapter = new TrainWagonsPagerAdapter(getChildFragmentManager(), trainWagonViewModels, new TrainWagonFragment.OnFragmentInteraction() {
            @Override
            public List<TrainSeatPassengerViewModel> getPassengers() {
                return passengers;
            }

            @Override
            public void onPassengerSeatChange(TrainSeatPassengerViewModel passenger, TrainSeatViewModel seat) {
                for (TrainSeatPassengerViewModel passengerSeat : passengers) {
                    if (passengerSeat.getName().equalsIgnoreCase(passenger.getName())) {
                        passengerSeat.setSeatViewModel(passenger.getSeatViewModel());
                        break;
                    }
                }
                Object fragment = adapter.instantiateItem(wagonViewPager, wagonViewPager.getCurrentItem());
                if (fragment != null && fragment instanceof TrainSeatListener) {
                    ((TrainSeatListener) fragment).notifyPassengerUpdate();
                }
            }
        });

        wagonViewPager.setOffscreenPageLimit(1);
        wagonViewPager.setAdapter(adapter);
    }

    @Override
    public void hidePage() {
        container.setVisibility(View.GONE);
    }

    @Override
    public void showPage() {
        container.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorGetSeatMaps(String message) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), message, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getSeatMaps();
            }
        });
    }

    @Override
    public String getExpireDate() {
        return expiredTime;
    }

    @Override
    public void renderExpireDateCountdown(Date expireDate) {
        countdownTimeView.setExpiredDate(expireDate);
    }

    @Override
    public void backToHomePage() {
        getActivity().finish();
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
