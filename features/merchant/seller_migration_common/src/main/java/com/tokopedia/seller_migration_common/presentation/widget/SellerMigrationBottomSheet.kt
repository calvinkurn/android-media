package com.tokopedia.seller_migration_common.presentation.widget

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants
import com.tokopedia.seller_migration_common.getSellerMigrationDate
import com.tokopedia.seller_migration_common.presentation.util.touchlistener.SellerMigrationTouchListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.widget_seller_migration_bottom_sheet.*

abstract class SellerMigrationBottomSheet(private val titles: List<String> = emptyList(),
                                          private val contents: List<String> = emptyList(),
                                          private val images: ArrayList<String> = arrayListOf()) : BottomSheetUnify() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPadding()
        setUpButtons()
        setupText()
        setupImages()
        setupWarningCard()
    }

    private fun setupPadding() {
        setShowListener {
            val headerMargin = 16.toPx()
            bottomSheetWrapper.setPadding(0,0,0,0)
            (bottomSheetHeader.layoutParams as LinearLayout.LayoutParams).setMargins(headerMargin,headerMargin,headerMargin,headerMargin)
        }
    }

    private fun setUpButtons() {
        val sellerMigrationBottomSheetButton: UnifyButton? = view?.findViewById(R.id.sellerMigrationBottomSheetButton)
        sellerMigrationBottomSheetButton?.setOnClickListener {
            goToSellerApp()
        }
        val sellerMigrationBottomSheetLink: Typography? = view?.findViewById(R.id.sellerMigrationBottomSheetLink)
        sellerMigrationBottomSheetLink?.text = context?.let { HtmlLinkHelper(it, getString(R.string.seller_migration_bottom_sheet_footer)).spannedString }
        sellerMigrationBottomSheetLink?.setOnTouchListener(SellerMigrationTouchListener {
            goToInformationWebview(it)
        })
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

    private fun setupWarningCard() {
        val remoteConfigDate = getSellerMigrationDate(context)
        if(remoteConfigDate.isNotBlank()) {
            val sellerMigrationWarningDate: Typography? = view?.findViewById(R.id.sellerMigrationWarningDate)
            sellerMigrationWarningCard.show()
            sellerMigrationWarningDate?.text = remoteConfigDate
        }
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
        with(SellerMigrationConstants) {
            try {
                val intent = context?.packageManager?.getLaunchIntentForPackage(PACKAGE_SELLER_APP)
                if(intent != null) {
                    intent.putExtra(SELLER_MIGRATION_KEY_AUTO_LOGIN, true)
                    activity?.startActivity(intent)
                } else {
                    activity?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(APPLINK_PLAYSTORE + PACKAGE_SELLER_APP)))
                }
            } catch (anfe: ActivityNotFoundException) {
                activity?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(URL_PLAYSTORE + PACKAGE_SELLER_APP)))
            }
        }
    }

    private fun goToInformationWebview(link: String) : Boolean {
        return RouteManager.route(activity, "${ApplinkConst.WEBVIEW}?url=${link}")
    }
}