package com.tokopedia.core.gallery;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.lang.ref.WeakReference;

/**
 * Created by hangnadi on 5/22/17.
 */

public class AlbumCollection implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 1;
    private static final String STATE_CURRENT_SELECTION = "state_current_selection";

    private AlbumCallbacks callbacks;
    private LoaderManager loaderManager;
    private WeakReference<Context> context;
    private int mCurrentSelection;
    private int galleryType;

    public AlbumCollection() {
        galleryType = GalleryType.ofAll();
    }

    public void setGalleryType(int galleryType) {
        this.galleryType = galleryType;
    }

    public interface AlbumCallbacks {
        void onAlbumLoad(Cursor cursor);
        void onAlbumReset();
    }

    public void onCreate(FragmentActivity activity, AlbumCallbacks callbacks) {
        this.context = new WeakReference<Context>(activity);
        this.loaderManager = activity.getSupportLoaderManager();
        this.callbacks = callbacks;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Context context = this.context.get();
        if (context == null) {
            return null;
        }
        return AlbumLoader.createInstance(context, galleryType);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Context context = this.context.get();
        if (context == null) {
            return;
        }
        callbacks.onAlbumLoad(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Context context = this.context.get();
        if (context == null) {
            return;
        }
        callbacks.onAlbumReset();
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }

        mCurrentSelection = savedInstanceState.getInt(STATE_CURRENT_SELECTION);
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_CURRENT_SELECTION, mCurrentSelection);
    }

    public void onDestroy() {
        loaderManager.destroyLoader(LOADER_ID);
        callbacks = null;
    }

    public void loadAlbums() {
        loaderManager.initLoader(LOADER_ID, null, this);
    }

    public int getCurrentSelection() {
        return mCurrentSelection;
    }

    public void setStateCurrentSelection(int currentSelection) {
        mCurrentSelection = currentSelection;
    }
}
