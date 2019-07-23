package com.tokopedia.gm.featured.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.base.list.seller.view.adapter.BaseEmptyDataBinder;
import com.tokopedia.base.list.seller.view.old.DataBindAdapter;
import com.tokopedia.gm.R;

@Deprecated
public class GMFeatureProductEmptyDataBinder extends BaseEmptyDataBinder {

    private String imageUrl;

    public GMFeatureProductEmptyDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    public GMFeatureProductEmptyDataBinder(DataBindAdapter dataBindAdapter, String imageUrl) {
        super(dataBindAdapter, -1);
        this.imageUrl = imageUrl;
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        ImageHandler imageHandler = new ImageHandler(parent.getContext());
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gm_featured_empty_list, parent, false);
        ImageView imageViewEmptyResult = view.findViewById(R.id.no_result_image);
        imageHandler.loadImage(imageViewEmptyResult, imageUrl);
        return new EmptyViewHolder(view);
    }
}