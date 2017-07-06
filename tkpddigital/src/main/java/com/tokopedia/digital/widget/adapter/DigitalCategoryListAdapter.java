package com.tokopedia.digital.widget.adapter;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.widget.data.entity.DigitalCategoryItemData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

public class DigitalCategoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HOLDER_CATEGORY_ITEM =
            R.layout.view_holder_digital_category_item_digital_module;
    private static final int TYPE_HOLDER_CATEGORY_ITEM_EMPTY =
            R.layout.view_holder_digital_category_item_empty_digital_module;


    private List<Object> dataList = new ArrayList<>();
    private final Fragment hostFragment;
    private final ActionListener actionListener;
    private final int column;

    public DigitalCategoryListAdapter(Fragment hostFragment,
                                      ActionListener actionListener,
                                      int column) {
        this.hostFragment = hostFragment;
        this.actionListener = actionListener;
        this.column = column;
    }

    public void addAllDataList(List<DigitalCategoryItemData> digitalCategoryItemDataList) {
        int countList = digitalCategoryItemDataList.size();
        int countIncrement = column - (countList % column);
        dataList.clear();
        dataList.addAll(digitalCategoryItemDataList);
        for (int i = 0; i < countIncrement; i++) {
            dataList.add(null);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HOLDER_CATEGORY_ITEM) {
            return new DigitalCategoryItem(LayoutInflater.from(
                    hostFragment.getActivity()).inflate(viewType, parent, false
            ));
        } else if (viewType == TYPE_HOLDER_CATEGORY_ITEM_EMPTY) {
            return new DigitalCategoryItemEmpty(LayoutInflater.from(
                    hostFragment.getActivity()).inflate(viewType, parent, false
            ));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int type = getItemViewType(position);
        if (type == TYPE_HOLDER_CATEGORY_ITEM) {
            DigitalCategoryItem holderItem = (DigitalCategoryItem) holder;
            final DigitalCategoryItemData data = (DigitalCategoryItemData) dataList.get(position);
            holderItem.tvName.setText(data.getName());
            ImageHandler.LoadImage(holderItem.ivIcon, data.getImageUrl());
            holderItem.itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            actionListener.onDigitalCategoryItemClicked(data);
                        }
                    }
            );
        } else {
            DigitalCategoryItemEmpty holderItemEmpty = (DigitalCategoryItemEmpty) holder;
            holderItemEmpty.tvName.setVisibility(View.INVISIBLE);
            holderItemEmpty.ivIcon.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object object = dataList.get(position);
        return object != null && object instanceof DigitalCategoryItemData
                ? TYPE_HOLDER_CATEGORY_ITEM : TYPE_HOLDER_CATEGORY_ITEM_EMPTY;
    }

    static class DigitalCategoryItem extends RecyclerView.ViewHolder {
        @BindView(R2.id.iv_icon)
        ImageView ivIcon;
        @BindView(R2.id.tv_category_name)
        TextView tvName;

        DigitalCategoryItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class DigitalCategoryItemEmpty extends RecyclerView.ViewHolder {
        @BindView(R2.id.iv_icon)
        ImageView ivIcon;
        @BindView(R2.id.tv_category_name)
        TextView tvName;

        DigitalCategoryItemEmpty(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ActionListener {
        void onDigitalCategoryItemClicked(DigitalCategoryItemData itemData);
    }


}
