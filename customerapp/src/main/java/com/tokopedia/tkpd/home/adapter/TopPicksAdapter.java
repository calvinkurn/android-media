package com.tokopedia.tkpd.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.network.entity.homeMenu.CategoryItemModel;
import com.tokopedia.core.network.entity.homeMenu.CategoryMenuModel;
import com.tokopedia.core.network.entity.topPicks.Group;
import com.tokopedia.core.network.entity.topPicks.Item;
import com.tokopedia.core.network.entity.topPicks.Toppick;
import com.tokopedia.core.util.NonScrollGridLayoutManager;
import com.tokopedia.core.widgets.DividerItemDecoration;
import com.tokopedia.tkpd.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Alifa on 12/29/2016.
 */

public class  TopPicksAdapter extends
        RecyclerView.Adapter<TopPicksAdapter.ItemRowHolder> {

    private final Context mContext;
    private List<Group> dataList;
    private int homeMenuWidth;

    private TopPicksItemAdapter.OnItemClickedListener onItemClickedListener;
    private TopPicksItemAdapter.OnTitleClickedListener onTitleClickedListener;
    private TopPicksAdapter.OnClickViewAll onClickViewAll;

    public TopPicksAdapter(Context context) {
        this.mContext = context;
        this.dataList = Collections.emptyList();
    }

    @SuppressWarnings("unused")
    public TopPicksAdapter(
            Context context, ArrayList<Group> dataList, int homeMenuWidth) {
        this.dataList = dataList;
        this.mContext = context;
        this.homeMenuWidth = homeMenuWidth;
    }

    @Override
    public TopPicksAdapter.ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.item_toppicks_category, null
        );
        return new TopPicksAdapter.ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(TopPicksAdapter.ItemRowHolder itemRowHolder, int i) {
        final Group toppickGroup = dataList.get(i);
        final Toppick firstTopPicks = dataList.get(0).getToppicks().get(i);
        itemRowHolder.itemTitle.setText(toppickGroup.getName());
        TopPicksItemAdapter itemAdapter = new TopPicksItemAdapter(
                dataList.get(0).getToppicks().get(i),
                homeMenuWidth);
        itemAdapter.setItemClickedListener(onItemClickedListener);
        itemAdapter.setTitleClickedListener(onTitleClickedListener);
        itemRowHolder.recycler_view_list.setHasFixedSize(true);
        itemRowHolder.recycler_view_list.setLayoutManager(
                new NonScrollGridLayoutManager(mContext, 2,
                        GridLayoutManager.VERTICAL, false));
        itemRowHolder.recycler_view_list.addItemDecoration(new DividerItemDecoration(mContext));
        itemRowHolder.recycler_view_list.setAdapter(itemAdapter);
                itemRowHolder.viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickViewAll.onClick(firstTopPicks);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setOnGimmicClickedListener(
            TopPicksItemAdapter.OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    public void setOnCategoryClickedListener(
            TopPicksItemAdapter.OnTitleClickedListener onTitleClickedListener) {
        this.onTitleClickedListener = onTitleClickedListener;
    }

    public void setDataList(List<Group> dataList) {
        this.dataList = dataList;
    }

    public void setHomeMenuWidth(int homeMenuWidth) {
        this.homeMenuWidth = homeMenuWidth;
    }

    public OnClickViewAll getOnClickViewAll() {
        return onClickViewAll;
    }

    public void setOnClickViewAll(OnClickViewAll onClickViewAll) {
        this.onClickViewAll = onClickViewAll;
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {

        TextView itemTitle;
        RecyclerView recycler_view_list;
        TextView viewAll;

        ItemRowHolder(View view) {
            super(view);
            this.itemTitle = (TextView) view.findViewById(R.id.itemTitle);
            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list);
            this.viewAll = (TextView) view.findViewById(R.id.view_all);
        }

    }

    public interface OnClickViewAll {
        void onClick(Toppick toppick);
    }

}