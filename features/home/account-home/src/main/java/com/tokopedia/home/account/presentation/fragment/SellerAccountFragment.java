package com.tokopedia.home.account.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.di.component.DaggerSellerAccountComponent;
import com.tokopedia.home.account.di.component.SellerAccountComponent;
import com.tokopedia.home.account.presentation.SellerAccount;
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.adapter.seller.SellerAccountAdapter;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.viewmodel.base.SellerViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author okasurya on 7/16/18.
 */
public class SellerAccountFragment extends BaseAccountFragment implements AccountItemListener, SellerAccount.View {
    public static final String TAG = SellerAccountFragment.class.getSimpleName();
    public static final String SELLER_DATA = "seller_data";

    private RecyclerView recyclerView;
    private SellerAccountAdapter adapter;

    @Inject
    SellerAccount.Presenter presenter;

    public static Fragment newInstance() {
        Fragment fragment = new SellerAccountFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_account, container, false);
        recyclerView = view.findViewById(R.id.recycler_seller);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new SellerAccountAdapter(new AccountTypeFactory(this), new ArrayList<>());
        recyclerView.setAdapter(adapter);

        if (getContext() != null) {
            GraphqlClient.init(getContext());

            presenter.getSellerData(GraphqlHelper.loadRawString(getContext().getResources(), R.raw
                    .query_seller_account_home));
        }
    }

    @Override
    public void loadSellerData(SellerViewModel model) {
        if(model.getItems() != null) {
            adapter.clearAllElements();
            adapter.setElement(model.getItems());
        }
    }

    @Override
    protected String getScreenName() {
        return TAG;
    }

    private void initInjector() {
        SellerAccountComponent component = DaggerSellerAccountComponent.builder()
                .baseAppComponent(
                        ((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent()
                ).build();

        component.inject(this);
        presenter.attachView(this);
    }
}
