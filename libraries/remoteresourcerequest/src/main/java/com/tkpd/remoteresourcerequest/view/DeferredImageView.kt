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
import com.tkpd.remoteresourcerequest.utils.Constants.REQUIRE_COMPLETE_URL_ERROR_MSG
import com.tkpd.remoteresourcerequest.utils.Constants.URL_SEPARATOR


/**
 * Use this class in place of ImageView when you want to download images from remote
 */
class DeferredImageView : AppCompatImageView {
    var mRemoteFileName: String = ""
    var dpiSupportType = ImageDensityType.SUPPORT_MULTIPLE_DPI
    var mCompleteUrl = ""

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
        mRemoteFileName = typedArray.getString(R.styleable.DeferredImageView_remoteFileName
        ) ?: mRemoteFileName
        dpiSupportType =
                typedArray.getInt(R.styleable.DeferredImageView_imageDpiSupportType, dpiSupportType)
        mCompleteUrl =
                typedArray.getString(R.styleable.DeferredImageView_completeUrl) ?: mCompleteUrl
        typedArray.recycle()
        downloadAndSetResource()
    }

    private fun downloadAndSetResource() {
        task?.let { it.deferredImageView?.clear() }
        /* start with originally no image otherwise old dimensions will be picked for all decoding.
        Also useful in case image is loaded dynamically based on conditions. So each images will be
        decoded based on their actual requested dimens and not have any impact on other's decoding.
         */
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
     * Use this method when you need to set a remote image in this View from the Java code. Useful
     * in the case when we need to set image based on conditions. E.g. in no network condition, or
     * if a condition satisfies then display one image and if condition fails then another image.
     * @param name denotes name of the file to be downloaded and set to this ImageView.
     * **/
    fun loadRemoteImageDrawable(name: String, completeUrl: String = "") {
        mCompleteUrl = completeUrl
        mRemoteFileName = if (name.isEmpty()) {
            require(completeUrl.isNotEmpty() &&
                    completeUrl.lastIndexOf(URL_SEPARATOR) != -1) {
                REQUIRE_COMPLETE_URL_ERROR_MSG
            }

            completeUrl.substring(completeUrl.lastIndexOf(URL_SEPARATOR) + 1)
        } else name
        downloadAndSetResource()
    }

    /**
     * This method gives more power to user to control this [DeferredImageView]. Similar to above
     * method in some extant; only difference is user can control which image type can this view
     * holds based on condition.
     * @sample **********************************************************************************
     *     if(isNetworkConnected)
     *         iv.loadRemoteImageDrawable("abc.png", ImageDensityType.SUPPORT_MULTIPLE_DPI)
     *     else
     *         iv.loadRemoteImageDrawable("other_image.png", ImageDensityType.SUPPORT_SINGLE_DPI)
     * ******************************************************************************************
     * By default the density type will be ImageDensityType.SUPPORT_MULTIPLE_DPI
     * **/
    fun loadRemoteImageDrawable(name: String, dpiSupportType: Int) {
        mRemoteFileName = name
        this.dpiSupportType = dpiSupportType
        downloadAndSetResource()
    }

    /**
     * Use this method when you need to set an image drawable from local drawable resource folder.
     * e.g. when we want to show an error image from local if network is not available else show
     * downloaded image.
     */
    fun loadImageDrawable(@DrawableRes drawableId: Int) {
        task?.let { it.deferredImageView?.clear() }
        setImageDrawable(null)
        setImageDrawable(getDrawable(drawableId))
    }

    private fun getDrawable(resId: Int): Drawable? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
            context.resources.getDrawable(resId, context.applicationContext.theme)
        else
            AppCompatResources.getDrawable(context, resId)
    }
}

