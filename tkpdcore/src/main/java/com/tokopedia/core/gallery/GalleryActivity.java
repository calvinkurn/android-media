package com.tokopedia.core.gallery;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.tokopedia.core.R;

import javax.annotation.Nonnull;

public class GalleryActivity extends AppCompatActivity implements AlbumCollection.AlbumCallbacks, AdapterView.OnItemSelectedListener {

    private static final String TAG = "hangnadi";
    private static final String BUNDLE_GALLERY_TYPE = "bundle_gallery_type";
    private static final String BUNDLE_MAX_SELECTION = "bundle_max_selection";
    private static final int DEFAULT_MAX_SELECTION = 1;
    private static final int DEFAULT_GALLERY_TYPE = GalleryType.ofImageOnly();
    private int typeGallery;
    private int maxSelection;

    private View previewButton;
    private View applyButtonn;

    private final AlbumCollection albumCollection = new AlbumCollection();
    private AlbumsSpinner albumSpinner;
    private AlbumAdapter albumAdapter;

    public static Intent createIntent(Context context) {
        return createIntent(context, DEFAULT_GALLERY_TYPE);
    }

    public static Intent createIntent(Context context, int galleryType) {
        return createIntent(context, galleryType, DEFAULT_MAX_SELECTION);
    }

    public static Intent createIntent(Context context, int galleryType, int maxSelection) {
        Intent intent = new Intent(context, GalleryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_GALLERY_TYPE, galleryType);
        bundle.putInt(BUNDLE_MAX_SELECTION, maxSelection);
        intent.putExtras(bundle);
        return intent;
    }

    protected void setupBundlePass(@Nonnull Bundle extras) {
        typeGallery = extras.getInt(BUNDLE_GALLERY_TYPE);
        maxSelection = extras.getInt(BUNDLE_MAX_SELECTION);
    }

    protected void initView() {
        previewButton = findViewById(R.id.button_preview);
        applyButtonn = findViewById(R.id.button_apply);
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
                Album album = Album.valueOf(cursor);
                if (album.isAll()) {
                    album.addCaptureCount();
                }
                onAlbumSelected(album);
            }
        });
    }

    private void onAlbumSelected(Album album) {
        if (album.isAll() && album.isEmpty()) {
            Log.d(TAG, "onAlbumSelected:empty");
        } else {
            Log.d(TAG, "onAlbumSelected: " + album.getDisplayName());
        }
    }

    @Override
    public void onAlbumReset() {
        albumAdapter.swapCursor(null);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        albumCollection.setStateCurrentSelection(position);
        albumAdapter.getCursor().moveToPosition(position);
        Album album = Album.valueOf(albumAdapter.getCursor());
        if (album.isAll()) {
            album.addCaptureCount();
        }
        onAlbumSelected(album);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
