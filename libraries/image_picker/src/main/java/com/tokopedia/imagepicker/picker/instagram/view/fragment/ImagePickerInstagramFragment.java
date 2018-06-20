package com.tokopedia.imagepicker.picker.instagram.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.gallery.widget.MediaGridInset;
import com.tokopedia.imagepicker.picker.instagram.data.source.exception.ShouldLoginInstagramException;
import com.tokopedia.imagepicker.picker.instagram.di.DaggerInstagramComponent;
import com.tokopedia.imagepicker.picker.instagram.di.InstagramModule;
import com.tokopedia.imagepicker.picker.instagram.util.InstagramConstant;
import com.tokopedia.imagepicker.picker.instagram.view.activity.InstagramLoginActivity;
import com.tokopedia.imagepicker.picker.instagram.view.adapter.ImageInstagramAdapter;
import com.tokopedia.imagepicker.picker.instagram.view.adapter.ImageInstagramAdapterTypeFactory;
import com.tokopedia.imagepicker.picker.instagram.view.adapter.ImagePickerInstagramViewHolder;
import com.tokopedia.imagepicker.picker.instagram.view.model.InstagramErrorLoginModel;
import com.tokopedia.imagepicker.picker.instagram.view.model.InstagramMediaModel;
import com.tokopedia.imagepicker.picker.instagram.view.presenter.ImagePickerInstagramContract;
import com.tokopedia.imagepicker.picker.instagram.view.presenter.ImagePickerInstagramPresenter;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerInterface;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


/**
 * Created by hendry on 19/04/18.
 */

public class ImagePickerInstagramFragment extends BaseListFragment<InstagramMediaModel, ImageInstagramAdapterTypeFactory>
        implements ImagePickerInstagramContract.View, InstagramErrorLoginModel.ListenerLoginInstagram,
        ImageInstagramAdapter.OnImageInstagramAdapterListener,
        ImagePickerInterface {

    public static final String ARGS_GALLERY_TYPE = "args_gallery_type";
    public static final String ARGS_SUPPORT_MULTIPLE = "args_support_multiple";
    public static final String ARGS_MIN_RESOLUTION = "args_min_resolution";
    public static final int REQUEST_CODE_INSTAGRAM_LOGIN = 342;

    @Inject
    ImagePickerInstagramPresenter imagePickerInstagramPresenter;

    private ListenerImagePickerInstagram listenerImagePickerInstagram;
    private String code = "";
    private String nextMediaId = "";

    private @GalleryType
    int galleryType;
    private boolean supportMultipleSelection;
    private int minImageResolution;

    private ImageInstagramAdapter imageInstagramAdapter;

    public static ImagePickerInstagramFragment newInstance(@GalleryType int galleryType,
                                                           boolean supportMultipleSelection,
                                                           int minImageResolution) {
        ImagePickerInstagramFragment fragment = new ImagePickerInstagramFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_GALLERY_TYPE, galleryType);
        bundle.putBoolean(ARGS_SUPPORT_MULTIPLE, supportMultipleSelection);
        bundle.putInt(ARGS_MIN_RESOLUTION, minImageResolution);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        galleryType = bundle.getInt(ARGS_GALLERY_TYPE);
        supportMultipleSelection = bundle.getBoolean(ARGS_SUPPORT_MULTIPLE);
        minImageResolution = bundle.getInt(ARGS_MIN_RESOLUTION);
        if(savedInstanceState != null) {
            code = savedInstanceState.getString(InstagramConstant.EXTRA_CODE_LOGIN, "");
        }

        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    protected BaseListAdapter<InstagramMediaModel, ImageInstagramAdapterTypeFactory> createAdapterInstance() {
        imageInstagramAdapter = new ImageInstagramAdapter(getAdapterTypeFactory(),
                this, listenerImagePickerInstagram.getImagePath(), supportMultipleSelection);
        return imageInstagramAdapter;
    }

    @Override
    protected void initInjector() {
        DaggerInstagramComponent
                .builder()
                .instagramModule(new InstagramModule())
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build()
                .inject(this);
        imagePickerInstagramPresenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        imagePickerInstagramPresenter.detachView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_picker_instagram, container, false);
    }

    @Nullable
    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout(View view) {
        return view.findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    public void onSwipeRefresh() {
        imagePickerInstagramPresenter.clearCacheInstagramMedia();
        super.onSwipeRefresh();
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
    public void loadData(int page) {
        imagePickerInstagramPresenter.getListMediaInstagram(code, nextMediaId);
    }

    @Override
    protected RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), InstagramConstant.SPAN_COUNT, LinearLayoutManager.VERTICAL, false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (getAdapter().getItemViewType(position) == ImagePickerInstagramViewHolder.LAYOUT) {
                    return InstagramConstant.SPAN_LOOK_UP;
                }
                return InstagramConstant.SPAN_COUNT;
            }
        });
        return gridLayoutManager;
    }

    @Override
    protected ImageInstagramAdapterTypeFactory getAdapterTypeFactory() {
        return new ImageInstagramAdapterTypeFactory(getImageResize());
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
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        listenerImagePickerInstagram = (ListenerImagePickerInstagram) context;
    }

    @Override
    public void showGetListError(Throwable throwable) {
        if (throwable instanceof ShouldLoginInstagramException) {
            InstagramErrorLoginModel instagramErrorLoginModel = new InstagramErrorLoginModel();
            instagramErrorLoginModel.setListenerLoginInstagram(this);
            getAdapter().setErrorNetworkModel(instagramErrorLoginModel);
        } else {
            getAdapter().setErrorNetworkModel(new ErrorNetworkModel());
        }
        super.showGetListError(throwable);
    }

    @Override
    public void renderList(List<InstagramMediaModel> instagramMediaModels, boolean hasNextPage, String nextMaxIdPage) {
        code = "";
        this.nextMediaId = nextMaxIdPage;
        renderList(instagramMediaModels, hasNextPage);
    }

    @Override
    public void onClickLoginInstagram() {
        Intent intent = new Intent(getActivity(), InstagramLoginActivity.class);
        startActivityForResult(intent, REQUEST_CODE_INSTAGRAM_LOGIN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_INSTAGRAM_LOGIN && resultCode == InstagramLoginActivity.RESULT_OK){
            this.code = data.getStringExtra(InstagramConstant.EXTRA_CODE_LOGIN);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean canAddMoreImage() {
        return !listenerImagePickerInstagram.isMaxImageReached();
    }

    @Override
    public boolean isImageValid(InstagramMediaModel instagramMediaModel) {
        //check image resolution
        if (instagramMediaModel.getMinResolution() < minImageResolution) {
            NetworkErrorHelper.showRedCloseSnackbar(getView(), getString(R.string.image_under_x_resolution, minImageResolution));
            return false;
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(InstagramConstant.EXTRA_CODE_LOGIN, code);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemClicked(InstagramMediaModel instagramMediaModel, boolean isChecked) {
        listenerImagePickerInstagram.onClickImageInstagram(instagramMediaModel.getImageStandardResolutionUrl(),
                isChecked,
                instagramMediaModel.getCaption());
    }

    @Override
    public void onItemClicked(InstagramMediaModel instagramMediaModel) {
        // no implementation, use onItemClicked(model, boolean) instead.
    }

    @Override
    public void afterThumbnailImageRemoved() {
        imageInstagramAdapter.notifyDataSetChanged();
    }

    public interface ListenerImagePickerInstagram {
        void onClickImageInstagram(String url, boolean isChecked, String imageDescription);
        boolean isMaxImageReached();
        ArrayList<String> getImagePath();
    }

}
