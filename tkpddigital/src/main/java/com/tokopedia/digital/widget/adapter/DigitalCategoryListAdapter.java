package com.tokopedia.digital.widget.adapter;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
            R.layout.view_holder_digital_category_item_digital_module;


    private List<Object> dataList = new ArrayList<>();
    private Fragment hostFragment;
    private ActionListener actionListener;


    public void addAllDataList(List<DigitalCategoryItemData> digitalCategoryItemDataList) {
        int countList = digitalCategoryItemDataList.size();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

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

    public interface ActionListener {

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
        DigitalCategoryItemEmpty(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
