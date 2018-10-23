package com.tokopedia.feedplus.view.adapter.viewholder.kol;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.analytics.FeedAnalytics;
import com.tokopedia.feedplus.view.analytics.FeedEnhancedTracking;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.feedplus.view.viewmodel.kol.ProductCommunicationItemViewModel;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by milhamj on 08/05/18.
 */

public class ProductCommunicationAdapter
        extends RecyclerView.Adapter<ProductCommunicationAdapter.ViewHolder> {

    private final int rowNumber;
    private final FeedAnalytics analytics;
    private List<ProductCommunicationItemViewModel> itemViewModels;
    private FeedPlus.View viewListener;

    public ProductCommunicationAdapter(int rowNumber, FeedPlus.View viewListener, FeedAnalytics analytics) {
        this.rowNumber = rowNumber;
        this.viewListener = viewListener;
        this.analytics = analytics;
        itemViewModels = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_communication_item, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (holder.parentView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams =
                    (ViewGroup.MarginLayoutParams) holder.parentView.getLayoutParams();
            Resources resources = holder.parentView.getContext().getResources();

            if (holder.getAdapterPosition() == 0) {
                marginLayoutParams.leftMargin = (int) resources.getDimension(R.dimen.dp_16);
                marginLayoutParams.rightMargin = 0;
            } else if (holder.getAdapterPosition() == getItemCount() - 1) {
                marginLayoutParams.leftMargin = (int) resources.getDimension(R.dimen.dp_8);
                marginLayoutParams.rightMargin = (int) resources.getDimension(R.dimen.dp_8);
            } else {
                marginLayoutParams.leftMargin = (int) resources.getDimension(R.dimen.dp_8);
                marginLayoutParams.rightMargin = 0;
            }
        }

        ImageHandler.loadImage2(holder.image,
                itemViewModels.get(position).getImageUrl(),
                R.drawable.ic_loading_image);

        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                viewListener.onContentProductLinkClicked(
                        itemViewModels.get(adapterPosition).getRedirectUrl());

                doEnhancedTracking(itemViewModels.get(adapterPosition));
            }
        });
    }

    private void doEnhancedTracking(ProductCommunicationItemViewModel item) {
        UserSessionInterface userSession = viewListener.getUserSession();
        int loginId = Integer.valueOf(
                !TextUtils.isEmpty(userSession.getUserId()) ? userSession.getUserId() : "0"
        );

        List<FeedEnhancedTracking.Promotion> list = new ArrayList<>();
        list.add(new FeedEnhancedTracking.Promotion(
                item.getActivityId(),
                FeedEnhancedTracking.Promotion.createContentNameBanner(),
                item.getImageUrl(),
                rowNumber,
                String.valueOf(itemViewModels.size()),
                item.getActivityId(),
                item.getRedirectUrl()
        ));

        analytics.eventTrackingEnhancedEcommerce(
                FeedEnhancedTracking.getClickTracking(list, loginId)
        );
    }

    @Override
    public int getItemCount() {
        return itemViewModels.size();
    }

    public void setData(List<ProductCommunicationItemViewModel> itemViewModels) {
        this.itemViewModels = itemViewModels;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View parentView;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            parentView = itemView;
            image = itemView.findViewById(R.id.banner);
        }
    }
}
