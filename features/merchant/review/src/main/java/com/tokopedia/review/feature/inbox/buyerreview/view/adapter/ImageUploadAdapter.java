package com.tokopedia.review.feature.inbox.buyerreview.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.review.R;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.ImageUpload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nisie on 2/16/16.
 */
public class ImageUploadAdapter extends RecyclerView.Adapter<ImageUploadAdapter.ViewHolder> {

    private static final int VIEW_UPLOAD_BUTTON = 100;
    private static final int VIEW_REVIEW_IMAGE = 97;
    private static final int MAX_IMAGE = 5;
    public static final int RADIUS_CORNER = 4;
    private int canUpload = 0;
    private boolean isReviewImage;

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image_upload);
        }
    }

    public interface ProductImageListener {
        View.OnClickListener onUploadClicked(int position);

        View.OnClickListener onImageClicked(int position, ImageUpload imageUpload);
    }

    private ProductImageListener listener;
    private ArrayList<ImageUpload> data;
    private ArrayList<ImageUpload> deletedImage;

    public ImageUploadAdapter(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
        this.deletedImage = new ArrayList<>();
    }

    public Context context;

    public static ImageUploadAdapter createAdapter(Context context) {
        return new ImageUploadAdapter(context);
    }

    public void setReviewImage(boolean isReviewImage){
        this.isReviewImage = isReviewImage;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        ViewHolder viewHolder;
        switch (viewType) {
            case VIEW_REVIEW_IMAGE :
                viewHolder = new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.review_listview_image_review_item, viewGroup, false));
                break;
            default:
                viewHolder = new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.review_listview_image_upload_review, viewGroup, false));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_UPLOAD_BUTTON:
                bindUploadButton(holder, position);
                break;
            default:
                bindImage(holder, position);
                break;
        }
    }

    private void bindImage(ViewHolder holder, int position) {

        try {
            if (data.get(position).getFileLoc() == null) {
                ImageHandler.loadImageRounded2(holder.image.getContext(),holder.image, data.get(position).getPicSrc(), convertDpToPx(holder.image.getContext(), RADIUS_CORNER));
            } else {
                Glide.with(holder.image.getContext())
                        .asBitmap()
                        .load(new File(data.get(position).getFileLoc()))
                        .centerCrop()
                        .into(getRoundedImageViewTarget(holder.image, convertDpToPx(holder.image.getContext(), RADIUS_CORNER)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.image.setOnClickListener(listener.onImageClicked(position, data.get(position)));

        setBorder(holder, position);

    }

    public float convertDpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    private static BitmapImageViewTarget getRoundedImageViewTarget(final ImageView imageView, final float radius) {
        return new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(imageView.getContext().getResources(), resource);
                circularBitmapDrawable.setCornerRadius(radius);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        };
    }

    private void setBorder(ViewHolder holder, int position) {
        if (data.get(position).isSelected()) {
            holder.image.setBackgroundColor(context.getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_G500));
        } else {
            holder.image.setBackgroundColor(context.getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0));
        }
    }

    private void bindUploadButton(ViewHolder holder, int position) {
        holder.image.setOnClickListener(listener.onUploadClicked(position));
    }

    @Override
    public int getItemCount() {
        if (data.size() < 5) {
            return data.size() + canUpload;
        } else {
            return data.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == data.size() && data.size() < MAX_IMAGE) {
            return VIEW_UPLOAD_BUTTON;
        } else if (isReviewImage) {
            return VIEW_REVIEW_IMAGE;
        }
        else {
            return super.getItemViewType(position);
        }
    }

    public void addList(ArrayList<ImageUpload> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }


    public ArrayList<ImageUpload> getList() {
        return this.data;
    }

    public void addImage(ImageUpload image) {
        data.add(image);
        notifyDataSetChanged();
    }

    public void removeImage(int currentPosition) {
        data.remove(currentPosition);
        for (int i = currentPosition; i < data.size(); i++) {
            data.get(i).setPosition(i);
        }
        notifyDataSetChanged();
    }

    public void setListener(ProductImageListener listener) {
        this.listener = listener;
    }

    public void setCanUpload(boolean canUpload) {
        this.canUpload = canUpload ? 1 : 0;
    }


    public List<ImageUpload> getDeletedList() {
        return deletedImage;
    }

    public void setDeletedList(List<ImageUpload> deletedImage) {
        this.deletedImage.clear();
        this.deletedImage.addAll(deletedImage);
    }

}
