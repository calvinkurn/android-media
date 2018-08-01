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

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.SellerAccount;
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.adapter.seller.SellerAccountAdapter;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.viewmodel.base.SellerViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author okasurya on 7/16/18.
 */
public class SellerAccountFragment extends BaseAccountFragment implements AccountItemListener, SellerAccount.View {
    public static final String TAG = SellerAccountFragment.class.getSimpleName();
    public static final String SELLER_DATA = "seller_data";

    private RecyclerView recyclerView;
    private SellerAccountAdapter adapter;

    public static Fragment newInstance(SellerViewModel sellerViewModel) {
        Fragment fragment = new SellerAccountFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(SELLER_DATA, sellerViewModel);
        fragment.setArguments(bundle);
        return fragment;
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

        if (getArguments() != null
                && getArguments().getParcelable(SELLER_DATA) != null
                && getArguments().getParcelable(SELLER_DATA) instanceof SellerViewModel) {
            loadData(((SellerViewModel) getArguments().getParcelable(SELLER_DATA)).getItems());
        }
    }

    @Override
    public void loadData(List<? extends Visitable> visitables) {
        if(visitables != null) {
            adapter.clearAllElements();
            adapter.setElement(visitables);
        }
    }

    @Override
    protected String getScreenName() {
        return TAG;
    }
}
