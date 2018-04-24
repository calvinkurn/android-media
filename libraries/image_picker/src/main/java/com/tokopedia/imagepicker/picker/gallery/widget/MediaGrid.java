package com.tokopedia.imagepicker.picker.gallery.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.picker.gallery.model.MediaItem;

import java.util.ArrayList;

/**
 * Created by hangnadi on 5/29/17.
 */

public class MediaGrid extends SquareFrameLayout implements View.OnClickListener {

    private ImageView mThumbnail;
    private TextView mGifTag;
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
        LayoutInflater.from(context).inflate(R.layout.media_grid_content, this, true);

        mThumbnail = (ImageView) findViewById(R.id.media_thumbnail);
        mGifTag = (TextView) findViewById(R.id.gif);
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

    public void bindMedia(MediaItem item, ArrayList<Long> selectionIdList) {
        mMedia = item;
        setGifTag();
        setImage();
        setVideoDuration();
        setSelection(selectionIdList);
    }

    private void setGifTag() {
        mGifTag.setVisibility(mMedia.isGif() ? View.VISIBLE : View.GONE);
    }

    private void setImage() {
        // TODO change glide to ImageHandler
        if (mMedia.isGif()) {
            Glide.with(getContext())
                    .load(mMedia.getContentUri())
                    .asBitmap()
                    .placeholder(mPreBindInfo.mPlaceholder)
                    .override(mPreBindInfo.mResize, mPreBindInfo.mResize)
                    .centerCrop()
                    .into(mThumbnail);
        } else {
            Glide.with(getContext())
                    .load(mMedia.getContentUri())
                    .asBitmap()  // some .jpeg files are actually gif
                    .placeholder(mPreBindInfo.mPlaceholder)
                    .override(mPreBindInfo.mResize, mPreBindInfo.mResize)
                    .centerCrop()
                    .into(mThumbnail);
        }
    }

    private void setVideoDuration() {
        if (mMedia.isVideo()) {
            mVideoDuration.setVisibility(VISIBLE);
            mVideoDuration.setText(DateUtils.formatElapsedTime(mMedia.getDuration() / 1000));
        } else {
            mVideoDuration.setVisibility(GONE);
        }
    }

    private void setSelection(ArrayList<Long> selectionIdList){
        if ( selectionIdList.contains(mMedia.getId())){
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
        Drawable mPlaceholder;
        RecyclerView.ViewHolder mViewHolder;

        public PreBindInfo(int resize, Drawable placeholder,
                           RecyclerView.ViewHolder viewHolder) {
            mResize = resize;
            mPlaceholder = placeholder;
            mViewHolder = viewHolder;
        }
    }

}
