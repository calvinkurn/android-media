package com.tokopedia.media.editor.ui.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.media.editor.R as editorR

class EditorAddTextTipsBottomSheet: BottomSheetUnify() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setTitle(TIPS_TITLE)
        inflater.inflate(editorR.layout.add_text_tips_bottomsheet, null)?.apply {
            setChild(this)
            setCarousel(
                findViewById(editorR.id.tips_text_carousel)
            )
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setCarousel(carousel: CarouselUnify) {
        var index = 0

        carousel.apply {
            slideToShow = 1f
            indicatorPosition = CarouselUnify.INDICATOR_BC
            freeMode = false
            centerMode = false
            autoplay = false
            addItems(
                editorR.layout.add_text_tips_carousel_item,
                arrayListOf(
                    context.getString(editorR.string.add_text_tips_carousel_item_1),
                    context.getString(editorR.string.add_text_tips_carousel_item_2),
                )
            ) { view, text ->
                try {
                    (text as String).apply {
                        view.findViewById<Typography>(editorR.id.carousel_item_text).text = this
                    }

                    // temporary image, will be replace later
                    view.findViewById<AppCompatImageView>(editorR.id.carousel_item_img).loadImage(
                        if (index == 0) {
                            BOTTOM_SHEET_RIBBON_TEXT
                        } else {
                            BOTTOM_SHEET_FREE_TEXT
                        }
                    )
                } catch (_: Exception) {}

                index++
            }
        }
    }

    companion object{
        private const val TIPS_TITLE = "Tips tambah teks"
        private const val BOTTOM_SHEET_FREE_TEXT = "https://images.tokopedia.net/img/FYkQxT/2023/3/29/867a9134-b77a-4fca-b2e1-2e8fcc86c4fa.png"
        private const val BOTTOM_SHEET_RIBBON_TEXT = "https://images.tokopedia.net/img/FYkQxT/2023/3/29/b1ee7176-286f-489c-ba3b-68de920462e9.png"
    }
}
