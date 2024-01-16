package com.tokopedia.datepicker.range.view.base;

import androidx.annotation.DrawableRes;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import com.tokopedia.base.list.seller.R;
import com.tokopedia.media.loader.JvmMediaLoader;

/**
 * Created by Nisie on 2/26/16.
 */

/**
 * Use base adapter with visitor pattern from tkpd abstraction
 */
@Deprecated
public class NoResultDataBinder extends DataBinder<NoResultDataBinder.ViewHolder> {

    protected boolean isFullScreen = false;
    @DrawableRes
    private int drawableAsset = com.tokopedia.abstraction.R.drawable.status_no_result;

    public NoResultDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.tokopedia.baselist.R.layout.item_view_no_result, null);
        if (isFullScreen) {
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        JvmMediaLoader.loadImage(holder.emptyImage, getDrawableAsset());
    }

    @DrawableRes
    public int getDrawableAsset() {
        return drawableAsset;
    }

    public void setDrawableAsset(@DrawableRes int drawableAsset) {
        this.drawableAsset = drawableAsset;
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void setIsFullScreen(boolean isFullScreen) {
        this.isFullScreen = isFullScreen;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView emptyImage;

        public ViewHolder(View itemView) {
            super(itemView);
            emptyImage = itemView.findViewById(com.tokopedia.baselist.R.id.no_result_image);

        }
    }
}