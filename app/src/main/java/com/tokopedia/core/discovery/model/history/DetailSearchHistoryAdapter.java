package com.tokopedia.core.discovery.model.history;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.R;
import com.tokopedia.core.discovery.presenter.history.SearchHistoryImpl;
import com.tokopedia.core.discovery.view.history.DetailSearchHistoryViewHolder;

import java.util.List;

/**
 * Created by Toped18 on 7/4/2016.
 */
public class DetailSearchHistoryAdapter extends RecyclerView.Adapter {

    private final SearchHistoryImpl.OnItemClickListener listener;
    private List<SearchHistoryModel.Data> datas;
    private String searchTerm;


    public DetailSearchHistoryAdapter(SearchHistoryImpl.OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(DetailSearchHistoryViewHolder.getItemViewLayout(), parent, false);
        return new DetailSearchHistoryViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(datas.get(position) != null) {
            SearchHistoryModel.Data data = datas.get(position);
            ((DetailSearchHistoryViewHolder) holder).setmSearchTerm(searchTerm);
            ((DetailSearchHistoryViewHolder) holder).setDetailSearchTextView(data);
            ((DetailSearchHistoryViewHolder) holder).setType(data.getType());

            switch (data.getType()){
                case SearchHistoryAdapter.HISTORY:
                    ((DetailSearchHistoryViewHolder) holder).setIconLeft(R.drawable.ic_history_black_24dp);
                    ((DetailSearchHistoryViewHolder) holder).setIconRight(R.drawable.ic_close_black_24dp);
                    break;
                case SearchHistoryAdapter.HOTLIST:
                    ((DetailSearchHistoryViewHolder) holder).setIconLeft(R.drawable.ic_whatshot_black_24dp);
                    ((DetailSearchHistoryViewHolder) holder).hideIconRight();
                    break;
                case SearchHistoryAdapter.SUGGESTION:
                    ((DetailSearchHistoryViewHolder) holder).setIconLeft(R.drawable.ic_search_black_24dp);
                    ((DetailSearchHistoryViewHolder) holder).hideIconRight();
                    break;
                case SearchHistoryAdapter.POPULAR:
                    ((DetailSearchHistoryViewHolder) holder).setIconLeft(R.drawable.ic_call_made);
                    ((DetailSearchHistoryViewHolder) holder).hideIconRight();
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void setDatas (List<SearchHistoryModel.Data> datas){
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
}
