package com.tokopedia.imagepicker.gallery;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.gallery.adapter.AlbumMediaAdapter;
import com.tokopedia.imagepicker.gallery.loader.AlbumLoader;
import com.tokopedia.imagepicker.gallery.loader.AlbumMediaLoader;
import com.tokopedia.imagepicker.gallery.model.AlbumItem;
import com.tokopedia.imagepicker.gallery.model.MediaItem;
import com.tokopedia.imagepicker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.gallery.widget.MediaGridInset;

import java.io.File;

/**
 * Created by hendry on 19/04/18.
 */

public class ImagePickerGalleryFragment extends TkpdBaseV4Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
        AlbumMediaAdapter.OnMediaClickListener {

    public static final String ARGS_GALLERY_TYPE = "args_gallery_type";

    private static final int ALBUM_LOADER_ID = 1;
    private static final int MEDIA_LOADER_ID = 2;

    private static final int PERMISSION_REQ_CODE = 125;
    public static final int SPAN_COUNT = 3;

    private OnImagePickerGalleryFragmentListener onImagePickerGalleryFragmentListener;
    private View loadingView;
    private RecyclerView recyclerView;
    private AlbumMediaAdapter albumMediaAdapter;

    private AlbumItem selectedAlbumItem;
    private @GalleryType int galleryType;

    public interface OnImagePickerGalleryFragmentListener {
        void onAlbumLoaded(Cursor cursor);
    }

    // TODO read external directory permission
    public static ImagePickerGalleryFragment newInstance(@GalleryType int galleryType) {
        ImagePickerGalleryFragment imagePickerGalleryFragment = new ImagePickerGalleryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_GALLERY_TYPE, galleryType);
        imagePickerGalleryFragment.setArguments(bundle);
        return imagePickerGalleryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        galleryType = getArguments().getInt(ARGS_GALLERY_TYPE);
        albumMediaAdapter = new AlbumMediaAdapter();
        albumMediaAdapter.registerOnMediaClickListener(this);
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
        recyclerView.addItemDecoration(new MediaGridInset (SPAN_COUNT, spacing, false));
        recyclerView.setAdapter(albumMediaAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE);
        } else {
            // permissions is already available
            onPermissionGranted();
        }
    }

    private void onPermissionGranted() {
        showLoading();
        getLoaderManager().initLoader(ALBUM_LOADER_ID, null, this);
    }

    private void showLoading(){
        loadingView.setVisibility(View.VISIBLE);
    }

    private void hideLoading(){
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // permission has been granted
            onPermissionGranted();
        } else {
            onPermissionDenied();
        }
    }

    private void onPermissionDenied() {
        new AlertDialog.Builder(getContext())
                .setMessage(getString(R.string.please_grant_external_storage_permission_in_settings))
                .setCancelable(true)
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
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
                onImagePickerGalleryFragmentListener.onAlbumLoaded(cursor);
                break;
            case MEDIA_LOADER_ID:
            default:
                albumMediaAdapter.swapCursor(cursor);
                break;
        }
    }

    public void selectAlbum(AlbumItem albumItem) {
        selectedAlbumItem = albumItem;
        hideLoading();
        if (albumItem.isAll() && albumItem.isEmpty()) {
            NetworkErrorHelper.showEmptyState(getContext(), getView(), getString(R.string.error_no_media_storage), null);
        } else {
            getLoaderManager().initLoader(MEDIA_LOADER_ID, null, this);
        }
    }

    @Override
    public void onMediaClick(AlbumItem album, MediaItem item, int adapterPosition) {
        getHeightAndWidth(item);
        // TODO select the item
//        Intent intent = new Intent();
//        intent.putExtra(GallerySelectedFragment.EXTRA_RESULT_SELECTION, item);
//        setResult(Activity.RESULT_OK, intent);
//        finish();
    }

    public void getHeightAndWidth(MediaItem item) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(item.getRealPath()).getAbsolutePath(), options);
        item.width = options.outWidth;
        item.height = options.outHeight;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case ALBUM_LOADER_ID:
                onImagePickerGalleryFragmentListener.onAlbumLoaded(null);
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
}
