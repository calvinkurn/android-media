package com.tokopedia.train.seat.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.seat.presentation.viewmodel.TrainWagonViewModel;

public class TrainWagonFragment extends BaseDaggerFragment {
    private static final String EXTRA_WAGON = "EXTRA_WAGON";
    private TrainWagonViewModel trainWagonViewModel;

    public static TrainWagonFragment newInstance(TrainWagonViewModel trainWagonViewModel) {
        TrainWagonFragment fragment = new TrainWagonFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_WAGON, trainWagonViewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trainWagonViewModel = getArguments().getParcelable(EXTRA_WAGON);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_train_seat, container, false);
        return view;
    }
}
