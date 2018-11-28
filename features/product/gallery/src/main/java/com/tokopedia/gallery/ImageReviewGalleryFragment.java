package com.tokopedia.gallery;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.gallery.adapter.GalleryAdapter;
import com.tokopedia.gallery.adapter.TypeFactory;
import com.tokopedia.gallery.customview.BottomSheetImageReviewSlider;
import com.tokopedia.gallery.domain.GetImageReviewUseCase;
import com.tokopedia.gallery.presenter.ReviewGalleryPresenter;
import com.tokopedia.gallery.presenter.ReviewGalleryPresenterImpl;
import com.tokopedia.gallery.tracking.ImageReviewGalleryTracking;
import com.tokopedia.gallery.viewmodel.ImageReviewItem;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import java.util.ArrayList;
import java.util.List;

public class ImageReviewGalleryFragment extends BaseListFragment<ImageReviewItem, TypeFactory> implements BottomSheetImageReviewSlider.Callback, GalleryView {

    private ReviewGalleryPresenter presenter;
    private ImageReviewGalleryActivity activity;

    public static Fragment createInstance() {
        return new ImageReviewGalleryFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (ImageReviewGalleryActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_review_gallery, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupBottomSheet();
    }

    private void setupBottomSheet() {
        activity.getBottomSheetImageReviewSlider().setup(this);
        if (activity.isImageListPreloaded()) {
            activity.getBottomSheetImageReviewSlider().displayImage(activity.getDefaultPosition());
        }
    }

    @Override
    protected boolean hasInitialSwipeRefresh() {
        return true;
    }

    @Override
    public void loadData(int page) {

        if (activity.isImageListPreloaded()) {
            ArrayList<String> imageUrlList = activity.getImageUrlList();
            handleItemResult(convertToImageReviewItemList(imageUrlList), false);
            return;
        }

        activity.getBottomSheetImageReviewSlider().onLoadingData();
        presenter.loadData(activity.getProductId(), page);
    }

    private List<ImageReviewItem> convertToImageReviewItemList(ArrayList<String> imageUrlList) {
        List<ImageReviewItem> imageReviewItemList = new ArrayList<>();
        for (String s : imageUrlList) {
            ImageReviewItem imageReviewItem = new ImageReviewItem();
            imageReviewItem.setImageUrlThumbnail(s);
            imageReviewItem.setImageUrlLarge(s);
            imageReviewItemList.add(imageReviewItem);
        }
        return imageReviewItemList;
    }

    @Override
    protected TypeFactory getAdapterTypeFactory() {
        return new GalleryAdapter(this);
    }

    @Override
    public void onItemClicked(ImageReviewItem imageReviewItem) {

    }

    @Override
    protected void initInjector() {
        presenter = new ReviewGalleryPresenterImpl(
                new GetImageReviewUseCase(getContext(), new GraphqlUseCase()),
                this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onGalleryItemClicked(int position) {
        activity.getBottomSheetImageReviewSlider().displayImage(position);
        ImageReviewGalleryTracking.eventClickReviewGalleryItem(getActivity(),
                Integer.toString(activity.getProductId()));
    }

    @Override
    public void handleItemResult(List<ImageReviewItem> imageReviewItemList, boolean isHasNextPage) {
        renderList(imageReviewItemList, isHasNextPage);
        activity.getBottomSheetImageReviewSlider().onLoadDataSuccess(imageReviewItemList, isHasNextPage);
    }

    @Override
    public void handleErrorResult(Throwable e) {
        showGetListError(e);
        activity.getBottomSheetImageReviewSlider().onLoadDataFailed();
    }

    @Override
    public boolean isAllowLoadMore() {
        return !getAdapter().isLoading();
    }

    @Override
    public void onButtonBackPressed() {
        activity.onBackPressed();
    }

    @Override
    public void onRequestLoadMore(int page) {
        loadData(page);
    }

    @Override
    protected void loadInitialData() {
        activity.getBottomSheetImageReviewSlider().resetState();
        super.loadInitialData();
    }
}
