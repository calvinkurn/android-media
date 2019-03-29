package com.tokopedia.common.travel.presentation.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.common.travel.R;

/**
 * @author  by alvarisi on 12/22/17.
 */

public class TravelSearchShimmeringViewHolder extends AbstractViewHolder<LoadingModel> {
    @LayoutRes
    public static int LAYOUT = R.layout.item_travel_search_shimmering;
    private LinearLayout linearLayout;

    public TravelSearchShimmeringViewHolder(View itemView) {
        super(itemView);
        linearLayout = itemView.findViewById(R.id.container);
    }

    @Override
    public void bind(final LoadingModel flightSearchViewModel) {
        LayoutInflater inflater = (LayoutInflater) itemView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View partialView = inflater.inflate(R.layout.partial_travel_search_shimmering_loading, null, false);
        partialView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        linearLayout.removeAllViews();
        if (partialView.getMeasuredHeight() > 0){
            renderLoadingItem(inflater, partialView);
        }
    }

    private void renderLoadingItem(LayoutInflater inflater, View partialView) {
        int numRows = Resources.getSystem().getDisplayMetrics().heightPixels / partialView.getMeasuredHeight();
        for (int i = 1; i < numRows; i++) {
            View newPartialView = inflater.inflate(R.layout.partial_travel_search_shimmering_loading, null, false);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(
                    convertToPixel(itemView.getResources().getDimension(R.dimen.dp_16)),
                    convertToPixel(itemView.getResources().getDimension(R.dimen.dp_8)),
                    convertToPixel(itemView.getResources().getDimension(R.dimen.dp_16)),
                    convertToPixel(itemView.getResources().getDimension(R.dimen.dp_8))
            );
            newPartialView.setLayoutParams(params);
            linearLayout.addView(newPartialView);
        }
    }

    private int convertToPixel(float dp){
        Resources resources = itemView.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX,
                dp,
                resources.getDisplayMetrics()
        );
    }
}
