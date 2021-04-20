package com.tokopedia.imagepicker.editor.adapter;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.imagepicker.R;

import java.util.ArrayList;

/**
 * Created by hendry on 02/05/18.
 */

public class ImageEditThumbnailAdapter extends RecyclerView.Adapter<ImageEditThumbnailAdapter.ImageEditThumbnailViewHolder> {

    private Context context;
    private ArrayList<ArrayList<String>> imagePathList;
    private ArrayList<Integer> currentEditStepIndexList;
    private RecyclerView recyclerView;

    private int selectedIndex = 0;
    private int itemWidth;

    private OnImageEditThumbnailAdapterListener onImageEditThumbnailAdapterListener;

    public interface OnImageEditThumbnailAdapterListener {
        void onThumbnailItemClicked(String imagePath, int position);
    }

    public ImageEditThumbnailAdapter(Context context, ArrayList<ArrayList<String>> imagePathList,
                                     ArrayList<Integer> currentEditStepIndexList,
                                     int imageIndex,
                                     OnImageEditThumbnailAdapterListener onImageEditThumbnailAdapterListener) {
        this.context = context;
        this.imagePathList = imagePathList;
        this.selectedIndex = imageIndex;
        this.onImageEditThumbnailAdapterListener = onImageEditThumbnailAdapterListener;
        itemWidth = context.getResources().getDimensionPixelOffset(R.dimen.image_editor_thumbnail_width);
        this.currentEditStepIndexList = currentEditStepIndexList;
    }


    public class ImageEditThumbnailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private View rectGreenView;

        public ImageEditThumbnailViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            rectGreenView = itemView.findViewById(R.id.rect_green_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position < 0) return;

            if (recyclerView != null) {
                scrollToCenter(position);
            }
            if (selectedIndex != position) {
                selectedIndex = position;
                notifyDataSetChanged();

                String imagePath = imagePathList.get(position).get(currentEditStepIndexList.get(position));
                onImageEditThumbnailAdapterListener.onThumbnailItemClicked(imagePath, position);
            }
        }
    }

    public void scrollToCenter(int position) {
        int centerOfScreen = recyclerView.getWidth() / 2 - itemWidth / 2;
        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(position, centerOfScreen);
    }

    public void setData(ArrayList<ArrayList<String>> imagePathList, ArrayList<Integer> currentEditStepIndexList, int imageIndex) {
        this.imagePathList = imagePathList;
        this.selectedIndex = imageIndex;
        this.currentEditStepIndexList = currentEditStepIndexList;
        notifyDataSetChanged();
    }

    public void setSelectedIndex(int imageIndex) {
        if (imageIndex != selectedIndex) {
            this.selectedIndex = imageIndex;
            scrollToCenter(imageIndex);
            notifyDataSetChanged();
        }
    }

    @Override
    public ImageEditThumbnailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_edit_thumbnail_item, parent, false);

        return new ImageEditThumbnailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageEditThumbnailViewHolder holder, int position) {
        String imagePath = imagePathList.get(position).get(currentEditStepIndexList.get(position));
        ImageHandler.loadImageFit2(holder.imageView.getContext(), holder.imageView, imagePath);
        holder.rectGreenView.setVisibility(selectedIndex == position ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        if (imagePathList == null) {
            return 0;
        } else {
            return imagePathList.size();
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }
}
