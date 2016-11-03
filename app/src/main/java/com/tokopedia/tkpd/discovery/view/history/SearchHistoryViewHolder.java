package com.tokopedia.tkpd.discovery.view.history;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.discovery.model.history.DetailSearchHistoryAdapter;
import com.tokopedia.tkpd.discovery.model.history.SearchHistoryModel;
import com.tokopedia.tkpd.discovery.presenter.history.SearchHistoryImpl;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Toped18 on 6/30/2016.
 */
public class SearchHistoryViewHolder extends RecyclerView.ViewHolder {


    private static final String TAG = SearchHistoryViewHolder.class.getSimpleName();
    public static final String HISTORY_TITLE = "History";
    public static final String HOTLIST_TITLE = "Hotlist";
    public static final String POPULAR_TITLE = "Popular Search";
    @Bind(R.id.textview_search_history_header)
    TextView searchHistoryHeader;

    @Bind(R.id.search_list)
    RecyclerView searchHistoryList;

    @Bind(R.id.clear_search_history)
    TextView clearSearchHistory;

    DetailSearchHistoryAdapter adapter;
    LinearLayoutManager linearLayoutManager;


    public static int getItemViewLayout(){
        return R.layout.recyclerview_search_history;
    }


    public SearchHistoryViewHolder(View itemView, SearchHistoryImpl.OnItemClickListener listener) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        adapter = new DetailSearchHistoryAdapter(listener);
        searchHistoryList.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(itemView.getContext());
        searchHistoryList.setLayoutManager(linearLayoutManager);
    }

    public void setSearchTerm(String searchTerm){
        adapter.setSearchTerm(searchTerm);
    }

    public void setSuggestion(List<SearchHistoryModel.Data> datas){
        searchHistoryHeader.setVisibility(View.GONE);
        clearSearchHistory.setVisibility(View.GONE);
        adapter.setDatas(datas);
    }

    public void setHotlist(List<SearchHistoryModel.Data> datas) {
        searchHistoryHeader.setText(HOTLIST_TITLE);
        clearSearchHistory.setVisibility(View.GONE);
        adapter.setDatas(datas);
    }

    public void setHistory(List<SearchHistoryModel.Data> datas, View.OnClickListener listener) {
        searchHistoryHeader.setText(HISTORY_TITLE);
        clearSearchHistory.setVisibility(View.VISIBLE);
        clearSearchHistory.setOnClickListener(listener);
        adapter.setDatas(datas);
    }

    public void setPopular(List<SearchHistoryModel.Data> datas){
        searchHistoryHeader.setText(POPULAR_TITLE);
        clearSearchHistory.setVisibility(View.GONE);
        adapter.setDatas(datas);
    }


}
