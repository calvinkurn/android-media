package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.home.R;
import com.tokopedia.home.analytics.HomePageTracking;
import com.tokopedia.home.beranda.helper.DynamicLinkHelper;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.LinearHorizontalSpacingDecoration;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicIconSectionViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HomeIconItem;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.SpotlightItemViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.SpotlightViewModel;
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils;

import java.util.ArrayList;
import java.util.List;

public class SpotlightViewHolder extends AbstractViewHolder<SpotlightViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_spotlight;
    private RecyclerView recyclerView;
    private SpotlightAdapter adapter;

    public SpotlightViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
        adapter = new SpotlightAdapter(itemView.getContext(), listener);
        recyclerView = itemView.findViewById(R.id.list);
        int edgeMargin = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.dp_16);
        int spacingBetween = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.dp_8);
        recyclerView.addItemDecoration(new LinearHorizontalSpacingDecoration(spacingBetween, edgeMargin));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
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
    public void bind(SpotlightViewModel element) {
        adapter.setData(element.getSpotlightItems());
    }

    private static class SpotlightAdapter extends RecyclerView.Adapter<SpotlightItemViewHolder> {

        private final Context context;
        private List<SpotlightItemViewModel> spotlightItemViewModels = new ArrayList<>();
        private HomeCategoryListener listener;

        public SpotlightAdapter(Context context, HomeCategoryListener listener) {
            this.context = context;
            this.listener = listener;
        }

        public void setData(List<SpotlightItemViewModel> spotlightItemViewModels) {
            this.spotlightItemViewModels.clear();
            this.spotlightItemViewModels.addAll(spotlightItemViewModels);
            notifyDataSetChanged();
        }

        @Override
        public SpotlightItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SpotlightItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_spotlight_item, parent, false),
                    listener);
        }

        @Override
        public void onBindViewHolder(SpotlightItemViewHolder holder, final int position) {
            holder.bind(spotlightItemViewModels.get(position), position);
        }

        @Override
        public int getItemCount() {
            return spotlightItemViewModels.size();
        }
    }

    private static class SpotlightItemViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView tag;
        private TextView description;
        private ImageView background;
        private View container;
        private Context context;
        private HomeCategoryListener listener;
        private Typeface titleTypeface;

        public SpotlightItemViewHolder(View itemView, HomeCategoryListener listener) {
            super(itemView);
            context = itemView.getContext();
            title = itemView.findViewById(R.id.spotlightTitle);
            tag = itemView.findViewById(R.id.spotlightTag);
            description = itemView.findViewById(R.id.spotlightDesc);
            background = itemView.findViewById(R.id.spotlightBackground);
            container = itemView.findViewById(R.id.spotlightContainer);
            this.listener = listener;
            titleTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/NunitoSans-ExtraBold.ttf");
        }

        public void bind(SpotlightItemViewModel model, final int position) {
            title.setTypeface(titleTypeface);
            title.setText(model.getTitle());
            if (!TextUtils.isEmpty(model.getTagName())) {
                tag.setText(model.getTagName().toUpperCase());
                tag.setTextColor(Color.parseColor(model.getTagNameHexcolor()));
                ViewCompat.setBackgroundTintList(
                        tag,
                        ColorStateList.valueOf(Color.parseColor(model.getTagHexcolor())));
                tag.setVisibility(View.VISIBLE);
            } else {
                tag.setVisibility(View.GONE);
            }

            SpannableStringBuilder longDescription = new SpannableStringBuilder();
            longDescription.append(model.getDescription());
            longDescription.append(" ");
            int start = longDescription.length();
            longDescription.append(model.getCtaText());
            longDescription.setSpan(new ForegroundColorSpan(Color.parseColor(model.getCtaTextHexcolor())), start, longDescription.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            longDescription.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, longDescription.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            description.setText(longDescription);

            ImageHandler.loadImageFitCenter(context, background, model.getBackgroundImageUrl());

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    eventClickSpotlight(view.getContext(), model, position);
                    listener.onSpotlightItemClicked(DynamicLinkHelper.getActionLink(model));

                }
            });
        }

        private void eventClickSpotlight(Context context, SpotlightItemViewModel model, int position) {
            HomePageTracking.eventEnhancedClickDynamicChannelHomePage(context, model.getEnhanceClickSpotlightHomePage(position));
        }
    }
}

