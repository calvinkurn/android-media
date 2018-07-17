package com.tokopedia.home.account.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.di.component.BuyerAccountComponent;
import com.tokopedia.home.account.di.component.DaggerBuyerAccountComponent;
import com.tokopedia.home.account.presentation.BuyerAccount;
import com.tokopedia.home.account.presentation.adapter.buyer.BuyerAccountAdapter;
import com.tokopedia.home.account.presentation.adapter.buyer.BuyerAccountTypeFactory;
import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author okasurya on 7/16/18.
 */
public class BuyerAccountFragment extends TkpdBaseV4Fragment implements BuyerAccount.View {
    public static final String TAG = BuyerAccountFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private BuyerAccountAdapter adapter;

    @Inject
    BuyerAccount.Presenter presenter;

    public static Fragment newInstance() {
        return new BuyerAccountFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buyer_account, container, false);
        recyclerView = view.findViewById(R.id.recycler_buyer);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new BuyerAccountAdapter(new BuyerAccountTypeFactory(), new ArrayList<>());
        recyclerView.setAdapter(adapter);

        presenter.getData();
    }

    @Override
    protected String getScreenName() {
        return TAG;
    }

    @Override
    public void loadData(List<Visitable> data) {
        Log.d("okasurya", TAG + ".loadData" + data.get(0).toString());
        adapter.addMoreData(data);
    }

    private void initInjector() {
        BuyerAccountComponent component = DaggerBuyerAccountComponent.builder()
                .baseAppComponent(
                        ((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent()
                ).build();

        component.inject(this);
        presenter.attachView(this);
    }
}
