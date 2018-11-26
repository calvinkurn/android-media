package com.tokopedia.gallery.customview;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.gallery.GalleryView;
import com.tokopedia.gallery.ImageReviewGalleryActivity;
import com.tokopedia.gallery.R;
import com.tokopedia.gallery.viewmodel.ImageReviewItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 12/03/18.
 */

public class BottomSheetImageReviewSlider extends FrameLayout implements ImageReviewSliderView {

    private UserLockBottomSheetBehavior bottomSheetBehavior;
    private View rootView;
    private View backButton;
    private View bottomSheetLayout;
    private RecyclerView recyclerView;
    private SliderAdapter adapter;

    private Callback callback;

    private EndlessRecyclerViewScrollListener loadMoreTriggerListener;

    public BottomSheetImageReviewSlider(@NonNull Context context) {
        super(context);
        init();
    }

    public BottomSheetImageReviewSlider(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BottomSheetImageReviewSlider(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
    }

    private void init() {
        bindView();
        initRecyclerView();
    }

    private void bindView() {
        rootView = inflate(getContext(), R.layout.review_image_slider, this);
        recyclerView = rootView.findViewById(R.id.review_image_slider_recycler_view);
        backButton = rootView.findViewById(R.id.backButton);
        bottomSheetLayout = this;
    }

    private void initRecyclerView() {
        adapter = new SliderAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        loadMoreTriggerListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (callback.isAllowLoadMore()) {
                    callback.onRequestLoadMore(page);
                } else {
                    updateStateAfterGetData();
                }
            }
        };
        recyclerView.addOnScrollListener(loadMoreTriggerListener);
    }

    public void setup(Callback callback) {
        this.callback = callback;
        initListener();
    }

    public void closeView() {
        if (bottomSheetBehavior != null
                && bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    private void initListener() {
        bottomSheetBehavior = (UserLockBottomSheetBehavior) UserLockBottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeView();
            }
        });
    }

    public boolean isBottomSheetShown() {
        return bottomSheetBehavior != null
                && bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN;
    }

    @Override
    public boolean onBackPressed() {
        if (isBottomSheetShown()) {
            closeView();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void resetState() {
        adapter.resetState();
        loadMoreTriggerListener.resetState();
    }

    @Override
    public void onLoadingData() {
        adapter.addLoading();
    }

    @Override
    public void displayImage(int position) {
        recyclerView.scrollToPosition(position);
        showBottomSheet();
    }

    private void showBottomSheet() {
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    public void onLoadDataSuccess(List<ImageReviewItem> imageReviewItems, boolean isHasNextPage) {
        adapter.removeLoading();
        adapter.appendItems(imageReviewItems);
        loadMoreTriggerListener.updateStateAfterGetData();
        loadMoreTriggerListener.setHasNextPage(isHasNextPage);
    }

    @Override
    public void onLoadDataFailed() {
        adapter.removeLoading();
        loadMoreTriggerListener.updateStateAfterGetData();
        recyclerView.scrollToPosition(adapter.getGalleryItemCount() - 1);
    }

    public interface Callback {
        void onRequestLoadMore(int page);
        boolean isAllowLoadMore();
    }

    private static class SliderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<ImageReviewItem> imageReviewItemList = new ArrayList<>();
        private boolean loadingItemEnabled = true;

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ImageSliderViewHolder) {
                ((ImageSliderViewHolder) holder).bind(imageReviewItemList.get(position));
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(viewType, parent, false);
            if (viewType == ImageSliderViewHolder.LAYOUT) {
                return new ImageSliderViewHolder(view);
            } else {
                return new LoadingSliderViewHolder(view);
            }
        }

        @Override
        public int getItemCount() {
            return loadingItemEnabled ? imageReviewItemList.size() + 1 : imageReviewItemList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (isGalleryItem(position)) {
                return ImageSliderViewHolder.LAYOUT;
            } else {
                return LoadingSliderViewHolder.LAYOUT;
            }
        }

        public void appendItems(List<ImageReviewItem> imageReviewItems) {
            imageReviewItemList.addAll(imageReviewItems);
            notifyDataSetChanged();
        }

        public void resetState() {
            imageReviewItemList.clear();
            loadingItemEnabled = true;
            notifyDataSetChanged();
        }

        public boolean isGalleryItem(int position) {
            return position < imageReviewItemList.size();
        }

        public void removeLoading() {
            loadingItemEnabled = false;
            notifyDataSetChanged();
        }

        public void addLoading() {
            loadingItemEnabled = true;
            notifyDataSetChanged();
        }

        public boolean isLoadingItemEnabled() {
            return loadingItemEnabled;
        }

        public int getGalleryItemCount() {
            return imageReviewItemList.size();
        }
    }

    private static class ImageSliderViewHolder extends RecyclerView.ViewHolder {

        @LayoutRes
        public static final int LAYOUT = R.layout.review_image_slider_item;

        private ImageView imageView;
        private TextView date;
        private TextView name;
        private ImageView rating;

        public ImageSliderViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.review_image_slider_item_image_view);
            date = itemView.findViewById(R.id.review_image_slider_date);
            name = itemView.findViewById(R.id.review_image_slider_name);
            rating = itemView.findViewById(R.id.review_image_slider_rating);
        }

        public void bind(ImageReviewItem item) {
            ImageHandler.LoadImage(imageView, item.getImageUrlLarge());
            name.setText(item.getReviewerName());
            date.setText(item.getFormattedDate());
            rating.setImageResource(RatingView.getRatingDrawable(item.getRating()));
        }
    }

    private static class LoadingSliderViewHolder extends RecyclerView.ViewHolder {

        @LayoutRes
        public static final int LAYOUT = R.layout.review_image_slider_loading;

        public LoadingSliderViewHolder(View itemView) {
            super(itemView);
        }
    }
}
