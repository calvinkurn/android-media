package com.tokopedia.imagepicker.picker.gallery;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.common.GalleryType;
import com.tokopedia.imagepicker.picker.album.AlbumPickerActivity;
import com.tokopedia.imagepicker.common.adapter.AlbumMediaAdapter;
import com.tokopedia.imagepicker.common.internal.entity.Album;
import com.tokopedia.imagepicker.common.loader.AlbumLoader;
import com.tokopedia.imagepicker.common.loader.AlbumMediaLoader;
import com.tokopedia.imagepicker.common.model.AlbumItem;
import com.tokopedia.imagepicker.common.model.MediaItem;
import com.tokopedia.imagepicker.common.widget.MediaGridInset;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerInterface;

import java.io.File;
import java.util.ArrayList;

import static com.tokopedia.imagepicker.common.BuilderConstantKt.DEFAULT_MIN_RESOLUTION;
import static com.tokopedia.imagepicker.picker.album.AlbumPickerActivity.EXTRA_ALBUM_ITEM;
import static com.tokopedia.imagepicker.picker.album.AlbumPickerActivity.EXTRA_ALBUM_POSITION;
import static com.tokopedia.imagepicker.common.model.AlbumItem.ALBUM_ID_ALL;

/**
 * Created by hendry on 19/04/18.
 */

public class ImagePickerGalleryFragment extends TkpdBaseV4Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
        AlbumMediaAdapter.OnMediaClickListener,
        ImagePickerInterface {

    public static final String ARGS_GALLERY_TYPE = "args_gallery_type";
    public static final String ARGS_SUPPORT_MULTIPLE = "args_support_multiple";
    public static final String ARGS_MIN_RESOLUTION = "args_min_resolution";
    public static final String ARGS_ERROR_MIN_RESOLUTION = "args_error_min_resolution";
    public static final String ARGS_ERROR_IMAGE_TOO_LARGE = "args_error_image_too_large";

    public static final String SAVED_ALBUM_TITLE_ID = "svd_album_title_id";
    public static final long MAX_VIDEO_DURATION_MS = 60000L;

    public static final int ALBUM_REQUEST_CODE = 932;

    private static final int ALBUM_LOADER_ID = 1;
    private static final int MEDIA_LOADER_ID = 2;
    public static final int BYTES_IN_KB = 1024;

    private OnImagePickerGalleryFragmentListener onImagePickerGalleryFragmentListener;
    private View loadingView;
    private RecyclerView recyclerView;
    private AlbumMediaAdapter albumMediaAdapter;

    private AlbumItem selectedAlbumItem;
    private int selectedAlbumPosition;
    private
    GalleryType galleryType = GalleryType.IMAGE_ONLY;
    private boolean supportMultipleSelection = false;
    private int minImageResolution = DEFAULT_MIN_RESOLUTION;
    private String belowMinImageResolutionErrorMessage = "";
    private String imageTooLargeErrorMessage = "";

    private LabelView labelViewAlbum;

    public interface OnImagePickerGalleryFragmentListener {
        void onAlbumItemClicked(MediaItem item, boolean isChecked);

        boolean isMaxImageReached();

        ArrayList<String> getImagePath();

        long getMaxFileSize();
    }

    @SuppressLint("MissingPermission")
    @RequiresPermission("android.permission.CAMERA")
    public static ImagePickerGalleryFragment newInstance(GalleryType galleryType,
                                                         boolean supportMultipleSelection,
                                                         int minImageResolution,
                                                         String imageBelowMinresolutionErrorMessage,
                                                         String imageTooLargeErrorMessage) {
        ImagePickerGalleryFragment imagePickerGalleryFragment = new ImagePickerGalleryFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_GALLERY_TYPE, galleryType);
        bundle.putBoolean(ARGS_SUPPORT_MULTIPLE, supportMultipleSelection);
        bundle.putInt(ARGS_MIN_RESOLUTION, minImageResolution);
        bundle.putString(ARGS_ERROR_MIN_RESOLUTION, imageBelowMinresolutionErrorMessage);
        bundle.putString(ARGS_ERROR_IMAGE_TOO_LARGE, imageTooLargeErrorMessage);
        imagePickerGalleryFragment.setArguments(bundle);
        return imagePickerGalleryFragment;
    }

    @SuppressLint("MissingPermission")
    @RequiresPermission("android.permission.CAMERA")
    public static ImagePickerGalleryFragment newInstance(GalleryType galleryType,
                                                         boolean supportMultipleSelection,
                                                         int minImageResolution) {
        return newInstance(galleryType, supportMultipleSelection, minImageResolution, "", "");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            galleryType = bundle.getParcelable(ARGS_GALLERY_TYPE);
            supportMultipleSelection = bundle.getBoolean(ARGS_SUPPORT_MULTIPLE);
            minImageResolution = bundle.getInt(ARGS_MIN_RESOLUTION);
            belowMinImageResolutionErrorMessage = bundle.getString(ARGS_ERROR_MIN_RESOLUTION, "");
            imageTooLargeErrorMessage = bundle.getString(ARGS_ERROR_IMAGE_TOO_LARGE, "");
        }
        if (belowMinImageResolutionErrorMessage == null || belowMinImageResolutionErrorMessage.isEmpty()) {
            belowMinImageResolutionErrorMessage = getString(R.string.image_under_x_resolution, minImageResolution);
        }
        if (imageTooLargeErrorMessage == null || imageTooLargeErrorMessage.isEmpty()) {
            imageTooLargeErrorMessage = getString(R.string.max_file_size_reached);
        }
        if (savedInstanceState != null) {
            selectedAlbumPosition = savedInstanceState.getInt(SAVED_ALBUM_TITLE_ID);
        }
        albumMediaAdapter = new AlbumMediaAdapter(supportMultipleSelection,
                onImagePickerGalleryFragmentListener.getImagePath(),
                this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_picker_gallery, container, false);
        loadingView = view.findViewById(R.id.loading);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        int spanCount = getContext().getResources().getInteger(R.integer.gallery_span_count);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), spanCount));
        int spacing = getResources().getDimensionPixelSize(com.tokopedia.imagepicker.common.R.dimen.image_picker_media_grid_spacing);
        recyclerView.addItemDecoration(new MediaGridInset(spanCount, spacing, false));
        recyclerView.setAdapter(albumMediaAdapter);
        RecyclerView.ItemAnimator itemAnimator = recyclerView.getItemAnimator();
        if (itemAnimator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
        labelViewAlbum = view.findViewById(R.id.label_view_album);
        labelViewAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String albumItemId = selectedAlbumItem == null ? AlbumItem.ALBUM_ID_ALL : selectedAlbumItem.getmId();
                Intent intent = AlbumPickerActivity.getIntent(getActivity(), albumItemId, galleryType);
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
                selectedAlbumItem = data.getParcelableExtra(EXTRA_ALBUM_ITEM);
                selectedAlbumPosition = data.getIntExtra(EXTRA_ALBUM_POSITION, 0);
                LoaderManager.getInstance(this).restartLoader(ALBUM_LOADER_ID, null, ImagePickerGalleryFragment.this);
            }
        }
    }

    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    @Override
    public void onResume() {
        super.onResume();
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (ActivityCompat.checkSelfPermission(getContext(), permission) == PackageManager.PERMISSION_GRANTED) {
            showLoading();
            LoaderManager.getInstance(this).initLoader(ALBUM_LOADER_ID, null, ImagePickerGalleryFragment.this);
        }
    }

    private void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void afterThumbnailImageRemoved() {
        albumMediaAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LoaderManager loaderManager = LoaderManager.getInstance(this);
        loaderManager.destroyLoader(ALBUM_LOADER_ID);
        loaderManager.destroyLoader(MEDIA_LOADER_ID);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (ActivityCompat.checkSelfPermission(getContext(), permission) == PackageManager.PERMISSION_GRANTED) {
            switch (id) {
                case ALBUM_LOADER_ID:
                    return AlbumLoader.newInstance(getContext(), galleryType);
                case MEDIA_LOADER_ID:
                    Album album = selectedAlbumItem.intoAlbum();
                    return AlbumMediaLoader.newInstance(getContext(), album, galleryType);
                default:
                    return new Loader<>(getContext());
            }
        } else {
            return new Loader<>(getContext());
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
                    if (isAdded()) {
                        if (cursor.isClosed()) {
                            return;
                        }
                        if (selectedAlbumPosition > 0) {
                            cursor.moveToPosition(selectedAlbumPosition);
                        } else {
                            cursor.moveToFirst();
                        }
                        AlbumItem albumItem = AlbumItem.valueOf(cursor);
                        onAlbumLoaded(albumItem);
                    }
                }
            });
        }
    }

    private void onAlbumLoaded(@Nullable AlbumItem albumItem) {
        if (albumItem == null) {
            albumItem = new AlbumItem(ALBUM_ID_ALL, null, null, 0);
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
            NetworkErrorHelper.showEmptyState(getContext(), getView(), getString(com.tokopedia.imagepicker.common.R.string.error_no_media_storage), null);
        } else {
            LoaderManager.getInstance(this).restartLoader(MEDIA_LOADER_ID, null, this);
        }
    }

    @Override
    public void onMediaClick(MediaItem item, boolean isChecked, int adapterPosition) {
        onImagePickerGalleryFragmentListener.onAlbumItemClicked(item, isChecked);
    }

    @Override
    public boolean canAddMoreMedia() {
        //check the image number allowed.
        if (onImagePickerGalleryFragmentListener.isMaxImageReached()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isMediaValid(MediaItem item) {
        Context context = getContext();
        if (context == null) {
            return false;
        }
        // check if file exists
        File file = new File(item.getPath());
        if (!file.exists()) {
            NetworkErrorHelper.showRedCloseSnackbar(getView(),
                    galleryType == GalleryType.VIDEO_ONLY ? getString(R.string.video_not_found) :
                            getString(R.string.image_not_found));
            return false;
        }
        //check image resolution
        if (item.isVideo() && item.getDuration() > 0) { // it is video
            if ((item.getSize() / BYTES_IN_KB) > onImagePickerGalleryFragmentListener.getMaxFileSize()) {
                NetworkErrorHelper.showRedCloseSnackbar(getView(), getString(R.string.max_video_size_reached));
                return false;
            }
            if (item.getDuration() > MAX_VIDEO_DURATION_MS) {
                NetworkErrorHelper.showRedCloseSnackbar(getView(), getString(R.string.max_video_duration_reached));
                return false;
            }
        } else {
            if ((item.getSize() / BYTES_IN_KB) > onImagePickerGalleryFragmentListener.getMaxFileSize()) {
                NetworkErrorHelper.showRedCloseSnackbar(getView(), imageTooLargeErrorMessage);
                return false;
            }
            if (item.getWidth(context) < minImageResolution || item.getHeight(context) < minImageResolution) {
                NetworkErrorHelper.showRedCloseSnackbar(getView(), belowMinImageResolutionErrorMessage);
                return false;
            }
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
        outState.putInt(SAVED_ALBUM_TITLE_ID, selectedAlbumPosition);
    }
}
