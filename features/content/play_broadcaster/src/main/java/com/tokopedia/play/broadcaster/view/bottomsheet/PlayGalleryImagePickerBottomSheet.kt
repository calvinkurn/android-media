package com.tokopedia.play.broadcaster.view.bottomsheet

import android.app.Dialog
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.imagepicker.common.GalleryType
import com.tokopedia.imagepicker.common.adapter.AlbumAdapter
import com.tokopedia.imagepicker.common.adapter.AlbumMediaAdapter
import com.tokopedia.imagepicker.common.loader.AlbumLoader
import com.tokopedia.imagepicker.common.loader.AlbumMediaLoader
import com.tokopedia.imagepicker.common.model.AlbumItem
import com.tokopedia.imagepicker.common.model.MediaItem
import com.tokopedia.imagepicker.common.widget.MediaGridInset
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.util.bottomsheet.PlayBroadcastDialogCustomizer
import com.tokopedia.play.broadcaster.util.extension.showToaster
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.bottom_sheet_play_cover_from_gallery.*
import java.io.File
import javax.inject.Inject

/**
 * @author by furqan on 08/06/2020
 */
class PlayGalleryImagePickerBottomSheet @Inject constructor(
        private val dialogCustomizer: PlayBroadcastDialogCustomizer
) : BottomSheetDialogFragment(),
        AlbumMediaAdapter.OnMediaClickListener,
        AlbumAdapter.OnAlbumAdapterListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    var mListener: Listener? = null

    private lateinit var albumMediaAdapter: AlbumMediaAdapter
    private lateinit var albumAdapter: AlbumAdapter

    private var currentViewState = MEDIA_VIEW
    private var albumTitle: String = DEFAULT_ALBUM_TITLE
    private var selectedAlbumItem: AlbumItem? = null
    private var selectedAlbumPosition: Int = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            dialogCustomizer.customize(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            albumTitle = it.getString(SAVED_ALBUM_TITLE) ?: DEFAULT_ALBUM_TITLE
        }

        initBottomSheet()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_play_cover_from_gallery, container, false)
        dialog?.let { setupDialog(it) }
        return view
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
        showLoading()
        hideMediaLayout()
        hideAlbumLayout()
        LoaderManager.getInstance(this).initLoader(ALBUM_LOADER_ID, null, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        LoaderManager.getInstance(this).destroyLoader(ALBUM_LOADER_ID)
        LoaderManager.getInstance(this).destroyLoader(MEDIA_LOADER_ID)
    }

    /**
     * Because the error toaster is different with in Image Picker Adapter (old error)
     * so we don't use this method validation, so it is always return true
     */
    override fun isMediaValid(item: MediaItem?): Boolean = true

    /**
     * Because it is not multiple selection, so can add more media always return true
     */
    override fun canAddMoreMedia(): Boolean = true

    /**
     * Our own validation runs here
     */
    override fun onMediaClick(item: MediaItem?, checked: Boolean, adapterPosition: Int) {
        item?.let {
            if (isMediaPassValidation(it)) {
                mListener?.onGetCoverFromGallery(it.contentUri)
                dismiss()
            }
        }
    }

    override fun onAlbumClicked(albumItem: AlbumItem?, position: Int) {
        selectedAlbumItem = albumItem
        selectedAlbumPosition = position
        currentViewState = MEDIA_VIEW
        LoaderManager.getInstance(this).restartLoader(ALBUM_LOADER_ID, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return when (id) {
            ALBUM_LOADER_ID -> AlbumLoader.newInstance(requireContext(), GalleryType.IMAGE_ONLY)
            MEDIA_LOADER_ID -> AlbumMediaLoader.newInstance(requireContext(), selectedAlbumItem?.intoAlbum(), GalleryType.IMAGE_ONLY)
            else -> Loader<Cursor>(requireContext())
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
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

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun initBottomSheet() {
        albumMediaAdapter = AlbumMediaAdapter(false,
                arrayListOf(), this)
        albumAdapter = AlbumAdapter(requireContext(), this, GalleryType.IMAGE_ONLY)
    }

    private fun setupDialog(dialog: Dialog) {
        dialog.setOnShowListener {
            val bottomSheetDialog = dialog as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            val maxHeight = (HEIGHT_MULTIPLIER * getScreenHeight()).toInt()
            bottomSheet?.layoutParams = bottomSheet?.layoutParams?.apply {
                height = maxHeight
            }
            bottomSheet?.setBackgroundColor(Color.TRANSPARENT)
            bottomSheet?.let {
                val bottomSheetBehavior = BottomSheetBehavior.from(it)
                bottomSheetBehavior.isHideable = true
                bottomSheetBehavior.skipCollapsed = true
                bottomSheetBehavior.peekHeight = maxHeight
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }

            isCancelable = true
        }
    }

    private fun initView() {
        rvPlayGallery.layoutManager = GridLayoutManager(requireContext(), DEFAULT_GALLERY_SPAN_COUNT)
        rvPlayGallery.addItemDecoration(MediaGridInset(DEFAULT_GALLERY_SPAN_COUNT,
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl1),
                false))
        rvPlayGallery.adapter = albumMediaAdapter

        rvPlayAlbum.layoutManager = LinearLayoutManager(requireContext(),
                RecyclerView.VERTICAL, false)
        rvPlayAlbum.adapter = albumAdapter

        containerPlayGalleryHeader.setOnClickListener {
            hideMediaLayout()
            showAlbumLayout()
        }

        showLoading()
        hideMediaLayout()
        hideAlbumLayout()
    }

    private fun showLoading() {
        containerPlayLoading?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        containerPlayLoading?.visibility = View.GONE
    }

    private fun showMediaLayout() {
        containerPlayGalleryHeader?.visibility = View.VISIBLE
        rvPlayGallery?.visibility = View.VISIBLE
    }

    private fun hideMediaLayout() {
        containerPlayGalleryHeader?.visibility = View.GONE
        rvPlayGallery?.visibility = View.GONE
    }

    private fun showAlbumLayout() {
        rvPlayAlbum?.visibility = View.VISIBLE
    }

    private fun hideAlbumLayout() {
        rvPlayAlbum?.visibility = View.GONE
    }

    private fun onAlbumLoadedCursor(cursor: Cursor?) {
        albumAdapter.swapCursor(cursor)
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
        showMediaLayout()
        hideAlbumLayout()
        selectedAlbumItem = albumItem

        tvPlayGalleryAlbumLabel?.text = if (albumItem.isAll) DEFAULT_ALBUM_TITLE else albumItem.displayName
        if (albumItem.isAll && albumItem.isEmpty) {
            showToaster(
                    message = getString(com.tokopedia.imagepicker.common.R.string.error_no_media_storage),
                    type = Toaster.TYPE_ERROR
            )
        } else {
            LoaderManager.getInstance(this).restartLoader(MEDIA_LOADER_ID, null, this)
        }
    }

    private fun isMediaPassValidation(mediaItem: MediaItem): Boolean {
        // check if file exists
        val file = File(mediaItem.path)
        if (!file.exists()) {
            showToaster(
                    message = getString(R.string.play_prepare_cover_gallery_error_not_found_label),
                    type = Toaster.TYPE_ERROR,
                    actionLabel = getString(R.string.play_ok)
            )
            return false
        }

        // check image resolution
        if ((file.length() / BYTES_IN_KB) > MAXIMUM_COVER_SIZE) {
            showToaster(
                    message = getString(R.string.play_prepare_cover_gallery_error_size_label, MAXIMUM_COVER_SIZE / BYTES_IN_KB),
                    type = Toaster.TYPE_ERROR,
                    actionLabel = getString(R.string.play_ok)
            )
            return false
        }
        if (mediaItem.getWidth(requireContext()) < MINIMUM_COVER_WIDTH ||
                mediaItem.getHeight(requireContext()) < MINIMUM_COVER_HEIGHT) {
            showToaster(
                    message = getString(R.string.play_prepare_cover_gallery_error_pixel_label, MINIMUM_COVER_WIDTH, MINIMUM_COVER_HEIGHT),
                    type = Toaster.TYPE_ERROR,
                    actionLabel = getString(R.string.play_ok)
            )
            return false
        }

        return true
    }

    private fun showToaster(
            message: String,
            type: Int = Toaster.TYPE_NORMAL,
            duration: Int = Snackbar.LENGTH_SHORT,
            actionLabel: String = ""
    ) {
        requireView().showToaster(
                message = message,
                actionLabel = actionLabel,
                duration = duration,
                type = type
        )
    }

    interface Listener {
        fun onGetCoverFromGallery(imageUri: Uri?)
    }

    companion object {
        const val TAG = "Gallery Image Picker"

        const val MINIMUM_COVER_WIDTH = 324
        const val MINIMUM_COVER_HEIGHT = 576

        private const val SAVED_ALBUM_TITLE = "SAVED_ALBUM_TITLE"

        private const val ALBUM_LOADER_ID = 1
        private const val MEDIA_LOADER_ID = 2

        private const val ALBUM_VIEW = 1
        private const val MEDIA_VIEW = 2

        private const val DEFAULT_ALBUM_TITLE = "Semua media"
        private const val DEFAULT_GALLERY_SPAN_COUNT = 4
        private const val MAXIMUM_COVER_SIZE = 5120
        private const val BYTES_IN_KB = 1024

        private const val HEIGHT_MULTIPLIER = 0.95f
    }
}