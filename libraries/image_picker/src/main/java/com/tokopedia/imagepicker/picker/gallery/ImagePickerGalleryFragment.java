package com.tokopedia.imagepicker.picker.gallery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.picker.album.AlbumPickerActivity;
import com.tokopedia.imagepicker.picker.gallery.adapter.AlbumMediaAdapter;
import com.tokopedia.imagepicker.picker.gallery.loader.AlbumLoader;
import com.tokopedia.imagepicker.picker.gallery.loader.AlbumMediaLoader;
import com.tokopedia.imagepicker.picker.gallery.model.AlbumItem;
import com.tokopedia.imagepicker.picker.gallery.model.MediaItem;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.gallery.widget.MediaGridInset;

import java.io.File;
import java.util.ArrayList;

import static com.tokopedia.imagepicker.picker.album.AlbumPickerActivity.EXTRA_ALBUM_ITEM;
import static com.tokopedia.imagepicker.picker.album.AlbumPickerActivity.EXTRA_ALBUM_POSITION;
import static com.tokopedia.imagepicker.picker.gallery.model.AlbumItem.ALBUM_ID_ALL;

/**
 * Created by hendry on 19/04/18.
 */

public class ImagePickerGalleryFragment extends TkpdBaseV4Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
        AlbumMediaAdapter.OnMediaClickListener {

    public static final String ARGS_GALLERY_TYPE = "args_gallery_type";
    public static final String ARGS_SUPPORT_MULTIPLE = "args_support_multiple";
    public static final String ARGS_MIN_RESOLUTION = "args_min_resolution";

    public static final String SAVED_ALBUM_SELECTION = "svd_album_selection";
    public static final String SAVED_ALBUM_TITLE_ID = "svd_album_title_id";

    public static final int ALBUM_REQUEST_CODE = 932;

    private static final int ALBUM_LOADER_ID = 1;
    private static final int MEDIA_LOADER_ID = 2;

    public static final int SPAN_COUNT = 3;

    private OnImagePickerGalleryFragmentListener onImagePickerGalleryFragmentListener;
    private View loadingView;
    private RecyclerView recyclerView;
    private AlbumMediaAdapter albumMediaAdapter;

    private AlbumItem selectedAlbumItem;
    private int selectedAlbumPosition;
    private @GalleryType
    int galleryType;
    private boolean supportMultipleSelection;
    private int minImageResolution;

    private ArrayList<Long> albumSelectionId;
    private LabelView labelViewAlbum;

    public interface OnImagePickerGalleryFragmentListener {
        void onAlbumItemClicked(MediaItem item, boolean isChecked);
    }

    @SuppressLint("MissingPermission")
    @RequiresPermission("android.permission.CAMERA")
    public static ImagePickerGalleryFragment newInstance(@GalleryType int galleryType,
                                                         boolean supportMultipleSelection,
                                                         int minImageResolution) {
        ImagePickerGalleryFragment imagePickerGalleryFragment = new ImagePickerGalleryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_GALLERY_TYPE, galleryType);
        bundle.putBoolean(ARGS_SUPPORT_MULTIPLE, supportMultipleSelection);
        bundle.putInt(ARGS_MIN_RESOLUTION, minImageResolution);
        imagePickerGalleryFragment.setArguments(bundle);
        return imagePickerGalleryFragment;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        galleryType = bundle.getInt(ARGS_GALLERY_TYPE);
        supportMultipleSelection = bundle.getBoolean(ARGS_SUPPORT_MULTIPLE);
        minImageResolution = bundle.getInt(ARGS_MIN_RESOLUTION);
        if (savedInstanceState != null) {
            albumSelectionId = (ArrayList<Long>) savedInstanceState.getSerializable(SAVED_ALBUM_SELECTION);
            selectedAlbumPosition = savedInstanceState.getInt(SAVED_ALBUM_TITLE_ID);
        }
        albumMediaAdapter = new AlbumMediaAdapter(supportMultipleSelection, albumSelectionId, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_picker_gallery, container, false);
        loadingView = view.findViewById(R.id.loading);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT));
        int spacing = getResources().getDimensionPixelSize(R.dimen.media_grid_spacing);
        recyclerView.addItemDecoration(new MediaGridInset(SPAN_COUNT, spacing, false));
        recyclerView.setAdapter(albumMediaAdapter);
        RecyclerView.ItemAnimator itemAnimator = recyclerView.getItemAnimator();
        if (itemAnimator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
        labelViewAlbum = view.findViewById(R.id.label_view_album);
        labelViewAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AlbumPickerActivity.getIntent(getActivity(), selectedAlbumItem.getmId(), galleryType);
                startActivityForResult(intent, ALBUM_REQUEST_CODE);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ALBUM_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                selectedAlbumPosition = data.getIntExtra(EXTRA_ALBUM_POSITION, 0);
                getLoaderManager().restartLoader(ALBUM_LOADER_ID, null, ImagePickerGalleryFragment.this);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showLoading();
        getLoaderManager().initLoader(ALBUM_LOADER_ID, null, ImagePickerGalleryFragment.this);
    }

    private void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getLoaderManager().destroyLoader(ALBUM_LOADER_ID);
        getLoaderManager().destroyLoader(MEDIA_LOADER_ID);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ALBUM_LOADER_ID:
                return AlbumLoader.createInstance(getContext(), galleryType);
            case MEDIA_LOADER_ID:
            default:
                return AlbumMediaLoader.newInstance(getContext(), selectedAlbumItem, galleryType);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        hideLoading();
        switch (loader.getId()) {
            case ALBUM_LOADER_ID:
                onAlbumLoadedCursor(cursor);
                break;
            case MEDIA_LOADER_ID:
            default:
                albumMediaAdapter.swapCursor(cursor);
                break;
        }
    }

    public void onAlbumLoadedCursor(final Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    if (selectedAlbumPosition > 0) {
                        cursor.moveToPosition(selectedAlbumPosition);
                    } else {
                        cursor.moveToFirst();
                    }
                    AlbumItem albumItem = AlbumItem.valueOf(cursor);
                    onAlbumLoaded(albumItem);
                }
            });
        }
    }

    private void onAlbumLoaded(AlbumItem albumItem) {
        if (albumItem == null) {
            albumItem = new AlbumItem(ALBUM_ID_ALL, null,null, 0);
        }
        if (albumItem.isAll()) {
            albumItem.addCaptureCount();
        }
        selectAlbum(albumItem);
    }

    public void selectAlbum(AlbumItem albumItem) {
        hideLoading();

        selectedAlbumItem = albumItem;

        labelViewAlbum.setContent(albumItem.isAll() ?
                getString(R.string.default_all_album) :
                albumItem.getDisplayName());
        if (albumItem.isAll() && albumItem.isEmpty()) {
            NetworkErrorHelper.showEmptyState(getContext(), getView(), getString(R.string.error_no_media_storage), null);
        } else {
            getLoaderManager().restartLoader(MEDIA_LOADER_ID, null, this);
        }
    }

    @Override
    public void onMediaClick(MediaItem item, boolean isChecked, int adapterPosition) {
        onImagePickerGalleryFragmentListener.onAlbumItemClicked(item, isChecked);
    }

    @Override
    public boolean isImageValid(MediaItem item) {
        //TODO check the image number allowed.

        // check if file exists
        if (! new File(item.getRealPath()).exists()){
            NetworkErrorHelper.showRedCloseSnackbar(getView(), getString(R.string.image_not_found));
            return false;
        }
        //check image resolution
        if (item.getWidth() < minImageResolution || item.getHeight() < minImageResolution) {
            NetworkErrorHelper.showRedCloseSnackbar(getView(), getString(R.string.image_under_x_resolution, minImageResolution));
            return false;
        }
        return true;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case ALBUM_LOADER_ID:
                onAlbumLoaded(null);
                break;
            case MEDIA_LOADER_ID:
            default:
                albumMediaAdapter.swapCursor(null);
                break;
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        onImagePickerGalleryFragmentListener = (OnImagePickerGalleryFragmentListener) context;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SAVED_ALBUM_SELECTION, albumSelectionId);
        outState.putInt(SAVED_ALBUM_TITLE_ID, selectedAlbumPosition);
    }
}
