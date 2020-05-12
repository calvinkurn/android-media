package com.tokopedia.topchat.chatroom.view.custom

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.StickerGroup
import com.tokopedia.topchat.chatroom.view.adapter.StickerFragmentStateAdapter

class ChatMenuStickerView : LinearLayout {

    var listener: StickerMenuListener? = null

    private var stickerViewPager: ViewPager2? = null
    private var stickerViewPagerAdapter: StickerFragmentStateAdapter? = null

    interface StickerMenuListener {
        fun onStickerOpened()
        fun onStickerClosed()
        fun getFragmentActivity(): FragmentActivity?
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        initViewLayout()
        initViewBind()
    }

    private fun initViewLayout() {
        View.inflate(context, LAYOUT, this)
    }

    private fun initViewBind() {
        stickerViewPager = findViewById(R.id.vp_sticker_menu)
    }

    fun updateStickers(stickers: List<StickerGroup>, isExpired: Boolean) {
        initStickerViewPager()
        stickerViewPagerAdapter?.updateStickers(stickers, isExpired)
    }

    private fun initStickerViewPager() {
        if (stickerViewPagerAdapter != null) return
        listener?.getFragmentActivity()?.let {
            stickerViewPagerAdapter = StickerFragmentStateAdapter(it)
            stickerViewPager?.adapter = stickerViewPagerAdapter
        }
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (isVisible) {
            listener?.onStickerOpened()
        } else {
            listener?.onStickerClosed()
        }
    }

    companion object {
        private val LAYOUT = R.layout.partial_chat_menu_sticker_view
    }

}