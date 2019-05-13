package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.home.R;
import com.tokopedia.home.analytics.HomePageTracking;
import com.tokopedia.home.beranda.helper.DynamicLinkHelper;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.DynamicIconDecoration;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicIconSectionViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HomeIconItem;
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class DynamicIconSectionViewHolder extends AbstractViewHolder<DynamicIconSectionViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_dynamic_icon_section;
    private RecyclerView recyclerView;
    private DynamicIconAdapter adapter;

    public DynamicIconSectionViewHolder(View itemView,
                                        HomeCategoryListener listener){
        super(itemView);
        adapter = new DynamicIconAdapter(itemView.getContext(), listener);
        recyclerView = itemView.findViewById(R.id.list);

        WindowManager windowManager = (WindowManager) itemView.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        recyclerView.addItemDecoration(new DynamicIconDecoration(
                itemView.getContext().getResources().getDimensionPixelSize(R.dimen.dp_16),
                width,
                5,
                itemView.getContext().getResources().getDimensionPixelOffset(
                        R.dimen.use_case_and_dynamic_icon_size
                )
        ));
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.clearOnScrollListeners();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    if (listener != null) {
                        listener.onDynamicIconScrollStart();
                    }
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (listener != null) {
                        listener.onDynamicIconScrollEnd();
                    }
                }
            }
        });
    }

    @Override
    public void bind(DynamicIconSectionViewModel element) {
        adapter.setSectionViewModel(element);
    }

    private static class DynamicIconAdapter extends RecyclerView.Adapter<DynamicIconViewHolder> {

        private final Context context;
        private DynamicIconSectionViewModel sectionViewModel;
        private HomeCategoryListener listener;

        public DynamicIconAdapter(Context context, HomeCategoryListener listener) {
            this.context = context;
            this.listener = listener;
        }

        public void setSectionViewModel(DynamicIconSectionViewModel sectionViewModel) {
            this.sectionViewModel = sectionViewModel;
            notifyDataSetChanged();
            HomeTrackingUtils.homeUsedCaseImpression(context, sectionViewModel.getItemList());
        }

        @Override
        public DynamicIconViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DynamicIconViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_use_case_and_dynamic_icon, parent, false));
        }

        @Override
        public void onBindViewHolder(DynamicIconViewHolder holder, final int position) {
            holder.title.setText(sectionViewModel.getItemList().get(position).getTitle());
            ImageHandler.loadImageThumbs(holder.getContext(), holder.icon, sectionViewModel.getItemList().get(position).getIcon());
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    eventClickDynamicIcon(view.getContext(), sectionViewModel.getItemList().get(position), position);
                    listener.onSectionItemClicked(DynamicLinkHelper.getActionLink(sectionViewModel.getItemList().get(position)));

                }
            });
        }

        private void eventClickDynamicIcon(Context context, HomeIconItem homeIconItem, int position) {
            HomePageTracking.eventEnhancedClickDynamicIconHomePage(context,
                    homeIconItem.getEnhanceClickDynamicIconHomePage(position));

            HomeTrackingUtils.homeUsedCaseClick(context,
                    homeIconItem.getTitle(), position + 1, homeIconItem.getApplink());
        }

        @Override
        public int getItemCount() {
            return sectionViewModel.getItemList().size();
        }
    }

    private static class DynamicIconViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView icon;
        private TextView title;
        private LinearLayout container;
        private View view;

        public DynamicIconViewHolder(View itemView) {
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
