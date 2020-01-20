package com.tokopedia.searchbar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.searchbar.helper.ViewHelper
import kotlinx.android.synthetic.main.home_main_toolbar.view.*


class FeedMainToolbar : MainToolbar {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun init(context: Context, attrs: AttributeSet?) {
        super.init(context, attrs)
        initImageSearch()
        initToolbarIcon()
    }

    private fun initImageSearch() {
        val imageViewImageSearch = findViewById<ImageView>(R.id.btn_search)

        imageViewImageSearch.setOnClickListener {
            RouteManager.route(context, ApplinkConstInternalDiscovery.AUTOCOMPLETE)
        }

    }

    private fun initToolbarIcon() {
        (findViewById<ImageView>(R.id.btn_wishlist)).visibility = View.GONE
    }

    override fun inflateResource(context: Context) {
        View.inflate(context, R.layout.feed_main_toolbar, this)
    }

    companion object {
        private const val FEED_SOURCE = "feed"
    }
}
