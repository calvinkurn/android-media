package com.tokopedia.product.manage.item.imagepicker.view.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.imagepicker.picker.gallery.widget.MediaGridInset;
import com.tokopedia.imagepicker.picker.instagram.util.InstagramConstant;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerInterface;
import com.tokopedia.product.manage.item.R;
import com.tokopedia.product.manage.item.imagepicker.di.DaggerImagePickerCatalogComponent;
import com.tokopedia.product.manage.item.imagepicker.di.ImagePickerCatalogModule;
import com.tokopedia.product.manage.item.imagepicker.util.CatalogConstant;
import com.tokopedia.product.manage.item.imagepicker.view.adapter.CatalogAdapterTypeFactory;
import com.tokopedia.product.manage.item.imagepicker.view.adapter.CatalogImageViewHolder;
import com.tokopedia.product.manage.item.imagepicker.view.adapter.ImageCatalogAdapter;
import com.tokopedia.product.manage.item.imagepicker.view.model.CatalogModelView;
import com.tokopedia.product.manage.item.imagepicker.view.presenter.ImagePickerCatalogContract;
import com.tokopedia.product.manage.item.imagepicker.view.presenter.ImagePickerCatalogPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 6/5/18.
 */

public class ImagePickerCatalogFragment extends BaseListFragment<CatalogModelView, CatalogAdapterTypeFactory> implements ImagePickerCatalogContract.View, ImagePickerInterface, ImageCatalogAdapter.OnImageCatalogAdapterListener {

    @Inject
    ImagePickerCatalogPresenter imagePickerCatalogPresenter;
    private String catalogId;
    private ImageCatalogAdapter imageCatalogAdapter;
    private ListenerImagePickerCatalog listenerImagePickerCatalog;

    public static ImagePickerCatalogFragment createInstance(String catalogId){
        ImagePickerCatalogFragment imagePickerCatalogFragment = new ImagePickerCatalogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CatalogConstant.CATALOG_ID_EXTRAS, catalogId);
        imagePickerCatalogFragment.setArguments(bundle);
        return imagePickerCatalogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        catalogId = getArguments().getString(CatalogConstant.CATALOG_ID_EXTRAS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_picker_catalog, container, false);
    }

    @Nullable
    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout(View view) {
        return view.findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    public void onSwipeRefresh() {
        imagePickerCatalogPresenter.clearCacheCatalog();
        super.onSwipeRefresh();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        DaggerImagePickerCatalogComponent
                .builder()
                .imagePickerCatalogModule(new ImagePickerCatalogModule())
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build()
                .inject(this);
        imagePickerCatalogPresenter.attachView(this);
    }

    @Override
    public void onItemClicked(CatalogModelView catalogModelView) {

    }

    @Override
    public void loadData(int page) {
        imagePickerCatalogPresenter.getCatalogImage(catalogId);
    }

    @Override
    public RecyclerView getRecyclerView(View view) {
        RecyclerView recyclerView = super.getRecyclerView(view);
        int spacing = getResources().getDimensionPixelSize(R.dimen.image_picker_media_grid_spacing);
        recyclerView.addItemDecoration(new MediaGridInset(InstagramConstant.SPAN_COUNT, spacing, false));
        RecyclerView.ItemAnimator itemAnimator = recyclerView.getItemAnimator();
        if (itemAnimator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
        return recyclerView;
    }

    @Override
    protected CatalogAdapterTypeFactory getAdapterTypeFactory() {
        return new CatalogAdapterTypeFactory(getImageResize());
    }

    private int getImageResize() {
        int imageResize;
        int spanCount = InstagramConstant.SPAN_COUNT;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int availableWidth = screenWidth - getResources().getDimensionPixelSize(
                R.dimen.image_picker_media_grid_spacing) * (spanCount - 1);
        imageResize = availableWidth / spanCount;
        imageResize = (int) (imageResize * 0.85f);
        return imageResize;
    }

    @Override
    protected BaseListAdapter<CatalogModelView, CatalogAdapterTypeFactory> createAdapterInstance() {
        imageCatalogAdapter = new ImageCatalogAdapter(getAdapterTypeFactory(),
                this, listenerImagePickerCatalog.getImagePath());
        return imageCatalogAdapter;
    }

    @Override
    public void afterThumbnailImageRemoved() {
        imageCatalogAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(CatalogModelView catalogModelView, boolean isChecked) {
        listenerImagePickerCatalog.onClickImageCatalog(catalogModelView.getImageUrl(), isChecked);
    }

    @Override
    public boolean canAddMoreImage() {
        return !listenerImagePickerCatalog.isMaxImageReached();
    }

    @Override
    protected RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), InstagramConstant.SPAN_COUNT, LinearLayoutManager.VERTICAL, false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (getAdapter().getItemViewType(position) == CatalogImageViewHolder.LAYOUT) {
                    return InstagramConstant.SPAN_LOOK_UP;
                }
                return InstagramConstant.SPAN_COUNT;
            }
        });
        return gridLayoutManager;
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        listenerImagePickerCatalog = (ListenerImagePickerCatalog) context;
    }

    @Override
    public void onDestroy() {
        imagePickerCatalogPresenter.detachView();
        super.onDestroy();
    }

    public interface ListenerImagePickerCatalog {
        void onClickImageCatalog(String url, boolean isChecked);
        boolean isMaxImageReached();
        ArrayList<String> getImagePath();
    }
}
