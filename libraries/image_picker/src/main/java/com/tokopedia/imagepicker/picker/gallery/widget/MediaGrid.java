package com.tokopedia.imagepicker.picker.gallery.widget;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.picker.gallery.model.MediaItem;

import java.util.ArrayList;

/**
 * Created by hangnadi on 5/29/17.
 */

public class MediaGrid extends SquareFrameLayout implements View.OnClickListener {

    private ImageView mThumbnail;
    private TextView mVideoDuration;

    private MediaItem mMedia;
    private PreBindInfo mPreBindInfo;
    private OnMediaGridClickListener mListener;
    private View ivCheck;

    public MediaGrid(Context context) {
        super(context);
        init(context);
    }

    public MediaGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.media_grid_picker, this, true);

        mThumbnail = (ImageView) findViewById(R.id.media_thumbnail);
        mVideoDuration = (TextView) findViewById(R.id.video_duration);
        ivCheck = findViewById(R.id.iv_check);

        mThumbnail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onThumbnailClicked(mThumbnail, mMedia, mPreBindInfo.mViewHolder);
        }
    }

    public void preBindMedia(PreBindInfo info) {
        mPreBindInfo = info;
    }

    public void bindMedia(MediaItem item, ArrayList<String> selectionIdList) {
        mMedia = item;
        setImage();
        setVideoDuration();
        setSelection(selectionIdList);
    }

    private void setImage() {
        Glide.with(getContext())
                .load(mMedia.getContentUri())
                .placeholder(mPreBindInfo.mPlaceholder)
                .error(mPreBindInfo.error)
                .override(mPreBindInfo.mResize, mPreBindInfo.mResize)
                .centerCrop()
                .into(mThumbnail);
    }

    private void setVideoDuration() {
        if (mMedia.isVideo()) {
            mVideoDuration.setVisibility(VISIBLE);
            mVideoDuration.setText(DateUtils.formatElapsedTime(mMedia.getDuration() / 1000));
        } else {
            mVideoDuration.setVisibility(GONE);
        }
    }

    private void setSelection(ArrayList<String> selectionIdList) {
        if (selectionIdList.contains(mMedia.getRealPath())) {
            ivCheck.setVisibility(View.VISIBLE);
        } else {
            ivCheck.setVisibility(View.GONE);
        }
    }

    public void setOnMediaGridClickListener(OnMediaGridClickListener listener) {
        mListener = listener;
    }

    public interface OnMediaGridClickListener {

        void onThumbnailClicked(ImageView thumbnail, MediaItem item, RecyclerView.ViewHolder holder);

    }

    public static class PreBindInfo {
        int mResize;
        int mPlaceholder;
        int error;
        RecyclerView.ViewHolder mViewHolder;

        public PreBindInfo(int resize, int placeholder, int error,
                           RecyclerView.ViewHolder viewHolder) {
            mResize = resize;
            mPlaceholder = placeholder;
            this.error = error;
            mViewHolder = viewHolder;
        }
    }

}
