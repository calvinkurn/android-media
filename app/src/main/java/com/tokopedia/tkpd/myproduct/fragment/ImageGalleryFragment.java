package com.tokopedia.tkpd.myproduct.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.myproduct.ProductActivity;
import com.tokopedia.tkpd.myproduct.adapter.ImageGalleryAdapter;
import com.tokopedia.tkpd.myproduct.model.ImageModel;
import com.tokopedia.tkpd.myproduct.presenter.ImageGalleryView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by m.normansyah on 03/12/2015.
 */
public class ImageGalleryFragment extends Fragment implements ImageGalleryAdapter.CountTitle{

    public static final String FRAGMENT_TAG = "ImageGalleryFragment";
    public static final String FRAGMENT_DATA = "ImageGalleryFragment_DATA";
    public static final String SELECTABLE_TAG = "SELECTABLE_TAG";

    List<ImageModel> datas;

    @Bind(R.id.gallery_gridview)
    RecyclerView recyclerView;

    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    int maxSelection = ImageGalleryAdapter.UNLIMITED_SELECTION;

    MultiSelector mMultiSelector = new MultiSelector();
    ModalMultiSelectorCallback mDeleteMode = new ModalMultiSelectorCallback(mMultiSelector) {

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            super.onCreateActionMode(actionMode, menu);
            getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
            actionMode.setTitle("1");
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            if (menuItem.getItemId()==  R.id.menu_item_add_gallery){
                // Need to finish the action mode before doing the following,
                // not after. No idea why, but it crashes.
                actionMode.finish();

                List<String> paths = new ArrayList<>();
                for (int i= 0; i < datas.size(); i++) {
                    if (mMultiSelector.isSelected(i, 0)) {
                        ImageModel imageModel = datas.get(i);
//                        photos.remove(imageModel);
//                        recyclerView.getAdapter().notifyItemRemoved(i);
                        paths.add(imageModel.getPath());
                    }
                }
                if(ImageGalleryFragment.this.getActivity()!=null&& ImageGalleryFragment.this.getActivity() instanceof ImageGalleryView){
                    ((ImageGalleryView)ImageGalleryFragment.this.getActivity()).sendResultImageGallery(paths);
                }

                mMultiSelector.clearSelections();

                //[START] this code now didn't implement, need consideration from UI Team
//                if(getActivity() != null && getActivity() instanceof ImageGalleryView)
//                {
//                    ((ImageGalleryView)getActivity()).sendResultImagesGalery(paths);
//                }
                //[START] this code now didn't implement, need consideration from UI Team
                return true;

            }
            return false;
        }
    };


    @Deprecated
    public static Fragment newInstance(List<ImageModel> imageModels){
        ImageGalleryFragment imageGalleryFragment = new ImageGalleryFragment();
        Bundle data = new Bundle();
        data.putParcelable(FRAGMENT_DATA, Parcels.wrap(imageModels));
        imageGalleryFragment.setArguments(data);
        return imageGalleryFragment;
    }

    public static Fragment newInstance(List<ImageModel> imageModels, int maxSelection){
        ImageGalleryFragment imageGalleryFragment = new ImageGalleryFragment();
        Bundle data = new Bundle();
        data.putParcelable(FRAGMENT_DATA, Parcels.wrap(imageModels));
        data.putInt(ProductActivity.MAX_IMAGE_SELECTION, maxSelection);
        imageGalleryFragment.setArguments(data);
        return imageGalleryFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (mMultiSelector != null) {
            Bundle bundle = savedInstanceState;
            if (bundle != null) {
                mMultiSelector.restoreSelectionStates(bundle.getBundle(SELECTABLE_TAG));
            }

            if (mMultiSelector.isSelectable()) {
                if (mDeleteMode != null) {
                    mDeleteMode.setClearOnPrepare(false);
                    ActionMode actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(mDeleteMode);
                }

            }
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBundle(SELECTABLE_TAG, mMultiSelector.saveSelectionStates());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!=null){
            if(getArguments().getParcelable(FRAGMENT_DATA)!=null){
                datas = Parcels.unwrap(getArguments().getParcelable(FRAGMENT_DATA));
                maxSelection = getArguments().getInt(ProductActivity.MAX_IMAGE_SELECTION);
            }
        }

        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.title_activity_gallery_browser);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_gallery_browser, container, false);
        ButterKnife.bind(this, parentView);
        return parentView;
    }

    @Override
    public void onResume() {
        super.onResume();

        layoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        adapter = new ImageGalleryAdapter(datas, mMultiSelector, maxSelection);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        ((ImageGalleryAdapter)adapter).setCountTitle(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        ((ImageGalleryAdapter)adapter).setCountTitle(null);
    }

    public ModalMultiSelectorCallback getmDeleteMode() {
        return mDeleteMode;
    }

    @Override
    public void onTitleChanged(int size) {
    }
}
