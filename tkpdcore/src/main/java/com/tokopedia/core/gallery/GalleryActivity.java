package com.tokopedia.core.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.myproduct.utils.FileUtils;
import com.tokopedia.core.network.NetworkErrorHelper;

import java.io.File;

import javax.annotation.Nonnull;

public class GalleryActivity extends TActivity implements AlbumCollection.AlbumCallbacks, AdapterView
        .OnItemSelectedListener, GallerySelectedFragment.ListenerSelected {

    private static final String TAG = "hangnadi";
    protected static final String BUNDLE_GALLERY_TYPE = "bundle_gallery_type";
    protected static final String BUNDLE_MAX_SELECTION = "bundle_max_selection";
    protected static final int DEFAULT_MAX_SELECTION = 1;
    protected static final int DEFAULT_GALLERY_TYPE = GalleryType.ofImageOnly();
    public static final String COMPRESS_TO_TKPD = "COMPRESS_TO_TKPD";
    public static final String TOKOPEDIA = "Tokopedia";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 873;

    private int typeGallery = GalleryType.ofAll();
    private int maxSelection;

    private View previewButton;
    private View applyButtonn;
    private View loading;
    private FrameLayout container;

    private final AlbumCollection albumCollection = new AlbumCollection();
    private AlbumsSpinner albumSpinner;
    private AlbumAdapter albumAdapter;
    private boolean compressToTkpd;

    public static Intent createIntent(Context context) {
        return createIntent(context, DEFAULT_GALLERY_TYPE);
    }

    public static Intent createIntent(Context context, int galleryType) {
        return createIntent(context, galleryType, DEFAULT_MAX_SELECTION, false);
    }

    public static Intent createIntent(Context context, int galleryType, int maxSelection, boolean compressToTkpd) {
        Intent intent = new Intent(context, GalleryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_GALLERY_TYPE, galleryType);
        bundle.putInt(BUNDLE_MAX_SELECTION, maxSelection);
        bundle.putBoolean(COMPRESS_TO_TKPD, compressToTkpd);
        intent.putExtras(bundle);
        return intent;
    }

    protected void setupBundlePass(@Nonnull Bundle extras) {
        typeGallery = extras.getInt(BUNDLE_GALLERY_TYPE);
        maxSelection = extras.getInt(BUNDLE_MAX_SELECTION);
        compressToTkpd = extras.getBoolean(COMPRESS_TO_TKPD);
    }

    protected void initView() {
        previewButton = findViewById(R.id.button_preview);
        applyButtonn = findViewById(R.id.button_apply);
        loading = findViewById(R.id.loading);
        container = (FrameLayout) findViewById(R.id.container);
    }

    protected void setViewListener() {
        previewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        applyButtonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Tokopedia3);
        setContentView(R.layout.activity_gallery_new);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {
            setupBundlePass(getIntent().getExtras());
        }

        initView();
        setViewListener();
        albumAdapter = new AlbumAdapter(this, null, false);
        albumSpinner = new AlbumsSpinner(this);
        albumSpinner.setOnItemSelectedListener(this);
        albumSpinner.setSelectedTextView((TextView) findViewById(R.id.selected_album));
        albumSpinner.setPopupAnchorView(findViewById(R.id.toolbar));
        albumSpinner.setAdapter(albumAdapter);
        albumCollection.onCreate(this, this);
        albumCollection.onRestoreInstanceState(savedInstanceState);
        albumCollection.setGalleryType(typeGallery);
        albumCollection.loadAlbums();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAlbumLoad(final Cursor cursor) {
        albumAdapter.swapCursor(cursor);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                cursor.moveToPosition(albumCollection.getCurrentSelection());
                albumSpinner.setSelection(GalleryActivity.this,
                        albumCollection.getCurrentSelection());
                AlbumItem albumItem = AlbumItem.valueOf(cursor);
                if (albumItem.isAll()) {
                    albumItem.addCaptureCount();
                }
                onAlbumSelected(albumItem);
            }
        });
    }

    private void onAlbumSelected(AlbumItem albumItem) {
        if (albumItem.isAll() && albumItem.isEmpty()) {
            loading.setVisibility(View.GONE);
            NetworkErrorHelper.showEmptyState(this, container, getString(R.string.error_no_media_storage), null);
        } else {
            loading.setVisibility(View.GONE);
            inflateFragment(albumItem);
        }
    }

    private void inflateFragment(AlbumItem albumItem) {
        Fragment fragment = GallerySelectedFragment.newInstance(albumItem, typeGallery);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment, TAG)
                .commit();
    }

    @Override
    public void onAlbumReset() {
        albumAdapter.swapCursor(null);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        albumCollection.setStateCurrentSelection(position);
        albumAdapter.getCursor().moveToPosition(position);
        AlbumItem albumItem = AlbumItem.valueOf(albumAdapter.getCursor());
        if (albumItem.isAll()) {
            albumItem.addCaptureCount();
        }
        onAlbumSelected(albumItem);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onSelectedImage(MediaItem item) {
        if (compressToTkpd) {
            File photo = FileUtils.writeImageToTkpdPath(item.getRealPath());
            if (photo != null) {
                finishWithPathFile(photo.getAbsolutePath());
            }
        } else {
            finishWithMediaItem(item);
        }
    }

    protected void finishWithPathFile(String absolutePath) {
        Intent intent = new Intent();
        intent.putExtra(GallerySelectedFragment.EXTRA_RESULT_SELECTION_PATH, absolutePath);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    protected void finishWithMediaItem(MediaItem item) {
        Intent intent = new Intent();
        intent.putExtra(GallerySelectedFragment.EXTRA_RESULT_SELECTION, item);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
