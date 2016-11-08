package com.tokopedia.core.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.home.model.homeMenu.CategoryItemModel;
import com.tokopedia.core.home.model.homeMenu.CategoryMenuModel;

import java.util.ArrayList;

/**
 * @author by mady on 9/23/16.
 */
public class RecyclerViewCategoryMenuAdapter extends
        RecyclerView.Adapter<RecyclerViewCategoryMenuAdapter.ItemRowHolder> {

    private ArrayList<CategoryMenuModel> dataList;
    private Context mContext;
    private int homeMenuWidth;

    private SectionListCategoryAdapter.OnCategoryClickedListener onCategoryClickedListener;
    private SectionListCategoryAdapter.OnGimmicClickedListener onGimmicClickedListener;

    public RecyclerViewCategoryMenuAdapter(
            Context context, ArrayList<CategoryMenuModel> dataList, int homeMenuWidth) {

        this.dataList = dataList;
        this.mContext = context;
        this.homeMenuWidth = homeMenuWidth;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.list_item, null
        );
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, int i) {
        final String sectionName = dataList.get(i).getHeaderTitle();
        ArrayList<CategoryItemModel> singleSectionItems = dataList.get(i).getAllItemsInSection();
        itemRowHolder.itemTitle.setText(sectionName);
        SectionListCategoryAdapter itemListDataAdapter = new SectionListCategoryAdapter(
                mContext, singleSectionItems,
                homeMenuWidth);
        itemListDataAdapter.setCategoryClickedListener(onCategoryClickedListener);
        itemListDataAdapter.setGimmicClickedListener(onGimmicClickedListener);

        itemRowHolder.recycler_view_list.setHasFixedSize(true);
        itemRowHolder.recycler_view_list.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        );

        itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public void setOnGimmicClickedListener(
            SectionListCategoryAdapter.OnGimmicClickedListener onGimmicClickedListener) {

        this.onGimmicClickedListener = onGimmicClickedListener;
    }

    public void setOnCategoryClickedListener(
            SectionListCategoryAdapter.OnCategoryClickedListener onCategoryClickedListener) {

        this.onCategoryClickedListener = onCategoryClickedListener;
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
