package com.tokopedia.home_wishlist.view.custom

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.home_wishlist.R
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
import java.util.concurrent.TimeUnit

class CustomSearchView : FrameLayout {
    private fun getLayout(): Int {
        return R.layout.custom_search_view
    }

    protected var view: View? = null

    interface Listener {
        fun onManageDeleteWishlistClicked()
        fun onCancelDeleteWishlistClicked()
    }

    var textManage: Typography? = null
    private var searchDrawable: Drawable? = null
    private var searchText: String? = null
    private var searchHint: String? = null
    private var delayTextChanged: Long = 0
    private var listener: Listener? = null
    private var wishlistTvCount: Typography? = null
    private var wishlistTvCountLoader: LoaderUnify? = null

    fun setListener(listener: Listener?) {
        this.listener = listener
    }

    fun setTextManageWording(text: String) {
        if (text.isNotBlank()) {
            textManage?.text = text
        }
    }

    fun setWishlistCount(count: Int) {
        if (count > 0) {
            wishlistTvCount?.text = String.format(
                context.getString(R.string.label_wishlist_count),
                count.toString()
            )
            wishlistTvCountLoader?.gone()
            wishlistTvCount?.visible()
        }
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet) {
        val styledAttributes: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.HomeWishlistSearchInputView)
        try {
            searchDrawable = styledAttributes.getDrawable(R.styleable.HomeWishlistSearchInputView_siv_search_icon)
            searchText = styledAttributes.getString(R.styleable.HomeWishlistSearchInputView_siv_search_text)
            searchHint = styledAttributes.getString(R.styleable.HomeWishlistSearchInputView_siv_search_hint)
        } finally {
            styledAttributes.recycle()
        }
        init()
    }

    private fun init() {
        view = View.inflate(context, getLayout(), this)
        wishlistTvCount = view?.findViewById(R.id.wishlist_tv_count)
        wishlistTvCountLoader = view?.findViewById(R.id.wishlist_tv_count_loader)
        textManage = view?.findViewById(textManageResourceId) as Typography
        delayTextChanged = DEFAULT_DELAY_TEXT_CHANGED
        textManage?.setOnClickListener {
            (it as? Typography)?.text?.let { text ->
                val labelManage = context.resources.getString(R.string.label_delete_wishlist_manage)
                val labelCancel = context.resources.getString(R.string.label_delete_wishlist_cancel)
                if (text == labelManage) {
                    setTextManageWording(labelCancel)
                    listener?.onManageDeleteWishlistClicked()
                } else {
                    setTextManageWording(labelManage)
                    listener?.onCancelDeleteWishlistClicked()
                }
            }
        }
    }

    fun resetLabelState() {
        val labelManage = context.resources.getString(R.string.label_delete_wishlist_manage)
        setTextManageWording(labelManage)
    }

    private val closeImageButtonResourceId: Int
        get() = R.id.image_button_close

    private val textManageResourceId: Int
        get() = R.id.text_manage

    companion object {
        private val DEFAULT_DELAY_TEXT_CHANGED = TimeUnit.SECONDS.toMillis(0)
    }
}