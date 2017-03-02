package com.tokopedia.core.myproduct.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.myproduct.model.FolderModel;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.core.newgallery.adapter.ImageAlbumAdapter;
import com.tokopedia.core.newgallery.presenter.ImageGalleryView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.tkpd.library.utils.CommonUtils.checkNotNull;

/**
 * Created by m.normansyah on 03/12/2015.
 */
public class ImageGalleryAlbumFragment extends Fragment {
    List<FolderModel> folderModels;
    int maxSelection = -1;
    public static final String FRAGMENT_TAG = "ImageGalleryAlbumFragment";
    private Unbinder unbinder;

    @Deprecated
    public static Fragment newInstance(){
        return new ImageGalleryAlbumFragment();
    }

    public static Fragment newInstance(int maxSelection){
        Bundle bundle = new Bundle();
        bundle.putInt(GalleryActivity.MAX_IMAGE_SELECTION, maxSelection);
        ImageGalleryAlbumFragment imageGalleryAlbumFragment = new ImageGalleryAlbumFragment();
        imageGalleryAlbumFragment.setArguments(bundle);
        return imageGalleryAlbumFragment;
    }

    @BindView(R2.id.gallery_listview)
    RecyclerView recyclerView;

    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_activity_gallery_browser);

        Bundle bundle = getArguments();
        if(checkNotNull(bundle)){
            maxSelection = bundle.getInt(GalleryActivity.MAX_IMAGE_SELECTION, -1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_gallery_album_browser, container, false);
        unbinder = ButterKnife.bind(this, parentView);
        return parentView;
    }

    @Override
    public void onResume() {
        super.onResume();

        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
        ImageAlbumAdapter imageAlbumAdapter = new ImageAlbumAdapter(new ArrayList<FolderModel>());
        imageAlbumAdapter.setMaxSelection(maxSelection);
        adapter = imageAlbumAdapter;

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        if(getActivity() != null && getActivity() instanceof ImageGalleryView){
            ((ImageGalleryView)getActivity()).fetchImageFromDb();
        }

    }

    public void loadData(List<FolderModel> datas){
        ((ImageAlbumAdapter)adapter).addAll(datas);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
