package com.tokopedia.autocomplete.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.autocomplete.OnScrollListenerAutocomplete;
import com.tokopedia.autocomplete.R;
import com.tokopedia.autocomplete.adapter.ItemClickListener;
import com.tokopedia.autocomplete.adapter.SearchAdapterTypeFactory;
import com.tokopedia.autocomplete.analytics.AppScreen;
import com.tokopedia.autocomplete.suggestion.SuggestionAdapter;

import java.util.List;

/**
 * @author erry on 23/02/17.
 */

public class SearchResultFragment extends TkpdBaseV4Fragment {

    private static final String ARGS_INSTANCE_NAME = "ARGS_INSTANCE_NAME";
    private static final String DEFAULT_INSTANCE_TPE = "unknown";
    private static final String ARGS_INSTANCE_TYPE = "ARGS_INSTANCE_TYPE";
    private SuggestionAdapter adapter;
    private LinearLayoutManager layoutManager;
    private ItemClickListener clickListener;
    private String instanceType;
    private int instanceIndex;

    private RecyclerView recyclerView;

    public SearchResultFragment() {
    }

    public static SearchResultFragment newInstance(String tabName,
                                                   int tabIndex,
                                                   ItemClickListener clickListener) {

        Bundle args = new Bundle();
        args.putString(ARGS_INSTANCE_NAME, tabName);
        args.putInt(ARGS_INSTANCE_TYPE, tabIndex);

        SearchResultFragment fragment = new SearchResultFragment();
        fragment.setCallBackListener(clickListener);
        fragment.setArguments(args);
        return fragment;
    }

    private void setCallBackListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        instanceType = getArguments().getString(ARGS_INSTANCE_NAME, DEFAULT_INSTANCE_TPE);
        instanceIndex = getArguments().getInt(ARGS_INSTANCE_TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_result, container, false);
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        prepareView(view);
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_UNIVERSEARCH;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void addBulkSearchResult(List<Visitable> list) {
        if (adapter != null) {
            adapter.addAll(list);
        }
    }

    private void prepareView(View view) {
        SearchAdapterTypeFactory typeFactory = new SearchAdapterTypeFactory(instanceType, clickListener);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        adapter = new SuggestionAdapter(typeFactory);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new OnScrollListenerAutocomplete(view.getContext(), view));
    }

    public void clearData() {
        if (adapter != null) {
            adapter.clearData();
        }
    }

}