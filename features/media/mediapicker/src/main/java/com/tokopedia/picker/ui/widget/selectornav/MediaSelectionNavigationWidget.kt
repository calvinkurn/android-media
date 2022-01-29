package com.tokopedia.picker.ui.widget.selectornav

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tokopedia.picker.R
import com.tokopedia.picker.databinding.WidgetSelectionBottomNavBinding
import com.tokopedia.picker.ui.uimodel.MediaUiModel
import com.tokopedia.picker.ui.widget.selectornav.adapter.MediaSelectionAdapter
import com.tokopedia.picker.utils.ActionType
import com.tokopedia.picker.utils.Unify_N0

class MediaSelectionNavigationWidget : FrameLayout {

    private var adapter: MediaSelectionAdapter? = null
    private var placeHolderBackgroundColor: Int? = null
    private var placeholderPreview: Int? = null
    private var canReorder: Boolean = false
    private var maxVideo: Int = 1

    private var binding: WidgetSelectionBottomNavBinding =
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
        addView(binding.root)

        setupRecyclerView()
        widgetProperties()

        binding.rvThumbnail.itemAnimator?.let {
            if (it is SimpleItemAnimator) {
                it.supportsChangeAnimations = false
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = MediaSelectionAdapter(arrayListOf())
        binding.rvThumbnail.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        binding.rvThumbnail.setHasFixedSize(true)
        binding.rvThumbnail.adapter = adapter
    }

    private fun widgetProperties() {
        placeHolderBackgroundColor?.let {
            adapter?.backgroundColorPlaceHolder = it
        }

        placeholderPreview?.let {
            adapter?.placeholderPreview = it
        }

        adapter?.canReorder = canReorder
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

        canReorder = typedArray.getBoolean(
            R.styleable.MediaPickerPreviewWidget_canReorder,
            false
        )

        maxVideo = typedArray.getInteger(
            R.styleable.MediaPickerPreviewWidget_maxVideo,
            1
        )

        placeholderPreview = typedArray.getResourceId(
            R.styleable.MediaPickerPreviewWidget_placeholder,
            R.drawable.picker_ic_placeholder_media_preview
        )
    }

    fun isAbleToReorder(canReorder: Boolean) {
        adapter?.canReorder = canReorder
    }

    fun getData(): List<MediaUiModel>? {
        return adapter?.getData()
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

    fun containsVideoMaxOf(count: Int): Boolean {
        return adapter?.containsVideoMaxOf(count) == true
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

    interface Listener {
        fun onDataSetChanged(action: ActionType)
    }

}