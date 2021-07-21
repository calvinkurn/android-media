package com.tokopedia.product.info.util

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.view.util.toDateId
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Yehezkiel on 21/10/20
 */
object ProductDetailBottomSheetBuilder {
    private const val PDP_TIME_PUKUL = "pukul"
    private const val SHIPPING_CHOOSE_ADDRESS_TAG = "SHIPPING_CHOOSE_ADDRESS_TAG"

    fun getShopNotesBottomSheet(context: Context, dateValue: String, descValue: String, titleValue: String): BottomSheetUnify {
        val bottomSheetUnify = BottomSheetUnify()
        val view = View.inflate(context, R.layout.bs_product_shop_notes_info, null)

        bottomSheetUnify.apply {
            setTitle(titleValue)
            setChild(view)
            val date = view.findViewById<Typography>(R.id.product_shop_notes_date)
            val desc = view.findViewById<Typography>(R.id.product_shop_notes_desc)

            date.text = dateValue toDateId ("dd MMM yyyy, '${PDP_TIME_PUKUL}' HH:mm")
            desc.text = HtmlLinkHelper(context, descValue.replace("(\r\n|\n)".toRegex(), "<br />")).spannedString
        }

        return bottomSheetUnify
    }

    fun getUspBottomSheet(context: Context, freeOngkirUrl: String, uspTokoCabangImgUrl: String): BottomSheetUnify {
        val bottomSheetUnify = BottomSheetUnify()
        val view = View.inflate(context, R.layout.bs_product_usp, null)

        bottomSheetUnify.apply {
            isDragable = true
            isHideable = true
            isSkipCollapseState = true
            setTitle(context.getString(R.string.pdp_usp_static_title))
            setChild(view)
            val imgFreeOngkir = view.findViewById<ImageView>(R.id.pdp_usp_static_free_ongkir)
            val imgUsp = view.findViewById<ImageView>(R.id.usp_pdp_img)

            imgUsp.loadImage(uspTokoCabangImgUrl)
            imgFreeOngkir.loadImage(freeOngkirUrl)
        }

        return bottomSheetUnify
    }

    fun getUspTokoNowBottomSheet(context: Context): BottomSheetUnify {
        val bottomSheetUnify = BottomSheetUnify()
        val view = View.inflate(context, R.layout.bs_product_usp_tokonow, null)
        val wordingDynamic = FirebaseRemoteConfigImpl(context).getString(RemoteConfigKey.REMOTE_CONFIG_APP_WORDING_TOKONOW_USP_PDP) ?: ""
        bottomSheetUnify.apply {
            isDragable = true
            isHideable = true
            isSkipCollapseState = true
            setTitle(context.getString(R.string.pdp_usp_tokonow_static_title))
            setChild(view)

            val subtitle3 = view.findViewById<Typography>(R.id.usp_illustration_3_subtitle)
            subtitle3.text = if (wordingDynamic.isNotEmpty()) HtmlLinkHelper(context, wordingDynamic).spannedString else context.getString(R.string.pdp_usp_tokonow_static_illustration_3_subtitle)
        }

        return bottomSheetUnify
    }

    fun openChooseAddressBottomSheet(listener: ChooseAddressBottomSheet.ChooseAddressBottomSheetListener, childFragmentManager: FragmentManager) {
        val chooseAddressBottomSheet = ChooseAddressBottomSheet()
        chooseAddressBottomSheet.setListener(listener)
        chooseAddressBottomSheet.show(childFragmentManager, SHIPPING_CHOOSE_ADDRESS_TAG)
    }
}