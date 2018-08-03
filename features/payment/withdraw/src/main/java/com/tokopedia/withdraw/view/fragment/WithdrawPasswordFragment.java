package com.tokopedia.withdraw.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.withdraw.R;
import com.tokopedia.withdraw.view.listener.WithdrawPasswordContract;

public class WithdrawPasswordFragment extends BaseDaggerFragment implements WithdrawPasswordContract.View{


    @Override
    protected String getScreenName() {
        return null;
    }
    
//    @Inject
//    WithdrawPasswordPresenter presenter;

    @Override
    protected void initInjector() {
//        WithdrawComponent withdrawComponent = DaggerWithdrawComponent.builder()
//                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
//                .build();
//
//        DaggerDepositWithdrawComponent.builder().withdrawComponent(withdrawComponent)
//                .build().inject(this);
//
//        presenter.attachView(this);
    }

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new WithdrawPasswordFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_withdraw, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

}
