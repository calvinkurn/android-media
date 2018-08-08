package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nisie on 2/16/16.
 */
public class ImageUploadAdapter extends RecyclerView.Adapter<ImageUploadAdapter.ViewHolder> {

    private static final int VIEW_UPLOAD_BUTTON = 100;
    private static final int MAX_IMAGE = 5;
    private int canUpload = 0;

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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.listview_image_upload, viewGroup, false));
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
                ImageHandler.loadImageRounded2(holder.image.getContext(),holder.image, data.get(position).getPicSrc());
            } else {
                Glide.with(holder.image.getContext())
                        .load(new File(data.get(position).getFileLoc()))
                        .asBitmap()
                        .centerCrop()
                        .into(getRoundedImageViewTarget(holder.image, 5.0f));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.image.setOnClickListener(listener.onImageClicked(position, data.get(position)));

        setBorder(holder, position);

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
            holder.image.setBackgroundColor(context.getResources().getColor(R.color.green_500));
        } else {
            holder.image.setBackgroundColor(context.getResources().getColor(R.color.white));
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
        } else {
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
