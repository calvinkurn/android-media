package com.tokopedia.media.editor.ui.component

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.ui.adapter.EditorViewPagerAdapter
import com.tokopedia.media.editor.ui.uimodel.EditorUiModel
import com.tokopedia.media.loader.loadImage

class EditorViewPager(context: Context, attrSet: AttributeSet): ViewPager(context, attrSet){
    private var editorAdapter: EditorViewPagerAdapter? = null
    private var previousVideoIndex = -1
    private var listRef = listOf<EditorUiModel>()
    private var callback: (position: Int, isVideo: Boolean) -> Unit = {_, _ ->}

    fun setAdapter(listData: List<EditorUiModel>){
        editorAdapter = EditorViewPagerAdapter(context, listData)
        adapter = editorAdapter

        listRef = listData

        addOnPageChangeListener(object: OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                if(previousVideoIndex != -1) stopVideoPlayer(previousVideoIndex)

                val isVideo = listRef[position].isVideo
                if(isVideo){
                    playVideoPlayer(position)
                    previousVideoIndex = position
                }

                callback(position, isVideo)
            }
        })
    }

    fun playVideoPlayer(index: Int){
        editorAdapter?.playVideo(index)
    }

    fun stopVideoPlayer(index: Int){
        editorAdapter?.stopVideo(index)
    }

    fun updateImage(index: Int, onImageUpdated: () -> Unit = {}){
        val view = getChildAt(index)?.findViewById<ImageView>(R.id.img_main_preview)
        view?.loadImage(listRef[index].getImageUrl()){
            listener(
                onSuccess = {_, _ ->
                    view.post {
                        onImageUpdated()
                    }
                }
            )
        }
    }

    fun setOnPageChanged(callback: (position: Int, isVideo: Boolean) -> Unit){
        this.callback = callback
    }
}