package com.tokopedia.contactus.orderquery.view.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.orderquery.data.ImageUpload;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by sandeepgoyal on 18/04/18.
 */

public class ImageUploadAdapter extends RecyclerView.Adapter<ImageUploadAdapter.ImageViewHolder> {

    private final OnSelectImageClick onSelectImageClick;


    private static final int VIEW_UPLOAD_BUTTON = 100;
    private int maxPicUpload = 5;

    private ArrayList<ImageUpload> imageUpload;

    private Context context;

    public ImageUploadAdapter(Context context, OnSelectImageClick onSelectImageClick) {
        this.context = context;
        this.onSelectImageClick = onSelectImageClick;
        imageUpload = new ArrayList<>();
        imageUpload.add(new ImageUpload());
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.selected_image_item, parent, false));

    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.setImage(imageUpload.get(position));
    }

    @Override
    public int getItemCount() {
        return imageUpload.size();
    }

    public void addImage(ImageUpload image) {
        if (imageUpload.size() < maxPicUpload) {
            image.setImgSrc(-1);
            imageUpload.add(imageUpload.size() - 1, image);
            notifyDataSetChanged();
        } else if (imageUpload.size() == maxPicUpload && imageUpload.get(maxPicUpload - 1).getImgSrc() != -1) {
            imageUpload.remove(imageUpload.size() - 1);
            image.setImgSrc(-1);
            imageUpload.add(image);
            notifyDataSetChanged();
            Toast.makeText(context, R.string.max_image_warning, Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<ImageUpload> getImageUpload() {
        ArrayList<ImageUpload> imageList = new ArrayList<>();
        for (ImageUpload image : imageUpload) {
            if (image.getImgSrc() == -1)
                imageList.add(image);
        }
        return imageList;
    }

    public void clearAll() {
        imageUpload.clear();
        imageUpload.add(new ImageUpload());
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageUpload image;
        ImageView selectedImage;
        ImageView deleteImage;

        public ImageViewHolder(View itemView) {
            super(itemView);
            selectedImage = itemView.findViewById(R.id.selected_image);
            deleteImage = itemView.findViewById(R.id.delete_image);
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
                selectedImage.setOnClickListener(null);
                deleteImage.setVisibility(View.VISIBLE);
            }

            deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onViewClicked();
                }
            });
        }

        public void onViewClicked() {
            if (imageUpload.size() == maxPicUpload) {
                ImageUpload lastImg = imageUpload.get(getAdapterPosition());
                if (lastImg.getImgSrc() == -1) {
                    lastImg.setImgSrc(R.drawable.ic_upload);
                }
            } else {
                imageUpload.remove(image);
            }
            notifyDataSetChanged();
        }


    }

    public interface OnSelectImageClick {
        public void onClick();
    }
}
