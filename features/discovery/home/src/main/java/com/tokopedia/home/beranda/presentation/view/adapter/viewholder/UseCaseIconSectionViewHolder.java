package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.R;
import com.tokopedia.home.analytics.HomePageTracking;
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon;
import com.tokopedia.home.beranda.helper.DynamicLinkHelper;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.UseCaseIconSectionDataModel;
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils;
import com.tokopedia.media.loader.JvmMediaLoader;
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy;

import androidx.annotation.LayoutRes;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author by errysuprayogi on 11/28/17.
 */
/**
 * Use dynamic icon section instead of use case icon
 */

@Deprecated
public class UseCaseIconSectionViewHolder extends AbstractViewHolder<UseCaseIconSectionDataModel> {

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
    public void bind(UseCaseIconSectionDataModel element) {
        adapter.setSectionViewModel(element);
    }

    public static class UseCaseIconAdapter extends RecyclerView.Adapter<UseCaseIconViewHolder> {

        private final Context context;
        private UseCaseIconSectionDataModel sectionViewModel;
        private HomeCategoryListener listener;

        public UseCaseIconAdapter(Context context, HomeCategoryListener listener) {
            this.context = context;
            this.listener = listener;
        }

        public void setSectionViewModel(UseCaseIconSectionDataModel sectionViewModel) {
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
            holder.title.setText(sectionViewModel.getItemList().get(position).getName());
            JvmMediaLoader.loadImage(holder.icon, sectionViewModel.getItemList().get(position).getImageUrl(), properties -> {
                properties.setCacheStrategy(MediaCacheStrategy.RESOURCE);
                return null;
            });
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    eventClickUseCase(view.getContext(), sectionViewModel.getItemList().get(position), position);
                    listener.onSectionItemClicked(DynamicLinkHelper.getActionLink(sectionViewModel.getItemList().get(position)));

                }
            });
        }

        private void eventClickUseCase(Context context, DynamicHomeIcon.DynamicIcon homeIconItem, int position) {
            HomePageTracking.eventClickHomeUseCase(homeIconItem.getName());

            HomeTrackingUtils.homeUsedCaseClick(context,
                    homeIconItem.getName(), position + 1, homeIconItem.getApplinks());
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
