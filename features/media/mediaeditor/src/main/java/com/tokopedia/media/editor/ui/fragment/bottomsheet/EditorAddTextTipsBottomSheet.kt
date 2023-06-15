package com.tokopedia.media.editor.ui.fragment.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.media.editor.databinding.AddTextTipsBottomsheetBinding
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.media.editor.R as editorR

class EditorAddTextTipsBottomSheet: BottomSheetUnify() {
    private var carouselIndex = 0

    private var viewBinding: AddTextTipsBottomsheetBinding? = null

    private val btnTextCollection = mutableListOf<String>()
    private var btmSheetTitle = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = AddTextTipsBottomsheetBinding.inflate(LayoutInflater.from(inflater.context))
        setChild(viewBinding?.root)

        setCarousel()
        setBtnListener()

        getBottomSheetText(inflater.context)
        setTitle(btmSheetTitle)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun updateCarouselIndex(newValue: Int, isCarouselUpdate: Boolean) {
        if (newValue > btnTextCollection.size - 1) {
            dismiss()
            return
        }

        carouselIndex = newValue

        updateBtnAndCarousel(isCarouselUpdate)
    }

    private fun setCarousel() {
        var index = 0

        viewBinding?.tipsTextCarousel?.apply {
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
                            getString(editorR.string.add_text_bottom_sheet_ribbon)
                        } else {
                            getString(editorR.string.add_text_bottom_sheet_free)
                        }
                    )
                } catch (_: Exception) {}

                index++
            }

            onActiveIndexChangedListener = object: CarouselUnify.OnActiveIndexChangedListener {
                override fun onActiveIndexChanged(prev: Int, current: Int) {
                    updateCarouselIndex(current, false)
                }
            }
        }
    }

    private fun setBtnListener() {

        viewBinding?.btnNext?.setOnClickListener {
            updateCarouselIndex(carouselIndex + 1, true)
        }
    }

    private fun updateBtnAndCarousel(isCarouselUpdate: Boolean) {
        viewBinding?.btnNext?.text = btnTextCollection[carouselIndex]

        if (isCarouselUpdate) {
            viewBinding?.tipsTextCarousel?.activeIndex = carouselIndex
        }
    }

    private fun getBottomSheetText(context: Context) {
        btnTextCollection.apply {
            add(getString(editorR.string.add_text_tips_cta_next))
            add(getString(editorR.string.add_text_tips_cta_done))
        }

        btmSheetTitle = getString(editorR.string.add_text_tips_title)
    }
}
