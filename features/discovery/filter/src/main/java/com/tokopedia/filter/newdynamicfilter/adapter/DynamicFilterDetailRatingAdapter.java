package com.tokopedia.filter.newdynamicfilter.adapter;

import android.view.View;
import android.widget.ImageView;

import com.tokopedia.filter.R;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.filter.newdynamicfilter.adapter.viewholder.DynamicFilterDetailViewHolder;
import com.tokopedia.filter.newdynamicfilter.helper.RatingHelper;
import com.tokopedia.filter.newdynamicfilter.view.DynamicFilterDetailView;

/**
 * Created by henrypriyono on 8/24/17.
 */

public class DynamicFilterDetailRatingAdapter extends DynamicFilterDetailAdapter {

    public DynamicFilterDetailRatingAdapter(DynamicFilterDetailView filterDetailView) {
        super(filterDetailView);
    }

    @Override
    protected int getLayout() {
        return R.layout.filter_detail_rating;
    }

    @Override
    protected DynamicFilterDetailViewHolder getViewHolder(View view) {
        return new RatingItemViewHolder(view, filterDetailView);
    }

    private static class RatingItemViewHolder extends DynamicFilterDetailViewHolder {

        private ImageView ratingView;

        public RatingItemViewHolder(View itemView, DynamicFilterDetailView filterDetailView) {
            super(itemView, filterDetailView);
            ratingView = (ImageView) itemView.findViewById(R.id.filter_rating_view);
        }

        @Override
        public void bind(final Option option) {
            super.bind(option);
            int ratingCount = Integer.parseInt(option.getName());
            ratingView.setImageResource(RatingHelper.getRatingDrawable(ratingCount));
        }
    }
}
