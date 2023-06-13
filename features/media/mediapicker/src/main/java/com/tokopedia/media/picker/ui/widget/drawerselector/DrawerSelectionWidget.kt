package com.tokopedia.media.picker.ui.widget.drawerselector

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tokopedia.media.R
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.media.databinding.WidgetSelectionBottomNavBinding
import com.tokopedia.media.picker.ui.widget.drawerselector.adapter.DrawerSelectionAdapter
import com.tokopedia.media.picker.ui.widget.drawerselector.viewholder.ThumbnailViewHolder
import com.tokopedia.picker.common.mapper.Unify_N0

class DrawerSelectionWidget : FrameLayout {

    private var adapter: DrawerSelectionAdapter? = null
    private var placeHolderBackgroundColor: Int? = null
    private var placeholderPreview: Int? = null
    private var isDraggable: Boolean = false
    private var maxVideo: Int = 1

    private var binding: WidgetSelectionBottomNavBinding? =
        WidgetSelectionBottomNavBinding.inflate(
            LayoutInflater.from(context)
        )

    constructor (context: Context) : super(context) {
        setupView(null)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        setupView(attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        setupView(attrs)
    }

    private fun setupView(attrs: AttributeSet?) {
        if (attrs != null) setAttribute(attrs)
        addView(binding?.root)

        setupRecyclerView()
        widgetProperties()

        binding?.rvThumbnail?.itemAnimator?.let {
            if (it is SimpleItemAnimator) {
                it.supportsChangeAnimations = false
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = DrawerSelectionAdapter(arrayListOf())
        binding?.rvThumbnail?.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        binding?.rvThumbnail?.setHasFixedSize(true)
        binding?.rvThumbnail?.adapter = adapter
    }

    private fun widgetProperties() {
        placeHolderBackgroundColor?.let {
            adapter?.backgroundColorPlaceHolder = it
        }

        placeholderPreview?.let {
            adapter?.placeholderPreview = it
        }

        adapter?.isDraggable = isDraggable
    }

    private fun setAttribute(attrs: AttributeSet?) {
        val defaultColor = ContextCompat.getColor(context, Unify_N0)

        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MediaPickerPreviewWidget,
            0,
            0
        )

        placeHolderBackgroundColor = typedArray.getColor(
            R.styleable.MediaPickerPreviewWidget_backgroundColorPlaceHolder,
            defaultColor
        )

        isDraggable = typedArray.getBoolean(
            R.styleable.MediaPickerPreviewWidget_draggable,
            DEFAULT_DRAGGABLE_MODE
        )

        maxVideo = typedArray.getInteger(
            R.styleable.MediaPickerPreviewWidget_maxVideo,
            DEFAULT_MAX_VIDEO
        )

        placeholderPreview = typedArray.getResourceId(
            R.styleable.MediaPickerPreviewWidget_placeholder,
            R.drawable.picker_ic_placeholder_media_preview
        )
    }

    fun isAbleToReorder(canReorder: Boolean) {
        adapter?.isDraggable = canReorder
    }

    fun removeData(media: MediaUiModel) {
        adapter?.removeData(media)
    }

    fun addData(media: MediaUiModel?) {
        if (media == null) return
        adapter?.setData(media)
    }

    fun addAllData(medias: List<MediaUiModel>) {
        adapter?.setData(medias.toMutableList())
    }

    fun setPlaceholderPreview(@DrawableRes drawable: Int) {
        adapter?.setPlaceholder(drawable)
    }

    fun setMaxAdapterSize(size: Int) {
        adapter?.setMaxData(size)
    }

    fun setMaxVideo(max: Int) {
        maxVideo = max
    }

    fun setListener(listener: Listener) {
       adapter?.setOnDataChangedListener(listener)
    }

    fun removeListener() {
        adapter?.removeListener()
    }

    fun setThumbnailSelected(previousIndex: Int? = null, nextIndex: Int){
        // set previous index item border back to normal
        previousIndex?.let { index ->
            if(!isThumbnailViewHolder(previousIndex)) return@let
            binding?.rvThumbnail?.findViewHolderForAdapterPosition(index)?.let {
                (it as ThumbnailViewHolder).setThumbnailSelected(false)
            }
        }

        // set next index item border to selected
        binding?.rvThumbnail?.findViewHolderForAdapterPosition(nextIndex)?.let {
            if(!isThumbnailViewHolder(nextIndex)) return@let
            (it as ThumbnailViewHolder).setThumbnailSelected(true)
        }
    }

    private fun isThumbnailViewHolder(index: Int) : Boolean{
        return binding?.rvThumbnail?.adapter?.getItemViewType(index) == DrawerSelectionAdapter.ITEM_TYPE
    }

    fun scrollTo(index: Int) {
        binding?.rvThumbnail?.scrollToPosition(index)
    }

    interface Listener {
        fun onDrawerItemClicked(media: MediaUiModel)
        fun onDrawerDataSetChanged(action: DrawerActionType)
    }

    companion object {
        private const val DEFAULT_MAX_VIDEO = 1
        private const val DEFAULT_DRAGGABLE_MODE = false
    }

}
