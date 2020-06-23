package com.tokopedia.seller_migration_common.presentation.widget

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.view.View
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.APPLINK_PLAYSTORE
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.PACKAGE_SELLER_APP
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.SELLER_MIGRATION_INFORMATION_LINK
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.URL_PLAYSTORE
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import kotlinx.android.synthetic.main.partial_seller_migration_footer.*
import kotlinx.android.synthetic.main.widget_seller_migration_bottom_sheet.*

abstract class SellerMigrationBottomSheet(val titles: List<String> = emptyList(),
        val contents: List<String> = emptyList(),
        val images: ArrayList<String> = arrayListOf()) : BottomSheetUnify() {

    companion object {
        const val KEY_AUTO_LOGIN = "is_auto_login"
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpButtons()
        setupText()
        setupImages()
        super.onViewCreated(view, savedInstanceState)
    }



    private fun setUpButtons() {
        sellerMigrationBottomSheetButton.setOnClickListener {
            goToSellerApp()
        }
        sellerMigrationBottomSheetLink.text = context?.let { HtmlLinkHelper(it, getString(R.string.seller_migration_bottom_sheet_footer)).spannedString }
        sellerMigrationBottomSheetLink.setOnClickListener {
            goToInformationWebview()
        }
    }

    private fun setupText() {
        if(contents.isNotEmpty() && titles.isNotEmpty()) {
            context?.let {
                setContentText(HtmlLinkHelper(it, contents.first()).spannedString)
            }
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
                    setContentText(HtmlLinkHelper(context, contents[current]).spannedString)
                }
            }
            visibility = View.VISIBLE
        }
        sellerMigrationBottomSheetImage.visibility = View.GONE
    }

    private fun setContentText(text: CharSequence?) {
        sellerMigrationBottomSheetContent.text = text
    }

    private fun setTitleText(text: String) {
        sellerMigrationBottomSheetTitle.text = text
    }

    private fun goToSellerApp() {
        try {
            val intent = context?.packageManager?.getLaunchIntentForPackage(PACKAGE_SELLER_APP)
            if(intent != null) {
                intent.putExtra(KEY_AUTO_LOGIN, true)
                this.activity?.startActivity(intent)
            } else {
                this.activity?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(APPLINK_PLAYSTORE + PACKAGE_SELLER_APP)))
            }
        } catch (anfe: ActivityNotFoundException) {
            this.activity?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(URL_PLAYSTORE + PACKAGE_SELLER_APP)))
        }
    }

    private fun goToInformationWebview() {
        RouteManager.route(activity, "${ApplinkConst.WEBVIEW}?url=${SELLER_MIGRATION_INFORMATION_LINK}")
    }
}