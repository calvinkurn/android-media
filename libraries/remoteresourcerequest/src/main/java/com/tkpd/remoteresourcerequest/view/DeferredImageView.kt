package com.tkpd.remoteresourcerequest.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.tkpd.remoteresourcerequest.R
import com.tkpd.remoteresourcerequest.task.ResourceDownloadManager


/**
 * Initialize this class with mandatory field @param url
 */
class DeferredImageView : ImageView {
    private lateinit var mRemoteFileName: String
    private var mContext: Context

    /**
     * While creating object of this class use this constructor to initialize the url.
     */
    constructor(context: Context, fileName: String) : super(context) {
        mContext = context
        mRemoteFileName = fileName
        init(null, 0)
    }

    constructor(context: Context) : super(context) {
        mContext = context
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mContext = context
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
            context,
            attrs,
            defStyle
    ) {
        mContext = context
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.DeferredImageView, defStyle, 0
        )
        mRemoteFileName = typedArray.getString(
                R.styleable.DeferredImageView_remoteFileName) ?: mRemoteFileName
        typedArray.recycle()
        downloadAndSetResource()
    }

    private fun downloadAndSetResource() {
        ResourceDownloadManager.getManager().startDownload(mRemoteFileName, this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        ResourceDownloadManager.getManager()
                .stopPendingDownload(mRemoteFileName, this)

    }
}

