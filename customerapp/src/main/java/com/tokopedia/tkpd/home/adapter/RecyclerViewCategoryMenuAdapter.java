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
import com.tokopedia.core.util.NonScrollGridLayoutManager;
import com.tokopedia.core.widgets.DividerItemDecoration;
import com.tokopedia.tkpd.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * @author by mady on 9/23/16.
 * Modified by erry
 */
public class RecyclerViewCategoryMenuAdapter extends
        RecyclerView.Adapter<RecyclerViewCategoryMenuAdapter.ItemRowHolder> {

    private final Context mContext;
    private List<CategoryMenuModel> dataList;
    private int homeMenuWidth;



    private SectionListCategoryAdapter.OnCategoryClickedListener onCategoryClickedListener;
    private SectionListCategoryAdapter.OnGimmicClickedListener onGimmicClickedListener;

    public RecyclerViewCategoryMenuAdapter(Context context) {
        this.mContext = context;
        this.dataList = Collections.emptyList();
    }

    @SuppressWarnings("unused")
    public RecyclerViewCategoryMenuAdapter(
            Context context, ArrayList<CategoryMenuModel> dataList, int homeMenuWidth) {
        this.dataList = dataList;
        this.mContext = context;
        this.homeMenuWidth = homeMenuWidth;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.item_section_home_category, null
        );
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, int i) {
        final String sectionName = dataList.get(i).getHeaderTitle();
        ArrayList<CategoryItemModel> singleSectionItems = dataList.get(i).getAllItemsInSection();
        itemRowHolder.itemTitle.setText(sectionName);
        SectionListCategoryAdapter itemListDataAdapter = new SectionListCategoryAdapter(
                singleSectionItems,
                homeMenuWidth);
        itemListDataAdapter.setCategoryClickedListener(onCategoryClickedListener);
        itemListDataAdapter.setGimmicClickedListener(onGimmicClickedListener);

        itemRowHolder.recycler_view_list.setHasFixedSize(true);
        itemRowHolder.recycler_view_list.setLayoutManager(
                new NonScrollGridLayoutManager(mContext, 2,
                GridLayoutManager.VERTICAL, false));
        itemRowHolder.recycler_view_list.addItemDecoration(new DividerItemDecoration(mContext,R.drawable.divider300));
        itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setOnGimmicClickedListener(
            SectionListCategoryAdapter.OnGimmicClickedListener onGimmicClickedListener) {

        this.onGimmicClickedListener = onGimmicClickedListener;
    }

    public void setOnCategoryClickedListener(
            SectionListCategoryAdapter.OnCategoryClickedListener onCategoryClickedListener) {

        this.onCategoryClickedListener = onCategoryClickedListener;
    }

    public void setDataList(List<CategoryMenuModel> dataList) {
        this.dataList = dataList;
    }

    public void setHomeMenuWidth(int homeMenuWidth) {
        this.homeMenuWidth = homeMenuWidth;
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {

        TextView itemTitle;

        RecyclerView recycler_view_list;

        ItemRowHolder(View view) {
            super(view);

            this.itemTitle = (TextView) view.findViewById(R.id.itemTitle);
            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list);
        }

    }

}