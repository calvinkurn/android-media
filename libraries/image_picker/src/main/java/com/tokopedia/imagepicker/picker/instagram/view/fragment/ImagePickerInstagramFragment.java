package com.tokopedia.imagepicker.picker.instagram.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.picker.instagram.InstagramConstant;
import com.tokopedia.imagepicker.picker.instagram.di.DaggerInstagramComponent;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.imagepicker.picker.instagram.di.InstagramModule;
import com.tokopedia.imagepicker.picker.instagram.view.adapter.ImageInstagramAdapterTypeFactory;
import com.tokopedia.imagepicker.picker.instagram.view.dialog.InstagramLoginDialog;
import com.tokopedia.imagepicker.picker.instagram.view.model.InstagramMediaModel;
import com.tokopedia.imagepicker.picker.instagram.view.presenter.ImagePickerInstagramContract;
import com.tokopedia.imagepicker.picker.instagram.view.presenter.ImagePickerInstagramPresenter;

import java.util.List;

import javax.inject.Inject;


/**
 * Created by hendry on 19/04/18.
 */

public class ImagePickerInstagramFragment extends BaseListFragment<InstagramMediaModel, ImageInstagramAdapterTypeFactory> implements ImagePickerInstagramContract.View, InstagramLoginDialog.ListenerLoginInstagram {

    @Inject
    ImagePickerInstagramPresenter imagePickerInstagramPresenter;

    private String code = "";
    private String nextMediaId = "";

    public static ImagePickerInstagramFragment newInstance() {
        return new ImagePickerInstagramFragment();
    }

    @Override
    protected void initInjector() {
        DaggerInstagramComponent
                .builder()
                .instagramModule(new InstagramModule())
                .baseAppComponent(((BaseMainApplication)getActivity().getApplication()).getBaseAppComponent())
                .build()
                .inject(this);
        imagePickerInstagramPresenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_picker_instagram, container, false);
    }

    @Override
    public void loadData(int page) {
        imagePickerInstagramPresenter.getListMediaInstagram(code, nextMediaId);
    }

    @Override
    protected RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        return new GridLayoutManager(getActivity(), InstagramConstant.SPAN_COUNT, LinearLayoutManager.VERTICAL, false);
    }

    @Override
    protected ImageInstagramAdapterTypeFactory getAdapterTypeFactory() {
        return new ImageInstagramAdapterTypeFactory();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onItemClicked(InstagramMediaModel instagramMediaModel) {

    }

    @Override
    public void onErrorShouldLoginInstagram() {
        InstagramLoginDialog instagramLoginDialog = new InstagramLoginDialog();
        instagramLoginDialog.setListenerLoginInstagram(this);
        instagramLoginDialog.show(getActivity().getSupportFragmentManager(), "instagram_dialog");
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
}
