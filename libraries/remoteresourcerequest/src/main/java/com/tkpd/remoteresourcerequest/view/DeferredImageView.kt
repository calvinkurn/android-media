package com.tkpd.remoteresourcerequest.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import com.tkpd.remoteresourcerequest.R
import com.tkpd.remoteresourcerequest.task.DeferredResourceTask
import com.tkpd.remoteresourcerequest.task.ResourceDownloadManager
import com.tkpd.remoteresourcerequest.type.ImageTypeMapper
import com.tkpd.remoteresourcerequest.type.RequestedResourceType


/**
 * Use this class in place of ImageView when you want to download images from remote
 */
class DeferredImageView : AppCompatImageView {
    var mRemoteFileName: String = ""
    var dpiSupportType = 0

    private var task: DeferredResourceTask? = null

    /**
     * Use this constructor to correctly initialize the object of this class.
     * @param context context of the image view
     * @param fileName name of the remote file to be downloaded
     */
    constructor(context: Context, fileName: String) : super(context) {
        mRemoteFileName = fileName
        init(null, 0)
    }

    constructor(context: Context, fileName: String, dpiSupportType: Int) : super(context) {
        mRemoteFileName = fileName
        this.dpiSupportType = dpiSupportType
        init(null, 0)
    }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
            context,
            attrs,
            defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.DeferredImageView, defStyle, 0
        )
        mRemoteFileName = typedArray.getString(
                R.styleable.DeferredImageView_remoteFileName
        ) ?: mRemoteFileName
        dpiSupportType =
                typedArray.getInt(R.styleable.DeferredImageView_imageDpiSupportType, 0)
        typedArray.recycle()
        downloadAndSetResource()
    }

    private fun downloadAndSetResource() {
        check(mRemoteFileName.isNotEmpty()) {
            context.getString(R.string.exception_file_name_not_found)
        }
        task?.let { it.deferredImageView?.clear() }
        //start with originally no image
        setImageDrawable(null)
        task = ResourceDownloadManager.getManager()
                .startDownload(getDensitySupportType(), null)

    }

    private fun getDensitySupportType(): RequestedResourceType {
        return ImageTypeMapper.getImageType(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        ResourceDownloadManager.getManager()
                .stopDeferredImageViewRendering(task)

    }

    /**
     * Use this method when you need to set a remote image
     * in this View while accessing this object somewhere from
     * the Java code
     * @param name denotes name of the file to be downloaded and set to this ImageView.
     * **/

    fun loadRemoteImageDrawable(name: String) {
        mRemoteFileName = name
        downloadAndSetResource()
    }

    /**
     * Use this method when you need to set an image drawable
     * from local drawable resource folder
     * **/
    fun loadImageDrawable(@DrawableRes drawableId: Int) {
        task?.let { it.deferredImageView?.clear() }
        setImageDrawable(getDrawable(drawableId))
    }

    private fun getDrawable(resId: Int): Drawable? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
            context.resources.getDrawable(resId, context.applicationContext.theme)
        else
            AppCompatResources.getDrawable(context, resId)
    }
}

