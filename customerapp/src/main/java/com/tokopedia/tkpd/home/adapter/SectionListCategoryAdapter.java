package com.tokopedia.tkpd.home.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.network.entity.homeMenu.CategoryItemModel;
import com.tokopedia.tkpd.R;

import java.util.ArrayList;

/**
 * @author by mady on 9/23/16.
 */
public class SectionListCategoryAdapter extends RecyclerView.Adapter<SectionListCategoryAdapter.CategoryMenuRowHolder> {

    private ArrayList<CategoryItemModel> itemsList;
    private OnCategoryClickedListener categoryClickedListener;
    private OnGimmicClickedListener gimmicClickedListener;
    private final int homeMenuWidth;

    SectionListCategoryAdapter(ArrayList<CategoryItemModel> itemsList, int homeMenuWidth) {

        this.itemsList = itemsList;
        this.homeMenuWidth = homeMenuWidth;
    }

    @Override
    public CategoryMenuRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.item_home_category, null
        );
//        int width = v.getMeasuredWidth() / 4;
        v.setMinimumWidth(homeMenuWidth);
        return new CategoryMenuRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final CategoryMenuRowHolder holder, final int i) {

        final CategoryItemModel singleItem = itemsList.get(i);

        holder.linWrapper.getLayoutParams().width = homeMenuWidth;

        holder.tvTitle.setText(singleItem.getName());

        ImageHandler.LoadImage(holder.itemImage, singleItem.getImageUrl());

        holder.view.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               if (singleItem.getType().equals(CategoryItemModel.TYPE.CATEGORY)) {
                                                   categoryClickedListener.onCategoryClicked(singleItem, holder.getAdapterPosition());
                                               } else {
                                                   if (CategoryItemModel.TYPE.DIGITAL.equals(singleItem.getType())) {
                                                       gimmicClickedListener.onDigitalCategoryClicked(singleItem);
                                                   } else {
                                                       gimmicClickedListener.onGimmicClicked(singleItem);

                                                   }
                                               }
                                           }
                                       }
        );

        if (i % 2 != 0) {
            holder.sparator.setVisibility(View.GONE);
        } else {
            holder.sparator.setVisibility(View.VISIBLE);
        }
    }


    void setCategoryClickedListener(OnCategoryClickedListener categoryClickedListener) {
        this.categoryClickedListener = categoryClickedListener;
    }

    void setGimmicClickedListener(OnGimmicClickedListener gimmicClickedListener) {
        this.gimmicClickedListener = gimmicClickedListener;
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }


    class CategoryMenuRowHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;

        ImageView itemImage;
        LinearLayout linWrapper;
        View sparator;
        protected View view;

        CategoryMenuRowHolder(View view) {
            super(view);
            this.view = view;
            this.sparator = view.findViewById(R.id.sparator);
            this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);

            this.linWrapper = (LinearLayout) view.findViewById(R.id.linWrapper);

        }

    }

    public interface OnCategoryClickedListener {
        void onCategoryClicked(CategoryItemModel categoryItemModel, int position);
    }

    public interface OnGimmicClickedListener {
        void onGimmicClicked(CategoryItemModel categoryItemModel);

        void onDigitalCategoryClicked(CategoryItemModel itemModel);
    }
}
