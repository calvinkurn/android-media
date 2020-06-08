package com.tokopedia.play.broadcaster.view.bottomsheet

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.imagepicker.picker.gallery.adapter.AlbumMediaAdapter
import com.tokopedia.imagepicker.picker.gallery.loader.AlbumLoader
import com.tokopedia.imagepicker.picker.gallery.loader.AlbumMediaLoader
import com.tokopedia.imagepicker.picker.gallery.model.AlbumItem
import com.tokopedia.imagepicker.picker.gallery.model.MediaItem
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.gallery.widget.MediaGridInset
import com.tokopedia.play.broadcaster.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.bottom_sheet_play_cover_from_gallery.*
import kotlinx.android.synthetic.main.bottom_sheet_play_cover_from_gallery.view.*

/**
 * @author by furqan on 08/06/2020
 */
class PlayBroadcastCoverFromGalleryBottomSheet : BottomSheetUnify(),
        AlbumMediaAdapter.OnMediaClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private lateinit var mChildView: View

    private var albumTitle: String = DEFAULT_ALBUM_TITLE
    private var selectedAlbumItem: AlbumItem? = null
    private var selectedAlbumPosition: Int = 0
    private lateinit var albumMediaAdapter: AlbumMediaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            albumTitle = it.getString(SAVED_ALBUM_TITLE) ?: DEFAULT_ALBUM_TITLE
        }

        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_ALBUM_TITLE, albumTitle)
    }

    override fun onResume() {
        super.onResume()
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        if (ActivityCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED) {
            showLoading()
            LoaderManager.getInstance(this).initLoader(ALBUM_LOADER_ID, null, this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LoaderManager.getInstance(this).destroyLoader(ALBUM_LOADER_ID)
        LoaderManager.getInstance(this).destroyLoader(MEDIA_LOADER_ID)
    }

    override fun isMediaValid(item: MediaItem?): Boolean = true

    override fun canAddMoreMedia(): Boolean = false

    override fun onMediaClick(item: MediaItem?, checked: Boolean, adapterPosition: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        return if (ActivityCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED) {
            when (id) {
                ALBUM_LOADER_ID -> AlbumLoader.createInstance(requireContext(), GalleryType.IMAGE_ONLY)
                MEDIA_LOADER_ID -> AlbumMediaLoader.newInstance(requireContext(), selectedAlbumItem, GalleryType.IMAGE_ONLY)
                else -> Loader<Cursor>(requireContext())
            }
        } else {
            Loader<Cursor>(requireContext())
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        hideLoading()
        when (loader.id) {
            ALBUM_LOADER_ID -> onAlbumLoadedCursor(data)
            MEDIA_LOADER_ID -> albumMediaAdapter.swapCursor(data)
            else -> albumMediaAdapter.swapCursor(data)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        when (loader.id) {
            ALBUM_LOADER_ID -> onAlbumLoaded(null)
            MEDIA_LOADER_ID -> albumMediaAdapter.swapCursor(null)
            else -> albumMediaAdapter.swapCursor(null)
        }
    }

    private fun initBottomSheet() {
        showCloseIcon = false
        showKnob = true
        isFullpage = false
        isDragable = true
        isHideable = true

        mChildView = View.inflate(requireContext(), R.layout.bottom_sheet_play_cover_from_gallery, null)
        setChild(mChildView)

        albumMediaAdapter = AlbumMediaAdapter(false,
                arrayListOf(), this)
    }

    private fun initView() {
        bottomSheetHeader.visibility = View.GONE
        bottomSheetWrapper.setPadding(0,
                bottomSheetWrapper.paddingTop,
                0,
                bottomSheetWrapper.paddingBottom)

        rvPlayGallery.setHasFixedSize(true)
        rvPlayGallery.layoutManager = GridLayoutManager(requireContext(), DEFAULT_GALLERY_SPAN_COUNT)
        rvPlayGallery.addItemDecoration(MediaGridInset(DEFAULT_GALLERY_SPAN_COUNT,
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl1),
                false))
        rvPlayGallery.adapter = albumMediaAdapter
    }

    private fun showLoading() {
        mChildView.progressPlayGallery?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        mChildView.progressPlayGallery?.visibility = View.GONE
    }

    private fun onAlbumLoadedCursor(cursor: Cursor?) {
        cursor?.let {
            if (it.count > 0) {
                Handler().post {
                    if (isAdded) {
                        if (it.isClosed) {
                            return@post
                        }
                        if (selectedAlbumPosition > 0) {
                            it.moveToPosition(selectedAlbumPosition)
                        } else {
                            it.moveToFirst()
                        }
                        val albumItem = AlbumItem.valueOf(it)
                        onAlbumLoaded(albumItem)
                    }
                }
            }
        }
    }

    private fun onAlbumLoaded(loadedAlbumItem: AlbumItem?) {
        val albumItem = loadedAlbumItem?.let {
            it
        } ?: AlbumItem(AlbumItem.ALBUM_ID_ALL, null, null, 0)
        if (albumItem.isAll) {
            albumItem.addCaptureCount()
        }
        selectAlbum(albumItem)
    }

    private fun selectAlbum(albumItem: AlbumItem) {
        hideLoading()
        selectedAlbumItem = albumItem

        tvPlayGalleryAlbumLabel?.text = if (albumItem.isAll) DEFAULT_ALBUM_TITLE else albumItem.displayName
        if (albumItem.isAll && albumItem.isEmpty) {
            Toaster.make(mChildView,
                    getString(com.tokopedia.imagepicker.R.string.error_no_media_storage),
                    Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR)
        } else {
            LoaderManager.getInstance(this).restartLoader(MEDIA_LOADER_ID, null, this)
        }
    }

    companion object {
        private const val SAVED_ALBUM_TITLE = "SAVED_ALBUM_TITLE"

        private const val ALBUM_LOADER_ID = 1
        private const val MEDIA_LOADER_ID = 2

        private const val DEFAULT_ALBUM_TITLE = "Semua media"
        private const val DEFAULT_GALLERY_SPAN_COUNT = 4
    }
}