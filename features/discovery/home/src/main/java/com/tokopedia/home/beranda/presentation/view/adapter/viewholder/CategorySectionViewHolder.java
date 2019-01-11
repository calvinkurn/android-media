package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

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

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.home.analytics.HomePageTracking;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.helper.DynamicLinkHelper;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.GridSpacingItemDecoration;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.CategorySectionViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.LayoutSections;
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class CategorySectionViewHolder extends AbstractViewHolder<CategorySectionViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_category_section;
    private RecyclerView recyclerView;
    private SectionItemAdapter adapter;
    private static final int spanCount = 5;
    private HomeCategoryListener listener;

    public CategorySectionViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
        this.listener = listener;
        adapter = new SectionItemAdapter(itemView.getContext(), listener);
        recyclerView = itemView.findViewById(R.id.list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), spanCount,
                GridLayoutManager.VERTICAL, false));
    }

    @Override
    public void bind(CategorySectionViewModel element) {
        adapter.setSectionViewModel(element);
    }

    public static class SectionItemAdapter extends RecyclerView.Adapter<SectionItemViewHolder> {

        private final Context context;
        private CategorySectionViewModel sectionViewModel;
        private HomeCategoryListener listener;

        public SectionItemAdapter(Context context, HomeCategoryListener listener) {
            this.context = context;
            this.listener = listener;
        }

        public void setSectionViewModel(CategorySectionViewModel sectionViewModel) {
            this.sectionViewModel = sectionViewModel;
            notifyDataSetChanged();
            HomeTrackingUtils.homeUsedCaseImpression(context, sectionViewModel.getSectionList());
        }

        @Override
        public SectionItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SectionItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_section_item, parent, false));
        }

        @Override
        public void onBindViewHolder(SectionItemViewHolder holder, final int position) {
            holder.title.setText(sectionViewModel.getSectionList().get(position).getTitle());
            ImageHandler.loadImageThumbs(holder.getContext(), holder.icon, sectionViewModel.getSectionList().get(position).getIcon());
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    eventClickUseCase(view.getContext(), sectionViewModel.getSectionList().get(position), position);
                    listener.onSectionItemClicked(DynamicLinkHelper.getActionLink(sectionViewModel.getSectionList().get(position)));

                }
            });
        }

        private void eventClickUseCase(Context context, LayoutSections layoutSections, int position) {
            if (layoutSections.getTypeCase() == LayoutSections.ICON_USE_CASE) {
                HomePageTracking.eventClickHomeUseCase(context, layoutSections.getTitle());
            } else {
                HomePageTracking.eventClickDynamicIcons(context, layoutSections.getTitle());

            }
            HomeTrackingUtils.homeUsedCaseClick(context,
                    layoutSections.getTitle(), position + 1, layoutSections.getApplink());

        }

        @Override
        public int getItemCount() {
            return sectionViewModel.getSectionList().size();
        }
    }

    public static class SectionItemViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView icon;
        private TextView title;
        private LinearLayout container;
        private View view;

        public SectionItemViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            container = itemView.findViewById(R.id.container);
        }

        public Context getContext() {
            return view.getContext();
        }
    }
}
