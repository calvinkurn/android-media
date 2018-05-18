package com.tokopedia.imagepicker.picker.instagram.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.picker.gallery.widget.MediaGridInset;
import com.tokopedia.imagepicker.picker.instagram.InstagramConstant;
import com.tokopedia.imagepicker.picker.instagram.data.source.exception.ShouldLoginInstagramException;
import com.tokopedia.imagepicker.picker.instagram.di.DaggerInstagramComponent;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.imagepicker.picker.instagram.di.InstagramModule;
import com.tokopedia.imagepicker.picker.instagram.view.adapter.ImageInstagramAdapter;
import com.tokopedia.imagepicker.picker.instagram.view.adapter.ImageInstagramAdapterTypeFactory;
import com.tokopedia.imagepicker.picker.instagram.view.adapter.ImagePickerInstagramViewHolder;
import com.tokopedia.imagepicker.picker.instagram.view.dialog.InstagramLoginDialog;
import com.tokopedia.imagepicker.picker.instagram.view.model.InstagramErrorLoginModel;
import com.tokopedia.imagepicker.picker.instagram.view.model.InstagramMediaModel;
import com.tokopedia.imagepicker.picker.instagram.view.presenter.ImagePickerInstagramContract;
import com.tokopedia.imagepicker.picker.instagram.view.presenter.ImagePickerInstagramPresenter;

import java.util.List;

import javax.inject.Inject;


/**
 * Created by hendry on 19/04/18.
 */

public class ImagePickerInstagramFragment extends BaseListFragment<InstagramMediaModel, ImageInstagramAdapterTypeFactory> implements ImagePickerInstagramContract.View, InstagramLoginDialog.ListenerLoginInstagram, InstagramErrorLoginModel.ListenerLoginInstagram {

    @Inject
    ImagePickerInstagramPresenter imagePickerInstagramPresenter;

    private ListenerImagePickerInstagram listenerImagePickerInstagram;
    private String code = "";
    private String nextMediaId = "";

    private ImageInstagramAdapter imageInstagramAdapter;

    public static ImagePickerInstagramFragment newInstance() {
        return new ImagePickerInstagramFragment();
    }

    @NonNull
    @Override
    protected BaseListAdapter<InstagramMediaModel, ImageInstagramAdapterTypeFactory> createAdapterInstance() {
        imageInstagramAdapter = new ImageInstagramAdapter(getAdapterTypeFactory());
        imageInstagramAdapter.setOnAdapterInteractionListener(this);
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

    @Override
    public RecyclerView getRecyclerView(View view) {
        RecyclerView recyclerView = super.getRecyclerView(view);
        int spacing = getResources().getDimensionPixelSize(R.dimen.media_grid_spacing);
        recyclerView.addItemDecoration(new MediaGridInset(InstagramConstant.SPAN_COUNT, spacing, false));
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
                R.dimen.media_grid_spacing) * (spanCount - 1);
        imageResize = availableWidth / spanCount;
        imageResize = (int) (imageResize * 0.85f);
        return imageResize;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onItemClicked(InstagramMediaModel instagramMediaModel) {
        listenerImagePickerInstagram.onClickImageInstagram(instagramMediaModel.getImageStandardResolution(), true);
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
    public void onSuccessLogin(String code) {
        this.code = code;
        loadInitialData();
    }

    @Override
    public void saveCookies(String cookies) {
        imagePickerInstagramPresenter.saveCookies(cookies);
    }

    @Override
    public void onClickLoginInstagram() {
        InstagramLoginDialog instagramLoginDialog = new InstagramLoginDialog();
        instagramLoginDialog.setListenerLoginInstagram(this);
        instagramLoginDialog.show(getActivity().getSupportFragmentManager(), "instagram_dialog");
    }

    public interface ListenerImagePickerInstagram {
        void onClickImageInstagram(String url, boolean isChecked);
    }

}
