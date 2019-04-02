package com.tokopedia.tokocash.historytokocash.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.historytokocash.presentation.adapter.WaitingTransactionAdapter;
import com.tokopedia.tokocash.historytokocash.presentation.model.ItemHistory;
import com.tokopedia.tokocash.historytokocash.presentation.model.TokoCashHistoryData;

import java.util.ArrayList;

/**
 * Created by nabillasabbaha on 2/13/18.
 */

public class WaitingTransactionFragment extends BaseDaggerFragment {

    public final static String TOKOCASH_HISTORY_DATA_EXTRA = "tokocash_history_data_extra";

    private RecyclerView waitingTransactionRV;
    private WaitingTransactionAdapter adapter;
    private TokoCashHistoryData tokoCashHistoryData;

    public static WaitingTransactionFragment newInstance(TokoCashHistoryData tokoCashHistoryData) {
        WaitingTransactionFragment fragment = new WaitingTransactionFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TOKOCASH_HISTORY_DATA_EXTRA, tokoCashHistoryData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_waiting_transaction, container, false);
        waitingTransactionRV = view.findViewById(R.id.waiting_transaction_rv);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tokoCashHistoryData = getArguments().getParcelable(TOKOCASH_HISTORY_DATA_EXTRA);

        waitingTransactionRV.setHasFixedSize(true);
        waitingTransactionRV.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        adapter = new WaitingTransactionAdapter(new ArrayList<ItemHistory>());
        waitingTransactionRV.setAdapter(adapter);
        adapter.addItem(tokoCashHistoryData.getItemHistoryList());
    }
}
