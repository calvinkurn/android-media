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
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.seat.di.TrainSeatComponent;
import com.tokopedia.train.seat.presentation.contract.TrainSeatContract;
import com.tokopedia.train.seat.presentation.fragment.viewpager.TrainSeatPagerAdapter;
import com.tokopedia.train.seat.presentation.presenter.TrainSeatPresenter;
import com.tokopedia.train.seat.presentation.viewmodel.TrainWagonViewModel;
import com.tokopedia.train.seat.presentation.widget.CountdownTimeView;
import com.tokopedia.train.seat.presentation.widget.TrainSeatPassengerAndWagonView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class TrainSeatFragment extends BaseDaggerFragment implements TrainSeatContract.View {

    @Inject
    TrainSeatPresenter presenter;

    private CountdownTimeView countdownTimeView;
    private TrainSeatPassengerAndWagonView trainSeatHeader;
    private ViewPager wagonViewPager;
    private Button submitButton;


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
        List<TrainWagonFragment> fragments = new ArrayList<>();
        for (TrainWagonViewModel wagon : trainWagonViewModels) {
            fragments.add(TrainWagonFragment.newInstance(wagon));

        }
        TrainSeatPagerAdapter adapter = new TrainSeatPagerAdapter(getChildFragmentManager(), fragments);
        wagonViewPager.setAdapter(adapter);
    }
}
