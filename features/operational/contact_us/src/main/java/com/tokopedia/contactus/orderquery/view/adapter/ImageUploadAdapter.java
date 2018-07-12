package com.tokopedia.contactus.orderquery.view.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.R2;
import com.tokopedia.contactus.orderquery.data.ImageUpload;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sandeepgoyal on 18/04/18.
 */

public class ImageUploadAdapter extends RecyclerView.Adapter<ImageUploadAdapter.ImageViewHolder> {

    private final OnSelectImageClick onSelectImageClick;


    private static final int VIEW_UPLOAD_BUTTON = 100;
    private int maxPicUpload = 5;

    public ArrayList<ImageUpload> getImageUpload() {
        return imageUpload;
    }

    ArrayList<ImageUpload> imageUpload = new ArrayList<>();

    private Context context;

    public ImageUploadAdapter(Context context, OnSelectImageClick onSelectImageClick) {
        this.context = context;
        this.onSelectImageClick = onSelectImageClick;
        imageUpload.add(new ImageUpload());
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.selected_image_item, parent, false));

    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        ImageUpload image = imageUpload.get(position);
        if (getItemViewType(position) != VIEW_UPLOAD_BUTTON) {
            image.setImgSrc(-1);
        } else {
            image.setImgSrc(R.drawable.ic_upload);
        }
        holder.setImage(image);
    }

    @Override
    public int getItemCount() {
        if (imageUpload.size() >= maxPicUpload) {
            return maxPicUpload;
        }
        return imageUpload.size();
    }

    public void addImage(ImageUpload image) {
        imageUpload.add(0, image);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == imageUpload.size() - 1) {
            return VIEW_UPLOAD_BUTTON;
        } else {
            return super.getItemViewType(position);
        }
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageUpload image;

        @BindView(R2.id.selected_image)
        ImageView selectedImage;
        @BindView(R2.id.delete_image)
        ImageView deleteImage;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setImage(ImageUpload image) {
            this.image = image;
            if (image.getImgSrc() != -1) {
                selectedImage.setImageResource(image.getImgSrc());
                deleteImage.setVisibility(View.GONE);
                selectedImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSelectImageClick.onClick();
                    }
                });
            } else {
                ImageHandler.loadImageFromFile(context, selectedImage, new File(image.getFileLoc()));
                deleteImage.setVisibility(View.VISIBLE);
            }
        }

        @OnClick(R2.id.delete_image)
        public void onViewClicked() {
            imageUpload.remove(image);
            notifyDataSetChanged();
        }


    }

    public interface OnSelectImageClick {
        public void onClick();
    }
}
