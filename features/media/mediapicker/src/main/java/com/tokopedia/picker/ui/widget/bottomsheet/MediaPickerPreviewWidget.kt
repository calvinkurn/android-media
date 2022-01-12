package com.tokopedia.picker.ui.widget.bottomsheet

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tokopedia.imagepicker.media_picker.adapter.MediaPickerThumbnailAdapter
import com.tokopedia.picker.R
import com.tokopedia.picker.data.entity.Media
import com.tokopedia.picker.databinding.MediaPickerPlaceholderThumbnailItemBinding
import com.tokopedia.picker.databinding.WidgetMediaPickerThumbnailListBinding
import com.tokopedia.picker.utils.ActionType
import com.tokopedia.picker.utils.EventBusFactory
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import java.lang.Exception

class MediaPickerPreviewWidget : FrameLayout {
    private var mediaPickerThumbnailAdapter: MediaPickerThumbnailAdapter? = null
    private var backgroundColorPlaceHolder: Int? = null
    private var canReorder: Boolean = false
    private var maxVideo: Int = 1
    private var placeholderPreview: Int? = null
    private lateinit var binding: WidgetMediaPickerThumbnailListBinding

    interface MediaPickerPreviewWidgetListener {
        fun onDataSetChanged(action: ActionType)
    }

    constructor (context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes){
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        binding = WidgetMediaPickerThumbnailListBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)
        mediaPickerThumbnailAdapter = MediaPickerThumbnailAdapter(
            context, arrayListOf()
        )
        if (attrs != null) {
            setAttribute(attrs)
        }
        binding.rvThumbnail.layoutManager =
            LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvThumbnail.setHasFixedSize(true)
        binding.rvThumbnail.adapter = mediaPickerThumbnailAdapter
        val animator = binding.rvThumbnail.itemAnimator
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }
    }

    private fun setAttribute(attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.MediaPickerPreviewWidget, 0 , 0)
        val defaultColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        backgroundColorPlaceHolder = typedArray.getColor(R.styleable.MediaPickerPreviewWidget_backgroundColorPlaceHolder, defaultColor)
        canReorder = typedArray.getBoolean(R.styleable.MediaPickerPreviewWidget_canReorder, false)
        maxVideo = typedArray.getInteger(R.styleable.MediaPickerPreviewWidget_maxVideo, 1)
        placeholderPreview = typedArray.getResourceId(R.styleable.MediaPickerPreviewWidget_placeholder, R.drawable.placeholder_media_preview)
        mediaPickerThumbnailAdapter?.backgroundColorPlaceHolder = backgroundColorPlaceHolder!!
        mediaPickerThumbnailAdapter?.placeholderPreview = placeholderPreview!!
        mediaPickerThumbnailAdapter?.canReorder = canReorder
    }

    fun setCanReorder(canReorder: Boolean) {
        mediaPickerThumbnailAdapter!!.canReorder = canReorder
    }

    fun setData(medias: List<Media>) {
        mediaPickerThumbnailAdapter!!.setData(medias.toMutableList(),)
    }

    fun setPlaceholderPreview(@DrawableRes drawable: Int) {
        mediaPickerThumbnailAdapter?.setPlaceholder(drawable)

    }

    fun addData(media: Media) {
        mediaPickerThumbnailAdapter!!.addData(media)
        binding.rvThumbnail.postDelayed({
            val position = mediaPickerThumbnailAdapter!!.getMediaPathList.size
            binding.rvThumbnail.smoothScrollToPosition(
                if (position >= mediaPickerThumbnailAdapter!!.itemCount) position - 1 else position
            )
        }, 1)
    }

    fun setMaxAdapterSize(size: Int) {
        mediaPickerThumbnailAdapter!!.setMaxData(size)
    }

    fun setMaxVideo(max: Int) {
        maxVideo = max
        mediaPickerThumbnailAdapter?.maxVideo = max
    }

    fun setListener(listener: MediaPickerPreviewWidgetListener) {
       mediaPickerThumbnailAdapter?.setOnDataChangedListener(listener)
    }

    fun removeListener() {
        mediaPickerThumbnailAdapter?.removeListener()
    }
}