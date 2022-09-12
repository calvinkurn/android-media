package com.tokopedia.product.info.util

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.BsProductShopNotesInfoBinding
import com.tokopedia.product.detail.view.util.toDateId
import com.tokopedia.product.info.model.productdetail.response.ShopNotesData
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoExpandableListDataModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper

/**
 * Created by Yehezkiel on 21/10/20
 */
object ProductDetailBottomSheetBuilder {
    private const val PDP_TIME_PUKUL = "pukul"
    private const val SHIPPING_CHOOSE_ADDRESS_TAG = "SHIPPING_CHOOSE_ADDRESS_TAG"
    private const val DATE_FORMATTER = "dd MMM yyyy, '${PDP_TIME_PUKUL}' HH:mm"
    private val DESC_REPLACE_CHAR_REGEX = "(\r\n|\n)".toRegex()
    private const val DESC_REPLACE_TO = "<br />"

    fun getShopNotesBottomSheet(
        context: Context,
        element: ProductDetailInfoExpandableListDataModel,
        shopNotesData: ShopNotesData
    ): BottomSheetUnify {
        val bottomSheetUnify = BottomSheetUnify()
        val view = View.inflate(context, R.layout.bs_product_shop_notes_info, null)
        val binding = BsProductShopNotesInfoBinding.bind(view)

        bottomSheetUnify.apply {
            setChild(binding.root)
            setTitle(shopNotesData.title)
        }

        binding.apply {
            pdpHeaderImg.loadImage(element.productImage)
            pdpHeaderProductTitle.text = element.productName
            productShopNotesDate.text = shopNotesData.updateTime toDateId DATE_FORMATTER
            productShopNotesDesc.text = HtmlLinkHelper(
                context,
                shopNotesData.content.replace(DESC_REPLACE_CHAR_REGEX, DESC_REPLACE_TO)
            ).spannedString
        }

        return bottomSheetUnify
    }

    fun openChooseAddressBottomSheet(listener: ChooseAddressBottomSheet.ChooseAddressBottomSheetListener, childFragmentManager: FragmentManager) {
        val chooseAddressBottomSheet = ChooseAddressBottomSheet()
        chooseAddressBottomSheet.setListener(listener)
        chooseAddressBottomSheet.show(childFragmentManager, SHIPPING_CHOOSE_ADDRESS_TAG)
    }
}