package com.tokopedia.contactus.createticket.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.contactus.createticket.model.ImageUpload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nisie on 2/16/16.
 */
public class ImageUploadAdapter extends RecyclerView.Adapter<ImageUploadAdapter.ViewHolder> {

    private static final int VIEW_UPLOAD_BUTTON = 100;
    private int maxImage = 5;
    private int canUpload = 0;

    public int getMaxImage() {
        return maxImage;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.image_upload)
        ImageView image;

        @BindView(R2.id.delete_but)
        ImageView deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
                .inflate(R.layout.listview_image_upload_delete, viewGroup, false));
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

    private void bindImage(ViewHolder holder, final int position) {

        try {
            if (data.get(position).getFileLoc() == null) {
                ImageHandler.LoadImage(holder.image, data.get(position).getPicSrc());
            } else {
                ImageHandler.loadImageFromFile(context, holder.image, new File(data.get(position).getFileLoc()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.image.setOnClickListener(listener.onImageClicked(position, data.get(position)));
        holder.deleteButton.setVisibility(View.VISIBLE);
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(position);
                notifyDataSetChanged();
            }
        });

        setBorder(holder, position);

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
        holder.deleteButton.setVisibility(View.GONE);
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
        if (position == data.size() && data.size() < maxImage) {
            return VIEW_UPLOAD_BUTTON;
        } else {
            return super.getItemViewType(position);
        }
    }

    public void addList(List<ImageUpload> data) {
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

    public void setMaxImage(int max) {
        this.maxImage = max;
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
