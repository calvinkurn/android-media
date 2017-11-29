package com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.beranda.domain.model.brands.BrandDataModel;
import com.tokopedia.tkpd.beranda.domain.model.category.CategoryLayoutRowModel;
import com.tokopedia.tkpd.beranda.listener.HomeCategoryListener;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.CategoryItemViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class CategoryItemViewHolder extends AbstractViewHolder<CategoryItemViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_category;
    @BindView(R.id.title)
    TextView titleTxt;
    @BindView(R.id.list)
    RecyclerView recyclerView;
    @BindView(R.id.see_more)
    TextView seeMoreBtn;

    private Context context;
    private ItemAdapter adapter;
    private int spanCount = 2;
    private int limitItem = 6;
    private HomeCategoryListener listener;
    private List<CategoryLayoutRowModel> rowModelList = new ArrayList<>();

    public CategoryItemViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.listener = listener;
        this.context = itemView.getContext();
        adapter = new ItemAdapter(context);
        recyclerView.setLayoutManager(new GridLayoutManager(context, spanCount,
                GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bind(final CategoryItemViewModel element) {
        titleTxt.setText(element.getTitle());
        if(rowModelList.isEmpty()) {
            if (element.getItemList().size() > limitItem) {
                for (int i = 0; i < limitItem; i++) {
                    rowModelList.add(element.getItemList().get(i));
                }
                seeMoreBtn.setVisibility(View.VISIBLE);
                int count = element.getItemList().size() - rowModelList.size();
                seeMoreBtn.setText(String.format(context.getString(R.string.format_btn_category_more), count));
            } else {
                seeMoreBtn.setVisibility(View.GONE);
                rowModelList.addAll(element.getItemList());
            }
            adapter.setData(rowModelList);
            seeMoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rowModelList = element.getItemList();
                    adapter.setData(rowModelList);
                    seeMoreBtn.setVisibility(View.GONE);
                }
            });
        }
    }


    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

        private final Context context;
        private List<CategoryLayoutRowModel> data;

        public ItemAdapter(Context context) {
            this.context = context;
            this.data = new ArrayList<>();
        }

        public void setData(List<CategoryLayoutRowModel> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_category_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, final int position) {
            holder.title.setText(data.get(position).getName());
            Glide.with(context).load(data.get(position).getImageUrl()).into(holder.icon);
            holder.conteiner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCategoryItemClicked(data.get(position), getAdapterPosition(), position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.icon)
            ImageView icon;
            @BindView(R.id.title)
            TextView title;
            @BindView(R.id.container)
            LinearLayout conteiner;

            public ItemViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
