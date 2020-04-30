package com.tokopedia.seller_migration_common.presentation.widget

import android.os.Bundle
import android.view.View
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.seller_migration_common.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import kotlinx.android.synthetic.main.partial_seller_migration_footer.*
import kotlinx.android.synthetic.main.widget_seller_migration_bottom_sheet.*

abstract class SellerMigrationBottomSheet(private val sellerMigrationBottomSheetListener: SellerMigrationBottomSheetListener) : BottomSheetUnify() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpButtons()
        setupText()
        setupImages()
        super.onViewCreated(view, savedInstanceState)
    }

    open var titles: List<String> = emptyList()
    open var contents: List<String> = emptyList()
    open var images: ArrayList<String> = arrayListOf()

    private fun setUpButtons() {
        sellerMigrationBottomSheetButton.setOnClickListener {
            sellerMigrationBottomSheetListener.onClickGoToSellerApp()
        }
        sellerMigrationBottomSheetLink.text = context?.let { HtmlLinkHelper(it, getString(R.string.seller_migration_bottom_sheet_footer)).spannedString }
        sellerMigrationBottomSheetLink.setOnClickListener {
            sellerMigrationBottomSheetListener.onClickBottomSheetFooter()
        }
    }

    private fun setupText() {
        if(contents.isNotEmpty() && titles.isNotEmpty()) {
            setContentText(contents.first())
            setTitleText(titles.first())
        }
    }

    private fun setupImages() {
        if(images.isEmpty()) {
            return
        }
        if(images.size == 1) {
            setupImageView()
            return
        }
        setupCarousel()

    }

    private fun setupImageView() {
        sellerMigrationBottomSheetImage.apply {
            loadImage(images.first())
            visibility = View.VISIBLE
        }
    }

    private fun setupCarousel() {
        sellerMigrationCarousel.apply {
            addImages(images)
            onActiveIndexChangedListener = object : CarouselUnify.OnActiveIndexChangedListener {
                override fun onActiveIndexChanged(prev: Int, current: Int) {
                    setTitleText(titles[current])
                    setContentText(contents[current])
                }
            }
            visibility = View.VISIBLE
        }
        sellerMigrationBottomSheetImage.visibility = View.GONE
    }

    private fun setContentText(text: String) {
        sellerMigrationBottomSheetContent.text = text
    }

    private fun setTitleText(text: String) {
        sellerMigrationBottomSheetTitle.text = text
    }
}