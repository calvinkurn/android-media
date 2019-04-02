package com.tokopedia.imagepicker.picker.album;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.picker.gallery.ImagePickerGalleryFragment;
import com.tokopedia.imagepicker.picker.gallery.loader.AlbumLoader;
import com.tokopedia.imagepicker.picker.gallery.model.AlbumItem;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;

/**
 * Created by hendry on 08/05/18.
 */

public class AlbumPickerActivity extends BaseSimpleActivity implements LoaderManager.LoaderCallbacks<Cursor>,AlbumAdapter.OnAlbumAdapterListener {

    public static final String EXTRA_ALBUM_ID = "extra_album_id";
    public static final String EXTRA_GALLERY_TYPE = "extra_gallery_type";

    public static final String EXTRA_ALBUM_ITEM = "extra_album_item";
    public static final String EXTRA_ALBUM_POSITION = "extra_album_position";

    private static final int ALBUM_LOADER_ID = 1;

    private int selectedAlbumId;
    private RecyclerView recyclerView;
    private View loadingView;

    private AlbumAdapter albumAdapter;
    private @GalleryType
    int galleryType;

    public static Intent getIntent(Context context, String selectedAlbumId, @GalleryType int galleryType) {
        Intent intent = new Intent(context, AlbumPickerActivity.class);
        intent.putExtra(EXTRA_ALBUM_ID, selectedAlbumId);
        intent.putExtra(EXTRA_GALLERY_TYPE, galleryType);
        return intent;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_album_picker;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        selectedAlbumId = intent.getIntExtra(EXTRA_ALBUM_ID, -1);
        galleryType = intent.getIntExtra(EXTRA_GALLERY_TYPE, GalleryType.IMAGE_ONLY);

        super.onCreate(savedInstanceState);

        albumAdapter = new AlbumAdapter(this, this, galleryType);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(albumAdapter);

        loadingView = findViewById(R.id.loading);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                showLoading();
                getSupportLoaderManager().initLoader(ALBUM_LOADER_ID, null, this);
            }
        } else {
            showLoading();
            getSupportLoaderManager().initLoader(ALBUM_LOADER_ID, null, this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getLoaderManager().destroyLoader(ALBUM_LOADER_ID);
    }

    private void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        loadingView.setVisibility(View.GONE);
    }


    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return AlbumLoader.createInstance(this, galleryType);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        hideLoading();
        albumAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        hideLoading();
        albumAdapter.swapCursor(null);
    }

    @Override
    public void onAlbumClicked(AlbumItem albumItem, int position) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_ALBUM_ITEM, albumItem);
        resultIntent.putExtra(EXTRA_ALBUM_POSITION, position);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
