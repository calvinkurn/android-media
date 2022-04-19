package com.tokopedia.imagepicker.picker.album;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.common.GalleryType;
import com.tokopedia.imagepicker.common.adapter.AlbumAdapter;
import com.tokopedia.imagepicker.common.loader.AlbumLoader;
import com.tokopedia.imagepicker.common.model.AlbumItem;

import org.jetbrains.annotations.NotNull;

/**
 * Created by hendry on 08/05/18.
 */

public class AlbumPickerActivity extends BaseSimpleActivity implements LoaderManager.LoaderCallbacks<Cursor>, AlbumAdapter.OnAlbumAdapterListener {

    public static final String EXTRA_ALBUM_ID = "extra_album_id";
    public static final String EXTRA_GALLERY_TYPE = "extra_gallery_type";

    public static final String EXTRA_ALBUM_ITEM = "extra_album_item";
    public static final String EXTRA_ALBUM_POSITION = "extra_album_position";

    private static final int ALBUM_LOADER_ID = 1;

    private View loadingView;

    private AlbumAdapter albumAdapter;
    private
    GalleryType galleryType;

    public static Intent getIntent(Context context, String selectedAlbumId, GalleryType galleryType) {
        Intent intent = new Intent(context, AlbumPickerActivity.class);
        intent.putExtra(EXTRA_ALBUM_ID, selectedAlbumId);
        intent.putExtra(EXTRA_GALLERY_TYPE, galleryType.getValue());
        return intent;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_album_picker;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        int selectedAlbumId = intent.getIntExtra(EXTRA_ALBUM_ID, -1);
        galleryType = GalleryType.fromInt(intent.getIntExtra(EXTRA_GALLERY_TYPE, GalleryType.IMAGE_ONLY.getValue()));

        super.onCreate(savedInstanceState);

        albumAdapter = new AlbumAdapter(this, this, galleryType);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(albumAdapter);

        loadingView = findViewById(R.id.loading);
    }

    @Override
    public void onResume() {
        super.onResume();
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            showLoading();
            LoaderManager.getInstance(this).initLoader(ALBUM_LOADER_ID, null, this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LoaderManager.getInstance(this).destroyLoader(ALBUM_LOADER_ID);
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

    @NotNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return AlbumLoader.newInstance(this, galleryType);
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
