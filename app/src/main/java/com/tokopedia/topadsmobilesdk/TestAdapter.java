package com.tokopedia.topadsmobilesdk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author by errysuprayogi on 4/11/17.
 */

public class TestAdapter extends BaseRecyclerViewAdapter {

    public TestAdapter(Context context, List<RecyclerViewItem> data) {
        super(context, data);
    }

    public void addHeader(){
        data.clear();
        data.add(0, new DummyData("Header", TkpdState.RecyclerView.VIEW_BANNER_HOT_LIST));
//        notifyDataSetChanged();
    }


    public void setDatas(List<DummyData> list) {
//        data = new ArrayList<RecyclerViewItem>(list);
        data.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(List<DummyData> list) {
        final int positionStart = getItemCount();
        data.addAll(list);
        notifyItemRangeInserted(positionStart, list.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                      final int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_item, parent, false);

        switch (viewType) {
            case TkpdState.RecyclerView.VIEW_PRODUCT:
                return new DemoViewHolder(itemView);
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_1:
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_2:
                return new DemoViewHolder(itemView);
            case TkpdState.RecyclerView.VIEW_BANNER_HOT_LIST:
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_header, parent, false));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        try {
            Log.d("TestAdapter ", viewHolder.getClass().getName());
            switch (getItemViewType(position)) {
                case TkpdState.RecyclerView.VIEW_PRODUCT:
                case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_1:
                case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_2:
                    DummyData dummyData = (DummyData) data.get(position);
                    ((DemoViewHolder) viewHolder).textView.setText(dummyData.getTitle());
                    break;
                case TkpdState.RecyclerView.VIEW_BANNER_HOT_LIST:

                    break;
                default:
                    super.onBindViewHolder(viewHolder, position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getItemId(final int position) {
        return (long) position;
    }

    @Override
    public int getItemViewType(int position) {
        if (checkDataSize(position)) {
            RecyclerViewItem recyclerViewItem = data.get(position);
            return isInType(recyclerViewItem);
        } else {
            return super.getItemViewType(position);
        }
    }

    protected int isInType(RecyclerViewItem recyclerViewItem) {
        switch (recyclerViewItem.getType()) {
            case TkpdState.RecyclerView.VIEW_PRODUCT:
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_1:
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_2:
            case TkpdState.RecyclerView.VIEW_BANNER_HOT_LIST:
                return recyclerViewItem.getType();
            default:
                return -1;
        }
    }

    public boolean isHotListBanner(int position) {
        if (checkDataSize(position))
            return data.get(position).getType() == TkpdState.RecyclerView.VIEW_BANNER_HOT_LIST;
        return false;
    }

    public boolean isCategoryHeader(int position) {
        if (checkDataSize(position))
            return (data.get(position).getType() == TkpdState.RecyclerView.VIEW_CATEGORY_HEADER
                    || data.get(position).getType() == TkpdState.RecyclerView.VIEW_CATEGORY_REVAMP_HEADER);
        return false;
    }

    public boolean isEmptySearch(int position) {
        if (checkDataSize(position))
            return data.get(position).getType() == TkpdState.RecyclerView.VIEW_EMPTY_SEARCH;
        return false;
    }

    private boolean checkDataSize(int position) {
        return data != null && data.size() > 0 && position > -1 && position < data.size();
    }

    /**
     * A view holder for R.layout.simple_list_item_1
     */
    static class DemoViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;

        public DemoViewHolder(final View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(final View itemView) {
            super(itemView);
        }
    }
}