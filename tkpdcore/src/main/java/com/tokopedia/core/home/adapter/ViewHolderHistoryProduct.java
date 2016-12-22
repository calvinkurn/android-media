package com.tokopedia.core.home.adapter;

import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.home.fragment.FragmentIndexMainHeader;

/**
 * @author Kulomady on 11/22/16.
 */
public class ViewHolderHistoryProduct extends RecyclerView.ViewHolder {

    public HistoryProductRecyclerViewAdapter historyAdapter;
    public TextView title;
    public TextView seeAll;
    public TextView findNow;
    public RecyclerView recyclerView;
    public RelativeLayout emptyLayout;
    public RelativeLayout emptyHistory;
    public TextView findFavoriteShop;

    public ViewHolderHistoryProduct(FragmentIndexMainHeader header) {
        super(header.getParentView());
        title = header.getTitle();
        seeAll = header.getSeeAll();
        findNow = header.getFindNow();
        recyclerView = header.getRecyclerView();
        emptyLayout = header.getEmptyLayout();
        emptyHistory = header.getEmptyHistory();
        historyAdapter = header.getAdapter();
        findFavoriteShop = header.getFindFavoriteShop();
    }
}
