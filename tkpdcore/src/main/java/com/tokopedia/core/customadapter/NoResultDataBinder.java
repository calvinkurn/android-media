package com.tokopedia.core.customadapter;

import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.core.util.DataBinder;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    private int drawableAsset = R.drawable.status_no_result;

    public NoResultDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public NoResultDataBinder.ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_no_result, null);
        if (isFullScreen) {
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        ImageHandler.loadImageWithId(holder.emptyImage, getDrawableAsset());

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
        @BindView(R2.id.no_result_image)
        ImageView emptyImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}