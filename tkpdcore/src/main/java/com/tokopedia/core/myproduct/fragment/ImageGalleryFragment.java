package com.tokopedia.core.myproduct.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.core.newgallery.adapter.ImageGalleryAdapter;
import com.tokopedia.core.newgallery.model.ImageModel;
import com.tokopedia.core.newgallery.presenter.ImageGalleryView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by m.normansyah on 03/12/2015.
 */
public class ImageGalleryFragment extends Fragment implements ImageGalleryAdapter.CountTitle{

    public static final String FRAGMENT_TAG = "ImageGalleryFragment";
    public static final String FRAGMENT_DATA = "ImageGalleryFragment_DATA";
    public static final String FRAGMENT_FOLDER_PATH = "ImageGalleryFragment_FOLDER_PATH";
    public static final String SELECTABLE_TAG = "SELECTABLE_TAG";

    List<ImageModel> datas = new ArrayList<>();

    @BindView(R2.id.gallery_gridview)
    RecyclerView recyclerView;

    ImageGalleryAdapter adapter;
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
                        paths.add(imageModel.getPathFile());
                    }
                }
                if(ImageGalleryFragment.this.getActivity()!=null&& ImageGalleryFragment.this.getActivity() instanceof ImageGalleryView){
                    ((ImageGalleryView)ImageGalleryFragment.this.getActivity()).sendResultImageGallery(paths);
                }

                mMultiSelector.clearSelections();
                return true;

            }
            return false;
        }
    };
    private Unbinder unbinder;
    private String folderPath;
    private ImageGalleryView imageGalleryView;

    @Deprecated
    public static Fragment newInstance(List<ImageModel> imageModels){
        ImageGalleryFragment imageGalleryFragment = new ImageGalleryFragment();
        Bundle data = new Bundle();
        data.putParcelable(FRAGMENT_DATA, Parcels.wrap(imageModels));
        imageGalleryFragment.setArguments(data);
        return imageGalleryFragment;
    }

    @Deprecated
    public static Fragment newInstance(List<ImageModel> imageModels, int maxSelection){
        ImageGalleryFragment imageGalleryFragment = new ImageGalleryFragment();
        Bundle data = new Bundle();
        data.putParcelable(FRAGMENT_DATA, Parcels.wrap(imageModels));
        data.putInt(GalleryActivity.MAX_IMAGE_SELECTION, maxSelection);
        imageGalleryFragment.setArguments(data);
        return imageGalleryFragment;
    }

    public static Fragment newInstance(String folderPath, int maxSelection) {
        ImageGalleryFragment imageGalleryFragment = new ImageGalleryFragment();
        Bundle data = new Bundle();
        data.putString(FRAGMENT_FOLDER_PATH, folderPath);
        data.putInt(GalleryActivity.MAX_IMAGE_SELECTION, maxSelection);
        imageGalleryFragment.setArguments(data);
        return imageGalleryFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null && context instanceof ImageGalleryView) {
            imageGalleryView = (ImageGalleryView) context;
        }
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

        if (getArguments() != null) {
            if (!TextUtils.isEmpty(getArguments().getString(FRAGMENT_FOLDER_PATH, null))) {
                folderPath = getArguments().getString(FRAGMENT_FOLDER_PATH, null);
            }

            if(getArguments().getParcelable(FRAGMENT_DATA)!=null){
                datas = Parcels.unwrap(getArguments().getParcelable(FRAGMENT_DATA));
            }
            maxSelection = getArguments().getInt(GalleryActivity.MAX_IMAGE_SELECTION);
        }

        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.title_activity_gallery_browser);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_gallery_browser, container, false);
        unbinder = ButterKnife.bind(this, parentView);

        layoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        adapter = new ImageGalleryAdapter(datas, mMultiSelector, maxSelection);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setCountTitle(this);

        if (imageGalleryView != null && !TextUtils.isEmpty(folderPath))
            imageGalleryView.fetchImageFromDb(folderPath);

        return parentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        adapter.setCountTitle(null);
    }

    public ModalMultiSelectorCallback getmDeleteMode() {
        return mDeleteMode;
    }

    @Override
    public void onTitleChanged(int size) { /* currently do nothing */ }

    public void addItems(ArrayList<ImageModel> data) {
        adapter.addItems(data);
    }
}
