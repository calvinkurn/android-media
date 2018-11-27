package com.tokopedia.gallery;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.List;

public class ImageReviewGalleryFragment extends BaseListFragment<ImageReviewItem, TypeFactory> implements BottomSheetImageReviewSlider.Callback, GalleryView {

    private ReviewGalleryPresenter presenter;
    private ImageReviewGalleryActivity imageReviewGalleryActivity;

    public static Fragment createInstance() {
        return new ImageReviewGalleryFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        imageReviewGalleryActivity = (ImageReviewGalleryActivity) getActivity();
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
        imageReviewGalleryActivity.getBottomSheetImageReviewSlider().setup(this);
    }

    @Override
    protected boolean hasInitialSwipeRefresh() {
        return true;
    }

    @Override
    public void loadData(int page) {
        imageReviewGalleryActivity.getBottomSheetImageReviewSlider().onLoadingData();
        presenter.loadData(imageReviewGalleryActivity.getProductId(), page);
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
        imageReviewGalleryActivity.getBottomSheetImageReviewSlider().displayImage(position);
        ImageReviewGalleryTracking.eventClickReviewGalleryItem(getActivity(),
                Integer.toString(imageReviewGalleryActivity.getProductId()));
    }

    @Override
    public void handleItemResult(List<ImageReviewItem> imageReviewItemList, boolean isHasNextPage) {
        renderList(imageReviewItemList, isHasNextPage);
        imageReviewGalleryActivity.getBottomSheetImageReviewSlider().onLoadDataSuccess(imageReviewItemList, isHasNextPage);
    }

    @Override
    public void handleErrorResult(Throwable e) {
        showGetListError(e);
        imageReviewGalleryActivity.getBottomSheetImageReviewSlider().onLoadDataFailed();
    }

    @Override
    public boolean isAllowLoadMore() {
        return !getAdapter().isLoading();
    }

    @Override
    public void onRequestLoadMore(int page) {
        loadData(page);
    }

    @Override
    protected void loadInitialData() {
        imageReviewGalleryActivity.getBottomSheetImageReviewSlider().resetState();
        super.loadInitialData();
    }
}
