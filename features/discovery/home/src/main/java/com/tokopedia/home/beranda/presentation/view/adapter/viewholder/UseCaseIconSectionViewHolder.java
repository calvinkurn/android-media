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
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HomeIconItem;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.UseCaseIconSectionViewModel;
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class UseCaseIconSectionViewHolder extends AbstractViewHolder<UseCaseIconSectionViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_use_case_icon_section;
    private RecyclerView recyclerView;
    private UseCaseIconAdapter adapter;
    private static final int spanCount = 5;

    public UseCaseIconSectionViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
        adapter = new UseCaseIconAdapter(itemView.getContext(), listener);
        recyclerView = itemView.findViewById(R.id.list);
        recyclerView.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(itemView.getContext(), spanCount,
                GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void bind(UseCaseIconSectionViewModel element) {
        adapter.setSectionViewModel(element);
    }

    public static class UseCaseIconAdapter extends RecyclerView.Adapter<UseCaseIconViewHolder> {

        private final Context context;
        private UseCaseIconSectionViewModel sectionViewModel;
        private HomeCategoryListener listener;

        public UseCaseIconAdapter(Context context, HomeCategoryListener listener) {
            this.context = context;
            this.listener = listener;
        }

        public void setSectionViewModel(UseCaseIconSectionViewModel sectionViewModel) {
            this.sectionViewModel = sectionViewModel;
            notifyDataSetChanged();
            HomeTrackingUtils.homeUsedCaseImpression(context, sectionViewModel.getItemList());
        }

        @Override
        public UseCaseIconViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new UseCaseIconViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_use_case_icon, parent, false));
        }

        @Override
        public void onBindViewHolder(UseCaseIconViewHolder holder, final int position) {
            holder.title.setText(sectionViewModel.getItemList().get(position).getTitle());
            ImageHandler.loadImageThumbs(holder.getContext(), holder.icon, sectionViewModel.getItemList().get(position).getIcon());
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    eventClickUseCase(view.getContext(), sectionViewModel.getItemList().get(position), position);
                    listener.onSectionItemClicked(DynamicLinkHelper.getActionLink(sectionViewModel.getItemList().get(position)));

                }
            });
        }

        private void eventClickUseCase(Context context, HomeIconItem homeIconItem, int position) {
            HomePageTracking.eventClickHomeUseCase(context, homeIconItem.getTitle());

            HomeTrackingUtils.homeUsedCaseClick(context,
                    homeIconItem.getTitle(), position + 1, homeIconItem.getApplink());
        }

        @Override
        public int getItemCount() {
            return sectionViewModel.getItemList().size();
        }
    }

    private static class UseCaseIconViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView icon;
        private TextView title;
        private LinearLayout container;
        private View view;

        public UseCaseIconViewHolder(View itemView) {
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
