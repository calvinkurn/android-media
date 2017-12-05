package com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.beranda.listener.HomeCategoryListener;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.itemdecoration.GridSpacingItemDecoration;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.CategorySectionViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class CategorySectionViewHolder extends AbstractViewHolder<CategorySectionViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_category_section;
    @BindView(R.id.list)
    RecyclerView recyclerView;
    private SectionItemAdapter adapter;
    private Context context;
    private static final int spanCount = 5;
    private HomeCategoryListener listener;

    public CategorySectionViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = itemView.getContext();
        this.listener = listener;
        adapter = new SectionItemAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, spanCount,
                GridLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount,
                context.getResources().getDimensionPixelSize(R.dimen.home_card_category_item_margin), true));
    }

    @Override
    public void bind(CategorySectionViewModel element) {
        adapter.setSectionViewModel(element);
    }

    public class SectionItemAdapter extends RecyclerView.Adapter<SectionItemViewHolder> {

        private CategorySectionViewModel sectionViewModel;

        public SectionItemAdapter() {
        }

        public void setSectionViewModel(CategorySectionViewModel sectionViewModel) {
            this.sectionViewModel = sectionViewModel;
            notifyDataSetChanged();
        }

        @Override
        public SectionItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SectionItemViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_section_item, parent, false));
        }

        @Override
        public void onBindViewHolder(SectionItemViewHolder holder, final int position) {
            holder.title.setText(sectionViewModel.getSectionList().get(position).getTitle());
            holder.icon.setImageResource(sectionViewModel.getSectionList().get(position).getIcon());
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onSectionItemClicked(sectionViewModel.getSectionList().get(position),
                            getAdapterPosition(), position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return sectionViewModel.getSectionList().size();
        }
    }

    public class SectionItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.icon)
        AppCompatImageView icon;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.container)
        LinearLayout container;

        public SectionItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
